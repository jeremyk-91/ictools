package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class RootNode implements Node {
    private final String identifier;
    private final List<Double> prior;
    private final Map<String, List<Double>> lambdaMessages;
    private final List<Edge> childEdges;

    private boolean instantiated = false;
    private int instantiatedValue;

    public RootNode(String identifier, List<Double> prior, List<Edge> childEdges) {
        this.identifier = identifier;
        this.prior = Lists.newArrayList(prior);
        this.lambdaMessages = Maps.newHashMap();
        this.childEdges = childEdges;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getDimensionality() {
        return prior.size();
    }

    @Override
    public List<Double> getPosteriorDistribution() {
        if (instantiated) {
            List<Double> posterior = Lists.newArrayList();
            for (int i = 0; i < prior.size(); i++) {
                posterior.add((i == instantiatedValue) ? 1.0 : 0.0);
            }
            return posterior;
        }
        List<Double> posterior = Lists.newArrayList(prior);
        for (List<Double> lambdaMessage : lambdaMessages.values()) {
            for (int i = 0; i < posterior.size(); i++) {
                posterior.set(i, posterior.get(i) * lambdaMessage.get(i));
            }
        }
        return VectorUtils.normalize(posterior);
    }

    @Override
    public void receiveLambdaMessage(LambdaMessage message) {
        if (instantiated) {
            return;
        }
        if (message.getLambdaMessage().size() != getDimensionality()) {
            throw new IllegalArgumentException("Node " + this + " does not expect a lambda message " + message + "; wrong dimensionality");
        }
        updateLambdaMessageStore(message);
        propagateEvidenceToChildren(message);
    }

    private void updateLambdaMessageStore(LambdaMessage message) {
        // Update the lambda evidence
        lambdaMessages.put(message.getSource().getIdentifier(), message.getLambdaMessage());
    }

    private void propagateEvidenceToChildren(LambdaMessage message) {
        // Now propagate to the other children
        for (Edge childEdge : childEdges) {
            if (childEdge.getSink().equals(message.getSource())) {
                continue;
            }
            List<Double> piEvidence = Lists.newArrayList(getPosteriorDistribution());
            for (int i = 0; i < piEvidence.size(); i++) {
                if (piEvidence.get(i) != 0.0 && lambdaMessages.containsKey(childEdge.getSink().getIdentifier())) {
                    piEvidence.set(i, piEvidence.get(i) / lambdaMessages.get(childEdge.getSink().getIdentifier()).get(i));
                }
            }
            childEdge.propagatePiEvidence(this, piEvidence);
        }
    }

    @Override
    public void receivePiMessage(PiMessage message) {
        // This means initialisation, with the established prior.
        // Note that the message is ignored.
        propagatePiEvidenceToChildren();
    }

    @Override
    public void instantiate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cannot instantiate the node " + this + " to a negative index!");
        } else if (value >= getDimensionality()) {
            throw new IllegalArgumentException("Value " + value + " is not a recognised value for the node " + this);
        }
        instantiated = true;
        instantiatedValue = value;
        propagatePiEvidenceToChildren();
    }

    @Override
    public boolean isInstantiated() {
        return instantiated;
    }

    private void propagatePiEvidenceToChildren() {
        List<Double> posterior = getPosteriorDistribution();

        for (Edge childEdge : childEdges) {
            childEdge.propagatePiEvidence(this, posterior);
        }
    }

    @Override
    public String toString() {
        return "RootNode{" +
                "identifier='" + identifier + '\'' +
                ", prior=" + prior +
                ", lambdaMessages=" + lambdaMessages +
                ", childEdges=" + childEdges +
                ", instantiated=" + instantiated +
                '}';
    }
}
