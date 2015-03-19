package org.prode;
/** @description
* Este programa forma parte del de PRODE
* Las clases de este programa funcionan como archivos CGI.
*/

import java.sql.*;

class salida {
    /** Define los caracteres para las salidas. */
    public static final int MAXWINNERS = 10;
    public static final String ENDLINE = "<BR>";
    public static final String START = "<TR><TD>";
    public static final String END = "</TR>";
    public static final String TAB = "<TD>";
    public static final String SECURITYCODE = "997iuop";

    public static void SecurityError() {
        System.out.println ("Security error detected.  You've no rights to modify this.");
        }

    public static void cierre(String msg) {
        System.out.println ("<hr><small><p align='center'>"+msg+"</p></small></body></html>");
        }


    public static void cabecera(String msg) {
        System.out.println ("<HTML><HEAD><TITLE>"+msg+"</TITLE></HEAD><BODY bgcolor='#DDDD00'>");
        }

    public static void cabeceraWinCard () {
        System.out.print ("<TABLE><CAPTIOn>Winning card</caption>");
        }

    public static void cierreWinCard () {
        System.out.print ("</TABLE>");
        }

    public static void cabeceraNewCard() {
        System.out.print ("<SCRIPT language='javascript'>");
        System.out.print ("function cancelEnvio (formu) ");
        System.out.print ("     {");
        System.out.print ("      var incorrect=false;");
        System.out.print ("      // Controlar el rellenado de los campos obligatorios ");
        System.out.print ("      if ((formu.name.value=='') || (formu.surname.value==''))");
        System.out.print ("         {");
        System.out.print ("         incorrect=true;");
        System.out.print ("         alert('Fill name and surname.');");
        System.out.print ("         formu.name.focus();");
        System.out.print ("         }");
        System.out.print ("      if (formu.email.value=='')");
        System.out.print ("         {");
        System.out.print ("         incorrect=true;");
        System.out.print ("         alert('email!')");
        System.out.print ("         formu.email.focus();");
        System.out.print ("         }");
        System.out.print ("      if ((formu.natid.value==''))");
        System.out.print ("         {");
        System.out.print ("         incorrect=true;");
        System.out.print ("         alert('Id Number!');");
        System.out.print ("         formu.natid.focus();");
        System.out.print ("         }");
        System.out.print ("      var cadena2=''");
        System.out.print ("      if (incorrect==false)");
        System.out.print ("      for (i=0;i<10;i++)");
        System.out.print ("         {");
        System.out.print ("         var cadena='formu.'+i+'.value';");
        System.out.print ("         if (eval('cadena')=='local') {");
        System.out.print ("                 cadena2+='A';");
        System.out.print ("                 }");
        System.out.print ("         else  ");
        System.out.print ("         if (eval('cadena')=='empate') {");
        System.out.print ("                 cadena2+='0';");
        System.out.print ("                 }");
        System.out.print ("         else ");
        System.out.print ("         if (eval('cadena')=='visita') {");
        System.out.print ("                 cadena2+='1';");
        System.out.print ("              }");
        System.out.print ("         else ");
        System.out.print ("              {");
        System.out.print ("              incorrect=true;");
        System.out.print ("              }"); 
        System.out.print ("         }");
        System.out.print ("      formu.result.value=cadena2;");
        System.out.print ("      return incorrect;");
        System.out.print ("      }");
        System.out.print ("</SCRIPT>");


        System.out.print ("<FORM name='formula' action='http://baquete.pvirt.com/prode/addcard.pl' method=POST onsubmit='if (cancelEnvio(this)) return false;'>");
        System.out.print ("<LABEL for='a'>Nombre:</label><input type='text' maxlength=20 name='name' id='a'>");
        System.out.print ("<LABEL for='b'>Apellido:</label><input type='text' maxlength=20 name='surname' id='b'>");
        System.out.print ("<label for='c'>Nro. Documento:</label><input type='text' maxlength=20 name='natid' id='c'>");
        System.out.print ("<label for='d'>Ciudad:</label><input type='text' maxlength=20 name='city' id='d'>");
        System.out.print ("<label for='e'>Pais:</label><input type='text' maxlength=20 name='country' id='e'>");
        System.out.print ("<label for='f'>E-Mail:</label><input type='text' maxlength=20 name='email' id='f'>");
        System.out.print ("<input type='hidden' maxlength=20 name='result'>");
        System.out.print ("<hr noshide>");
        System.out.print ("<TABLE><CAPTION>New card for the next season</CAPTION>");
        }

