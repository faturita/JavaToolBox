import java.io.*;
import java.security.MessageDigest;
import java.security.cert.*;
import java.util.Collection;
import javax.net.ssl.*;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


class Lock{
    
    private boolean isLocked = true;
    
    private String version = "";
    
    public synchronized void lock()
    throws InterruptedException{
      while(isLocked){
        wait();
      }
      isLocked = true;
    }
    
    public synchronized void unlock(){
      isLocked = false;
      notify();
    }

    public void setVersion(String substring) {
        this.version = substring;
        
    }
    
    public String getVersion()
    {
        return this.version;
    }
  }


class SSLSocketHandler implements Runnable {
    SSLSocket s;
    Lock lock;

    public SSLSocketHandler(SSLSocket s, Lock lock) {
        this.s = s;
        
        this.lock = lock;
        
        setup();
    }

    String xml = "<mobile><header><servicio><nombre>LOGIN</nombre><operacion/></servicio></header><body><login><idSesion>4QUQU7W84</idSesion></login><res>0</res></body></mobile>";
    
    public String soapXml()
    {
        String soapxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>    <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">       <soapenv:Body>          <executeResponse xmlns=\"http://webservice.mi.rio.santander.com.ar\">             <executeReturn><![CDATA["+xml+"]]></executeReturn>          </executeResponse>       </soapenv:Body>    </soapenv:Envelope>";

        return soapxml;
        
    }

    Hashtable maps = new Hashtable();
    
    public void setup()
    {
        
        maps.put("4.1.1", "<mobile>    <header>        <servicio>            <nombre>LOGIN</nombre>                </servicio>    </header>    <body>        <res>0</res>                    <idSession>asdfasdf</idSession>              <telUtil>0</telUtil>    </body></mobile>");
        
    }




    OutputStream out = null;
    InputStream in = null;
    
    public void close() throws IOException {
        System.out.println ("Connection closed by server.");
        //out.close();
        //in.close();
        //s.close();
        
    }
    public void write() throws IOException {
        out.write(soapXml().getBytes());
        
        System.out.println (">>>>>>>>>>Outgoing>>>>>>>>>>>>>>");
        System.out.println ( httpHeader() );
        System.out.println (soapXml());
    }
    
    public void addHeaders(StringBuffer str) throws IOException 
    {
        out.write( str.toString().getBytes());
        System.out.println ( str.toString() );
    }
    public String httpHeader()
    {
        StringBuffer str = new StringBuffer();
        
        
        str.append( "HTTP/1.1 200 HTTP OK").append("\r\n");
        str.append( "Server: Apache-Coyote/1.1 ").append("\r\n");
        str.append( "Date: Thu, 06 Sep 2012 18:36:23 GMT ").append("\r\n");

        
        return str.toString();
        
    }
    
    public void run() {

        try {
            out = s.getOutputStream();

            out.write(httpHeader().getBytes());
            
            lock.lock();
            
            
            if (    maps.get(lock.getVersion()) != null)
            {
                xml = (String) maps.get(lock.getVersion());
            }
            
            StringBuffer str = new StringBuffer();
            str.append( "Content-Length: " + soapXml().getBytes().length).append("\n");
            str.append( "Connection: close ").append("\r\n\r\n");
            
            addHeaders( str );
                   
            write();
            
            close();
            

        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            //System.out.println("Done processing request....");
            if (s != null)
                try {
                    s.close();
                } catch (Exception e) {
                }
        }

    }

}


class SSLSocketHandlerReader extends SSLSocketHandler
{
    public SSLSocketHandlerReader(SSLSocket s, Lock lock) {
        super(s, lock);
        

    }
    
    public void close() throws IOException {
        System.out.println ("Connection closed by client.");
        in.close();
    }
    
