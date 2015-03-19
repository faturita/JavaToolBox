package org.basicsamples;
/** Este programa visualiza como utilizar matrices en java, y cracion de matrices
de tama¤os diferentes para cada valor. Ademas muestra el metodo estatico de la
clase Integer (parseInt) y la utilizacion de Constructores (init para Java) */

class Triangulo {

        private int prof;
        private int[][] triPas;


        public Triangulo(int deep)
                {
                if (deep<2) deep=2;
                prof=deep;
                triPas=new int[deep][];
                triPas[0]=new int[1];
                triPas[0][0]=1;
                triPas[1]=new int[3];
                triPas[1][0]=1;triPas[1][1]=2;triPas[1][2]=1;
                for (int cont=2;cont<deep;cont++)
                        {
                        triPas[cont]=new int[cont+2];
                        triPas[cont][0]=1;
                        for (int contD=1;contD<cont+1;contD++)
                                {
                                triPas[cont][contD]=triPas[cont-1][contD-1]+triPas[cont-1][contD];
                                }
                        triPas[cont][cont+1]=1;
                        }
                System.out.println("Triangulo creado con una profundidad de:"+prof);
                }

        public void mostrar()
                {
                for (int cont=0;cont<prof;cont++)
                        {
                        for (int contD=0;contD<triPas[cont].length;contD++)
                                {
                                System.out.print("\t"+triPas[cont][contD]);
                                }
                        System.out.print("\n");
                        }
                }

        public static void main(String [] args)
                {
                Triangulo tri=new Triangulo(Integer.parseInt(args[0]));
                tri.mostrar();
                }


        }





