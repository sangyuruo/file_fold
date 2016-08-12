package com.billionav.jni;

import java.util.ArrayList;

import com.billionav.voicerecog.NewsInfo;
import com.billionav.voicerecog.POIInfo;

public class UIVoiceControlJNI {
	private static UIVoiceControlJNI instance;
	//onTextShow Param1
	public static final int UITEXT_TYPE_PROMPT		= 0;
	public static final int UITEXT_TYPE_VR 			= 1;		
	public static final int UITEXT_TYPE_HINTS 		= 2;
	
	// BEEP_ID
	public static final int UI_VOICE_BEEP_INVALID 					= 0;
	public static final int UI_VOICE_BEEP_TONE1 					= 12;
	public static final int UI_VOICE_BEEP_TONE2 					= 13;
	public static final int UI_VOICE_BEEP_TONE_VR 					= 15;
	public static final int UI_VOICE_BEEP_TONE_PIC_SAFE 			= 40;
	public static final int UI_VOICE_BEEP_TONE_PIC_CAREFUL 			= 41;
	public static final int UI_VOICE_BEEP_TONE_PIC_STOPGO 			= 42;
	public static final int UI_VOICE_BEEP_TONE_PIC_LDW01 			= 43;
	public static final int UI_VOICE_BEEP_TONE_PIC_LDW02 			= 44;
	public static final int UI_VOICE_BEEP_TONE_VIDEO_RECORD_LONG 	= 51;
	public static final int UI_VOICE_BEEP_TONE_VIDEO_RECORD_SHORT 	= 52;
	public static final int UI_VOICE_BEEP_TONE_CAMERA_SOUND			= 53;
	
	// SOUND_PRIORITY_ID
	public static final int UI_VOICE_SOUND_PRIORITY_VR					= 0;    // voice recognitiong
	public static final int UI_VOICE_SOUND_PRIORITY_REROUTE 			= 1;    // pathg
	public static final int UI_VOICE_SOUND_PRIORITY_GUIDE_REQUEST 		= 2;
	public static final int UI_VOICE_SOUND_PRIORITY_GUIDE_DESTINATION 	= 3;
	public static final int UI_VOICE_SOUND_PRIORITY_GUIDE_WAYPOINT 		= 4;
	public static final int UI_VOICE_SOUND_PRIORITY_GUIDE 				= 5;
	public static final int UI_VOICE_SOUND_PRIORITY_TRAFFIC 			= 6;
	public static final int UI_VOICE_SOUND_PRIORITY_ORBIS 				= 7;
	public static final int UI_VOICE_SOUND_PRIORITY_INFO 				= 8;	// guide use
	public static final int UI_VOICE_SOUND_PRIORITY_UI 					= 9;
	
	// TTS_PRIORITY_ID
	public static final int UI_VOICE_TTS_PRIORITY_AUTO				= 0;
	public static final int UI_VOICE_TTS_PRIORITY_VR				= 1;    // voice recognitiong
	public static final int UI_VOICE_TTS_PRIORITY_REROUTE			= 2;    // pathg
	public static final int UI_VOICE_TTS_PRIORITY_GUIDE_REQUEST		= 3;
	public static final int UI_VOICE_TTS_PRIORITY_GUIDE_DESTINATION	= 4;
	public static final int UI_VOICE_TTS_PRIORITY_GUIDE_WAYPOINT	= 5;
	public static final int UI_VOICE_TTS_PRIORITY_GUIDE				= 6;
	public static final int UI_VOICE_TTS_PRIORITY_TRAFFIC			= 7;
	public static final int UI_VOICE_TTS_PRIORITY_ORBIS				= 8;
	public static final int UI_VOICE_TTS_PRIORITY_INFO				= 9;	// guide use
	public static final int UI_VOICE_TTS_PRIORITY_UI				= 10;
	
//	public static final int UI_VOICE_NAVI_TO_POI_LIST				= 0;
//	public static final int UI_VOICE_POI_LIST_SHOW					= 1;
	
	private boolean theLineIsBusy = false;
	
	public boolean isTheLineIsBusy() {
		return theLineIsBusy;
	}

	public void setTheLineIsBusy(boolean theLineIsBusy) {
		this.theLineIsBusy = theLineIsBusy;
	}

	public boolean startVR(){
		if(theLineIsBusy){
			return false;
		}else{
			startVoiceRecog();
			return true;
		}
	}
	
	public void stopVR(){
		if(!theLineIsBusy){
			stopVoiceRecog();
			theLineIsBusy = true;
		}
	}
	
	//voice recog
	public native void startVoiceRecog();
	
	public native void stopVoiceRecog();
	
	public native void onItemSelected(int tag, int index);
	
	private native Object[] getNaviToPOIList();
	
	public ArrayList<POIInfo> getNaviToPoiList(){
		Object[] objArray = getNaviToPOIList();
		ArrayList<POIInfo> results = new ArrayList<POIInfo>();
		for (int i = 0; i < objArray.length; ++i) {
			results.add((POIInfo)objArray[i]);
		}
		return results;
	}
	
	private native Object[] getPoiInfoList();
	
	private native Object[] getNewsfoList();
	public ArrayList<POIInfo> getPoiList(){
		Object[] objArray = getPoiInfoList();
		ArrayList<POIInfo> results = new ArrayList<POIInfo>();
		for (int i = 0; i < objArray.length; ++i) {
			results.add((POIInfo)objArray[i]);
		}
		return results;
	}
	
	public ArrayList<NewsInfo> getNewsList(){
		
		Object[] objArray = getNewsfoList();
		ArrayList<NewsInfo> results = new ArrayList<NewsInfo>();
		for (int i = 0; i < objArray.length; ++i) {
			results.add((NewsInfo)objArray[i]);
		}
		return results;
	}
	
	public static UIVoiceControlJNI getInstance(){
		if(instance == null){
			instance = new UIVoiceControlJNI();
		}
		return instance;
	}
	
	
	public native void startConfirmVoiceRecog(String promt);
	
	//voice player
	public native void PlayBeep(int eBeepId, int iUserdata, boolean bNeedPlayEndNotificy);
	
	public native void PlaySoundId(int iSoundId, int eSoundPriority, int iUserdata, boolean bNeedPlayEndNotificy);

	public void playGuide(int iSoundId) {
		PlaySoundId(iSoundId, UI_VOICE_SOUND_PRIORITY_GUIDE, 0 ,false);
	}
	public native void requestVoiceInput(int requestID, String promot);
	public native void volumeUP();
	public native void volumeDown();
	public native void volumeMute();
	public native void volumeResume();
	public native void initCurrentVolume();
	//voice TTS
	public native void PlaySoundTts(String strText, int eTTSPriority, int iUserdata, boolean bNeedPlayEndNotificy);
	
	public native void StopPlay(int iUserdata, boolean bStopall);
}
