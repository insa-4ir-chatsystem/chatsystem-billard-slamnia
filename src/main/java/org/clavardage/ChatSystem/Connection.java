package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Observable;

public class Connection extends Thread {


    private Socket sock;
    private MessagesHistory hist;
    private boolean running = true;
    public Connection(Socket sock, MessagesHistory msgHist) {
        this.sock = sock;
        this.hist = msgHist;
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
                    this.hist.addMessage(new Message(inputline, this.hist.getContact(), Origin.REMOTE));
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