    public static String opcionNewCard(String op,String title) {
        return ("<input type='radio' name='"+op+"' value='"+title+"'>");
        }
        

    public static void cierreNewCard() {
        System.out.println ("</TABLE><p align='center'><input type='submit' ></p></FORM>");
        }


    public static void cabeceraSeeResults () {
        System.out.println ("<TABLE><CAPTION>See results</caption>");
        }

    public static void cierreSeeResults() {
        System.out.println ("</TABLE>");
        }



    public static void gameClosed_() {
        cabecera("The game has been closed.");
        System.out.println ("<h1><font color='blue'>");
        System.out.println ("The game has been closed now, because the current season started.");
        System.out.println ("<br>You must to wait until this season ends.");
        System.out.println ("</font></h1>");
        cierre("Try the next time....");
        }

    public static void gameOver_() {
        cabecera("The game is over.");
        System.out.println ("<h1>");
        System.out.println ("The game has finished.  Wait until the next tournament.");
        System.out.println ("</h1><hr noshide size=4>");
        cierre("Wait a few weeks only.");
        }



    public static void GameSet(Inicializador in,String fecha,String uid) throws SQLException {
        cabecera("Game set before.");
        System.out.println ("<font face='Times New Roman' size='5' color='Red'>");
        System.out.println ("You can't play twice for the same season.<BR>");
        System.out.println ("This is your today card for the next season.<BR>");
        ResultSet rs=in.consulta("SELECT * FROM game_"+fecha+" WHERE uid="+uid);
        System.out.println ("<TABLE border>");
        System.out.println ("<CAPTION>Card of user:"+uid+"</CAPTION>");
        System.out.println ("<TR><TH>Match_number<TH>Local<TH>Visitor<TH>Result</TR>");
        if (rs.next())
            {
            juegoCoding jCo=new juegoCoding(rs.getString("juego"));
            rs=in.consulta("SELECT * FROM partido_"+fecha);
            int i=0;
            while (rs.next())
                {
                System.out.println ("<TR><TD>"+rs.getString("pid")+"<TD>"+rs.getString("local")+"<TD>"+rs.getString("visitor")+"<TD>"+jCo.getCode(i++));
                }
            }
        cierre("Good luck!");
        }

    public static void CantPlay() {
        cabecera("ERROR!");
        System.out.println ("You can't play because the first match of the season are going to start recently or the season has begun.");
        cierre("Try the next season!");
        }

    public static void WelcomeUser(String name) {
        cabecera("Welcome back to our site!!!");
        System.out.println ("<font face='Times New Roman'>");
        System.out.println ("<p align='center'>");
        System.out.println ("Welcome "+name);
        System.out.println ("</p></font>");
        }

    public static void UpdateUser(String nombre,String id) {
        cabecera("Welcome and Thank you to join our site !!!.");
        System.out.println ("<font color='blue'><center>");
        System.out.println ("Welcome to our Site "+nombre+"<BR>");
        System.out.println ("Your id is: "+id+"<BR>");
        System.out.println ("We hope you enjoy it.<BR>");
        System.out.println ("</center></font>");
        }
    }




class salida_anulado {
    /** Define los caracteres para las salidas. */
    public static final int MAXWINNERS = 10;
    public static final String ENDLINE = "\n";
    public static final String TAB = "\t";
    public static final String SECURITYCODE = "997iuop";

    public static void SecurityError() {
        System.out.println ("Security error detected.  You've no rights to modify this.");
        }


