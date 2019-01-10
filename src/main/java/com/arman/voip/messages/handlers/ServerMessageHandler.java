package com.arman.voip.messages.handlers;

import com.arman.voip.ClientHandler;
import com.arman.voip.messages.FileMessage;
import com.arman.voip.messages.TextMessage;

public class ServerMessageHandler extends MessageHandler {

    private ClientHandler clientHandler;

    public ServerMessageHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    protected boolean handleTextMessage(TextMessage message) {
        this.clientHandler.broadcastToServer(message);
        return true;
    }

    @Override
    protected boolean handleFileMessage(FileMessage message) {
        this.clientHandler.broadcastToServer(message);
        return true;
    }

}
