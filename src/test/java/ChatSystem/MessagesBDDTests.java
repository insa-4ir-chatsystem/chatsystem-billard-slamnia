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

    @BeforeEach
    public void cleanUp() {
        MessagesBDDTests.bdd.recreateDatabase();
    }

    @DisplayName("Add Contact Test")
    @Test
    public void addContact() {
        assertDoesNotThrow(() -> {
            MessagesBDDTests.bdd.addContact(new Contact("Tom", "3.4.5.6"));
        });
    }

    @DisplayName("Create Messages Test")
    @Test
    public void createMessages() {
        Contact tom = new Contact("Tom", "3.4.5.2");
        assertDoesNotThrow(() -> {
            MessagesBDDTests.bdd.addContact(new Contact("Tom", "3.4.5.6"));
            MessagesBDDTests.bdd.addMessage(new Message("Coucou", tom, Origin.LOCAL));
        });
    }
}
