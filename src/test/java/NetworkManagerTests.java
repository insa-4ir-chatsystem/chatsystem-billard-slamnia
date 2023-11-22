import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.clavardage.DiscoverySystem.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicReference;


public class NetworkManagerTests
{
    private static NetworkManager netManager;
    private static ContactManager contManager;
    private static DatagramSocket socket;
    private static int port = 6666;

    @BeforeAll
    public static void setUp() {
        NetworkManagerTests.contManager = ContactManager.getInstance();
        NetworkManagerTests.netManager = NetworkManager.getInstance();
    }

    @BeforeEach
    public void setUpEach() {
        try {
            NetworkManagerTests.socket = new DatagramSocket(NetworkManagerTests.port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanUp() {
        contManager.resetList();
        NetworkManagerTests.socket.close();
    }

    @AfterAll
    public static void cleanUpFinal() {
        netManager = null;
        NetworkManager.release();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test pour l'envoi d'un message")
    @Test
    public void sendTest() {
        try {
            netManager.sendToPort("127.0.0.1", port,"a");
            byte[] buf = new byte[256];
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            NetworkManagerTests.socket.receive(inPacket);
            String received = new String(inPacket.getData(), 0, inPacket.getLength());

            assertEquals("a",received) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test pour l'envoi d'un message en broadcast")
    @Test
    public void sendAllTest() {
        try {
            netManager.sendAllToPort(port,"a");
            byte[] buf = new byte[256];
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            NetworkManagerTests.socket.receive(inPacket);
            String received = new String(inPacket.getData(), 0, inPacket.getLength());

            assertEquals("a",received) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test pour la réception d'une connexion")
    @Test
    public void connectionTest() {
        byte[] bufConnection = "c".getBytes();
        byte[] bufChangePseudo = "pPaul".getBytes();
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket packet1 = new DatagramPacket(bufConnection, bufConnection.length, address, NetworkManager.getPort());
            NetworkManagerTests.socket.send(packet1);
            DatagramPacket packet2 = new DatagramPacket(bufChangePseudo, bufChangePseudo.length, address, NetworkManager.getPort());
            NetworkManagerTests.socket.send(packet2);

            Thread.sleep(500);
            assertEquals(1,contManager.getConnectedContacts().size());
            assertEquals("Paul",contManager.getConnectedContacts().get(0).getName());
        } catch (Exception ignored) {
        }
    }

    @DisplayName("Test pour la réception d'une déconnexion")
    @Test
    public void disconnectionTest() {
        byte[] bufConnection = "c".getBytes();
        byte[] bufChangePseudo = "pPaul".getBytes();
        byte[] bufDisconnection = "e".getBytes();
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket packet1 = new DatagramPacket(bufConnection, bufConnection.length, address, NetworkManager.getPort());
            NetworkManagerTests.socket.send(packet1);
            DatagramPacket packet2 = new DatagramPacket(bufChangePseudo, bufChangePseudo.length, address, NetworkManager.getPort());
            NetworkManagerTests.socket.send(packet2);
            DatagramPacket packet3 = new DatagramPacket(bufDisconnection, bufDisconnection.length, address, NetworkManager.getPort());
            NetworkManagerTests.socket.send(packet3);

            Thread.sleep(500);
            assertEquals(0,contManager.getConnectedContacts().size());
        } catch (Exception ignored) {
        }
    }
}
