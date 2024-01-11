package org.clavardage.DiscoverySystem;

import org.clavardage.ChatSystem.messageManagement.MessagesBDD;

import javax.swing.*;
import java.sql.SQLException;
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
                    contact.setState(ContactState.UNNAMED);
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
            try {
                MessagesBDD.getInstance().addContact(newContact);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Issue with database when adding contact");
                throw new RuntimeException(e);
            }
        }
        this.updateObservers();
    }

    public synchronized Contact getContactByIp(String ip) throws NoContactFoundException{
        for (Contact contact: this.contacts) {
            if (contact.sameIP(ip)) {
                return contact;
            }
        }
        throw new NoContactFoundException();
    }

    public synchronized Contact getContactByName(String name) throws NoContactFoundException{
        for (Contact contact: this.contacts) {
            if (contact.sameName(name)) {
                return contact;
            }
        }
        throw new NoContactFoundException();
    }

    public synchronized void changePseudo(String name, String ip) {
        boolean changed = false;
        for (Contact contact : contacts) {
            if (contact.sameIP(ip)) {
                contact.setName(name);
                try {
                    MessagesBDD.getInstance().changePseudo(contact);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (contact.getState() == ContactState.UNNAMED) {
                    contact.setState(ContactState.CONNECTED);
                }
                this.updateObservers();
                changed = true;
            }
        }
        if (!changed) {
            this.addContact(new Contact(name, ip));

        }

    }

    public synchronized void changeState(ContactState state, String ip) {
        boolean changed = false;
        for (Contact contact : contacts) {
            if (contact.sameIP(ip)) {
                contact.setState(state);
                changed = true;
            }
        }
        if (changed){
            this.updateObservers();
        }
    }

    public synchronized void setAllToDisconnected() {
        for (Contact contact : this.contacts) {
            contact.setState(ContactState.DISCONNECTED);
        }
        this.updateObservers();
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

    public void updateObservers() {
        this.setChanged();
        this.notifyObservers(this.contacts);
    }

    public void resetList() {
        this.contacts.clear();
        this.updateObservers();
    }
}
