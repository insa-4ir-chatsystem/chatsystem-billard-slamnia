package org.clavardage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionView extends JPanel {

    private final JTextField pseudoField;
    private JButton connectButton;

    public ConnectionView(ActionListener listener) {
        pseudoField = new JTextField("Pseudonym",15);
        connectButton = new JButton("Connect");

        this.add(pseudoField);
        this.add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.actionPerformed(new ActionEvent(this, 0, "login"));
            }
        });
    }
}
