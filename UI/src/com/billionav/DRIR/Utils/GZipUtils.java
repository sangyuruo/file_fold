package com.billionav.DRIR.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
   
public abstract class GZipUtils {  
  
    public static final int BUFFER = 1024;  
    public static final String EXT = ".gz";  
	public static boolean QuitFlag = false;
  
	public static void setQuitFlag(boolean quitFlag) {
		QuitFlag = quitFlag;
	}
	
    /** 
     * File compress
     *  
     * @param file 
     * @throws Exception 
     */  
    public static boolean compress(File resFile){  
        return compress(resFile, null , true);  
    }  
  
    /** 
     * File compress
     *  
     * @param file 
     * @throws Exception 
     */  
    public static boolean compress(File resFile, File desFile){  
        return compress(resFile, desFile , true);  
    }  
  
    /** 
     * File compress with delete file
     *  
     * @param file 
     * @param delete 
     * @throws Exception 
     */  
    public static boolean compress(File resFile, File desFile, boolean delete){  
    	boolean bRet = false;
    	QuitFlag = false;
        FileInputStream fis;
		try {
			fis = new FileInputStream(resFile);
			
			FileOutputStream fos = null;
	        if (desFile == null)
	        {
	        	fos = new FileOutputStream(resFile.getPath() + EXT);
	        }
	        else
	        {
	        	fos = new FileOutputStream(desFile.getPath());
	        }
	          
	  
	        bRet = compress(fis, fos);  
	  
	        fis.close();  
	        fos.flush();  
	        fos.close();  
	        
	        if (bRet)
	        {
	        	if (delete) {  
		        	resFile.delete();  
		        }
	        }
	        else
	        {
	        	if (desFile.exists())
	        	{
	        		desFile.delete();
	        	}
	        }       
	        
		} catch (FileNotFoundException e) {
			bRet = false;
			e.printStackTrace();
		} catch (IOException e) {
			bRet = false;
			e.printStackTrace();
		}  
        return bRet;
    }  
  
    /** 
     * Data compress with stream
     *  
     * @param is 
     * @param os 
     * @throws IOException 
     * @throws Exception 
     */  
    public static boolean compress(InputStream is, OutputStream os)  
    {  
    	boolean bRet = false;
        GZIPOutputStream gos;
		try {
			gos = new GZIPOutputStream(os);
			int count;
			boolean bInterrupt = false;
	        byte data[] = new byte[BUFFER];  
	        while ((count = is.read(data, 0, BUFFER)) != -1) {
	        	if (QuitFlag)
	        	{
	        		bInterrupt = true;
	        	}
	            gos.write(data, 0, count);  
	        } 
	        gos.finish();  
	        
	        gos.flush();  
	        gos.close();
	        
	        if (bInterrupt)
	        {
	        	bRet = false;
	        }
	        else
	        {
	        	bRet = true;
	        }	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bRet;
    }  
  
    /** 
     * File compress
     *  
     * @param path 
     * @throws Exception 
     */  
    public static boolean compress(String resPath) {  
        return compress(resPath, null, true);  
    }
    
    /** 
     * File compress
     *  
     * @param path 
     * @throws Exception 
     */  
    public static boolean compress(String resPath, String desPath) {  
        return compress(resPath, desPath, true);  
    }  
  
    /** 
     * File compress
     *  
     * @param path 
     * @param delete 
     * @throws Exception 
     */  
    public static boolean compress(String resPath, String desPath, boolean delete){  
        File resFile = new File(resPath);
        File desFile = null;
        if (null != desPath)
        {
        	desFile = new File(desPath);
        }
        return compress(resFile, desFile, delete);  
    }    
}  
