package com.arman.voip;

import com.arman.voip.messages.Message;
import com.arman.voip.messages.TextMessage;
import com.arman.voip.messages.handlers.ServerMessageHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@SuppressWarnings("Duplicates")
public class ClientHandler extends Thread {

    private final Server server;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String clientName;
    private ServerMessageHandler messageHandler;

    public ClientHandler(Server server, Socket sock) throws IOException {
        this.server = server;
        this.in = new ObjectInputStream(sock.getInputStream());
        this.out = new ObjectOutputStream(sock.getOutputStream());
        this.messageHandler = new ServerMessageHandler(this);
    }

    public void announce() throws IOException, ClassNotFoundException {
        this.clientName = ((TextMessage) this.in.readObject()).getSenderName();
        this.server.broadcast(this.clientName, "[has entered]", this);
        sendMessage("Connected to server...");
    }

    public void run() {
        Object object = null;
        try {
            while ((object = this.in.readObject()) != null) {
                Message message = (Message) object;
                this.messageHandler.handleMessage(message);
            }
        } catch (IOException e) {
            shutdown();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void broadcastToServer(Message message) {
        this.server.broadcast(message);
    }

    public void sendMessage(Message message) {
        try {
            message.write(this.out);
        } catch (IOException e) {
            shutdown();
        }
    }

    public void sendMessage(String msg) {
        sendMessage(new TextMessage(this.clientName, msg));
    }

    public void shutdown() {
        this.server.remove(this);
        this.server.broadcast(this.clientName, "[has left]", this);
    }

    public String getClientName() {
        return this.clientName;
    }

}
