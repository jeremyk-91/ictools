package com.ictools.idapi.bayesnet;

import java.util.List;

public interface Edge {
    public void propagateLambdaMessage(List<Double> lambdaEvidence);
    public void propagatePiMessage(List<Double> piEvidence);
}
