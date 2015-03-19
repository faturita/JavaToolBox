package org.applet;
import java.awt.*;
import java.net.*;
import java.applet.*;
import java.io.*;
import java.util.*;

class DaemonToSend implements Runnable 
        {
        /** Clase encargada de enviar los datos por el stream conectado
        al IO Socket. */
        
        private DataOutputStream streamToSend;
        

        /** Constructor */
        public DaemonToSend(DataOutputStream da) 
                {
                streamToSend=da;
                }

        /* Enviar los datos por el Socket */
        public void send(String msgToSend) throws IOException
                {
                streamToSend.writeBytes(msgToSend+"\n\r");
                }
        

        /** Metodo Run por si se desea utilizar como un demonio */

        public void run() 
                {
                String msg;
                DataInputStream inKeyb=new DataInputStream(System.in);
                System.out.println ("q for quit");
                System.out.print ("Mensaje:");
                try {
                    msg=inKeyb.readLine();
                    while (0!=msg.compareTo("q"))
                        {
                        send(msg);
                        // Parser para el protocolo que devuelve msg
                        msg=new String(inKeyb.readLine());
                        }
                    }
                catch (IOException e)
                    {
                    System.out.println ("Error !");
                    }
                }

        }



class DaemonToRecv implements Runnable
        {
        /** <B>Clase para ser implementada como un demonio que muestrea la Red </B>*/
        
        DataInputStream streamToRecv;
        java.awt.TextArea msgList;
        IRCProtocol mIRC;
        
        public DaemonToRecv(DataInputStream da,java.awt.TextArea msgList,IRCProtocol mIRC)
                {
                this.msgList = msgList;
                streamToRecv = da;
                this.mIRC=mIRC;
                }

        public String receive() throws IOException
                {
                return streamToRecv.readLine();
                }
                
        public void run()
                {
                try {
                    String msgg;
                    while (true)
                        {
                        msgg=receive();
                        if (msgg==null)
                                throw (new NullPointerException());
                        else
                                {
                                //for debugging: System.out.print(msgg);
                                msgg=new String(mIRC.rcvMsg(msgg));
                                if (msgg.length()!=0)
                                    msgList.append(msgg+"\n");
                                }
                        }
                    }
                catch (IOException e)
                    {
                    msgList.append("Error I/O. Se produjo un ERROR en I/O.");
                    }
                catch (NullPointerException e)
                    {
                    msgList.append("El servidor corto la comunicacion.");
                    }
                }

        }


