/**
 * This is one of the oldest programs that I wrote in Java.
 *
 * Surprisingly, I used it thoughtfully in a lot of places and was always like a swiss-knife
 * helping me in a variety of situations.
 *
 * This is a very basic set of classes to handle with TCP Sockets.
 */
package org.socketbridge;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.ServerSocket;
import java.util.Hashtable;

/**
 * Socket Bridge
 * <p/>
 * Genera un bridge a nivel de aplicación para redireccionar todos los mensajes
 * desde un servidor a un cliente posible que se conecte, registrando en un log
 * toda la transferencia de mensajes.
 *
 * TODO: Need to handle multiple connections at the same time sharing the dumping file.
 * TODO: log the files serialized.
 */
class SocketBridge {
    /**
     * Puerto de trabajo por default.
     */
    private static int DEFAULTPORT = 21;
    /**
     * SocketChance para recibir posibles conexiones.
     */
    private SocketChanceServer serverSockHandler;
    /**
     * SocketChanceClient para conectarse con el servidor del cual toma los mensajes.
     */
    private SocketChanceClient clientSockHandler;
    /**
     * Handler para acceso a archivos de acceso aleatorio.
     */
    private RandomAccessFile logFile;
    /**
     * Puerto de trabajo con el que se conecta con el servidor.
     */
    private int workPortCient;
    /**
     * Puerto de trabajo en el que escucha las posibles conexiones.
     */
    private int workPortServer;
    private static ServerSocket servSock;
    private static SocketBridge so = null;
    private String fileName;

    private static String semaphore = new String("lockme");

    private ScriptEngine engine = null;


    /**
     * Construye una instancia para recibir conexiones sobre bayPort, y conectarse
     * a hostName sobre el puerto bayPortClient registrando la mensajería en fileName
     */
    public SocketBridge(int bayPort, int bayPortClient, String hostName, String fileName) {
        try {
            if ((bayPort < 0 || bayPortClient < 0)) {
                throw new Exception("Puertos de Cliente y/o Servidor erroneos.");
            }
            // Apertura e inicializacion de las conexiones de circuito virtual
            //   con el servidor especificado, una vez establecida la conexion
            //   con el cliente.

            System.out.println("Inicializando....");
            if (servSock == null) servSock = new ServerSocket(bayPort);
            serverSockHandler = new SocketChanceServer(servSock);
            serverSockHandler.EsperarConexion();
            System.out.println("Conectarse a servidor remoto sobre puerto " + bayPortClient);
            clientSockHandler = new SocketChanceClient(hostName, bayPortClient);
            System.out.println("BRIDGE ESTABLECIDO.");
            System.out.println("Salida de datos: Archivo " + fileName);

            // Apertura de archivo donde se almacenaran los datos de transferencia
            // Posicionamiento sobre el final, Grabacion de la cabecera.
            synchronized (semaphore)
            {
                this.fileName = fileName;
                logFile = new RandomAccessFile(this.fileName, "rw");
                logFile.seek(logFile.length());
                logFile.close();
            }

            ScriptEngineManager mgr = new ScriptEngineManager();
            engine = mgr.getEngineByName("JavaScript");

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR:" + e);
            CerrarAll();
        }
    }

    /**
     * Cierra todas las conexiones abiertas.
     */
    public void CerrarAll() {
        try {
            serverSockHandler.Cerrar();
            clientSockHandler.Cerrar();
            logFile.close();
            System.out.println("Terminadas las conexiones.");
         }
        catch (IOException e) {
            System.out.println("Error al cerrar todos los archivos.");
         }
    }

    private boolean asks = false;

    private Hashtable<Integer,String> msgTable = new Hashtable<Integer,String>();

