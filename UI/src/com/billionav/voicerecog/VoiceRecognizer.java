package com.billionav.voicerecog;

//import com.billionav.navi.cn.R;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.W3JNI;
import com.billionav.jni.jniVoicePlayIF;
import com.billionav.jni.jniVoiceRecog;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PSyncRequest;
import com.billionav.navi.net.PostData;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uicommon.UIC_WeiboCommon;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;

public class VoiceRecognizer implements TriggerListener {
	private static final String TAG = "VR";
	// Voice prompt playback start
	public static final int NOTIFY_PROMPT_PLAY = 0;

	// Voice user playback start
	public static final int NOTIFY_VR_PLAY = 1;
	
	// Voice prompt command
	public static final int NOTIFY_COMMAND_PROMPT_HINT = 2;
	
	// Voice recognition start
	public static final int NOTIFY_RECOG_START = 3;
	// Voice recognition start
	public static final int NOTIFY_RECOG_SPEECHDONE = 4;
	// Voice recognition end
	public static final int NOTIFY_RECOG_END = 5;
	
	// Swing hand
	public static final int NOTIFY_SWING_HAND = 8;
	
	// Take picture to publish
	public static final int NOTIFY_PICTURE_READY = 9;
	
	// Search result
	public static final int NOTIFY_SEARCH_RESULT = 10;
	
	// SNS messages arrive
	public static final int NOTIFY_SNS_MSG_ARRIVE = 11;
	// SNS messages playback start
	public static final int NOTIFY_SNS_MSG_START = 12;
	// All SNS messges playback complete
	public static final int NOTIFY_SNS_MSG_END = 13;
	// search around logomark
	public static final int NOTIFY_SEARCH_AROUND_LOGOMARK = 14;
	
	private static final int MSG_START_RECOGNITION = 1;
	
	// Singletone
	private static VoiceRecognizer sInstance = null;
	private jniVoiceRecog mJniVoiceRecog;
	
	private VrContextNat mVrContext;
	private VoiceRecogTools mVrTool;
	private String mDeviceInfo;
//	private boolean useServer = false;
	private RecognitionListener recogListener;
//	private TimerHandler timerHandler;
	
	public interface RecognitionListener {
		void onVrResults(List<String> results);
	}
	
//	class TimerHandler extends Handler {
//		public void handleMessage(Message msg) {
//			switch(msg.what) {
//			case MSG_START_RECOGNITION:
//				mVrContext.startSpeech();
//				break;
//			default:
//				break;
//			}
//		}
//	};
	
	// Singletone and consturtor
	public static VoiceRecognizer instance() {
		if (null != sInstance) {
			return sInstance;
		}

		synchronized(VoiceRecognizer.class) {
			if (null == sInstance) {
				sInstance = new VoiceRecognizer();
			}
		}
		VrLog.d(TAG, "VoiceRecognizer instance=" + sInstance);
		return sInstance;
	}	
	
	/**
	 * UI should call when application exit
	 * @return none
	 * @attention it will be suspended if UI don't call this method when app exit
	 */
	public void release() {
		GlobalTrigger.getInstance().removeTriggerListener(this);
	}
	
	private VoiceRecognizer() {
	}
		
	/**
	 * UI should call when new activity be created and old activity be destroyed
	 * @param ctx current activity context
	 * @return true if context setting is success
	 * @attention if it's not necessary to call it if old activity not be destroyed
	 */
	public boolean setContext(Context ctx) {
		//VrLog.i(TAG, "setContext, thread name=" + Thread.currentThread().getName());
		if (null == ctx) {
			VrLog.e(TAG, "setContext ctx is null");
			return false;
		}
		
//		timerHandler = new TimerHandler();
		mJniVoiceRecog = jniVoiceRecog.instance();
		
		// Set device,user IDs
//		UserControl_UserInfo userInfo = UIC_WeiboCommon.getUserInfo();
//		mJniVoiceRecog.setParams(tm.getDeviceId()+":"+SystemTools.getVersionString(), (null != userInfo) ? userInfo.m_strUserId : "");
		String uuid = SystemInfo.GetSysUUID();
		mDeviceInfo = uuid + ":" + SystemTools.getVersionString();
		mJniVoiceRecog.setParams(mDeviceInfo, "");
		
		GlobalTrigger.getInstance().addTriggerListener(this);
		mVrTool = VoiceRecogTools.instance();
		mVrTool.init(ctx);
	
		
		mVrContext = new VrContextNat();
		VrEngine eng = CreateVrEngine(ctx, "keda");
		mVrContext.setEngine(eng);
//		mVrContext.setListener(listener);
		return mJniVoiceRecog.initialize(mVrContext);
	}
	
	private VrEngine CreateVrEngine(Context ctx, String engType) {
		VrEngine engine = VrEngineFactory.getVrEngine(engType);
		if (null == engine || !engine.setContext(ctx)) {
			VrLog.e(TAG, "CreateVrEngine engine.setContext error: " + engType);
			return null;
		}
		return engine;
	}
	
