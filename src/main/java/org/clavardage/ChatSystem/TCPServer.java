package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.ContactManager;
import org.clavardage.DiscoverySystem.NoContactFoundException;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Observable;

public class TCPServer {

    static private TCPServer instance = null;
    static private final int SERVERPORT = 8789;

    private ArrayList<Connection> connections;

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
                    Connection con = new Connection(sender, msgMgr.getHistory(contact));
                    connections.add(con);
                    con.start();
                } catch (NoContactFoundException e) {
                    sender.close();
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
        for (Connection con: this.connections) {
            con.halt();
        }
        for (Connection con: this.connections) {
            try {
                con.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}