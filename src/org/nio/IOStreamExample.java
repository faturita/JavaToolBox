package org.nio;
/** pag 204 */
import java.io.*;

class Translate {
    public static void main(String[] args) {
        InputStream in = System.in;
        OutputStream out = System.out;

        if (args.length != 2)
            error("Debe proporcionar argumentos./a");
        String from = args[0], to = args[1];
        int ch, i;
        if (from.length() != to.length())
            error("de y a deben tener la misma longitud");
        try {
            while ((ch = in.read()) != -1) {
                if ((i = from.indexOf(ch)) != -1)
                    out.write(to.charAt(i));
                else
                    out.write(ch);
            }
        } catch (IOException e) {
            error("Excepcion E/S: " + e);
        }
    }

    public static void error(String err) {
        System.err.print("Traducir: " + err);
        System.exit(1);
    }
}
