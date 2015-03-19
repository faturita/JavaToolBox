package org.basicsamples;
/** Rutinas de manejos de clases String en java */

class RegionMatch {
        public static void main( String [] args)
                {
                String str = "!Mira,mira!";
                boolean b1,b2,b3;
                b1 = str.regionMatches(6, "Mira",0,4);
                b2 = str.regionMatches(true, 6, "Mira",0,4);
                b3 = str.regionMatches(true, 6, "Mira",0,5);
                System.out.println("b1 =" + b1);
                System.out.println("b2 =" + b2);
                System.out.println("b3 =" + b3);
                }
        }

class Cadena {

        public static String squeezeOut (String from, char toss) {
                /* toCharArray transforma un objeto String en una cadena de chars */
                char [] chars=from.toCharArray();
                int len=chars.length;
                for (int i=0;i<len;i++)
                        {
                        if (chars[i]==toss) {
                                --len;
                                System.arraycopy(chars, i+1, chars, i, len-1);
                                --i;
                                }
                        }
                return new String(chars, 0, len-1);
                }
    

        public static StringBuffer addDate (StringBuffer buf) {
                /** ensureCapacity establece el tamanio del buffer ,
                en tanto que insert inserta en las posiciones iniciales y finales */
                String now = new java.util.Date().toString();
                buf.ensureCapacity(buf.length() + now.length() + 2);
                buf.insert(0,now).insert(now.length(), ": ");
                return buf;
                }
        public static void main(String [] args) {
                StringBuffer buf = new StringBuffer("hola a todos");
                Cadena cad= new Cadena();
                System.out.println((cad.addDate(buf)).toString());
                }

        }





