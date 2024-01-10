package org.clavardage.ChatSystem.messageManagement;

import org.clavardage.DiscoverySystem.Contact;

import java.io.File;
import java.util.Objects;

public class Message {
    private String message;
    private Contact contact;
    private File file;
    private MessageType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(message, message1.message) && Objects.equals(contact, message1.contact) && type == message1.type && origin == message1.origin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, contact, type, origin);
    }

    private Origin origin;

    public Message(String message, Contact contact, Origin origin) {
        this.message = message;
        this.contact = contact;
        this.type = MessageType.TEXT;
        this.origin = origin;
    }

    public Message(File file, Contact contact, Origin origin) {
        this.file = file;
        this.contact = contact;
        this.type = MessageType.FILE;
        this.origin = origin;
    }

    public Message(File file, String message, Contact contact, Origin origin) {
        this.file = file;
        this.message = message;
        this.contact = contact;
        this.type = MessageType.TEXT_AND_FILE;
        this.origin = origin;
    }

    public String getMessage() {
        return this.message;
    }

    public Contact getContact() {
        return this.contact;
    }

    public File getFile() {
        return this.file;
    }

    public MessageType getType() {
        return this.type;
    }

    public Origin getOrigin() {
        return this.origin;
    }
}
