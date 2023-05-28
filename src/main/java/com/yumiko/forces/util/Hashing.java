package com.yumiko.forces.util;

import java.security.MessageDigest;

public class Hashing {

    public static String hash(String data) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();

    }

    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        while (length-- != 0) {
            int character = (int)(Math.random()*chars.length());
            stringBuilder.append(chars.charAt(character));
        }
        return stringBuilder.toString();
    }


}
