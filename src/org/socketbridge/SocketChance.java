package org.socketbridge;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;

/**
 * SocketChance
 * 
 * User: rramele
 * Date: Apr 28, 2010
 * Time: 10:37:04 AM
 *
 */
public abstract class SocketChance {
    protected DataInputStream entrada;
    protected DataOutputStream salida;
    protected MessageHandler messageHandler;
    public ScriptEngine engine = null;

    public SocketChance() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }

    public void EnviarMensaje(String msg) throws IOException  {
        try {
            salida.writeBytes(msg);
            salida.flush();
        }
        catch (IOException e) {
            System.out.println("Error al intentar escribir datos.");
            throw e;
        }
    }

    public static String readMessageFromKeyboard(BufferedReader inKey)
    {
        String msg = null;
        try {
            msg = inKey.readLine();
            StringBuffer msg2 = new StringBuffer();

            new MessageHandler().parseMessage(msg, msg2);

            msg = msg2.toString();
        }
        catch (IOException e) {
            System.out.println("Debia ingresar el mensaje...");
            msg = "";
        }

        return msg;
    }

    public static int i = 0;

    public String load(int messageNumber)
    {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        String msg = null;
        try
        {
            String filename = "msg_"+messageNumber+".ser";
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            msg = (String)in.readObject();
            in.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }

        return msg;
    }


    public String store(String msg) {
        return store(msg,i++);
    }
    
    public String store(String msg, int msgNumber) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        String filename = null;
        try
        {
            filename = "msg_"+msgNumber+".ser";
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(msg);
            out.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return filename;
    }

    public String RecibirMensaje() throws IOException {
        return messageHandler.readMessage(entrada);
    }

    public String readSyncMessage() throws IOException {
        String msg = "-1";
        while ( (msg = messageHandler.readMessage(entrada)).equals("-1") )
        {
            // do nothing....
        }
        return msg;
    }

    public static String readFromKeyboard(BufferedReader inKey) {
        System.out.print(">");
        String input = null;
        try {
            input  =inKey.readLine();
        }
        catch (IOException e) {
            // Ctrl+C pressed (likely)
        }
        return input;
    }
}
