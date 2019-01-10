package com.arman.voip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientGUI extends JFrame implements ActionListener, NetworkUI {

    private static final long serialVersionUID = 8120480775194029576L;
    private JButton connect;
    private JTextField host;
    private JTextField port;
    private JTextField name;
    private JTextField myMessage;
    private JTextArea txtArea;
    private JScrollPane messages;
    private Client client;

    public ClientGUI() {
        setup();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        myMessage.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 8213220564984309807L;

            public void actionPerformed(ActionEvent e) {
                String msg = myMessage.getText().trim();
                if (!msg.isEmpty()) {
                    client.sendMessage(myMessage.getText());
                }
                myMessage.setText("");
            }
        });
    }

    public static void main(String[] args) {
        new ClientGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == connect) {
            connect();
        }
    }

    public void setup() {
        setSize(600, 435);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel p1 = new JPanel(new FlowLayout());
        connect = new JButton("Connect");
        connect.addActionListener(this);
        p1.add(createStartPanel(), BorderLayout.WEST);
        p1.add(connect, BorderLayout.EAST);
        JPanel p2 = new JPanel(new BorderLayout());
        JLabel textMyMessage = new JLabel("My message: ");
        myMessage = new JTextField("", 50);
        myMessage.setEditable(false);
        p2.add(textMyMessage);
        p2.add(myMessage, BorderLayout.SOUTH);
        JPanel p3 = new JPanel(new BorderLayout());
        JLabel textMessages = new JLabel("Messages: ");
        txtArea = new JTextArea("", 14, 50);
        messages = new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setEditable(false);
        p3.add(textMessages);
        p3.add(messages, BorderLayout.SOUTH);
        this.setLayout(new FlowLayout());
        this.add(p1);
        this.add(p2);
        this.add(p3);
    }

    private JPanel createStartPanel() {
        JPanel start = new JPanel(new GridLayout(3, 2));
        JLabel textHost = new JLabel("Hostname: ");
        host = new JTextField("", 12);
        JLabel textPort = new JLabel("Port: ");
        port = new JTextField("2727", 5);
        JLabel textName = new JLabel("Name: ");
        name = new JTextField("", 20);
        start.add(textHost);
        start.add(host);
        start.add(textPort);
        start.add(port);
        start.add(textName);
        start.add(name);
        return start;
    }

    public void connect() {
        InetAddress addr = null;
        int portnr = 0;
        try {
            String hostName = host.getText().trim();
            if (!hostName.isEmpty()) {
                addr = InetAddress.getByName(host.getText());
            } else {
                throw new UnknownHostException();
            }
        } catch (UnknownHostException e) {
            addMessage("Invalid hostname!");
            return;
        }
        try {
            portnr = Integer.parseInt(port.getText());
            if (portnr < 0 || portnr > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            addMessage("Invalid port!");
            return;
        }
        try {
            String clientName = name.getText().trim();
            if (clientName.isEmpty() || clientName.length() > 20) {
                addMessage("Invalid name!");
                return;
            }
            client = new Client(clientName, addr, portnr, this);
            client.sendMessage(clientName);
            client.start();
            myMessage.setEditable(true);
            host.setEditable(false);
            port.setEditable(false);
            name.setEditable(false);
            connect.setEnabled(false);
        } catch (IOException e) {
            addMessage("Failed to connect");
        }
    }

    public void addMessage(String msg) {
        txtArea.append(msg + "\n");
    }

}
