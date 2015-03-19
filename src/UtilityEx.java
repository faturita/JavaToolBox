import java.util.Vector;
import java.io.*;

class SortingFile {
    private DataInputStream fileInput;
    private Vector          vecOrd;

    public SortingFile(String fil) {
        try {
            fileInput = new DataInputStream(new FileInputStream(fil));
            vecOrd = new Vector(fileInput.available());
        } catch (FileNotFoundException e) {
            System.out.println("El archivo especificado no se encuentra.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Ocurrio el error" + e);
        }
    }

    public void Situar(String msg) {
        boolean aviso = false;
        for (int in = 0; in < vecOrd.size(); in++) {
            if (msg.compareTo((String) vecOrd.elementAt(in)) < 0) {
                vecOrd.insertElementAt(msg, in);
                aviso = true;
                break;
            }
        }
        if (!aviso)
            vecOrd.addElement(msg);
    }

    public void Sort() {
        String msg;
        try {
            while ((msg = fileInput.readLine()) != null) {
                Situar(msg);
            }
        } catch (IOException e) {
            System.out.println("Excepcion:" + e);
        } finally {
            vecOrd.trimToSize();
        }

    }

    public void Show() {
        for (int in = 0; in < vecOrd.size(); in++) {
            System.out.println(vecOrd.elementAt(in));
        }
    }

    public static void main(String[] args) {
        SortingFile so = new SortingFile(args[0]);
        so.Sort();
        so.Show();
    }

}
