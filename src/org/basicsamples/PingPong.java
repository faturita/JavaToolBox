package org.basicsamples;
/**
 * Threading usage sample for Java
 * 
 * @author rramele
 */
public class PingPong extends Thread {
    private String word;
    int            delay;

    PingPong(String whatToSay, int delayTime) {
        word = whatToSay;
        delay = delayTime;
    }

    public void run() {
        try {
            for (;;) {
                System.out.print(word + " ");
                sleep(delay);
            }
        } catch (InterruptedException e) {
            // Finaliza el thread por una interrupcion
            return;
        }
    }

    public static void main(String[] args) {
        new PingPong("ping", 33).start();
        new PingPong("PONG", 100).start();
    }

}

class RunPingPong implements Runnable {
    String word;
    int    delay;

    RunPingPong(String whatToSay, int delayTime) {
        word = whatToSay;
        delay = delayTime;
    }

    public void run() {
        try {
            for (;;) {
                System.out.print(word + " ");
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    public static void main(String[] args) {
        Runnable ping = new RunPingPong("ping", 33);
        Runnable pong = new RunPingPong("PONG", 100);
        new Thread(ping).start();
        new Thread(pong).start();
    }
}
