package org.socketbridge;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: Apr 21, 2010
 * Time: 2:22:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamTransferThread implements Runnable {

    DataInputStream input;
    DataOutputStream output;

    public StreamTransferThread(DataInputStream in, DataOutputStream out) {
        input = in;
        output = out;
    }


    public void run() {
        try {
            int msg;
            while (true) {
                msg = input.readByte();
                output.writeByte(msg);
            }
        }
        catch (Exception e) {
            System.out.println("Finalizando stream de informacion.");
            System.out.println("Returned message:" + e);
        }
    }
}
