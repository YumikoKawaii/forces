package com.yumiko.forces.util;

import java.time.LocalDateTime;

public class Token {

    private static final long tokenDuration = 7;

    public static String generateToken(String id) throws Exception {

        return id + "." + Encryptor.encrypt(id + LocalDateTime.now().plusDays(tokenDuration).toString());

    }

    public static boolean validateToken(String token) throws Exception {

        String[] tokenParts = token.split("\\.");
        String id = tokenParts[0];
        String auth = Encryptor.decrypt(tokenParts[1]);

        if (!id.equals(auth.substring(0, id.length() - 1))) {
            throw new Exception("Invalid token");
        }

        String expiry = auth.substring(id.length(), auth.length() - 1);

        LocalDateTime expiryDate = LocalDateTime.parse(expiry);
        if (LocalDateTime.now().isAfter(expiryDate)) {
            throw new Exception("Token expired");
        }

        return true;

    }

}
