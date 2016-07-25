package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javafx.util.Pair;

import java.util.*;

public class InternalNode implements Node {
    private final String identifier;
    private final int dimensionality;
    private final Map<String, List<Double>> lambdaMessages;
    private final Map<List<Node>, List<Double>> piMessages;
    private final List<Edge> parentEdges;
    private final List<Edge> childEdges;

    private boolean instantiated = false;
    private int instantiatedValue;

    public InternalNode(String identifier, int dimensionality, List<Edge> parentEdges, List<Edge> childEdges) {
        this.identifier = identifier;
        this.dimensionality = dimensionality;
        lambdaMessages = Maps.newHashMap();
        piMessages = Maps.newHashMap();
        this.parentEdges = parentEdges;
        this.childEdges = childEdges;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getDimensionality() {
        return dimensionality;
    }

    @Override
    public List<Double> getPosteriorDistribution() {
        if (instantiated) {
            List<Double> posterior = Lists.newArrayList();
            for (int i = 0; i < dimensionality; i++) {
                posterior.add((i == instantiatedValue) ? 1.0 : 0.0);
            }
            return posterior;
        }
        List<Double> posterior = Lists.newArrayList(getLambdaEvidence());

        for (List<Double> piMessage : piMessages.values()) {
            for (int i = 0; i < dimensionality; i++) {
                posterior.set(i, posterior.get(i) * piMessage.get(i));
            }
        }
        return VectorUtils.normalize(posterior);
    }

    private List<Double> getLambdaEvidence() {
        List<Double> accumulator = Lists.newArrayList();
        for (int i = 0; i < dimensionality; i++) {
            accumulator.add(1.0);
        }
        for (List<Double> lambdaMessage : lambdaMessages.values()) {
            for (int i = 0; i < dimensionality; i++) {
                accumulator.set(i, accumulator.get(i) * lambdaMessage.get(i));
            }
        }
        return accumulator;
    }

    @Override
    public void receiveLambdaMessage(LambdaMessage message) {
        if (instantiated) {
            return; // Does nothing
        }
        lambdaMessages.put(message.getSource().getIdentifier(), message.getLambdaMessage());
        List<Double> lambdaEvidence = getLambdaEvidence();

        List<Double> piEvidence = Lists.newArrayList(getPosteriorDistribution());
        for (int i = 0; i < piEvidence.size(); i++) {
            if (piEvidence.get(i) != 0.0) {
                piEvidence.set(i, piEvidence.get(i) / message.getLambdaMessage().get(i));
            }
        }

        parentEdges.stream().forEach(edge -> edge.propagateLambdaEvidenceSelective(this, lambdaEvidence, this));
        childEdges.stream()
                .filter(childEdge -> !childEdge.getSink().equals(message.getSource()))
                .forEach(childEdge -> childEdge.propagatePiEvidence(this, piEvidence));
    }

    @Override
    public void receivePiMessage(PiMessage message) {
        piMessages.put(message.getSource(), message.getPiMessage());
        if (!instantiated) {
            // TODO: Recompute the correct pi message for each child (get rid of lambda evidence for each specific child)
            for (Edge childEdge : childEdges) {
                List<Double> piEvidence = Lists.newArrayList(getPosteriorDistribution());
                List<Double> lambdaEvidenceFromChild = lambdaMessages.get(childEdge.getSink().getIdentifier());
                for (int i = 0; i < piEvidence.size(); i++) {
                    if (piEvidence.get(i) != 0.0 && lambdaEvidenceFromChild != null) {
                        piEvidence.set(i, piEvidence.get(i) / lambdaEvidenceFromChild.get(i));
                    }
                }
                childEdge.propagatePiEvidence(this, piEvidence);
            }
        }

        // TODO: If lambda evidence then talk to the other parents
        if (hasLambdaEvidence()) {
            // Talk to the other parents too
            List<Double> lambdaEvidence = getLambdaEvidence();
            parentEdges.stream()
                .forEach(edge -> edge.propagateLambdaEvidenceSelective(this, lambdaEvidence, message.getIndividualSource()));
        }
    }

    @Override
    public boolean hasLambdaEvidence() {
        return !lambdaMessages.isEmpty();
    }

    private boolean checkInstantiationSafety() {
        if (instantiated) {
            return false;
        }

        // O(n). We need to keep the ordering though
        List<Node> active = Lists.newArrayList();
        List<Node> visited = Lists.newArrayList();

        try {
            checkInstantiationSafety(this, active, visited, null, null);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void checkInstantiationSafety(Node node, List<Node> active, List<Node> visited, MessageType messageType, Node messageSource) {
        if (active.contains(node)) {
            throw new IllegalStateException("Loop!");
        }
        if (!visited.contains(node)) {
            active.add(node);
            visited.add(node);
            if (messageType == null) {
                // Send to all
                for (Edge edge : node.getParentEdges()) {
                    for (Node parent : edge.getSources()) {
                        checkInstantiationSafety(parent, active, visited, MessageType.LAMBDA, node);
                    }
                }
                for (Edge edge : node.getChildEdges()) {
                    checkInstantiationSafety(edge.getSink(), active, visited, MessageType.PI, node);
                }
            } else if (messageType == MessageType.LAMBDA) {
                if (!node.isInstantiated()) {
                    // Pi to ALL other children
                    // Lambda to ALL parents
                    for (Edge edge : node.getParentEdges()) {
                        for (Node parent : edge.getSources()) {
                            checkInstantiationSafety(parent, active, visited, MessageType.LAMBDA, node);
                        }
                    }
                    for (Edge edge : node.getChildEdges()) {
                        if (!edge.getSink().equals(messageSource)) {
                            checkInstantiationSafety(edge.getSink(), active, visited, MessageType.PI, node);
                        }
                    }
                }
            } else {
                // PI message
                if (!node.isInstantiated()) {
                    // Pi to ALL children
                    for (Edge edge : node.getChildEdges()) {
                        checkInstantiationSafety(edge.getSink(), active, visited, MessageType.PI, node);
                    }
                }
                if (node.hasLambdaEvidence()) {
                    // Lambda to other parents
                    for (Edge edge : node.getParentEdges()) {
                        for (Node parent : edge.getSources()) {
                            if (!parent.equals(messageSource)) {
                                checkInstantiationSafety(parent, active, visited, MessageType.LAMBDA, node);
                            }
                        }
                    }
                }
            }
            active.remove(node);
        }
    }

    private static enum MessageType {
        LAMBDA, PI
    }

    @Override
    public void instantiate(int value) {
        if (instantiated) {
            throw new IllegalStateException("Cannot instantiate a node twice.");
        }
        if (!checkInstantiationSafety()) {
            throw new IllegalStateException("It's not safe to instantiate this node at this time, because it will cause endless propagation.");
        }
        instantiated = true;
        instantiatedValue = value;

        List<Double> posterior = getPosteriorDistribution(); // This is both Lambda and Pi, because of the pearl equations
        lambdaMessages.put(identifier, posterior);
        parentEdges.stream().forEach(edge -> edge.propagateLambdaEvidence(this, posterior));
        childEdges.stream().forEach(edge -> edge.propagatePiEvidence(this, posterior));
    }

    @Override
    public boolean isInstantiated() {
        return instantiated;
    }

    @Override
    public List<Edge> getParentEdges() {
        return parentEdges;
    }

    @Override
    public List<Edge> getChildEdges() {
        return childEdges;
    }

    @Override
    public String toString() {
        return "InternalNode{" +
                "identifier='" + identifier + '\'' +
                ", dimensionality=" + dimensionality +
                ", lambdaMessages=" + lambdaMessages +
                ", piMessages=" + piMessages +
                ", parentEdges=" + parentEdges +
                ", childEdges=" + childEdges +
                ", instantiated=" + instantiated +
                ", instantiatedValue=" + instantiatedValue +
                '}';
    }
}
