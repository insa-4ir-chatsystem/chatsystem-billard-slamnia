package org.clavardage.ChatSystem.Views;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.ContactManager;
import org.clavardage.DiscoverySystem.DiscoverySystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ContactView extends JPanel implements Observer{
    private JScrollPane scrollPane;
    private JList contactListDisplay;
    private DefaultListModel contactList;
    private DiscoverySystem ds;
    private MessageView msgView;

    public ContactView(MessageView msgView) {
        this.msgView = msgView;

        this.contactList = new DefaultListModel<String>();

        this.ds = DiscoverySystem.getInstance();
        this.ds.attachObserverToContactList(this);
        this.ds.updateObservers();

        this.contactListDisplay = new JList(contactList);
        this.contactListDisplay.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.contactListDisplay.setVisibleRowCount(25);

        this.scrollPane = new JScrollPane(contactListDisplay);
        this.scrollPane.setPreferredSize(new Dimension(250, 80));

        this.add(this.scrollPane);

        ContactManager.getInstance().addContact(new Contact("AAA","IP"));
    }

    @Override
    public void update(Observable observable, Object o) {
        ArrayList<Contact> contacts = (ArrayList<Contact>) o;
        this.contactList.removeAllElements();
        for(Contact contact : contacts) {
            this.contactList.addElement(contact.getName());
        }
    }

    @Override
    protected void finalize() {
        this.ds.deleteObserver(this);
    }
}
