package com.ictools.idapi.bayesnet;

import java.util.List;

public class LambdaMessage {
    private final List<Double> lambdaMessage;
    private final Node source;

    public LambdaMessage(List<Double> lambdaMessage, Node source) {
        this.lambdaMessage = lambdaMessage;
        this.source = source;
    }

    public List<Double> getLambdaMessage() {
        return lambdaMessage;
    }

    public Node getSource() {
        return source;
    }
}
