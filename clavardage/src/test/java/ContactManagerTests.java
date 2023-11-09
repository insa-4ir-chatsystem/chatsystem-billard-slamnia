import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.clavardage.DiscoverySystem.*;

import java.lang.reflect.Executable;
import java.util.concurrent.atomic.AtomicReference;

public class ContactManagerTests {

    private static ContactManager manager;

    @BeforeAll
    public static void setUp() {
        ContactManagerTests.manager = ContactManager.getInstance();
    }

    @AfterEach
    public void cleanUp() {
        manager.resetList();
    }

    @DisplayName("Test pour l'insertion d'un contact")
    @Test
    public void insertionContactTest() {
        manager.addContact(new Contact("a"));
        manager.changePseudo("Michel", "a");
        AtomicReference<Contact> michel = new AtomicReference<>();
        assertDoesNotThrow(() -> {
            michel.set(manager.getConnectedContacts().get(0));
        });
        assertEquals(michel.get().getName(), "Michel");
        assertEquals(michel.get().getIp(), "a");
        assertEquals(ContactState.CONNECTED, michel.get().getState());
    }

    @DisplayName("Test pour l'insertion d'un contact deux fois")
    @Test
    public void insertionContactTwiceTest() {
        manager.addContact(new Contact("a"));
        manager.addContact(new Contact("a"));
        manager.changePseudo("Michel", "a");
        assertEquals(1, manager.getConnectedContacts().size());
    }

    @DisplayName("Test pour le changement du pseudo de l'utilisateur (agent)")
    @Test
    public void changePseudoUserTest() {
        assertDoesNotThrow(() -> {
            manager.setPseudo("Claude");
        });
        assertEquals("Claude", manager.getPseudo());
    }

    @DisplayName("Test pour un changement de pseudo invalide")
    @Test
    public void changeToInvalidPseudoTest() {
        manager.addContact(new Contact("a"));
        manager.changePseudo("Michel", "a");
        assertThrows(Exception.class, () -> {
            manager.setPseudo("Michel");
        });
    }

    @DisplayName("Test pour une déconnexion d'un agent")
    @Test
    public void disconnectingTest() {
        manager.addContact(new Contact("a"));
        manager.changePseudo("Michel", "a");
        manager.addContact(new Contact("b"));
        manager.changePseudo("Lola", "b");
        manager.changeState(ContactState.DISCONNECTED,"b");
        assertEquals(1, manager.getConnectedContacts().size());
    }

    @DisplayName("Test pour la connexion d'un agent sans pseudo")
    @Test
    public void connectionWithoutPseudoTest() {
        manager.addContact(new Contact("z"));
        assertEquals(0, manager.getConnectedContacts().size());
    }

    @DisplayName("Test pour la connexion d'un agent sans pseudo")
    @Test
    public void connectionWithoutPseudoTwiceTest() {
        manager.addContact(new Contact("z"));
        manager.changeState(ContactState.DISCONNECTED, "z");
        manager.addContact(new Contact("z"));
        assertEquals(0, manager.getConnectedContacts().size());
    }
}