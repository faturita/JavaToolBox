package org.socketbridge;

import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: Apr 21, 2010
 * Time: 2:31:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientManagerProxy implements Runnable {
    private Socket c_sock;
    private ConNumber contNum;
    private Socket toServSock;

    public ClientManagerProxy(Socket c_sock, ConNumber contNum) {
        this.c_sock = c_sock;
        contNum.inc();
        this.contNum = contNum;
    }

    public void run() {
    }

}
