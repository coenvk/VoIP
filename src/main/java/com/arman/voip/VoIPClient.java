package com.arman.voip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class VoIPClient extends Thread {

    private Socket sock;
    private AudioInputDevice in;
    private AudioOutputDevice out;

    public VoIPClient(InetAddress host, int port) throws IOException {
        this.sock = new Socket(host, port);
        this.out = new AudioOutputDevice(new BufferedInputStream(sock.getInputStream()));
        this.in = new AudioInputDevice(new BufferedOutputStream(sock.getOutputStream()));
    }

    public static void main(String[] args) {
        InetAddress host = null;
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            VoIPClient client = new VoIPClient(host, 2727);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            sock.setKeepAlive(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.out.start();
        this.in.start();
    }

}
