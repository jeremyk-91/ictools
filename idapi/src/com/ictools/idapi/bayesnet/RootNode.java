package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;

import java.util.List;

public class RootNode implements Node {
    private final String identifier;
    private final List<Double> prior;
    private final List<Double> posterior;
    private final List<Node> children;

    public RootNode(String identifier, List<Double> prior, List<Node> children) {
        this.identifier = identifier;
        this.prior = Lists.newArrayList(prior);
        this.posterior = Lists.newArrayList(prior);
        this.children = children;
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
        return posterior;
    }

    @Override
    public void receiveLambdaMessage(LambdaMessage m) {
        List<Double> underlyingMessage = m.getLambdaMessage();
        if (underlyingMessage.size() != getDimensionality()) {
            throw new IllegalArgumentException("Node " + this + " does not expect a lambda message " + m + "; wrong dimensionality");
        }
        // Otherwise, update the lambda evidence
        List<Double> newPosterior = Lists.newArrayList();
        for (int i = 0; i < posterior.size(); i++) {
            newPosterior.add(posterior.get(i) * underlyingMessage.get(i));
        }
        // And then normalize
        List<Double> posterior = VectorUtils.normalize(newPosterior);
        for (int i = 0; i < this.posterior.size(); i++) {
            this.posterior.set(i, posterior.get(i));
        }
    }

    @Override
    public void receivePiMessage(PiMessage m) {
        // Should not be possible
        throw new IllegalStateException("A root node should not receive pi messages, by definition!");
    }

    @Override
    public void instantiate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cannot instantiate the node " + this + " to a negative index!");
        } else if (value >= getDimensionality()) {
            throw new IllegalArgumentException("Value " + value + " is not a recognised value for the node " + this);
        }
        for (int i = 0; i < posterior.size(); i++) {
            posterior.set(i, i == value ? 1.0 : 0.0);
        }
    }

    @Override
    public String toString() {
        return "RootNode{" +
                "identifier='" + identifier + '\'' +
                ", prior=" + prior +
                ", posterior=" + posterior +
                '}';
    }
}
