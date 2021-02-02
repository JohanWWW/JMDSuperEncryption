package com.company;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;

class JMDSuperEncryption {

    public static StringBuffer encrypt(String text, BigInteger key) {
        var result = new StringBuffer();

        byte[] keyBytes = key.toByteArray();

        distortKey(keyBytes);

        if (keyBytes.length > text.length()) {
            Character[] distinctChars = distinctChars(text.toCharArray());
            // Add salt
            var random = new SecureRandom();
            int missingCharsCount = keyBytes.length - text.length();
            for (int i = 0; i < missingCharsCount; i++) {
                int randomIndex = random.nextInt(distinctChars.length);
                text += distinctChars[randomIndex];
            }
        }

        for (int i = 0; i < text.length(); i++) {
            char ch = (char) (((int) text.charAt(i) + keyBytes[i % (keyBytes.length - 1)]));
            result.append(ch);
        }

        return result;
    }

    public static StringBuffer decrypt(String cipher, BigInteger key) {
        var result = new StringBuilder();

        byte[] keyBytes = key.toByteArray();

        distortKey(keyBytes);

        for (int i = 0; i < cipher.length(); i++) {
            char ch = (char) (((int) cipher.charAt(i) - keyBytes[i % (keyBytes.length - 1)]));
            result.append(ch);
        }

        // Remove salt
        return new StringBuffer(result.substring(0, keyBytes[0]));
    }

    /**
     * Shifts each byte in the key by the length of the string (first index).
     * Alternates between (+) and (-).
     * @param key byte representation of the key
     */
    private static void distortKey(byte[] key) {
        for (int i = 1; i < key.length; i++) {
            if (i % 2 == 0)
                key[i] += key[0];
            else
                key[i] -= key[0];
        }
    }

    private static Character[] distinctChars(char[] array) {
        var set = new HashSet<Character>();
        for (char c : array) {
            set.add(c);
        }
        return set.toArray(Character[]::new);
    }

    private static BigInteger generateKey(int keyLength, String plainText) {
        var random = new SecureRandom();

        byte[] bytes = new byte[keyLength];
        bytes[0] = (byte) plainText.length();
        for (int i = 1; i < keyLength; i++) {
            bytes[i] = (byte) (random.nextInt(255) - 128); // -128 to 127
        }

        return new BigInteger(bytes);
    }

    public static void main(String[] args) {
        String originalText = "Testing JMDSuperEncryption!";

        BigInteger key = generateKey(128, originalText);

        System.out.println("Key: " + key);
        System.out.println("Key Bytes: " + Arrays.toString(key.toByteArray()));
        System.out.println();
        System.out.println("Encryption");
        System.out.println("Text: " + originalText);

        String cipher = encrypt(originalText, key).toString();
        System.out.println("Encrypted Cipher: " + cipher);

        /*byte[] keyBytes = key.toByteArray();
        keyBytes[0] = 27;
        key = new BigInteger(keyBytes);*/

        String decryptedPlainText = decrypt(cipher, key).toString();
        System.out.println("Decrypted Plain Text: " + decryptedPlainText);
    }
}