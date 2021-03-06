package org.socketbridge;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyStore;

/**
 * Created by IntelliJ IDEA.
 * User: rramele
 * Date: May 27, 2010
 * Time: 10:51:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class SocketChanceSSLServer extends SocketChance {

    String args[];

    public void connect() throws Exception {

      String host = null;
      int port = -1;
      String path = null;
      for (int i = 0; i < args.length; i++)
         System.out.println(args[i]);

      if (args.length < 3) {
         System.out.println(
               "USAGE: java SSLSocketClientWithClientAuth " +
               "host port requestedfilepath");
         System.exit(-1);
      }

      try {
         host = args[0];
         port = Integer.parseInt(args[1]);
         path = args[2];
      } catch (IllegalArgumentException e) {
         System.out.println("USAGE: java SSLSocketClientWithClientAuth " +
               "host port requestedfilepath");
         System.exit(-1);
      }

      try {

         /*
          * Set up a key manager for client authentication
          * if asked by the server.  Use the implementation's
          * default TrustStore and secureRandom routines.
          */
         SSLSocketFactory factory = null;
         try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "passphrase".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("testkeys"), passphrase);

            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            factory = ctx.getSocketFactory();
         } catch (Exception e) {
            throw new IOException(e.getMessage());
         }

         SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

         /*
          * send http request
          *
          * See SSLSocketClient.java for more information about why
          * there is a forced handshake here when using PrintWriters.
          */
         socket.startHandshake();

         PrintWriter out = new PrintWriter(
               new BufferedWriter(
                     new OutputStreamWriter(
                           socket.getOutputStream())));
         out.println("GET " + path + " HTTP/1.0");
         out.println();
         out.flush();

         /*
          * Make sure there were no surprises
          */
         if (out.checkError())
            System.out.println(
                  "SSLSocketClient: java.io.PrintWriter error");

         /* read response */
         BufferedReader in = new BufferedReader(
               new InputStreamReader(
                     socket.getInputStream()));

         String inputLine;

         while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);

         in.close();
         out.close();
         socket.close();

      } catch (Exception e) {
         e.printStackTrace();
      }
         
    }

}
