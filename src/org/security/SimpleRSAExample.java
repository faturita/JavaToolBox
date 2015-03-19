package org.security;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * This class uses BC provedire (BouncyCastle).
 *
 * It need to have the bc jar file in the classpath (or added as an extension)
 * and the security file configured to allow this new provider.
 */
public class SimpleRSAExample {
   public static void main(String [] args) throws Exception {
      System.out.println("Genearting. a symmetric blowfish key...");
      KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish","BC");
      keyGenerator.init(128);
      Key blowfishKey = keyGenerator.generateKey();

      System.out.println("Format:"+blowfishKey.getFormat());

      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA","BC");
      keyPairGenerator.initialize(1024);
      KeyPair keyPair = keyPairGenerator.genKeyPair();

      System.out.println("Done generating key...");
      System.out.println("Private Key:"+keyPair.getPrivate().getEncoded());
      System.out.println("Public Key:"+keyPair.getPublic().getEncoded());


      Cipher cipher= Cipher.getInstance("RSA","BC");
      cipher.init(Cipher.ENCRYPT_MODE,keyPair.getPublic());

      byte[] blowfishKeyBytes = blowfishKey.getEncoded();
      System.out.println("PlainKey:"+blowfishKeyBytes);

      byte[] cipherText = cipher.doFinal(blowfishKeyBytes);
      System.out.println("CipherKey:"+cipherText);

      cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

      // perform the decryption
      byte[] decryptedKeyBytes = cipher.doFinal(cipherText);

      SecretKey newBlowfishKey = new SecretKeySpec(decryptedKeyBytes, "Blowfish");
      System.out.println("PlainKey:"+decryptedKeyBytes);
   }

}
