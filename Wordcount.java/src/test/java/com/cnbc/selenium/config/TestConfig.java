package com.cnbc.selenium.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads CNBC test credentials from environment variables or
 * {@code cnbc.credentials.properties} on the classpath (copy from
 * {@code cnbc.credentials.example.properties}).
 */
public final class TestConfig {

    private static final String PROPERTIES_FILE = "cnbc.credentials.properties";

    private final String email;
    private final String password;
    private final String baseUrl;

    private TestConfig(String email, String password, String baseUrl) {
        this.email = email;
        this.password = password;
        this.baseUrl = baseUrl;
    }

    public static TestConfig load() {
        Properties fileProps = loadPropertiesFile();

        String email = firstNonBlank(
                System.getenv("CNBC_EMAIL"),
                fileProps.getProperty("cnbc.email"));
        String password = firstNonBlank(
                System.getenv("CNBC_PASSWORD"),
                fileProps.getProperty("cnbc.password"));
        String baseUrl = firstNonBlank(
                System.getenv("CNBC_BASE_URL"),
                fileProps.getProperty("cnbc.base.url"),
                "https://www.cnbc.com/");

        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "CNBC email not set. Set CNBC_EMAIL or cnbc.email in " + PROPERTIES_FILE);
        }
        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "CNBC password not set. Set CNBC_PASSWORD or cnbc.password in " + PROPERTIES_FILE);
        }

        return new TestConfig(email.trim(), password, baseUrl.trim());
    }

    private static Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try (InputStream stream = TestConfig.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + PROPERTIES_FILE, e);
        }
        return properties;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
