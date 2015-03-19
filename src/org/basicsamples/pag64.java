package org.basicsamples;
class That {
        /** Devolver el nombre de la clase */
        protected String nm() {
                return "That";
                }
        }

class More extends That {
        protected String nm()
                {
                return "More";
                }
        protected void printNM()
                {
                That sref=new That();
                System.out.println("this.nm() = "+this.nm());
                System.out.println("sref.nm() = "+sref.nm());
                System.out.println("super.nm() = "+super.nm());
                }
        public static void main(String [] args)
                {
                More hola=new More();
                hola.printNM();
                }
        }

