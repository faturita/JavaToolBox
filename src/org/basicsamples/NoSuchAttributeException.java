package org.basicsamples;
public class NoSuchAttributeException extends Exception {
        public String attrName;
        public Object newValue;

        NoSuchAttributeException(String name, Object value) {
                super ("No encontrado atributo llamado \""+name+"\"");
                attrName=name;
                newValue=value;
                }
        }

