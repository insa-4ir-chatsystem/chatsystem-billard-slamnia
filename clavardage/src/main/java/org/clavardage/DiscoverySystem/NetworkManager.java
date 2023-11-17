package org.clavardage.DiscoverySystem;
import java.net.*;

public class NetworkManager {
    private static NetworkManager instance;
    private static final int PORT = 4269;
    private static UDPServer udpServer;
    private static DatagramSocket socket;

    private NetworkManager(int port) {
        try {
            //NetworkManager.socket = new DatagramSocket(NetworkManager.PORT);
            NetworkManager.socket = new DatagramSocket(port);
        } catch (Exception e) {
            throw new RuntimeException("Could not create client Socket");
        }
        NetworkManager.udpServer = new UDPServer(socket, this);
        NetworkManager.udpServer.start();
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
            NetworkManager.socket.send(packet);
        } catch (Exception ignored) {

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
            NetworkManager.socket.send(packet);
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
        NetworkManager.socket.close();
    }

}
