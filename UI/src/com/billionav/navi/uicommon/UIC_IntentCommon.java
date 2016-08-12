package com.billionav.navi.uicommon;

import java.util.Arrays;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;

public class UIC_IntentCommon {
	
	private static final String TAG_INTENT_CALL = "IntentCall";
	
	private UIC_IntentCommon(){}
	
	private static UIC_IntentCommon instance = new UIC_IntentCommon();
	
	public static UIC_IntentCommon  Instance() {
		return instance;
	}
	
	
	public static final int INTENT_CALL_TYPE_MARK = 0x0800;
	public static final int INTENT_CALL_TYPE_CONTACTS = INTENT_CALL_TYPE_MARK + 1;
	public static final int INTENT_CALL_TYPE_POINT = INTENT_CALL_TYPE_MARK + 2;
	public static final int INTENT_CALL_TYPE_GOOGLE_MAP = INTENT_CALL_TYPE_MARK + 3;
	
	private int mIntentCallType = INTENT_CALL_TYPE_MARK;
	
	public void setIntentCallType(int type) {
		mIntentCallType = type;
	}
	
	public int getIntentCallType() {
		return mIntentCallType;
	}
	
	public void resetIntentCallType() {
		mIntentCallType = INTENT_CALL_TYPE_MARK;
	}
	
	public boolean isIntentCallVaild() {
		return (mIntentCallType != INTENT_CALL_TYPE_MARK);
	}

	
	private long[] lonlatForExternalSearch = new long[2];
	private String addressForExternalSearch;
	
	
	public long[] getLonlatForExternalSearch() {
		return lonlatForExternalSearch;
	}

	public void setLonlatForExternalSearch(long lon, long lat) {
		lonlatForExternalSearch[0] = lon;
		lonlatForExternalSearch[1] = lat;
	}
	
	public String getAddressForExternalSearch() {
		return addressForExternalSearch;
	}

	public void setAddressForExternalSearch(String addressForExternalSearch) {
		this.addressForExternalSearch = addressForExternalSearch;
	}

	// return: true if data err
	public boolean isExteralSearchDataError() {
		long[] lonlat = UIC_IntentCommon.Instance().getLonlatForExternalSearch();
		
		return (lonlat[0] <= 0 || lonlat[1] <= 0 
				/*|| !CheckLonLat(lonlat[0], lonlat[1])*/); 
	}
	
	
	public void doAfterOnIntentCall(boolean receive, int type) {
//		Activity curActivity = MenuControlIF.Instance().GetCurrentActivity();
//		
//		if (curActivity == null) {
//			Log.v(TAG_INTENT_CALL, "MenuControlIF.GetCurActivity() null.");
//			return;
//		}
//		
//		switch (type) {
//		case INTENT_CALL_TYPE_CONTACTS:
//		case INTENT_CALL_TYPE_GOOGLE_MAP:
//		case INTENT_CALL_TYPE_POINT:
//			if (receive) {
//				if (curActivity.getClass().equals(ADT_Main_Map_Navigation.class)) {
//					((ADT_Main_Map_Navigation)curActivity).callIntentPoint();
//					
//				} else {
//					MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
//				}
//			} else {
//				Toast.makeText(NaviViewManager.GetViewManager(), "can't do search intent", Toast.LENGTH_SHORT).show();
//				UIC_IntentCommon.Instance().resetIntentCallType();
//			}
//			
//			
//			break;
//		default:
//			break;
//		}
	}
	
	public String getString() {
		return Arrays.toString(lonlatForExternalSearch)+", "+addressForExternalSearch+", "+Integer.toHexString(mIntentCallType);
	}
	
	public interface IntentCall{
		boolean onIntentCall(int type);
	}


}
