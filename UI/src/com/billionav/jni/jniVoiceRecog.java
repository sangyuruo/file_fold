package com.billionav.jni;

import com.billionav.navi.system.PLog;
import com.billionav.voicerecog.VoiceRecogTools;
import com.billionav.voicerecog.VrEngine;
import com.billionav.voicerecog.VrContextNat;

public class jniVoiceRecog {
	public static final int ERR_NONE 	= 0;
	public static final int ERR_NETWORK = 1;
	public static final int ERR_WEIBO 	= 2;
    
    private static final int VRTYPE_SMS = 0;
    private static final int VRTYPE_CMD = 1;
    
	private static final String TAG = "VRC";
	
	private static jniVoiceRecog sInstance = null;
	private boolean inited = false;
	private VrContextNat mVrContext = null;
	
	// Native methods
	private native int Initialize();
	private native void Deinitialize();
	private native void OnVrResult(int reqID, String content, int confident, int errCode);
	
	private native void SetParams(String deviceId, String userId);
//	private native void OnMic();
//	private native void OnWeiboResult(int err);
//	
//	private native void OnBack();
//	private native void OnSelect(int index);
//	
//	public void onBack() {
//		OnBack();
//	}
//	
//	public void onSelect(int index) {
//		OnSelect(index);
//	}
//	
	
	public boolean initialize(VrContextNat ctx) {
		int result = Initialize();
		PLog.d(TAG, "jniVoiceRecog initialize result: " + result);
		if (0 == result) {
			ctx.getEngine().setListener(ctx);
			mVrContext = ctx;
			inited = true;
		}
		return (0 == result);
	}
	
	public void uninitialize() {
		Deinitialize();
	}
	
	public void setParams(String deviceId, String userId) {
		SetParams(deviceId, userId);
	}
	
	public void handleVrResult(int reqID, String content, int confident, int errCode) {
		this.OnVrResult(reqID, content, confident, errCode);
	}
	
	public static jniVoiceRecog instance() {
		if (null != sInstance) {
			return sInstance;
		}
		
		synchronized(jniVoiceRecog.class) {
			if (null == sInstance){
				sInstance = new jniVoiceRecog();
			}
			return sInstance;
		}
	}
//	
//	public void onMic() {
//		if (!inited) {
//			PLog.e(TAG, "jniVoiceRecog onMic not be initialized!");
//			return;
//		}
//		OnMic();
//	}
//	
//	public void onWeiboResult(int err) {
//		if (!inited) {
//			PLog.e(TAG, "jniVoiceRecog onWeiboResult not be initialized!");
//			return;
//		}
//		OnWeiboResult(err);
//	}
	
	public static void upVolume(int type, int val) {
		VoiceRecogTools.instance().upVolume(type, val);
	}
	
	public static void downVolume(int type, int val) {
		VoiceRecogTools.instance().downVolume(type, val);
	}
	
	public static void setMute(int type, boolean mute) {
		VoiceRecogTools.instance().setMute(type, mute);
	}
	
	
	public static void uploadContacts() {
		//FIXME:
		//VoiceRecogTools.instance().uploadContacts();
	}
	
	public static void callPhone(String name, String phoneNo) {
		VoiceRecogTools.instance().callPhone(name, phoneNo);
	}
	
	public static void sendSMS(String name, String phoneNo, String content) {
		VoiceRecogTools.instance().sendSMS(name, phoneNo, content);
	}
	
	// callback from native
	private static boolean OnRequestVr(int reqId, int type, String grammerID, String audioFile) {
		PLog.d(TAG, "jniVoiceRecogIF OnRequestVr reqId: " + reqId + " type: " + type);
		jniVoiceRecog jvr = jniVoiceRecog.instance();
		if (null == jvr || null == jvr.mVrContext) {
			PLog.e(TAG, "jniVoiceRecog OnRequestVr jvr is NULL!");
			return false;
		}
		
		int engMode = (VRTYPE_CMD == type && (null != grammerID && grammerID.length()>0)) 
			? VrEngine.MODE_COMMAND : VrEngine.MODE_GENERAL;
		
		if (VrEngine.MODE_COMMAND == engMode) {
			jvr.mVrContext.setEngineParam(grammerID);
		}
		return jvr.mVrContext.startSpeech(reqId, engMode, 4000, audioFile);
	}
//	
//	private static void OnUpdateUI(int p1, int p2, String strCnt, Object[] poiInfo) {
//		PLog.d(TAG, "jniVoiceRecog OnUpdateUI p1: " + p1 + " p2: " + p2 + " strCnt: " + strCnt);
//		jniVoiceRecog jvr = jniVoiceRecog.instance();
//		if (null == jvr || null == jvr.mVrContext) {
//			PLog.e(TAG, "jniVoiceRecog OnUpdateUI jvr is NULL!");
//			return;
//		}
//		jvr.mVrContext.notifyUI(p1, p2, strCnt, poiInfo);
//	}
	
	private static boolean OnCancel(int type) {
		PLog.d(TAG, "jniVoiceRecog OnCancel type: " + type);
		jniVoiceRecog jvr = jniVoiceRecog.instance();
		if (null == jvr) {
			PLog.e(TAG, "jniVoiceRecog OnCancel jvr is NULL!");
			return false;
		}
		return jvr.mVrContext.cancel(type);
	}
//	
//	private static boolean OnUploadWeibo(int reqId, String content, String image) {
//		PLog.d(TAG, "jniVoiceRecog OnUploadWeibo reqId: " + reqId + ",content:" + content + ",image:" + image);
//		jniVoiceRecog jvr = jniVoiceRecog.instance();
//		if (null == jvr || null == jvr.mVrContext) {
//			PLog.e(TAG, "jniVoiceRecog OnUploadWeibo jvr is NULL!");
//			return false;
//		}
//		return jvr.mVrContext.uploadWeibo(content, image);
//	}
//	
//	private static boolean OnIsBindWeibo() {
//		PLog.d(TAG, "jniVoiceRecog OnIsBindWeibo");
//		jniVoiceRecog jvr = jniVoiceRecog.instance();
//		if (null == jvr || null == jvr.mVrContext) {
//			PLog.e(TAG, "jniVoiceRecog OnIsBindWeibo jvr is NULL!");
//			return false;
//		}
//		
//		return jvr.mVrContext.isBindWeibo();
//	}
}
