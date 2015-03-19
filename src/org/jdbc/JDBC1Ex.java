package org.jdbc;
import java.net.URL;
import java.sql.*;

class jdbc_connect {
    public static void main(String[] argv) {
        if (argv.length == 0) {
            System.err.println("java jdbc_connect URL");
            System.exit(1);
        }

        try {
            Class.forName("ORG.as220.tinySQL.textFileDriver");
            // Class.forName("jdbc.odbc.JdbcOdbcDriver");
            // Class.forName("imaginary.sql.iMsqlDriver");

            String url = argv[0];

            String user, pwd;
            user = argv[1];
            pwd = argv[2];

            Connection con = DriverManager.getConnection(url, user, pwd);
            con.close();
            System.out.println("Connection successful.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
