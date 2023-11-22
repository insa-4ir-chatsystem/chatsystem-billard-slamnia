import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.clavardage.DiscoverySystem.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicReference;

public class DiscoverySystemTests {
    private static DiscoverySystem ds;
    private static DatagramSocket socket;
    private static ContactManager contactManager;
    private static final int testPort = 1234;
    @BeforeAll
    public static void setUp() {

        try {
            socket = new DatagramSocket(NetworkManager.getPort());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        DiscoverySystemTests.ds = DiscoverySystem.getInstance(DiscoverySystemTests.testPort);
        DiscoverySystemTests.contactManager = ContactManager.getInstance();
    }

    @AfterEach
    public void cleanUp() {
        DiscoverySystemTests.contactManager.setAllToDisconnected();
    }

    @DisplayName("Test pour la connexion de l'agent")
    @Test
    public void connectionTest() {
        this.connectionPhase("Pierre");
    }

    @DisplayName("Test pour la connexion de l'agent avec un pseudo existant")
    @Test
    public void connectionExistingPseudoTest() {

        DiscoverySystemTests.contactManager.addContact(new Contact("Pierre", "127.0.0.1"));
        assertThrows(ExistingPseudoException.class,() -> {
            ds.connect("Pierre");
        });
        byte[] buf = new byte[256];
        DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(inPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String received = new String(inPacket.getData(), 0, inPacket.getLength());

        assertEquals("c", received);
        byte[] buf1 = "pPierre".getBytes();
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket packet = new DatagramPacket(buf1, buf1.length, address, DiscoverySystemTests.testPort);
            socket.send(packet);
        } catch (Exception ignored) {

        }
    }

    @DisplayName("Test pour la déconnexion de l'agent")
    @Test
    public void disconnectionTest() {

    }

    public void connectionPhase(String pseudo) {
        assertDoesNotThrow(() -> {
            ds.connect(pseudo);
        });
        byte[] buf = new byte[256];
        DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(inPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String received = new String(inPacket.getData(), 0, inPacket.getLength());

        assertEquals("c", received);

        try {
            socket.receive(inPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        received = new String(inPacket.getData(), 0, inPacket.getLength());

        assertEquals("p" + pseudo, received);
    }

    @DisplayName("Test pour le changement de pseudo de l'agent")
    @Test
    public void changePseudoTest() {

        assertThrows(ExistingPseudoException.class,() -> {
            ds.connect("Pierre");
        });
        byte[] buf = new byte[256];
        DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(inPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String received = new String(inPacket.getData(), 0, inPacket.getLength());

        assertEquals("c", received);
        byte[] buf1 = "pPierre".getBytes();
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket packet = new DatagramPacket(buf1, buf1.length, address, DiscoverySystemTests.testPort);
            socket.send(packet);
        } catch (Exception ignored) {

        }
    }

    @DisplayName("Test pour la notification des Observers")
    @Test
    public void notifyTest() {

    }
}
