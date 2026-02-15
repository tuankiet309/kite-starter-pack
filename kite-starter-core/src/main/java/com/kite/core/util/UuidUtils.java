package com.kite.core.util;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class UuidUtils {
    private static final String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private UuidUtils() {
        throw new AssertionError("No instances allowed");
    }

    // --- UUID ---

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String uuidNoDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // --- Fast Random ---

    public static String random(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be > 0");
        }

        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }

        return sb.toString();
    }

    // --- Secure Random ---

    public static String secureRandom(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be > 0");
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(SECURE_RANDOM.nextInt(ALPHANUMERIC.length())));
        }

        return sb.toString();
    }
}
