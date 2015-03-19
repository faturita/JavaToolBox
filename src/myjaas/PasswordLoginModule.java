package myjaas;

import javax.security.auth.spi.LoginModule;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.callback.*;
import java.security.Principal;
import java.util.Map;
import java.io.IOException;

/**
 * @author VEXRAROD
 * @version $Id:$
 * @date 13/02/2006 08:29:39
 */
public class PasswordLoginModule implements LoginModule {
   private Subject mSubject;
   private CallbackHandler mCallbackHandler;

   private boolean mLoginSucceeded = false;
   private boolean mCommitSucceeded = false;

   private String mUsername;
   private char[] mPassword;

   private Principal mPrincipal;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
      mSubject = subject;
      mCallbackHandler = callbackHandler;
      mLoginSucceeded = false;
      mCommitSucceeded = false;
      mUsername = null;
      clearPassword();
   }

   private void clearPassword() {
      if (mPassword == null) return ;
      for (int i=0;i<mPassword.length;i++) {
         mPassword[i] = ' ';
      }
      mPassword = null;
   }

   public boolean login() throws LoginException {
      if (mCallbackHandler == null) {
         throw new LoginException();
      }

      Callback[] callbacks = new Callback[2];
      callbacks[0] = new NameCallback("Username");
      callbacks[1] = new PasswordCallback("Password", false);

      try {
         mCallbackHandler.handle(callbacks);
         mUsername = ((NameCallback)callbacks[0]).getName();
         char[] tempPassword = ((PasswordCallback)callbacks[1]).getPassword();
         mPassword = new char[tempPassword.length];
         System.arraycopy(tempPassword,0,mPassword,0,tempPassword.length);

         ((PasswordCallback)callbacks[1]).clearPassword();

      } catch (IOException e) {
         e.printStackTrace();
      } catch (UnsupportedCallbackException uce) {
         uce.printStackTrace();
      }

      System.out.println (mUsername);
      
      if ("testuser".equals(mUsername) &&
            (new String(mPassword)).equals("sasquatch") ) {
         mLoginSucceeded = true;
         return true;
      } else {
         mLoginSucceeded = false;
         mUsername = null;
         clearPassword();
         throw new FailedLoginException("Incorrect password");
      }
   }

   public boolean commit() throws LoginException {
      if (mLoginSucceeded == false) {
         return false;
      }

      mPrincipal = new PrincipalImpl(mUsername);
      if (!(mSubject.getPrincipals().contains(mPrincipal))) {
         mSubject.getPrincipals().add(mPrincipal);
      }

      mUsername = null;
      clearPassword();
      mCommitSucceeded = true;
      return true;
   }

   public boolean abort() throws LoginException {
      if (mLoginSucceeded == false) {
         return false;
      } else if (mLoginSucceeded == true && mCommitSucceeded == false) {
         mLoginSucceeded = false;
         mUsername = null;
         clearPassword();
         mPrincipal = null;
      } else {
         logout();
      }
      return true;
   }

   public boolean logout() throws LoginException {
      mSubject.getPrincipals().remove(mPrincipal);
      mLoginSucceeded = false;
      mCommitSucceeded = false;
      mUsername = null;
      clearPassword();
      mPrincipal = null;
      return true;
   }
}
