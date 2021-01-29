package com.company;

import java.math.BigInteger;
import java.util.Arrays;

class JMDSuperEncryption {

    public static StringBuffer encrypt(String text, BigInteger key) {
        StringBuffer result = new StringBuffer();

        byte[] keyBytes = key.toByteArray();

        for (int i = 0; i < text.length(); i++) {
            if (Character.isUpperCase(text.charAt(i))) {
                char ch = (char) (((int) text.charAt(i) +
                        keyBytes[i % (keyBytes.length - 1)] - 65) % 256 + 65);
                result.append(ch);
            } else {
                char ch = (char) (((int) text.charAt(i) +
                        keyBytes[i % (keyBytes.length - 1)] - 97) % 256 + 97);
                result.append(ch);
            }
        }
        return result;
    }

    public static StringBuffer decrypt(String cipher, BigInteger key) {
        StringBuffer result = new StringBuffer();

        byte[] keyBytes = key.toByteArray();

        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte)(256 - keyBytes[i]);
        }

        for (int i = 0; i < cipher.length(); i++) {
            if (Character.isUpperCase(cipher.charAt(i))) {
                char ch = (char) (((int) cipher.charAt(i) +
                        keyBytes[i % (keyBytes.length - 1)] - 65) % 256 + 65);
                result.append(ch);
            } else {
                char ch = (char) (((int) cipher.charAt(i) +
                        keyBytes[i % (keyBytes.length - 1)] - 97) % 256 + 97);
                result.append(ch);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String originalText = "Efter att tidningen Göteborgs-Posten avslöjat att Ola Serneke skrivit inlägg under pseudonym, som kan ha gynnat hans eget bolag på Placeras börsforum, kräver nu organisationen Aktiespararna hans omedelbara avgång.";
        byte[] shiftCount = { 24, 12, 18, 6, 89, 24, 11, 1, -127 };

        BigInteger bigInt = new BigInteger(shiftCount);
        System.out.println("Key: " + bigInt);

        System.out.println("Example");
        System.out.println("Encryption");
        System.out.println("Text  : " + originalText);
        System.out.println("Key Bytes : " + Arrays.toString(bigInt.toByteArray()));
        String cipher = encrypt(originalText, bigInt).toString();
        System.out.println("Encrypted Cipher: " + cipher);
        String decryptedPlainText = decrypt(cipher, bigInt).toString();
        System.out.println("Decrypted Plain Text  : " + decryptedPlainText);
    }
}