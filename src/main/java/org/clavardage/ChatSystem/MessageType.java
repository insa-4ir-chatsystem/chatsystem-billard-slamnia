package org.clavardage.ChatSystem;

public enum MessageType {
    TEXT(1),
    FILE(2),
    TEXT_AND_FILE(3);

    private int id;

    public MessageType(int id) {
        this.id = id;
    }

    public static MessageType fromInt(int id) throws InvalidMessageTypeException{
        return switch (id) {
            case 1 -> UserType.Voluntary;
            case 2 -> UserType.Asker;
            case 3 -> UserType.Checker;
            default -> throw new InvalidMessageTypeException;
        };
    }

    public int getId () {
        return this.id;
    }

}
