package org.basicsamples;
/** Este programa ejemplifica la utilizacion de interfaces. */


import java.util.Enumeration;


class CuerpoAtribuido extends Cuerpo
        implements Atribuido
                {
                AtribuidoImpl attrImpl = new AtribuidoImpl();

                CuerpoAtribuido() {
                        super();
                        }


                // Reenviar todas las llamadas al objeto attrImpl

                public void add(Attr newAttr)
                        { attrImpl.add(newAttr); }
                public Attr find(String name)
                        { return attrImpl.find(name); }
                public Attr remove(String name)
                        { return attrImpl.remove(name); }
                public Enumeration attrs()
                        { return attrImpl.attrs(); }

}


