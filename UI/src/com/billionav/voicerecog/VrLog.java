package com.billionav.voicerecog;

import com.billionav.navi.system.PLog;

public class VrLog 
{
//	private static final String TAG = "VR";
	public static final int LEVEL_VERBOSE 	= 0;
	public static final int LEVEL_DEBUG 	= 1;
	public static final int LEVEL_INFO 		= 2;
	public static final int LEVEL_WARN 	  	= 3;
	public static final int LEVEL_ERROR 	= 4;
 
	private static int logLevel = LEVEL_DEBUG;		
	
	public static void setLogLevel(int level){
		logLevel = level;
	}
	
	//Send an ERROR log message.
	public static int e(String tag, String msg) {
		return (logLevel <= LEVEL_ERROR) ? PLog.e(tag, msg): 0;
	}
	
	//Send a VERBOSE log message.
	public static int w(String tag, String msg) {
		return (logLevel <= LEVEL_WARN) ? PLog.w(tag, msg): 0;
	}

	//Send an INFO log message.
	public static int i(String tag, String msg) {
		return (logLevel <= LEVEL_INFO) ? PLog.i(tag, msg): 0;
	}
	
	//Send an debug log message.
	public static int d(String tag, String msg){
		return (logLevel <= LEVEL_DEBUG) ? PLog.d(tag, msg): 0;
	}
	
	//Send an verbose log message.
	public static int v(String tag, String msg){
		return (logLevel <= LEVEL_VERBOSE) ? PLog.v(tag, msg): 0;
	}
}
