package com.billionav.jni;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;

import com.billionav.ui.R;
import com.billionav.navi.system.PLog;

public class jniVoicePlayerManager{
	private static final String TAG = "VP";

	private static final int MSG_PCM_CB_TIMEOUT	= 1;
	private static final int PCM_CB_TIMEOUT = 4000;		// in millseconds
	
	public static final int PLAYER_DELAY_MAX = 200; 	// in millseconds
	public static final int TRACK_DELAY_MAX = 20;		// in millseconds
	public static final int VOLUME_MAX = 15;
	static final int MAX_PLAYER_NUMBER = 2;
	static final int PLAYER_ID_SOUND = 0;  // Must synchronize with VP_Define.h
	static final int PLAYER_ID_BEEP = 1;  // Must synchronize with VP_Define.h
	
	public static interface CompleteListener {
		void onComplete(int playerId, boolean success);
		void onSetCallbackBuffer(int playerId, byte[] buffer);
	}
	
	interface EventListener {
		void onTimeout(int arg);
	}
	
	class EventHandler extends Handler {
		private EventListener listener;
		
		public EventHandler(EventListener listener) {
			this.listener = listener;
		}
		public void handleMessage(Message msg) {
			listener.onTimeout(msg.arg1);
		}
	}
	
	abstract class PlayerBase {
		protected int mPlayerId;
		protected long mEndTime;
		protected boolean mPlaying;
		
		public PlayerBase(int playerId) {
			mPlayerId = playerId;
			mPlaying = false;
		}
		
		public void stop(int imediately) {
			PLog.i(TAG, "stop imediately=" + imediately + " mPlaying=%d" + mPlaying);
			if (mPlaying) {
				doStop(imediately);
				mPlaying = false;
			}
		}
		
		protected float calcVolume(int volume) {
			if (volume < 0) return 0;
			if (volume > VOLUME_MAX) return 1.0f;
			return volume/(float)VOLUME_MAX;
		}
		
		protected void delay() {
			if (PLAYER_ID_SOUND == mPlayerId) {
				long delay = PLAYER_DELAY_MAX - Math.abs(SystemClock.uptimeMillis()- mEndTime);
				if ( delay > 0) {
					SystemClock.sleep(delay); // Will improve sound effect between phrases
				}
			}
		}
		
		protected abstract void doStop(int imediately);
	}

	class PcmPlayer extends PlayerBase implements AudioTrack.OnPlaybackPositionUpdateListener, EventListener {
		private static final int RETRY_COUNT = 3;
		private static final int MIN_BUF_SIZE = 8192;
		private AudioTrack mTrack = null;
		private byte[] mPcmBuff = null;
		private int mPcmLen;
		private int mPlaySeq = 0;
		private int mMinBufSize;
		private int mSampleRate = 0;
		private int mVolume = VOLUME_MAX;
		
		private EventHandler mHandler;
		
		public PcmPlayer(int playerId) {
			super(playerId);
			mHandler = new EventHandler(this);
		}
		
		public boolean open(int sampleRate) {
			PLog.d(TAG, "mSampleRate=" + mSampleRate + "  sampleRate=" + sampleRate);
			// Create shared buffer 
			if (mSampleRate != sampleRate) {
				mSampleRate = sampleRate;
				PLog.d(TAG, "releaseTrack...");
				releaseTrack();
				PLog.d(TAG, "releaseTrack end");
				
				mMinBufSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
				PLog.d(TAG, "getMinBufferSize end");
				PLog.d(TAG, "createAudioTrack sample rate=" + sampleRate +", min buffsize=" + mMinBufSize);
				if (mMinBufSize < MIN_BUF_SIZE) {
					mMinBufSize = MIN_BUF_SIZE;
				}
				mMinBufSize <<= 1;
				
				mPcmBuff = new byte [mMinBufSize];
				mListener.onSetCallbackBuffer(mPlayerId, mPcmBuff);
			}
			return true;
		}
		
		public synchronized boolean play(byte[] buf, int len) {
			PLog.d(TAG, "PlayPcm, data len=" + len);
			// Create audio track only once
			if (null == mTrack) {
				mTrack = createAudioTrack(mSampleRate);
				//SetAudioTrackVolume(mVolume);
			}
			
			if (0 == len) { // Last PCM block data
				setMarkerPos();
				return true;
			} else if (-1 == len) { // Abort playback
				releaseTrack();
				return true;
			}
			
			// New PCM playback request
			if (0 == mPcmLen) {
				delay();
				mPlaying = true;
				try {
					mTrack.play();
				} catch (Exception e) {
					PLog.e(TAG, "Play PCM exception:" + e.toString());
					mPlaying = false;
				}
				mTrack.setNotificationMarkerPosition(-1);
				mTrack.setPlaybackHeadPosition(0);
				++mPlaySeq;
			}
			
			int ret = outputPcm(buf, len);
			if (ret > 0) {
				mPcmLen += ret;
			}
			PLog.d(TAG, "ret=" + ret + "len=" + len);
			return (ret == len);
		}
		
