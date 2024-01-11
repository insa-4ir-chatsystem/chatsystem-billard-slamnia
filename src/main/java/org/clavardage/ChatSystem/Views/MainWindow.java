package org.clavardage.ChatSystem.Views;

import org.clavardage.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener {
    private CardLayout windowChoice;
    private Container container;
    private ConnectionView connectionView;
    private ConnectedView connectedView;

    public MainWindow() {
        /** Parameters of the window **/
        setTitle("Chat System");
        setSize(1200,600);
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                Main.halt();
            }
        };
        addWindowListener(l);

        /** Content of the window **/
        this.windowChoice = new CardLayout();
        this.container = this.getContentPane();
        this.container.setLayout(this.windowChoice);

        this.connectionView = new ConnectionView(this);
        this.container.add(connectionView.$$$getRootComponent$$$());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "login" -> {
                this.connectedView = new ConnectedView(this);
                this.container.add(connectedView.$$$getRootComponent$$$());
                this.windowChoice.next(this.container);
            }
            case "logout" -> {
                this.windowChoice.previous(this.container);
                this.container.remove(1);
                this.connectedView.clearObserver();
                this.connectedView = null;
            }
        }
    }
}