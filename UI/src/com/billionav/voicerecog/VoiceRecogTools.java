package com.billionav.voicerecog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;


public class VoiceRecogTools {
	private static final String TAG = "VR";
	
	public static final int TYPE_SYSTEM = 1;
	public static final int TYPE_MUSIC = 2;
	public static final String SENT_SMS_ACTION = "voice_send_sms_action";
	
	private VoiceRecogTools() {}
	
	public static VoiceRecogTools instance() {
		if (null == inst) {
			inst = new VoiceRecogTools();
		}
		return inst;
	}

	public void init(Context c) {
		VrLog.i(TAG, "VoiceRecogTools.Init");
		mCtx = c;
		mCtx.registerReceiver(sendSMSRcv, new IntentFilter(SENT_SMS_ACTION));
		mAM = (AudioManager)mCtx.getSystemService(Context.AUDIO_SERVICE);
		mPM = new PlayMusic(mCtx);
	}
	
	private void setVolume(int type, int vol) {
		if (mAM == null) {
			VrLog.e(TAG, "setVolume am is null");
			return;
		}
		
		int streamType;
		switch (type) {
		case TYPE_MUSIC:
			streamType = AudioManager.STREAM_MUSIC;
			break;
		case TYPE_SYSTEM:
			streamType = AudioManager.STREAM_SYSTEM;
			break;
		default:
			VrLog.e(TAG, "setVolume not surport type:" + type);
			return;
		}
		
		int max = mAM.getStreamMaxVolume(streamType);
		int cur = mAM.getStreamVolume(streamType);
		cur += vol;
		cur = Math.min(cur, max);
		if (cur < 0) {
			cur = 0;
		} else if (cur > max) {
			cur = max;
		}
		
		mAM.setStreamVolume(streamType, cur, AudioManager.FLAG_SHOW_UI);
	}
	
	public void upVolume(int type, int vol) {
		VrLog.i(TAG, "upVolume, type=" + type + ",vol=" + vol);
		setVolume(type, vol);
	}
	
	public void downVolume(int type, int vol) {
		VrLog.i(TAG, "downVolume, type=" + type + ",vol=" + vol);
		setVolume(type, -vol);
	}
	
	public void setMute(int type, boolean mute) {
		VrLog.i(TAG, "VoiceRecogTools.SetMute");
		if (mAM == null) {
			VrLog.i(TAG, "VoiceRecogTools.SetVolume am is null");
			return;
		}
		if (type == TYPE_MUSIC)
			mAM.setStreamMute(AudioManager.STREAM_MUSIC, mute);
		else if (type == TYPE_SYSTEM)
			mAM.setStreamMute(AudioManager.STREAM_SYSTEM, mute);
	}
	
