import java.util.BitSet;
import java.util.Enumeration;

/** Este ejemplo contiene utilidades estandar de JAVA 
 * @see Autor:El lenguaje de programacion JAVA
 * @version 1
 *
 * <b>
 * ESTE PROGRAMA TIENE LOS DERECHOS DE COPYRIGHTS DESCRITOS EN EL LIBRO
 * ANTERIORMENTE CITADO.
 * </b>
 *
 */

/** Utilizacion del vector dinamico BitSet para almacenar elementos de bits. */

public class Utilidades {
    private BitSet used = new BitSet();

    public Utilidades(String str) {
        for (int i = 0; i < str.length(); i++)
            used.set(str.charAt(i));

        // En cada una de las posiciones se asigna true si el valor de ese
        // caracter esta presente en la cadena.
    }

    public String toString() {
        String desc = "[";
        int size = used.size();
        for (int i = 0; i < size; i++) {
            if (used.get(i))
                desc += (char) i;
        }
        return desc + "]";
    }

    public Enumeration characters() {
        // Utiliza para devolver un enumeraion de objetos de este tipo.
        return null;
    }
}

class EnumerateWhichChars implements Enumeration {
    private BitSet bits;
    private int    pos;
    private int    setSize;

    EnumerateWhichChars(BitSet whichBits) {
        bits = whichBits;
        setSize = whichBits.size();
        pos = 0;
    }

    public boolean hasMoreElements() {
        while (pos < setSize && !bits.get(pos))
            pos++;
        return (pos < setSize);
    }

    public Object nextElement() {
        if (hasMoreElements())
            return new Character((char) pos++);
        else
            return null;
    }

}
