package com.billionav.jni;

import java.io.File;

import com.billionav.navi.app.AndroidNaviAPP;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public final class FileSystemJNI {

	private static FileSystemJNI jFS= new FileSystemJNI();
	
	public final static String apkdata = "privatedata";
	public final static String FILE_NDATA_PATH = "NDATA/NDATA/";
	public final static String FILE_USER_PATH = "USER/";
	public final static String FILE_SDCARD_PATH = "SD/";
	public final static String FILE_NDATA_DOWNLOAD_PATH = "NDATA/";
	public final static String NAVI_ROOT_JP = ".kanavius";
	public final static String OFFLINE_DATA_PATH = "/USER/RW/WebNaviData/offline";
	
	private FileSystemJNI() {
		
	}
	
	public static FileSystemJNI instance() {
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
//		File dirExternal = context.getExternalFilesDir(null).getParentFile();
		File dirExternal = Environment.getExternalStorageDirectory() ;
		Log.d("test", "dirExternal = "+dirExternal);
//		setApkExternalDataFullPath(dirExternal.getAbsolutePath());
		
		String path = getNativeDataPath();
		Log.i("SDL", "data path = " + path);
		setApkExternalDataFullPath(path);
		setNaviRoot(NAVI_ROOT_JP);

		// icu4c lib path /data/data/appname/lib
//		setApkLibPath(dir.getAbsolutePath().replace("app_" + apkdata, apklib));
		// icu4c data, store in ndata
//		setICUDataPath(getSystemPath(FILE_NDATA_PATH));
	}
	
	public String getNativeDataPath() {

		String secondary = System.getenv("SECONDARY_STORAGE") + "/";
		String external = System.getenv("EXTERNAL_STORAGE") + "/";

		Log.i("SDL", "@@@ getNativeDataPath secondary = " + secondary
				+ "external = " + external);
		String offline = secondary + NAVI_ROOT_JP + OFFLINE_DATA_PATH;

		Log.i("SDL", "@@@ getNativeDataPath offline = " + offline);
		File secondaryFile = new File(offline);
		if (secondaryFile.exists()) {
			return secondary;
		} else {
			return external;
		}
	}

	private native void setNaviRoot(String naviRootJp);

	private native void setApkExternalDataFullPath(String absolutePath);
	
}
