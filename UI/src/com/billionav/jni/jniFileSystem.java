package com.billionav.jni;

import java.io.File;

import android.content.Context;

public final class jniFileSystem {

	private static jniFileSystem jFS= new jniFileSystem();
	
	public final static String apkdata = "privatedata";
	public final static String FILE_NDATA_PATH = "NDATA/NDATA/";
	public final static String FILE_USER_PATH = "USER/";
	public final static String FILE_SDCARD_PATH = "SD/";
	
	private jniFileSystem() {
		
	}
	
	public static jniFileSystem instance() {
		return jFS;
	}
	
	//get the apl specialize path which would do a mapping
	//for example:
	//			NDATA\\NDATA would be mapped into /sdcard/NDATA/NDATA
	public native String getSystemPath(String FilePath);
	
	private native void setApkDataFullPath(String path);
	
	//get apl private data ,which is under the /data/data/packagename/
	public native String getApkDataFullPath();
	
	//mapping a
	public void mapPath(Context context) {
		File dir = context.getDir(apkdata,Context.MODE_PRIVATE);
		setApkDataFullPath(dir.getAbsolutePath());
	}
	
}
