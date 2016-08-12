package com.billionav.jni;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.billionav.navi.system.PLog;

/**
 * It's voice play IF interface
 *
 */
public class jniVoicePlayIF implements jniVoicePlayerManager.CompleteListener {
	private static final String TAG = "VP";
	
	public static final int BLUETOOTH_OFF = 0;
	public static final int BLUETOOTH_TURNING_OFF = 1;
	public static final int BLUETOOTH_ON = 2;
	public static final int BLUETOOTH_NOISY = 3;
	public static final int BLUETOOTH_CONNECTED = 4;
	
	// Sync with VP_Define.h
	
	// languages
	public static final int VP_LANGUAGE_CHN_MANDARIN = 0;
	public static final int VP_LANGUAGE_CHN_ENGLISH = 1;
	public static final int VP_LANGUAGE_JPN_JAPANESE = 2;
	public static final int VP_LANGUAGE_OS_US_ENGLISH = 3;
	public static final int VP_LANGUAGE_OS_UK_ENGLISH = 4;
	public static final int VP_LANGUAGE_OS_GERMAN = 5;
	public static final int VP_LANGUAGE_OS_ITALIAN = 6;
	public static final int VP_LANGUAGE_OS_FRENCH = 7;
	public static final int VP_LANGUAGE_OS_SPANISH = 8;
	public static final int VP_LANGUAGE_OS_PORTUGUESE = 9;
	public static final int VP_LANGUAGE_OS_DUTCH = 10;
	public static final int VP_LANGUAGE_OS_FLEMISH =11;
	public static final int VP_LANGUAGE_OS_SWEDISH =12;
	public static final int VP_LANGUAGE_OS_DANISH =13;
	public static final int VP_LANGUAGE_OS_NORWEGIAN = 14;
	public static final int VP_LANGUAGE_OS_RUSSIAN = 15;
	public static final int VP_LANGUAGE_AS_MALAY = 16;
	public static final int VP_LANGUAGE_AS_INDONESIAN =17;
	public static final int VP_LANGUAGE_AS_ENGLISH = 18;
    
	// VP maxium volume
	private static final int PLAYER_VOLUME_MAX = 32; 
	// VP Prompt priority
	public static final int VP_PRIORTY_VR = 1;
	public static final int VP_PRIORTY_UI = 10;
	// VP request task owner
	public static final int VP_TASKID_VR = 0;
	public static final int VP_TASKID_UI = 6;
	
	// beep IDs
	public static final int BEEP_ID_TONE1 = 12;
	public static final int BEEP_ID_TONE2 = 13;
	public static final int BEEP_ID_TONE_VR = 15;

	// regions
	public static final int VP_REGION_CHN_BEIJINGSHI = 0;
	public static final int VP_REGION_CHN_TIANJINSHI = 1;
	public static final int VP_REGION_CHN_HEBEISHENG = 2;
	public static final int VP_REGION_CHN_SHANXISHENG = 3;
	public static final int VP_REGION_CHN_NEIMENGGUZIZHIQU = 4;
	public static final int VP_REGION_CHN_LIAONINGSHENG = 5;
	public static final int VP_REGION_CHN_JILINSHENG = 6;
	public static final int VP_REGION_CHN_HEILONGJIANGSHENG = 7;
	public static final int VP_REGION_CHN_SHANGHAISHI = 8;
	public static final int VP_REGION_CHN_JIANGSUSHENG = 9;
	public static final int VP_REGION_CHN_ZHEJIANGSHENG = 10;
	public static final int VP_REGION_CHN_ANHUISHENG = 11;
	public static final int VP_REGION_CHN_FUJIANSHENG = 12;
	public static final int VP_REGION_CHN_JIANGXISHENG = 13;
	public static final int VP_REGION_CHN_SHANDONGSHENG = 14;
	public static final int VP_REGION_CHN_HENANSHENG = 15;
	public static final int VP_REGION_CHN_HUBEISHENG = 16;
	public static final int VP_REGION_CHN_HUNANSHENG = 17;
	public static final int VP_REGION_CHN_GUANGDONGSHENG = 18;
	public static final int VP_REGION_CHN_GUANGXIZHUANGZUZIZHIQU = 19;
	public static final int VP_REGION_CHN_HAINANSHENG = 20;
	public static final int VP_REGION_CHN_CHONGQINGSHI = 21;
	public static final int VP_REGION_CHN_SICHUANSHENG = 22;
	public static final int VP_REGION_CHN_GUIZHOUSHENG = 23;
	public static final int VP_REGION_CHN_YUNNANSHENG = 24;
	public static final int VP_REGION_CHN_XIZANGZIZHIQU = 25;
	public static final int VP_REGION_CHN_SHAANXISHENG = 26;
	public static final int VP_REGION_CHN_GANSUSHENG = 27;
	public static final int VP_REGION_CHN_QINGHAISHENG = 28;
	public static final int VP_REGION_CHN_NINGXIAHUIZUZIZHIQU = 29;
	public static final int VP_REGION_CHN_XINJIANGWEIWUERZIZHIQU = 30;
	
