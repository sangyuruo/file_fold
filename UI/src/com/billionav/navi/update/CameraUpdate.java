package com.billionav.navi.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import android.util.Log;

import com.billionav.DRIR.jni.jniDRIR_CameraPreview;
import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.W3JNI;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;

public class CameraUpdate {
	
	public static String TAG = "CameraCalibration";
	
	public static String filePath = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH)
					+ "ADAS_DATA" + File.separator+"config";
	
	public static String tempFilePath =  filePath + File.separator + "TempCameraPara.DAT";
	public static String realFilePath =  filePath + File.separator + "CameraPara.DAT";
	
	public static void checkoutCameraData() {
		VersionControl_ManagerIF.Instance().GetCameraInfos();
	}
	
	//if has temp file, and then update
	public static void updateCameraData() {
		File toBeRenamed = new File(tempFilePath);
		if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
			Log.d(TAG, "File does not exist: " + tempFilePath);
			return;
		}else{
			File delFile = new File(realFilePath);
		    if (delFile.isFile() && delFile.exists()) {
		    	delFile.delete();   
		    }
		}
		File newFile = new File(realFilePath);
		if (toBeRenamed.renameTo(newFile)) {
			Log.d(TAG, "File has been renamed.");
		} else {
			Log.d(TAG, "Error renmaing file");
		}
	}
	
	
}
