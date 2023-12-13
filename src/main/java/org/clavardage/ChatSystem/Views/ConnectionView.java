package org.clavardage.ChatSystem.Views;

import org.clavardage.DiscoverySystem.DiscoverySystem;
import org.clavardage.DiscoverySystem.ExistingPseudoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionView extends JPanel {

    private final JTextField pseudoField;
    private JButton loginButton;

    public ConnectionView(ActionListener listener) {
        this.pseudoField = new JTextField("Pseudonym",15);
        this.loginButton = new JButton("Log in");
        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    DiscoverySystem.getInstance().connect(pseudoField.getText());
                    listener.actionPerformed(new ActionEvent(this, 0, "login"));
                } catch (ExistingPseudoException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });

        this.add(this.pseudoField);
        this.add(this.loginButton);
    }
}
