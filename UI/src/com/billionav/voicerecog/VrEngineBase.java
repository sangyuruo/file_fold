package com.billionav.voicerecog;

import java.io.File;

import android.content.Context;

import android.os.Handler;
import android.os.Message;

public abstract class VrEngineBase implements VrEngine {
	protected static final String TAG = "VR";
	
	// Speech recognizer state
	protected static final int STATE_NONE 		= 0;
	protected static final int STATE_PENDING 	= 1;
	protected static final int STATE_LISTENING 	= 2;
	protected static final int STATE_SPEECHING 	= 3;
	protected static final int STATE_SPEECH_END = 4;
	
	// Engine operation message id
	private static final int MSG_START_LISTENING	= 1;
	private static final int MSG_STOP_LISTENING 	= 2;
	private static final int MSG_SPEECH_TIMEOUT 	= 3;
	
	protected VrEngineListener listener;
	protected int state = STATE_NONE;
	
	private boolean checkVrPath = false;
	
	// Must be main thread handler
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_LISTENING:
				onStart(msg.arg1, msg.arg2, (String)msg.obj);
				break;
			case MSG_STOP_LISTENING:
				onStop();
				break;
			case MSG_SPEECH_TIMEOUT:
				if (isTimeout()) {
					VrLog.w(TAG, "User don't speak for long time");
					onStop();
				}
				break;
			}
		}
	};
	
	public void setListener(VrEngineListener listener) {
		this.listener = listener;
	}
	
	public void setParam(int param1, String param2) {
		
	}
	
	public boolean setContext(Context ctx) {
        return true;
	}
	
	public boolean uploadData(String category, String data) {
		return false;
	}
	
	public void startTimeout(int timeout) {
		mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_SPEECH_TIMEOUT, timeout), timeout);
	}
	
	public boolean startSpeech(int cmdMode, int timeout, String output) {
		VrLog.i(TAG, "startSpeech mode=" + cmdMode + ", timeout=" + timeout);
		makeVrDir(output);
		Message.obtain(mHandler, MSG_START_LISTENING, cmdMode, timeout, output).sendToTarget();
		return true;
	}
	
	public void stopSpeech() {
		VrLog.i(TAG, "stopSpeech ...");
		if (STATE_NONE == state) {
			VrLog.w(TAG, "stopSpeech invalid state=" + state);
		}
		Message.obtain(mHandler, MSG_STOP_LISTENING).sendToTarget();
	}
	
	private void makeVrDir(String path) {
		if (!checkVrPath) {
			checkVrPath = true;
			if (null != path && path.length()>0) {
				int index  = path.lastIndexOf(File.separatorChar);
				if (index > 0) {
					path = path.substring(0, index);
					VrLog.i("TAG", "vr rec data path=" + path);
					File file = new File(path);
					if (!file.exists()) {
						file.mkdirs();
					}
				}
			}
		}
	}
	
    protected abstract boolean isTimeout();
    protected abstract void onStart(int cmdMode, int timeout, String output);
    protected abstract void onStop();
}
