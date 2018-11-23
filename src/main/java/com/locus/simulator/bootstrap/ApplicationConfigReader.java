package com.locus.simulator.bootstrap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Neophy
 */
public class ApplicationConfigReader {
    private final Properties prop;

    public ApplicationConfigReader(String propFileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop = new Properties();
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
        }
    }

    public String getProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }
}
