package org.security;

/**
 * Main.java
 * @author Heymo Vehse
 *
 * More about this little piece of code, and how to contact me, at:
 * http://www.leafraker.com/yavi/
 *
 * Created on October 9, 2006
 * Last edited on November 20, 2006
 *
 * Functionality:
 * Just a tiny demo how the VigenereCipherBean can be used
 *
 * Copyright (C) 2006 Heymo Vehse
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;

public class VigenereTesterMain {

    public VigenereTesterMain() {
    }

    public static void main(String[] args) throws Exception {
        String key = "BAUFEST";

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        StringBuffer str = new StringBuffer();

        String data = bf.readLine();
        while (!data.equals(".")) {
            str.append(new String(data.getBytes("ISO-8859-1")));
            str.append(" ");
            data = bf.readLine();
        }

        System.out.println("Text:" + str.toString());

        String encrypted = VigenereCipherBean.Cipher(str.toString(), key);
        System.out.println("\nThe encrypted secret is:\n" + encrypted);

        String decrypted = VigenereCipherBean.DeCipher(encrypted, key);
        System.out.println("\nThe decrypted secret is:\n" + decrypted);
    }

}
