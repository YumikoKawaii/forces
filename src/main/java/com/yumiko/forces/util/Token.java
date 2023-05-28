package com.yumiko.forces.util;

import java.time.LocalDateTime;

public class Token {

    private static final long tokenDuration = 7;

    public static String generateToken(String id) throws Exception {

        return id + "." + Encryptor.encrypt(id + LocalDateTime.now().plusDays(tokenDuration).toString());

    }

    public static String extractId(String token) throws Exception {

        String[] tokenParts = token.split("\\.");
        return tokenParts[0];

    }

    public static boolean validateToken(String token) {

        String[] tokenParts = token.split("\\.");

        String id = tokenParts[0];
        String auth = null;

        try {
            auth = Encryptor.decrypt(tokenParts[1]);
        } catch (Exception e) {
            return false;
        }

        if (!id.equals(auth.substring(0, id.length()))) {
            return false;
        }

        String expiry = auth.substring(id.length());
        LocalDateTime expiryDate = LocalDateTime.parse(expiry);
        return !LocalDateTime.now().isAfter(expiryDate);

    }

}
