package org.basicsamples;
/** Universidad Nacional de La Matanza  -- Laboratorios de Fisica/Quimica
        ClaseFormula:  Contiene codigo para almacenar y retribuir informacion
                relacionada con un topico(es la clave de la tabla)
        TablaForm:  Applet que utiliza a la clase anterior para visualizar
                los resultados de consultas por la clave(topico)

        Creacion:  28 de Septiembre de 1999 por Rodrigo Ramele
        Modificiones:
                - 19 de Octubre de 1999 por Rodrigo Ramele
                        Codigo para optimizar la busqueda.

        Utilizacion:
                Este applet se utiliza con la pagina TableForm.htm

                Para introducir mas datos es necesario utilizar el interprete
                de java (java.exe), para asi poder actualizar la base de datos
                que, por cuestiones de seguridad de los applets no puede hacerse
                desde la pagina.

                Sintaxis:  java ClaseForm topico "formula"
 */

import java.awt.*;
import java.io.RandomAccessFile;
import java.io.IOException;

class ClaseFormula {
    private RandomAccessFile   archAleat;

    /* status=1 archivo abierto correctamente. */
    /* status=-1 archivo no abierto. */
    private int                status;

    public final static int    MAXLENGTH = 20;
    public final static String DEFAULT   = "formulas.dat";

    ClaseFormula(String base) {
        status = 1;
        try {
            archAleat = new RandomAccessFile(base, "r");
        } catch (IOException e) {
            status = -1;
        }
    }

    ClaseFormula(String base, boolean b) {
        status = 1;
        try {
            archAleat = new RandomAccessFile(base, "rw");
        } catch (IOException e) {
            status = -1;
        }
    }

    public long secSearch(String topic) {
        String top;
        try {
            if ((status == -1) || (archAleat.length() == 0))
                return -1;
            archAleat.seek(0);
            top = archAleat.readLine();
            while (true) {
                // System.out.println("->"+top);
                // System.out.println("->"+topic);
                if (top.equals(topic))
                    return (archAleat.getFilePointer() - top.length() - 1);
                else if (top == null)
                    return -1;
                archAleat.readLine();
                top = archAleat.readLine();
            }
        } catch (IOException e) {
            return -1;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public String[] query(String topic) {
        topic = topic.toUpperCase();
        String[] result = new String[MAXLENGTH];
        long index = secSearch(topic);
        if ((index == -1)) {
            return null;
        }
        int in = 0;

        try {
            // System.out.println(index);
            archAleat.seek(index);

            for (; in < MAXLENGTH; in++) {
                String topicRead = archAleat.readLine();
                result[in] = archAleat.readLine();
                // System.out.println(topicRead+"|");
                // System.out.println(topic+"|");
                if (!(topicRead.equals(topic))) {
                    result[in] = null;
                    break;
                }
            }
        } finally {
            return result;
        }
    }

    private void sortFil() {
        if (status == -1)
            return;
    }

    private void blanquear(long ultimo, long largo) {
        try {
            for (long in = ultimo + 1; in < (ultimo + 1 + largo); in++) {
                archAleat.seek(in);
                archAleat.writeByte('\n');
            }
        } catch (IOException e) {
        }
    }

    private void acomodate(long index, long largo) {
        long in = 0;
        try {
            long ultimoAnt = archAleat.length() - 1;
            blanquear(ultimoAnt, largo);
            long ultimoAct = archAleat.length() - 1;
            for (; ultimoAnt >= index; ultimoAnt--, ultimoAct--) {
                archAleat.seek(ultimoAnt);
                byte transfByte = archAleat.readByte();
                archAleat.seek(ultimoAct);
                archAleat.writeByte(transfByte);
            }
        } catch (IOException e) {
        }
    }

    public void grabar(String topico, String formula) {
        if (status == -1)
            return;
        StringBuffer top = new StringBuffer(topico);
        StringBuffer form = new StringBuffer(formula);
        long index = secSearch(topico.toUpperCase());
        try {
            if (index == -1)
                index = archAleat.length();
            else
                acomodate(index, (topico.length() + formula.length() + 2));

            top.append("\n");
            form.append("\n");

            archAleat.seek(index);
            archAleat.writeBytes(top.toString().toUpperCase());
            archAleat.writeBytes(form.toString());
            sortFil();
        } catch (IOException e) {
        }
    }

    public void finalize() throws Throwable {
        archAleat.close();
        super.finalize();
    }

    public static void main(String[] args) {
        ClaseFormula formu = new ClaseFormula(ClaseFormula.DEFAULT, true);
        try {
            formu.grabar(args[0], args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out
                    .println("ClaseFormula: utilizacion para el rellado de datos.");
            System.out.println("Archivo por defecto:" + ClaseFormula.DEFAULT);
            System.out
                    .println("\nUtilice las comillas para especificar los parametros donde:");
            System.out.println("\n\tPrimer par metro: t¢pico de la materia.");
            System.out.println("\tSegundo par metro: formula a almacenar.");
        }
        formu = null;
    }

}
