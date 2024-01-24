package ChatSystem;

import static org.junit.jupiter.api.Assertions.*;

import org.clavardage.ChatSystem.messageManagement.*;
import org.junit.jupiter.api.*;
import org.clavardage.DiscoverySystem.Contact;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MessagesBDDTests {

    private static MessagesBDD bdd;
    private static MessagesMgr msgmgr;

    @BeforeAll
    public static void setUp() {
        MessagesBDDTests.bdd = MessagesBDD.getInstance();
        MessagesBDDTests.msgmgr = MessagesMgr.getInstance();
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

    @DisplayName("Add Contact Test")
    @Test
    public void changePseudo() {
        assertDoesNotThrow(() -> {
            MessagesBDDTests.bdd.addContact(new Contact("Tom", "3.4.5.6"));
            MessagesBDDTests.bdd.changePseudo(new Contact("Alain", "3.4.5.6"));
        });
    }

    @DisplayName("Create Messages Test")
    @Test
    public void createMessages() {
        Contact tom = new Contact("Tom", "3.4.5.2");
        assertDoesNotThrow(() -> {
            MessagesBDDTests.bdd.addContact(tom);
        });
        assertDoesNotThrow(() -> {
            MessagesBDDTests.bdd.addMessage(new Message("Coucou", tom, Origin.LOCAL));
        });
    }

    @DisplayName("Observer Test")
    @Test
    public void observerTest() {

        Contact tom = new Contact("Tom", "3.4.5.2");
        Message msg = new Message("Coucou", tom, Origin.LOCAL);
        class MsgObserver implements Observer {
            private int length;
            private Message msg;
            @Override
            public void update(Observable observable, Object o) {
                ArrayList<Message> messages = (ArrayList<Message>) o;
                this.length = messages.size();
                if (this.length == 1) {
                    this.msg = messages.get(0);
                }
            }

            public int getLength() {
                return this.length;
            }

            public Message getMsg() {
                return this.msg;
            }
        }
        MsgObserver msgObs = new MsgObserver();
        assertDoesNotThrow(() -> {
            MessagesBDDTests.bdd.addContact(tom);
        });
        MessagesHistory hist = MessagesBDDTests.msgmgr.getHistory(tom);
        hist.addObserver(msgObs);
        assertDoesNotThrow(() -> {
            MessagesBDDTests.msgmgr.addMessage(msg);
        });
        assertEquals(1,msgObs.getLength());
        assertEquals(msg, msgObs.getMsg());
    }
}
