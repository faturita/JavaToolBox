/** Este programa ejemplifica como utilizar procesos y ejecutarlos en Java, ademas
muestra como utilizar mecanismos ipc sin importar el so donde se encuentre. */

/** Notas a posteriori:
 Este programa ejecuta tres threads que son los encargados de enlazar la
 salida, entrada, y errores del proceso hijo, con los del sistema general.
 Si al finalizar el proceso padre de estos threads, este no emite la
 se¤al System.exit() explicitamente, queda esperando que los threads terminen
 su ejecucion por alguna razon.  */

/** Los procesos solo pueden ejecutar programas del file system, es decir
 si se quisiera ejecutar algun comando del command.com hay que ejecutar este
 programa obligatoriamente. */

/** Al leer los datos de un flujo, y consecuentemente respetando la convencion
 UNIX, int read() devuelve -1 si el flujo no tiene mas datos. */

import java.io.*;

class inOut implements Runnable {
    private OutputStream out;
    private InputStream  in;
    private PrintStream  prt;

    public inOut(Object stream1, Object stream2) {
        if (stream2 instanceof PrintStream)
            out = (PrintStream) stream2;
        else
            out = (OutputStream) stream2;

        in = (InputStream) stream1;
    }

    public void run() {
        try {
            System.out
                    .println("Iniciando ejecucion de thread para enlazar los streams.");
            int ent;
            while ((ent = in.read()) != -1) {
                out.write(ent);
            }
            System.out.println("Terminando ejecucion de Thread de enlace.");
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Se ha producido un error :" + e);
            System.exit(0);
        }
    }
}

class Procesos {
    public Process userProg(String cmd) throws IOException {
        System.out.println("Ejecutando proceso con el comando " + cmd);
        Process proc = Runtime.getRuntime().exec(cmd);
        System.out.println("Finalizada ejecucion....");

        plugTogether(System.in, proc.getOutputStream());
        plugTogether(proc.getInputStream(), System.out);
        plugTogether(proc.getErrorStream(), System.err);

        return proc;
    }

    public void plugTogether(Object stream1, Object stream2) {
        Runnable inOut = new inOut(stream1, stream2);
        new Thread(inOut).start();
    }

    public static void main(String[] args) {
        try {
            Procesos prc = new Procesos();
            Process pprc = prc.userProg(args[0]);

            /** Analizar estado de terminacion del hijo... */

            System.out.println("Estado devuelto por el hijo:" + pprc.waitFor());
            System.out.println("Terminando el programa.");

            System.exit(0);

        } catch (IOException e) {
            System.err.println("Finalizado abruptamente....");
            System.err.println(e);
            System.exit(-1);
        } catch (InterruptedException e) {
            System.err.println("Se ha interrumpido el proceso principal.");
            System.exit(-1);
        }
    }
}
