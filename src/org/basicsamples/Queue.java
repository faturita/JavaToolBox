package org.basicsamples;
/** Este programa corresponde a la pagina 176 del libro */
/**
 * Esta cola quita y agrega elementos en forma concurrente de mutua exclusion
 * evitando asi corrupcion en los datos, y si no hay datos, espera a que alguien
 * los inserte o que se cancele el programa
 */

class QueueManager extends Thread {
    Queue   cola;
    String  cad1;
    boolean comienza;

    public QueueManager(Queue colaN, String elemento, boolean strt) {
        cola = colaN;
        cad1 = elemento;
        comienza = strt;
    }

    // run no lleva parametros ni puede devolver nada.
    public void run() {
        // Agregar datos a la cola
        if (comienza) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                return;
            }

            cola.append(new Element(cad1));
            System.out.println("Fueron escritos datos en la cola...");

        } else {
            System.out
                    .println("Esperando que se graben datos en la cola.......");
            System.out.println("Datos extraidos :" + (String) cola.get().data);
        }
        return;
    }

    public static void main(String[] args) {
        Queue q = new Queue();
        new QueueManager(q, "PrimerThread", true).start();
        new QueueManager(q, "SegundoThread", false).start();
    }

}

class Element {
    public Object  data;

    public Element next;

    public Element(Object o) {
        data = o;
    }

}

class Queue {
    Element head, tail;

    public synchronized void append(Element p) {
        if (tail == null)
            head = p;
        else
            tail.next = p;
        p.next = null;
        tail = p;
        notify();
    }

    public synchronized Element get() {
        try {
            while (head == null)
                wait();
        } catch (InterruptedException e) {
            return null;
        }
        Element p = head;
        head = head.next;
        if (head == null)
            tail = null;
        return p;
    }

}
