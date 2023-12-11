package org.clavardage.ChatSystem;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.ChatSystem.MessageType;
import java.io.File;

public class Message {
    private String message;
    private Contact contact;
    private File file;
    private MessageType type;

    public Message(String message, Contact contact) {
        this.message = message;
        this.contact = contact;
        this.type = MessageType.TEXT;
    }

    public Message(File file, Contact contact) {
        this.file = file;
        this.contact = contact;
        this.type = MessageType.FILE;
    }

    public Message(File file, String message, Contact contact) {
        this.file = file;
        this.message = message;
        this.contact = contact;
        this.type = MessageType.TEXT_AND_FILE;
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
}
