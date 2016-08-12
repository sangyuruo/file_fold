
package com.billionav.DRIR.jni;

public class jniDRIR_HWInfo {
	public native static void SetHWInfo(String sModel, int iSize); 
	public native static void SetDisplayMetrics(int iHeightPixels, int iWidthPixels);
	public native static void SetCPUInfo(int NumCores,int CPUFreq);
	public native static void SetHWAandroidVer(String sAandroidVer, int iSize);
	public native static void SetDeviceID(String sDevID, int iSize);
	public native static void SetDeviceAPILevel(int iLevel);  
} 

