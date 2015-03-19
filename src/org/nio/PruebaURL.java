package org.nio;
/** Ejemplo de la utilizacion de los objetos URL y URLConnection. */
/** Con URLConnection se puede utilizar sus metodos getInputStream y getOutputStream
 para abrir canales de entrada y salida para el url especificado. */

import java.net.*;
import java.io.*;

class OpenStreamTest {
    public static void main(String[] args) {
        try {
            URL yahoo = new URL(args[0]);
            DataInputStream dis = new DataInputStream(yahoo.openStream());
            String inputLine;

            while ((inputLine = dis.readLine()) != null) {
                System.out.println(inputLine);
            }
            dis.close();
        } catch (MalformedURLException me) {
            System.out.println("MalformedURLException: " + me);
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
        }
    }
}
