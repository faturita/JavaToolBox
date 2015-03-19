package org.basicsamples;
/**
 * InputStream counter...
 * 
 */

import java.io.*;

public class BlankCounter {
    public static void main(String[] args) throws IOException {
        // Se declara un objeto InputStream
        InputStream in;
        if (args.length == 0)
            // Si no se especifico nada lee de la entrada de la terminal
            in = System.in;
        else
            // Abre el archivo especificado en la linea de comandos
            in = new FileInputStream(args[0]);

        int ch;
        int total;
        int spaces = 0;

        // Cuenta los espacios en blanco utilizando metodo estatico de Character
        for (total = 0; (ch = in.read()) != -1; total++) {
            if (Character.isSpace((char) ch))
                spaces++;
        }

        System.out.println(total + " chars, " + spaces + " spaces.");
    }
}
