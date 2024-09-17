package com.credentek.msme.utils;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.KeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.crypto.NoSuchPaddingException;

public class AesUtil {

	private static final Logger log = LogManager.getLogger(AesUtil.class);
    private  int keySize=128;
    private  int iterationCount=1000;
    private final Cipher cipher;
    String salt="1b2fa91c4190e4a5109cb03ba240d82b";
    String iv="14742b48f49b679486ffbec22ac426cb";
    
    public AesUtil() {
      
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        	log.info("Exceptione: ",e);
            throw fail(e);
        }
    }
    /** 
     * Added by Ashitosh Rajguru
     * @param passphrase, plain text
     * @return AES Encrypted string
     */
    public String encrypt(String passphrase, String plaintext) {
    	
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException e) {
        	log.info("Exceptione: ",e);
            throw fail(e);
        }
    }
    
public net.sf.json.JSONObject encryptAmazon(String passphrase, String plaintext,String encryptText) {
	net.sf.json.JSONObject returnObj=new net.sf.json.JSONObject();
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
            String afterEncrypt=base64(encrypted);
            if(encryptText.equalsIgnoreCase(afterEncrypt)){
            	returnObj.put("errorCode", "00");
            	log.info("Match");
            }else{
            	returnObj.put("errorCode", "01");
            	log.info("Not Match");
            }
        }
        catch (UnsupportedEncodingException e) {
        	returnObj.put("errorCode", "01");
        	log.info("Exceptione: ",e);
            throw fail(e);
        }
        return returnObj;
    }
    
    /** 
     * Added by Ashitosh Rajguru
     * @param passphrase, ciphertext
     * @return AES Decrypted string
     */
    public String decrypt(String passphrase, String ciphertext) {
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
        	log.info("Exceptione: ",e);
            throw fail(e);
        }
    }
    public net.sf.json.JSONObject decryptAmazon(String passphrase, String ciphertext, String sha) {
    	net.sf.json.JSONObject returnObj=new net.sf.json.JSONObject();
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            returnObj.put("errorCode", "00");
            returnObj.put("Data", new String(decrypted, "UTF-8"));
            
            String shaValue = sha1EncryptPass(new String(decrypted, "UTF-8"));
    		if (!sha.equals(shaValue)) {
    			returnObj.put("errorCode", "01");
    			returnObj.put("errorMsg", "Parameter Manipulation");
    		}
        }
        catch (UnsupportedEncodingException e) {
        	returnObj.put("errorCode", "01");
        	returnObj.put("errorMsg", "UnsupportedEncodingException");
        	log.info("Exceptione: ",e);
        } catch (Exception e) {
        	returnObj.put("errorCode", "01");
        	returnObj.put("errorMsg", "Exception");
        	log.info("Exceptione: ",e);
        }
        return returnObj;
    }
    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
        	log.info("Exceptione: "+e);
            throw fail(e);
        }
    }
    
    private SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
           SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        	log.info("Exceptione: ",e);
            throw fail(e);
        }
    }
	public String sha1EncryptPass(String input){
		log.info("Inside sha1EncryptPass method ::");
         StringBuffer sb = new StringBuffer();
        MessageDigest mDigest;
       
        try {
            mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
           
        } catch (NoSuchAlgorithmException e) {
           
        	log.info("Exception ::"+e);
        }       
        log.info("End of sha1EncryptPass method ::");
        return sb.toString();
    } 
    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }
    
    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }
    
   
    
    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }
    
    public static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
    
    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
        	log.info("Exceptione: "+e);
            throw new IllegalStateException(e);
        }
    }
    
    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }	
	
	
}
