package org.clavardage.ChatSystem.Views;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.ContactManager;
import org.clavardage.DiscoverySystem.DiscoverySystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ContactView extends JPanel implements Observer{
    private JList contactList;
    private JButton logoutButton;
    private ArrayList<Contact> contacts;
    public ContactView(ActionListener listener) {
        this.logoutButton = new JButton("Log out");
        this.logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.actionPerformed(new ActionEvent(this, 0, "logout"));
                DiscoverySystem.getInstance().disconnect();
            }
        });

        this.contacts = new ArrayList<>();
        this.contactList = new JList(contacts.toArray());
        this.add(this.contactList);
        this.add(this.logoutButton);

        ContactManager.getInstance().addContact(new Contact("nom2", "10.69.69.68"));
    }

    @Override
    public void update(Observable observable, Object o) {
        this.contacts = (ArrayList<Contact>) o;
        this.contactList = new JList(contacts.toArray());
        this.add(this.contactList);
    }
}
