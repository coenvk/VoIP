package com.arman.voip.messages;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Serializable;

public abstract class Message implements Serializable {

    protected Object message;
    protected String senderName;

    public Message(String senderName, Object message) {
        this.senderName = senderName;
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public Object getMessage() {
        return message;
    }

    public void write(ObjectOutput out) throws IOException {
        out.writeObject(this);
        out.flush();
    }

}
