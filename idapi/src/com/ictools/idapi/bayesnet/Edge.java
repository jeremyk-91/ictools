package com.ictools.idapi.bayesnet;

import java.util.List;
import java.util.Map;

public interface Edge {
    public List<Node> getSources();
    public Node getSink();
    public void propagateLambdaEvidence(Node source, List<Double> lambdaEvidence);
    public void propagateLambdaEvidenceSelective(Node source, List<Double> lambdaEvidence, Node blockedParent);
    public void propagatePiEvidence(Node source, List<Double> piEvidence);
}
