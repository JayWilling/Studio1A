package com.example.studiomerge.lib;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    private static final String TAG = "HASH";

    public Hash() {}

    /**
     * Return the hashed version of the given String.
     *
     * Hash the given String using SHA-256 and return its hexadecimal
     * representation. Return null when the hashing operation is not
     * found.
     *
     * @param s the String to be hashed
     * @return  hexadecimal representation of the hashed String
     */
    public String hash(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    s.getBytes(StandardCharsets.UTF_8));  // Requires Android 4.4 (KitKat)
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "MessageDigest algorithm not found.", e);
        }
        return null;
    }

    /**
     * Return the hexadecimal representation of the given byte array.
     *
     * @param bytes byte array to be converted to hexadecimal
     * @return      hexadecimal representation of the converted byte array
     */
    private String bytesToHex(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
