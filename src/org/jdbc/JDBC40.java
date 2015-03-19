package org.jdbc;
import java.net.*; //import ORG.as220.tinySQL.*;
import java.sql.*;

class jdbc_update {
    public static void main(String[] argv) {
        try {
            // Registracion del driver de jdbc para tinySQL
            Class.forName("ORG.as220.tinySQL.textFileDriver").newInstance();

            // Toma los parametros de la url de la linea de comando.
            String url = argv[0];
            String user = argv[1], pwd = argv[2];
            // Realiza la conexion al URL especifico.

            Connection con = DriverManager
                    .getConnection("jdbc:tinySQL", "", "");
            System.out.println("Connection established");

            // Crea un objeto Statement sentencia de SQl
            Statement stmt = con.createStatement();

            // Crea una tabla con dos columnas.
            stmt.executeUpdate("CREATE TABLE test (name CHAR(25), id INT)");

            // Inserta datos en la tabla recien creada.
            stmt
                    .executeUpdate("INSERT INTO test (name,id) VALUES ('Usuario1',1)");
            stmt
                    .executeUpdate("INSERT INTO test (name,id) VALUES ('Usuario2',2)");

            stmt.close();
            con.close();
            System.out.println("Operacion realizada satisfactoriamente.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
