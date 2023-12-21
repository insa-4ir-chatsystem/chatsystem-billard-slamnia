package org.clavardage.ChatSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TCPSender {
    private static TCPSender instance = null;
    private final HashMap<String, PrintWriter> writers;

    private TCPSender() {
        this.writers = new HashMap<>();
    }

    public static TCPSender getInstance() {
        if (TCPSender.instance == null) {
            TCPSender.instance = new TCPSender();
        }
        return TCPSender.instance;
    }

    public void sendMessage(Message msg) {
        String ip = msg.getContact().getIp();
        PrintWriter out = this.writers.get(ip);
        if (out == null) {
            Socket sock = null;
            try {
                sock = new Socket(ip, TCPServer.getPort());
                out = new PrintWriter(sock.getOutputStream(), true);
                this.writers.put(ip, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (msg.getOrigin() == Origin.LOCAL) {
            switch (msg.getType()) {
                case FILE -> {}
                case TEXT -> {
                    this.writers.get(ip).println(msg.getMessage());
                }
                case TEXT_AND_FILE -> {}
            }
        }
    }
}
