package com.yumiko.forces.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryptor {

    private static final String algorithm = "AES";
    private static final String key = "YumikoForces@12F";

    public static String encrypt(String data) throws Exception {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);

    }

    public static String decrypt(String encryptedData) throws Exception {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted);

    }


}
