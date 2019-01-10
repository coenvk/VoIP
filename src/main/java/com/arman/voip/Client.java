package com.arman.voip;

import com.arman.voip.messages.Message;
import com.arman.voip.messages.TextMessage;
import com.arman.voip.messages.handlers.ClientMessageHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

@SuppressWarnings("Duplicates")
public class Client extends Thread {

    private String clientName;
    protected Socket sock;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ClientGUI gui;
    private ClientMessageHandler messageHandler;

    public Client(String name, InetAddress host, int port, ClientGUI gui) throws IOException {
        this.clientName = name;
        this.gui = gui;
        this.sock = new Socket(host, port);
        this.out = new ObjectOutputStream(sock.getOutputStream());
        this.in = new ObjectInputStream(sock.getInputStream());
        this.messageHandler = new ClientMessageHandler(this);
    }

    public void run() {
        Object object = null;
        try {
            while ((object = in.readObject()) != null) {
                Message message = (Message) object;
                this.messageHandler.handleMessage(message);
            }
        } catch (IOException e) {
            shutdown();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(TextMessage message) {
        this.gui.addMessage(message.toString());
    }

    public void sendMessage(Message message) {
        try {
            message.write(out);
        } catch (IOException e) {
            shutdown();
        }
    }

    public void sendMessage(String msg) {
        sendMessage(new TextMessage(this.clientName, msg));
    }

    public void shutdown() {
        try {
            sock.close();
        } catch (IOException e) {
        } finally {
            gui.dispose();
        }
        System.exit(0);
    }

    public String getClientName() {
        return clientName;
    }

}
