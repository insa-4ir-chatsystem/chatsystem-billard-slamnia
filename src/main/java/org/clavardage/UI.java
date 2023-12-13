package org.clavardage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame implements ActionListener {
    private CardLayout windowChoice;
    private Container container;
    private JPanel panel;
    public UI() {

        setTitle("Chat System");
        setSize(800,400);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){System.exit(0);}
        };

        this.windowChoice = new CardLayout();
        this.container = this.getContentPane();
        this.container.setLayout(this.windowChoice);

        ConnectionView con = new ConnectionView(this);

        container.add(con);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "login" -> {this.windowChoice.next(this.container);}
        }
    }

    public static void main(String[] args) {
        try {
            UI form = new UI();
            form.setVisible(true);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
