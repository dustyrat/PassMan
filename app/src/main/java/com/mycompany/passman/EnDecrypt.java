package com.mycompany.passman;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EnDecrypt {
    public static String password;

    //TODO expand/shave key
    public static String encrypt(byte[] keyStr, String data) {
        try {
            Key key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            data =  Arrays.toString(cipher.doFinal(data.getBytes()));
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String decrypt(byte[] keyStr, String data) {
        try {
            Key key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            data =  Arrays.toString(cipher.doFinal(data.getBytes()));
        } catch (IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
