package org.applet;
import java.awt.*;

public class MiApplet3 extends java.applet.Applet {
  Button b1;
  TextField f1;
  String cadena;
  public void init() {
                /** Establece formato de disposicion. */
                setLayout (new FlowLayout());
                
                add((f1=new TextField("Write here the formulae",10)));
                add((b1=new Button("Go !")));
                cadena=new String("Hola a todos.");
                }

  public boolean action (Event eve, Object obj)
                {
                if (eve.target instanceof Button)
                        {
                        if ( ((String)obj).equals(new String("Go !s")) )
                                {
                                cadena=new String("La verdad es ahora.");
                                f1.setText("Ahora si");
                                }
                        }
                return true;        
                }

                        

  public void paint(Graphics g) {
                g.setColor(new Color(255,0,0));
                g.setFont(new Font("Courier New",20,Font.PLAIN));
                g.drawString(cadena,40,40);
                }
  public String getAppletInfo() {
    return "Draws.";
  }
}
