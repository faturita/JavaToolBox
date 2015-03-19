package org.security;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
import sun.security.x509.X500Name;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateAlgorithmId;


/**
 * Created by IntelliJ IDEA.
 * User: vexrarod
 * Date: 14/10/2003
 * Time: 08:53:53
 * To change this template use Options | File Templates.
 */
public class SignCertificate {
   private static String SIG_ALG_NAME ="MD5WithRSA";
   private static int VALIDITY = 365;

   public static void main(String [] args) throws Exception {
      String keystoreFile = args[0];
      String caAlias = args[1];
      String certToSign = args[2];
      String newAlias = args[3];

      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      System.out.print("KeyStore pass:");
      char[] password = in.readLine().toCharArray();


   }
}
