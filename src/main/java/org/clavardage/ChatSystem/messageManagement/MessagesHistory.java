package org.clavardage.ChatSystem.messageManagement;

import org.clavardage.DiscoverySystem.Contact;

import java.util.ArrayList;
import java.util.Observable;

public class MessagesHistory extends Observable {
    private Contact contact;
    private ArrayList<Message> messages;

    public MessagesHistory(Contact contact) {
        this.contact = contact;
        this.messages = new ArrayList<>();
    }

    synchronized public void addMessage(Message msg) {
        this.messages.add(msg);
        this.updateObservers();
    }

    public void updateObservers() {
        this.setChanged();
        this.notifyObservers(this.messages);
    }

    public Contact getContact() {
        return this.contact;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

}
