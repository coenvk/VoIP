package com.arman.voip;

import com.arman.voip.messages.Message;
import com.arman.voip.messages.TextMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {

    private int port;
    private Lobby lobby;
    private List<Room> rooms;
    private ServerGUI gui;
    protected ServerSocket sock;
    private AtomicInteger clientsOnServer;

    public Server(int port, ServerGUI gui) {
        this.lobby = new Lobby();
        this.rooms = new ArrayList<>();
        this.port = port;
        this.gui = gui;
        this.clientsOnServer = new AtomicInteger(0);
    }

    public void run() {
        ClientHandler handler;
        try {
            sock = new ServerSocket(port);
            while (true) {
                Socket client = sock.accept();
                handler = new ClientHandler(this, client);
                add(handler);
                gui.addMessage("[client no. " + clientsOnServer + " connected.]");
                handler.announce();
                handler.start();
            }
        } catch (IOException e) {
            shutdown();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Message message) {
        this.gui.addMessage(message.toString());
        this.lobby.broadcast(message);
    }

    public void broadcast(String senderName, String msg) {
        broadcast(new TextMessage(senderName, msg));
    }

    public void broadcast(Message message, ClientHandler excluded) {
        gui.addMessage(message.toString());
        this.lobby.broadcast(message, excluded);
    }

    public void broadcast(String senderName, String msg, ClientHandler excluded) {
        broadcast(new TextMessage(senderName, msg), excluded);
    }

    public void add(ClientHandler client) {
        if (this.lobby.add(client)) {
            this.clientsOnServer.getAndIncrement();
        }
    }

    public void remove(ClientHandler client) {
        if (this.lobby.remove(client)) {
            this.clientsOnServer.getAndDecrement();
        }
    }

    public void add(Room room) {
        this.rooms.add(room);
    }

    public void remove(Room room) {
        this.rooms.remove(room);
    }

    public void shutdown() {
        broadcast("", "Server shutting down...");
        try {
            sock.close();
        } catch (IOException e) {
        } finally {
            gui.dispose();
        }
        System.exit(0);
    }

}
