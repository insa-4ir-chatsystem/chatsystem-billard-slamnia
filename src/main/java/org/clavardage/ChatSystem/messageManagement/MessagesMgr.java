package org.clavardage.ChatSystem.messageManagement;

import org.clavardage.ChatSystem.exceptions.UserUnobtainableException;
import org.clavardage.DiscoverySystem.Contact;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessagesMgr {
    static private MessagesMgr instance = null;
    private MessagesBDD msgBdd = null;

    private ArrayList<MessagesHistory> histories;

    static public MessagesMgr getInstance() {
        if (MessagesMgr.instance == null) {
            MessagesMgr.instance = new MessagesMgr();
        }
        return MessagesMgr.instance;
    }
    private MessagesMgr() {
        this.histories = new ArrayList<>();
        this.msgBdd = MessagesBDD.getInstance();
    }

    public MessagesHistory getHistory(Contact contact) {
        for (MessagesHistory hist: this.histories) {
            if (hist.getContact().sameIP(contact.getIp())) {
                return hist;
            }
        }
        MessagesHistory history = new MessagesHistory(contact);
        this.histories.add(history);
        this.msgBdd.fillHistory(history);
        return history;
    }

    public void addMessage(Message msg) {
        MessagesHistory hist = this.getHistory(msg.getContact());
        hist.addMessage(msg);
        try {
            this.msgBdd.addMessage(msg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (UserUnobtainableException e) {
            System.out.println("User not found");
        }
    }

}
