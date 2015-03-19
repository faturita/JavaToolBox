package org.applet;
import java.awt.*;
import java.applet.*;

public class MiApplet extends Applet {
        public void init() {
                 setLayout(new BorderLayout());
                 add("North",new Label("Este es un applet",Label.CENTER));
                 add("South",new TextField("Hi everybody...",5));
                 }

        public void paint(Graphics g) {
                g.setColor(new Color(10,10,10));
                g.setFont(new Font("Times New Roman",Font.BOLD,10));
                g.drawString("I wish that you can see this...",1,1);
                }

        }




