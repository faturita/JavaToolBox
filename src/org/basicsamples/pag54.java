package org.basicsamples;
class Attr {
        private String name;
        private Object value=null;

        public Attr(String nameOf) {
                name = nameOf;
                }

        public Attr(String nameOf, Object valueOf) {
                name=nameOf;
                value=valueOf;
                }

        public String nameOf() {
                return name;
                }

        public Object valueOf() {
                return value;
                }

        public Object valueOf(Object newValue) {
                Object oldVal=value;
                value=newValue;
                return oldVal;
                }


        public static void main(String args[])
                {
                Attr atributo=new Attr("clasificado");
                String valor=null;
                System.out.println("The program has been executed succesfully");
                System.out.println(atributo.valueOf(valor));
                }

        }


