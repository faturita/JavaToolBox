package org.basicsamples;
/** Utilizacion de clone */
/** Clone copia por defecto pero hay casos donde debe hacerse obligatoriamente */

class IntegerStack implements Cloneable {
        private int[] buffer;
        private int top;

        public IntegerStack(int cantidadEl) {
                buffer = new int[cantidadEl];
                top=-1;
                }

        /** Cuando llama a buffer.clone() llama a clone definido para copiar
                un array.  Una vez que se duplica correctamente el array se
                devuelve la referencia a este nuevo array, y asi se tiene
                todo el objeto pila duplicado, porque los otros valores, que
                fueron copiados por super.clones, ya estan correctos porque
                son solo por "valor". */


        public Object clone() {
                try {
                        IntegerStack newOb = (IntegerStack)super.clone();
                        newOb.buffer=(int[])buffer.clone();
                        return newOb;
                        } catch (CloneNotSupportedException e) {
                                throw new InternalError(e.toString());
                                }
                }                        

        public void push(int el) {
                buffer[++top]=el;
                }

        public int pop() {
                return (buffer[top--]);
                }

        public static void main (String [] args)
                {
                IntegerStack first=new IntegerStack(3);
                first.push(2);
                first.push(9);
                IntegerStack second = (IntegerStack)first.clone();
                System.out.println("El valor sacado de la primer pila es :"+first.pop());
                System.out.println("El valor sacado de la segunda pila es :"+second.pop());
                System.out.println("Si estos valores no coinciden significa que la copia se hizo mal....");
                }
        }


                




