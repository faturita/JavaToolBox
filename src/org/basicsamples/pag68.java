package org.basicsamples;
/** Implementacion y utilizacion de clases abstracatas en java */

abstract class Benchmark {
        abstract void benchmark();

        public long repeat(int count) {
                long start = System.currentTimeMillis();
                for (int i=0;i<count;i++)
                        benchmark();
                return (System.currentTimeMillis() - start);
                }
        }

/** Esta clase es la implementacion de una derivacion de la clase abstract */
/** parseInt(string) es un metodo de Integer que convierte a integer una cadena */
/** System.currentTimeMillis() Devuelve el tiempo actual en milisegundos */

class MethodBenchmark extends Benchmark {
        void benchmark() {
                }

        public static void main(String [] args) {
                int count=Integer.parseInt(args[0]);
                long time=new MethodBenchmark().repeat(count);
                System.out.println(count + " methods in "+time+" milisegundos.");
                }
        }

