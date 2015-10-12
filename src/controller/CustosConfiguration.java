package controller;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.files.Closer;
import hochberger.utilities.properties.LoadProperties;
import hochberger.utilities.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CustosConfiguration extends SessionBasedObject {

    private static final String CONFIGURATION_FILE_NAME = "custos.cfg";
    private static final String FILE_SEPARATOR = "/";
    private static final String USER_DIR = "user.dir";

    private Properties properties;

    public CustosConfiguration(final BasicSession session) {
        super(session);
    }

    public void load() {
        final String configFilePath = configFilePath();
        final File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (final IOException e) {
                logger().fatal("Unable to create custos configuration file", e);
            }
        }
        try {
            this.properties = LoadProperties.fromExtern(configFilePath);
        } catch (final IOException e) {
            logger().fatal("Config file not found.");
        }
    }

    private String configFilePath() {
        final String userDirectoryPath = System.getProperty(USER_DIR);
        final String configFilePath = userDirectoryPath + FILE_SEPARATOR + CONFIGURATION_FILE_NAME;
        return configFilePath;
    }

    public String getValueFor(final String key, final String defaultValue) {
        if (!this.properties.containsKey(key)) {
            this.properties.put(key, defaultValue);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(configFilePath());
                this.properties.store(fileOutputStream, Text.empty());
            } catch (final IOException e) {
                logger().error("Unable to write properties");
            } finally {
                Closer.close(fileOutputStream);
            }
        }
        return this.properties.getProperty(key);
    }

    public int getValueForAsInt(final String key, final int defaultValue) {
        if (!this.properties.containsKey(key)) {
            this.properties.put(key, defaultValue);
        }
        try {
            return Integer.valueOf(this.properties.getProperty(key));
        } catch (final NumberFormatException e) {
            logger().error("Invalid value for " + key + ". Using default value " + defaultValue);
            return defaultValue;
        }
    }

    public void setValueFor(final String key, final String value) {
        this.properties.put(key, value);
    }

    public void setValueFor(final String key, final int value) {
        setValueFor(key, String.valueOf(value));
    }
}
