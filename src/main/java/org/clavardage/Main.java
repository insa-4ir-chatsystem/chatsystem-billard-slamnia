package org.clavardage;

import org.clavardage.ChatSystem.Network.TCPSender;
import org.clavardage.ChatSystem.Network.TCPServer;
import org.clavardage.ChatSystem.Views.MainWindow;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            TCPServer.getInstance().start();
            MainWindow form = new MainWindow();
            form.setVisible(true);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void halt() {
        TCPServer.getInstance().halt();
        TCPSender.getInstance().halt();
        System.exit(0);
    }
}