package org.clavardage;

import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.DiscoverySystem;
import org.clavardage.DiscoverySystem.ExistingPseudoException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Main {

    static class ContactObserver implements Observer {

        @Override
        public void update(Observable observable, Object o) {
            ArrayList<Contact> contacts = (ArrayList<Contact>) o;
            for (Contact contact : contacts) {
                System.out.println(contact);
            }
        }
    }
    public static void main(String[] args) {
        boolean running = true;
        boolean connected = false;
        ContactObserver obs = new ContactObserver();
        DiscoverySystem ds = DiscoverySystem.getInstance();
        ds.attachObserverToContactList(obs);
        while (running) {
            System.out.println("Chose your actions: connect [c], change pseudo [p], disconnect [d], quit [q]");
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine();
            switch (choice.charAt(0)) {
                case 'c' -> {
                    System.out.println("Please enter your pseudo");
                    try {
                        ds.connect(sc.nextLine());
                        connected = true;
                    } catch (ExistingPseudoException e) {
                        System.out.println("A user already exists with this pseudo");
                    }
                }
                case 'p' -> {
                    if (connected) {
                        System.out.print("Please enter your pseudo: ");
                        try {
                            ds.changePseudo(sc.nextLine());
                        } catch (ExistingPseudoException e) {
                            System.out.println("A user already exists with this pseudo");
                        }
                    } else {
                        System.out.println("You are not yet connected");
                    }
                }
                case 'd' -> {
                    if (connected) {
                        ds.disconnect();
                    } else {
                        System.out.println("You were already disconnected");
                    }
                }
                case 'q' -> {
                    running = false;
                    if (connected) {
                        ds.disconnect();
                    }
                }
                default -> {
                    System.out.println("Your command was not recognized");
                }
            }
        }
    }
}