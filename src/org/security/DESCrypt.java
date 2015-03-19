package org.security;
import java.util.BitSet;

/**
 * Created by IntelliJ IDEA. User: vexrarod Date: 04/09/2003 Time: 09:49:19 To
 * change this template use Options | File Templates.
 * 
 * Local implementation of Simple DES algorithm.
 * 
 * Very simple and fast !
 * 
 * <a href=
 * "http://www.mapleapps.com/powertools/cryptography/HTML/DES-KeyExpansion.html"
 * /> <a href=
 * "http://www.mapleapps.com/powertools/cryptography/HTML/DES-Example.html" />
 */

class Bits extends BitSet {
    private int size;

    public Bits() {
        super(64);
        size = 64;
    }

    public Bits xor(Bits s) {
        super.xor(s);
        return this;
    }

    public int size() {
        return size;
    }

    public Bits(int lIndex) {
        super(lIndex);
        size = lIndex;
    }

    public void set(int iIndex, boolean bval) {
        if (bval) {
            set(iIndex);
        } else {
            clear(iIndex);
        }
    }

    public void copyFrom(int iToFrom, Bits b, int iFrom, int iLength) {
        for (int i = 0; i < iLength; i++) {
            set(i + iToFrom, b.get(i + iFrom));
        }
    }

    public void copyFrom(Bits b, int iTo, int iLength) {
        copyFrom(0, b, iTo, iLength);
    }

    public void copyFrom(Bits b, int iLength) {
        copyFrom(0, b, 0, iLength);
    }

    public void copyFrom(Bits b) {
        copyFrom(b, b.size());
    }

    public void copyFrom(int iToFrom, Bits b) {
        copyFrom(iToFrom, b, 0, b.size());
    }

    public int getInt(int iIndex) {
        if (get(iIndex))
            return 1;
        else
            return 0;
    }

    public Object clone() {
        Bits b = new Bits(this.size());

        b.copyFrom(this);

        return b;
    }

    public boolean add(int iIndex, boolean bVal) {
        set(iIndex, (bVal || get(iIndex)));

        return get(iIndex);
    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < size(); i++) {
            str.append(getInt(i));
        }

        return str.toString();
    }

    public Bits add(Bits b) {
        load(b.toLong() + toLong());
        return this;
    }

    public long toLong() {
        long a = 0;
        long exp = 1;
        for (int i = size() - 1; i >= 0; i--) {
            a += (exp * getInt(i));
            exp *= 2;
        }

        return a;
    }

    public void load(long iVal) {
        for (int i = 0; i < size(); i++) {
            // System.out.println ((iVal>>i)+""+((iVal >> i) & (0x01)));

            if (((iVal >> i) & (0x01)) > 0) {
                set(size() - 1 - i);
            } else {
                clear(size() - 1 - i);
            }

        }
    }

    public boolean exGet(int iIndex) {
        if (iIndex >= size()) {
            return (get(iIndex - size()));
        } else
            return (get(iIndex));

    }

    public Bits lshifts(int offset) {
        Bits aux = (Bits) clone();

        for (int i = 0; i < size(); i++) {
            set(i, aux.exGet(i + offset));
        }

        return this;
    }

}

public class DESCrypt {
    int[]   IP     = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20,
            12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16,
            8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61,
            53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

    int[]   IP_    = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23,
            63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61,
            29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

    int[]   E      = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12,
            13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23,
            24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

