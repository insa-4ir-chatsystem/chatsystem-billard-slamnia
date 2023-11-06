package org.clavardage.DiscoverySystem;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {

    private boolean running;
    private final ContactManager contactMgr;
    private final NetworkManager networkMgr;
    private final DatagramSocket socket;

    public UDPServer(int port) {
        super("UDPServer");
        this.contactMgr = ContactManager.getInstance();
        this.networkMgr = NetworkManager.getInstance();
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        this.running = true;
    }

    @Override
    public void run() {
        try {
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
                socket.receive(inPacket);
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
        } catch (Exception e) {
            throw new RuntimeException("Failure of UDP Server");
        }
    }

    public synchronized void halt() {
        this.running = false;
    }
}
