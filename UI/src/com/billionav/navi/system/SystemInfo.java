package com.billionav.navi.system;

import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class SystemInfo {
	private static char hexDigits[] = {       // convert byte 2 char     
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'}; 
	private static String sysDeviceID = null;
	private static String sysSimSN = null;
	private static String sysAndroidID = null;
	private static String sysMacAddr = null;	
	private static String phoneNumber = null;
	private static String sysUUID = null;
	private static float[] dpi = new float[2];
	
	// Initialize System Info, Called when APL Start.  
	// Input ViewManager's context.
	public static  void Initialize(Context c) {
		if(c != null) {
			GetSystemInfo(c);
			PLog.d("SysInfo", "SysInfo Init");
		}
	}
	
	private static void GetSystemInfo(Context sysContext) {
		final TelephonyManager tm = (TelephonyManager) sysContext.getSystemService(Context.TELEPHONY_SERVICE); 
//		sysDeviceID = tm.getDeviceId(); 
		sysDeviceID = "";
		sysAndroidID = android.provider.Settings.Secure.getString(sysContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);     
		sysSimSN = tm.getSimSerialNumber();

	    // Get MacAddress
	    WifiManager wifi = (WifiManager) sysContext.getSystemService(Context.WIFI_SERVICE);  
		WifiInfo info = wifi.getConnectionInfo();  
		sysMacAddr = info.getMacAddress();  	   
		phoneNumber = tm.getLine1Number();	

		DisplayMetrics dm  = sysContext.getResources().getDisplayMetrics();  
		dpi[0] = dm.xdpi;             
		dpi[1] = dm.ydpi; 
		PLog.d("SysInfo", "SysInfo:" + sysDeviceID + "|" + sysAndroidID + "|" + sysSimSN + "|" + sysMacAddr);
		initSystemUuid();
	}
	public static String GetSysUUID(){
		if(null == sysUUID){
			initSystemUuid();
		}
		return sysUUID;
	}
	
	public static String GetDeviceNumber() {
		return sysDeviceID;
	}

	public static String GetAndroidID() {
		return sysAndroidID;
	}
	
	public static String GetSimSN() {
		return sysSimSN;
	}

	public static String GetMacAddr() {
		return sysMacAddr;
	}
	
	public static String GetPhoneNumber() {
		return phoneNumber;
	}
	
	public static String getDeviceNo(){
//		String deviceNo = null;
//		 deviceNo = SystemInfo.GetDeviceNumber();
//		 if(TextUtils.isEmpty(deviceNo)){
//			 deviceNo = SystemInfo.GetMacAddr();
//			 deviceNo = "M"+deviceNo;
//		 }else{
//			 deviceNo = "I"+deviceNo;
//		 }
//		return deviceNo;
		return GetSysUUID();
	}
	
	public static float[] getDpi() {
		return dpi;
	}
	//get MAC address +"000", degest MD5 value
	private static void initSystemUuid(){
		byte[] b = (GetMacAddr()+"000").getBytes();
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
			md.update( b );
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) { 
				byte byte0 = tmp[i];  
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			sysUUID = new String(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
