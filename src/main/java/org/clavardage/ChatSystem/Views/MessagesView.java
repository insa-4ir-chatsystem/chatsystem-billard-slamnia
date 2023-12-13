package org.clavardage.ChatSystem.Views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagesView extends JPanel {
    private JButton logoutButton;
    public MessagesView(ActionListener listener) {
        logoutButton = new JButton("Log out");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.actionPerformed(new ActionEvent(this, 0, "logout"));
            }
        });

        this.add(logoutButton);
    }
}