    public static void GameSet(Inicializador in,String fecha,String uid) throws SQLException {
        System.out.println ("You can't play twice for the same season.");
        System.out.println ("This is your today card for the next season.");
        ResultSet rs=in.consulta("SELECT * FROM game_"+fecha+" WHERE uid="+uid);
        while (rs.next())
            {
            System.out.println (rs.getString("juego"));
            }
        }

    public static void CantPlay() {
        System.out.println ("You can't play because the first match of the season are going to start recently.");
        }

    public static void WelcomeUser(String name) {
        // Mensajes esporadicos
        System.out.println ("<font face='Times New Roman'>");
        System.out.println ("<p align='center'>");
        System.out.println ("Wellcome back "+name);
        System.out.println ("</p></font>");
        System.out.println ("<hr noshide width=70%>");
        }

    public static void UpdateUser(String nombre,String id) {
        System.out.println ("Welcome to our Site "+nombre);
        System.out.println ("Your id is: "+id);
        System.out.println ("We hope you enjoy it.");
        }
    }




class Inicializador {
    private Connection con;
    private Statement stmt;

    public Inicializador() {
        try {
            Class.forName("ORG.as220.tinySQL.textFileDriver");
            con=DriverManager.getConnection("jdbc:tinySQL","","");
            stmt=con.createStatement();
            }
        catch (Exception e) {
            System.out.println (e);
            }
        }

    public Inicializador(int CANTPARTIDOS)
        {
        try {
            // Creacion del objeto class para usar de driver.
            Class.forName("ORG.as220.tinySQL.textFileDriver");

            // Conectando con la base de datos.
            con=DriverManager.getConnection("jdbc:tinySQL","","");

            stmt = con.createStatement();

                try {
                    stmt.executeUpdate("DROP TABLE users");
                    stmt.executeUpdate("DROP TABLE fechas");
                    for (int i=1;i<CANTPARTIDOS;i++)
                        {
                        stmt.executeUpdate("DROP TABLE partido_"+String.valueOf(i));
                        stmt.executeUpdate("DROP TABLE game_"+String.valueOf(i));
                        }
                    }
                catch (Exception e) {
                    }

            // Tabla estado
            stmt.executeUpdate("CREATE TABLE fechas (yeari INT, monthi INT, dayi INT, yearf INT, monthf INT, dayf INT, fid INT)");

            // Tabla users
            stmt.executeUpdate("CREATE TABLE users (nombre CHAR(20), apellido CHAR(20), dni CHAR(15), cdad CHAR(15), pais CHAR(15), email CHAR(15),id INT)");

            // Tablas para los partidos de las CANTPARTIDOS fechas
            for (int i=1;i<CANTPARTIDOS;i++)
                stmt.executeUpdate("CREATE TABLE partido_"+String.valueOf(i)+" (local CHAR(20), visita CHAR(20), result CHAR(3),pid INT)");

            // Tablas para las apuetas que realicen los participantes para las CANTPARTIDOS fechas
            for (int i=1;i<CANTPARTIDOS;i++)
                stmt.executeUpdate("CREATE TABLE game_"+String.valueOf(i)+" (uid INT , juego CHAR(10), puntos INT)");

            // Tablas creadas...

            stmt.close();
            con.close();
            
            System.out.println ("All tables were maded successfully.\n");
            }
        catch (Exception e) {
            System.out.println (e);
            }
        }

    public int contar() throws SQLException {
        return (stmt.getUpdateCount());
        }

    public ResultSet consulta (String consultS) throws SQLException {
        /** @description
            Permite realizar una consulta sql devolviendo el objeto
            ResultSet de la consulta.
            Lanza SQLException
            */
        ResultSet rs=stmt.executeQuery(consultS);
        return (rs);
        }

    public void actualiza(String updateS) throws SQLException {
        /** @description
            Permite realizar un update SQL.
            Lanza SQLException.
            */
        stmt.executeUpdate(updateS);
        }

