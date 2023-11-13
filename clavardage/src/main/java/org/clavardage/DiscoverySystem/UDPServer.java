package org.clavardage.DiscoverySystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {

    private boolean running;
    private final ContactManager contactMgr;
    private final NetworkManager networkMgr;
    private final DatagramSocket socket;

    public UDPServer(DatagramSocket socket, NetworkManager netMgr) {
        super("UDPServer");
        this.contactMgr = ContactManager.getInstance();
        this.networkMgr = netMgr;
        this.socket = socket;
        this.running = true;
    }

    @Override
    public void run() {
        boolean syncRunning;
        synchronized (this) {
            syncRunning = this.running;
        }
        byte[] buf = new byte[256];

        while (syncRunning) {
            synchronized (this) {
                syncRunning = this.running;
            }
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(inPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String received = new String(inPacket.getData(), 0, inPacket.getLength());
            String ip = inPacket.getAddress().toString();

            if (received.isEmpty()) {
                continue;
            }

            switch (received.charAt(0)) {
                case 'e' -> {
                    contactMgr.changeState(ContactState.DISCONNECTED, ip);
                }
                case 'c' -> {
                    contactMgr.addContact(new Contact(ip));
                    networkMgr.send(ip, "p" + contactMgr.getPseudo());
                }
                case 'p' -> {
                    contactMgr.changePseudo(received.substring(1), ip);
                }
                default -> {
                    continue;
                }
            }
        }
        socket.close();
    }

    public synchronized void halt() {
        this.running = false;
    }
}
