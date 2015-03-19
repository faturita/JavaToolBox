package org.basicsamples;
class X {
    protected int xMask = 0x00ff;
    protected int fullMask;

    public X() {
      System.out.println("Ejecutando el constructor de la SUPERclase X");
      System.out.println("fullmask:"+fullMask);
      fullMask = xMask;
      System.out.println(fullMask);
      }
    public int mask(int orig) {
      return (orig & fullMask);
      }
    }

class Y extends X {
    protected int yMask = 0xff00;

    public Y() {
      System.out.println("Ejecutando el constructor de la subclase Y");
      System.out.println(fullMask);
      fullMask |= yMask;
      System.out.println(fullMask);
      }
    public static void main(String[]args) {
      System.out.println ("Inicio del programa.");
      Y ObjetoY = new Y();
    }
       


    }




