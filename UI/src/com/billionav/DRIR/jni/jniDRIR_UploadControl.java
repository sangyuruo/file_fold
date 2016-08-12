package com.billionav.DRIR.jni;

import java.io.File;

import android.util.Log;

import com.billionav.DRIR.Upload.UploadHandler;
import com.billionav.DRIR.Utils.GZipUtils;
import com.billionav.DRIR.Utils.MD5Util;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class jniDRIR_UploadControl {
	private final static String TAG = "UPLOAD";
	 
	public static native void	J2CAttach();
	public static native void	J2CDetach();
	public static native void UFResultFromJava(String fileName);
	public static native void SetWifiCnntStatus(int cnntStatus);
	public native static int IsOnServer(String[] strFilePath, int fileType);
	public native static int AddFile(String strFilePath, int fileType);
	public native static int uploadFile(String strFilePath, int fileType);
	public native static int CancelUpload(String strFilePath, int fileType);
	public native static void DeleteFile(String strFilePath, int fileType);
	public native static void SetMD5(String sDevID, int iSize);
	  
	public static int CheckNetWork()
	{ 
		return UploadHandler.getInstance().GetNetWorkStatus();
	}
	
	public static boolean CompressFile(String sInFileName, String sOutFileName)
	{ 
		return GZipUtils.compress(sInFileName, sOutFileName, false); 
	}  
   
	public static void SetQuitFlag(boolean bQuitFlag)
	{ 
		GZipUtils.setQuitFlag(bQuitFlag);
	} 
	
	public static boolean IsLogIn()
	{
		return UserControl_ManagerIF.Instance().HasLogin();
	}
	 
	public static void SendTriggerToUI(String strFilePath, int param1)
	{
		Log.i("UPLOAD", "SendTrg in JNI: File:" + strFilePath +"param1:" + param1);
		UploadHandler.getInstance().sendTigger(strFilePath, param1);
	}
	
	public static void GetMD5(String strFilePath)
	{
		String strMD5 = "";
		File file = new File(strFilePath); 
		if(file != null)
		{ 
			strMD5 = MD5Util.md5(file);
		}
		Log.i("UPLOAD_C", "File:" + strFilePath + "MD5:" + strMD5);
		 
		SetMD5(strMD5, strMD5.length());
	} 
}
