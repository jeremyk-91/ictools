package com.ictools.idapi.bayesnet;

public class MessageAction {
    private final Node source;
    private final Node sink;
    private final MessageType messageType;

    public MessageAction(Node source, Node sink, MessageType messageType) {
        this.source = source;
        this.sink = sink;
        this.messageType = messageType;
    }

    public Node getSource() {
        return source;
    }

    public Node getSink() {
        return sink;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageAction that = (MessageAction) o;

        if (messageType != that.messageType) return false;
        if (sink != null ? !sink.equals(that.sink) : that.sink != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (sink != null ? sink.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageAction{" +
                "source=" + source.getIdentifier() +
                ", sink=" + sink.getIdentifier() +
                ", messageType=" + messageType +
                '}';
    }
}