    public void run() {
        StringBuffer inputData = new StringBuffer();
        
        try {

            //BufferedReader d = new BufferedReader(new InputStreamReader(in));
            in = s.getInputStream();
            
            String line;
            
            byte buffer[] = new byte[1024];
            
            int len = 0;
            
            int count = 0;
            
            int iContentLength = 0;
                
            // Read until something useful is found...
            while (true) {
                
                {
                    len = in.read(buffer, 0, 1024);
                }
                if (len > 0)
                {
                    System.out.println ("Adding data...");
                    //line = new String(cbuf,0,len);
                    line = new String(buffer,0,len);
                    inputData.append(line);
                    System.out.println( inputData.toString() );
                    
                    String contentLength = "Content-Length: ";
                    String str = inputData.toString();
                    
                    if ( str.indexOf(contentLength) > 0)
                    {
            
                        int startIdx = str.indexOf(contentLength) + contentLength.length();
                        int endIdx = str.substring(startIdx).indexOf("\n");
                        
                        System.out.println ("Content Length:" + str.substring(startIdx,startIdx + endIdx));
                        
                        iContentLength = new Integer( str.substring(startIdx,startIdx + endIdx-1) ).intValue();
                    }
                    
                    
                }
                
                //System.out.println( count );
                if (count++>999999 || inputData.indexOf("\r\n") > 0)
                {
                    break;
                }
            }

            
            if (iContentLength != 0)
            {
                len = in.read(buffer, 0, iContentLength);
    
                if (len > 0)
                {
                    System.out.println ("Payload");
                    line = new String(buffer,0,len);
                    inputData.append(line);
                    System.out.println( inputData.toString() );
                    
                    String contentLength = "<idVersion>";
                    String str = line.toString();
                    
                    if ( str.indexOf(contentLength) > 0)
                    {
            
                        int startIdx = str.indexOf(contentLength) + contentLength.length();
                        int endIdx = str.substring(startIdx).indexOf("</");
   
                        System.out.println ("Version:" + str.substring(startIdx,startIdx + endIdx));
                        
                        lock.setVersion(str.substring(startIdx,startIdx + endIdx).trim());
                        
                   }
                        
    
                }
            }
            
            
            if (len == -1) 
            {
                close();
                return;
            }

            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println ("<<<<<<<Incoming<<<<<<<<<<<<<<<<<");
            System.out.println( inputData.toString() );
            //System.out.println("Done processing request....");
            
            lock.unlock();
        }
    }    
}

public class SSLProducer {
    X509CRL crl;

    public void loadCRL() throws IOException, CertificateException,
            CRLException {
        InputStream inStream = new FileInputStream(
                "C:\\j2sdk1.4.1_05\\bin\\crl.pem");
        Collection col = crl.getRevokedCertificates();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        crl = (X509CRL) cf.generateCRL(inStream);
        inStream.close();
    }

    public static void getEnCipherSuites(SSLServerSocket ss) {
        String[] cs = null;
        /*
         * cs = new String[1]; cs[0]= "SSL_RSA_WITH_RC4_128_MD5";
         * ss.setEnabledCipherSuites(cs);
         */

        cs = ss.getEnabledCipherSuites();
        for (int i = 0; i < cs.length; i++)
            System.out.println(cs[i]);

    }

    public void digest() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        String d = new String("La vida loca...");

        md.update(d.getBytes());

        byte[] by = md.digest();

        System.out.println("Message" + d + "-Digest SHA-1:");
        for (int i = 0; i < by.length; i++)
            System.out.print("(" + by[i] + ")");
        System.out.println("");

    }

    public static void main(String[] args) throws Exception {

        int port = 18100;
        
        try {
            
            if (args.length == 0)
            {
                port = 18100;
            }
            else
            {
                port = new Integer(args[0]).intValue();
            }

            // System.setProperty("javax.net.ssl.trustStore","D:\\backup\\C\\j2sdk1.4.1_05\\bin\\websentry.keystore");
            // System.setProperty("javax.net.ssl.trustStorePassword","websentrypass");

            System.setProperty("javax.net.ssl.keyStore",
                    "D:\\workspace\\JavaToolbox\\tester\\ssl.keystore");
            System.setProperty("javax.net.ssl.keyStorePassword", "gilbarco");

            SSLServerSocketFactory sslFact = (SSLServerSocketFactory) SSLServerSocketFactory
                    .getDefault();

            SSLServerSocket ss = (SSLServerSocket) sslFact
                    .createServerSocket(port);

            /**
             * new SSLProducer().getEnCipherSuites(ss); String cs[] = new
             * String[1]; cs[0]= "SSL_NULL_WITH_NULL";
             * ss.setEnabledCipherSuites(cs);
             */

            getEnCipherSuites(ss);

            // ss.setNeedClientAuth(true);
            while (true) {
                // if (1 == 3) break; // stupid compiler (you reader should know
                // why)
                SSLSocket s = null;
                try {

                    System.out.println("Waiting for new incoming connections....");
                    s = (SSLSocket) ss.accept();
                    
                    System.out.println("Connected !");
                    s.startHandshake();
                    
                    Lock lock = new Lock();
                    
                    Thread ts = new Thread(new SSLSocketHandlerReader(s,lock));
                    ts.start();
                    
                    Thread.sleep(0);

                    SSLSocketHandler sslhandler = null;
                    Thread tt = new Thread(sslhandler = new SSLSocketHandler(s,lock));
                    tt.start();
                    

                 
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //if (s!=null) try { s.close(); } catch (Exception e) {}
                }
            }

            // ss.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
