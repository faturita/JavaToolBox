package myjaas;

import javax.security.auth.Subject;
import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * @author VEXRAROD
 * @version $Id:$
 * @date 14/02/2006 11:56:33
 */
public class CheckJAAS {
   public CheckJAAS(Object o) {
      System.out.println("--------------------------------");
      System.out.println("Checking JAAS Config");
      java.security.ProtectionDomain d = this.getClass().getProtectionDomain();
      System.out.println(d);

      AccessControlContext context = AccessController.getContext();
      Subject subject = Subject.getSubject(context);

      System.out.println ("Subject:"+subject);

      try {
         java.io.FilePermission dp = new java.io.FilePermission("a.txt","read");
         SecurityManager s = System.getSecurityManager();
         if ( s != null) s.checkPermission(dp);
      } catch (Exception e) {
         System.out.println ("No se puede tener acceso a a.txt");
      }
      System.out.println("--------------------------------");
   }
}
