package ChatSystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.clavardage.ChatSystem.*;
import org.clavardage.DiscoverySystem.Contact;

import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessagesBDDTests {

    private static MessagesBDD bdd;

    @BeforeAll
    public static void setUp() {
        MessagesBDDTests.bdd = MessagesBDD.getInstance();
    }

    @AfterEach
    public void cleanUp() {
        MessagesBDDTests.bdd.recreateDatabase();
    }

    @DisplayName("Verify Integrity of Database")
    @Test
    public void integrityCheck() {
        System.out.println("Start of the test");
        assertDoesNotThrow(() -> {
            assertNotEquals(MessagesBDDTests.bdd, null);
            MessagesBDDTests.bdd.addContact(new Contact("Tom", "3.4.5.6"));
        });
    }
}
