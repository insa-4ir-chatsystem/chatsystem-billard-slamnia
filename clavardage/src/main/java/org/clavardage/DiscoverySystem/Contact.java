package org.clavardage.DiscoverySystem;

public class Contact {
    private String name;
    private String ip;
    private ContactState state;

    public Contact(String ip) {
        this.name = "";
        this.ip = ip;
        this.state = ContactState.UNNAMED;
    }

    public Contact(String name, String ip) {
        this.name = name;
        this.ip = ip;
        this.state = ContactState.CONNECTED;
    }

    public String getIp() {
        return ip;
    }

    public ContactState getState() {
        return state;
    }

    public void setState(ContactState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean sameIP(String ip) {
        return ip.equals(this.ip);
    }
}