    public void fillData(String fecha,String locales,String visitantes)
        {
        /** @description
            Fill the data about the matches.
        */

        java.util.StringTokenizer localTok = new java.util.StringTokenizer(locales,",");
        java.util.StringTokenizer visitTok = new java.util.StringTokenizer(visitantes,",");
        if ( (localTok.countTokens()!=10) || (visitTok.countTokens()!=10) )
            {
            System.out.println("Error in data.  You might type 10 elements from any list.");
            return;
            }
        try {
            // Elimina los datos de la tabla, si estos ya existen.
            try {
                stmt.executeUpdate("DELETE FROM partido_"+fecha+" WHERE pid>=0");
                }
            catch (Exception e) {
                }

            for (int i=0;i<10;i++)
                {
                stmt.executeUpdate("INSERT INTO partido_"+fecha+" (local,visita,result,pid) VALUES ('"+localTok.nextToken()+"','"+visitTok.nextToken()+"','101',"+String.valueOf(i)+")");
                }

            /** Devuelve el estado actual de la tabla. */
            ResultSet rs=stmt.executeQuery("SELECT * FROM partido_"+fecha);

            while (rs.next()) {
                System.out.println(rs.getString("local")+salida.TAB+rs.getString("visita")+salida.TAB+rs.getString("pid"));
                }

            stmt.close();
            con.close();
            }
        catch (Exception e) {
            System.out.println (e);
            e.printStackTrace();
            System.exit(0);
            }
        }
        

    public void fillDates (String fecha, int anoi, int mesi, int diai, int anof, int mesf, int diaf)
        {
        /** @description
            Completa los datos ingresados de las fechas de inicio y de fin
            de cada serie de partidos de la liga.
            Ademas devuelve como resultado toda la tabla completa que se ha
            modificado.
            */

        try {

            stmt.executeUpdate("DELETE FROM fechas WHERE fid="+fecha);

            stmt.executeUpdate("INSERT INTO fechas (yeari,monthi,dayi,yearf,monthf,dayf,fid) VALUES ("+String.valueOf(anoi)+","+String.valueOf(mesi)+","+String.valueOf(diai)+","+String.valueOf(anof)+","+String.valueOf(mesf)+","+String.valueOf(diaf)+","+fecha+")");
            
            ResultSet rs=stmt.executeQuery("SELECT * FROM fechas");

            System.out.println ("YEAR"+salida.TAB+"MONTH"+salida.TAB+"DAY"+salida.TAB+"YEAR"+salida.TAB+"MONTH"+salida.TAB+"DATE"+salida.TAB+"FiD");
            
            while (rs.next()) {
                System.out.println (rs.getInt("yeari")+salida.TAB+rs.getInt("monthi")+salida.TAB+rs.getInt("dayi")+salida.TAB+rs.getInt("yearf")+salida.TAB+rs.getInt("monthf")+salida.TAB+rs.getInt("dayf")+salida.TAB+rs.getString("fid"));
                }
            
            stmt.close();
            con.close();
            }
        catch (Exception e) {
            System.out.println ("ERROR:"+e);
            }
        }

    public void finalize () throws SQLException {
        con.close();
        stmt.close();
        }

    public static void main(String [] args) {
        if (args.length==0)
            {
            System.out.println ("Usage:  java Inicializador -c  to create data structures.");
            System.out.println ("        java Inicializador -m match_number list_of_locals list_of_visitors ");
            System.out.println ("        java Inicializador -d match_number first_date(yy mm dd) final_date(yy mm dd)");
            System.exit(1);
            }
        Inicializador in;
        if (args[0].equals("-c"))
            in=new Inicializador(19);
        else
            if (args[0].equals("-d"))
                {
                in=new Inicializador();
                in.fillDates(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),Integer.parseInt(args[7]));
                }
            else if (args[0].equals("-m"))
                {
                in=new Inicializador();
                in.fillData(args[1],args[2],args[3]);
                }
        }

    }

class newcard {
    /**********************************************************************
        @description
        El metodo principal de esta clase, analiza la fecha actual, calcula
        cual es la proxima fecha a disputarse en el torneo, obtiene la tabla
        con los valores de los equipos y devuelve la tabla como si fuera una
        tarjeta vacia incluyendo un boton de enviar a addcard.pl
        Ademas controla que la fecha actual no se encuentre entre la fecha
        de inicio y la fecha final de la fecha que se calculo.
        */