	public void callPhone(String name, String phoneNo) {
		VrLog.i(TAG, "VoiceRecogTools.callPhone name:" + name + ", no:" + phoneNo);
		
		Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phoneNo));
		mCtx.startActivity(intent);
		
		VrLog.i(TAG, "VoiceRecogTools.callPhone end");
	}
	
	public void sendSMS(String name, String phoneNo, String content) {
		VrLog.i(TAG, "VoiceRecogTools.sendSMS phoneNo:" + phoneNo + "  content:" + content);
		if (null == phoneNo || null == content) {
			VrLog.e(TAG, "VoiceRecogTools.sendSMS phoneNo or content is null");
			return;
		}

		if (phoneNo.equals("") || content.equals("")) {
			VrLog.e(TAG, "VoiceRecogTools.sendSMS phoneNo or content is empty");
			return;
		}
		SmsManager sm = SmsManager.getDefault();
		PendingIntent pi = PendingIntent.getBroadcast(mCtx, 0, new Intent(SENT_SMS_ACTION), 0);
		ArrayList<String> msgs = sm.divideMessage(content);  
		for (String msg : msgs) {  
			sm.sendTextMessage(phoneNo, null, msg, pi, null);  
		}
	}
	
	public StringBuilder getContacts() {
		VrLog.i(TAG, "VoiceRecogTools.getContacts begin");
		StringBuilder sb = new StringBuilder();
		sb.append("{\"phoneBookItems\":[");
		
		boolean ok = readPhoneContacts(sb);
		if (isSimReady()) {
			readSimContacts(sb, !ok);
		}

		sb.append("]");
		VrLog.i(TAG, "VoiceRecogTools.getContacts: " + sb);
		return sb;
	}
	
	private boolean isSimReady() {
		try { 
	        TelephonyManager mgr = (TelephonyManager) mCtx.getSystemService(Context.TELEPHONY_SERVICE); 
	        return TelephonyManager.SIM_STATE_READY == mgr.getSimState(); 
	    } catch (Exception e) {
	        e.printStackTrace();
	        VrLog.i(TAG, "VoiceRecogTools.IsSIMCanRead read sim error");
	    }
	    return false; 
	}
	
	public int playMusic(MusicInfo info) {
		return mPM.playMusic(info);
	}
	
	public void pauseMusic() {
		mPM.pauseMusic();
	}
	
	public void stopMusic() {
		mPM.stopMusic();
	}
	
	public void playNext() {
		mPM.playNext();
	}
	
	public void playPrev() {
		mPM.playPrev();
	}
	
	private boolean readPhoneContacts(StringBuilder sb) {
		String columns[] = {Contacts._ID, Contacts.DISPLAY_NAME, Contacts.HAS_PHONE_NUMBER};
		String phoneCols[] = {Phone.NUMBER, Phone.TYPE, Phone.LABEL};
		
		ContentResolver resolver = mCtx.getContentResolver();
		Cursor cursor = resolver.query(Contacts.CONTENT_URI, columns, null, null, null);
		if (null == cursor) {
			return false;
		}
		
		boolean mainFirst = true; 
		while (cursor.moveToNext()) {
			String id= cursor.getString(0); 
			String name = cursor.getString(1);
			boolean hasPhoneNo = cursor.getString(2).equals("1");
			if (!hasPhoneNo) {
				continue;
			}
			
			if (mainFirst) {
				mainFirst = false;
			} else {
				sb.append(",");
			}
			sb.append("{\"FNAME\":\"\",\"NAME\":\"").append(name).append("\",");
			sb.append("\"TEL_PREF\":\"").append("").append("\",");
			sb.append("\"TEL_TYPE\":[");

			Cursor phoneCurosor = resolver.query(Phone.CONTENT_URI, phoneCols, Phone.CONTACT_ID + "=" + id, null, null);
			if (null == phoneCurosor) {
				continue;
			}
			
			boolean first = true;
			while (phoneCurosor.moveToNext()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("{");
				sb.append("\"TEL_NUM\":\"").append(phoneCurosor.getString(0)).append("\",");
				sb.append("\"TEL_TAG\":\"").append(
						GetPhoneTypeName(phoneCurosor.getInt(1),phoneCurosor.getString(2))).append("\"");
				sb.append("}");
			}
			
			phoneCurosor.close();
			sb.append("]}");
		}
		
		cursor.close();
		return true;
	}
	
	private void readSimContacts(StringBuilder sb, boolean first) {
	    String phoneName = null; 
        String phoneNo = null;
        
        try {
	        Uri simUri = Uri.parse("content://icc/adn"); 
	        Cursor cursor = mCtx.getContentResolver().query(simUri,null,null,null,null);
	        VrLog.i(TAG, "sim total: " + cursor.getCount());
	        
	        while (cursor.moveToNext()) {
	        	phoneName =cursor.getString(cursor.getColumnIndex("name"));
	        	phoneNo = cursor.getString(cursor.getColumnIndex("number"));
	        	phoneName = phoneName.replace("|", "");
	        	phoneNo.replaceAll("\\D", "");
	        	phoneNo.replaceAll("&",  "");
	        	VrLog.d(TAG, "name: "+phoneName+" phone: "+phoneNo);
	        	
	        	if (phoneNo.length() == 0) {
					continue;
				}
	        	
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("{\"FNAME\":\"\",\"NAME\":\"").append(phoneName).append("\",");
				sb.append("\"TEL_PREF\":\"").append(phoneNo).append("\",");
				sb.append("\"TEL_TYPE\":[]}");
	        }
	        
	        cursor.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	private String GetPhoneTypeName(int type, String name) {
		switch (type) {
		case Phone.TYPE_HOME:
			return "HOME";
		case Phone.TYPE_MOBILE:
			return "CELL";
		case Phone.TYPE_WORK:
		case Phone.TYPE_WORK_MOBILE:
			return "WORK";
		}
		
		return (String) Phone.getTypeLabel(mCtx.getResources(), type, name);
	}
	
	private static BroadcastReceiver sendSMSRcv = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        switch (getResultCode()) {
		        case Activity.RESULT_OK:
		        	VrLog.i(TAG, "VoiceRecogTools send SMS success");
		        	break;
		        
		        default:
		        	VrLog.i(TAG, "VoiceRecogTools send SMS fail");
		        	break;
	        }
	    }
	};
	
	public static final String md5(final String s) {
	    final String MD5 = "MD5";
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance(MD5);
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuilder hexString = new StringBuilder();
	        for (byte aMessageDigest : messageDigest) {
	            String h = Integer.toHexString(0xFF & aMessageDigest);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	private static VoiceRecogTools inst;
	private Context mCtx;
	private AudioManager mAM;
	private PlayMusic mPM;
}
