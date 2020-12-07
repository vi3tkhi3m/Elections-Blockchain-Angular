package org.han.ica.oose.sneeuwklokje.database;

import org.han.ica.oose.sneeuwklokje.database.util.SQLConnection;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoImpl implements Dao {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    public SQLConnection sqlConnection;

    public Connection con;
    public PreparedStatement stmt;
    public ResultSet rs;

    /**
     * Closes the database connection
     */
    public void closeConnection() {
        try {
            if (con != null) con.close();
            if (stmt != null) stmt.close();
            if (rs != null) con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Can't close connection with database. ", e);
        }
    }
}
