package com.billionav.voicerecog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.billionav.jni.jniVoiceRecog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.uicommon.UIC_WeiboCommon;
import com.billionav.voicerecog.VoiceRecognizer;
import com.billionav.voicerecog.VrListener;
import com.billionav.voicerecog.VrLog;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.WeiboException;


public class VrContextNat implements VrEngineListener{
	private static final String TAG = "VR";
	enum State {
	    BUSY, IDLE
	};
	
	private State current = State.IDLE;
	
//	private static final int TRIGGER_TYPE_STARTVR = 0;
//	private static final int TRIGGER_TYPE_ADDVIEW = 1;
//	private static final int TRIGGER_TYPE_SHOWTEXT = 2;
//	private static final int TRIGGER_TYPE_SHOWIMAGE = 3;
//	private static final int TRIGGER_TYPE_SHOWPOILIST = 4;
//	private static final int TRIGGER_TYPE_HIDEPOILIST = 5;
//	private static final int TRIGGER_TYPE_SETDESTINATION = 6;
//	private static final int TRIGGER_TYPE_CHANGEVRSTATE = 7;
//	private static final int TRIGGER_TYPE_GOHOME = 8;
//	private static final int TRIGGER_TYPE_GOCORP = 9;
//	private static final int TRIGGER_TYPE_ENTER = 10;
//	private static final int TRIGGER_TYPE_EXIT = 11;
//	private static final int TRIGGER_TYPE_SHOWCONCATLIST = 12;
//	private static final int TRIGGER_TYPE_HIDECONCATLIST = 13;
//	private static final int TRIGGER_TYPE_SHOWLOGOMARK= 14;
    
//    private static final int UI_TEXT_TYPE_PROMPT = 0;
//    private static final int UI_TEXT_TYPE_VR = 1;
//    private static final int UI_TEXT_TYPE_PROMPT_HINT = 2;

//    private static final int VRSTATE_IDLE = 0;	// gray
//    private static final int VRSTATE_SPEECH = 1; // twinkling(red)
//    private static final int VRSTATE_BUSY = 2;	// swirling(red)
	
    private static final int RECOGNITION_ONCE = -2;
//	private VrListener mListener;
	private VrEngine engine;
	private int mReqId = -1;
	private boolean mTried = false;
	
	public VrContextNat() {
	}
	
	public void setEngine(VrEngine engine) {
		this.engine = engine;
		this.engine.setListener(this);
	}
	
	public VrEngine getEngine() {
		return this.engine;
	}
	
//	public void setListener(VrListener listener) {
//		mListener = listener;
//	}
//	
//	public VrListener getListener() {
//		return mListener;
//	}
	private boolean changeState(State from, State to){
		synchronized(current){
			if (current != from && !mTried) {
				mTried = true;
				return false;
			}
			
			mTried = false;
			current = to;
			return true;
		}
	}
	public boolean startSpeech() {
		if (null == engine) return false;
		if(! changeState(State.IDLE, State.BUSY)){
			VrLog.e(TAG, "VrContextNat startSpeech current=" + current);
			return false;
		}
		mReqId = RECOGNITION_ONCE;
		return engine.startSpeech(VrEngine.MODE_GENERAL, 5000, null);
	}
	
	public boolean startSpeech(int reqId, int cmdMode, int timeout, String output) {
		if (null == engine) return false;
		if(! changeState(State.IDLE, State.BUSY)){
			VrLog.e(TAG, "VrContextNat startSpeech current=" + current);
			return false;
		}
		mReqId = reqId;
		return engine.startSpeech(cmdMode, timeout, output);
	}
	
	public boolean cancel(int type) {
		VrLog.i(TAG,  "VrContextNat cancel type=" + type);
		if (null != engine) {
			synchronized(current){
				engine.stopSpeech();
				current = State.IDLE;
			}
			return true;
		}
		return false;
	}
//	
//	public boolean isBindWeibo() {
//		return (null != UIC_WeiboCommon.getUserInfo());
//	}
//	
//	public boolean uploadWeibo(String content, String image) {
//		return UIC_WeiboCommon.publishWeibo(image, content, this);
//	}
	
