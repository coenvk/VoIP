package com.arman.voip;

import com.arman.voip.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private static final int UNBOUNDED = -1;

    protected List<ClientHandler> clientHandlers;

    private int clientBound;

    public Room(int clientBound) {
        if (clientBound < 0) {
            this.clientBound = UNBOUNDED;
        } else {
            this.clientBound = clientBound;
        }
        this.clientHandlers = new ArrayList<>();
    }

    public Room() {
        this.clientBound = UNBOUNDED;
        this.clientHandlers = new ArrayList<>();
    }

    public void broadcast(Message message) {
        for (int i = 0; i < this.clientHandlers.size(); i++) {
            this.clientHandlers.get(i).sendMessage(message);
        }
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

    public int getClientBound() {
        return this.clientBound;
    }

    public boolean isFull() {
        if (isUnbounded()) {
            return false;
        }
        return this.clientHandlers.size() == this.clientBound;
    }

    public boolean add(ClientHandler clientHandler) {
        if (isFull()) {
            return false;
        }
        return this.clientHandlers.add(clientHandler);
    }

    public boolean remove(ClientHandler clientHandler) {
        return this.clientHandlers.remove(clientHandler);
    }

    public List<ClientHandler> getClientHandlers() {
        return this.clientHandlers;
    }

    public boolean isUnbounded() {
        return this.clientBound == UNBOUNDED;
    }

}
