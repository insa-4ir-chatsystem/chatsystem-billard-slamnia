package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;

public class Message {
    private String message;
    private Contact contact;

    public Message(String message, Contact contact) {
        this.message = message;
        this.contact = contact;
    }

    public String getMessage() {
        return message;
    }

    public Contact getContact() {
        return contact;
    }
}
