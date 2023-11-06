package org.clavardage.DiscoverySystem;

import java.util.ArrayList;

public class ContactManager {
    private static ContactManager instance;

    private final ArrayList<Contact> contacts;

    private String pseudo;

    private ContactManager() {
        contacts = new ArrayList<>();
    }

    public static ContactManager getInstance() {
        if (ContactManager.instance == null) {
            return new ContactManager();
        }
        return ContactManager.instance;
    }

    public void setPseudo(String pseudo) throws Exception {
        for (Contact contact : contacts) {
            if (contact.getState() == ContactState.CONNECTED && contact.getName().equals(pseudo)) {
                throw new Exception("Pseudonym already existing");
            }
        }
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void addContact(Contact newContact) {
        boolean contactPresent = false;
        for (Contact contact : contacts) {
            if (contact.sameIP(newContact.getIp())) {
                contact.setName(newContact.getName());
                contact.setState(ContactState.CONNECTED);
                contactPresent = true;
                break;
            }
        }
        if (!contactPresent) {
            contacts.add(newContact);
        }
    }

    public void changePseudo(String name, String ip) {
        for (Contact contact : contacts) {
            if (contact.sameIP(ip)) {
                if (contact.getState() == ContactState.UNNAMED) {
                    contact.setState(ContactState.CONNECTED);
                }
                contact.setName(name);
            }
        }
    }
    public void changeState(ContactState state, String ip) {
        for (Contact contact : contacts) {
            if (contact.sameIP(ip)) {
                contact.setState(state);
            }
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
}
