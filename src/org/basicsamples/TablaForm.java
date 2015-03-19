package org.basicsamples;
/** Universidad Nacional de La Matanza  -- Laboratorios de Fisica/Quimica
        ClaseFormula:  Contiene codigo para almacenar y retribuir informacion
                relacionada con un topico(es la clave de la tabla)
        TablaForm:  Applet que utiliza a la clase anterior para visualizar
                los resultados de consultas por la clave(topico)

        Creacion:  28 de Septiembre de 1999 por Rodrigo Ramele
        Modificiones:
                - 19 de Octubre de 1999 por Rodrigo Ramele
                        Codigo para optimizar la busqueda.

        Utilizacion:
                Este applet se utiliza con la pagina TableForm.htm
                Para poder utilizarlo es necesario configurar el navegador
                para que permita al apple leer sobre el sistema de archivos.

                Para ver el programa sin el navegador utilicese:

                        appletviewer TablaForm.htm

                Para introducir mas datos es necesario utilizar el interprete
                de java (java.exe), para asi poder actualizar la base de datos
                que, por cuestiones de seguridad de los applets no puede hacerse
                desde la pagina.

                Sintaxis:  java ClaseForm topico "formula"
 */

import java.awt.*;


public class TablaForm extends java.applet.Applet {
    private Button       b1;
    private TextField    f1;
    private List         l1;
    private ClaseFormula formula;

    public void init() {
        /** Establece formato de layout de los componenetes */
        setLayout(new FlowLayout());

        add((new Label("Topico:")));
        add((f1 = new TextField("", 10)));
        add((b1 = new Button("Ver Formulas")));
        add((l1 = new List(10, false)));

        try {
            formula = new ClaseFormula(ClaseFormula.DEFAULT);
        } catch (SecurityException e) {
            l1.addItem("Debe reconfigurar la seguridad de su explorador...");
        }
    }

    public boolean action(Event eve, Object obj) {
        if (eve.target instanceof Button) {
            if (((String) obj).equals(new String("Ver Formulas"))) {
                if ((f1.getText()).length() != 0) {
                    String[] result = formula.query(f1.getText());
                    l1.addItem("Buscando datos.....");
                    if (result != null) {
                        l1.clear();
                        int in = 0;
                        while (result[in] != null) {
                            l1.addItem(result[in]);
                            in++;
                        }
                    }

                }
            }
        }
        return true;
    }

    /**
     * public void paint(Graphics g) { }
     */
    public String getAppletInfo() {
        return "Permite buscar formulas por topico.";
    }
}
