package com.ictools.idapi.bayesnet;

import java.util.List;

public interface Node {
    int getDimensionality();
    List<Double> getPosteriorDistribution();

    void receiveLambdaMessage(LambdaMessage m);
    void receivePiMessage(PiMessage m);
}
