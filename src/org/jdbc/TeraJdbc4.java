package org.jdbc;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.io.*;

/**
 * Created by IntelliJ IDEA. User: vexrarod Date: 29/10/2003 Time: 08:23:54 To
 * change this template use Options | File Templates.
 */
public class TeraJdbc4 {

    public static void main(String[] args) throws Exception {
        // Class.forName("ar.com.crear.quickdb.MicrosoftDriver").newInstance();
        // Class.forName("com.ncr.teradata.jdbc_4.Driver");
        Class.forName("com.ncr.teradata.TeraDriver");

        // Connection con =
        // DriverManager.getConnection("jdbc:quickdb_microsoft://172.16.0.52:1433/Produccion","autorizaCarga","autoriza");
        Connection con = DriverManager.getConnection(
                "jdbc:teradata://172.16.0.175", "P_AP_VisaHome",
                "P_AP_VisaHome");

        String cPassword = null;

        if (con == null) {
            System.out.println("Fallo en la conexion.");
            System.exit(-1);
        }

        java.sql.Statement s = con.createStatement();
        String query = "EXECUTE P_Ap_Visahome.CC_GetFechaLiquidacionEmpresa ( "
                + "015" + ", '" + "SAPA" + "')";
        ResultSet rs1 = s.executeQuery(query);

        if (rs1.next()) {
            System.out.println(rs1.getDate("Ultima_Fecha_Liquidacion"));
            System.out.println(rs1.getInt("Cartera_Empresa"));
            System.out.println(rs1.getInt("Ultimo_Mes_Liquidado"));
            System.out.println(rs1.getInt("Ultimo_Anio_Liquidado"));
        }
        // s.execute("select numdoc from maetar (nolock) where numtar = "+args[0]);
        // s.execute("select * from P_Views.Cuenta where Nro_Cuenta=82512295");
        s.close();
        s = con.createStatement();

        java.sql.ResultSet rs = s
                .executeQuery("select * from p_views.empresa where Cod_Banco = 15 and Cod_Empresa = 'SAPA'");
        // java.sql.ResultSet rs = s.getResultSet();
        if (rs.next()) {

            System.out.println(rs.getInt("Cod_Banco"));
            System.out.println(rs.getString("Cod_Empresa"));
            System.out.println((rs.getLong("Limite_Compra")));
            System.out.println(rs.getString("Denominacion"));
            System.out.println(rs.getString("Cod_Tipo_Tarjeta_Empresa"));
            System.out.println(rs.getLong("Lim_Compra_Cuotas"));
        }

        rs.close();
        s.close();
        con.close();

    }
}
