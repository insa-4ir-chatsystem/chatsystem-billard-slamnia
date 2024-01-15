import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.clavardage.DiscoverySystem.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    @AfterAll
    public static void finishTests() {
        DiscoverySystem.release();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanUp() {
        DiscoverySystemTests.contactManager.setAllToDisconnected();
    }

    public void sendFromTestingNetwork(String msg) {
        byte[] buf1 = msg.getBytes();
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            DatagramPacket packet = new DatagramPacket(buf1, buf1.length, address, DiscoverySystemTests.testPort);
            socket.send(packet);
        } catch (Exception ignored) {

        }
    }

    public void expectPacket(String msg) {
        byte[] buf = new byte[256];
        DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(inPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String received = new String(inPacket.getData(), 0, inPacket.getLength());

        assertEquals(msg, received);
    }

    public void connectionPhase(String pseudo) {
        assertDoesNotThrow(() -> {
            ds.connect(pseudo);
        });
        expectPacket("c");

        expectPacket("p" + pseudo);
    }
    @DisplayName("Test pour la connexion de l'agent")
    @Test
    public void connectionTest() {
        this.connectionPhase("Pierre");
    }

//   TODO: correct the test
//    @DisplayName("Test pour la connexion de l'agent avec un pseudo existant")
//    @Test
//    public void connectionExistingPseudoTest() {
//
//        ExecutorService es = Executors.newSingleThreadExecutor();
//        Future<?> future = es.submit(() -> {
//                ds.connect("Pierre");
//                return null;
//        });
//
//        expectPacket("c");
//        sendFromTestingNetwork("pPierre");
//        assertThrows(ExistingPseudoException.class, future::get);
//    }

    @DisplayName("Test pour la déconnexion de l'agent")
    @Test
    public void disconnectionTest() {
        connectionPhase("Josianne");
        ds.disconnect();
        expectPacket("e");
    }


    @DisplayName("Test pour le changement de pseudo de l'agent")
    @Test
    public void changePseudoTest() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<?> future = es.submit(() -> {
            assertDoesNotThrow(() -> {
                ds.connect("Pierre");
            });
            assertDoesNotThrow(() -> {
                ds.changePseudo("Valerie");
            });
            Thread.sleep(100);
            System.out.println("Trying to connect as Cedric");
            assertThrows(ExistingPseudoException.class, () -> {
                ds.changePseudo("Cedric");
            });
            System.out.println("Connected as Cedric");
            return null;
        });

        expectPacket("c");
        sendFromTestingNetwork("pCedric");
        expectPacket("pPierre");
        expectPacket("pValerie");

        try {
            future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            fail(e.getMessage());
        }
    }

    class ContactObserver implements Observer {
        private boolean status;

        public ContactObserver() {
            this.status = false;
        }

        @Override
        public void update(Observable observable, Object o) {
            this.status = true;
        }

        public boolean getStatus() {
            return this.status;
        }
    }

    @DisplayName("Test pour la notification des Observers")
    @Test
    public void notifyTest() {
        ContactObserver obs = new ContactObserver();
        ds.attachObserverToContactList(obs);
        connectionPhase("Jean");
        contactManager.addContact(new Contact("45.45.45.45"));
//        sendFromTestingNetwork("c");
//        sendFromTestingNetwork("pValentin");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertTrue(obs.getStatus());
    }
}
