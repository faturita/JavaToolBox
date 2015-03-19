package myjaas;

import javax.security.auth.callback.*;

/**
 * @author VEXRAROD
 * @version $Id:$
 * @date 13/02/2006 08:18:18
 */
public class UsernamePasswordCallbackHandler implements CallbackHandler {
   private String mUsername;
   private char[] mPassword;

   public UsernamePasswordCallbackHandler(String username, char [] password) {
      mUsername = username;
      mPassword = password;
   }

   public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
      for (int i=0;i<callbacks.length;i++) {
         Callback callback = callbacks[i];

         if (callback instanceof NameCallback) {
            NameCallback nameCallback = (NameCallback)callback;
            nameCallback.setName(mUsername);

         } else if (callback instanceof PasswordCallback) {
            PasswordCallback passwordCallback = (PasswordCallback) callback;
            passwordCallback.setPassword(mPassword);
         } else {
            throw new UnsupportedCallbackException(callback, "Unsupported Callback Type");
         }
      }
   }
}
