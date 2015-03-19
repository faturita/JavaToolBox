package org.basicsamples;
/** Ejemplo de los Objetos Class y sus metodos.
* Todos los sistemas tienen el sistema de clases activo para todos los
* programas que se ejecuten en el.
* Las referencias a las clases se deben hacer con su sendero java completo.
* Class usa:  forName(clase) obteniendo una clase en class.
*          :  isInterface() devuelve true en caso de ser interface
*          :  getInterfaces() devulve un array de class.
*          :  getSuperClass() devuelve la clase super.
*/


public class ObjectClass {
    public static void main(String [] args) {
        ObjectClass desc=new ObjectClass();
        for (int i=0;i<args.length;i++) {
            try {
                desc.printType (Class.forName(args[i]),0);
            } catch (ClassNotFoundException e) {
                System.err.print(e);
                }
            }
        }

    public java.io.PrintStream out=System.out;
    private static String[]
        basic    = {"clase","interfaz"      },
        extended = {"extiende","implementa" };

    public void printType (Class type, int depth) throws ClassNotFoundException {

        /** @see Los dos ifs siguientes muestran que para la clase Class, los
        objetos que devuelve forName son instancias de CLASS unicas, que se refieren
        al mismo elemento.  Esto es que dos objetos creados con forName con java.lang.Object
        se estan refiriendo como clases a esta clase los dos a la vez.  Esto no significa
        que no se puedan crear con newInstance() todos los objetos que se deseen y que
        son todos diferentes, en cuyo caso, si se los compara con == va a dar FALSE. */

        if (type==null)
            return;
        
/*        if (type.equals(Class.forName("java.lang.Object")))
            return;*/

        if (type==(Class.forName("java.lang.Object")))
            return;

        for (int i=0;i<depth;i++)
            out.print(" ");
        String[] labels = (depth == 0 ? basic:extended);
        out.print(labels[type.isInterface() ? 1 : 0] + " ");
        out.println(type.getName());

        Class[] interfaces = type.getInterfaces();
        for (int i=0;i<interfaces.length;i++)
            printType(interfaces[i], depth + 1);

        printType(type.getSuperclass(), depth + 1);
        }
    }
                