    int[]   P      = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31,
            10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };

    int[][] S      = {
            { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7,
            4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2,
            11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3,
            14, 10, 0, 6, 13 },
            { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4,
            7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4,
            13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6,
            7, 12, 0, 5, 14, 9 },
            { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0,
            9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3,
            0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14,
            3, 11, 5, 2, 12 },
            { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11,
            5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7,
            13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5,
            11, 12, 7, 2, 14 },
            { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 1, 14, 9, 14, 11, 2,
            12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7,
            8, 15, 9, 12, 5, 6, 3, 1, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 1,
            9, 10, 4, 5, 3 },
            { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4,
            2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12,
            3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1,
            7, 6, 0, 8, 13 },
            { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11,
            7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7,
            14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0,
            15, 14, 2, 3, 12 },
            { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13,
            8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14,
            2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9,
            0, 3, 5, 6, 11 }, };

    int[]   PCA    = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36 };

    int[]   PCB    = { 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

    int[]   shifts = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    int[]   PC2    = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12,
            4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51,
            45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

    public static void streamcompress(String data, String sKey) {
        DESCrypt t = new DESCrypt();
        byte[] s = data.getBytes();
        byte[] outs = null;

        Bits[] plaintext = new Bits[s.length / 8 + 1];
        Bits[] cipher = new Bits[s.length / 8 + 1];

        Bits key = new Bits(64);

        key.load(0x133457799bbcdff3L);

        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < 8; j++) {
                boolean bSet = ((s[i] >> j) & 0x01) == 1 ? true : false;
                plaintext[i].set(i * 8 + j, bSet);
            }
            cipher[i] = t.des(plaintext[i], key, false);
            System.out.println(cipher);
        }
    }

    public static void main(String args[]) {
        streamcompress(args[0], args[1]);
    }

    public static void maind(String args[]) {
        DESCrypt t = new DESCrypt();
        Bits plaintext = new Bits(64);
        Bits key = new Bits(64);
        long a, b;

        plaintext.load(0x0123456789abcdefL);
        System.out.println(plaintext + "-Plaintext:" + plaintext.toLong());
        a = plaintext.toLong();

        key.load(0x133457799bbcdff3L);
        System.out.println(key + "-Key      :" + key.toLong());

        Bits out = t.des(plaintext, key, false);
        System.out.println(out + "-Cipher   :" + out.toLong());

        Bits in = t.des(out, key, true);
        System.out.println(in + "-Plaintext:" + in.toLong());
        b = in.toLong();

        if (a == b)
            System.out.println("Perfect match.!");
    }

    public Bits KS(int i, Bits Key) {
        Bits Cn = new Bits(28);
        Bits Dn = new Bits(28);

        for (int s = 0; s < 28; s++) {
            Cn.set(s, Key.get(PCA[s] - 1));
            Dn.set(s, Key.get(PCB[s] - 1));
        }

        for (int d = 0; d <= i; d++) {
            Cn.lshifts(shifts[d]);
            Dn.lshifts(shifts[d]);
        }

        Bits b1 = new Bits(56);

        b1.copyFrom(Cn, 28);
        b1.copyFrom(28, Dn);

        Bits b2 = new Bits(48);

        for (int s = 0; s < 48; s++) {
            b2.set(s, b1.get(PC2[s] - 1));
        }

        // System.out.println("Key "+i+":"+b2);
        // This works perfectly.

        return b2;

    }

    public Bits f(Bits R, Bits K) {
        Bits b = (Bits) K.clone();
        Bits ER = new Bits(48);
        Bits Out = new Bits(32);

        // E(R)
        for (int i = 0; i < 48; i++) {
            ER.set(i, R.get(E[i] - 1));
        }

        // System.out.println("E(R):"+ER);
        // System.out.println("K:"+b);
        // K+E(R)
        // b.add(ER);
        b.xor(ER);

        // System.out.println("bin3:"+b);

        // b is the resulting from K(+)E(R)

        for (int i = 0; i < 8; i++) {
            Bits aux = new Bits(6);
            Bits outs = new Bits(4);

            aux.copyFrom(b, i * 6, 6);

            // System.out.println("Vector "+i+":"+aux);

            int row = aux.getInt(5) + aux.getInt(0) * 2;
            int col = aux.getInt(4) + aux.getInt(3) * 2 + aux.getInt(2) * 4
                    + aux.getInt(1) * 8;

            outs.load(S[i][row * 16 + col]);

            // Checked ok.

            Out.copyFrom(i * 4, outs, 0, 4);
        }

        // System.out.println("Salida:"+Out);

        Bits fOut = new Bits(32);

        for (int i = 0; i < 32; i++) {
            fOut.set(i, Out.get(P[i] - 1));
        }

        // System.out.println("Permuted:"+fOut);

        return fOut;
    }

    public Bits des(Bits plaintext, Bits key, boolean bDecrypt) {
        Bits cipher = new Bits(64);

        Bits La, Ra;
        Bits L = new Bits(32);
        Bits R = new Bits(32);

        Bits K = new Bits(48);

        // First permutation
        for (int j = 0; j < 64; j++) {
            cipher.set(j, plaintext.get(IP[j] - 1));
        }

        // This point ok
        for (int j = 0; j < 32; j++) {
            L.set(j, cipher.get(j));
            R.set(j, cipher.get(j + 32));
        }
        La = (Bits) L.clone();
        Ra = (Bits) R.clone();

        // this point is ok.

        // System.out.println("L 0:"+La);
        // System.out.println("R 0:"+Ra);

        for (int i = 0; i < 16; i++) {
            if (bDecrypt)
                K = KS(15 - i, key);
            else
                K = KS(i, key);
            L.copyFrom(Ra, 32);
            R.copyFrom(La.xor(f(Ra, K)));

            La = (Bits) L.clone();
            Ra = (Bits) R.clone();
            // System.out.println("L "+i+":"+La);
            // System.out.println("R "+i+":"+Ra);
        }

        Bits join = new Bits(64);
        Bits out = new Bits(64);

        // From here

        join.copyFrom(R, 32);
        join.copyFrom(32, L, 0, 32);

        // Ok here

        for (int i = 0; i < 64; i++) {
            out.set(i, join.get(IP_[i] - 1));
        }

        return out;
    }
}
/*
 * 
 * IP 58 50 42 34 26 18 10 2 60 52 44 36 28 20 12 4 62 54 46 38 30 22 14 6 64 56
 * 48 40 32 24 16 8 57 49 41 33 25 17 9 1 59 51 43 35 27 19 11 3 61 53 45 37 29
 * 21 13 5 63 55 47 39 31 23 15 7
 * 
 * 
 * public static void
 * 
 * IP-1
 * 
 * 40 8 48 16 56 24 64 32 39 7 47 15 55 23 63 31 38 6 46 14 54 22 62 30 37 5 45
 * 13 53 21 61 29 36 4 44 12 52 20 60 28 35 3 43 11 51 19 59 27 34 2 42 10 50 18
 * 58 26 33 1 41 9 49 17 57 25
 * 
 * 
 * 
 * E BIT-SELECTION TABLE
 * 
 * 32 1 2 3 4 5 4 5 6 7 8 9 8 9 10 11 12 13 12 13 14 15 16 17 16 17 18 19 20 21
 * 20 21 22 23 24 25 24 25 26 27 28 29 28 29 30 31 32 1
 * 
 * 
 * 
 * 
 * P
 * 
 * 
 * 16 7 20 21 29 12 28 17 1 15 23 26 5 18 31 10 2 8 24 14 32 27 3 9 19 13 30 6
 * 22 11 4 25
 * 
 * 
 * 
 * 
 * S1
 * 
 * 14 4 13 1 2 15 11 8 3 10 6 12 5 9 0 7 O 15 7 4 14 2 13 1 10 6 12 11 9 5 3 8 4
 * 1 14 8 13 6 2 11 15 12 9 7 3 10 5 0 15 12 8 2 4 9 1 7 5 11 3 14 10 O 6 13
 * 
 * S2
 * 
 * 15 1 8 14 6 11 3 4 9 7 2 13 12 O 5 10 3 13 4 7 15 2 8 14 12 0 1 10 6 9 11 5 0
 * 14 7 11 10 4 13 1 5 8 12 6 9 3 2 15 13 8 10 1 3 15 4 2 11 6 7 12 0 5 14 9
 * 
 * S3
 * 
 * 10 0 9 14 6 3 15 5 1 13 12 7 11 4 2 8 13 7 O 9 3 4 6 10 2 8 5 14 12 11 15 1
 * 13 6 4 9 8 15 3 0 11 1 2 12 5 10 14 7 1 10 13 0 6 9 8 7 4 15 14 3 11 5 2 12
 * 
 * S4
 * 
 * 7 13 14 3 0 6 9 10 1 2 8 5 11 12 4 15 13 8 11 5 6 15 O 3 4 7 2 12 1 10 14 9
 * 10 6 9 0 12 11 7 13 15 1 3 14 5 2 8 4 3 15 O 6 10 1 13 8 9 4 5 11 12 7 2 14
 * 
 * 
 * S5
 * 
 * 2 12 4 1 7 10 11 6 8 5 3 15 13 O 14 9 14 11 2 12 4 7 13 1 5 0 15 10 3 9 8 6 4
 * 2 1 11 10 13 7 8 15 9 12 5 6 3 O 14 11 8 12 7 1 14 2 13 6 15 O 9 10 4 5 3
 * 
 * 
 * S6
 * 
 * 12 1 10 15 9 2 6 8 O 13 3 4 14 7 5 11 10 15 4 2 7 12 9 5 6 1 13 14 O 11 3 8 9
 * 14 15 5 2 8 12 3 7 0 4 10 1 13 11 6 4 3 2 12 9 5 15 10 11 14 1 7 6 0 8 13
 * 
 * S7
 * 
 * 4 11 2 14 15 0 8 13 3 12 9 7 5 10 6 1 13 0 11 7 4 9 1 10 14 3 5 12 2 15 8 6 1
 * 4 11 13 12 3 7 14 10 15 6 8 0 5 9 2 6 11 13 8 1 4 10 7 9 5 0 15 14 2 3 12
 * 
 * 
 * S8
 * 
 * 13 2 8 4 6 15 11 1 10 9 3 14 5 0 12 7 1 15 13 8 10 3 7 4 12 5 6 11 0 14 9 2 7
 * 11 4 1 9 12 14 2 0 6 10 13 15 3 5 8 2 1 14 7 4 10 8 13 15 12 9 0 3 5 6 11
 * 
 * The primitive function P is: 16 7 20 21 29 12 28 17 1 15 23 26 5 18 31 10 2 8
 * 24 14 32 27 3 9 19 13 30 6 22 11 4 25
 * 
 * 
 * 
 * PC-1
 * 
 * 
 * 57 49 41 33 25 17 9 1 58 50 42 34 26 18 10 2 59 51 43 35 27 19 11 3 60 52 44
 * 36
 * 
 * 63 55 47 39 31 23 15 7 62 54 46 38 30 22 14 6 61 53 45 37 29 21 13 5 28 20 12
 * 4
 * 
 * 
 * Iteration Number of Number Left Shifts
 * 
 * 1 1 2 1 3 2 4 2 5 2 6 2 7 2 8 2 9 1 10 2 11 2 12 2 13 2 14 2 15 2 16 1
 * 
 * 
 * PC-2
 * 
 * 
 * 14 17 11 24 1 5 3 28 15 6 21 10 23 19 12 4 26 8 16 7 27 20 13 2 41 52 31 37
 * 47 55 30 40 51 45 33 48 44 49 39 56 34 53 46 42 50 36 29 32
 */
