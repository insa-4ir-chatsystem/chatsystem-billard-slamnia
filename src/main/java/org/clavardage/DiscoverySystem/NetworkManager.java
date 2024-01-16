package org.clavardage.DiscoverySystem;
import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class NetworkManager {
    private static NetworkManager instance;
    private static final int PORT = 4269;
    private UDPServer udpServer;
    private DatagramSocket socket;

    private NetworkManager(int port) {
       try {
            //NetworkManager.socket = new DatagramSocket(NetworkManager.PORT);
            this.socket = new DatagramSocket(port);
//            System.out.println("Port network server : " + this.socket.getLocalPort());
        } catch (Exception e) {
            throw new RuntimeException("Could not create client Socket");
        }
        this.udpServer = new UDPServer(socket);
        this.udpServer.start();
    }

    public synchronized static NetworkManager getInstance(int port) {
        if (NetworkManager.instance == null) {
            NetworkManager.instance = new NetworkManager(port);
        }
        return NetworkManager.instance;
    }

    public synchronized static NetworkManager getInstance() {
        if (NetworkManager.instance == null) {
            NetworkManager.instance = new NetworkManager(NetworkManager.PORT);
        }
        return NetworkManager.instance;
    }

    public void sendToPort (String ip, int port, String msg) {
        byte[] buf = msg.getBytes();
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            this.socket.send(packet);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not send " + msg + " to " + ip + ". ERROR: " + e.getMessage());
        }
    }

    public void send (String ip, String msg) {
        sendToPort(ip, NetworkManager.PORT, msg);
    }

    public void sendAllToPort (int port, String msg) {
            byte[] buf = msg.getBytes();
            try {
                InetAddress address = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                this.socket.send(packet);
            } catch (IOException e) {
                System.out.println("--------- ERROR de NetworkManager.sendAllToPort: " + e.getMessage());
            } catch (Exception ignored) {
            }
    }

    public void sendAll (String msg) {
        sendAllToPort(NetworkManager.PORT, msg);
    }

    public static int getPort() {
        return NetworkManager.PORT;
    }

    protected void finalize() {
        this.socket.close();
        try {
            this.udpServer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void release() {
        if (NetworkManager.instance != null) {
            NetworkManager.instance.finalize();
            NetworkManager.instance = null;
        }
    }

    public void ignoreNextConnection() {
        this.udpServer.ignoreNextConnection();
    }

}
