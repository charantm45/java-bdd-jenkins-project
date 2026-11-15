package com.ctm.driver;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();
    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new IllegalStateException("config.properties not found");
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }
    private ConfigReader() {}
    public static String get(String key) { return PROPERTIES.getProperty(key); }
}
