package ChatSystem;

import static org.junit.jupiter.api.Assertions.*;
import org.clavardage.ChatSystem.Network.TCPSender;

import org.clavardage.ChatSystem.Network.TCPServer;
import org.clavardage.ChatSystem.messageManagement.Message;
import org.clavardage.ChatSystem.messageManagement.MessagesHistory;
import org.clavardage.ChatSystem.messageManagement.MessagesMgr;
import org.clavardage.ChatSystem.messageManagement.Origin;
import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.DiscoverySystem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
// import org.junit.jupiter.api.BeforeEach;


public class TCPTests {
    static private TCPServer svr;
    static private TCPSender sender;
    static private DiscoverySystem ds;
    static private MessagesMgr msgMgr;

    @BeforeAll
    static public void setup() {
        TCPTests.svr = TCPServer.getInstance();
        TCPTests.svr.start();
        TCPTests.sender = TCPSender.getInstance();
        TCPTests.ds = DiscoverySystem.getInstance();
        TCPTests.msgMgr = MessagesMgr.getInstance();
    }

    @AfterAll
    static public void cleanUp() {
        TCPTests.svr.halt();
    }

    @DisplayName("sending a message test")
    @Test
    public void sendMessageTest() {
        class ContactObserver implements Observer {

            private ArrayList<Contact> contacts;
            @Override
            public void update(Observable observable, Object o) {
                if (o instanceof ArrayList<?>) {
                    this.contacts = (ArrayList<Contact>) o;
                }
            }

            public Contact getAContact() {
                return this.contacts.get(0);
            }
        }

        class MessageObserver implements Observer {
            private Message lastMessage;
            @Override
            public void update(Observable observable, Object o) {
                if (o instanceof ArrayList<?>) {
                    ArrayList<Message> messages = (ArrayList<Message>) o;
                    this.lastMessage = messages.get(messages.size()-1);
                }
            }

            public Message getLastMessage() {
                return this.lastMessage;
            }
        }
        ContactObserver contactObserver = new ContactObserver();
        MessageObserver msgObs = new MessageObserver();
        TCPTests.ds.attachObserverToContactList(contactObserver);
        assertDoesNotThrow(() -> {
            TCPTests.ds.connect("Peter");
        });
        Contact peter = contactObserver.getAContact();
        assertNotNull(peter);
        MessagesHistory hist = TCPTests.msgMgr.getHistory(peter);
        hist.addObserver(msgObs);
        Message msg = new Message("Coucou", peter, Origin.LOCAL);
        TCPTests.sender.sendMessage(msg);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Message lastMessage = msgObs.getLastMessage();
        assertNotNull(lastMessage);
        Message recievedMessage = new Message("Coucou", peter, Origin.REMOTE);
        assertEquals(recievedMessage, lastMessage);
    }

}
