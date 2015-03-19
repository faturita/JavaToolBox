
import java.io.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStoreException;
import java.security.Security;
import java.security.cert.*;
import java.util.*;
import javax.net.ssl.*;
import javax.net.ssl.SSLSession;
import javax.security.cert.Certificate;

public class SSLConsumer {
    public static CRL    crl;

    public static String crlFile = "C:\\j2sdk1.4.1_05\\bin\\pca1.1.1.crl";
    public static String host    = "172.16.6.206";
    public static int    port    = 443;
    public static String keyStore;
    public static String keyStorePassword;
    public static String trusStore;
    public static String trusStorePassword;

    // Carga desde un archivo la lista de certificados caducos.
    public static void loadCRL() throws IOException, CertificateException,
            CRLException {
        InputStream inStream = new FileInputStream(crlFile);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        crl = (X509CRL) cf.generateCRL(inStream);
        inStream.close();
    }

    /**
     * Abre un SSLSocket conectandose a el puerto 443 (https) de localhost.
     * 
     * @param args
     */
    public static void main(String[] args) {
        int port = 443;
        String host = "172.16.109.221";

        System.setProperty("javax.net.ssl.keyStore",
                "C:\\j2sdk1.4.1_05\\bin\\websentry.keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "websentrypass");

        // Security.setProperty("https.proxyHost","172.16.0.20");
        // Security.setProperty("https.proxyPort","80");

        // Se setean los parametros para el trustStore por defaul.
        System.setProperty("javax.net.ssl.trustStore",
                "C:\\j2sdk1.4.1_05\\bin\\websentry.keystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "websentrypass");

        try {

            /*
             * Set up a key manager for client authentication if asked by the
             * server. Use the implementation's default TrustStore and
             * secureRandom routines.
             */
            SSLSocketFactory factory = null;
            KeyStore ks = null;
            try {
                SSLContext ctx;
                KeyManagerFactory kmf;
                TrustManagerFactory tmf;

                char[] passphrase = System.getProperty(
                        "javax.net.ssl.keyStorePassword").toCharArray();
                char[] trustphrase = System.getProperty(
                        "javax.net.ssl.trustStorePassword").toCharArray();

                ctx = SSLContext.getInstance("SSL");
                kmf = KeyManagerFactory.getInstance("SunX509");
                ks = KeyStore.getInstance("JKS");
                tmf = TrustManagerFactory.getInstance("SunX509");

                ks.load(new FileInputStream(System
                        .getProperty("javax.net.ssl.keyStore")), passphrase);

                KeyStore ts = KeyStore.getInstance("JKS");
                ts.load(new FileInputStream(System
                        .getProperty("javax.net.ssl.trustStore")), trustphrase);

                kmf.init(ks, passphrase);
                tmf.init(ts);

                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                factory = ctx.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
            // factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

            socket.setUseClientMode(true);
            socket.startHandshake();

            // checkSecurity(socket, ks);

            // for(int i=0;i<x.length;i++)
            // System.out.println(x[i]);

            /*
             * send http request
             * 
             * See SSLSocketClient.java for more information about why there is
             * a forced handshake here when using PrintWriters.
             */
            // socket.startHandshake();
            System.out.println("Using client authentication:"
                    + socket.getNeedClientAuth());

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())));
            out.println("GET " + args[0] + " HTTP/1.0");
            out.println();
            out.flush();

            /*
             * Make sure there were no surprises
             */
            if (out.checkError())
                System.out
                        .println("SSLSocketClient: java.io.PrintWriter error");

            /* read response */
            BufferedReader in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkSecurity(SSLSocket socket, KeyStore ks)
            throws SSLPeerUnverifiedException {
        // Accedemos a la session establecida de ssl y obtenemos la lista de
        // certificados
        // transmitida desde la otra punta y mostramos el contenido de los
        // certificados.
        SSLSession ssl = socket.getSession();

        java.security.cert.Certificate[] xd = ssl.getPeerCertificates();

        // Debug: Mostrar los certificados que envío el peer.
        // for(int i=0;i<xd.length;i++) {
        // System.out.println (xd[i]);
        // }

        // Creamos un trustmanager para validar el certificado del server.
        TrustManager x[] = null;
        try {

            TrustManagerFactory trust = TrustManagerFactory
                    .getInstance("SunX509");
            trust.init(ks);

            x = trust.getTrustManagers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // Casteamos la lista de certificados a certificados del tipo x.509.
        X509Certificate[] x509CertArray = new X509Certificate[xd.length];
        for (int i = 0; i < xd.length; i++) {
            x509CertArray[i] = (X509Certificate) xd[i];
        }

        // Verficacion del peer.
        X509TrustManager xt = (X509TrustManager) x[0];
        try {
            xt.checkServerTrusted(x509CertArray, "RSA");
        } catch (Exception _e) {
            System.out.println("Peer not trusted.");
            _e.printStackTrace();
        }

        // Verificacion del Chain Certifica and CRL.
        try {
            PKIXParameters pk = new PKIXParameters(ks);

            // Aca se debería configurar que el path tenga acceso a un CertStore
            // que apunte a un CRL
            // para que valide los certificados contra el store, o setear el
            // false y hacerlo a mano
            // o usar un CertPathChecker que lo haga también a mano (Recorrer
            // los certificados y ver si el
            // número de serie está en la lista)
            // Carga un CRL de un archivo (Desde Verisign se pueden bajar
            // ejemplos)
            loadCRL();

            // Se crea un parametro de CertStore con el crl que se cargo del
            // archivo.
            CollectionCertStoreParameters colCertStoreParams = new CollectionCertStoreParameters(
                    Collections.singleton(crl));

            // Se crea un CertStore del Tipo Collection (Esta coleccion puede
            // tener certificados o CRL's)
            CertStore certStore = CertStore.getInstance("Collection",
                    colCertStoreParams);

            // Se agrega el CertStore a los Parametros de PKIX
            pk.addCertStore(certStore);

            // Debug System outs
            // System.out.println("CRL:"+crl);
            // System.out.println("Col certstore:"+colCertStoreParams);
            // System.out.println("Pk:"+pk);

            // Seteamos en el PKIX parameters que no implemente el mecanismo por
            // default de
            // buscar en CRL (a través de los CertStores) y lo verificamos
            // manualmente.
            pk.setRevocationEnabled(false);

            for (int i = 0; i < x509CertArray.length; i++) {
                if (crl.isRevoked(x509CertArray[i])) {
                    throw new Exception("Certificado Revocado..."
                            + x509CertArray[i]);
                }
            }

            // Se crea una instancia de un Path Validator para PKIX y se genera
            // el CertPath para
            // certificados X.509
            CertPathValidator cPathValidator = CertPathValidator
                    .getInstance("PKIX");
            CertificateFactory cFactory = CertificateFactory
                    .getInstance("X.509");
            CertPath certPath = cFactory.generateCertPath(Arrays.asList(xd));

            // System.out.println("CerthPath:"+certPath);

            // Construir el CertPath.
            // CertPathBuilder cBuilder = CertPathBuilder.getInstance("PKIX");
            // X509CertSelector cSelector = new X509CertSelector();
            // cSelector.setSubject("C=US");
            // PKIXBuilderParameters pkb = new
            // PKIXBuilderParameters(ks,cSelector);
            // CertPath certPath = cBuilder.build(pkb).getCertPath();

            // Se valida el CertPath.
            PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) cPathValidator
                    .validate(certPath, pk);

        } catch (Exception _e) {
            _e.printStackTrace();
        }
    }

}
