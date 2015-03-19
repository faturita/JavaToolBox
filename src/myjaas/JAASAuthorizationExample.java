package myjaas;

import javax.security.auth.Subject;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.util.Set;
import java.util.Iterator;

/**
 * @author VEXRAROD
 * @version $Id:$
 * @date 13/02/2006 08:47:21
 */
public class JAASAuthorizationExample {
   private static final String text = "this is the text for the EVERYBODY";
   private static final String supertext = "this is plain text for TESTUSER";

   public static String getSecretText() {
      AccessControlContext context = AccessController.getContext();
      Subject subject = Subject.getSubject(context);

      if (subject == null) {
         System.out.println ("Subject is null...!!!");
         return text;
      }

      Set principals = subject.getPrincipals();
      Iterator iterator = principals.iterator();
      while (iterator.hasNext()) {
         Principal principal = (Principal)iterator.next();
         System.out.println ("Principal:"+principal.getClass().getName());
         System.out.println ("Principal name:"+principal.getName());
         if (principal.getName().equals("testuser")) {
            return supertext;
         }
         if (principal.getName().equals("VEXRAROD")) {
            return "VEXRAROD NT LOGGED SUCCESSFULY USER.";
         }
      }
      return text;
   }
}
