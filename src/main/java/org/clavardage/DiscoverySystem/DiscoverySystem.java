package org.clavardage.DiscoverySystem;

import java.util.Observer;

public class DiscoverySystem {
    private NetworkManager networkManager;

    private ContactManager contactManager;

    private boolean connected = false;

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
        networkManager.ignoreNextConnection();
        networkManager.sendAll("c");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        contactManager.setPseudo(pseudo);
        networkManager.sendAll("p"+pseudo);

        this.connected = true;
    }

    public void disconnect() {
        networkManager.sendAll("e");
        contactManager.setAllToDisconnected();
        this.connected = false;
    }

    public void changePseudo(String pseudo) throws ExistingPseudoException {
        contactManager.setPseudo(pseudo);
        networkManager.sendAll("p" + pseudo);
    }

    public void attachObserverToContactList(Observer o) {
        this.contactManager.addObserver(o);
    }

    public void updateObservers() {
        this.contactManager.updateObservers();
    }

    public void deleteObserver(Observer o) {
        this.contactManager.deleteObserver(o);
    }

    public void clearObservers() {
        this.contactManager.deleteObservers();
    }

    public static void release() {
        if (DiscoverySystem.instance != null && DiscoverySystem.instance.connected) {
            DiscoverySystem.instance.disconnect();
        }
        DiscoverySystem.instance = null;
        ContactManager.release();
        NetworkManager.release();
    }
}
