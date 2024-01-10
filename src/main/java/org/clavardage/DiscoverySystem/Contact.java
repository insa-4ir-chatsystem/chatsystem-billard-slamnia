package org.clavardage.DiscoverySystem;

import java.util.Objects;

public class Contact {
    private String name;
    private String ip;
    private ContactState state;

    public Contact(String ip) {
        this.name = "";
        this.ip = cleanIp(ip);
        this.state = ContactState.UNNAMED;
    }

    public Contact(String name, String ip) {
        this.name = name;
        this.ip = cleanIp(ip);
        this.state = ContactState.CONNECTED;
    }

    public String getIp() {
        return ip;
    }

    public ContactState getState() {
        return state;
    }

    private String cleanIp(String ip) {
        return ip.replace("/", "");
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

    public boolean equals(Contact o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return name.equals(o.name) && ip.equals(o.ip) && state == o.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ip, state);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", state=" + state +
                '}';
    }
}
