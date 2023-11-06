import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.clavardage.DiscoverySystem.*;

public class ContactManagerTests {

    private static ContactManager manager;

    @BeforeAll
    public static void setUp() {
        ContactManagerTests.manager = ContactManager.getInstance();
    }

    @DisplayName("Test pour l'insertion d'un contact")
    @Test
    public void insertionContactTest() {
        manager.addContact(new Contact("a"));
        manager.changePseudo("Michel", "a");
        Contact michel = manager.getConnectedContacts().get(0);
        assertEquals(michel.getName(), "Michel");
        assertEquals(michel.getIp(), "a");
        assertEquals(michel.getState(), ContactState.CONNECTED);
    }

    public void insertionContactTwiceTest() {
        manager.addContact(new Contact("a"));
        manager.addContact(new Contact("a"));
        manager.changePseudo("Michel", "a");
        assertEquals(manager.getConnectedContacts().size(), 1);
    }
}