	private void uploadContactsAsync(String uuid) {
		UploadContactTask task = new UploadContactTask();
		String dialogUrl = W3JNI.getConfigValue("VrServerHost");
	    task.execute(new String[] {dialogUrl + "/data/contacts/md5", dialogUrl + "/data/contacts", uuid});
	}

	/**
	 * Enter Voice Recognition mode
	 * 
	 * @return true if enter VR mode successfully
	 * @attention UI must call this function when user request to switch VR mode
	 */
	public boolean enterVrMode() {
//		if (null == mJniVoiceRecog) {
//			VrLog.e(TAG, "VoiceRecog jni not be initialized!");
//			return false;
//		}
//		mJniVoiceRecog.onMic();
		return true;
	}
	
	/**
	 * Exit Voice Recognition mode
	 * @return none
	 * @attention it's a asynchronous call,onVrTerminated method will be called if operation complete
	 */
	public void exitVrMode() {
//		if (null == mJniVoiceRecog) {
//			VrLog.e(TAG, "VoiceRecog jni not be initialized!");
//			return;
//		}
//		VrLog.i(TAG, "exit VR mode!");
//		mJniVoiceRecog.onBack();
	}
	
	/**
	 * One shot call to acquire voice recognition result.
	 * @param listener recognition result listener
	 * @return true if start recognition success
	 * @attention intent to use by input method
	 */
	public boolean startRecongnition(String prompt, RecognitionListener listener) {
		this.recogListener = listener;
//		return jniVoicePlayIF.Instance().PlayTts(prompt, this);
		return true;
	}
	
	/**
	 * One shot call to cancel the voice recognition.
	 * @attention intent to use when cancel input method
	 */
	public void cancelRecongnition() {
	}
	
	public void loginLater() {
	//	uploadContactsAsync(mDeviceInfo);
	}
	
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
//		if (triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN) {
//			if (UserControl_ManagerIF.Instance().HasLogin()) {
//				VrLog.i(TAG, "login done");
//				uploadContactsAsync(mDeviceInfo);
//			}
//		}
		return false;
	}

	 private class UploadContactTask extends AsyncTask<String, Void, Boolean> {
		    @Override
		    protected Boolean doInBackground(String... urls) {
		    	String uuid = urls[2];
		    	VrLog.i(TAG, "http upload uuid=" + uuid);
		    	
		      	PSyncRequest req = new PSyncRequest();
				VrLog.i(TAG, "Http get url=" + urls[0]);
				req.setAuthFlag(true);
				req.addHttpHeader("UUID", uuid);
				req.setURL(urls[0]);
				req.setMethod(PRequest.METHODS_GET);
				req.setResDataType(PRequest.RESPONSE_DATA_BUF);
				
				req.sendRequest();
				req.waitResponse();
				if (200 == req.getResCode()) {
					return upload(urls[1], new String(req.getReceivebuf()), uuid);
				} else {
					VrLog.e(TAG, "Get records md5 failed");
				}
				return false;
		    }

		    @Override
		    protected void onPostExecute(Boolean result) {
		    	
		    }
		    
		    private Boolean upload(String url, String lastMD5, String uuid) {
		    	String contacts = mVrTool.getContacts().toString();
		    	if (null == contacts || 0 == contacts.length()) {
		    		return  false;
		    	}
		    	
		    	String md5 = VoiceRecogTools.md5(contacts);
		    	VrLog.i(TAG,"last md5:" + lastMD5 + ", md5:" + md5);
		    	if (!md5.equals(lastMD5)) {
			      	PSyncRequest req = new PSyncRequest();
					VrLog.i(TAG, "Http post md5 url=" + url);
					req.setAuthFlag(true);
					req.setURL(url);
					req.setMethod(PRequest.METHODS_POST);
					req.addHttpHeader("UUID", uuid);
					req.setResDataType(PRequest.RESPONSE_DATA_BUF);
					
					PostData data = new PostData();
					data.setPostData(mVrTool.getContacts().append( ", \"md5\":" + md5 + "}").toString().getBytes());
					req.setPostData(data);
					
					req.sendRequest();
					req.waitResponse();
					if (200 != req.getResCode()) {
						VrLog.e(TAG, "Upload records failed, errCode=" + req.getResCode());
						return false;
					}
		    	}
		    	return true;
		    }
		  }
	
//	public void OnPlayEnd(int res) {
//		Message msg = Message.obtain(timerHandler, MSG_START_RECOGNITION);
//		timerHandler.sendMessageDelayed(msg, 200);
//	}
	
//	public void handleResult(String text) {
//		if (null != recogListener) {
//			List<String> results = null;
//			if (null != text) {
//				results = new ArrayList<String>(1);
//				results.add(text);
//			}
//			recogListener.onVrResults(results);
//		}
//	}
	
//	public boolean isUseServer() {
//		return useServer;
//	}
	
//	private String getAppLang(Context ctx) {
//		//	return ctx.getResources().getString(R.string.vr_language)
//		switch(UIMapControlJNI.GetModelInfo()) {
//		case 0:
//			return "zh";
//		case 2:
//			return "ja";
//		default:
//			return "en";
//		}
//	}
}
