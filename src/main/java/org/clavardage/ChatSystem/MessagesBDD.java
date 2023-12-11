package org.clavardage.ChatSystem;

import java.sql.*;
import org.clavardage.DiscoverySystem.Contact;

public class MessagesBDD {
    private static MessagesBDD instance;
    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;
    private MessagesBDD() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            this.connection = DriverManager.getConnection("");
        } catch (ClassNotFoundException e) {
            System.err.println("There's a problem with the HSQLDB package");
        } catch (SQLException e) {
            System.err.println("No Database found");
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
            this.createDatabase();
            this.fillDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Could not recreate Database");
        }
    }

    public void dropDatabase() throws SQLException {
        Statement statement;
        statement = this.connection.createStatement();
        statement.addBatch("DROP TABLE contacts;");
        statement.addBatch("DROP TABLE messages;");
        statement.executeBatch();
        statement.close();
    }

    public void createDatabase() throws SQLException {
        Statement statement;
        statement = this.connection.createStatement();
        statement.addBatch("CREATE TABLE contacts(id int primary key auto_increment, " +
                "name varchar(50) not null, " +
                "ip varchar(15) not null;");
        statement.addBatch("CREATE TABLE messages(id int primary key aute_increment, " +
                "TEXT message," +
                "VARCHAR(200) fileName," +
                "BLOB fileContent," +
                "type int not null check (type in (1,2,3)))");
        statement.executeBatch();
        statement.close();

    }

    public void addContact(Contact contact) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.addBatch("INSERT INTO contacts (name, ip) VALUES (\"" +
                           contact.getName() + "\", \"" + contact.getIp() + "\");");
        statement.executeBatch();
        statement.close();
    }

    // public void addMessage(Message msg) {
    //     Statement statement;
    //     statement = this.connection.createStatement();
    //     switch (msg.getType()) {
    //         case MessageType.TEXT -> {
    //             statement.addBatch("INSERT INTO ")
    //         };
    //         case MessageType.FILE -> {};
    //         case MessageType.TEXT_AND_FILE -> {};
    //     };
    //     statement.executeBatch();
    //     statement.close();
    // }

    public void fillDatabase() throws SQLException {
        this.addContact(new Contact("Toto", "1.2.3.4"));
        this.addContact(new Contact("Frank", "1.2.3.4"));
        this.addContact(new Contact("Zoé", "1.2.3.4"));
    }
}
