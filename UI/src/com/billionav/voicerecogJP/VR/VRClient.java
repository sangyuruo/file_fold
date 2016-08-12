package com.billionav.voicerecogJP.VR;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

import com.billionav.navi.app.AndroidNaviAPP;
import com.billionav.voicerecogJP.VRIflytek;
import com.billionav.voicerecogJP.VRIflytek.IFlytecListener;

public class VRClient{
	public static final int VR_REQUEST_CODE			=0x8000;
	
	public static final int ENG_TYPE_GOOGLE 		= 0;
	public static final int ENG_TYPE_IFLY 			= 1;
	public static final int ENG_TYPE_LOCAL_TEST 	= 2;
	
	public static final int ERR_SR_NETWORK_OFF		= 1;
	public static final int ERR_SR_NETWORK_TIMEOUT 	= 2;
	public static final int ERR_SR_SEPECH_TIMEOUT 	= 3;
	public static final int ERR_SR_NOMATCH 			= 4;
	public static final int ERR_SR_SERVICE 			= 5;
	public static final int ERR_SR_CANCEL 			= 6;
	public static final int ERR_SR_INVOP 			= 7;
	public static final int ERR_VRPKG_INVALID 		= 8; 
	
	private static final int STATE_IDLE 			= 0;
	private static final int STATE_RECOG 			= 1;
	private static final int STATE_REQUEST 			= 2;
	
	public static final int VRC_RECOGNIZE_READY = 200;
	public static final int VRC_NO_ENGINE =401;
	public static final int VRC_ENGINE_OPEN_FAILED = 402;
	public static final int VRC_WRONG_STATE = 403;
	
	private VrListener listener;
	
	private int curEngineType;
	private int state;
	private boolean cancel;
	
	public VRClient() {
		resetState();
	}
	
	public int startRecognition(Activity activity, VrListener listener) {
		if (STATE_IDLE != state) {
			return VRC_WRONG_STATE;
		}
		this.listener = listener;
		cancel = false;

		if (AndroidNaviAPP.getInstance().getLanguageType() == AndroidNaviAPP.LANGUAGE_CH) {
			curEngineType = ENG_TYPE_IFLY;
		} else {
			curEngineType = ENG_TYPE_GOOGLE;
		}

		boolean ok;
		if (ENG_TYPE_IFLY == curEngineType) {
			ok = startiFly();
			if (!ok) {
				return VRC_ENGINE_OPEN_FAILED;
			}
			
			if(null != listener){
				listener.onStart(ENG_TYPE_IFLY);
			}
			
		} else {
			ok = startGoogle(activity);
			if (!ok) {
				return VRC_NO_ENGINE;
			}
		}

		state = STATE_RECOG;
		return VRC_RECOGNIZE_READY;
	}
	
	public void stopRecognition() {
		switch (state) {
		case STATE_RECOG:
			if(curEngineType == ENG_TYPE_IFLY){
				VRIflytek.sharedInstance().stopRecognize();
			}
			cancel = true;
			break;
		case STATE_REQUEST:
			IntentionClient.instance().cancel();
			break;
		default:
			break;
		}
		resetState();
	}
	
	private void resetState() {
		state = STATE_IDLE;
	}
	
	public void notifyResult(int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode) {
			if (null != listener) {
				listener.onError(ERR_SR_NETWORK_TIMEOUT, curEngineType);
			}
			resetState();
			return;
		}

		ArrayList<String> results = null;
		if (ENG_TYPE_IFLY == curEngineType) {
			// result = parseFromGoogle(data);
		} else {
			results = parseGoogle(data);
		}

		parseResultList(results);
	}
//	private RecognitionListener iFlyListener = new RecognitionListener() {
//		public void onVrResults(final List<String> results) {
//			parseResultList(results);
//		}
//	};

	private boolean startiFly() {

		return VRIflytek.sharedInstance().startIflytecRecognize(
				new IFlytecListener() {
					public void onResult(List<String> results) {
						parseResultList(results);
					}

					public void onError() {
						listener.onError(ERR_SR_NOMATCH, curEngineType);
					}

					public void onEnd() {

					}

					public void onCancel() {
						listener.onError(ERR_SR_SEPECH_TIMEOUT,curEngineType);
					}
				});
	}
	
	private boolean startGoogle(Activity activity) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		try {
			activity.startActivityForResult(intent, VR_REQUEST_CODE);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private void parseResultList(List<String> list){
		
		if(null == listener){
			resetState();
			return;
		}
		
		boolean isResultOK = false;
		String result = "";
		if (null != list && list.size() > 0) {
			result = list.get(0);
			if (!"".equals(result) && !cancel) {
				isResultOK = true;
			} 
		}
		
		if(isResultOK && null != listener){
			state = STATE_REQUEST;
			IntentionClient.instance().request(result);
			listener.onResults(list, curEngineType);
		}else{
			listener.onError(ERR_SR_NOMATCH, curEngineType);
			resetState();
		}
	}
	
	private ArrayList<String> parseGoogle(Intent intent) {
		ArrayList<String> items = intent.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS);
		if (items.size() > 0) {
		}
		return items;
	}
	
}