	public void setEngineParam(String grammarId) {
		if (null == engine) return ;
		engine.setParam(-1, grammarId);
	}
	
//	private void sendTrigger(long param1, long param2, String text) {
//		NSTriggerInfo info = new NSTriggerInfo();
//	    info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_VOICE_RECOGNITION; 
//	    info.m_lParam1 = param1;
//	    info.m_lParam2 = param2;
//	    info.m_String1 = text;
//	    
//	    MenuControlIF.Instance().TriggerForScreen(info);
//	    VrLog.d(TAG, "Send trigger id=" + info.m_iTriggerID + ", param1=" + param1 
//	    		+ ", param2=" + param2);
//	}
//	
//	private void onShowText(long type, String text) {
//		if (UI_TEXT_TYPE_PROMPT == type) {
//			sendTrigger(VoiceRecognizer.NOTIFY_PROMPT_PLAY, 0, text);
//		}
//		else if (UI_TEXT_TYPE_VR == type) {
//			sendTrigger(VoiceRecognizer.NOTIFY_RECOG_END, 0, text);
//		}
//		else if (UI_TEXT_TYPE_PROMPT_HINT == type) {
//			sendTrigger(VoiceRecognizer.NOTIFY_COMMAND_PROMPT_HINT, 0, text);
//		}
//		else {
//			VrLog.e(TAG, "VrContext onShowText type error: " + type + " " + text);
//		}
//	}
//
//	private void onPoiResults(Object[] objArray) {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onPoiResults mListener is NULL");
//			return;
//		}
//		List<POIInfo> results = new ArrayList<POIInfo>();
//		for (int i = 0; i < objArray.length; ++i) {
//			results.add((POIInfo)objArray[i]);
//		}
//		mListener.onPOIResults(results);
//	}
//	
//	private void onHidePoiList() {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onPoiResults mListener is NULL");
//			return;
//		}
//		mListener.onVrSwitchUI(false);
//	}
//	
//	private void onDestination(Object[] objArray) {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onDestination mListener is NULL");
//			return;
//		}
//		
//		if (objArray.length < 1) {
//			VrLog.e(TAG, "VrContextNat onDestination target list is less than 1");
//			return;
//		}
//		
//		POIInfo target = (POIInfo)objArray[0];
//		List<POIInfo> wayPoints = null;
//		if (objArray.length > 1) {
//			wayPoints = new ArrayList<POIInfo>(objArray.length-1);
//			for (int i=1; i<objArray.length; ++i) {
//				wayPoints.add((POIInfo)objArray[i]);
//			}
//		}
//		
//		VrLog.i(TAG, "Goto Destination POI, lon=" + target.getLon() + ",lat=" + target.getLat());
//		mListener.onVrGotoTarget(target, wayPoints);
//	}	
//	
//	private void onChangeVrState(int type) {
//		switch (type) {
//		case VRSTATE_IDLE:
//			sendTrigger(VoiceRecognizer.NOTIFY_RECOG_END, 0, null);
//			break;
//		case VRSTATE_SPEECH:
//			sendTrigger(VoiceRecognizer.NOTIFY_RECOG_START, 0, null);
//			break;
//		case VRSTATE_BUSY:
//			sendTrigger(VoiceRecognizer.NOTIFY_RECOG_END, 0, null);
//			break;
//		default:
//			VrLog.e(TAG, "VrContextNat onChangeVrState state error: " + type);
//			break;
//		}
//	}	
//	
//	private void onGoHome() {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onGoHome mListener is NULL");
//			return;
//		}
//		mListener.onVrGotoRegPOI(VrListener.TYPE_HOME);		
//	}
//	
//	private void onGoCompany() {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onGoHome mListener is NULL");
//			return;
//		}
//		mListener.onVrGotoRegPOI(VrListener.TYPE_CORP);		
//	}
//	
//	private void onVrExit() {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onGoHome mListener is NULL");
//			return;
//		}
//		mListener.onVrTerminated(true);
//	}
	
//	private void onContactResult(Object[] objArray) {
//		VrLog.i(TAG, "VrContextNat onContactResult");
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onContactResult mListener is NULL");
//			return;
//		}
//		List<ContactInfo> list = null;
//		if ((null != objArray) && (objArray.length > 0)) {
//			list = new ArrayList<ContactInfo>(objArray.length - 1);
//			for (int i = 0; i < objArray.length; ++i) {
//				list.add((ContactInfo)objArray[i]);
//				VrLog.i(TAG, "VrContextNat onContactResult ContactInfo=" + objArray[i]);
//			}
//		}
//		mListener.onContactResults(list);
//	}
//	
//	private void onHideContactList() {
//		if (null == mListener) {
//			VrLog.e(TAG, "VrContextNat onHideContactList mListener is NULL");
//			return;
//		}
//		mListener.onVrSwitchUI(false);
//	}
//	
//	private void onLogoMark(String name) {
//		//  TODO
//		sendTrigger(VoiceRecognizer.NOTIFY_SEARCH_AROUND_LOGOMARK, 0, name);
//		VrLog.d(TAG, "VrContextNat onLogoMark name="+name);
//	}
//	
//	public void notifyUI(long param1, long param2, String text, Object[] objArray) {
//		
//		switch ((int)param1) {
//		case TRIGGER_TYPE_STARTVR:
//			// do nothing
//			break;
//		case TRIGGER_TYPE_ADDVIEW:
//			// do nothing
//			break;
//		case TRIGGER_TYPE_SHOWTEXT:
//			onShowText(param2, text);
//			break;
//		case TRIGGER_TYPE_SHOWIMAGE:
//			sendTrigger(VoiceRecognizer.NOTIFY_PICTURE_READY, 0, text);
//			break;
//		case TRIGGER_TYPE_SHOWPOILIST:
//			onPoiResults(objArray);
//			break;
//		case TRIGGER_TYPE_HIDEPOILIST:
//			onHidePoiList();
//			break;
//		case TRIGGER_TYPE_SETDESTINATION:
//			onDestination(objArray);
//			break;
//		case TRIGGER_TYPE_CHANGEVRSTATE:
//			onChangeVrState((int)param2);
//			break;
//		case TRIGGER_TYPE_GOHOME:
//			onGoHome();
//			break;
//		case TRIGGER_TYPE_GOCORP:
//			onGoCompany();
//			break;
//		case TRIGGER_TYPE_ENTER:
//			// FixMe
//			break;
//		case TRIGGER_TYPE_EXIT:
//			onVrExit();
//			break;
//		case TRIGGER_TYPE_SHOWCONCATLIST:
//			onContactResult(objArray);
//			break;
//		case TRIGGER_TYPE_HIDECONCATLIST:
//			onHideContactList();
//			break;
//		case TRIGGER_TYPE_SHOWLOGOMARK:
//			onLogoMark(text);
//			break;
//		default:
//			VrLog.e(TAG, "VrContextNat notifyUI param error: " + param1);
//			break;
//		}
//	}
	
