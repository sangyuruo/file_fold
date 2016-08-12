package com.billionav.DRIR.DRDM;

import com.billionav.DRIR.jni.jniDRIR_AudioData;
import com.billionav.navi.camera.CameraView;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;

public class DRIRAudioRecord {
	private static DRIRAudioRecord sInstance;
	// The audio input source
	private final static int AUDIOSOURCE = MediaRecorder.AudioSource.MIC;
	// The audio sample rate
	private final static int SAMPLERATE = 8000;
	// The audio channel
	private final static int CHANNELCONFIG = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	// The audio format
	private final static int AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT;
	// The one frame PCM data size
	private final static int ONEFRAMESIZE = 320;
	private final static String TAG = "DRIRAudioRecord";
	// The audiorecord buffersize
	private int m_iBufSize = 0;
	private AudioRecord m_cAudioRec;
	private boolean m_bIsRec = false;
	
	public static DRIRAudioRecord createInstance(){
		   if(sInstance == null){
			   sInstance = new DRIRAudioRecord();
		   }
	       return sInstance;
	}
	
	public void init()
	{
		createAudioRecord();
	}
	
	public void startRecord()
	{
		if (AudioRecord.STATE_INITIALIZED == m_cAudioRec.getState())
		{
			m_cAudioRec.startRecording();
			
			m_bIsRec = true;
			
			new Thread(new AudioRecThread()).start();
		}
	}
	
	public void stopRecord()
	{
		if (null != m_cAudioRec)
		{
			m_bIsRec = false;
			m_cAudioRec.stop();
			m_cAudioRec.release();
			m_cAudioRec = null;
		}
	}
	
	private void createAudioRecord()
	{
		
		m_iBufSize = AudioRecord.getMinBufferSize(SAMPLERATE, 
				CHANNELCONFIG, AUDIOFORMAT);
		if (0 != m_iBufSize % ONEFRAMESIZE)
		{
			m_iBufSize = (m_iBufSize / ONEFRAMESIZE + 1) * ONEFRAMESIZE;
		}
		
		m_cAudioRec = new AudioRecord(AUDIOSOURCE, SAMPLERATE, 
				CHANNELCONFIG, AUDIOFORMAT, m_iBufSize);
		if (AudioRecord.STATE_INITIALIZED != m_cAudioRec.getState())
		{
			Log.e("TAG", "AudioRecord init failed");
		}				
	}
	
	private void transAudioData()
	{
		byte[] byData = new byte[ONEFRAMESIZE];
		int iReadSize = 0;
		
		while (true == m_bIsRec)
		{
			iReadSize = m_cAudioRec.read(byData, 0, ONEFRAMESIZE);
			
			if (ONEFRAMESIZE == iReadSize)
			{
				//Log.d("DR_DRDM_AudioEncTask", "Transfer the PCM data to audio encode task");
				// Transfer the PCM data to audio encode task
				//jniDRIR_AudioData.AudioDataInput(byData, iReadSize);
			}
		}	
	}
	
	class AudioRecThread implements Runnable{
		@Override
		public void run() {
			// Input the audio PCM data to the audio encode task
			transAudioData();
		}
	}
}
