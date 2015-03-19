import com.sun.mail.smtp.SMTPSSLTransport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA. User: faturita
 * 
 * mailapi.jar / smtp.jar
 * 
 * Date: Jan 25, 2010 Time: 9:03:15 AM To change this template use File |
 * Settings | File Templates.
 */
public class SecureMailer {

    static class PopupAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("rramele@gmail.com",
                    "17@ImaKenkyuu");
        }
    }

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_PORT      = "465";
    private static final String SSL_FACTORY    = "javax.net.ssl.SSLSocketFactory";

    public static void main(String[] args) throws Exception {
        String mailHost = "smtp.gmail.com";

        // /////// set this variable to be your desired email recipient

        String to = "rramele@gmail.com";

        // these variables come from the mail form

        String from = args[0];
        String subject = args[1];
        String body = args[2];

        if ((from != null) && (to != null) && (subject != null)
                && (body != null)) // we have mail to send
        {

            try {

                // Get system properties
                Properties props = System.getProperties();

                // Specify the desired SMTP server
                // props.put("mail.smtps.host", mailHost);
                // props.put("mail.smtps.auth", "true");

                props.put("mail.smtp.host", SMTP_HOST_NAME);
                props.put("mail.smtp.auth", "true");
                // props.put("mail.debug", "true");
                props.put("mail.smtp.port", SMTP_PORT);
                props.put("mail.smtp.socketFactory.port", SMTP_PORT);
                props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
                props.put("mail.smtp.socketFactory.fallback", "false");

                // Setup mail server
                Authenticator auth = new PopupAuthenticator();

                // create a new Session object
                Session session = Session.getInstance(props, auth);

                // session.setDebug(true);

                // create a new MimeMessage object (using the Session created
                // above)
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO,
                        new InternetAddress[] { new InternetAddress(to) });
                message.setSubject(subject);
                // message.setContent(body, "text/plain");

                // create and fill the first message part
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setText(body);

                // create and fill the second message part
                MimeBodyPart mbp2 = new MimeBodyPart();
                // Use setText(text, charset), to show it off !
                mbp2.attachFile(new File(
                        "SecureMailer.class"));
                // "/home/faturita/Desktop/C.V.-R.Ramele.pdf");

                // create the Multipart and its parts to it
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);

                // SMTPSSLTransport transport = (SMTPSSLTransport)
                // session.getTransport("smtps");

                message.setContent(mp);

                Transport.send(message);

                // it worked!
                System.out.println("<b>Thank you.  Your message to " + to
                        + " was successfully sent.</b>");

            } catch (Throwable t) {

                System.out.println("<b>Unable to send message: <br><pre>");
                t.printStackTrace(System.out);
                System.out.println("</pre></b>");
            }
        }

    }

}
