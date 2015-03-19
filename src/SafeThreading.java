public class SafeThreading {
    public static void safeExit(int status) {
        // Obtiene la lista de este thread en ejecucion
        Thread trdBase = Thread.currentThread();
        ThreadGroup trdGroup = trdBase.getThreadGroup();
        int count = trdGroup.activeCount();
        Thread[] trds = new Thread[count + 20];
        trdGroup.enumerate(trds);

        // Detener todos los threads .
        for (int i = 0; i < trds.length; i++) {
            if (trds[i] != null && trds[i] != trdBase)
                trds[i].stop();
        }

        // Esperar a que se terminenen....
        for (int i = 0; i < trds.length; i++) {
            if (trds[i] != null && trds[i] != trdBase) {
                try {
                    trds[i].join();
                } catch (InterruptedException e) {
                }
            }
        }

        System.exit(status);
    }

    public static void main(String[] args) {
        SafeThreading.safeExit(1);
    }
}
