/**
 * VigenereCipherBean.java
 * @author Heymo Vehse
 *
 * More about this little piece of code, and how to contact me, at:
 * http://www.leafraker.com/yavi/
 *
 * Created on October 9, 2006
 * Last edited on November 20, 2006
 *
 * Functionality:
 * Encrypt and decrypt a string using the Vigenere Algorithm
 *
 * Copyright (C) 2006 Heymo Vehse
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

package org.security;

public class VigenereCipherBean {

    /** Creates a new instance of VigenereCipherBean */
    public VigenereCipherBean() {
    }

    /**
     * Encrypt a String using the Vigenere Algorithm
     * 
     * @param secret
     *            the string that needs to be encrypted
     * @param key
     *            the key used for the encryption
     * @return the encrypted string
     */
    public static final String Cipher2(String secret, String key) {
        String encrypted = "";

        // Loop through all characters of the secret
        for (int i = 0; i < secret.length(); i++) {
            // Get the current character of the key
            String currKey = String.valueOf(key.charAt(i % (key.length())));

            // Get the numeric value of the current key character
            int hash = currKey.hashCode() - 31;

            // Shift the character of the secret; rotate if necessary
            int n = secret.charAt(i) + hash;
            if (n > 126)
                n -= 95;

            // Append to the ciphered string
            encrypted += (char) n;
        }

        return encrypted;
    }

    /**
     * Encrypt a String using the Vigenere Algorithm
     * 
     * @param secret
     *            the string that needs to be encrypted
     * @param key
     *            the key used for the encryption
     * @return the encrypted string
     */
    public static final String Cipher(String secret, String key) {
        String encrypted = "";

        secret = secret.toUpperCase();

        int counter = 0;
        // Loop through all characters of the secret
        for (int i = 0; i < secret.length(); i++) {

            char letter = key.charAt(counter % (key.length()));

            // System.out.print("."+(int)letter+".");

            int initial = 65; // 32; // 65
            int end = 90; // 126; // 90
            int siz = end - initial + 1;

            char finalletter;

            if (initial <= secret.charAt(i) && secret.charAt(i) <= end) {
                // Shift the character of the secret; rotate if necessary
                int n = (int) secret.charAt(i) + ((int) letter - initial);
                counter++;
                if (n > end) {

                    n = (n - initial) % siz;
                    n = n + initial;

                }

                System.out.print("[" + (int) secret.charAt(i) + "->" + n + "]");

                finalletter = (char) n;
            } else {
                finalletter = secret.charAt(i);
            }

            // Append to the ciphered string
            encrypted += (char) finalletter;
        }

        return encrypted;
    }

    /**
     * Decrypt a String using the Vigenere Algorithm
     * 
     * @param encrypted
     *            the encrypted string that needs to be decrypted
     * @param key
     *            the key used for the encryption
     * @return the decrypted string
     */
    public static final String DeCipher(String encrypted, String key) {
        String plaintext = "";

        int counter = 0;
        // Loop through all characters of the secret
        for (int i = 0; i < encrypted.length(); i++) {

            char letter = key.charAt(counter % (key.length()));

            // System.out.print("."+(int)letter+".");
            System.out.print("[" + (int) encrypted.charAt(i) + "]");

            int initial = 65; // 32; // 65
            int end = 90; // 126; // 90
            int siz = end - initial + 1;

            char finalletter;

            if (initial <= encrypted.charAt(i) && encrypted.charAt(i) <= end) {
                // Shift the character of the secret; rotate if necessary
                int n = (int) encrypted.charAt(i) - ((int) letter - initial);
                counter++;
                if (n < initial) {

                    n = (end - (initial - n));
                    n = n + 1;

                }

                System.out.print("[" + (int) encrypted.charAt(i) + "]");

                finalletter = (char) n;
            } else {
                finalletter = encrypted.charAt(i);
            }

            // Append to the ciphered string
            plaintext += (char) finalletter;
        }

        return plaintext;
    }

    public static final String DeCipher2(String encrypted, String key) {
        String secret = "";

        // Loop through all characters of the encrypted string
        for (int i = 0; i < encrypted.length(); i++) {
            // Get the current character of the key
            String currKey = String.valueOf(key.charAt(i % (key.length())));

            // Get the numeric value of the current key character
            int hash = currKey.hashCode() - 31;

            // Shift the character of the encrypted string; rotate if necessary
            int n = encrypted.charAt(i) - hash;
            if (n < 32)
                n += 95;

            // Append to the de-ciphered secret
            secret += (char) n;
        }

        return secret;
    }

}
