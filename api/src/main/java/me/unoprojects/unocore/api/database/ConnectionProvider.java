package me.unoprojects.unocore.api.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    Connection getConnection() throws SQLException;

    void connect();

    void disconnect();
}
