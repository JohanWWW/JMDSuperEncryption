package com.company;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;

class JMDSuperEncryption {

    public static StringBuffer encrypt(String text, BigInteger key) {
        var result = new StringBuffer();

        byte[] keyBytes = key.toByteArray();

        if (keyBytes.length > text.length()) {
            Character[] distinctChars = distinctChars(text.toCharArray());
            // Add salt
            for (int i = 0; i < keyBytes.length - text.length(); i++) {
                var random = new SecureRandom();
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

        for (int i = 0; i < cipher.length(); i++) {
            char ch = (char) (((int) cipher.charAt(i) - keyBytes[i % (keyBytes.length - 1)]));
            result.append(ch);
        }

        // Remove salt
        return new StringBuffer(result.substring(0, keyBytes[0]));
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
            bytes[i] = (byte) (random.nextInt(255) - 128);
        }

        return new BigInteger(bytes);
    }

    public static void main(String[] args) {
        String originalText = "Password123";

        BigInteger key = generateKey(100, originalText);

        System.out.println("Key: " + key);
        System.out.println("Key Bytes: " + Arrays.toString(key.toByteArray()));
        System.out.println();
        System.out.println("Encryption");
        System.out.println("Text: " + originalText);
        String cipher = encrypt(originalText, key).toString();
        System.out.println("Encrypted Cipher: " + cipher);
        String decryptedPlainText = decrypt(cipher, key).toString();
        System.out.println("Decrypted Plain Text : " + decryptedPlainText);
    }
}