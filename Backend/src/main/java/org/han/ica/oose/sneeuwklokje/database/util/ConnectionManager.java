package org.han.ica.oose.sneeuwklokje.database.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager implements SQLConnection {

    private final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());
    private DatabaseProperties databaseProperties = new DatabaseProperties();

    /**
     * Returns the database connection
     * @return Connection
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(databaseProperties.driver());
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Can't load JDBC Driver " + databaseProperties.driver(), e);
        }
        return DriverManager.getConnection(databaseProperties.connectionString());
    }
}
