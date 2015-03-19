package myjaas;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.Subject;
import java.io.FileReader;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * El problema que esta teniendo es que no esta tomando los valores especificos
 * del java.policy para que los aplique directamente y me los tome.
 * 
 * @author VEXRAROD
 * @version $Id:$
 * @date 13/02/2006 08:26:18
 */
public class JAASSampleApp {

   public static void main(String[] args) throws Exception {
      if (args.length != 2) {
         System.err.println ("Usage: java JAASSampleApp username password");
         System.exit(1);
      }

      /**
       * gran Principal PrincipalImpl "testuser" {
       *    permission java.io.FilePermission "c:.....","read,write";
       * };
       */

      //java -Djava.security.auth.login.config==jaas.config JAASSimpleApp testuser sasquatch

      String username = args[0];
      char[] password = args[1].toCharArray();

      LoginContext loginContext = null,loginContext2 = null;

      new CheckJAAS(new Object());

      try {
         loginContext2 = new LoginContext("Sample2",new UsernamePasswordCallbackHandler(username,password));
         loginContext = new LoginContext("Sample", new com.sun.security.auth.callback.TextCallbackHandler());
         loginContext.login();
         loginContext2.login();
         System.out.println("Login succeeded");
      } catch (LoginException e) {
         e.printStackTrace();
         System.out.println("Login failed");
         throw e;
      }

      new CheckJAAS(new Object());

      Subject subject = loginContext.getSubject();

      java.security.Principal p = ((java.security.Principal)subject.getPrincipals().iterator().next());
      System.out.println (p);

      Subject.doAs(subject,new ExampleAction());


      subject = loginContext2.getSubject();

      Subject.doAs(subject,new ExampleAction());

      loginContext.logout();
      loginContext2.logout();
   }
}
