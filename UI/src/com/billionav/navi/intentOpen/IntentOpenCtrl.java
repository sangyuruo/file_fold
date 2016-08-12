package com.billionav.navi.intentOpen;

import com.billionav.navi.menucontrol.NSViewManager;

import android.content.Intent;

public class IntentOpenCtrl {

	public static final int INTENT_KIND_NONE = 0;
	public static final int INTENT_KIND_POI = 1;
	public static final int INTENT_KIND_ROUTE = 2;
	
	private static int s_intentKind = INTENT_KIND_NONE;
	private static POIData s_poiData; 
	private static boolean s_calledFlag;
	
	public static void pushIntent(int intentKind, POIData data) {
		s_intentKind = intentKind;
		s_poiData = data;
	}
	
	public static POIData PopPoiData() {
		s_intentKind = INTENT_KIND_NONE;
		return s_poiData;
	}
	
	public static int getIntentKind() {
		return s_intentKind;
	}
	
	public static void sendIntent(String msg) {
		Intent intent = new Intent(); 
	      
		intent.setAction("com.suntec.iAuto.btspp.share");                                                                                                                                       
		intent.setType("data/map");//Map 数据格式
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		NSViewManager.GetViewManager().sendBroadcast(intent);
	}
	
	public static class POIData{
		private long m_Lonlat[];
		private String m_POIName;
		public POIData(String poiName , long[] lonlat){
			this.m_POIName = poiName;
			this.m_Lonlat = lonlat;
		}
		public long[] getM_Lonlat() {
			return m_Lonlat;
		}
		public String getM_POIName() {
			return m_POIName;
		}
		
	}
}
