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
/* Class: EnDecrypt
 * Purpose: Holds session password, Encrypt/Decrypt and hashing
 */
public class EnDecrypt {
    public static String password;

    /* Method: generateSHA1
     * Purpose: Sets hashing SHA-1 algorithm
     * Parameters: message - String data to be hashed
     * Returns: String
    */
    public static String generateSHA1(String message) {
        return hashString(message, "SHA-1");
    }

    /* Method: hashString
     * Purpose: Hashes message with with given algorithm
     * Parameters: message - String data to be hashed
     *             algorithm - String hashing algorithm
     * Returns: String
    */
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

    /* Method: hashString
     * Purpose: Hashes message with with SHA-256 algorithm
     * Parameters: message - String data to be hashed
     * Returns: byte[]
    */
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

    /* Method: convertByteArrayToHexString
     * Purpose: converts hashed byte[] into hex String
     * Parameters: arrayBytes - byte[] to be converted into hex String
     * Returns: String
    */
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte arrayByte : arrayBytes) {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    /* Method: encrypt
     * Purpose: Encrypts data with given key with AES algorithm
     * Parameters: keyStr - String password
     *             data - String account data
     * Returns: String
    */
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

    /* Method: decrypt
     * Purpose: Decrypts data with given key with AES algorithm
     * Parameters: keyStr - String password
     *             data - String account data
     * Returns: String
    */
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
