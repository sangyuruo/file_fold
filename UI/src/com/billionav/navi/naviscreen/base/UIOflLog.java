package com.billionav.navi.naviscreen.base;

import android.util.Log;

public class UIOflLog {
	public static String TAG1 = "requestListLog";
	public static boolean  OPENTAG1 = true; 
	public static String TAG2 = "state";
	public static boolean  OPENTAG2 = true; 
	public static String TAG3 = "operate";
	public static boolean  OPENTAG3 = true; 
	public static String TAG4 = "status";
	public static boolean  OPENTAG4 = true;
	public static String TAG5 = "today";
	public static boolean  OPENTAG5 = true;
	public static void TAG1(String str){
		if(OPENTAG1){
			Log.i(TAG1, str);	
		}
	}
	public static void TAG_STATE(String str){
		if(OPENTAG2){
			Log.i(TAG2, str);	
		}
	}
	public static void TAG_OPERATE(String str){
		if(OPENTAG3){
			Log.i(TAG3, str);	
		}
	}
	public static void TAG4(String str){
		if(OPENTAG4){
			Log.i(TAG4, str);	
		}
	}
	public static void TAG5(String str){
		if(OPENTAG5){
			Log.i(TAG5, str);	
		}
	}
}
