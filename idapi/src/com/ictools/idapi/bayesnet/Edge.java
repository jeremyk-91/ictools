package com.ictools.idapi.bayesnet;

import java.util.List;

public interface Edge {
    public Node getSource();
    public Node getSink();
    public void propagateLambdaEvidence(List<Double> lambdaEvidence);
    public void propagatePiEvidence(List<Double> piEvidence);
}
