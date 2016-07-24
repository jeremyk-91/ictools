package com.ictools.idapi.bayesnet;

import java.util.List;

public interface Node {
    public String getIdentifier();
    public int getDimensionality();
    public List<Double> getPosteriorDistribution();

    public void receiveLambdaMessage(LambdaMessage m);
    public void receivePiMessage(PiMessage m);
    public void instantiate(int value);
}
