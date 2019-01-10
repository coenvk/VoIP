package com.arman.voip.messages;

public class TextMessage extends Message {

    public TextMessage(String senderName, Object message) {
        super(senderName, message);
    }

    @Override
    public String toString() {
        return this.getSenderName() + ": " + this.message.toString();
    }

}
