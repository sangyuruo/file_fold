package com.billionav.navi.uicommon;

import java.io.BufferedReader;
import java.io.FileReader;

import com.billionav.jni.UIHudControl;
import com.billionav.navi.uitools.SharedPreferenceData;

public class UIC_SystemCommon {

private static boolean isDay = true;
	
	public static void setIsDayStatus(boolean isday){
		isDay = isday;
	}

	public static boolean getIsDayStatus(){
		return isDay;
	}
	public static String getFileContext(String filepath){
		if (filepath == null || "".equals(filepath)) {
			return null;
		}
		BufferedReader reader = null;
		String temp = null;
		StringBuffer InfoStr = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(filepath));
			while ((temp = reader.readLine()) != null) {
				InfoStr.append(temp);
				InfoStr.append("\n");
			}
			reader.close();
			return InfoStr.toString();
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public static void setHUD(){
		boolean bPoi = SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_POI, false);
		boolean bCongestion = SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_TRAFFIC_INFO, false);;
		boolean bRestriction = SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_TRAFFIC_RESTRICT, false);;
		boolean bSpeedcamera = SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_SPEEDCAMER, false);;
		boolean bTimeFormat_24H = SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_TIME_24, false);;

//		System.out.println("poi:"+bPoi+"  bCongestion:"+bCongestion+"   bRestriction:"+bRestriction);
		int[] LogoMarkItmes = {SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK1, -1),
				SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK2, -1),
				SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK3, -1),
				SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK4, -1),
				SharedPreferenceData.getInt(SharedPreferenceData.HUD_LOGOMARK5, -1)};
		UIHudControl.HUDSetting(bPoi, bCongestion, bRestriction, bSpeedcamera, bTimeFormat_24H, LogoMarkItmes );
	}
	
}
