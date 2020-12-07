package org.han.ica.oose.sneeuwklokje.database.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLConnection {
    public Connection getConnection() throws SQLException;
}
