package org.clavardage.ChatSystem.Network;

import org.clavardage.ChatSystem.messageManagement.Message;
import org.clavardage.ChatSystem.messageManagement.Origin;

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
                throw new RuntimeException(e);
            }
            this.sockets.put(ip, sock);
            try {
                out = new PrintWriter(sock.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (msg.getOrigin() == Origin.LOCAL) {
                    switch (msg.getType()) {
                        case FILE -> {}
                        case TEXT -> {
                            out.println(msg.getMessage());
                        }
                        case TEXT_AND_FILE -> {}
                    }
                }
        }
    }

    public void halt() {
        for (Socket sock: this.sockets.values()) {
            try {
                sock.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
