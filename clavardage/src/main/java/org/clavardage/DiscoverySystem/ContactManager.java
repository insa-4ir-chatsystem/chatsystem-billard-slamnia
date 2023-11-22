package org.clavardage.DiscoverySystem;

import java.util.ArrayList;
import java.util.Observable;

public class ContactManager extends Observable {
    private static ContactManager instance;

    private final ArrayList<Contact> contacts;

    private String pseudo;

    private ContactManager() {
        contacts = new ArrayList<>();
    }


    public synchronized static void release() {
        ContactManager.instance = null;
    }

    public synchronized static ContactManager getInstance() {
        if (ContactManager.instance == null) {
            ContactManager.instance = new ContactManager();
        }
        return ContactManager.instance;
    }

    public void setPseudo(String pseudo) throws ExistingPseudoException {
        for (Contact contact : contacts) {
//            System.out.println(contact.getName());
            if (contact.getState() == ContactState.CONNECTED && contact.getName().equals(pseudo)) {
                throw new ExistingPseudoException("Pseudonym already existing");
            }
        }
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public synchronized void addContact(Contact newContact) {
        boolean contactPresent = false;
        for (Contact contact : contacts) {
            if (contact.sameIP(newContact.getIp())) {
                if (newContact.getName().isEmpty()) {
                    if (contact.getName().isEmpty()) {
                        contact.setState(ContactState.UNNAMED);
                    } else {
                        contact.setState(ContactState.CONNECTED);
                    }
                } else {
                    contact.setState(ContactState.CONNECTED);
                    contact.setName(newContact.getName());
                }
                contactPresent = true;
                break;
            }
        }
        if (!contactPresent) {
            contacts.add(newContact);
            this.setChanged();
            this.notifyObservers(this.contacts);
        }
    }

    public synchronized void changePseudo(String name, String ip) {
        boolean changed = false;
        for (Contact contact : contacts) {
            if (contact.sameIP(ip)) {
                contact.setName(name);
                if (contact.getState() == ContactState.UNNAMED) {
                    contact.setState(ContactState.CONNECTED);
                } else {
                    this.setChanged();
                    this.notifyObservers(this.contacts);
                }
                changed = true;
            }
        }
        if (!changed) {
            this.addContact(new Contact(name, ip));
        }
    }

    public synchronized void changeState(ContactState state, String ip) {
        for (Contact contact : contacts) {
            if (contact.sameIP(ip)) {
                contact.setState(state);
                this.setChanged();
                this.notifyObservers(this.contacts);
            }
        }
    }

    public synchronized void setAllToDisconnected() {
        for (Contact contact : this.contacts) {
            contact.setState(ContactState.DISCONNECTED);
        }
    }

    public ArrayList<Contact> getConnectedContacts() {
        ArrayList<Contact> result = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getState() == ContactState.CONNECTED) {
                result.add(contact);
            }
        }
        return result;
    }

    public void resetList() {
        this.contacts.clear();
        this.setChanged();
        this.notifyObservers(this.contacts);
    }
}
