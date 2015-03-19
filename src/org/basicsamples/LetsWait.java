package org.basicsamples;
public class LetsWait {
    public void letsdo() throws Exception {
        synchronized (Thread.currentThread()) {
            System.out.println("Waiting....");
            Thread.currentThread().wait(1000 * 3 * 60);
        }
    }

    public static void main(String[] args) throws Exception {
        LetsWait myProcess = new LetsWait();
        myProcess.letsdo();
    }
}