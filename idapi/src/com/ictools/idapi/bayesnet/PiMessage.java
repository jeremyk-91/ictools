package com.ictools.idapi.bayesnet;

import java.util.List;

public class PiMessage {
    private final List<Double> piMessage;
    private final List<Node> source;

    private final Node individualSource;

    public PiMessage(List<Double> piMessage, List<Node> source, Node individualSource) {
        this.piMessage = piMessage;
        this.source = source;
        this.individualSource = individualSource;
    }

    public List<Double> getPiMessage() {
        return piMessage;
    }

    public List<Node> getSource() {
        return source;
    }

    public Node getIndividualSource() {
        return individualSource;
    }

    @Override
    public String toString() {
        return "PiMessage{" +
                "piMessage=" + piMessage +
                ", source=" + source +
                ", individualSource=" + individualSource +
                '}';
    }
}
