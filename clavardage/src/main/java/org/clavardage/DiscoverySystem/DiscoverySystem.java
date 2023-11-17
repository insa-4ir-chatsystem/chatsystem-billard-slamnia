package org.clavardage.DiscoverySystem;

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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        contactManager.setPseudo(pseudo);
        networkManager.sendAll("p"+pseudo);
    }

    public void disconnect() {

    }

    public void changePseudo(String pseudo) throws ExistingPseudoException{

    }

    public void attachObserverToContactList() {

    }

    public void deleteObserver() {

    }
}
