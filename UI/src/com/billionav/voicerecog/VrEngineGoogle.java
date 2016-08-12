package com.billionav.voicerecog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class VrEngineGoogle extends VrEngineBase implements RecognitionListener {
	// Error map
	private static final int[][] errMap = {
		{SpeechRecognizer.ERROR_NETWORK, 		VrEngine.ERR_NETWORK_OFF},
		{SpeechRecognizer.ERROR_RECOGNIZER_BUSY,VrEngine.ERR_SERVICE},
		{SpeechRecognizer.ERROR_SERVER, 		VrEngine.ERR_SERVICE},
		{SpeechRecognizer.ERROR_NETWORK_TIMEOUT,VrEngine.ERR_NETWORK_TIMEOUT},
		{SpeechRecognizer.ERROR_SPEECH_TIMEOUT, VrEngine.ERR_SPEECH_TIMEOUT},
		{SpeechRecognizer.ERROR_NO_MATCH, 		VrEngine.ERR_NOMATCH}
	};
	
	private SpeechRecognizer recognizer;
	private int timeout;
	
	private File waveFile;
	private OutputStream output;
	private int mWaveLen;
	
	public boolean setContext(Context ctx) {
    	VrLog.i(TAG, "Google VR engine setContext ...");
    	// First release it
		releaseEngine();
		try {
		    // Create voice recognizer
	        recognizer = SpeechRecognizer.createSpeechRecognizer(ctx);
	        recognizer.setRecognitionListener(this);
		} catch (Exception ex) {
			VrLog.e(TAG, "onInit failed, errmsg=" + ex.getMessage());
			return false;
		}
		return true;
	}
	// Voice recognition callback
    public void onReadyForSpeech(Bundle params) {
    	VrLog.i(TAG, "VR CB: onReadyForSpeech");
    	state = STATE_LISTENING;
    	
    	// create output stream
    	if (null != waveFile) {
    		try {
    			output = new FileOutputStream(waveFile);
    			VrUtils.writeWaveHeader(output, 8000, 16, (short)1);
    		} catch (Exception ex) {
    			VrLog.e(TAG, ex.getMessage());
    			output = null;
    		}
    		mWaveLen = 0;
    	}
    	
		// Start timeout timer
    	startTimeout(timeout);
    	listener.onSpeechReady();
    }

    public void onBeginningOfSpeech() {
    	VrLog.i(TAG, "VR CB: onBeginningOfSpeech");
    	if (STATE_LISTENING != state) {
    		VrLog.e(TAG, "Invalid callback sequence, VR state=" + state);
    		return;
    	}
    	timeout = 0;
    	state = STATE_SPEECHING;
    	listener.onSpeechBegin();
    }

    public void onRmsChanged(float rmsdB) {
    //	Log.d(TAG, "VR CB: onRmsChanged, rmsdB="+rmsdB);   
    }

    public void onBufferReceived(byte[] buffer){
    //	Log.d(TAG, "VR CB: onBufferReceived");
    	if (null != output && STATE_SPEECHING == state) {
    		try {
    			output.write(buffer);
    			mWaveLen += buffer.length;
    		} catch (Exception ex) {
    			VrLog.e(TAG, ex.getMessage());
    			output = null;
    		}
    	}
    }

    public void onEndOfSpeech() {
    	VrLog.i(TAG, "VR CB: onEndOfSpeech");
    	state = STATE_SPEECH_END;
    	
    	if (null != output) { // Fill wave header length
    		try {
    			output.close();
    		} catch (Exception ex) {
    			VrLog.e(TAG, ex.getMessage());
    		}
    		output = null;
    		VrUtils.FillHeader(waveFile, mWaveLen);
    	}
    	listener.onSpeechEnd();
    }

    public void onError(int error) {
    	VrLog.e(TAG, "VR CB: onError, err="+ error);
    	if (SpeechRecognizer.ERROR_SPEECH_TIMEOUT == error) {
    		VrLog.e(TAG, "VR CB: onError, speech timeout");
    		recognizer.stopListening();
    	} else {
    		state = STATE_NONE;
    	}
    	
    	if (SpeechRecognizer.ERROR_CLIENT == error) {
    		return;
    	}
    	
    	int err = ERR_SERVICE;
    	for (int i=0; i<errMap.length; ++i) {
    		if (error == errMap[i][0]) {
    			err = errMap[i][1];
    		}
    	}
    	listener.onSpeechError(err);
    }

    public void onResults(Bundle results) {
    	VrLog.i(TAG, "VR CB: onResults");
    	state = STATE_NONE;
    	ArrayList<String>texts = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    	VrLog.i(TAG, "VR text=" + texts.get(0));
    	
    	listener.onSpeechResult(texts.get(0), 100);
    }

    public void onPartialResults(Bundle partialResults) {
    	VrLog.i(TAG, "VR CB: onPartialResults"); 
    }

    public void onEvent(int eventType, Bundle params) {
    	VrLog.i(TAG, "VR CB: onEvent, type=" + eventType);
    }
    
    @Override
    public boolean supportSpeech() {
    	return (null != recognizer);
    }
    
    @Override
    protected boolean isTimeout() {
    	return (0 != timeout);
    }
    
    @Override
    protected void onStart(int cmdMode, int timeout, String output) {
    	VrLog.i(TAG, "doStartListening timeout=" + timeout);
		if (STATE_NONE != state) {
			VrLog.w(TAG, "startListening invalid state=" + state);
		}
		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.pset.navi");
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Navi Speech Recognition Test");
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 8); 
		try {
			this.timeout = timeout;
			this.waveFile = (null != output) ? new File(output) : null;
			recognizer.startListening(intent);
			
			state = STATE_PENDING;
		} catch (Exception ex) {
			VrLog.e(TAG, "doStartListening failed, err msg=" + ex.getMessage());
			this.output = null;
			listener.onSpeechError(ERR_NOMATCH);
		}
    }
    
    @Override
    protected void onStop() {
    	VrLog.i(TAG, "doStopListening state=" + state);
		if (STATE_SPEECH_END == state) {
			recognizer.cancel();
		} else if (STATE_SPEECHING == state){
			recognizer.stopListening();
		}
    }
    
	private void releaseEngine() {
		if (null != recognizer) {
			recognizer.destroy();
			recognizer = null;
		}
	}
}
