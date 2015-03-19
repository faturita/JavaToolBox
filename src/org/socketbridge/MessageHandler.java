package org.socketbridge;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: Apr 23, 2010
 * Time: 8:55:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageHandler {

    public static NumberFormat num = new DecimalFormat("000"); 

    public void parseMessage(String msgIn, StringBuffer msgOut) {
        for (int i = 0; i < msgIn.length(); i++) {
            if (msgIn.charAt(i) == '\\') {
                msgOut.append((char) Integer.parseInt(msgIn.substring(i + 1, i + 4)));
                i += 3;
            } else if (msgIn.charAt(i) == '*') {
                msgOut.append((char) Integer.parseInt(msgIn.substring(i + 1, i + 3), 16));
                i += 2;
            } else if ((msgIn.charAt(i) == '(') && (i+4) <= msgIn.length() && msgIn.charAt(i+4) == ')' ) {
                msgOut.append((char) Integer.parseInt(msgIn.substring(i + 1, i + 4)));
                i += 4; 
            } else {
                msgOut.append(msgIn.charAt(i));
            }
        }
    }

    public String readMessage(DataInputStream entrada) throws IOException {
        return readMessage(entrada, System.out);
    }

    public void encodedOutput(char dato, PrintStream out)
    {
        if ((dato >= 'a' && dato <= 'z') ||
            (dato >= 'A' && dato <= 'Z') ||
            (dato >= '0' && dato <= '9') ||
            (dato >= 32 && dato <= 47) ||
            (dato >= 58 && dato <= 64) ||
            (dato >= 91 && dato <= 96) ||
            (dato >= 123 && dato <= 126))
        {
            out.print(dato);
        }
        else
        {
            out.print("(" + num.format(dato) + ")");
        }
    }
    
    public String readMessage(DataInputStream entrada, PrintStream encodedOut) throws IOException {
        char dato = 'a';
        StringBuffer rcvMsg = new StringBuffer("");
        try {
            if (entrada.available() == 0) {
                //System.out.println("No hay datos que leer.");
                rcvMsg = new StringBuffer("-1");
            } else {
                while (entrada.available() != 0)
                {
                    byte in = entrada.readByte();
                    dato = (char) (  in & 0x00FF )   ;
                    rcvMsg.append(dato);
                    encodedOutput(dato, encodedOut);
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error al intentar leer datos.");
            throw e;
        }

        return (rcvMsg.toString());
    }    
}
