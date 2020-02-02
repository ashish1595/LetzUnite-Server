package com.gateway.server.security;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Ashish Gupta
 */

public class EdCipher {

    private final static String ALGORITHM = "AES";
    private final static String SECRET_KEY_ALGO = "PBKDF2WithHmacSHA1";
    private final static String CIPHER_ALGO = "AES/CBC/PKCS5Padding";
    private final static String CHAR_SET_NAME = "UTF-8";
    private final static int ITERATION_COUNT = 1000;
    private final static int KEY_LENGTH = 256;

    public EdCipherData encrypt(String data, String passKey) {
        EdCipherData edCipherData = null;
        try {
            byte[] bytes = data.toString().getBytes(CHAR_SET_NAME);
            return encryptData(bytes, passKey);
        } catch (Exception e) {
        }
        return edCipherData;
    }

    private EdCipherData encryptData(byte[] rawData, String passKey) throws Exception {
        int saltLength = KEY_LENGTH / 8; // same size as key output
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        SecretKeySpec skeySpec = new SecretKeySpec(getKey(passKey, salt), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParams);
        byte[] ciphertext = cipher.doFinal(rawData);
        EdCipherData edCipherData = new EdCipherData();
        edCipherData.setData(ciphertext);
        edCipherData.setIv(iv);
        edCipherData.setPassKey(passKey);
        edCipherData.setSalt(salt);
        return edCipherData;
    }

    private byte[] getKey(String passKey, byte[] salt) throws Exception {
        KeySpec keySpec = new PBEKeySpec(passKey.toCharArray(), salt,
                ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance(SECRET_KEY_ALGO);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        return keyBytes;
    }

    public String decrypt(EdCipherData edCipherData) {
        String decryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            IvParameterSpec ivParams = new IvParameterSpec(edCipherData.getIv());
            SecretKeySpec skeySpec = new SecretKeySpec(getKey(edCipherData.getPassKey(), edCipherData.getSalt()), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParams);
            byte[] plaintext = cipher.doFinal(edCipherData.getData());
            decryptedData = new String(plaintext, CHAR_SET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }
}