    public void setNewCard() {
        java.util.Date fechaactual = new java.util.Date();
        fechaManager fMan = new fechaManager();
        String fecha = fMan.match_Number_ck(fechaactual.getDate(),fechaactual.getMonth()+1,fechaactual.getYear()+1900);
        // Este metodo devuelve la proxima fecha que se va a jugar.
        if (fecha.equals("-1"))
            {
            salida.gameClosed_();
            System.exit(0);
            }
        if (fecha.equals("19"))
            {
            salida.gameOver_();
            System.exit(0);
            }
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM partido_"+fecha);
            salida.cabecera("Making the card.....");
            salida.cabeceraNewCard();
            while (rs.next()) {
                String pidd=rs.getString("pid");
                System.out.print(salida.START+rs.getString("local")+salida.opcionNewCard(pidd,"local")+salida.TAB+salida.opcionNewCard(pidd,"empate")+salida.TAB+rs.getString("visita")+salida.opcionNewCard(pidd,"visita"));
                System.out.print(salida.TAB+pidd+salida.END);
                }
            salida.cierreNewCard();
            salida.cierre("");
            }
        catch (Exception e) {
            System.out.println ("ERROR:"+e);
            }
        }

    public static void main(String [] args) {
        newcard neC = new newcard();
        neC.setNewCard();
        }

    }

class addcard {
    /** *******************************************************************
        @description
        Esta clase es la encargada de procesar e incluir los juegos de los
        usuarios.
        Captura la entrada de datos del usuario con respecto a los resultados
        Estos estan codificados en una secuencia de caracteres, y realizan
        la correspondencia con el numero del pid.
        -Luego agrega el usuario a la base de datos del usuario si este no
        se encuntra.  El usuario registrado, solo debera ingresar su numero
        de dni para entrar a la base.
        -Analiza la fecha obteniendo la fecha a disputar.
        -Agrega una entrada a la tabla de game_fecha.
        -Devuelve aclarando que esta todo ok.
        -Se requiere de preprocesamiento del lado de cliente para verificar
        que los datos hallan sido colocados correctamente.
        */

    public void addCard(String results,String id)
        {
        // Para llegar a este punto el dni del usuario ya tuvo que haberse
        // registrado en la tabla users.
        java.util.Date fechaactual = new java.util.Date();
        fechaManager fMan = new fechaManager();
        String fecha = fMan.match_Number_ck(fechaactual.getDate(),fechaactual.getMonth()+1,fechaactual.getYear()+1900);
        if (fecha.equals("-1"))
            {
            salida.CantPlay();
            System.exit(0);
            }
        // Este metodo devuelve la proxima fecha que se va a jugar.
        // Para aumentar la velocidad no se comprueban errores.
        // Se considera que llegar a este punto es libre de errores.
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM game_"+fecha+" WHERE uid="+id);
            if (rs.next())
                {
                // En caso de que halla devuelto algun dato.....
                // salida se encarga de emitir la boleta actual de este usuario
                salida.GameSet(in,fecha,id);
                System.exit(1);
                }
            // El usuario no ha hecho ninguna jugada.
            // Agregar la jugada a la tabla correspondiente a esta fecha
            in.actualiza("INSERT INTO game_"+fecha+" (uid,juego,puntos) VALUES ("+id+",'"+results+"',0)");
            System.out.println ("Your game has been setting up successfully.  Good luck!");
            }
        catch (Exception e) {
            System.out.println ("ERROR:"+e);
            System.exit(0);
            }
        }

    public int ckadduser (String nombre, String apellido, String dni,String cdad, String pais,String email)
        {
        /** Chequea si el usuario esta en la base de datos.  En caso afirmativo sale,
        sino lo agrega. */
        /** Si el dni coincide significa que un usuario. */
        // Se supone que los datos estan ya controlados.
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM users WHERE dni='"+dni+"'");
            if (rs.next()) {
                // Existen los datos de ese usuario.
                int id=rs.getInt("id");
                salida.WelcomeUser(rs.getString("nombre"));
                return id;
                }
            // Averiguar posible id
            int id=0;
            rs=in.consulta("SELECT * FROM users ");
            while (rs.next()) {
                id++;
                }
            if (id>10000) System.exit(0);

            in.actualiza("INSERT INTO users (nombre,apellido,dni,cdad,pais,email,id) VALUES ('"+nombre+"','"+apellido+"','"+dni+"','"+cdad+"','"+pais+"','"+email+"',"+String.valueOf(id)+")");
            salida.UpdateUser(nombre,String.valueOf(id));
            // Datos agregados.
            return id;
            }
        catch (Exception e) {
            System.out.println ("ERROR:"+e);
            System.exit(0);
            return 0;
            }
        }

    public static void main(String [] args) {
        if (args.length<7)
            {
            System.out.println ("Usage: addcard name surname nat_id_number city country email result_string.");
            System.out.println ("\n     The result_string must be: A for local, 1 for visitor and 0 for deuce.");
            System.exit(0);
            }
        addcard adU = new addcard();
        int id=adU.ckadduser(args[0],args[1],args[2],args[3],args[4],args[5]);
        adU.addCard(args[6],String.valueOf(id));
        }
    }


