package com.ictools.idapi.bayesnet;

import java.util.List;

public class RootNode implements Node {
    private final List<Double> prior;

    public RootNode(List<Double> prior) {
        this.prior = Lists.newArrayList();
    }

    @Override
    public int getDimensionality() {
        return dimensionality;
    }

    @Override
    public List<Double> getPosteriorDistribution() {
        return null;
    }

    @Override
    public void receiveLambdaMessage(LambdaMessage m) {

    }

    @Override
    public void receivePiMessage(PiMessage m) {

    }
}