	// JP phrase definition
	public static final int VL_PL_DIRGUIDE_010A = 0x00010A;
	public static final int VL_PL_DIRGUIDE_010B = 0x00010B;
	public static final int VL_PL_DIRGUIDE_00F4 = 0x0000F4;
	public static final int VL_PL_DIRGUIDE_00F5 = 0x0000F5;
	
	// Message definitions
	static final int MSG_PLAY_FILE = 1;
	static final int MSG_PLAY_TTS = 2;
	static final int MSG_PLAY_BUF = 3;
	static final int MSG_PLAY_PCM = 4;
	static final int MSG_STOP = 5;
	static final int MSG_PLAY_END = 6;
	static final int MSG_AUDIO_SESSION = 7;
	static final int MSG_AUDIO_OPEN = 8;
	static final int MSG_SET_VOLUME = 9;
	static final int MSG_GET_VOLUME = 10;

	class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
		public void onAudioFocusChange(int focusChange) {
			PLog.i(TAG, "onAudioFocusChange, status=" + focusChange);
		}
	}
	
	public interface PlaybackListener {
		void OnPlayEnd(int res);
	};
	
	public class TelStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			PLog.i(TAG, "telphone state=" + state);
			mTelState = state;
		}
	}
	
	public static jniVoicePlayIF Instance() {
		if (null != s_instance) {
			return s_instance;
		}
		
		synchronized(jniVoicePlayIF.class) {
			if (null == s_instance) {
				s_instance = new jniVoicePlayIF();
			}
		}
		return s_instance;
	}
	
	// should use playBeep
	@Deprecated
	public boolean PlayBeep(int beepId) {
		return playBeep(beepId);
	}
	
	// play beep tone
	public boolean playBeep(int beepId) {
		return native_playBeep(beepId);
	}
	
	private native int native_initialize(Object weak_me,String conf);
	private native void native_setCallbackBuffer(int playerId, byte[] buffer);
	
	private native boolean native_playBeep(int beepId);
	// play road name by road name string and region,  not-implemented now
	private native boolean native_playRoadName(String roadName, int region);
	
	private native boolean native_playGuide(int guideId, int taskid, int priority, int reqid);
	private native boolean native_playTts(String tts, int taskId, int priority, int reqid);
	private native void native_stopPlay();
	private native boolean native_setVolume(int volume);
	private native boolean native_setVrMode(boolean solo);
	private native void native_setBackground(boolean bBackground);
	private native boolean native_changeLanguage(int lang);
	private native void native_complete(int playerId, boolean success);
	private native void native_uninitialize();
	
	public boolean initialize(Context ctx) {
		PLog.i(TAG, "jniVoicePlayIF Initialize ...");
		if (-1 != mInitResult) {
			PLog.e(TAG, "Repeat call Initialize method!");
			return false;
		}
		
		if (null != ctx) {
			TelephonyManager telMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			telMgr.listen(new TelStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
			mAudioManager = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
		}
		
		mAuidoFocusListener = new AudioFocusListener();
		
		// Initialize VP JNI 
		mInitResult = native_initialize(new WeakReference<jniVoicePlayIF>(this), "");
		PLog.i(TAG, "Initialize, res code=" + mInitResult);
		return true;
	}
	
	/*public boolean setContext(Context ctx) {
		PLog.i(TAG, "setContext ...");
		if (1 != mInitResult || Build.VERSION.SDK_INT <= 9) return false;
		return mPlayerMgr.setContext(ctx);
	}*/
	
	// Should use volumeKeyEvent
	@Deprecated
	public boolean VolumeKeyEvent(boolean up) {
		return volumeKeyEvent(up);
	}
	
	// Return true if VP has consumed this event, 
	// so user must return from it's dispatchXXXEvent function.
	public boolean volumeKeyEvent(boolean up) {
		PLog.d(TAG, "Volume Key Event: " + up);
		if (null == mAudioManager) {
			PLog.e(TAG, "Volume Key Event: AudioManager is null " + up);
			return false;
		}
		
		int streamtype = AudioManager.STREAM_MUSIC;
		int direction = up ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
		// int flag = up ? AudioManager.FX_FOCUS_NAVIGATION_UP : AudioManager.FX_FOCUS_NAVIGATION_DOWN;
		int flag = AudioManager.FLAG_SHOW_UI;
		mAudioManager.adjustStreamVolume(streamtype, direction, flag);
		return true;
	}
	
	// Should use bluetoothChange
	@Deprecated
	public void BluetoothChange(int state) {
		bluetoothChange(state);
	}
	public void bluetoothChange(int state) {
		//TODO:
	}
	
	public void handleResume() {
		//TODO:
	}
	
	public void handlePause() {
		//TODO:
	}
	
	public void uninitialize() {
		PLog.i(TAG, "Uninitialize ...");
		native_uninitialize();
	}
	
	public synchronized boolean setVolume(float volume) {
		if (volume < 0.0 || volume > 1.0) {
			return false;
		}
		int nVolume = (int)volume*PLAYER_VOLUME_MAX;
		return native_setVolume(nVolume);
	}
	
	public synchronized boolean PlayTts(String tts, PlaybackListener listener) {
		CallbackData item = new CallbackData(++mReqId, listener);
		mCallbacks.add(item);
		boolean ok = native_playTts(tts, VP_TASKID_VR, VP_PRIORTY_VR, mReqId);
		if (!ok) {
			mCallbacks.remove(item);
		}
		return ok;
	}
	
	// Play guide sound by phrase id with default priority
	public boolean playGuide(int id) {
		return playGuide(id, VP_TASKID_UI, 0, null);
	}
	
	// Should use playGuide
	@Deprecated
	public boolean PlayGuide(int id) {
		return playGuide(id);
	}
	
	public synchronized boolean playGuide(int id, int taskid, int priority, PlaybackListener listener) {
		int reqId = 0;
		CallbackData item = null;
		if (null != listener) {
			reqId = ++mReqId;
			item = new CallbackData(reqId, listener);
			mCallbacks.add(item);
		}
		
		boolean ok = native_playGuide(id, taskid, priority, reqId);
		if (!ok && null != item) {
			mCallbacks.remove(item);
		}
		return ok;
	}
	
	public synchronized void stopPlay() {
		PLog.d(TAG, "jniVoicePlayI stopPlay");
		native_stopPlay();
	}
	
	public synchronized boolean changeLanguage(int lang) {
		PLog.i(TAG, "jniVoicePlayIF changeLanguage lang: " + lang);
		return native_changeLanguage(lang);
	}

	public synchronized boolean setVrMode(boolean vrMode) {
		PLog.i(TAG, "jniVoicePlayIF setVrMode vrMode: " + vrMode);
		boolean ok = native_setVrMode(vrMode);
		if (!ok) {
			PLog.i(TAG, "jniVoicePlayIF native_setVrMode failed, vrMode: " + vrMode);
			return false;
		}
		
		if (vrMode) {
			mAudioManager.requestAudioFocus(mAuidoFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		} else {
			mAudioManager.abandonAudioFocus(mAuidoFocusListener);
		}
		mVrMode = vrMode;
		return true;
	}
	

	public synchronized void setBackground(boolean bBackground) {
		PLog.i(TAG, "jniVoicePlayIF setBackground. back: " + bBackground);		
		native_setBackground(bBackground);
	}
	
	public void onComplete(int playerId, boolean success) {
		native_complete(playerId, success);
	}
	
	public void onSetCallbackBuffer(int playerId, byte[] buffer) {
		native_setCallbackBuffer(playerId, buffer);
	}
	
	protected jniVoicePlayIF() {
		mPlayerMgr = new jniVoicePlayerManager(this);
		mCallbacks = new ArrayList<CallbackData>(20);
		mReqId = 0;
	}
	
	private void handleAudioSession(boolean active) {
		PLog.i(TAG, "Session running=" + active);
		if (null == mAudioManager || mVrMode) return;
		if (active) {
			mAudioManager.requestAudioFocus(mAuidoFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
		} else {
			mAudioManager.abandonAudioFocus(mAuidoFocusListener);
		}
	}
	
	class CallbackData {
		public CallbackData(int reqId, PlaybackListener listener) {
			this.reqId = reqId;
			this.listener = listener;
		}
		
		private int reqId;
		private PlaybackListener listener;
	};

	private void doPlayEnd(int res, int reqId) {
		if (0 == reqId) return;
		Iterator<CallbackData> it = mCallbacks.iterator();
		while(it.hasNext()) {
			CallbackData item = it.next();
			if (reqId == item.reqId) {
				item.listener.OnPlayEnd(res);
				it.remove();
				return;
			}
		}
		PLog.e(TAG, "Not matched playback callbck,seq=" + reqId);
	}
	
	// be called by native layer
	private static int postEventFromNative(Object this_ref,
	            int what, int arg1, int arg2, Object obj) {
	    @SuppressWarnings("unchecked")
		jniVoicePlayIF me = ((WeakReference<jniVoicePlayIF>)this_ref).get();
		if (null == me) return -1;
		return me.handleEvent(what, arg1, arg2, obj);
	}
	
	private int handleEvent(int what, int arg1, int arg2, Object obj) {
		if (null == mPlayerMgr) return -1;
		PLog.d(TAG, "handleEvent what=" + what + ",arg1=" + arg1 + ",arg2=" + arg2);
		
		boolean ok = false;
		final int playerId = what >> 8;
		switch(what & 0xff) {
		case MSG_PLAY_FILE:
			//ok = mPlayerMgr.playFile(playerId, (String)obj, arg1, arg2);
            break;
		case MSG_PLAY_PCM:
			ok = mPlayerMgr.playPcm(playerId, (byte[])obj, arg1);
			break;
		case MSG_PLAY_TTS:
			//ok = mPlayerMgr.playTts(playerId,(String)obj, arg1);
			break;
		case MSG_PLAY_END:
			doPlayEnd(arg1, arg2);
			ok = true;
			break;
		case MSG_STOP:
			mPlayerMgr.stop(playerId, arg1);
			ok = true;
			break;
		case MSG_AUDIO_SESSION:
			handleAudioSession(arg1 != 0);
			ok = true;
			break;
		case MSG_AUDIO_OPEN:
			ok = mPlayerMgr.openAudio(playerId, arg1);
			break;
		case MSG_SET_VOLUME:
			ok = mPlayerMgr.setVolume(playerId, arg1, mAudioManager);
			break;
		case MSG_GET_VOLUME:
			return mPlayerMgr.getVolume(playerId, mAudioManager);
		}
		
		return ok ? 0 : 1;
	}

	private static jniVoicePlayIF s_instance;
	private int mReqId;
	private ArrayList<CallbackData> mCallbacks;
	
	private int mInitResult = -1;
	private AudioManager mAudioManager;
	private AudioFocusListener mAuidoFocusListener;
	private jniVoicePlayerManager mPlayerMgr;

	private int mTelState = 0;
	private boolean mVrMode;
}
