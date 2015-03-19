package org.basicsamples;
/** Pag 210 */
import java.io.*;

class FindChar {
    public static void main(String[] args) throws Exception {
        if (args.length != 2)
            throw new Exception("Se necesita caracter y archivo as parameters.");
        int match = args[0].charAt(0);
        FileInputStream fileIn = new FileInputStream(args[1]);
        LineNumberInputStream in = new LineNumberInputStream(fileIn);
        int ch;
        while ((ch = in.read()) != -1) {
            if (ch == match) {
                System.out.println("'" + (char) ch + "' en linea:"
                        + in.getLineNumber());
                System.exit(0);
            }
        }
        System.out.println(ch + " no encontrado");
        System.exit(1);
    }
}

class InputLineClass extends FilterInputStream {
    InputStream InS;

    public InputLineClass(InputStream in) {
        super(in);
        InS = in;
    }

    public void gets(String linE) {
        StringBuffer buf = new StringBuffer();
        try {
            char ch;
            while ((ch = (char) read()) != '\n') {
                buf.append(ch);
            }
        } catch (IOException e) {
            System.err.print("Ha ocurrido un error:" + e);
            System.exit(-1);
        }
        linE = buf.toString();
    }

    public static void main(String[] args) {
        InputLineClass s = new InputLineClass(System.in);
        String strs = new String("first line");
        while (true) {
            s.gets(strs);
            System.out.println(strs);
        }
    }
}
