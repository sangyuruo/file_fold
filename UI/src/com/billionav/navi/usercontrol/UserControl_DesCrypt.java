package com.billionav.navi.usercontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.billionav.jni.FileSystemJNI;

public class UserControl_DesCrypt {
	
	private final String m_sCryptAlgorithm = "DES";
	public static final String 	STORAGE_VERSION  = "0.0.3";//0.0.1
	private String strStoredPath = "USER/RW/UserinfoLogin";

	//create a secret key by KeyGenerator
	private String m_sFilePath = "";
	public UserControl_DesCrypt() {
		FileSystemJNI js=FileSystemJNI.instance();
		m_sFilePath = js.getSystemPath(strStoredPath)+"/DesUserInfo.dat";
		String folder=js.getSystemPath(strStoredPath);
		
		
		if( ! new File(folder).exists())
		{
			new File(folder).mkdirs();
		}
		if(! new File(m_sFilePath).exists())
		{
			try {
				new File(m_sFilePath).createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public SecretKey CreateSecretKey() {
		KeyGenerator keygen;   
		SecretKey deskey = null;  
		try {
			keygen = KeyGenerator.getInstance(m_sCryptAlgorithm);
			deskey = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();  
		}  
		return deskey;  
	}  
	
	//Encrypt info parameter to byte2hex by using Cipher
	public String Encrypt(SecretKey key, String info) {
		SecureRandom sr = new SecureRandom();  
		byte[] cipherByte = null;  
		try {
			Cipher c1 = Cipher.getInstance(m_sCryptAlgorithm);
			c1.init(Cipher.ENCRYPT_MODE, key, sr);
			cipherByte = c1.doFinal(info.getBytes());  
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
		return Byte2hex(cipherByte);
    }
	 
	public String Decrypt(SecretKey key, String sInfo) {  
		 SecureRandom sr = new SecureRandom();  
		 byte[] cipherByte = null;  
		 try {
			 Cipher c1 = Cipher.getInstance(m_sCryptAlgorithm);
			 c1.init(Cipher.DECRYPT_MODE, key, sr);
		     cipherByte = c1.doFinal(Hex2byte(sInfo));  
		 } catch (Exception e) {
			 e.printStackTrace();  
		 }
		 return new String(cipherByte);
    }
	public byte[] Hex2byte(String hex) {
		int size = hex.length();
		byte[] ret = new byte[size/2];
		byte[] byHex = hex.getBytes();
		for (int i = 0; i < size; i+=2) {
			String str = new String(byHex, i, 2);
			ret[i/2] = (byte) Integer.parseInt(str, 16);
		}  
		return ret;
	}
	 
	public String Byte2hex(byte[] b) {  
		String hs = "";  
		String stmp = "";  
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	private boolean ReadObjFromFile(Object[] objs) {
		ObjectInputStream ois = null;
		boolean result = true;
		try {
			FileInputStream fis = new FileInputStream(m_sFilePath);
			ois = new ObjectInputStream(fis);
			for (int i = 0; i < objs.length; i++) {
				objs[i] = ois.readObject();
		    }
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			try {
				ois.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
		return result; 
	}

	public boolean CleanContextOfFile() {
		File f = new File(m_sFilePath);
		FileWriter fw;
		try {
			fw = new FileWriter(f);
			fw.write("");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//write objs.length object to file,it contain an key at first
	private synchronized boolean WriteObjToFile(Object[] objs) {
		ObjectOutputStream oos = null;
		boolean result = true;
		try {  
		    FileOutputStream fos = new FileOutputStream(m_sFilePath);  
		    oos = new ObjectOutputStream(fos);
		    for (int i = 0; i < objs.length; i++) {
			    oos.writeObject(objs[i]);
		    }
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void SetFilePath(String path) {
		m_sFilePath = path;
	}
	//save objs.length+1 objects to file,and the first object saved is key
	public boolean SaveEncryptInfoToFile(Object[] objs){
		if( 1 > objs.length) {
			return false;
		}
		Object[] saveObjs = new Object[objs.length+2];
		SecretKey key = CreateSecretKey();
		saveObjs[0] = STORAGE_VERSION;
		saveObjs[1] = key;
		for(int i = 2; i < saveObjs.length; i++) {
			if(null == objs[i-2]) {
				return false;
			}
			saveObjs[i] = Encrypt(key, (String)objs[i-2]);
		}
		return WriteObjToFile(saveObjs);
	}
	
	public boolean GetDecryptInfoFromFile(Object[] objs/*out*/) {
		//if m_sFilePath file is empty then it will return false
		if(0 == new File(m_sFilePath).length() || 1 > objs.length) {
			return false;
		}
		Object[] saveObjs = new Object[objs.length+1];
		//we read two objects from file,the first object is key ,the second is what we need at last 
		if(! ReadObjFromFile(saveObjs)) {
			return false;
		}
		try{
			objs[0] = saveObjs[0];
			SecretKey key = (SecretKey)saveObjs[1];
			for(int i = 2; i < saveObjs.length; i++) {
				if(null != saveObjs[i] && null != key) {
					//basing on the first object which is key,we can get the real object by decrypt
					objs[i-1] = Decrypt(key, (String)saveObjs[i]);
				} else {
					objs[i-1] = null;
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}finally {
			
		}
		return true;
	}
}
