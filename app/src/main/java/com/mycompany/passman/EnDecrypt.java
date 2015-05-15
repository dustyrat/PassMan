package com.mycompany.passman;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EnDecrypt {
    public static String password;

    public static String generateSHA1(String message) {
        return hashString(message, "SHA-1");
    }

    private static String hashString(String message, String algorithm) {
        byte[] hashedBytes = new byte[0];
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            hashedBytes = digest.digest(message.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertByteArrayToHexString(hashedBytes);
    }

    private static byte[] hashString(String message) {
        byte[] hashedBytes = new byte[0];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA256");
            hashedBytes = digest.digest(message.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hashedBytes;
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte arrayByte : arrayBytes) {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    public static String encrypt(String keyStr, String data) {
        try {
            Key key = new SecretKeySpec(hashString(keyStr), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted =  cipher.doFinal(data.getBytes("UTF-8"));
            data = Base64.encodeToString(encrypted,Base64.DEFAULT);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String decrypt(String keyStr, String data) {
        try {
            Key key = new SecretKeySpec(hashString(keyStr), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
            data = new String(decrypted, "UTF-8");
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
