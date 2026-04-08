package com.nicmsaraiva.api.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static String generateEmail() {
        return "nic" + UUID.randomUUID() + "@qa.com.br";
    }

    public static String generatePassword() {
        return "pass" + UUID.randomUUID();
    }

    public static String generateAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(ThreadLocalRandom.current().nextInt(characters.length())));
        }

        return sb.toString();
    }
}