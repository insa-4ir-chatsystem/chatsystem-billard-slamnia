package org.clavardage.ChatSystem.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestUI extends JFrame implements ActionListener {
    private CardLayout windowChoice;
    private Container container;
    private TestConnectionView testConnectionView;
    private TestConnectedView testConnectedView;

    public TestUI() {
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

        this.testConnectionView = new TestConnectionView(this);
        this.container.add(testConnectionView.$$$getRootComponent$$$());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "login" -> {
                this.testConnectedView = new TestConnectedView(this);
                this.container.add(testConnectedView.$$$getRootComponent$$$());
                this.windowChoice.next(this.container);
            }
            case "logout" -> {
                this.windowChoice.previous(this.container);
                this.testConnectedView.clearObserver();
            }
        }
    }

    public static void main(String[] args) {
        try {
            TestUI form = new TestUI();
            form.setVisible(true);


        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}