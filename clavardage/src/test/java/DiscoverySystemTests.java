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
    @BeforeAll
    public static void setUp() {
        ds = DiscoverySystem.getInstance(1234);
        try {
            socket = new DatagramSocket(NetworkManager.getPort());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test pour la connexion de l'agent")
    @Test
    public void connectionTest() {
        assertDoesNotThrow(() -> {
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

        try {
            socket.receive(inPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        received = new String(inPacket.getData(), 0, inPacket.getLength());

        assertEquals("pPierre", received);
    }

    @DisplayName("Test pour la connexion de l'agent avec un pseudo existant")
    @Test
    public void connectionExistingPseudoTest() {

    }

    @DisplayName("Test pour la déconnexion de l'agent")
    @Test
    public void disconnectionTest() {

    }

    @DisplayName("Test pour le changement de pseudo de l'agent")
    @Test
    public void changePseudoTest() {

    }

    @DisplayName("Test pour la notification des Observers")
    @Test
    public void notifyTest() {

    }
}
