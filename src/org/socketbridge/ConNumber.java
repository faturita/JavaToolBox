package org.socketbridge;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: Apr 21, 2010
 * Time: 2:24:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConNumber {
    private int conN;

    public ConNumber(int con) {
        conN = con;
    }

    public synchronized void inc() {
        conN++;
    }

    public synchronized void dec() {
        conN--;
    }

    public boolean lessEq(int rival) {
        return (conN <= rival);
    }


    public synchronized void reBuild(int con) {
        conN = con;
    }

    public int getNumber() {
        return conN;
    }

}
