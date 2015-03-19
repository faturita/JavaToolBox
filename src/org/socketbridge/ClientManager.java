package org.socketbridge;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: Apr 21, 2010
 * Time: 2:30:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientManager implements Runnable {
    private Socket c_sock;
    private ConNumber contNum;
    private String proxyHost;
    private int proxyPort;
    private Socket toServSock;

    public ClientManager(Socket c_sock, ConNumber contNum, String proxyHost, int proxyPort) {
        this.c_sock = c_sock;
        contNum.inc();
        this.proxyPort = proxyPort;
        this.contNum = contNum;
        this.proxyHost = proxyHost;
    }

    public void run() {
        // Se ejecuta cuando start le pasa el comando para el thread.

        // Abrir conexion con el servidor proxy.
        try {
            System.out.println("Cliente conectado con servidor proxy: " + proxyHost + " en " + proxyPort);
            toServSock = new Socket(proxyHost, proxyPort);

            // Creacion de los flujos de enlace.
            DataInputStream fromServ = new DataInputStream(toServSock.getInputStream());
            DataInputStream fromClie = new DataInputStream(c_sock.getInputStream());
            DataOutputStream toServ = new DataOutputStream(toServSock.getOutputStream());
            DataOutputStream toClie = new DataOutputStream(c_sock.getOutputStream());
            int continuar = 2;
            String msg = null;
            System.out.println("Iniciando transferencias....");

            // Iniciando threads de control de transferencia de streams

            Runnable sTrans1 = new StreamTransferThread(fromClie, toServ);
            Runnable sTrans2 = new StreamTransferThread(fromServ, toClie);

            Thread thToServ = new Thread(sTrans1);
            Thread thToClie = new Thread(sTrans2);
            thToServ.start();
            thToClie.start();

            // Espera hasta que finalicen todos los threads.
            while (thToClie.isAlive() && thToServ.isAlive()) {
            }
            thToClie.stop();
            thToServ.stop();

            // Cierre de streams
            fromServ.close();
            fromClie.close();
            toServ.close();
            toClie.close();
        }
        catch (IOException e) {
            System.out.println("Thread cerrado abruptamente.");
            System.out.println("Mensaje del sistema:" + e);
            System.out.println("Cerrando conexion " + contNum.getNumber());
        }
        /*catch (InterruptedException e) {
            System.out.println ("Sistema interrumpido.");
            }*/
        finally {
            contNum.dec();
            try {
                c_sock.close();
                toServSock.close();
            }
            catch (Exception e) {
                System.out.println("No hay conexion");
            }
        }
    }

    public void stop() {
        try {
            c_sock.close();
            toServSock.close();
        }
        catch (Exception e) {
        }
    }
}