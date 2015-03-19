package org.socketbridge;

import javax.script.ScriptException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


    class LoggeableBufferedReader extends BufferedReader {
        Writer out;
        public LoggeableBufferedReader(Reader in, Writer out) {
            super(in);
            this.out = out;
        }

        public String readLine() throws IOException {
            String msg = super.readLine();
            if (msg!=null)
            {
                out.write(msg+"\r\n");
                out.flush();
            }
            return msg;
        }

        public void close() throws IOException {
            out.close();
            super.close();
        }

    }

/**
 * TODO:
 * * Add options to allow bridging.
 * * Allow sending messages from files
 * * Allow scripts execution.
 * * Allow SSL support.
 *
 *
 * User: rramele
 * Date: Apr 21, 2000
 * Time: 2:27:11 PM
 *
 */
public class SocketChanceServer extends SocketChance {
    public static int DEFAULTPORT = 21;
    int portSet;
    ServerSocket servSock;
    private Socket sockCon;

    public SocketChanceServer(ServerSocket servSock) throws IOException {
        setup(servSock);
    }

    public SocketChanceServer(BufferedReader inKey) {
        int portBay = 0;
        try {
            System.out.print("Puerto de Servicio:");
            portBay = Integer.parseInt(inKey.readLine());
        }
        catch (IOException e) {
            System.out.println("Debe de ingresar port.");
            portBay = DEFAULTPORT;
        }
        setup(portBay);
    }
    public SocketChanceServer(int portBay) {
        setup(portBay);
    }

    public void setup(ServerSocket servSock) throws IOException {
        this.servSock = servSock;
        portSet = this.servSock.getLocalPort();
    }

    public void setup(int port) {
        try {
            if (port <= 0)
                port = DEFAULTPORT;
            servSock = new ServerSocket(port);
            portSet = port;
        }
        catch (IOException e) {
            System.out.println("Error al abrir ServSocket:" + e);
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Socket servidor abierto correctamente sobre puerto:" + port);
        System.out.println("Esperando sobre localhost en direccion:");
    }

    public void EsperarConexion() {
        System.out.println("Esperando conexion cliente sobre puerto:" + portSet);
        try {
            setSockCon(servSock.accept());
            entrada = new DataInputStream(getSockCon().getInputStream());
            salida = new DataOutputStream(getSockCon().getOutputStream());
            messageHandler = new MessageHandler();
        }
        catch (IOException e) {
            System.out.println("Error al conectar:" + e);
            System.exit(1);
        }
        System.out.println("Se ha realizado la conexion con el cliente exitosamente.");
    }

    public void Cerrar() {
        try {
            //.servSock.close();
            getSockCon().close();
            entrada.close();
            salida.close();
            System.out.println("Cerrando todas las streams.");
        }
        catch (IOException e) {
            System.out.println("Se produjo un error al cerrar los canales:" + e);
        }

    }

    public static void showHelp() {
        System.out.println("Connection Debuger running..");
        System.out.println(" Comandos:  C - Cerrar ");
        System.out.println("            E - Enviar ");
        System.out.println("            F - Forward mensaje en el buffer (escrito o leido)");
        System.out.println("            R - Recibir linea ");
        System.out.println("            B - Recibir sincronico (espera x datos)");
        System.out.println("            A - Abrir conexion.");
        System.out.println("            W - Esperar por conexiones remotas.");
        System.out.println("            Q - Salir. ");
        System.out.println();
        System.out.println("            bridge ");
        System.out.println("                connect HOST PORT ");
        System.out.println("                [r|b|e]");
    }




    public static void main(String[] args) throws IOException {
        System.out.println("Ejecutado programa SERVER.");

        BufferedReader inKey = new BufferedReader( new InputStreamReader(System.in)  );

        if (args.length > 0 && args[0].equals("-script")) {
            inKey = new BufferedReader( new InputStreamReader(new FileInputStream(args[1])));
        }

        if (args.length > 0 && args[0].equals("-savescript")) {
            String scriptFileName = args[1];
            inKey = new LoggeableBufferedReader( new InputStreamReader(System.in), new FileWriter(scriptFileName));
        }

        SocketChanceServer so = null;
        char opcion = 'd';
        int portBay = DEFAULTPORT;
        String msg = null;
        showHelp();

        SocketChanceClient soBridge = null;

        while ((opcion != 'q') && (opcion != 'Q')) {
            System.out.print(">");
            String input = null;
            try {
                input  =inKey.readLine();
                if (input != null && input.length()>0)
                {
                    opcion = (input).charAt(0);
                }
                // TODO: put some cookies here.
            }
            catch (IOException e) {
                // Ctrl+C pressed (likely)
                return;
            }
            if (input != null && input.equals("help"))
            {
                showHelp();
                continue;
            } else if (input != null && input.startsWith("l")) {
                msg = so.load(new Integer(input.substring(1)).intValue());
                System.out.println ("Message loaded:"+msg);
                opcion = 'f';
            } else if (input != null && input.startsWith("bridge")) {
                String [] commands = input.split(" ");

                if (commands.length > 0 && commands[1].equals("connect")) {
                    soBridge = new SocketChanceClient(commands[2], new Integer(commands[3]).intValue());
                }  else if (commands.length > 0 && commands[1].equals("f")) {
                    soBridge.EnviarMensaje(msg);
                } else if (commands.length > 0 && commands[1].equals("r")) {
                    msg = soBridge.RecibirMensaje();
                } else if (commands.length > 0 && commands[1].equals("b")) {
                    msg = soBridge.readSyncMessage();
                } else if (commands.length > 0 && commands[1].equals("c")) {
                    soBridge.Cerrar();
                }
                continue;
            }
            switch (opcion) {
                case 'c':
                case 'C':
                    so.Cerrar();
                    so = null;
                    break;
                case 'i':case 'I':
                    msg = readMessageFromKeyboard(inKey);
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
                    so = new SocketChanceServer(inKey);
                    break;
                case 'W':
                case 'w':
                    so.EsperarConexion();
                    break;
                case 's':case 'S':
                    String filename = so.store(msg);
                    System.out.println ("Msg stored into:"+filename);
                    break;
                case ':':
                    // script processing
                    try {
                        so.engine.put("msg",msg);
                        so.engine.put("toserver", soBridge);
                        so.engine.put("toclient", so);
                        so.engine.eval(input.substring(1));
                     } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        if (so!=null) so.Cerrar();
    }

    public Socket getSockCon() {
        return sockCon;
    }

    public void setSockCon(Socket sockCon) {
        this.sockCon = sockCon;
    }
}
