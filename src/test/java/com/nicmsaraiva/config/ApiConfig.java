package com.nicmsaraiva.config;

import io.restassured.RestAssured;

import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {

    public static void configure() {
        Properties props = loadProperties();
        RestAssured.baseURI = props.getProperty("base.uri");
    }

    public static Properties loadProperties() {
        try {
            Properties props = new Properties();
            InputStream stream = ApiConfig.class.getResourceAsStream("/config.properties");
            props.load(stream);
            return props;
        } catch (Exception ex) {
            throw new RuntimeException("Fail to load config.properties", ex);
        }
    }
}