		@Override
		protected void doStop(int imediately) {
			PLog.d(TAG, "stop..................................");
			if(1 == imediately) {
				stopTrack();
			} else {
				setMarkerPos();
//				mTrack.flush();
//				PLog.d(TAG, "%%%%%="+mTrack.getPlaybackHeadPosition());
//				synchronized (this) {
//					try {
//						PLog.d(TAG, "wait start....");
//						this.wait();
//						PLog.d(TAG, "wait end...");
//					} catch (Exception e) {
//						PLog.e(TAG, "stop wait occur exception" + e.getMessage());
//					}
//				}
				int pos = 0;
				do {  // Montior playback to find when done
	                pos = mTrack.getPlaybackHeadPosition();
	                PLog.d(TAG, "current pos=" + pos);
	                if(pos >= (mPcmLen / 2)) {
	                	break;
	                }
	                
	 				try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						PLog.e(TAG, e.getMessage());
					}
				} while (pos < mPcmLen / 2);
				doTrackEnd();
			}
		}
		
		public boolean SetAudioTrackVolume(int volume)
		{
			final float vol = calcVolume(volume);
			mTrack.setStereoVolume(vol, vol);
			return true;
		}

		public boolean SetVolume(int volume,AudioManager audiomanager) {
			audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_SHOW_UI);
			PLog.d(TAG, "SetVolume(),setStreamVolume volume = " + volume );
			return true;
		}
		
		public int GetVolume(AudioManager audiomanager){
			int vol = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
			PLog.d(TAG, "GetVolume(),getStreamVolume vol = " + vol);
			return vol;
		}

		private AudioTrack createAudioTrack(int sampleRate) {
			AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, 
					AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
					mMinBufSize, AudioTrack.MODE_STREAM);
			track.setPlaybackPositionUpdateListener(this);
			return track;
		}
		
		private int outputPcm(byte[] buf, int len) {
			PLog.d(TAG, "outputPcm write data, len=" + len + ",buflen=" + buf.length);
			int count = 0;
			int offset = 0;
			do {
				int ret = mTrack.write(buf, offset, len);
				if (ret < 0) {
					PLog.e(TAG, "outputPcm write data, ret=" + ret + ", offset=" + offset + ", len=" + len + ",buflen=" + buf.length);
					if (0 == count && AudioTrack.ERROR_INVALID_OPERATION == ret) {
						releaseTrack();
						createAudioTrack(mSampleRate);
						continue;
					}
					break;
				}
				
				len -= ret;
				offset += ret;
				if (len > 0) {
					PLog.e(TAG, "outputPcm waiting, wlen=" + ret +", len=" + len + ",count=" + count);
					SystemClock.sleep(TRACK_DELAY_MAX);
				}
				++count;
			} while (len > 0 && count < RETRY_COUNT);
			return offset;
		}
		
		private void setMarkerPos() {
			PLog.d(TAG, "setMarkerPos,mPcmLen=" + mPcmLen + ",mMinBufSize=" +mMinBufSize);
			if (mPcmLen < mMinBufSize) { // fill dummy sound
			PLog.d(TAG, "mPcmLen=" + mPcmLen + "  mMinBufSize=" + mMinBufSize);
				byte[] dummy = new byte[mMinBufSize - mPcmLen];
				Arrays.fill(dummy, (byte)0);
				int len = mTrack.write(dummy, 0, dummy.length);
				mPcmLen += len;
				dummy = null;
				mTrack.setNotificationMarkerPosition(mMinBufSize/2-1);
			} else {
				mTrack.setNotificationMarkerPosition(mPcmLen/2-1);
			}
			
			//mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_PCM_CB_TIMEOUT, mPlaySeq, 0, this), PCM_CB_TIMEOUT);
		}
		
		public synchronized void onMarkerReached(AudioTrack track) {
			PLog.d(TAG, "PcmPlayer, onMarkerReached");
			if (track != mTrack) {
				PLog.e(TAG, "onMarkerReached, delayed end event!");
				return;
			}
			
//			synchronized (this) {
//				this.notify();
//				PLog.d(TAG, "notify==================");
//			}
			
//			mEndTime = SystemClock.uptimeMillis();
//			doTrackEnd();
		}
		
		public void onPeriodicNotification(AudioTrack track) {
	    }
		
		public void handleMessage(Message msg) {
			PcmPlayer me = (PcmPlayer)msg.obj;
			me.onTimeout(msg.arg1);
		}
		
		public void onTimeout(int seq) {
			if (mPcmLen > 0 && seq == mPlaySeq) {
				PLog.d(TAG, "%%%%fadfa%="+mTrack.getPlaybackHeadPosition());
				PLog.w(TAG, "PlayPcm, onTimeout, total len=" + mPcmLen + ", seq=" + seq);
				//doTrackEnd();
			}
		}
		
		private void doTrackEnd() {
			stopTrack();
			mListener.onComplete(mPlayerId, true);
		}
		
		private void stopTrack() {
			if (null != mTrack) {
				PLog.d(TAG, "stopTrack stop...");
				mPcmLen = 0;
				mPlaying = false;
				mTrack.stop();
				PLog.d(TAG, "stopTrack stop end...");
			}
		}
		
		private void releaseTrack() {
			if (null != mTrack) {
				stopTrack();
				mTrack.release();
				mTrack = null;
				PLog.d(TAG, "releaseTrack release end...");
			}
		}
	}

	public jniVoicePlayerManager(CompleteListener listener) {
		mListener = listener;

		mSoundPcmPlayer = new PcmPlayer(PLAYER_ID_SOUND);
		mBeepPcmPlayer = new PcmPlayer(PLAYER_ID_BEEP);
	}
	
	public synchronized boolean playPcm(int playerId, byte[] buf, int len) {
		PLog.d(TAG, "playPcm len=" + len);
		if (null == mSoundPcmPlayer && null ==  mBeepPcmPlayer) { 
			return false;
		}

		if(PLAYER_ID_SOUND == playerId) {
			return mSoundPcmPlayer.play(buf, len);
		} else if(PLAYER_ID_BEEP == playerId) {
			return mBeepPcmPlayer.play(buf, len);
		} else {
			return false;
		}
	}
	
	public boolean setVolume(int playerId, int volume, AudioManager audiomanager) {
		if (null == mSoundPcmPlayer && null ==  mBeepPcmPlayer) {
			PLog.e(TAG, "mSoundPcmPlayer is null and mBeepPcmPlayer is null");
			return false;
		}
		
		if(PLAYER_ID_SOUND == playerId) {
			PLog.d(TAG, "PLAYER_ID_SOUND setVolume,volume=" + volume);
			return mSoundPcmPlayer.SetVolume(volume,audiomanager);
		} else if(PLAYER_ID_BEEP == playerId) {
			PLog.d(TAG, "PLAYER_ID_BEEP setVolume,volume=" + volume);
			return mBeepPcmPlayer.SetVolume(volume,audiomanager);
		} else {
			return false;
		}
	}
	
	public int getVolume(int playerId, AudioManager audiomanager){
		int volume = 0;
		if(PLAYER_ID_SOUND == playerId) {
			volume = mSoundPcmPlayer.GetVolume(audiomanager);
		} else if(PLAYER_ID_BEEP == playerId) {
			volume = mBeepPcmPlayer.GetVolume(audiomanager);
		}
		
		return volume;
	}
	
	public boolean openAudio(int playerId, int sampleRate) {
		if (null == mSoundPcmPlayer && null ==  mBeepPcmPlayer) {
			PLog.e(TAG, "mSoundPcmPlayer is null and mBeepPcmPlayer is null");
			return false;
		}
		
		if(PLAYER_ID_SOUND == playerId) {
			return mSoundPcmPlayer.open(sampleRate);
		} else if(PLAYER_ID_BEEP == playerId) {
			return mBeepPcmPlayer.open(sampleRate);
		} else {
			return false;
		}
	}
	
	public void stop(int playerId, int imediately) {
			if (null != mSoundPcmPlayer && PLAYER_ID_SOUND == playerId) {
				PLog.d(TAG, "==================2");
				mSoundPcmPlayer.stop(imediately);
			}
			
			if (null != mBeepPcmPlayer && PLAYER_ID_BEEP == playerId) {
				PLog.d(TAG, "==================3");
				mBeepPcmPlayer.stop(imediately);
			}
	}
	
	private CompleteListener mListener;
	private PcmPlayer	mSoundPcmPlayer;
	private PcmPlayer	mBeepPcmPlayer;
}
