package org.basicsamples;
/** Clases de prueba */



class Vehiculo {
   private float velocidad;
   private String propietario;
   private float direccion;
   static final float GIRAR_IZQUIERDA = 180;
   static final float GIRAR_DERECHA = 0;
   private int IdNumber=0;
   private static int Id=0;

   public static int DevuelveId() {   // Metodo estatico
          return Id;
          }
   public String toString () {
          
          return propietario;
          }


   public Vehiculo() {
          velocidad=0;
          propietario="Anonimo";
          direccion=0;
          Id++;
          IdNumber=Id;
          }

   public Vehiculo (String prop) {
          this();
          propietario=prop;
          }

   public void cambiarVelocidad (float vel) {
          velocidad = vel;
          }

   public void parar () {
          velocidad = 0;
          }

   public void girar (float angulo) {
          direccion = angulo;
         }
   

   public float Speed () {
          return velocidad;
          }
   public float Direction () {
          return direccion;
          }
   public String Owner () {
          return propietario;
          }



   public static void main(String[]args) {
       Vehiculo auto1 = new Vehiculo(args[0]);  // pone el nombre del propietario que se puso en la linea de comando
       Vehiculo auto2 = new Vehiculo(args[1]);
       Vehiculo auto3 = new Vehiculo(args[2]);
       System.out.println("Auto1:");
       System.out.println(auto1.Speed() + " " + auto1.Owner() + " " + auto1.Direction());
       System.out.println("Auto2:");
       System.out.println(auto2.Owner());
       System.out.println("El due¤o del auto1 es:"+auto1);


   }
}

class VehiculoPasajeros extends Vehiculo {
   private int Asientos;
   private int Ocupados;
   public VehiculoPasajeros (){  // Constructor de esta clase
          Asientos=30;
          Ocupados=0;
          }
   public VehiculoPasajeros (String propietario) {
          super(propietario);
          }
   public int Capacidad() {   // Devuelve la capacidad de asientos del vehiculo
          return Asientos;
          }
   public String Ocupado () {    // Devuelve el porcetage de los asientos que estan ocupados
          return (Ocupados*100/Asientos+" %");
          }

   public void CambiaPasajeros(int Pasajeros) {
          if (Pasajeros>Asientos)
              {System.out.println(Pasajeros-Asientos+" no pudieron ingresar al vehiculo");
               System.out.println("Vehiculo lleno.");
              }
          else
              Ocupados=Pasajeros;
          }


          
   public static void main(String[]args) {
          VehiculoPasajeros Scania = new VehiculoPasajeros(args[0]);
          VehiculoPasajeros Mercedes = new VehiculoPasajeros();
          VehiculoPasajeros Ferrari  = new VehiculoPasajeros("FATU");
          Ferrari.CambiaPasajeros(22);    // Cambia los pasajeros en Ferrari
          System.out.println(Ferrari.Ocupado());            // Devuelve el porcetaje de ocupado
          Mercedes.CambiaPasajeros(100);   // Cambia los pasajeros de Mercees
          System.out.println(Mercedes.Ocupado());           // Devuelve el porcetage ocupado de Mercedes
          System.out.println("Ferrari:"+Ferrari);  // Tiene que devolver el propietario de Ferrari por ser esta una rutina ToString de la clase que es heredada
          System.out.println("Direccion de Ferrari:"+Ferrari.Direction());
          }
  }

          







class ListaEnlazada {
   /* Esta clase muestra la utilizacion de objetos y miembros estaticos
   dentro de las clases.  Ademas muestra la utilizacion de objetos Exception
   para preveer las situaciones que pueden derivar en errores. */

   private Object datos;
   private ListaEnlazada proximo;
   private static int contador = 0;

   public ListaEnlazada() {
       datos = null;
       proximo = null;
       contador++;
       }

   public int Cantidad () {
       return contador;
       }

   public void Siguiente (ListaEnlazada nod) {
       proximo = nod;
       }

   public void Llenar (Object data) {
       datos = data;
       }

   public Object Ver () {
        return datos;
        }

   public ListaEnlazada find(Object data) throws ObjectNotFoundException {
        /* Se llama al find del primero y este va llamando a los demas. */
        if (datos.equals(data))
                {
                return (this);
                }
        else
                {
                if (proximo!=null)
                        return proximo.find(data);
                else
                        /* return null; */
                        throw new ObjectNotFoundException(data);
                }
        }
                
   public static void main(String[]args) throws ObjectNotFoundException {
       ListaEnlazada nodo1 = new ListaEnlazada();
       ListaEnlazada nodo2 = new ListaEnlazada();
       ListaEnlazada nodo3 = new ListaEnlazada();
       nodo1.Siguiente(nodo2);
       nodo1.Llenar("primero");
       nodo2.Siguiente(nodo3);
       nodo2.Llenar("segundo");
       nodo3.Siguiente(null);
       nodo3.Llenar("tercero");
       System.out.println("Cantidad de elementos"+ListaEnlazada.contador);
       System.out.println((nodo1.find("quinto")).Ver());


       }
   }


class ObjectNotFoundException extends Exception {
        /* Esta clase esta dise¤ada para trabajar con la clase ListaEnlazada */
        
        public Object searchedData;
        
        public ObjectNotFoundException (Object datos)
                {
                super("No hay ningun nodo que contenga los datos requeridos:"+(String)datos);
                searchedData=datos;
                }
        }

        
