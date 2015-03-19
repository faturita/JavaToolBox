package org.applet;
import java.awt.*;

public class MiApplet2 extends java.applet.Applet {
        public void paint(Graphics g) {
                setLayout(new FlowLayout());
                add(new Button("Presiona aqui..."));
                g.setColor(new Color(10,10,10));
                g.setFont(new Font("Times New Roman",Font.BOLD,20));
                g.drawString("What about now",30,30);
                
                }

        public String getAppletInfo() {
                return "This is a test.";
                }


        }




