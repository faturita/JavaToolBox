package org.socketbridge;

import java.io.*;
import java.net.Socket;

/**
 * This is a very handful TCP/IP client.
 * It was initially developed in 1999.  I used it a lot and I wanted to improve it and
 * beautify it a little bit.
 *
 * User: unknown
 * Date: Apr 21, 1999
 * Time: 2:27:39 PM
 *
 *
 */
public class SocketChanceClient extends SocketChance {
    public static int DEFAULTPORT = 21;
    Socket sockCon;

    public SocketChanceClient(String hostn, int port) throws IOException {
        connect(hostn, port);
    }

    public void connect(String hostname, int port) throws IOException {
        System.out.println("Realizando conexion con servidor sobre puerto:" + port);

        if (port <= 0)
            port = DEFAULTPORT;
        sockCon = new Socket(hostname, port);
        entrada = new DataInputStream(sockCon.getInputStream());
        salida = new DataOutputStream(sockCon.getOutputStream());
        messageHandler = new MessageHandler();

        System.out.println("Se ha realizado la conexion con el servidor exitosamente.");
    }

    public SocketChanceClient(BufferedReader inKey) throws IOException {
        String hostname = null;
        int portBay = 0;
        try {
            System.out.print("Host:");
            hostname = inKey.readLine();
            System.out.print("Port:");
            portBay = Integer.parseInt(inKey.readLine());
        }
        catch (IOException e) {
            System.out.println("Debe de ingresar host y port.");
            hostname = new String("localhost");
            portBay = DEFAULTPORT;
        }
        connect(hostname,portBay);
    }

    public void EnviarMensaje(String msg) throws IOException {
        try {
            salida.writeBytes(msg);
        }
        catch (IOException e) {
            System.out.println("Error al intentar escribir datos.");
            throw e;
        }
    }

    public String RecibirMensaje() throws IOException {
        return messageHandler.readMessage(entrada);
    }

    public String RecibirMensaje(OutputStream encodedOutput) throws IOException {
        return messageHandler.readMessage(entrada, new java.io.PrintStream(encodedOutput));
    }

    public void Cerrar() {
        try {
            sockCon.close();
            entrada.close();
            salida.close();
            System.out.println("Todos los canales cerrados correctamente.");
        }
        catch (IOException e) {
            System.out.println("Se produjo un error al cerrar los canales:" + e);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader inKey = new BufferedReader( new InputStreamReader(System.in)  );
        SocketChanceClient so = null;
        char opcion = ' ';
        String msg = null;
        showHelp();

        while ((opcion != 'q') && (opcion != 'Q')) {
            String input = readFromKeyboard(inKey);
            if (input!=null && input.length()>0)
            {
                opcion = (input).charAt(0);
            }

            
            if (input != null && input.equals("help"))
            {
                showHelp();
                continue;
            } else if (input != null &&  input.startsWith("l")) {
                msg = so.load(new Integer(input.substring(1)).intValue());
                System.out.println ("Message loaded:"+msg);
                opcion = 'f';
            }
            switch (opcion) {
                case 'c':
                case 'C':
                    so.Cerrar();
                    so = null;
                    break;
                case 'e':
                case 'E':
                    msg = readMessageFromKeyboard(inKey);
                case 'f':case 'F':
                    so.EnviarMensaje(msg);
                    break;
                case 'r':
                case 'R':
                    msg = so.RecibirMensaje();
                    break;
                case 'b':case 'B':
                    msg = so.readSyncMessage();
                    break;                
                case 'a':
                case 'A':
                    so = new SocketChanceClient(inKey);
                    break;
                case 's':case 'S':
                    String filename = so.store(msg);
                    System.out.println ("Msg stored into:"+filename);
                    break;

            }
        }
        if (so!=null) so.Cerrar();
    }

    private static void showHelp() {
        System.out.println("V5.1 Ejecutado programa cliente.");
        System.out.println("Connection Debuger running..");
        System.out.println(" Comandos:  C - Cerrar ");
        System.out.println("            E - Enviar ");
        System.out.println("            R - Recibir linea ");
        System.out.println("            A - Abrir conexion.");
        System.out.println("            Q - Salir. ");
    }
}
