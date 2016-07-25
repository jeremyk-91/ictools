package com.ictools.idapi.bayesnet;

import java.util.List;

public interface Node {
    public String getIdentifier();
    public int getDimensionality();
    public List<Double> getPosteriorDistribution();

    public void receiveLambdaMessage(LambdaMessage message);
    public void receivePiMessage(PiMessage message);
    public void instantiate(int value);
    public boolean isInstantiated();
}
