package com.billionav.voicerecog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;

import com.iflytek.Setting;
import com.iflytek.speech.DataUploader;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechListener;
import com.iflytek.speech.SpeechRecognizer;

public class VrEngineKeda extends VrEngineBase implements RecognizerListener
{
	// Refer to Keda engine doc and source code
	private static final String APP_ID = "appid=4ed86508";
	private static final String UPLOAD_DATA_TYPE = "subject=asr,data_type=keylist";
	
	private static final String MODE_SMS = "sms";
	private static final String MODE_ASR = "asr";
	private static final int	VAD_EOS	 = 1400;	// in millseconds

	private static final int SAMPLE_RATE		= 2*8000;
	private static final int RECORDER_BPP		= 16;
	private static final int CHANNEL_CONFIG    	= AudioFormat.CHANNEL_IN_MONO;
	private static final int AUDIO_FORMAT      	= AudioFormat.ENCODING_PCM_16BIT;
	private static final int AUDIO_SOURCE      	= MediaRecorder.AudioSource.MIC;
	//private static final int SPLIT_BUFFER_SIZE = 4096;
	
	private static final int[][] errMap = {
		{SpeechError.ERROR_NO_NETWORK, 		VrEngine.ERR_NETWORK_OFF},
		{SpeechError.ERROR_NET_EXPECTION, 	VrEngine.ERR_NETWORK_OFF},
		{SpeechError.ERROR_SERVER_CONNECT, 	VrEngine.ERR_SERVICE},
		{SpeechError.ERROR_NETWORK_TIMEOUT, VrEngine.ERR_NETWORK_TIMEOUT},
		{SpeechError.ERROR_SPEECH_TIMEOUT, 	VrEngine.ERR_SPEECH_TIMEOUT},
		{SpeechError.ERROR_NO_MATCH, 		VrEngine.ERR_NOMATCH}
	};
	
	private Context context;
	private DataUploader uploader;
	private SpeechRecognizer recognizer;
	private UploadDataListener uploadListener;
	private String grammar;
	private boolean forceUpload;
	private boolean dumpEngLog;
	private int recogMode = -1;
	private int  confidence;
	private StringBuilder resultSb;
	
	private File waveFile;
	private OutputStream output;
	private int mWaveLen;
	private AudioRecord audioRecord = null;
	private Thread audioThread = null;
	private boolean recording = false;
	private int minBufferSizeInBytes;
	private int mCmdMode;
	
	class UploadDataListener implements SpeechListener {
		@Override
		public void onEnd(SpeechError error) {		
			if(error !=null) {
		    	VrLog.e(TAG, "Update data failed, errmsg=" + error.toString());
		    }
		}

		@Override
		public void onData(byte[] arg0) {
			grammar = new String(arg0);
	    	listener.onCommandUploaded("", grammar);
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}
	}

	public VrEngineKeda() {
		resultSb = new StringBuilder(256);
		uploader = null;
		grammar = null;
		forceUpload = false;
		dumpEngLog = false;
	}
	
	public boolean setContext(Context ctx) {
		VrLog.i(TAG, "Keda VR engine setContext ...");
		if (null == ctx) {
			VrLog.e(TAG, "setContext ctx is null");
			return false;
		}
		
	 	if (dumpEngLog) {
	 		Setting.saveLogFile(Setting.LOG_LEVEL.all, "/sdcard/mscnu.log");
    		VrLog.d(TAG, "@@@ Dump Keda VR engine speech data!!!");
    	}
		
		if (null == grammar || forceUpload) {
			VrLog.i(TAG, "Keda VR engine upload data");
			uploader = new DataUploader();
			uploadListener = new UploadDataListener();
		}
		
		if (null != recognizer) {
			recognizer.cancel();
			recognizer = null;
		}
		
		recognizer = SpeechRecognizer.createRecognizer(ctx, APP_ID);
		recognizer.setSampleRate(RATE.rate16k);
		context = ctx;
	    return true;
	}
	
	public void setParam(int param1, String param2) {
		if (param1 != -1) {
			this.forceUpload = (param1 & FORCE_UPLOAD_DICT) > 0;
			this.dumpEngLog = (param1 & DUMP_SPEECH_DATA) > 0;
		}
		this.grammar = param2;
	}
	
