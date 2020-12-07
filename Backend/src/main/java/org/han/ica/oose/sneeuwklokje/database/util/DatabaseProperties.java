package org.han.ica.oose.sneeuwklokje.database.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseProperties {

    private Logger LOGGER = Logger.getLogger(getClass().getName());
    private Properties properties;

    /**
     * Gets info from the properties file and sets it in the properties
     */
    public DatabaseProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("configuration.properties"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't access property file configuration.properties.properties", e);
        }
    }

    public String driver()
    {
        return properties.getProperty("driver");
    }

    public String connectionString()
    {
        return properties.getProperty("connectionString");
    }

}
