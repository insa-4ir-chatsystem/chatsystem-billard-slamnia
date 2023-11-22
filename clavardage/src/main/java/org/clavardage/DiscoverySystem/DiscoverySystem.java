package org.clavardage.DiscoverySystem;

import java.util.Observer;

public class DiscoverySystem {
    private NetworkManager networkManager;

    private ContactManager contactManager;

    private static DiscoverySystem instance;

    public static DiscoverySystem getInstance(int port) {
        if (DiscoverySystem.instance == null) {
            DiscoverySystem.instance = new DiscoverySystem(port);
        }
        return DiscoverySystem.instance;
    }

    public static DiscoverySystem getInstance() {
        if (DiscoverySystem.instance == null) {
            DiscoverySystem.instance = new DiscoverySystem();
        }
        return DiscoverySystem.instance;
    }

    private DiscoverySystem() {
        this.networkManager = NetworkManager.getInstance();
        this.contactManager = ContactManager.getInstance();
    }

    private DiscoverySystem(int port) {
        this.networkManager = NetworkManager.getInstance(port);
        this.contactManager = ContactManager.getInstance();
    }

    public void connect(String pseudo) throws ExistingPseudoException{
        networkManager.sendAll("c");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        contactManager.setPseudo(pseudo);
        networkManager.sendAll("p"+pseudo);
    }

    public void disconnect() {
        networkManager.sendAll("e");
        contactManager.setAllToDisconnected();
    }

    public void changePseudo(String pseudo) throws ExistingPseudoException{
        contactManager.setPseudo(pseudo);
    }

    public void attachObserverToContactList(Observer o) {
        this.contactManager.addObserver(o);
    }

    public void deleteObserver(Observer o) {
        this.contactManager.deleteObserver(o);
    }

    public void clearObservers() {
        this.contactManager.deleteObservers();
    }

    public static void release() {
        DiscoverySystem.instance = null;
        ContactManager.release();
        NetworkManager.release();
    }
}