	@Override
	public boolean uploadData(String category, String data) {
		VrLog.i(TAG, "uploadData cat=" + category + ", data=" + data);
		if (null == uploader) {
			return false;
		}
		
		setData(category, data);
		return true;
	}
	
	@Override
	public void onCancel() {
		recording = false;
		listener.onSpeechError(ERR_USERSTOP);
	}
	
    @Override
    public void onBeginOfSpeech() {
    	VrLog.i(TAG, "Keda onBeginOfSpeech ...");
        // create output stream
    	mWaveLen = 0;
    	output = null;
       	listener.onSpeechReady();
       	
    }
    
//    @Override
    public void onBufferReceived(byte[] buffer, int vol) {
    	VrLog.d(TAG, "onBufferReceived len=" + buffer.length +",vol=" + vol);
    	if (null != output) {
    		try {
    			output.write(buffer, 0, buffer.length);
    			mWaveLen += buffer.length;
    		} catch (Exception ex) {
    			VrLog.e(TAG, ex.getMessage());
    			output = null;
    		}
    	}
    }
    
	@Override
	public void onVolumeChanged(int val) {
		VrLog.d(TAG, "volume value:"+val);		
	}
    
    @Override
    public void onEndOfSpeech() {
    	VrLog.i(TAG, "Keda onEndOfSpeech ...");
    	recording = false;
//    	if (null != output) { // Fill wave header length
//    		try {
//    			output.close();
//    		} catch (Exception ex) {
//    			VrLog.e(TAG, ex.getMessage());
//    		}
//    		output = null;
//    		VrUtils.FillHeader(waveFile, mWaveLen);
//    	}
    	listener.onSpeechEnd();
    }
    
    @Override
    public void onEnd(SpeechError error) {
		recording = false;
    	if (null == error) {
    		return;
    	}
    	
    	VrLog.w(TAG, "Keda onEnd errType=" + error.getErrorType() + ",errMsg=" + error);
    	int err = ERR_SERVICE;
    	for (int i=0; i<errMap.length; ++i) {
    		if (error.getErrorType() == errMap[i][0]) {
    			err = errMap[i][1];
    			break;
    		}
    	}
    	
    	/*
    	if (SpeechError.ERROR_NETWORK_TIMEOUT == error.getErrorType() 
    			&& VrEngine.MODE_COMMAND == recogMode
    			&& retryByWave()
    			) {
    		return;
    	}
    	*/
    	
    	listener.onSpeechError(err);
    	// Save speech engine record voice data
    	// saveSpeechWave();
    }
    
    @Override
    public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
    	VrLog.d(TAG, "Keda Recognized result size=" + results.size() + ",last=" + isLast);
    	Iterator<RecognizerResult> it = results.iterator();
		while (it.hasNext()) {
			RecognizerResult item = it.next();
			VrLog.d(TAG, "text=" + item.text + ", confidence=" + item.confidence);
		}
		
		if (VrEngine.MODE_COMMAND == recogMode && 0 == resultSb.length()) {
			isLast = true;
		}
		
    	if (results.size() > 0) {
    		RecognizerResult result = results.get(0);
    		confidence = result.confidence;
    		resultSb.append(result.text);
    	}
    	