class IRCProtocol 
        {
        /** Layer de IRC 
        * Estado:
        *       Mantiene una tabla de los canales actuales en el chat y 
        *       de los usuarios (si esta conectado a uno de los canales)
        *
        * Metodos:
        *       Estan implementados para devolver la cadena en el formato IRC
        */
        
        private static String NOCHANNEL = "NOCHANNEL";
        private String nickname;
        private String channel;
        private String name;
        private String surname;
        private String place;
        private Choice channels;
        private java.awt.List users;
        private boolean isPinging;
        
        /** NOTA: Si se agrega mas de un mensaje de irc, recordar separarlos
        por \n\r.  El ultimo de ellos, no hace falta que se le agregue puesto
        que las rutinas de envio le agregan el crlf por defecto siempre al final*/
        
        
        public IRCProtocol(String nickname,String name,String surname, String place,Choice channels,java.awt.List users)
                {
                this.nickname=nickname;
                this.channel=IRCProtocol.NOCHANNEL;
                this.name=name;
                this.surname=surname;
                this.place=place;
                this.channels=channels;
                this.users=users;
                }

        public String login()
                {
                // Cadena para loguearse en el chat
                String msg=new String("NICK "+nickname+"\n\r"+"USER "+name+" "+surname+" "+place+" nomatter nomatter");
                return msg;
                }

        public String list()
                {
                channels.removeAll();
                return ("LIST");
                }

        public String names()
                {
                users.removeAll();
                return ("NAMES");
                }


        public String part()
                {
                if (channel.equals(IRCProtocol.NOCHANNEL))
                        {
                        /* Rutinas de aviso puede ser que sean necesarias
                        para manejar estas posibilidades */
                        return ("");
                        }
                else
                        {
                        String cha=new String(channel);
                        channel=IRCProtocol.NOCHANNEL;
                        return ("PART "+cha);
                        }
                }


        public String join(String chan)
                {
                String msg=new String();
                if (!channel.equals(IRCProtocol.NOCHANNEL))
                       msg=new String("PART "+channel+"\n\r");
                channel=new String(chan); 
                msg=new String(msg+"JOIN "+chan);
                return (msg);
                }

        public String sendTo (String to,String msg)
                {
                /**
                /* Descripcion: 
                /*      Recibe el msg y lo formatea para IRC.
                /*      Luego lo devuelve con el formato correcto
                /*      Si no, lo envia a ese usuario con PRIVMSG
                /* NOTA: El to pueden ser tanto usuarios 'habilitados' como
                /* mensajes broadcast para el canal actual usando el nombre
                /* del canal.
                */

                if ( ( to != null) && (to.equals("")) )
                        to=new String(channel);
                return (new String("PRIVMSG "+to+" :"+msg));
                }


        public String getOnlyNickName (String fullNick) throws NoSuchElementException
                {
                /** Descripcion:
                *       Obtiene el nombre del nick del full identifier de IRC
                *       Para esto divide la palabra buscando el simbolo ! .
                */
                StringTokenizer nick=new StringTokenizer(fullNick,"!");
                return (nick.nextToken());
                }

        public boolean getIsPinging()
                {
                if (isPinging==true)
                    {
                    isPinging=false;
                    return (true);
                    }
                else
                    return false;
                }
                                                 

        
        public String rcvMsg (String rcvMsg)
                {
                /** Descripcion:
                /*      Detecta si es un mensaje para todos los del canal con NOTICE
                /*      o si es un mensaje para la persona privado con PRIVMSG 
                /*      Los mensajes de un solo campo son descartados por considerarlos
                /*      mensajes del servidor no tratados por esta implementacion del 
                /*      protocolo
                /*  - Adicion de canales:
                /*      Los canales son agregados automaticamente al recibir los
                /*      mensajes de canales identificados por 322
                /*  - Adicion de usuarios:
                /*      Los usuarios son agregados automaticamente al recibir los
                /*      mensajes de usuarios identificados por 353, a la vez
                /*      que son mantenidos dinamicamente utilizando los mensajes
                /*      de PART y JOIN.
                /*      Para permitir los mensajes BroadCast, le asigna a la tabla
                /*      donde almacena los usuarios, una entrada con el nombre del
                /*      canal, el cual extrae del mismo mensaje 353
                /*  - NOTA: Esta implementacion de IRC no es completa, por lo 
                /*      que no soporta la creacion de canales dinamicos (asi como
                /*      su baja), tampoco soporta modos de usuarios.
                */

                try {
                    /** Msj Tipo:
                        322 - recepcion de lists de los canales.....
                        323 - Fin de la recepcion. Mismo formato

                    * En caso de recibir mensajes tipo
                        353 - recepcion de cada canal con los usuarios.
                        366 - Fin de la recepcion.
                    * Recepcion de PING.
                        Activa el flag de ping (isPinging), que es chequeado
                        para enviar el correspondiente mensaje PONG.


                    */

                    // Implementacion del PARSER

                    String serverName = new String("");
                
                    String firstPart = new String("");
                    String secondPart = new String("");

                    String returnMsg = new String("");

                    if (rcvMsg.substring(0,4).toUpperCase().equals("PING"))
                        isPinging=true;

                    StringTokenizer strOid=new StringTokenizer(rcvMsg,":");
                
                    if (!strOid.hasMoreTokens())
                            return ("");
                
                    firstPart = strOid.nextToken();
                    if (!strOid.hasMoreTokens())
                            secondPart="";
                    else
                            secondPart = strOid.nextToken();

                    StringTokenizer hdComp=new StringTokenizer(firstPart," ");
                
                    String whoIsName=new String("");
                    if (hdComp.hasMoreTokens())
                            whoIsName=getOnlyNickName(hdComp.nextToken());
                    
                    String commandS=new String("");
                    if (hdComp.hasMoreTokens())
                            commandS=new String(hdComp.nextToken());
                    
                    if (commandS.equals("PART"))
                            {
                            // Puede ser que haya cambiado de nombre ...
                            users.remove(whoIsName);
                            returnMsg = whoIsName+" ha dejado el canal...";
                            return (returnMsg);
                            }

                    if (commandS.equals("JOIN"))
                            {
                            users.addItem(whoIsName);
                            returnMsg = whoIsName+" se ha sumado al canal...";
                            return (returnMsg);
                            }
                
                    if (commandS.equals("PRIVMSG") || commandS.equals("NOTICE"))
                            {
                            String dest = new String("");
                            if (hdComp.hasMoreTokens())
                                dest=hdComp.nextToken();
                            else
                                return (secondPart);

                            if (dest.equals(channel))    
                                returnMsg = "["+whoIsName+"]: "+secondPart;
                            else
                                returnMsg = "["+whoIsName + " susurra a "+dest+"]\n\t"+secondPart;
                            return (returnMsg);
                            }
                
                
                    switch ((Integer.parseInt(commandS)))
                            {
                            case 322:/* Agregar a canales */
                                     if (!hdComp.hasMoreTokens())
                                        return ("");
                                     hdComp.nextToken();
                                     channels.addItem(hdComp.nextToken());
                                     returnMsg="";
                                     break;
                            case 353:/* Agregar a usuarios */
                                     /* Se eliminan todos los que estaban */
                                     /* La primera entrada es el nombre del canal */
                                     /* Para evitar problemas de sincronizacion,
                                        no considera mensajes de agregar usuario
                                        si el canal ha sido cerrado pero todavia
                                        no se registro el logout en el SERVER */
                                     if (channel.equals(IRCProtocol.NOCHANNEL))
                                        return ("");
                                     hdComp.nextToken();
                                     hdComp.nextToken();
                                     /* Si la lista esta vacia agrega el nombre
                                     del canal y quita el nombre del nickname
                                     que se agrega cuando se hacer el reply del
                                     servidor del mensaje Join.
                                     */
                                     if (users.countItems()==1)
                                        // Aunque sea siempre hay dos.
                                        users.remove(nickname);
                                     if (users.countItems()==0)
                                        {
                                        users.addItem(hdComp.nextToken()); 
                                        users.select(0);
                                        }
                                     StringTokenizer allUsers = new StringTokenizer(secondPart," ");
                                     while (allUsers.hasMoreTokens())
                                            {
                                            String user=new String(allUsers.nextToken());
                                            users.addItem(user);
                                            }
                                     break;
                            case 323:case 366:case 376:
                                     return("");
                            default:
                                     returnMsg=secondPart;
                                     break;
                            }
                    return (returnMsg);
                    }
                catch (Exception e) {return ("");}
                
                }

        public static void main(String[] args) 
                {
                System.out.println ("Main para testeo del parser...");
                if (args.length<1) 
                        {
                        System.out.println("Parametro: -Sentencia IRC a evaluar.");
                        System.exit(-1);
                        }
                IRCProtocol mIRC=new IRCProtocol ("","","","",new Choice(),new java.awt.List());
                System.out.println (mIRC.rcvMsg(args[0]));
                }


        }



