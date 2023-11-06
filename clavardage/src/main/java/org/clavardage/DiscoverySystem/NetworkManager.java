package org.clavardage.DiscoverySystem;
import java.net.*;

public class NetworkManager {
    private static NetworkManager instance;
    private static final int PORT = 4269;
    private static UDPServer udpServer;
    private static DatagramSocket socket;

    private NetworkManager() {
        NetworkManager.udpServer = new UDPServer(NetworkManager.PORT);
        NetworkManager.udpServer.start();
        try {
            NetworkManager.socket = new DatagramSocket();
        } catch (Exception e) {
            throw new RuntimeException("Could not create client Socket");
        }
    }

    public static NetworkManager getInstance() {
        if (NetworkManager.instance == null) {
            return new NetworkManager();
        }
        return NetworkManager.instance;
    }

    public void send (String ip, String msg) {
        byte[] buf = msg.getBytes();
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, NetworkManager.PORT);
            NetworkManager.socket.send(packet);
        } catch (Exception ignored) {

        }
    }

    protected void finalize() {
        NetworkManager.socket.close();
    }

}
