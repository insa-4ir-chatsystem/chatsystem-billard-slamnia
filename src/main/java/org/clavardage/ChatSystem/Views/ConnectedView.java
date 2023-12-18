package org.clavardage.ChatSystem.Views;

import org.clavardage.DiscoverySystem.DiscoverySystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectedView extends JPanel{
    private JButton logoutButton;
    private ContactView contactView;
    private MessageView messageView;

    public ConnectedView(ActionListener listener) {
        this.logoutButton = new JButton("Log out");
        this.logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.actionPerformed(new ActionEvent(this, 0, "logout"));
                DiscoverySystem.getInstance().disconnect();
            }
        });

        this.messageView = new MessageView();

        this.contactView = new ContactView(this.messageView);

        this.add(this.logoutButton, BorderLayout.PAGE_START);
        this.add(this.contactView, BorderLayout.LINE_START);
        this.add(this.messageView, BorderLayout.CENTER);
    }
}
