package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.ChatSystem.MessageType;
import java.io.File;

public class Message {
    private String message;
    private Contact contact;
    private File file;
    private MessageType type;

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
