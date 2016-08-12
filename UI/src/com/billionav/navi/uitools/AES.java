package com.billionav.navi.uitools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class AES {
	/*
   * encrypt key contain 26 letters and digits
   *here use AES-128-CBC model锛宬ey's length must be 16銆�
   */
	public static String cKey = "1234567898765432";

    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key id null");
            return null;
        }
        // judge whethe Key's length is 16
        if (sKey.length() != 16) {
            System.out.print("Key's length is not 16");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//use CBC model锛宯eed a iv
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        return DataTools.byteTOString(Base64.getInstance().encode(encrypted, 0, encrypted.length));
    }

    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
        	// judge whethe Key's length is 16
            if (sKey == null) {
                System.out.print("Key id null");
                return null;
            }
            // 鍒ゆ柇Key鏄惁涓�6浣�
            if (sKey.length() != 16) {
                System.out.print("Key's length is not 16");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708"
                    .getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getInstance().decode(sSrc.getBytes(), 0,  sSrc.getBytes().length);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
}