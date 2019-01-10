package com.arman.voip.messages.handlers;

import com.arman.voip.messages.FileMessage;
import com.arman.voip.messages.Message;
import com.arman.voip.messages.TextMessage;

public abstract class MessageHandler {

    public MessageHandler() {

    }

    protected abstract boolean handleTextMessage(TextMessage message);

    protected abstract boolean handleFileMessage(FileMessage message);

    public boolean handleMessage(Message message) {
        if (message instanceof TextMessage) {
            return handleTextMessage((TextMessage) message);
        }
        return false;
    }

}
