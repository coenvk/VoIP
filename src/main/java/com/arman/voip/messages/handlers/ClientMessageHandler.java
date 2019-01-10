package com.arman.voip.messages.handlers;

import com.arman.voip.Client;
import com.arman.voip.messages.FileMessage;
import com.arman.voip.messages.TextMessage;

public class ClientMessageHandler extends MessageHandler {

    private Client client;

    public ClientMessageHandler(Client client) {
        this.client = client;
    }

    @Override
    protected boolean handleTextMessage(TextMessage message) {
        this.client.showMessage(message);
        return true;
    }

    @Override
    protected boolean handleFileMessage(FileMessage message) {
        this.client.showMessage(new TextMessage(message.getSenderName(), "{has sent a file}"));
        return true;
    }

}
