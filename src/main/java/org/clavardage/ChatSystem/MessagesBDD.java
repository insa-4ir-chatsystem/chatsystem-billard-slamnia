package org.clavardage.ChatSystem;

import java.sql.*;

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
}
