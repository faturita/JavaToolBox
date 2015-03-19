package org.socketbridge;


import java.io.*;
import java.net.*;

class WebServer {
    private static int DEFAULTPORT=80;
    private static String INITIALPATH="\\";
    private static String FILENOTFOUND="<HTML><HEAD><TITLE>Archivo no encontrado</TITLE></head><body><h1>El archivo especificado no se ha encontrado</h1><hr noshide>Java Server 1.1</html>";

    private SocketChanceServer serverSockHandler;
    private int portS;
    private String activePath;
    private String[] lastDirectory;

    public WebServer (int port,String aPath)
        {
        if (port<=0) port=DEFAULTPORT;
        if (!(new File(aPath)).isDirectory()) aPath=INITIALPATH;
        System.out.println("WEB SERVER - Interactive File Browser.");
        serverSockHandler = new SocketChanceServer(port);
        portS=port;
        activePath=aPath;
        lastDirectory = null;
        }

    public void SndHtml(String msg) throws IOException {
        /** Esta funcion toma el msg y devuelve un boolean especificando
            si se esta o no solicitando ver una pagina requerida especifica
            en cuyo caso envia la pagina por el socket.
            */
        String wita=new String(decode(msg));
        try {

            /** Apertura del archivo html especificado */
            FileInputStream ffhtml = new FileInputStream(activePath+"\\"+wita);
            DataInputStream htmli=new DataInputStream(ffhtml);
            String msgTo;

            /** El archivo html se ha encontrado se procede a enviarlo */
            while ( (msgTo=htmli.readLine()) != null)
                {
                serverSockHandler.EnviarMensaje(msgTo);
                }

            
            }
        catch (IOException e) {
            System.err.println ("Archivo solicitado por el cliente no encontrado:"+wita+":"+e);
            serverSockHandler.EnviarMensaje(activePath+"\\"+wita);
            serverSockHandler.EnviarMensaje(FILENOTFOUND);
            }
        finally {
            serverSockHandler.EnviarMensaje("\n\r");
            serverSockHandler.Cerrar();
            }
        }

            

    public String decode (String msg) {
        // Decodifica el directorio especificado segun formato URL
        StringBuffer buf= new StringBuffer("");
        int initVal=5;
        int i=initVal;
        while (i<msg.length())
            {
            if (msg.charAt(i)=='%')
                {
                buf.append((char)32);
                i+=3;
                }

            if (msg.charAt(i)==' ')
                break;
            buf.append(msg.charAt(i));
            i++;
            }
        if (i==initVal)
            return (new String("-1"));
        return buf.toString();
        }

    public void ReActivar() {
        serverSockHandler = new SocketChanceServer(portS);
        }


    public String Rcv()
        {
        try {
            serverSockHandler.EsperarConexion();
            String rcvMsg;
            // Bloquea hasta que obtiene el mensaje.
            while ((rcvMsg=serverSockHandler.RecibirMensaje()).equals("-1")) ;
            return rcvMsg;
            }
        catch (Exception e) {
            System.err.println ("Error al recibir...:"+e);
            serverSockHandler.Cerrar();
            System.exit(0);
            }
        return " ";    
        }

    public void SndDirectory (String rcvMsg) throws IOException {
        try {
        if (!decode(rcvMsg).equals("-1"))
            activePath=new String(activePath+decode(rcvMsg)+"\\");
        serverSockHandler.EnviarMensaje("<html><head><title>Directorio de "+activePath+"</title></head><body><b><u>Directorio de<i> "+activePath+"</i>.</u></b><br><hr color='red'><font face='Times New Roman'>");
        serverSockHandler.EnviarMensaje(decode(rcvMsg));
        System.out.println(activePath);

        File tempFil = new File(activePath);
        String[] dirList = tempFil.list();
        for (int i=0;i<dirList.length;i++)
            {
            serverSockHandler.EnviarMensaje("<a href='"+dirList[i]+"'>");
            serverSockHandler.EnviarMensaje(dirList[i]);
            serverSockHandler.EnviarMensaje("</a>");
            if ( (new File(activePath+dirList[i])).isDirectory() )
                serverSockHandler.EnviarMensaje("<b>-DIR-</b><br>");
            else
                serverSockHandler.EnviarMensaje("<br>");
            }
        serverSockHandler.EnviarMensaje("<hr color='red'></body></html>\n\r");
        serverSockHandler.EnviarMensaje("\r\r\r\r\n");
        serverSockHandler.Cerrar();
        }
        catch (Exception e) {
            System.err.print("ERROR AL TX:"+e);
            serverSockHandler.EnviarMensaje("DONE");
            serverSockHandler.Cerrar();
            }
        }

    public static void main(String [] args) throws IOException {
        WebServer wSrv = new WebServer (Integer.parseInt(args[0]),args[1]);
        wSrv.SndHtml(wSrv.Rcv());
        
        }

    }


