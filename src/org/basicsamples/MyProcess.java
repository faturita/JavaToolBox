package org.basicsamples;
public class MyProcess {
    
    
    public int ExecuteProcess(String[] process) {
        int retVal = 0;
        try {
            Process setBrandProc = Runtime.getRuntime().exec(process);
            retVal = setBrandProc.waitFor();
        } catch (Exception ex) {
            retVal = 1;
        }
        return retVal;
    }

    Thread thr  = null;

    String lock = "";

    public void letsdoit() throws Exception {

        thr = new Thread() {

            public void run() {
                try {
                    System.out.println("Thread has been started.");
                    synchronized (lock) {
                        System.out.println("Waiting at do....");
                        lock.wait(1000 * 100 * 60);
                    }
                    System.out.println("Time expired on thread.");
                } catch (Exception ex) {
                    System.out.println("Thread has been interrupted.");
                }

            }

        };
        thr.start();
    }

    public void free() throws Exception {

        try {
            synchronized (Thread.currentThread()) {
                System.out.println("Waiting at free....");
                Thread.currentThread().wait(1000 * 1 * 60);
            }
        } catch (Exception ex) {
            System.out.println("Has been interrupted....");
        }

        System.out.println("Releasing....");

        synchronized (lock) {
            lock.notify();
        }
    }

    public void letsdo() throws Exception {

        synchronized (Thread.currentThread()) {
            System.out.println("Waiting....");
            Thread.currentThread().wait(1000 * 1 * 60);
        }

        String[] parms = { "C:\\Windows\\System32\\cmd.exe",
                "/c \"C:\\Work\\Java\\Run.bat \" " };
        ExecuteProcess(parms);

        synchronized (Thread.currentThread()) {
            System.out.println("Waiting....");
            Thread.currentThread().wait(1000 * 1 * 60);
        }
    }

    public static void main(String[] args) throws Exception {
        MyProcess myProcess = new MyProcess();
        myProcess.letsdoit();
        myProcess.free();
    }
}