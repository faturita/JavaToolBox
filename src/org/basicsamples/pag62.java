package org.basicsamples;
/* En este ejemplo se ve que se oculta el campo str en la clase extendida, pero
sigue existiendo el campo de la clase base.  Siendo que si se utiliza una referencia
a un objeto ExtendShow como SuperShow, aparece el valor de str de la clase base.
  Sin embargo con los metodos no pasa lo mismo, y en el la clase Extendshow el metodo
  hace desaparecer a el metodo show de la clase base. */

class SuperShow {
        public String str = "SuperStr";

        public void show() {
                System.out.println("Super.show "+str);
                }
        }

class ExtendShow extends SuperShow {

        public String str = "Extendstr";
        public void show() {
                System.out.println("Extend.show:"+str);
                }
        

        public static void main(String [] args) {
                ExtendShow ext=new ExtendShow();
                SuperShow sup=ext;
                sup.show();
                ext.show();
                System.out.println("sup.str ="+sup.str);
                System.out.println("ext.str ="+ext.str);
                }
        }


        
