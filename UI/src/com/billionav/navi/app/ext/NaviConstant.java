
package com.billionav.navi.app.ext;


public interface NaviConstant {
	boolean OPEN_POI = false;
	
	boolean isNonLoopForWriteHU = false;
//	1: show only one devices , 2: show on two devices
	int MODE_MEDIA_SURFACE = 2; //0721 test
	
	boolean IS_CACHE_READPIXEL = false;
	
	boolean IS_OFF_SCREEN = false;
	int SURFACE_TYPE = 3;
	//0: default , 1: pbuffer , 2: bitmap, 3: MediaCodec
	//是否使用信号量方式来传递数据给applink
	boolean IS_BUFFER_POOL_FOR_EGL = true;
	long SEND_SLEEP_TIME = 100;
	
//	boolean WRITE_ERROR_LOG_TOFILE = false;
//	boolean DEBUG_MEDIA_CODEC = false;
//	boolean DEBUG_EGL_STEP = false;
//	boolean WRITE_EGL_STEP_LOG_TOFILE = false;
//	boolean WRITE_COST_TIME_TOFILE = false;
//	boolean DEBUG_EGL_DRAW_COST_TIME = false;
//	boolean WRITE_EGL_DRAW_COST_TIME_TOFILE = false;
//	boolean DEBUG_DECODER_COST_TIME = false;
//	boolean WRITE_DECODER_COST_TIME_TOFILE = false;
//	boolean DEBUG_QUEUE_TOHU_COST_TIME = false;
//	boolean WRITE_QUEUE_TOHU_COST_TIME_TOFILE = false;
//	boolean LOG_ERROR = false;
	
	boolean WRITE_ERROR_LOG_TOFILE = true;
	boolean DEBUG_MEDIA_CODEC = true;
	boolean DEBUG_EGL_STEP = true;
	boolean WRITE_EGL_STEP_LOG_TOFILE = true;
	boolean WRITE_COST_TIME_TOFILE = true;
	boolean DEBUG_EGL_DRAW_COST_TIME = true;
	boolean WRITE_EGL_DRAW_COST_TIME_TOFILE = true;
	boolean DEBUG_DECODER_COST_TIME = true;
	boolean WRITE_DECODER_COST_TIME_TOFILE = true;
	boolean DEBUG_QUEUE_TOHU_COST_TIME = true;
	boolean WRITE_QUEUE_TOHU_COST_TIME_TOFILE = true;
	boolean LOG_ERROR = true;
	
	boolean DEBUG_COST_TIME = false;
	
	boolean WRITE_LOG_TOFILE = false;
	boolean DEBUG = false;
	boolean DEBUG_MAP = false;
	boolean DEBUG_SEND_SUCCESS = false;
	boolean DEBUG_SEND_TOHU = false;
	
	boolean DEBUG_TOUCH = false;
	boolean DEBUG_TO_HU_LOOP_TIME = false;
	
	String TAG_DEBUG_TO_HU_LOOP_TIME = "SDLToHuLoopTime";
	String TAG_TOUCH = "SNTouch";
	String TAG_MEDIA_CODEC = "Mediacodec";
	String TAG_EGL_STEP = "EGLStep";
	String TAG_EGL_ERROR = "EGLError";
	String TAG_CLASS_CALL = "SDLNaviCall";
	String TAG_ERROR = "SDLNaviError";
	String TAG_MCODE_TIME = "SDLNaviMCodeTime";
	String TAG_MAP = "SDLNaviMap";
	String TAG = "SDLNavi";
	String TAG_PROXY_SERVICE = "SDLNaviServ";
	String TAG_LOOP_SEND = "SDLNaviLoopSend";
	String TAG_LOOP_SEND_SUCCESS = "SDLNaviLoopSendSuccess";
	String TAG_LOOP_SEND_TOHU = "SDLNaviToHU";
	String TAG_HMI = "SDLNaviHMI";

	String TAG_LOOP_SEND_ERROR = "SDLNaviLoopSendError";
	String TAG_POI_ERROR = "POI_ERR";
	String TAG_PROXY_CIRCLE = "SDLNaviProxyCircle";
	boolean IS_HUAWEI = false;

	/*
	 * 
	 * for sync
	 */
//	int FRAME_RATE = 15;
//	int IFRAME_INTERVAL = 10;
//	String MIME_TYPE = "video/avc";

	long BUFFER_POOL_GET_WAIT_TIME = 50;
	long BUFFER_POOL_PUT_WAIT_TIME = 10;
	long BUFFER_LOOP_WAIT_TIME = 50;
	
	
}
