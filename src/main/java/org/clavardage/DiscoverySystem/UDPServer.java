package org.clavardage.DiscoverySystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {

    private boolean running;
    private final ContactManager contactMgr;
    private final DatagramSocket socket;

    private boolean ignoreNext = false;

    public UDPServer(DatagramSocket socket) {
        super("UDPServer");
        this.contactMgr = ContactManager.getInstance();
        this.socket = socket;
        this.running = true;
    }

    /** Thread which listen to incoming traffic **/
    @Override
    public void run() {
        byte[] buf = new byte[256];

        while (this.running) {
            /** Creation of a new UDP PDU to receive incoming packets **/
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(inPacket);
            } catch (SocketException e) {
                System.out.println("--------- ERROR de UDPServer.run(): " + e.getMessage());
                this.halt();
                continue;
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

            /** Extraction of the data from the incoming PDU **/
            String received = new String(inPacket.getData(), 0, inPacket.getLength());
            String ip = inPacket.getAddress().toString().replace("/", "");

            if (received.isEmpty()) {
                continue;
            }

            /** Processing of the incoming data **/
            switch (received.charAt(0)) {
                /** Disconnection of an Agent **/
                case 'e' -> {
                    contactMgr.changeState(ContactState.DISCONNECTED, ip);
                }
                /** ConnectionListener of an Agent **/
                case 'c' -> {
                    contactMgr.addContact(new Contact(ip));
                    if (ignoreNext) {
                        ignoreNext = false;
                    } else {
                        if (contactMgr.getPseudo() != null) {
                            NetworkManager.getInstance().send(ip, "p" + contactMgr.getPseudo());
                        }
                    }
                }
                /** New Pseudo of an Agent **/
                case 'p' -> {
                    String name = received.substring(1);
                    contactMgr.changePseudo(name, ip);
                }
                default -> {
                    continue;
                }
            }
        }
        this.socket.close();
    }

    synchronized public void ignoreNextConnection() {
        this.ignoreNext = true;
    }

    public void halt() {
        this.running = false;
    }
}
