package org.clavardage.ChatSystem.Views;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionView extends JPanel {

    private final JTextField pseudoField;
    private JButton loginButton;

    public ConnectionView(ActionListener listener) {
        pseudoField = new JTextField("Pseudonym",15);
        loginButton = new JButton("Log in");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.actionPerformed(new ActionEvent(this, 0, "login"));
            }
        });

        this.add(pseudoField);
        this.add(loginButton);
    }
}
