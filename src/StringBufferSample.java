/**
 * StringBuffer sample
 * 
 * @author rramele
 */

class StringBufferSample {
    public static StringBuffer addDots(StringBuffer buf, char sep, int spc) {
        // Toma una cadena con un numero y le agrega los puntos separadores
        int max = buf.length();
        for (int i = max - 1; i > 0; i--) {
            if (((max - i) % spc) == 0)
                buf.insert(i, sep);
        }
        return buf;
    }

    public static void main(String[] args) {
        String cad2 = new String("123.23");

        // Convertir un numero en string a un double.
        // No reconoce si el numero esta en hexa o en octal
        double dob = new Double(cad2).doubleValue();

        // La f representa que es un literal float.
        // Para representar un double se usa una d
        float floa = 2332.2f;

        // Conversion de float o numerico, a String usando valueOf

        String cad1 = String.valueOf(floa);
        System.out.println("El numero es:" + dob);
        System.out.println("El valor del string es:" + cad1);
        StringBuffer buf = new StringBuffer("22232123212321");
        System.out.println(StringBufferSample.addDots(buf, '.', 3));
    }
}
