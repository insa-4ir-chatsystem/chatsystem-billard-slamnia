package org.clavardage.ChatSystem;

public enum Origin {
    LOCAL(1),
    REMOTE(2);

    private int id;

    Origin (int id) {
        this.id = id;
    }

    public static Origin fromInt(int id) throws InvalidOriginException {
        return switch (id) {
            case 1 -> Origin.LOCAL;
            case 2 -> Origin.REMOTE;
            default -> throw new InvalidOriginException();
        };
    }

    public int getId () {
        return this.id;
    }

}