class putresult {
    /** *********************************************************************
    @description
        Esta clase es la encargada de actualizar los resultados de los
        partidos una vez que paso una fecha completa.
    @see #juegoCoding
        Codificacion de los resultados:


            A   Gana el local
            0   Empate
            1   Gana el visitante
            100 Partido no convalidado para el juego por suspension.
            101 Partido aun no jugado.

    @requirements
        The requirements needed for running this class are:
            All tables maded.
            All tables filled with the properties sets of data.
            All data of current players filled.

    */

    private void update_partido(String fecha, String results) throws SQLException {
        /** Actualiza la tabla correspondiente a esta fecha jugada. */
        Inicializador in=new Inicializador();
        juegoCoding jCo = new juegoCoding(results);
        for (int i=0;i<10;i++) {
            in.actualiza("UPDATE partido_"+fecha+" SET result='"+jCo.get(i)+"' WHERE pid="+String.valueOf(i));
            }
        System.out.println ("Update results in games tables...");
        }

    private java.util.Vector update_game(String fecha,String results) throws SQLException {
        // Actualiza los puntajes de los usuarios.....
        Inicializador in=new Inicializador();
        java.util.Vector ids=new java.util.Vector(10);
        int puntMax=0;
        juegoCoding jCo = new juegoCoding(results);
        // Obtengo toda los datos de los que jugaron
        ResultSet rs=in.consulta("SELECT * FROM game_"+fecha);
        while (rs.next()) {
            int puntos=jCo.getPuntos(rs.getString("juego"));
            int uid=rs.getInt("uid");
            if ( (puntos>=puntMax) && (puntos!=0) )
                {
                puntMax=puntos;
                ids.addElement(new Integer(uid));
                }
            in.actualiza("UPDATE game_"+fecha+" SET puntos="+String.valueOf(puntos)+" WHERE uid="+String.valueOf(uid));
            }
        System.out.println ("Update Successfully !!!!!!");
        return ids;
        }

    private void notify_winners(java.util.Vector ids) {
        // Este es experimental y lo que debe hacer es mandar un email.
        }


    public static void main(String [] args) {
        if (args.length!=3) {
            System.out.println ("Usage: putresults code season_number results_string");
            System.out.println ("\n\n\t results_string must follow the next pattern.");
            System.out.println ("\t A - Local wins.");
            System.out.println ("\t 0 - Deuce.");
            System.out.println ("\t 1 - Visitor wins.");
            System.exit(0);
            }

        if (!(args[0].equals(salida.SECURITYCODE))) {
            salida.SecurityError();
            System.exit(0);
            }

        if (args[2].length()!=10) {
            System.out.println ("The results_String field must be 10 char sized.");
            System.exit(0);
            }
        try {
            putresult pRe = new putresult();
            pRe.update_partido(args[1],args[2]);
            java.util.Vector ids=pRe.update_game(args[1],args[2]);
            pRe.notify_winners(ids);
            }
        catch (SQLException e) {
            System.out.println ("Error:"+e);
            System.exit(0);
            }
        }
    }



