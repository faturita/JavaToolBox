package org.security;
/**
 * CryptoUtil.java
 *
 * Common 1.1.0 vexrarod 2004-07-07
 * Migracion de versiones de soft base, migracion a BouncyCastle.
 *
 * @author Fabian Mandelbaum <mandelbaum@visa.com.ar>, Jorge Campos <campos@visa.com.ar>
 * @version 0.3 2001-03-12
 *
 * This class implements a few methods for cryptography. It uses the IAIK kit.
 */


import java.util.Random;
import java.security.Key;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.*;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


     /** PBEKeySpec          pbeSpec = new PBEKeySpec(_string.toCharArray());
      SecretKeyFactory keyFact = null;
      try {
         keyFact = SecretKeyFactory.getInstance("PBEWithSHAAnd3-KeyTripleDES-CBC", "BC");
      } catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      }

      _pbeKey = keyFact.generateSecret(pbeSpec);**/

public class CryptoUtil {
   static {
      Security.insertProviderAt(new BouncyCastleProvider(),2);
   }

   /**
    * Encrypts the data given in _abPlainData with the key _key using a
    * predefined encryption method (PbeWithSHAAnd3_KeyTripleDES_CBC)
    *
    * @param _abPlainData the data to encrypt as an array of bytes
    * @param _key the key to use for encryption. Should be a PBEKeyBMP key type
    * @param _abSalt is the <quote>salt</quote> value to use
    * @param _iCount is the number of loops of the algorithm
    *
    * @return the encrypted data as an array of bytes or an array of size 0 if
    * an exception ocurrs.
    */
   public static byte[] encryptPBE(
         byte[] _abPlainData,
         Key _key,
         byte[] _abSalt,
         int _iCount
         )
         throws
         InvalidAlgorithmParameterException,
         NoSuchPaddingException,
         BadPaddingException,
         InvalidKeyException,
         IllegalBlockSizeException,
         NoSuchAlgorithmException {
      PBEParameterSpec _pbeParameterSpec;
      Cipher _pbeCipher = null;
      byte[] _abEncryptedData;

      // PBE parameters
      _pbeParameterSpec = new PBEParameterSpec(_abSalt, _iCount);
      try {
         _pbeCipher = Cipher.getInstance("PBEWithSHAAnd3-KeyTripleDES-CBC", "BC");
      } catch (NoSuchProviderException _nspe) {
         _nspe.printStackTrace();
      }
      // Initialize for encryption
      _pbeCipher.init(Cipher.ENCRYPT_MODE, _key, _pbeParameterSpec);
      //_pbeCipher.init(Cipher.ENCRYPT_MODE, (PBEKeyBMP) new PBEKeyBMP("test"), _pbeParameterSpec);
      // Encrypt data
      _abEncryptedData = _pbeCipher.doFinal(_abPlainData);
      //System.out.println("Encrypted Length: " + _abEncryptedData.length);
      return _abEncryptedData;
   } // encrypt


   /**
    * Decrypts the data given in _abEncryptedData using the key _key
    *
    * @param _abEncryptedData the data to decrypt as an array of bytes
    * @param _key the key to use for decryption
    * @param _abSalt the <quote>salt</quote> value to use
    * @param _iCount the number of loops of the algorithm
    *
    * @return the decrypted data as an array of bytes
    */
   public static byte[] decryptPBE(
         byte[] _abEncryptedData,
         Key _key,
         byte[] _abSalt,
         int _iCount
         )
         throws
         InvalidAlgorithmParameterException,
         NoSuchPaddingException,
         BadPaddingException,
         InvalidKeyException,
         IllegalBlockSizeException,
         NoSuchAlgorithmException {
      PBEParameterSpec _pbeParameterSpec;
      Cipher _pbeCipher = null;
      byte[] _abDecryptedData;

      // PBE parameters
      _pbeParameterSpec = new PBEParameterSpec(_abSalt, _iCount);
      try {
         _pbeCipher = Cipher.getInstance("PBEWithSHAAnd3-KeyTripleDES-CBC", "BC");
      } catch (NoSuchProviderException _nspe) {
         _nspe.printStackTrace();
      }
      // Initialize for decryption
      _pbeCipher.init(Cipher.DECRYPT_MODE, _key, _pbeParameterSpec);
      // Decrypt data
      _abDecryptedData = _pbeCipher.doFinal(_abEncryptedData);
      return _abDecryptedData;
   } // doDecrypt

} // Crypto