public class appletTry extends Applet
        {
        private Socket clientSock;
        private String host;
        private int port;
        private TextField tField;
        private TextField msgBar;
        private TextField sndMsg;
        private Button sendBtton;
        private Button login;
        private Button join;
        private Button part;

        private Choice channels;
        private java.awt.List users;

        //private java.awt.List msgList;
        private TextArea msgList;
        private DataInputStream sockIn;
        private DataOutputStream sockOut;
        private IRCProtocol mIRC;
        private Thread ThreadDaRc;
        private DaemonToSend DaeToSnd;
        //private Checkbox toAll;
        public void init()
                {
                //Inicializando el applet

                
                setBackground(Color.blue);
                //setForeground(Color.black);
                setLayout(new BorderLayout());

                Panel cont5=new Panel();
                cont5.setLayout(new FlowLayout(FlowLayout.LEADING));
                cont5.setForeground(Color.blue);
                cont5.setBackground(Color.green);
                cont5.add((new Label("Sobrenombre")));
                cont5.add((tField = new TextField("",20)));
                cont5.add((login  = new Button("Connect")));
                cont5.add((join = new Button("Entrar canal")));
                cont5.add((part = new Button("Salir canal.")));
                join.resize(1,1);
                join.show();
                add(cont5,BorderLayout.NORTH);
                
                Panel cont=new Panel();
                cont.add((msgList = new TextArea(10,50)));
                msgList.setBackground(Color.white);
                msgList.setForeground(Color.black);
                cont.add((users   = new java.awt.List(10,false)));
                //cont.add((toAll=new Checkbox("Todos",false)));
                users.setForeground(Color.red);
                add(cont,BorderLayout.CENTER);

                Panel cont3=new Panel();
                cont3.setLayout(new FlowLayout());
                cont3.setForeground(Color.black);
                cont3.setBackground(Color.blue);
                cont3.add((sendBtton = new Button("Hablar")));
                cont3.add((sndMsg = new TextField("",20)));
                //cont3.add((new Label("Canales:")));
                cont3.add((channels = new Choice()));
                channels.addItem("Lista de canales");
                cont3.add((msgBar = new TextField("Esperando Conexion...",20)));
                msgBar.setForeground(Color.red);
                msgBar.setBackground(Color.white);
                msgBar.setEditable(false);
                sndMsg.setForeground(Color.black);
                sndMsg.setBackground(Color.white);
                add(cont3,BorderLayout.SOUTH);

                
                join.disable();
                sendBtton.disable();
                part.disable();
                }

        public boolean action (Event evt, Object obj)
                {
                if ( (mIRC != null) && (mIRC.getIsPinging()) )
                    {
                    try {
                        DaeToSnd.send("PONG");
                        }
                    catch (IOException e) {}
                    }


                if (evt.target instanceof Button)
                    {
                    if (evt.target == login)
                        {
                        if ((tField.getText()).length()==0)
                                {
                                msgBar.setText("Ingrese un nick antes!");
                                return false;
                                }
                        mIRC = new IRCProtocol (tField.getText(),"guess","guess","guess",channels,users);
                        msgBar.setText("Conectando...");
                        String host;
                        int port;
                        try {
                                if ( (getParameter("HOST")!=null) &&
                                     (getParameter("PORT")!=null) )
                                     {
                                     host=getParameter("HOST");
                                     port=Integer.parseInt(getParameter("PORT"));
                                     }
                                else
                                     {
                                     host="127.0.0.1";
                                     port=6667;
                                     }
                        
                                openCon(host,port);
                                startDialog(msgList,mIRC);

                                DaeToSnd.send(mIRC.login());
                                msgBar.setText("Login!");
                                join.enable();
                                login.disable();
                                DaeToSnd.send(mIRC.list());
                                }
                        catch (IOException e) {msgBar.setText("ERROR");}
                        catch (Exception e) {msgBar.setText("ERROR:"+e);}
                        }
                    if (evt.target == join)
                        {
                        if ( (channels.getSelectedItem()).length()==0)
                                {
                                msgBar.setText("Seleccione el canal de la lista de canales...");
                                return false;
                                }
                        try {
                            DaeToSnd.send(mIRC.join(channels.getSelectedItem()));
                            msgBar.setText("Chateando en "+channels.getSelectedItem());
                            // Falta analizar si el canal no existe                        
                            msgList.setText("");
                            part.enable();
                            sendBtton.enable();
                            }
                        catch (IOException e){msgBar.setText("ERROR"+e);}
                        }
                    if (evt.target == part)
                        {
                        try {
                            msgBar.setText("En linea pero en ningun canal....");
                            DaeToSnd.send(mIRC.part());
                            sendBtton.disable();
                            sndMsg.setText("");
                            users.removeAll();
                            }
                        catch (IOException e) {msgBar.setText("ERROR"+e);}
                        }
                    if (evt.target == sendBtton)
                        {
                        try {
                            if (users.getSelectedItem()==null) 
                                {
                                msgBar.setText("Debe seleccionar un destinatario.");
                                }
                            else
                                {
                                if (sndMsg.getText().equals(""))
                                        {
                                        msgBar.setText ("Debe escribir algo!");
                                        }
                                else
                                        {
                                        DaeToSnd.send(mIRC.sendTo(users.getSelectedItem(),sndMsg.getText()));
                                        sndMsg.setText("");
                                        }
                                }
                            }
                        catch (IOException e) {msgBar.setText("ERROR"+e);}
                        }
                    }
                return false;
                }




        public void openCon(String host, int port)
                {
                System.out.println ("Abriendo conexion IRC a:"+host+" sobre puerto :"+port+"....");
                try {
                        this.host=host;
                        this.port=port;
                        clientSock = new Socket(host,port);
                        sockIn=new DataInputStream(clientSock.getInputStream());
                        sockOut=new DataOutputStream(clientSock.getOutputStream());
                    }
                catch (Exception e) {
                        msgBar.setText("Error en la conexion:"+e);
                        }
               }



        public void cerrarCon () throws IOException
                {
                System.out.println ("Cerrando conexiones.");
                clientSock.close();
                sockIn.close();
                sockOut.close();
                }

        public void startDialog(java.awt.TextArea msgList,IRCProtocol mIRC)
                {
                // Ejecutando los threads para recibir y enviar datos
                
                //Runnable daeToSnd = new DaemonToSend(sockOut);
                Runnable daeToRcv = new DaemonToRecv(sockIn,msgList,mIRC);
                //Thread ThreadDaSn = new Thread (daeToSnd);
                ThreadDaRc = new Thread (daeToRcv);
                //ThreadDaSn.start();
                
                ThreadDaRc.start();
                ThreadDaRc.setPriority(Thread.MAX_PRIORITY);                
                
                DaeToSnd = new DaemonToSend(sockOut);

                // Espera hasta que finalice el thread de envios
                //while (ThreadDaSn.isAlive());
                //ThreadDaRc.stop();
                }
        
        public static void main (String [] args) 
                {
                DataInputStream inKeyb=new DataInputStream(System.in);
                String hsst=new String();
                int prrt=6667;
                appletTry apTry = new appletTry();
                try {
                        System.out.print ("Host:");hsst=inKeyb.readLine();
                        System.out.print ("Puerto IRC:");prrt=Integer.parseInt(inKeyb.readLine());
                        apTry.openCon(hsst,prrt);
                        apTry.startDialog(new java.awt.TextArea(),new IRCProtocol("","","","",new Choice(),new java.awt.List()));
                        apTry.cerrarCon();
                    }
                catch (IOException e) {
                        System.err.println ("Error:"+e);
                        System.exit(-1);
                        }

                }
        }

        