    	if (isLast) {
    		recording = false;
    		listener.onSpeechResult(resultSb.toString(), confidence);
    	}
    }
    
	@Override
    public boolean supportSpeech() {
    	return (null != recognizer);
    }
	
	@Override
    protected boolean isTimeout() {
    	return false;
    }
    
    @Override
    protected void onStart(int cmdMode, int timeout, String outputPath) {
    	VrLog.i(TAG, "Keda onStart mode=" + cmdMode + ",timeout=" + timeout + ", Output file=" + outputPath);
    	recording = true;
    	mCmdMode = cmdMode;
    	this.recogMode = cmdMode;
    	this.waveFile = (null != outputPath && outputPath.length() > 0) ? new File(outputPath) : null;
 
 		minBufferSizeInBytes = AudioRecord.getMinBufferSize( SAMPLE_RATE,
        		                                             CHANNEL_CONFIG,
        		                                             AUDIO_FORMAT);
 		VrLog.i(TAG, "min buffer size=" + minBufferSizeInBytes);
  
        if( minBufferSizeInBytes == AudioRecord.ERROR_BAD_VALUE ) {
        	VrLog.e( TAG, "Bad Value for \"minBufferSize\", recording parameters are not supported by the hardware");
        }
        
        if( minBufferSizeInBytes == AudioRecord.ERROR ) {
        	VrLog.e( TAG, "Bad Value for \"minBufferSize\", implementation was unable to query the hardware for its output properties");
        }
       
        // Initialize Audio Recorder.
        try {
        	audioRecord = new AudioRecord(AUDIO_SOURCE,
        									SAMPLE_RATE,
        									CHANNEL_CONFIG,
        									AUDIO_FORMAT,
        									minBufferSizeInBytes);
        } catch(IllegalArgumentException ex) {
        	VrLog.e( TAG, "new AudioRecord failed, errmsg=" + ex.getMessage() );
        	return;
        }
        
        audioThread = new Thread( new Runnable() {
			@Override
			public void run() {
				int numByte;
				OutputStream os;
				final int BUF_SIZE = Math.max(minBufferSizeInBytes, 4*1024);
				byte audioBuffer[] = new byte[BUF_SIZE];  // minBufferSizeInBytes
				
				VrLog.d(TAG, "@@@ audio buffer size=" + BUF_SIZE);
				int readedBufferLength = 0;
				
				try {
					os = (null != waveFile) ? new FileOutputStream(waveFile) : null;
		    		if(null != os) {
		    			VrUtils.writeWaveHeader(os, SAMPLE_RATE, RECORDER_BPP, (short)1);
		    		}

					startRecog();
					audioRecord.startRecording();

					int len = 0;
					while (recording) {
						numByte = 0;
						do {
							len = audioRecord.read(audioBuffer, numByte, minBufferSizeInBytes);
							if (len < 0) break; // TODO:
							numByte += len;
						} while (recording && numByte+minBufferSizeInBytes < BUF_SIZE);
						
						if (numByte <= 0) break;
						
						// write to rec file
						if(null != os) {
							os.write(audioBuffer, 0, numByte);
						}
						
						int offset = 0;
						do {
							len = Math.min(2048, (numByte-offset));
							if (!recognizer.writeAudio(audioBuffer, offset, len)) {
								VrLog.e(TAG, "writeAudio Error, offset:" + offset + ", len: " + len);
								break;
							}
							offset += len;
						} while (offset < numByte);
						readedBufferLength += numByte;
					}

					// recognizer.stopListening();	
					VrLog.i(TAG, "stop recording ...");
					audioRecord.stop();
					audioRecord.release();
					audioRecord = null;
					VrLog.i(TAG, "stop recording done");
					
					if(null != os) {
						os.close();
						VrUtils.FillHeader(waveFile, readedBufferLength);	
					}
				
					VrLog.i(TAG, "recording stopped!");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
        });

    	resultSb.setLength(0);
        audioThread.start();
       	listener.onSpeechReady();
    	VrLog.i(TAG, "start recording ok!");
    }
    
    @Override
    protected void onStop() {
    	VrLog.i(TAG, "Keda onStop state=" + state);
    	recording = false;
    	recognizer.stopListening();
    }
    
    private boolean setData(String category, String data) {
    	byte[] utf8Data = null;
    	try {
    		utf8Data = data.getBytes("UTF-8");
    	} catch (UnsupportedEncodingException ex) {
    		VrLog.e(TAG, "setCommands failed, err=" + ex.getMessage());
    		return false;
    	}
    	
		uploader.uploadData(context, uploadListener, category, UPLOAD_DATA_TYPE, utf8Data);
		VrLog.i(TAG, "Upload Keda engine command data, cat=" + category + ",len=" + utf8Data.length);
		return true;
    }
    
    public void startRecog() {
    	VrLog.i(TAG, "start with vad_eos,grammar=" + grammar + "$");
    	if (MODE_COMMAND == mCmdMode && null != grammar) {
			recognizer.recognizeStream(this, MODE_ASR , "vad_eos=" + VAD_EOS, grammar);
		} else{
			recognizer.recognizeStream(this, MODE_SMS , null, null);
		}
    }
}
