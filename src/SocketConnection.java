import java.io.*;
import java.net.*;

class SocketChanceClient2 {
    private static int       DEFAULTPORT = 21;
    private Socket           sockCon;
    private DataInputStream  entrada;
    private DataOutputStream salida;

    public SocketChanceClient2(String hostn, int bayPort) {
        System.out.println("Realizando conexion con servidor sobre puerto:"
                + DEFAULTPORT);
        try {
            if (bayPort <= 0)
                bayPort = DEFAULTPORT;
            sockCon = new Socket(hostn, bayPort);
            entrada = new DataInputStream(sockCon.getInputStream());
            salida = new DataOutputStream(sockCon.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error al conectar:" + e);
            System.exit(1);
        }
        System.out
                .println("Se ha realizado la conexion con el servidor exitosamente.");
    }

    public void EnviarMensaje(String msg) {
        try {
            System.out.println("Escribiendo....");
            // salida.writeBytes((new
            // StringBuffer(msg)).append('\n').toString());
            salida.writeBytes(msg);
        } catch (IOException e) {
            System.out.println("Error al intentar escribir datos.");
            System.exit(1);
        }
    }

    public void RecibirMensaje() {
        char dato = 'a';
        try {
            if (entrada.available() == 0)
                System.out.println("No hay datos que leer.");
            else {
                while (entrada.available() != 0) {
                    dato = (char) entrada.readByte();
                    if ((dato < 31))
                        System.out.print("," + (int) dato + ",");
                    else
                        System.out.print(dato);
                }
                System.out.print("\n");
            }
        } catch (IOException e) {
            System.out.println("Error al intentar leer datos.");
            System.exit(1);
        }
    }

    public void Cerrar() {
        try {
            sockCon.close();
            entrada.close();
            salida.close();
            System.out.println("Todos los canales cerrados correctamente.");
        } catch (IOException e) {
            System.out
                    .println("Se produjo un error al cerrar los canales:" + e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        DataInputStream inKey = new DataInputStream(System.in);
        SocketChanceClient2 so = null;
        char opcion = 'Q';
        int portBay = DEFAULTPORT;
        String msg;
        System.out.println("Ejecutado programa cliente.");
        System.out.println("Connection Debuger running..");
        System.out.println(" Comandos:  C - Cerrar ");
        System.out.println("            E - Enviar ");
        System.out.println("            R - Recibir linea ");
        System.out.println("            A - Abrir conexion.");
        System.out.println("            Q - Salir. ");
        System.out.print(">");
        try {
            opcion = (inKey.readLine()).charAt(0);
        } catch (IOException e) {
            System.exit(1);
        }

        while ((opcion != 'q') && (opcion != 'Q')) {
            switch (opcion) {
            case 'c':
            case 'C':
                if (so == null) {
                    System.out
                            .println("No se puede cerrar porque todavia no esta abierto.");
                    break;
                }
                so.Cerrar();
                so = null;
                break;
            case 'e':
            case 'E':
                if (so == null) {
                    System.out.println("El canal no esta abierto.");
                    break;
                }
                try {
                    msg = inKey.readLine();
                    StringBuffer msg2 = new StringBuffer();

                    for (int i = 0; i < msg.length(); i++) {
                        if (msg.charAt(i) == '\\') {
                            msg2.append((char) Integer.parseInt(msg.substring(
                                    i + 1, i + 4)));
                            i += 3;
                        } else
                            msg2.append(msg.charAt(i));
                    }
                    msg = msg2.toString();
                } catch (IOException e) {
                    System.out.println("Debia ingresar el mensaje...");
                    msg = "";
                }
                so.EnviarMensaje(msg);
                break;
            case 'r':
            case 'R':
                if (so == null) {
                    System.out.println("El canal no esta abierto.");
                    break;
                }
                so.RecibirMensaje();
                break;
            case 'a':
            case 'A':
                if (so != null) {
                    System.out
                            .println("El canal ya esta abierto. Cierrelo primero.");
                    break;
                }
                try {
                    msg = inKey.readLine();
                    portBay = Integer.parseInt(inKey.readLine());
                } catch (IOException e) {
                    System.out.println("Debe de ingresar host y port.");
                    msg = new String("localhost");
                    portBay = DEFAULTPORT;
                }
                so = new SocketChanceClient2(msg, portBay);
                break;
            }
            System.out.print(">");
            try {
                opcion = (inKey.readLine()).charAt(0);
            } catch (IOException e) {
                System.exit(1);
            }
        }
        so.Cerrar();
    }
}

/*
 * class SocketChance { private static int DEFAULTPORT=21; private int portSet;
 * private ServerSocket servSock; private Socket sockCon; private
 * DataInputStream entrada; private DataOutputStream salida;
 * 
 * public SocketChance (int portBay) { try { if (portBay <= 0)
 * portBay=DEFAULTPORT; servSock = new ServerSocket(portBay); portSet=portBay; }
 * catch (IOException e) { System.out.println("Error al abrir ServSocket:"+e);
 * System.exit(1); }
 * System.out.println("Socket servidor abierto correctamente sobre puerto:"
 * +portBay); System.out.println("Esperando sobre localhost en direccion:"); }
 * 
 * public void EsperarConexion() { System.out.println
 * ("Esperando conexion cliente sobre puerto:"+portSet); try { sockCon =
 * servSock.accept(); entrada=new DataInputStream(sockCon.getInputStream());
 * salida=new DataOutputStream(sockCon.getOutputStream()); } catch (IOException
 * e) { System.out.println ("Error al conectar:"+e); System.exit(1); }
 * System.out
 * .println("Se ha realizado la conexion con el cliente exitosamente."); }
 * 
 * public void EnviarMensaje(String msg) { try {
 * System.out.println("Escribiendo..."); salida.writeBytes(msg); } catch
 * (IOException e) { System.out.println ("Error al intentar escribir datos.");
 * System.exit(1); } } public void RecibirMensaje() { char dato='a'; try { if
 * (entrada.available()==0) System.out.println("No hay datos que leer."); else {
 * while (entrada.available()!=0) { dato=(char)entrada.readByte(); if (dato<32)
 * System.out.print(","+(int)dato+","); else System.out.print(dato); }
 * 
 * System.out.print("\n"); } } catch (IOException e) { System.out.println
 * ("Error al intentar leer datos."); System.exit(1); } } public void Cerrar() {
 * try { servSock.close(); sockCon.close(); entrada.close(); salida.close();
 * System.out.println("Cerrando todas las streams."); } catch (IOException e) {
 * System.out.println("Se produjo un error al cerrar los canales:"+e);
 * System.exit(1); }
 * 
 * } public static void main(String [] args) { System.out.println
 * ("Ejecutado programa SERVER."); DataInputStream inKey = new
 * DataInputStream(System.in); SocketChance so=null; char opcion='Q'; int
 * portBay=DEFAULTPORT; String msg=null; System.out.println
 * ("Connection Debuger running.."); System.out.println
 * (" Comandos:  C - Cerrar "); System.out.println ("            E - Enviar ");
 * System.out.println ("            R - Recibir linea "); System.out.println
 * ("            A - Abrir conexion."); System.out.println
 * ("            W - Esperar por conexiones remotas."); System.out.println
 * ("            Q - Salir. "); System.out.print (">"); try { opcion=
 * (inKey.readLine()).charAt(0); } catch (IOException e) { System.exit(1); }
 * 
 * while ((opcion != 'q') && (opcion != 'Q')) { switch (opcion) { case 'c':case
 * 'C': if (so == null) {
 * System.out.println("No se puede cerrar porque todavia no esta abierto.");
 * break; } so.Cerrar(); so=null; break; case 'e':case 'E': if (so == null) {
 * System.out.println("El canal no esta abierto."); break; } try {
 * msg=inKey.readLine(); StringBuffer msg2=new StringBuffer();
 * 
 * for (int i=0;i<msg.length();i++) { if (msg.charAt(i)=='\\') {
 * msg2.append((char)Integer.parseInt(msg.substring(i+1,i+4))); i+=3; } else
 * msg2.append(msg.charAt(i)); } msg=msg2.toString(); } catch (IOException e) {
 * System.out.println("Debia ingresar el mensaje..."); msg=""; }
 * so.EnviarMensaje(msg); break; case 'r':case 'R': if (so == null) {
 * System.out.println("El canal no esta abierto."); break; }
 * so.RecibirMensaje(); break; case 'a':case 'A': if (so != null) {
 * System.out.println ("El canal ya esta abierto. Cierrelo primero."); break; }
 * try { System.out.print("Puerto de Servicio:");
 * portBay=Integer.parseInt(inKey.readLine()); } catch (IOException e) {
 * System.out.println("Debe de ingresar host y port."); msg=new
 * String("localhost"); portBay=DEFAULTPORT; } so = new SocketChance(portBay);
 * break; case 'W':case 'w': if (so == null) {
 * System.out.println("Debe abrir una conexion de socket servidor inicialmente."
 * ); break; } so.EsperarConexion(); // se deberia haber comprobado que
 * estuviera conectado con un canal en todos los demas opciones y aqui tambien.
 * break; } System.out.print (">"); try { opcion= (inKey.readLine()).charAt(0);
 * } catch (IOException e) { System.exit(1); } } so.Cerrar(); } }
 */
