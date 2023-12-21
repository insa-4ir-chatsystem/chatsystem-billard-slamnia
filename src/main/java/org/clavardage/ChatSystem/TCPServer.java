package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.ContactManager;
import org.clavardage.DiscoverySystem.NoContactFoundException;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class TCPServer {

    static private TCPServer instance = null;
    static private final int SERVERPORT = 8789;

    private ArrayList<ConnectionListener> connectionListeners;

    private TCPServer() {
        ContactManager contactMgr = ContactManager.getInstance();
        MessagesMgr msgMgr = MessagesMgr.getInstance();
        try {
            ServerSocket srvSock = new ServerSocket(TCPServer.SERVERPORT);
            while (true) {
                Socket sender = srvSock.accept();
                String ip = sender.getInetAddress().toString();
                try {
                    Contact contact = contactMgr.getContact(ip);
                    ConnectionListener con = new ConnectionListener(sender, contact);
                    connectionListeners.add(con);
                    con.start();
                } catch (NoContactFoundException e) {
                    System.out.println("Suspicious connection from " + ip);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public TCPServer getInstance() {
        if (TCPServer.instance == null) {
            TCPServer.instance = new TCPServer();
        }
        return TCPServer.instance;
    }

    public void halt() {
        for (ConnectionListener con: this.connectionListeners) {
            con.halt();
        }
        for (ConnectionListener con: this.connectionListeners) {
            try {
                con.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int getPort() {
        return TCPServer.SERVERPORT;
    }

}