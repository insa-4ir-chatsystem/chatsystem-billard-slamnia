package org.clavardage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Exception;

public class UI extends JFrame implements ActionListener {
    JPanel panel;
    final JTextField pseudoField;
    JButton connectButton;

    public UI() {
        pseudoField = new JTextField("Pseudonym",15);
        connectButton = new JButton("Connect");

        panel = new JPanel(new GridLayout(3, 1));
        panel.add(pseudoField);
        panel.add(connectButton);

        add(panel, BorderLayout.CENTER);

        connectButton.addActionListener(this);
        setTitle("Chat System");
    }

    public void actionPerformed(ActionEvent ae) {
        String userValue = pseudoField.getText();

        if (userValue.equals("admin")) {

            NewPage page = new NewPage();

            page.setVisible(true);

            JLabel welLabel = new JLabel("Welcome "+userValue);
            page.getContentPane().add(welLabel);
        }
        else {
            System.out.println("Please enter another Pseudonym");
        }
    }
}

class LoginFormDemo {
    public static void main(String[] args) {
        try {
            UI form = new UI();
            form.setSize(300,100);
            form.setVisible(true);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}