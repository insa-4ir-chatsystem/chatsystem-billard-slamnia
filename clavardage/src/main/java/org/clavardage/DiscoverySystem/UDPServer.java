package org.clavardage.DiscoverySystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {

    private boolean running;
    private final ContactManager contactMgr;
    private NetworkManager networkMgr;
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
        byte[] buf = new byte[256];

        while (this.running) {
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            System.out.println("Port Udp server : " + this.socket.getLocalPort());
            try {
                socket.receive(inPacket);
            } catch (SocketException e) {
                this.halt();
                continue;
            }catch (IOException e) {
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
                    System.out.println("pseudo received : " +received.substring(1) );
                    contactMgr.changePseudo(received.substring(1), ip);
                }
                default -> {
                    continue;
                }
            }
        }
    }

    public void halt() {
        this.running = false;
        this.networkMgr = null;
    }
}
