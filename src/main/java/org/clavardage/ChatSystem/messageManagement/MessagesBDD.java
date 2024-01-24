package org.clavardage.ChatSystem.messageManagement;

import java.sql.*;
import java.sql.Connection;

import org.clavardage.ChatSystem.Views.ConnectedView;
import org.clavardage.ChatSystem.exceptions.InvalidMessageTypeException;
import org.clavardage.ChatSystem.exceptions.InvalidOriginException;
import org.clavardage.ChatSystem.exceptions.UserUnobtainableException;
import org.clavardage.DiscoverySystem.Contact;

import javax.swing.*;

public class MessagesBDD {
    private static MessagesBDD instance;
    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;
    private MessagesBDD() {
        try {
            String home_dir = System.getProperty("user.home");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + home_dir + "/.cache/chatsystem/history.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessagesBDD getInstance() {
        if (MessagesBDD.instance == null) {
            MessagesBDD.instance = new MessagesBDD();
        }
        return MessagesBDD.instance;
    }

    public void recreateDatabase() {
        try {
            this.dropDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Could not drop Database");
        }
        this.createDatabase();
        try {
            this.fillDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Could not fill Database");
        }
    }

    public void dropDatabase() throws SQLException {
        Statement statement;
        statement = this.connection.createStatement();
        statement.addBatch("DROP TABLE messages;");
        statement.addBatch("DROP TABLE contacts;");
        statement.executeBatch();
        statement.close();
    }

    public void createDatabase() {
        Statement statement;
        try {
            statement = this.connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.addBatch("CREATE TABLE contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "ip VARCHAR(15) NOT NULL);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
        statement.addBatch("CREATE TABLE messages(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "message TEXT ," +
                "fileName TEXT ," +
                "fileContent BLOB ," +
                "type INTEGER NOT NULL CHECK (type IN (1,2,3)), " +
                "origin INTEGER NOT NULL CHECK (type IN (1,2))," +
                "contactId INTEGER," +
                "FOREIGN KEY (contactId) REFERENCES contacts(id));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addContact(Contact contact) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.addBatch("INSERT INTO contacts (name, ip) VALUES (\"" +
                           contact.getName() + "\", \"" + contact.getIp() + "\");");
        statement.executeBatch();
        statement.close();
    }

    public void changePseudo(Contact contact) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.addBatch("UPDATE contacts SET name=\"" + contact.getName() + "\"" +
                "WHERE ip= \"" + contact.getIp() + "\";");
        statement.executeBatch();
        statement.close();
    }

    private int getContactId(Contact contact) throws UserUnobtainableException {
        Statement statement = null;
        ResultSet res = null;
        try {
            statement = this.connection.createStatement();
            res = statement.executeQuery("SELECT id FROM contacts WHERE ip = '" + contact.getIp() + "';");
            if (res.next()) {
                int id = res.getInt("id");
                statement.close();
                if (!res.next()) {
                    return id;
                } else {
                    statement.close();
                    throw new UserUnobtainableException();
                }
            } else {
                statement.close();
                throw new UserUnobtainableException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     public void addMessage(Message msg) throws SQLException, UserUnobtainableException {
         Statement statement = this.connection.createStatement();
         switch (msg.getType()) {
             case TEXT -> {
                 int id = this.getContactId(msg.getContact());
                 statement.addBatch("INSERT INTO messages (message, type, origin, contactId) VALUES (\"" +
                         msg.getMessage() + "\", " + msg.getType().getId() + "," + msg.getOrigin().getId() +
                         "," + id + ");");
             }
             case FILE -> {}
             case TEXT_AND_FILE -> {}
         };
         statement.executeBatch();
         statement.close();
     }

     public void fillHistory(MessagesHistory history) {
         try {
             int id = this.getContactId(history.getContact());
             Statement statement = this.connection.createStatement();
             ResultSet res = statement.executeQuery("SELECT message, filename, fileContent, type, origin FROM messages WHERE contactId = " + id + ";");
             while (res.next()) {
                 switch (MessageType.fromInt(res.getInt("type"))) {
                     case FILE -> {}
                     case TEXT_AND_FILE -> {}
                     case TEXT -> {history.addMessage(
                             new Message(
                                     res.getString("message"),
                                     history.getContact(),
                                     Origin.fromInt(res.getInt("origin"))
                             )
                     );}
                 }
             }
             statement.close();
         } catch (SQLException e) {
             throw new RuntimeException(e);
         } catch (InvalidMessageTypeException e) {
             System.out.println("Message has invalid type");
         } catch (UserUnobtainableException e) {
             JOptionPane.showMessageDialog(null, "User unknown");
         } catch (InvalidOriginException e) {
             JOptionPane.showMessageDialog(null, "Message without origin in database");
         }
     }

    public void fillDatabase() throws SQLException {
        this.addContact(new Contact("Toto", "1.2.3.4"));
        this.addContact(new Contact("Frank", "1.22.3.4"));
        this.addContact(new Contact("Zoé", "1.2.33.4"));
    }
}
