package org.basicsamples;
class Garage implements Cloneable {
        public Vehiculo[] autos;

        public Garage(int cant) {
                autos=new Vehiculo[cant];
                }

        public Object clone() {
                try {
                        Garage gar=(Garage)super.clone();
                        gar.autos = (Vehiculo[])autos.clone();
                        return gar;
                     } catch (CloneNotSupportedException e) {
                        throw new InternalError(e.toString());
                        }
                }

        public static void main(String [] args)
                {
                Garage gar=new Garage(4);
                Vehiculo v=new Vehiculo();
                gar.autos[1]=v;
                gar.autos[2]=v;
                Garage ger=(Garage)gar.clone();
                ger.autos[0]=new Vehiculo("Perez");
                gar.autos[0]=new Vehiculo("Gonzales");
                System.out.println("Se ha realizado una clonacion.");
                System.out.println(ger.autos[0]);
                System.out.println(gar.autos[0]);
                }

        }

