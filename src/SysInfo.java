public class SysInfo {
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        System.out.println("La memoria disponible es:" + rt.freeMemory());
        System.out.println("La memoria total  es  de:" + rt.totalMemory());
        System.out.println("Propiedades del sistema:");
        System.getProperties().list(System.out);
    }
}
