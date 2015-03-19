package org.applet;
/** Es necesario incluir el paquete de las librerias AWT de JAVA para WinX */
import java.awt.*;

public class AwtExample extends Frame {
    public AwtExample(String sp) {
        super(sp);
        resize(300, 300);
        setLayout(new BorderLayout());
        add("North", new Button("Press Here"));
        add("South", new Label("hola a todos....", Label.CENTER));

        show();
    }

    public void paint(Graphics gp) {
        gp.setColor(Color.red);
        System.out.println("Se ejecuta el paint...");
        gp.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        gp.drawString("Espero que esto se vea", 100, 100);
    }

    public static void main(String[] args) {
        AwtExample frm = new AwtExample("Hola a todos");
    }

}
