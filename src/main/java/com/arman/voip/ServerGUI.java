package com.arman.voip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame implements ActionListener, NetworkUI {

    private static final long serialVersionUID = -6433149565887723287L;
    private JButton connect;
    private JTextField port;
    private JTextArea txtArea;
    private JScrollPane messages;
    private Server server;

    public ServerGUI() {
        setup();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (server != null) {
                    server.shutdown();
                }
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new ServerGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == connect) {
            listen();
        }
    }

    public void setup() {
        setSize(600, 435);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel p1 = new JPanel(new FlowLayout());
        connect = new JButton("Listen");
        connect.addActionListener(this);
        p1.add(createStartPanel(), BorderLayout.WEST);
        p1.add(connect, BorderLayout.EAST);
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        JLabel textMessages = new JLabel("Messages: ");
        txtArea = new JTextArea("", 18, 50);
        messages = new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setEditable(false);
        p2.add(textMessages);
        p2.add(messages, BorderLayout.SOUTH);
        this.setLayout(new FlowLayout());
        this.add(p1);
        this.add(p2);
    }

    private JPanel createStartPanel() {
        JPanel start = new JPanel(new GridLayout(2, 2));
        JLabel textAddress = new JLabel("Address: ");
        JTextField inputAddress = new JTextField(getHostAddress(), 12);
        inputAddress.setEditable(false);
        JLabel textPort = new JLabel("Port: ");
        port = new JTextField("2727", 5);
        start.add(textAddress);
        start.add(inputAddress);
        start.add(textPort);
        start.add(port);
        return start;
    }

    public String getHostAddress() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            return "?unknown?";
        }
    }

    public void listen() {
        int portnr = 0;
        try {
            portnr = Integer.parseInt(port.getText());
            if (portnr < 0 || portnr > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            addMessage("Invalid port!");
            return;
        }
        port.setEditable(false);
        connect.setEnabled(false);
        server = new Server(portnr, this);
        addMessage("Started listening on port " + portnr + "...");
        server.start();
    }

    public void addMessage(String msg) {
        txtArea.append(msg + "\n");
    }

}
