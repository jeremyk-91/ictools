package com.ictools.idapi.bayesnet;

import java.util.List;

public class PiMessage {
    private final List<Double> piMessage;
    private final List<Node> source;

    public PiMessage(List<Double> piMessage, List<Node> source) {
        this.piMessage = piMessage;
        this.source = source;
    }

    public List<Double> getPiMessage() {
        return piMessage;
    }

    public List<Node> getSource() {
        return source;
    }
}