    /**
     * Realiza la transferencia entre el cliente que solicitó una conexión,
     * y el servidor al cual se está conectando.
     */
    public void Transfer()  {
        BufferedReader inKey = new BufferedReader(new InputStreamReader(System.in));
        String ent = new String("A");
        String trfMsg = new String("");
        int msgIndex = 0;
        System.out.println("Comenzando transferencia de datos.");
        System.out.println("Q - Salir");

        try {

            while (true) {

                if (isAsks())
                {

                    ent = inKey.readLine();

                    if (ent != null && ent.length()>0) {

                        if (ent.equals("c") || ent.equals("C"))
                        {
                            CerrarAll();
                            return;
                        }
                        else if (ent.equals("q") || ent.equals("Q"))
                        {
                            CerrarAll();
                            System.exit(1);
                        }
                        else if (ent.equals("continue")) {
                            setAsks(false);
                            System.out.println ("[Command processed OK]");
                        }
                        else if (ent.startsWith("store")) {
                            clientSockHandler.store(msgTable.get((int)ent.charAt(5)));
                        } else {
                            try {
                                if (msgIndex > 0) {
                                    engine.put("msg",trfMsg);
                                    engine.put("toserver", clientSockHandler);
                                    engine.put("toclient", serverSockHandler);
                                    engine.put("list", msgTable.values().toArray(new String[1]));
                                    engine.eval(ent);
                                }
                            } catch (ScriptException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                // Leer datos del servidor
                trfMsg = serverSockHandler.RecibirMensaje();
                if (!trfMsg.equals("-1")) {
                    System.out.println();
                    toLogFile("from Client:", trfMsg);
                    clientSockHandler.EnviarMensaje(trfMsg);
                    msgIndex++;
                    msgTable.put(msgIndex,trfMsg);
                }
                trfMsg = clientSockHandler.RecibirMensaje();
                if (!trfMsg.equals("-1")) {
                    System.out.println();
                    toLogFile("from Server:", trfMsg);
                    serverSockHandler.EnviarMensaje(trfMsg);
                    msgIndex++;
                    msgTable.put(msgIndex,trfMsg);                    
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            CerrarAll();
        }
    }

    public void toLogFile(String aclaracion, String toMsg) {
        char dato = 'a';
        try {
            synchronized (semaphore) {
                logFile = new RandomAccessFile(fileName, "rw");
                logFile.seek(logFile.length());
                if (logFile.length() != logFile.getFilePointer())
                    logFile.seek(logFile.length());
                //logFile.writeBytes(aclaracion);

                //logFile.writeBytes("\r\n");

                MessageHandler messageHandler = new MessageHandler();

                ByteArrayOutputStream bt = new ByteArrayOutputStream();

                messageHandler.readMessage(new DataInputStream(new ByteArrayInputStream(toMsg.getBytes())),new java.io.PrintStream(bt));

                logFile.writeBytes(bt.toString());
                logFile.writeBytes("\r\n");
                logFile.close();
            }
        }
        catch (IOException e) {
            System.out.println("Error:" + e);
            CerrarAll();
        }
    }


    public static void main(final String[] args) {
        if (args.length != 5) {
            System.out.println("SocketBridge v1.1 - 1999.");
            System.out.println("Uso:");
            System.out.println("  java SocketBridge <serverPort> <clientPort> <hostname> <logFile> \"control\"");
            System.out.println("");
            System.out.println("    <serverPort>   Puerto de servicio para las conexiones de los clientes.");
            System.out.println("    <clientPort>   Puerto para conectarse con el servidor de datos.");
            System.out.println("    <hostName>     Nombre del host al cual conectarse.");
            System.out.println("    <logFile>      Archivo en el que registrar la mensajeria.");
            System.out.println("    \"control\"      Especificando este tag, permite la interacción online de la transferencia de datos.");
            System.out.println("\n");
            return;
        }
        boolean bAsks = true;
        if (args.length > 4) {
            if (args[4].toLowerCase().trim().equals("-control"))
                bAsks = false;
        }
        boolean bContinue = true;
        while (bContinue) {
            so = new SocketBridge(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], args[3]);
            so.setAsks(bAsks);
            new Thread(new Runnable() {
                public void run() {
                    so.Transfer();
                }
            }).start();
        }
    }

    public boolean isAsks() {
        return asks;
    }

    public void setAsks(boolean asks) {
        this.asks = asks;
    }
}

