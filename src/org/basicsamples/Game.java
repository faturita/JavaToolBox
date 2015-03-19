package org.basicsamples;
/** Este ejemplo ilustra como utilizar la clase ClassLoader para la carga
RUNTIME de clases para ser ejecutadas, por el mecanismo non-default del siste
ma java. */

/** @explain:
* Este codigo trabaja con Jugador.java, clase1.java, clase2.java, Game.java (
* este archivo).
* Una vez generado el bytecode de clase1 y clase2 hay que renombrar los archivos
* a clase1 y clase2 (sin la extension .class).
* Todas estas clases deben ser publicas o deben estar en el mismo paquete.
*/


import java.util.*;
import java.io.*;

public class Game {
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
        /** @method
        * Genera un objeto juego.
        * Luego busca entre los jugadores, y carga la clase que corresponde
        * a lo que ellos deseen hacer.
        */
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
    /** loadClass(name, resolve):
        encargada de cargar la clase de alguna manera y devolver un objeto
        Class que haga referencia a ella.

        defineClass(bytes[], inicio, fin)
        devuelve un objeto class basado en los bytes que se le han pasado al mismo

        findSystemClass(name)
        devuelve un objeto class si encuentra la clase en el sistema incluyendo
        todos los senderos de classpath.

        resolveClass(Class clase)
        se encarga de cargar todas las clases que sean necesarias para la clase
        que se cargo.
        */

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
        /** Carga de un archivo los bytes code de la clase que se desea cargar. */
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



        
