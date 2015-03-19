package myjaas;

import java.security.PrivilegedAction;

/**
 * @author VEXRAROD
 * @version $Id:$
 * @date 13/02/2006 08:53:43
 */
public class ExampleAction implements PrivilegedAction {
   public ExampleAction() {

   }

   public Object run() {
      new CheckJAAS(this);
      System.out.println ("Secret text: "+JAASAuthorizationExample.getSecretText());
      java.io.FilePermission dp = new java.io.FilePermission("a.txt","read");
      SecurityManager s = System.getSecurityManager();
      if ( s != null) s.checkPermission(dp);
      return null;
   }
}
