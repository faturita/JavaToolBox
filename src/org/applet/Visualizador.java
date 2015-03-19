package org.applet;
/**
 * UDP Visualizer and verified application.
 * 
 * This is an applet application.
 * 
 */

import java.awt.*;

import org.udp.MsgSnd;

public class Visualizador extends java.applet.Applet {
    private Button    b1;
    private TextField t1;

    public void init() {
        /** Inicializador si es ejecutada como un applet */
        setLayout(new FlowLayout());
        add(new Label("Message Sender"));
        add(t1 = new TextField("", 100));
        add(b1 = new Button("Enviar"));
        System.out.println("Host:" + getCodeBase().getHost());
    }

    public boolean action(Event eve, Object ob) {
        if ((eve.target instanceof Button)) {
            String str = new String(t1.getText());
            System.out.println("Llego.");

            byte[] buf = new byte[256];
            str.getBytes(0, (str.length() > 256) ? 255 : str.length(), buf, 0);
            MsgSnd Msnd = new MsgSnd(4000, "127.0.0.1");
            try {
                Msnd.Snd(buf);
                System.out.println("Msg Send");
            } catch (Exception e) {
            }
        }
        return true;
    }
}
