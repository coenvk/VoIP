package com.arman.voip;

import com.arman.voip.messages.Message;
import com.arman.voip.messages.TextMessage;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<ClientHandler> clientHandlers;

    public Lobby() {
        this.clientHandlers = new ArrayList<>();
    }

    public void broadcast(Message message) {
        for (int i = 0; i < this.clientHandlers.size(); i++) {
            this.clientHandlers.get(i).sendMessage(message);
        }
    }

    public void broadcast(String senderName, String msg) {
        broadcast(new TextMessage(senderName, msg));
    }

    public void broadcast(Message message, ClientHandler excluded) {
        for (ClientHandler ch : clientHandlers) {
            if (!ch.getClientName().equals(excluded.getClientName())) {
                ch.sendMessage(message);
            }
        }
    }

    public void broadcast(String senderName, String msg, ClientHandler excluded) {
        broadcast(new TextMessage(senderName, msg), excluded);
    }

    public boolean add(ClientHandler clientHandler) {
        return this.clientHandlers.add(clientHandler);
    }

    public ClientHandler getClientHandler(int index) {
        return this.clientHandlers.get(index);
    }

    public boolean hasClientHandler(ClientHandler clientHandler) {
        return this.clientHandlers.contains(clientHandler);
    }

    public boolean isEmpty() {
        return this.clientHandlers.isEmpty();
    }

    public List<ClientHandler> getClientHandlers() {
        return this.clientHandlers;
    }

    public boolean remove(ClientHandler clientHandler) {
        return this.clientHandlers.remove(clientHandler);
    }

}
