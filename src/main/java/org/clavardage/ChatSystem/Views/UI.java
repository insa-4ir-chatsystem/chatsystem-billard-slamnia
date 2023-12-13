package org.clavardage.ChatSystem.Views;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.ContactManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame implements ActionListener {
    private CardLayout windowChoice;
    private Container container;
    private ConnectionView connection;
    private ContactView contacts;
    public UI() {
        /** Parameters of the window **/
        setTitle("Chat System");
        setSize(800,400);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){System.exit(0);}
        };
        addWindowListener(l);

        /** Content of the window **/
        this.windowChoice = new CardLayout();
        this.container = this.getContentPane();
        this.container.setLayout(this.windowChoice);

        this.connection = new ConnectionView(this);
        this.container.add(connection);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "login" -> {
                this.contacts = new ContactView(this);
                this.container.add(contacts);
                this.windowChoice.next(this.container);
            }
            case "logout" -> {
                this.windowChoice.previous(this.container);
            }
        }
    }

    public static void main(String[] args) {
        try {
            ContactManager.getInstance().addContact(new Contact("nom","@ip"));
            UI form = new UI();
            form.setVisible(true);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
