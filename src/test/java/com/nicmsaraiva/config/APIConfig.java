package com.nicmsaraiva.config;

import io.restassured.RestAssured;

import java.io.InputStream;
import java.util.Properties;

public class APIConfig {

    private static Properties properties;

    public static void configure() {
        RestAssured.baseURI = loadProperties().getProperty("base.uri");
    }

    public static Properties loadProperties() {
        if (properties != null) {
            return properties;
        }

        try (InputStream stream = APIConfig.class.getResourceAsStream("/config.properties")) {
            if (stream == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load config.properties", ex);
        }
    }
}