class seeresults {
    /** ********************************************************************
    @description:
        Esta clase se utiliza principalmente para devolver datos relacionados
        con los resultados de la ultima fecha (wincard.pl), los resultados de
        alguna fecha concreta (seeresults.pl) o ver el hall of fame.
        */
    public void seeMatch (String fecha) {
        /** @see:
            Se considera que la fecha ya es una fecha valida 1-19
            */
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM partido_"+fecha);
            salida.cabecera("Season number:"+fecha);
            salida.cabeceraSeeResults();
            System.out.println ("<TR><TH>HOME<TH>VISITOR<TH>RESULT<TH>Game number"+salida.END);
            while (rs.next()) {
                System.out.print(rs.getString(salida.START+"local")+salida.TAB+rs.getString("visita")+salida.TAB);
                String rresult = rs.getString("result");
                if (rresult.equals("100"))
                    System.out.print ("NULL");
                else
                if (rresult.equals("101"))
                    System.out.print ("NO RESULTS");
                else
                    if (rresult.equals("A"))
                        System.out.print ("HOME WINS");
                    else
                        if (rresult.equals("0"))
                            System.out.print ("DEUCE");
                        else
                            System.out.print ("VISITOR WINS");
                System.out.println (salida.TAB+rs.getString("pid")+salida.END);
                }
            salida.cierreSeeResults();
            salida.cierre("Play !");
            }
        catch (Exception e) {
            System.out.println ("ERROR:"+e);
            }
        }
    public static void main (String [] args) {
        seeresults ser = new seeresults();
        ser.seeMatch(args[0]);
        }

    }


class wincard {
    /** ********************************************************************
    @description:
        Analiza la fecha actual y devuelve la ultima tarjeta ganadora.
        */

    public void seewincard () {
        java.util.Date fechaactual = new java.util.Date();
        fechaManager fMan = new fechaManager();
        String fecha = fMan.match_Number(fechaactual.getDate(),fechaactual.getMonth()+1,fechaactual.getYear()+1900);
        if (fecha.equals("-1"))
            {
            salida.gameClosed_();
            System.exit(0);
            }
        if (fecha.equals("0"))
            {
            salida.gameClosed_();
            System.exit(0);
            }
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM partido_"+fecha);
            salida.cabecera("Last winning card.");
            salida.cabeceraWinCard();
            System.out.println (salida.START+"HOME"+salida.TAB+"VISITOR"+salida.TAB+"RESULT"+salida.TAB+"Game number"+salida.END);
            while (rs.next()) {
                System.out.print(salida.START+rs.getString("local")+salida.TAB+rs.getString("visita")+salida.TAB);
                String rresult = rs.getString("result");
                if (rresult.equals("100"))
                    System.out.print ("NULL");
                else
                if (rresult.equals("101"))
                    System.out.print ("NO RESULTS");
                else
                    if (rresult.equals("A"))
                        System.out.print ("HOME WINS");
                    else
                        if (rresult.equals("0"))
                            System.out.print ("DEUCE");
                        else
                            System.out.print ("VISITOR WINS");
                System.out.println (salida.TAB+rs.getString("pid")+salida.END);
                }
            salida.cierreWinCard();
            salida.cierre("");
            }
        catch (Exception e) {
            System.out.println ("ERROR:"+e);
            }
        }

    public static void main (String [] args) {
        wincard wnCard = new wincard();
        wnCard.seewincard();
        }

    }


class fechaManager {
    /** ********************************************************************
    @description
            Esta clase es de uso interno.

    */
    public boolean controlFecha(int dd, int mm, int yy) {
        java.util.Date fech = new java.util.Date(yy, mm, dd);
        if (fech == null)
            return false;
        else
            return true;
        }

