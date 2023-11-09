package org.clavardage.DiscoverySystem;

public enum ContactState {
    CONNECTED, DISCONNECTED, UNNAMED;

    public String toString() {
        switch (this) {
            case CONNECTED -> {
                return "CONNECTED";
            }
            case DISCONNECTED -> {
                return "DISCONNECTED";
            }
            case UNNAMED -> {
                return "UNNAMED";
            }
            default -> {
                return "";
            }
        }
    }
}

