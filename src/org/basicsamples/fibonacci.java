package org.basicsamples;

class Fibonacci {
    static final int MAX = 50;

    /** Imprimir la secuencia Fibonacci para valores >50 */
    public static void main(String[] args) {
        int hi = 1;
        int lo = 1;
        int[] nn = new int[MAX];
        System.out.println("Secuencia Fibonacci....");
        System.out.println(hi);
        for (int i = 0; i < 40; i++) {
            nn[i] = hi;
            hi = hi + lo;
            lo = hi - lo;
        }
        for (int i = 0; i < 40; i++) {
            System.out.println(nn[i]);
        }
    }
}
