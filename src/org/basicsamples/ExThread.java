package org.basicsamples;
/** Este programa da ejemplos de utilizacion de Threads en Java */

class classQueso {
    private int calorias;

    public classQueso() {
        calorias = 100;
    }

    public synchronized int cantCalorias() {
        return calorias;
    }

    public synchronized void comer() {
        if (calorias > 0)
            calorias--;
    }
}

class ComeQueso implements Runnable {
    /** Implementa la interface Runnable para poder depues generar un thread */
    int                         Priority;
    String                      mouseName;
    int                         quesoComido;
    private volatile classQueso queso;

    ComeQueso(int pri, String msName, classQueso quesito) {
        Priority = pri;
        mouseName = msName;
        quesoComido = 0;
        queso = quesito;
    }

    public void run() {
        /**
         * Este metodo se ejecuta automaticamente cuando se inicia el thread
         */
        System.out.println("Ha aparecido un nuevo raton:" + mouseName);
        System.out.println("Este raton comio queso.");
        queso.comer();
        System.out.println("Quedan:" + queso.cantCalorias());
    }

    public static void main(String[] args) {
        /** Se crea un threadGroup donde se alojaran todos los threads */
        ThreadGroup tGp = new ThreadGroup("Roedores");
        classQueso Queso = new classQueso();

        /** Se crean dos objetos ComeQueso */
        Runnable raton1 = new ComeQueso(1, "Perez", Queso);
        Runnable raton2 = new ComeQueso(2, "Gonzales", Queso);

        /** Se crean los dos objetos Threads */
        Thread t1 = new Thread(tGp, raton1, "Thread 2do");
        Thread t2 = new Thread(tGp, raton2, "Thread 1ro");
        t1.start();
        t2.start();
        System.out.println("Grupo de threads:" + t1.getThreadGroup());

        System.out
                .println("La cantidad de calorias que quedan en el queso son:"
                        + Queso.cantCalorias());

    }
}
