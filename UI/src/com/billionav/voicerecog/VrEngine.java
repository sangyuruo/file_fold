package com.billionav.voicerecog;

import android.content.Context;

public interface VrEngine {
	public static final int MODE_GENERAL	= 0;	// Free style speech
	public static final int MODE_COMMAND	= 1;	// Fixed command speech
	public static final int MODE_RAWTEXT	= 2;	// Free style speech without any command match
	
	public static final int FORCE_UPLOAD_DICT	= 0x1;
	public static final int DUMP_SPEECH_DATA 	= 0x2;
	
	// VR error code
	public static final int ERR_NONE 			= 0;// success
	public static final int ERR_NOMATCH 		= 1;// No recognized text
	public static final int ERR_SPEECH_TIMEOUT  = 2;// Speech timeout
	public static final int ERR_NETWORK_TIMEOUT = 3;// Network timeout
	public static final int ERR_SERVICE			= 4;// Service is not available
	public static final int ERR_USERSTOP		= 5;// User stop engine
	public static final int ERR_NETWORK_OFF		= 6;// Network is off
	public static final int ERR_BUSY 			= 7;// Internal error 

	boolean supportSpeech();
	void setListener(VrEngineListener listener);
	void setParam(int param1, String param2);
	boolean setContext(Context ctx);
	boolean uploadData(String category, String data);
	boolean startSpeech(int cmdMode, int timeout, String output);
	void stopSpeech();
}
