package com.billionav.voicerecogJP;

import java.util.ArrayList;
import java.util.List;

import com.billionav.navi.naviscreen.NaviViewManager;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechRecognizer;

public class VRIflytek {

	private static final String CREATE_PARAMS = "appid=4e38dd5f,timeout=20000,server_url=http://222.151.222.142:80/index.htm,search_best_url=0"; // YRP

	private static final String ENT = "sms";

	private static final String START_PARAMS = "vad_timeout=10000,vad_speech_tail=800,eos=800";

	private static SpeechRecognizer sSpeechRecognizer;

	private boolean mStartFlg = false;

	private static VRIflytek instanceObj = null;

	public static VRIflytek sharedInstance() {
		if (null == instanceObj) {
			instanceObj = new VRIflytek();
		}
		return instanceObj;
	}

	public interface IFlytecListener {

		public void onResult(final List<String> results);

		public void onError();

		public void onCancel();

		public void onEnd();
	}

	private IFlytecListener listener = null;

	private VRIflytek() {
		sSpeechRecognizer = SpeechRecognizer.createRecognizer(
				NaviViewManager.GetViewManager(), CREATE_PARAMS);
		sSpeechRecognizer.setSampleRate(RATE.rate16k);
	}

	public boolean startIflytecRecognize(IFlytecListener l) {
		listener = l;
		sSpeechRecognizer.startListening(mRecoListener, ENT, START_PARAMS, null);
		return true;
	}
	
	public void stopRecognize() {
		try {
			if (mStartFlg) {
				sSpeechRecognizer.stopListening();
			} else {
				sSpeechRecognizer.cancel();
			}
		} catch (Exception e) {
		}
	}

	private RecognizerListener mRecoListener = new RecognizerListener() {
		@Override
		public void onResults(ArrayList<RecognizerResult> results,
				boolean isLast) {
			StringBuffer result = new StringBuffer();
			if (results != null) {
				for (RecognizerResult recognizerResult : results) {
					result.append(recognizerResult.text);
				}
			}
			
			if (isLast) {
				mStartFlg = false;
				if (null != listener) {
					ArrayList<String> resultsList = new ArrayList<String>();
					resultsList.add(result.toString());
					listener.onResult(resultsList);
				}
				
			} else {
			}
		}

		@Override
		public void onEnd(SpeechError error) {
			mStartFlg = false;
			if (null == listener) {
				return;
			}
			if (error != null) {
				listener.onError();
			} else {
				listener.onEnd();
			}
		}

		@Override
		public void onBeginOfSpeech() {
			mStartFlg = true;
		}

		@Override
		public void onEndOfSpeech() {
		}

		@Override
		public void onCancel() {
			mStartFlg = false;
			if (null != listener) {
				listener.onCancel();
			}
		}

		@Override
		public void onVolumeChanged(int arg0) {
		}
	};
}
