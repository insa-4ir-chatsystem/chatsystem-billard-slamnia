package org.clavardage.ChatSystem.Views;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.clavardage.ChatSystem.messageManagement.Message;
import org.clavardage.ChatSystem.messageManagement.MessagesHistory;
import org.clavardage.ChatSystem.messageManagement.MessagesMgr;
import org.clavardage.ChatSystem.messageManagement.Origin;
import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.ContactManager;
import org.clavardage.DiscoverySystem.DiscoverySystem;
import org.clavardage.DiscoverySystem.NoContactFoundException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ConnectedView implements Observer {
    private JPanel panel1;
    private JButton logOutButton;
    private JTextArea messageArea;
    private JButton sendButton;
    private JList messagesListDisplay;
    private JList contactsListDisplay;
    private JScrollPane contactScrollPane;
    private DefaultListModel contactList;
    private DefaultListModel messageList;
    private MessagesMgr msgManager = MessagesMgr.getInstance();
    private MessagesHistory msgHistory;
    private DiscoverySystem ds = DiscoverySystem.getInstance();

    private Origin lastMessageOrigin = null;

    public ConnectedView(ActionListener listener) {
        this.logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listener.actionPerformed(new ActionEvent(this, 0, "logout"));
                DiscoverySystem.getInstance().disconnect();
            }
        });

        // Contact list part
        this.contactList = new DefaultListModel<String>();
        this.ds.attachObserverToContactList(this);

        this.contactsListDisplay.setModel(this.contactList);

        // Messages display part
        ConnectedView self = this;
        this.contactsListDisplay.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Contact c = getSelectedContact();
                if (c != null) {
                    msgHistory = msgManager.getHistory(c);
                    msgHistory.addObserver(self);
                    msgHistory.updateObservers();
                }
            }
        });

        this.messageList = new DefaultListModel<String>();
        this.messagesListDisplay.setModel(this.messageList);

        this.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!messageArea.getText().equals("")) {
                    Contact contact = getSelectedContact();
                    if (contact == null) {
                        JOptionPane.showMessageDialog(null, "Select contact before sending message");
                    } else {
                        String[] msgs = messageArea.getText().split("\n");
                        for (int i = msgs.length - 1; i >= 0; i--) {
                            Message msg = new Message(msgs[i], contact, Origin.LOCAL);
                            msgManager.sendMessage(msg);
                        }
                    }
                }
            }
        });
    }

    private String messagePrefix(Message msg) {
        String res;
        if (this.lastMessageOrigin == null) {
            if (msg.getOrigin() == Origin.LOCAL) {
                res = "You: ";
            } else {
                res = msg.getContact().getName() + ": ";
            }
        } else {
            if (this.lastMessageOrigin == msg.getOrigin()) {
                if (msg.getOrigin() == Origin.LOCAL) {
                    res = "     ";
                } else {
                    int length = msg.getContact().getName().length() + 2;
                    res = " ".repeat(length);
                }
            } else {
                if (msg.getOrigin() == Origin.LOCAL) {
                    res = "You: ";
                } else {
                    res = msg.getContact().getName() + ": ";
                }
            }
        }
        this.lastMessageOrigin = msg.getOrigin();
        return res;
    }

    private Contact getSelectedContact() {
        Contact res = null;
        try {
            String contactName = (String) contactsListDisplay.getSelectedValue();
            if (contactName != null) {
                res = ContactManager.getInstance().getContactByName(contactName);
            }
        } catch (NoContactFoundException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    synchronized public void update(Observable observable, Object o) {
        if (observable instanceof ContactManager) {
            ArrayList<Contact> contacts = (ArrayList<Contact>) o;
            this.contactList.removeAllElements();
            for (Contact contact : contacts) {
                this.contactList.add(0, contact.getName());
            }
        } else if (observable instanceof MessagesHistory) {
            ArrayList<Message> messages = (ArrayList<Message>) o;
            this.messageList.removeAllElements();
            this.lastMessageOrigin = null;
            for (Message message : messages) {
                this.messageList.add(0, this.messagePrefix(message) + message.getMessage());
            }
        }
    }

    public void clearObserver() {
        this.ds.deleteObserver(this);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        messageArea = new JTextArea();
        panel2.add(messageArea, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        panel2.add(sendButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        messagesListDisplay = new JList();
        messagesListDisplay.setLayoutOrientation(2);
        messagesListDisplay.setVisibleRowCount(50);
        scrollPane1.setViewportView(messagesListDisplay);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        logOutButton = new JButton();
        logOutButton.setText("Log Out");
        panel3.add(logOutButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contactsListDisplay = new JList();
        contactsListDisplay.setLayoutOrientation(2);
        contactsListDisplay.setVisibleRowCount(10);
        scrollPane2.setViewportView(contactsListDisplay);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
