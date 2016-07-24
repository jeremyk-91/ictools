package com.ictools.idapi.bayesnet;

import java.util.List;

public class LambdaMessage {
    private final List<Double> lambdaMessage;

    public LambdaMessage(List<Double> lambdaMessage) {
        this.lambdaMessage = lambdaMessage;
    }

    public List<Double> getLambdaMessage() {
        return lambdaMessage;
    }
}
