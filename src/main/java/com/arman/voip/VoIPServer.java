package com.arman.voip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VoIPServer extends Thread {

    protected ServerSocket sock;
    private int port;
    private List<VoIPClientHandler> clientHandlers;
    private AtomicInteger clientsOnServer;

    public VoIPServer(int port) {
        this.port = port;
        this.clientHandlers = new ArrayList<>();
        this.clientsOnServer = new AtomicInteger(0);
    }

    public static void main(String[] args) {
        VoIPServer server = new VoIPServer(2727);
        server.start();
    }

    public void run() {
        VoIPClientHandler handler;
        try {
            sock = new ServerSocket(port);
            while (true) {
                Socket client = sock.accept();
                handler = new VoIPClientHandler(this, client);
                add(handler);
                System.out.println("[client no. " + clientsOnServer + " connected.]");
                handler.start();
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(byte[] bytes) {
        for (int i = 0; i < this.clientHandlers.size(); i++) {
            this.clientHandlers.get(i).sendMessage(bytes);
        }
    }

    public void remove(VoIPClientHandler clientHandler) {
        if (this.clientHandlers.remove(clientHandler)) {
            this.clientsOnServer.getAndDecrement();
        }
    }

    public void add(VoIPClientHandler clientHandler) {
        if (this.clientHandlers.add(clientHandler)) {
            this.clientsOnServer.getAndIncrement();
        }
    }

    public void shutdown() {
        try {
            sock.close();
        } catch (IOException e) {
        }
        System.exit(0);
    }

}
