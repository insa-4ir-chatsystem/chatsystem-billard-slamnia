package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ConnectionListener extends Thread {


    private Socket sock;
    private MessagesMgr  msgMgr;
    private Contact contact;
    private boolean running = true;
    public ConnectionListener(Socket sock, Contact contact) {
        this.sock = sock;
        this.msgMgr = MessagesMgr.getInstance();
        this.contact = contact;
    }

    @Override
    public void run() {
        BufferedReader in;
        TCPServer tcpServer = TCPServer.getInstance();
        try {
            in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String inputline;
        while (running) {
            try {
                inputline = in.readLine();
                synchronized (this) {
                    // TODO: make this more efficient, because this has to find the right history
                    this.msgMgr.addMessage(new Message(inputline, this.contact, Origin.REMOTE));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.sock.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void halt() {
        this.running = false;
    }
}
