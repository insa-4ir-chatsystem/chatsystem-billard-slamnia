package org.clavardage.ChatSystem.Network;

import org.clavardage.ChatSystem.messageManagement.Message;
import org.clavardage.ChatSystem.messageManagement.MessagesMgr;
import org.clavardage.ChatSystem.messageManagement.Origin;
import org.clavardage.DiscoverySystem.Contact;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ConnectionListener extends Thread {


    private Socket sock;
    private MessagesMgr msgMgr;
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
        try {
            in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            String inputline;
            while (this.running) {
                try {
                    inputline = in.readLine();
                    if (inputline == null) {
                        this.running = false;
                    } else {
                        synchronized (this) {
                            // TODO: make this more efficient, because this has to find the right history
                            this.msgMgr.addMessage(new Message(inputline, this.contact, Origin.REMOTE));
                        }
                    }
                } catch (IOException e) {
                    if (this.running) {
                        JOptionPane.showMessageDialog(null, "Could not read incoming message");
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot receive messages from contact");
        }
    }

    public synchronized void halt() {
        this.running = false;
        try {
            this.sock.close();
        } catch (IOException e) {
        }
    }
}
