package com.nicmsaraiva.api.utils;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    public static String generateEmail() {
        return "nic" + UUID.randomUUID() + "@qa.com.br";
    }

    public static String generatePassword() {
        return "pass" + UUID.randomUUID();
    }

    public static String generateAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(characters.charAt(random.nextInt(characters.length()))))
                .collect(Collectors.joining());
    }
}
