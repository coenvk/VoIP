package com.arman.voip;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class VoIPClientHandler extends Thread {

    private final VoIPServer server;
    private final InputStream in;
    private final OutputStream out;

    public VoIPClientHandler(VoIPServer server, Socket sock) throws IOException {
        this.server = server;
        this.in = new BufferedInputStream(sock.getInputStream());
        this.out = new BufferedOutputStream(sock.getOutputStream());
    }

    public void run() {
        byte[] bytes = new byte[64];
        try {
            while (this.in.read(bytes) != 0) {
                System.out.println(Arrays.toString(bytes));
                this.server.broadcast(bytes);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void sendMessage(byte[] bytes) {
        try {
            this.out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.server.remove(this);
    }

}
