package org.security;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: vexrarod
 * Date: 07/10/2003
 * Time: 08:13:31
 * To change this template use Options | File Templates.
 */
public class FileEncriptorRSA {
   private static final String ENCRYPTED_FILENAME_SUFFIX = ".encrypted";
   private static final String DECRYPTED_FILENAME_SUFFIX = ".decrypted";
   private static final int ITERATIONS = 1000;

   public static void main(String [] args) throws Exception {
      if ((args.length < 1) || (args.length > 2)) {
         usage();
      } else if ("-c".equals(args[0])) {
         createKey();
      } else if ("-e".equals(args[0])) {
         encrypt(args[1]);
      } else if ("-d".equals(args[0])) {
         decrypt(args[1]);
      } else {
         usage();
      }
   }

   private static void usage() {
      System.err.println("-c|-e-|d filename");
      System.exit(1);
   }

   private static void createKey() throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Password:");
      String password = in.readLine();
      System.out.println("geneartion key pair");

      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(1024);
      KeyPair keyPair = keyPairGenerator.genKeyPair();

      System.out.println("Done generating key");

      System.out.println("Public key filename:");
      String publicKeyFilename = in.readLine();

      byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

      FileOutputStream fos = new FileOutputStream(publicKeyFilename);
      fos.write(publicKeyBytes);
      fos.close();

      System.out.print("Private key filename:");
      String privateKeyFilename = in.readLine();

      byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

      byte[] encryptedPrivateKeyBytes = passwordEncrypt(password.toCharArray(), privateKeyBytes);

      fos = new FileOutputStream(privateKeyFilename);
      fos.write(encryptedPrivateKeyBytes);
      fos.close();
   }

   private static byte[] passwordEncrypt(char[] password, byte[] plaintext) throws Exception {
      byte[] salt = new byte[8];
      Random random = new Random();
      random.nextBytes(salt);

      PBEKeySpec keySpec = new PBEKeySpec(password);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwoFish-CBC");
      SecretKey key = keyFactory.generateSecret(keySpec);
      PBEParameterSpec paramSpec=new PBEParameterSpec(salt, ITERATIONS);
      Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwoFish-CBC");
      cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

      byte[] ciphertext = cipher.doFinal(plaintext);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(salt);
      baos.write(ciphertext);
      return baos.toByteArray();
   }

   private static void encrypt(String fileInput) throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Public key to encrypt with:");
      String publicKeyFilename = in.readLine();

      FileInputStream fis = new FileInputStream(publicKeyFilename);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int theByte = 0;
      while ((theByte = fis.read()) != -1 )
      {
         baos.write(theByte);
      }
      fis.close();

      byte[] keyBytes = baos.toByteArray();
      baos.close();

      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey publicKey = keyFactory.generatePublic(keySpec);

      String fileOutput = fileInput + ENCRYPTED_FILENAME_SUFFIX;
      DataOutputStream output = new DataOutputStream(new FileOutputStream(fileOutput));

      Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

      KeyGenerator rijndaelKeyGenerator = KeyGenerator.getInstance("Rijndael");
      rijndaelKeyGenerator.init(256);
      System.out.println("Generating session key");
      Key rijndaelKey = rijndaelKeyGenerator.generateKey();
      System.out.println("Done generating key");

      byte[] encodedKeyBytes = rsaCipher.doFinal(rijndaelKey.getEncoded());
      output.writeInt(encodedKeyBytes.length);
      output.write(encodedKeyBytes);

      SecureRandom random = new SecureRandom();
      byte[] iv = new byte[16];
      random.nextBytes(iv);

      output.write(iv);
      IvParameterSpec spec = new IvParameterSpec(iv);

      Cipher symmetricCipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
      symmetricCipher.init(Cipher.ENCRYPT_MODE, rijndaelKey, spec);

      CipherOutputStream cos = new CipherOutputStream(output, symmetricCipher);
      System.out.println("Encrypting the file...");

      FileInputStream input = new FileInputStream(fileInput);

      theByte = 0;
      while ((theByte = input.read()) != -1)
      {
         cos.write(theByte);

      }
      input.close();
      cos.close();
      System.out.println("File encrypted.");
      return;
   }

   private static byte[] passwordDecrypt(char[] password, byte[] ciphertext) throws Exception {
      byte[] salt = new byte[8];
      ByteArrayInputStream bais = new ByteArrayInputStream(ciphertext);
      bais.read(salt,0,8);

      byte[] remainingCiphertext = new byte[ciphertext.length-8];
      bais.read(remainingCiphertext,0,ciphertext.length-8);

      PBEKeySpec keySpec = new PBEKeySpec(password);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
      SecretKey secretKey = keyFactory.generateSecret(keySpec);

      PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);

      Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

      return cipher.doFinal(remainingCiphertext);
   }

   private static void decrypt(String fileInput) throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Private Key FileName to decrypt:");
      String privateKeyFilename = in.readLine();
      System.out.print("Password:");
      String password = in.readLine();

      FileInputStream fis  = new FileInputStream(privateKeyFilename);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int theByte = 0;
      while ((theByte = fis.read())!= -1)
      {
         baos.write(theByte);
      }
      fis.close();

      byte[] keyBytes = baos.toByteArray();
      baos.close();

      keyBytes = passwordDecrypt(password.toCharArray(), keyBytes);

      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

      Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

      DataInputStream dis = new DataInputStream(new FileInputStream(fileInput));
      byte[] encryptedKeyBytes = new byte[dis.readInt()];
      dis.readFully(encryptedKeyBytes);

      rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] rijndaelKeyBytes = rsaCipher.doFinal(encryptedKeyBytes);

      SecretKey rijndaelKey = new SecretKeySpec(rijndaelKeyBytes, "Rijndael");

      byte[] iv = new byte[16];
      dis.read(iv);
      IvParameterSpec spec = new IvParameterSpec(iv);

      Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, rijndaelKey, spec);
      CipherInputStream cis = new CipherInputStream(dis, cipher);

      System.out.println("Decrypting file");
      FileOutputStream fos = new FileOutputStream(fileInput + DECRYPTED_FILENAME_SUFFIX);

      theByte = 0;

      while ((theByte = cis.read())!= -1)
      {
         fos.write(theByte);
      }
      cis.close();
      fos.close();
      System.out.println("Done");
      return;



   }
}
