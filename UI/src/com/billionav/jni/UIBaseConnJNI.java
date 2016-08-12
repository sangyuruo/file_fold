package com.billionav.jni;

public class UIBaseConnJNI {

	public static final int DAY_OPTION_AUTO = 0;
	public static final int DAY_OPTION_ALWAYS_DAY = 1;
	public static final int DAY_OPTION_ALWAYS_NIGHT = 2;
	
	public static native boolean setNetConnState(int curState);
	
	public static native int getNetConnStatus();
	
	public static native void setDayNightOption(int option);
	
	public static native void setSessionToken(String token);
	
	public static native void notifyLogin(String userID, String token);
	
	public static native void notifyLogout();
	
	public static native void addCommonHttpHeader(String key, String Value);
	
	public static native void sendBtEvent(boolean isConnected);
}
