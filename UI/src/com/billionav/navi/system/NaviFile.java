package com.billionav.navi.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;

public class NaviFile {
	public final static int NAVI_FILE_SUCCESS = 0;
	public final static int NAVI_FILE_ERR_FAILED = 1;
	public final static int NAVI_FILE_ERR_MEMORY = 2;
	
	public final static int NAVI_SYNCMODE_OVERWRITE = 0;
	public final static int NAVI_SYNCMODE_RESERVED = 1;
	public final static int NAVI_SYNCMODE_NEWER = 2;

	public final static int FILE_COPY_BUFFERSIZE = 8*1024;
	
	private static AssetManager naviAssetManager = null;
//	protected final int errorcode = NAVI_FILE_SUCCESS;	
	
	private NaviFile() {
	}
	
	public final static void SetAssetManager(AssetManager am) {
		naviAssetManager = am;
	}
	
//	public final int AssetFileCopy(String source, String desti) {
	public final static int AssetFileCopy(String source, String desti) {
		int returnValue = NAVI_FILE_SUCCESS;
		// to ensure mBuffer is allocated before call it
		if (null == naviAssetManager){
			return NAVI_FILE_ERR_FAILED;
		}
		
		byte[] mBuffer = new byte[FILE_COPY_BUFFERSIZE];

		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		int len = 0;
		try {
			inputStream = naviAssetManager.open(source, AssetManager.ACCESS_STREAMING);
			outputStream = new FileOutputStream(desti);
			while((len = inputStream.read(mBuffer))!=-1) {
				outputStream.write(mBuffer,0,len);
			}
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			returnValue = NAVI_FILE_SUCCESS;
		} catch (IOException e) {
			returnValue = NAVI_FILE_ERR_FAILED;
		}
		inputStream = null;
		outputStream = null;
		mBuffer = null;
		return returnValue;
	}

	// Copy AssetDir
	public final static int AssetDirCopy(String source, String desti, int syncmode)  {
		AssetDirCopy(source, desti, syncmode, null);
		return NAVI_FILE_SUCCESS;
	}
	
	// Copy AssetDir
	public final static int AssetDirCopy(String source, String desti, int syncmode, String exclude)  {
		String[] assertDir = null;
		
		File destiExist = new File(desti);
		if(!destiExist.exists()){
			destiExist.mkdirs();
		}
		destiExist = null;

		try {
			assertDir = naviAssetManager.list(source);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int dirCount = 0;
		if ( assertDir != null){
			dirCount = assertDir.length;
		}
		else{
			dirCount = -1;
		}

		if ( dirCount > 0){
			// Is DIR
			for (int i = 0; i < dirCount; i++){
				if ( (null != exclude) &&( null != assertDir[i]) && exclude.contentEquals(assertDir[i])){
					continue;
				}
				if (assertDir[i].contains(".")){
					//  Is File,  FileName must contains "."				
					String deststring = null;
					if (assertDir[i].contains(".awb")){
						deststring = assertDir[i].replaceAll(".awb", "");
					}
					else{
						deststring = assertDir[i];
					}
					
					File destiFile = new File(desti + "/" + deststring);
					if (destiFile.exists()){
						if ( NAVI_SYNCMODE_OVERWRITE == syncmode ){
							destiFile.delete();							
						}
						else{
							continue;							
						}
					}
					AssetFileCopy(source+"/"+assertDir[i], desti + "/" + deststring);
					PLog.d("NaviFile", "NaviFile copy: "+source+"/"+assertDir[i] + " to Dest:" +desti + "/" + deststring);
					deststring = null;
				}
				else{
					//  Is DIR 
					File destiDir = new File(desti + "/" +assertDir[i]);
					if (!destiDir.exists()){
						destiDir.mkdir();
					}
					AssetDirCopy(source+"/"+assertDir[i], desti + "/" +assertDir[i], syncmode, exclude);						
				}
			}
		}
		assertDir = null;
		return NAVI_FILE_SUCCESS;
	}
}

