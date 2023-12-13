package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;

import java.util.ArrayList;
import java.util.Observable;

public class MessagesHistory extends Observable {
    private Contact contact;
    private ArrayList<Message> messages;

    public MessagesHistory(Contact contact) {
        this.contact = contact;
    }

    public void addMessage(Message msg) {
        this.messages.add(msg);
        this.setChanged();
        this.notifyObservers();
    }

    public Contact getContact() {
        return this.contact;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
}
