package org.basicsamples;
/** @Author: Libro de JAVA SERIES
 * @Description: Esta clase muestra la utilizacion de StringTokenizer
 *               Basicamente, este hereda de Elements y devuelve
 *               los elementos separados en tokens.
 */

import java.util.*;

class decodeData {
    private String delim;

    public decodeData(String delm) {
        delim = new String(delm);
    }

    public decodeData() {
        delim = new String(" ");
    }

    public void setDelim(String delm) {
        delim = new String(delm);
    }

    public String getDelim() {
        return delim;
    }

    public StringTokenizer decode(String msg) {
        /**
         * El constructor basico se le manda le msg, el string con los
         * delimitadores y se le puede mandar un boolean especificando si se
         * desea o no que los delimitadores vuelvan como tokens. Por defecto es
         * false.
         */
        StringTokenizer strT = new StringTokenizer(msg, delim);
        return strT;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usese: java decodeData \"msg\"  para testar.");
            System.exit(0);
        }
        decodeData ddt = new decodeData("\n\r ");
        StringTokenizer str = ddt.decode(args[0]);
        while (str.hasMoreTokens())
            System.out.println(str.nextToken());
    }

}