    public String match_Number_ck (int dd, int mm, int yy) 
        /** @description
            Este metodo es igual al match_Number lo unico que devuelve la
            fecha que continua, en vez de devolver la que sigue, comprobando
            si la fecha dada se encuntra entre el inicio y el fin.
            */
        {

        java.util.Date fech=new java.util.Date(yy,mm,dd);
        String fechaMap = "0";
        /** Realiza consulta SQL a la base de datos. */
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM fechas");
            while (rs.next()) {
                java.util.Date fechai = new java.util.Date(rs.getInt("yeari"),rs.getInt("monthi"),rs.getInt("dayi"));
                java.util.Date fechaf = new java.util.Date(rs.getInt("yearf"),rs.getInt("monthf"),rs.getInt("dayf"));
                String fecha = rs.getString("fid");
                if (fech.after(fechaf))
                    fechaMap=new String(fecha);
                else
                    {
                    fechaMap=new String(fecha);
                    // En este punto la fecha fech es menor o igual a fechaf
                    // Esta fech debe ser a la vez menor que fecha inicial para
                    //   permitir jugar.
                    if (fech.before(fechai))
                        return fechaMap;
                    else
                        return "-1";
                    }
                }
            fechaMap=String.valueOf((Integer.parseInt(fechaMap))+1);
            return fechaMap;
            }
        catch (Exception e) {
            System.out.println("error:"+e);
            System.exit(1);
            }
        finally {
            return fechaMap;
            }
        }


    public String match_Number (int dd, int mm, int yy) 
        /** @description
            Este metodo retorna en un STRING el valor entero que le corresponde
            a la fecha que esta en vigencia.
            Busca en la tabla correspondiente a fechas, si este dia es posterior
            a la fecha final de cada ronda de la liga, pero es menor que la fecha final
            de la ronda de la que sigue sigue.  Si no hay mas devuelve la ultima fecha.
            En el caso de que la fecha corresponda a una fecha no valida, o sea la misma
            fecha en que se juega los ultimos partidos de la serie devuelve al anterior.
            */
        {

        java.util.Date fech=new java.util.Date(yy,mm,dd);
        String fechaMap = "0";
        /** Realiza consulta SQL a la base de datos. */
        try {
            Inicializador in=new Inicializador();
            ResultSet rs=in.consulta("SELECT * FROM fechas");
            while (rs.next()) {
                java.util.Date fechai = new java.util.Date(rs.getInt("yeari"),rs.getInt("monthi"),rs.getInt("dayi"));
                java.util.Date fechaf = new java.util.Date(rs.getInt("yearf"),rs.getInt("monthf"),rs.getInt("dayf"));
                String fecha = rs.getString("fid");
                if (fech.after(fechaf))
                    fechaMap=new String(fecha);
                else
                    return fechaMap;
                }
            return fechaMap;
            }
        catch (Exception e) {
            System.out.println("error:"+e);
            System.exit(1);
            }
        finally {
            return fechaMap;
            }
        }

    public static void main(String [] args) {
        fechaManager man=new fechaManager();
        String fech=man.match_Number(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        System.out.println ("El match devuelto es:"+fech);
        fech=man.match_Number(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        System.out.println ("El match devuelto por el second metodo es:"+fech);

        }
    }

class juegoCoding {
    /** *********************************************************************
    @description
        esta clase tiene metodos que son los utilizados principalmente por
        putresult para modificar y controlar la equivalencia entre la forma
        de almacenar los datos de los resultados y los resultados en si.
        */
    private char[] ids;
    private String resultsS;

    public juegoCoding(String results) {
        ids=new char[10];
        resultsS=results;
        for (int i=0;i<10;i++) {
            ids[i]=results.charAt(i);
            }
        }

    public String getCode(int i) {
        if (ids[i]=='A')
            return "LOCAL WINS";
        else if (ids[i]=='0')
            return "DEUCE";
        else if (ids[i]=='1')
            return "VISITOR WINS";
        return "NO RESULTS";
        }


    public int getPuntos (String results) {
        int puntos=0;
        for (int i=0;i<10;i++) {
            if (results.charAt(i)==resultsS.charAt(i)) {
                // Esto representa un punto....
                puntos++;
                }
            }
        return puntos;
        }

    public String get(int i) {
        return (""+ids[i]);
        }
    }


