package org.socketbridge;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: Apr 21, 2000
 * 
 */
public class ProxyServer {
    private static int HTTPPORT = 8080;
    private static int SMTPPORT = 25;
    private static int POP3PORT = 110;

    private int MaxConnectionNumber = 48;

    private int httpPort;
    private int smtpPort;
    private int pop3Port;
    private String proxyHost;
    private int proxyPort;

    public ProxyServer(int hport) {
        httpPort = hport;
    }

    public void RunHttp() {
        System.out.println("Oel Amon Yag Proxy Server for LAN. - Version 1.1 2000");
        System.out.println("Ejecutandose como DEMONIO.");
        System.out.println("Numero m ximo de conexiones simultaneas:" + MaxConnectionNumber);
        System.out.println("Esperando conexiones sobre el puerto Proxy:" + httpPort);

        ConNumber conNum = new ConNumber(0);

        try {
            // Creacion de socket servidor en puerto especifico
            ServerSocket servSock = new ServerSocket(httpPort);

            Socket c_sock;
            Runnable c_thread;

            // Bucle de espera de conexiones.

            while (conNum.lessEq(MaxConnectionNumber)) {
                c_sock = servSock.accept();
                System.out.println("-----------------------------------------");
                System.out.print("Nueva Conexi¢n establecida.\n");
                System.out.println("Cantidad actual:" + conNum.getNumber());
                System.out.println("------------------------------------------");
                c_thread = new ClientManagerProxy(c_sock, conNum);
                new Thread(c_thread).start();
            }
        }
        catch (Exception e) {
            System.out.println("Ha ocurrido un error:" + e);
            System.out.println("El sistema se suspende por ser el mismo dentro del modulo principal.");
            System.out.println("Los servicios Proxy est n suspendidos.");
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Oel Amon Yag Proxy Server  -  Version 1.1 2000");
            System.out.println("Usage:");
            System.out.println("  [java interpreter] ProxyServer Proxy_port  ");
            System.out.println("\n\nCopyright(C) 2000 Oel Amon Yag Quality Software.");
            System.exit(0);
        }

        ProxyServer tnS = new ProxyServer(Integer.parseInt(args[0]));
        tnS.RunHttp();
    }

}