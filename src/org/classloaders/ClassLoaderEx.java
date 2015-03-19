package org.classloaders;
/** Este ejemplo ilustra como utilizar la clase ClassLoader para la carga
RUNTIME de clases para ser ejecutadas, por el mecanismo non-default del siste
ma java. */

import java.util.*;
import java.io.*;

class Jugador {
    public void play (Game g) {
        }

    }


        

class Game {
    Hashtable points = new Hashtable();
    private final String[] names = {"clase1","clase2"};
    private int index=0;

    public void addPoints (int ind, int pointss) {
        points.put(names[ind],String.valueOf(pointss));
        }

    public String getNextJugador() {
        if (index==names.length)
            return (null);
        else
            return (names[index++]);
        }

    public void GameOver() {
        Enumeration el = points.elements();
        while (el.hasMoreElements())
            System.out.println (el.nextElement());
        }

    public void reportScore(String name) {
        System.out.println ("El puntaje de "+name+" es "+points.get(name));
        }

    static public void main(String [] args) {
        String name;
        Game game=new Game();
        while ((name = game.getNextJugador()) != null) {
            try {
                JugadorLoader loader = new JugadorLoader();
                Class classOf = loader.loadClass(name, true);
                Jugador jugador = (Jugador)classOf.newInstance();
                jugador.play(game);
                game.reportScore(name);
                } catch (Exception e) {
                    System.err.println("ERROR:"+name+e);
                    System.exit(0);
                    }
            }
        game.GameOver();
        }
    }


class JugadorLoader extends ClassLoader {
    private Hashtable Classes = new Hashtable();

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException
        {
        try {
            Class newClass = (Class)Classes.get(name);
            if (newClass == null)
                {
                try {
                    newClass = findSystemClass(name);
                    if (newClass != null)
                        return newClass;
                    }
                catch (ClassNotFoundException e) {
                    ;
                    }
                /* En este punto se hace necesario cargar la clase que no se encuentra
                en el sistema "findSystemClass" ni se encuentra en nuestra tabla de hash
                por lo que todavia no ha sido cargada */

                                            
                byte [] buf= bytesForClass(name);
                newClass = defineClass(buf,0,buf.length);
                Classes.put(name, newClass);
                }
            if (resolve)
                resolveClass(newClass);
            System.out.println ("La carga de la clase fue exitosisima!");
            return newClass;
            }
        catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
            }
        }
    protected byte[] bytesForClass(String name) throws IOException, ClassNotFoundException
        {
        FileInputStream in=new FileInputStream(name);
        int length=in.available();
        if (length==0)
            throw new ClassNotFoundException(name);
        byte[] buf = new byte[length];
        in.read(buf);
        System.out.println ("La clase "+name+" ha sido cargada.");
        return buf;
        }

    }



        