	// implement VrEngineListener
	public void onCommandUploaded(String contentID, String externID) {
		VrLog.e(TAG, "VrContextNat onCommandUploaded is called!");
	}
	
	public void onSpeechReady() {
		VrLog.d(TAG, "VrContextNat onSpeechReady !");
	}
	
	public void onSpeechBegin() {
		VrLog.d(TAG, "VrContextNat onSpeechBegin !");	
	}
	
	public void onSpeechEnd() {
		VrLog.d(TAG, "VrContextNat onSpeechEnd !");	
	}
	
	public void onSpeechError(int err) {
		if(! changeState(State.BUSY, State.IDLE)){
			VrLog.e(TAG, "VrContextNat onSpeechError current=" + current);
			return ;
		}
		VrLog.e(TAG, "VrContextNat onSpeechError reqid : " + mReqId + " err: " + err);
		if (RECOGNITION_ONCE == mReqId) {
//			VoiceRecognizer.instance().handleResult(null);
		} else {
			jniVoiceRecog.instance().handleVrResult(mReqId, "", 0, err);
		}
	}
	
	public void onSpeechResult(String result, int confidence) {
		if(! changeState(State.BUSY, State.IDLE)){
			VrLog.e(TAG, "VrContextNat onSpeechResult current=" + current);
			return ;
		}
		VrLog.d(TAG, "VrContextNat onSpeechResult reqid : " + mReqId + " content: " + result + "confidence: " + confidence);
		if (RECOGNITION_ONCE == mReqId) {
//			VoiceRecognizer.instance().handleResult(result);
		} else {
			jniVoiceRecog.instance().handleVrResult(mReqId, result, confidence, 0);
		}
	}
	
	// for Weibo listener
//	@Override
//    public void onComplete(String response) {
//		VrLog.i(TAG, "VrContextNat onComplete, response=" + response);
//		jniVoiceRecog.instance().onWeiboResult(jniVoiceRecog.ERR_NONE);
//    }
//
//	@Override
//    public void onIOException(IOException e) {
//		VrLog.e(TAG, "VrContextNat onIOException, err=" + e.getMessage());
//		jniVoiceRecog.instance().onWeiboResult(jniVoiceRecog.ERR_NETWORK);
//    }
//
//	@Override
//    public void onError(WeiboException e) {
//		VrLog.e(TAG, "VrContextNat onError, err=" + e.getMessage());
//		jniVoiceRecog.instance().onWeiboResult(jniVoiceRecog.ERR_WEIBO);
//    }
}
