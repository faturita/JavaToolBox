package org.basicsamples;
/** Este programa muestra la implementacion de interfaces en java */


import java.util.*;


interface Atribuido {
   void add(Attr newAttr);

   Attr find(String attrName);

   Attr remove(String attrName);

   java.util.Enumeration attrs();
}


class AtribuidoImpl implements Atribuido {
   protected Hashtable attrTable = new Hashtable();
   // La clase Hashtable se importa desde java.util

   public void add(Attr newAttr) {
      attrTable.put(newAttr.nameOf(), newAttr);
   }

   /** Se obtiene un string con nameOf() el cual corresponde
    al nombre del objeto.  La HashTable asocia el nombre con
    el objeto, que se almacena en la misma tabla. */

   public Attr find(String name) {
      return (Attr) attrTable.get(name);
   }


   public Attr remove(String name) {
      return (Attr) attrTable.remove(name);
   }

   public Enumeration attrs (){
      return attrTable.elements();
   }
}
