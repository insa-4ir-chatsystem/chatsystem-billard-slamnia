package org.clavardage.ChatSystem.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame implements ActionListener {
    private CardLayout windowChoice;
    private Container container;
    private ConnectionView connectionView;
    private ConnectedView connectedView;
    public UI() {
        /** Parameters of the window **/
        setTitle("Chat System");
        setSize(1200,600);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){System.exit(0);}
        };
        addWindowListener(l);

        /** Content of the window **/
        this.windowChoice = new CardLayout();
        this.container = this.getContentPane();
        this.container.setLayout(this.windowChoice);

        this.connectionView = new ConnectionView(this);
        this.container.add(connectionView);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "login" -> {
                this.connectedView = new ConnectedView(this);
                this.container.add(connectedView);
                this.windowChoice.next(this.container);
            }
            case "logout" -> {
                this.windowChoice.previous(this.container);
            }
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
