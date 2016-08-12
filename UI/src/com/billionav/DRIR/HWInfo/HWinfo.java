package com.billionav.DRIR.HWInfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.billionav.DRIR.jni.jniDRIR_HWInfo;

public class HWinfo {
	public static String sModel;
	public static String sRelease;
	
	public static void SetHWInfo()
	{  
		sModel = Build.MODEL; 
		sRelease = Build.VERSION.RELEASE;
	 
		//set MODEL
		jniDRIR_HWInfo.SetHWInfo(sModel, sModel.length());
		//set the version of android OS
		jniDRIR_HWInfo.SetHWAandroidVer(sRelease, sRelease.length());
		
		jniDRIR_HWInfo.SetDeviceAPILevel(android.os.Build.VERSION.SDK_INT);
	}
	  
	public static void SetDisplayMetrics(int heightPixels, int widthPixels)
	{  
		jniDRIR_HWInfo.SetDisplayMetrics(heightPixels, widthPixels);
	}
	
	public static void SetCPUInfo(int NumCores,int CPUFreq)
	{
		jniDRIR_HWInfo.SetCPUInfo(NumCores, CPUFreq);
	}
	
	public static boolean IsExistCamera(Context c)
	{
		if(c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
