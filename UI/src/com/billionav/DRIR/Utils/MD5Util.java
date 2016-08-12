package com.billionav.DRIR.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

/**
 * md5 algorithm tool
 */
public class MD5Util {
    
    static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
            Log.e("MD5", "NoSuchAlgorithmException: md5", ne);
        }
    }

    /**
     * calculator the md5 of the file
     * @param f 
     * @return md5 string  
     */ 
    public static String md5(File f) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
//            byte[] buffer = new byte[8192];
			byte[] buffer = null;
		    try{
		    	buffer = new byte[8192];
		    }
		    catch (OutOfMemoryError e)
		    {
		    	e.printStackTrace();
		    	return null;
		    }
		    
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            
            //return new String(Base64.encode(md.digest(), md.digest().length));
            return byte2hex(md.digest());
            //return new String(Hex.encodeHex(md.digest()));

        } catch (FileNotFoundException e) {
            Log.e("MD5", "md5 file " + f.getAbsolutePath() + " failed:" + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("MD5", "md5 file " + f.getAbsolutePath() + " failed:" + e.getMessage());
            return null;
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static String byte2hex(byte[] b){
        String hexstr="";
        String tempstr="";
        for (int n=0; n<b.length; n++){
        	tempstr=(java.lang.Integer.toHexString(b[n] & 0xFF));
            if (tempstr.length()==1) 
            {
            	hexstr = hexstr+"0"+tempstr;
            }
            else
            { 
            	hexstr = hexstr+tempstr;
            }
        }
        return hexstr;
    }

}