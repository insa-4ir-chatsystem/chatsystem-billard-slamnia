package org.clavardage.ChatSystem.messageManagement;

import org.clavardage.ChatSystem.exceptions.InvalidMessageTypeException;

public enum MessageType {
    TEXT(1),
    FILE(2),
    TEXT_AND_FILE(3);

    private int id;

    MessageType(int id) {
        this.id = id;
    }

    public static MessageType fromInt(int id) throws InvalidMessageTypeException {
        return switch (id) {
            case 1 -> MessageType.TEXT;
            case 2 -> MessageType.FILE;
            case 3 -> MessageType.TEXT_AND_FILE;
            default -> throw new InvalidMessageTypeException();
        };
    }

    public int getId () {
        return this.id;
    }

}
