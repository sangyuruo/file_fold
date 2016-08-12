package com.billionav.navi.naviscreen.map;

import com.billionav.jni.UIMapControlJNI;


public class POI_Mark_Control {

	public final static int MARK_SHOWN_STATE_ON = 1;
	public final static int MARK_SHOWN_STATE_OFF = 0;
	
	
	public static void forNavigaitonView(){
		setMarks(MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_ON, true);
	}
	public static void forDemoRun(){
		setMarks(MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, false);
	}
	
	public static void forSearchResultView(){
		setMarks(MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, false);
	}
	public static void forPOISearchResultView(){
		forSearchResultView();
	}
	public static void forAddPointView(){
		setMarks(MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, false);
	}
	public static void forRoutePreviewView(){
		setMarks(MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, false);
	}
	public static void forRouteEditView(){
		
		forAddPointView();
	}
	
	public static void forFavoriteView(){
		setMarks(MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_ON, MARK_SHOWN_STATE_OFF, MARK_SHOWN_STATE_OFF, false);
	}
	
	private static void setMarks(int guidePoint, int logoMark, int RegPlace, int vicinityMark, int snsMark, boolean trafficMark){
		UIMapControlJNI.SetGuidePointState(guidePoint);
		UIMapControlJNI.SetLogoMarkState(logoMark);
		UIMapControlJNI.SetRegPlaceState(RegPlace);
		UIMapControlJNI.SetSNSMarkState(snsMark);
		UIMapControlJNI.SetVicinityMarkState(vicinityMark);
		UIMapControlJNI.SetTrafficState(trafficMark);
		UIMapControlJNI.executeSets();
//		if(jniSetupControl.isOn(jniSetupControl.STUPDM_FAVORITE_DISPLAY_INT_KEY)) {
//			UIMapControlJNI.SetRegPlaceState(RegPlace);
//		}else{
//			UIMapControlJNI.SetRegPlaceState(MARK_SHOWN_STATE_OFF);
//		}
//		if(jniSetupControl.isOn(jniSetupControl.STUPDM_SURROUNDING_EVENT_INT_KEY)){
//			UIMapControlJNI.SetSNSMarkState(snsMark);
//		}else{
//			UIMapControlJNI.SetSNSMarkState(MARK_SHOWN_STATE_OFF);
//		}
//		if(jniSetupControl.isOn(jniSetupControl.STUPDM_TRAFFIC_LINE_DISPLAY)){
//			UIMapControlJNI.SetTrafficState(trafficMark);
//		}else{
//		}
	}
}
