package org.clavardage.ChatSystem.Network;

import org.clavardage.ChatSystem.messageManagement.Message;
import org.clavardage.ChatSystem.messageManagement.Origin;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class TCPSender {
    private static TCPSender instance = null;
    private final HashMap<String, Socket> sockets;

    private TCPSender() {
        this.sockets = new HashMap<>();
    }

    public static TCPSender getInstance() {
        if (TCPSender.instance == null) {
            TCPSender.instance = new TCPSender();
        }
        return TCPSender.instance;
    }

    public void sendMessage(Message msg) {
        String ip = msg.getContact().getIp();
        Socket sock = this.sockets.get(ip);
        PrintWriter out;
        if (sock == null) {
            try {
                sock = new Socket(ip, TCPServer.getPort());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not open socket to this contact");
            }
            this.sockets.put(ip, sock);
        }
        try {
            out = new PrintWriter(sock.getOutputStream(), true);
            if (msg.getOrigin() == Origin.LOCAL) {
                switch (msg.getType()) {
                    case FILE -> {}
                    case TEXT -> {
                        out.println(msg.getMessage());
                    }
                    case TEXT_AND_FILE -> {}
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Issue with database when adding contact");
        }
    }

    public void halt() {
        for (Socket sock: this.sockets.values()) {
            try {
                sock.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not close socket when halting TCPSender");
            }
        }
    }
}
