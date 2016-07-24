package com.ictools.idapi.bayesnet;

import java.util.List;

public class PiMessage {
    private final List<Double> piMessage;

    public PiMessage(List<Double> piMessage) {
        this.piMessage = piMessage;
    }

    public List<Double> getPiMessage() {
        return piMessage;
    }
}
