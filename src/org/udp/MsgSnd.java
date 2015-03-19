package org.udp;
/** Programa ejemplo que muestra como funciona la utilizacion de UDP en JAVA */
/**
* @see
*    DatagramSocket
*    DatagramPacket
*/


import java.net.*;
import java.io.*;
import java.awt.*;


class MsgRcv implements Runnable {
    /** @see Esta clase es un thread demonio que se ejecuta y espera mensajes
             enviados con protocolo UDP. */

    private int activePort;
    private OutputStream outPut;
	
	private int whatToDo = 0;
  

    public MsgRcv (int port,OutputStream out) {
        if (port<0) port=4000;
        activePort=port;
        outPut=out;
        }

    public void run() {
        /** @method Cuerpo principal, puesto que este thread sera demonio es
                    un bloque infinito.
                    */
        DatagramPacket pack;
        /** Se abre un socket y se lo deja fijo sobre un puerto determinado
            conocido, pero no se lo conecta con ningun lado. */

        try {
            DatagramSocket sock=new DatagramSocket(activePort);
            if (activePort == 8888) sock.setBroadcast(true);
            while (true) {
		byte[] buf = new byte[256];		    
                pack = new DatagramPacket(buf,256);
                sock.receive(pack);
                buf=pack.getData();
		System.out.println("Data Received");
                outPut.write(buf,0,buf.length);
		System.out.println("");
		if (buf[0] == 'N') whatToDo = 1;
		if (buf[0] == 'R') whatToDo = 0;
		if (whatToDo != 0) { System.out.println ("Skiping response...");continue; }
		if (buf[1]!=1 && buf[1]!=2)  { buf[1]=0x00; } else { System.out.println ("Health check!"); }
		System.out.println("Data Sent");
		sock.send( new DatagramPacket(buf,buf.length,pack.getAddress(),pack.getPort()));
                outPut.write(buf,0,buf.length);
		System.out.println("");
		System.out.println("\n");
                }
            }
        catch (IOException e) {
            System.err.println ("Error "+e);
            return;
            }
        }
    public static void main(String [] args) {
        Runnable rnu=new MsgRcv(Integer.parseInt(args[0]),System.out);
        new Thread(rnu).start();
        }
    }

public class MsgSnd {
    private int DPort;
    private String Dhost;

    public MsgSnd (int port,String host) {
        if (port<0) port=4000;
        DPort=port;
        Dhost=new String(host);
        }

    public static int val = 1;
    
    public void Snd (byte[] buf) {
        if (buf.length>256)
            return;
        try {
            DatagramSocket sock=new DatagramSocket();
            DatagramPacket pack = null;
            if  (DPort != 8888) {
                pack=new DatagramPacket(buf,buf.length,InetAddress.getByName(Dhost),DPort);
            } else {
                sock.setBroadcast(true);
                pack=new DatagramPacket(buf,buf.length,InetAddress.getByName(Dhost),DPort);
            }
            sock.send(pack);
            byte[] buff = new byte[256];
            pack = new DatagramPacket(buff,256);
            if (val == 1) return;
            sock.receive(pack);
            buff=pack.getData();
            System.out.println("Data Received ("+pack.getLength()+")");
            System.out.write(buff,0,pack.getLength());
            System.out.println("");
            }
        catch (Exception e) {
            System.err.println ("Error en socket:"+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
            }
        }

    public static String encode(String msg)
    {
        StringBuffer msg2=new StringBuffer();

        for (int i=0;i<msg.length();i++)
            {
            if (msg.charAt(i)=='\\')
                {
                msg2.append((char)Integer.parseInt(msg.substring(i+1,i+4)));
                i+=3;
                }
            else
                msg2.append(msg.charAt(i));
            }
  

        return msg2.toString();
        
    }

    public static void maind(String[] args) {
        for(int i=0;i<65000;i++) {
            main(new String[] {args[0],new Integer(i).toString(),"\\000\\003"});
        }
    }

    public static void main(String[] args) {
        MsgSnd MSnd = new MsgSnd(Integer.parseInt(args[1]),args[0]);
        System.out.println ("Msg Sender");
        //System.out.print ("Escriba el mensaje a enviar y finalice con ENTER:");
        byte[] buf=new byte[256];

        try {
            //System.in.read(buf);
            String msg = encode(args[2]);
            buf = msg.getBytes();
            System.out.println("Sending "+msg);
            MSnd.Snd(buf);
            System.out.println ("El datagrama con el mensaje fue enviado correctamente.");
            }
        catch (Exception e) {
            System.err.println ("Error al intentar leer datos de entrada standard");
            System.err.println (e);
            }
        }
    }        
