package org.clavardage.ChatSystem.Views;

import org.clavardage.ChatSystem.Message;
import org.clavardage.ChatSystem.MessagesHistory;
import org.clavardage.ChatSystem.MessagesMgr;
import org.clavardage.DiscoverySystem.Contact;
import org.clavardage.DiscoverySystem.DiscoverySystem;
import org.clavardage.DiscoverySystem.ExistingPseudoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MessageView extends JPanel implements Observer {
    private JScrollPane scrollPane;
    private JList messageListDisplay;
    private DefaultListModel messageList;
    private JTextField messageField;
    private JButton sendButton;
    private MessagesMgr msgMgr;

    public MessageView() {
        this.messageList = new DefaultListModel<String>();
//        this.msgMgr = MessagesMgr.getInstance();
//        this.msgMgr.getHistory();

        this.messageListDisplay = new JList(messageList);
        this.messageListDisplay.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.messageListDisplay.setVisibleRowCount(20);

        this.scrollPane = new JScrollPane(messageListDisplay);
        this.scrollPane.setPreferredSize(new Dimension(250, 80));

        this.messageField = new JTextField(50);

        this.sendButton = new JButton("⊳");
        this.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        this.add(this.scrollPane, BorderLayout.PAGE_START);
        this.add(this.messageField, BorderLayout.PAGE_END);
        this.add(this.sendButton, BorderLayout.PAGE_END);
    }

    // TODO à remplir
    public void loadMessagesFrom(Contact contact) {

    }

    @Override
    public void update(Observable observable, Object o) {
        ArrayList<Message> msgs = (ArrayList<Message>) o;
        this.messageList.removeAllElements();
        for(Message message : msgs) {
            this.messageList.addElement(message.getMessage());
        }
    }
}
