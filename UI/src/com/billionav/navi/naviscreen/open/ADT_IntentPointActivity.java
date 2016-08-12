package com.billionav.navi.naviscreen.open;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.dest.ADT_Top_Menu;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.uicommon.UIC_IntentCommon;


public class ADT_IntentPointActivity extends Activity {
	
	private static final String TAG = "IntentCall";
	private static String matchString = "\\d+\\.?\\d*,\\d+\\.?\\d*";  //match 1254.255,155.25
	private static final String INTENT_GOOGLE_MAP_PREFIX = "http://maps.google.com/maps?daddr=";
	private static final String INTENT_GOOGLE_MAP_PREFIX_PREFIX = "http://maps.google.co.jp/maps?";
	private static final String INTENT_GOOGLE_MAP_PARAMETER = "daddr";
	private static final String INTENT_GOOGLE_MAP_GEOCODE_PARAMETER = "geocode";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(NaviConstant.TAG_CLASS_CALL , " ADT_IntentPointActivity onCreate " );
		if(!isLaunch()) {
			Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Intent receiveIntent = getIntent();
		
		if ((receiveIntent != null) && (receiveIntent.getData() != null)) {
			Log.v(TAG, "Uri of Intent IntentPoint send: " + receiveIntent.getData().toString());
		}
		
		if (UIC_IntentCommon.Instance().getIntentCallType()
				!= UIC_IntentCommon.INTENT_CALL_TYPE_MARK) {
			finish();
			Log.v(TAG, "Intent Recall.");
			return;
		}
		
		setIntentType(receiveIntent.getData());
		
		setLonlat();
		
		if ((!isLaunch()) || isStarting()) {
			Log.v(TAG, "Apl is not launch or starting.");
			
			if (UIC_IntentCommon.Instance().getIntentCallType() != UIC_IntentCommon.INTENT_CALL_TYPE_GOOGLE_MAP) {
			}
		} else {
			sendTrigger2MenuControl(UIC_IntentCommon.Instance().getIntentCallType());
		}
		
		finish();
		
		System.out.println("111111111111111111 search="+UIC_IntentCommon.Instance().getString());
		
	}	
	
	private void setIntentType(Uri data) {
		if (data == null) {
			return;
		}
		
		if (data.toString().trim().startsWith(INTENT_GOOGLE_MAP_PREFIX)) {
			UIC_IntentCommon.Instance().setIntentCallType(
										UIC_IntentCommon.INTENT_CALL_TYPE_GOOGLE_MAP);
		} else if (data.toString().trim().startsWith(INTENT_GOOGLE_MAP_PREFIX_PREFIX)) {
			if(data.getQueryParameter(INTENT_GOOGLE_MAP_GEOCODE_PARAMETER) != null ||
					data.getQueryParameter(INTENT_GOOGLE_MAP_PARAMETER) != null){
				UIC_IntentCommon.Instance().setIntentCallType(
										UIC_IntentCommon.INTENT_CALL_TYPE_GOOGLE_MAP);
			} else {
				UIC_IntentCommon.Instance().setIntentCallType(
										UIC_IntentCommon.INTENT_CALL_TYPE_POINT);
			}
		} else {
			UIC_IntentCommon.Instance().setIntentCallType(
										UIC_IntentCommon.INTENT_CALL_TYPE_POINT);
		}
	}
	
	private void setLonlat() {
		long[] Lonlat = getLonlat();
		UIC_IntentCommon.Instance().setLonlatForExternalSearch(Lonlat[0], Lonlat[1]);
	}

	private String getLonLatFromGoogleMapIntent(Uri data) {
		if ((data == null) 
				|| ((!data.toString().trim().
										startsWith(INTENT_GOOGLE_MAP_PREFIX))
					&& !data.toString().trim().startsWith(INTENT_GOOGLE_MAP_PREFIX_PREFIX))) {
			return null;
		}
		
		String ret = null;
		if (data.toString().trim().startsWith(INTENT_GOOGLE_MAP_PREFIX)) {	
			ret = data.getQueryParameter(INTENT_GOOGLE_MAP_PARAMETER);
		} else if (data.toString().trim().startsWith(INTENT_GOOGLE_MAP_PREFIX_PREFIX)) {
			ret = data.getQueryParameter(INTENT_GOOGLE_MAP_GEOCODE_PARAMETER);
			if (ret != null) {
				ret = ret.substring(ret.indexOf(',')+1); //0,lon,lat
			} else {
				ret = data.getQueryParameter(INTENT_GOOGLE_MAP_PARAMETER);
			}
		}
		
//		String ret = data.getQueryParameter(INTENT_GOOGLE_MAP_PARAMETER);
		Log.i(TAG, "ret = " + ret);
		if (ret == null || "".equals(ret)) {
			return null;
		}
		
		return getGeoString(ret);
	}
	
	private long[] getLonlat() {
		String geoString = getGeoString();
		if (geoString == null) {
			return new long[]{-1, -1};
		}
		String[] lonlat = geoString.split(",");
		
		if(lonlat.length != 2){
			Log.v(TAG, "latitude or longitude format wrong, geoString = "+geoString);
			return new long[]{-1, -1};
		}
		
		try{
			double latitude = Double.parseDouble(lonlat[0]);
			double longitude = Double.parseDouble(lonlat[1]);
			
			if(Math.abs(latitude)>90 || Math.abs(longitude)>180){
				Log.v(TAG, "not satisfy |latitude|<90 or |longitude|<180, latitude="+lonlat[0]+", longitude="+lonlat[1]);
				return new long[]{-1, -1};
			}
			
			return translateLonlatFromWGS2TKY(longitude, latitude);
		} catch(NumberFormatException e) {
			Log.v(TAG, "latitude or longitude format wrong, latitude="+lonlat[0]+", longitude="+lonlat[1]);
			return new long[]{-1, -1};
		}
	}

	/**
	 * 
	 * @return geo string; return null if data error
	 */
	private String getGeoString() {
		Uri uri = getIntent().getData();
		
		if (UIC_IntentCommon.Instance().getIntentCallType() 
				== UIC_IntentCommon.INTENT_CALL_TYPE_GOOGLE_MAP) {
			return getLonLatFromGoogleMapIntent(uri);
		}
		
		String scheme = uri.getScheme();
		String schemeSpecificPart = uri.getSchemeSpecificPart();
		
		if("geo".equals(scheme) || "point".equals(scheme)) {
			String queryString = getQueryParameter(schemeSpecificPart, "q");
			UIC_IntentCommon.Instance().setAddressForExternalSearch(doAddress(queryString));
			return schemeSpecificPart.indexOf("?")>0 ? schemeSpecificPart.substring(0, schemeSpecificPart.indexOf("?")) : schemeSpecificPart;
		} else if("http".equals(scheme)) {
			String queryString = uri.getQueryParameter("q");
			if (queryString == null || "".equals(queryString)) {
				UIC_IntentCommon.Instance().setAddressForExternalSearch(null);
				return null;
			}
			UIC_IntentCommon.Instance().setAddressForExternalSearch(getAddress(queryString));
			return getGeoString(queryString);
		} else {
			Toast.makeText(this, "not found this case, scheme = "+scheme, Toast.LENGTH_LONG).show();
			UIC_IntentCommon.Instance().setAddressForExternalSearch(null);
			return null;
		}
	}
	
	private String doAddress(String s) {
		s = s.replaceFirst(matchString, "");
		s = s.replaceFirst(matchString, "");
		s = s.trim();
		if(s.startsWith("(") && s.endsWith(")")){
			s = s.substring(1, s.length()-1);
		}
		
		return s;
		
	}
	
	private String getQueryParameter(String schemeSpecificPart, String key){
		if(schemeSpecificPart==null || key==null){
			return null;
		}
		int index = schemeSpecificPart.indexOf("?");
		if(index<0){
			return null;
		}
		String parameterStr = schemeSpecificPart.substring(index+1, schemeSpecificPart.length());
		String[] parameters = parameterStr.split("&");
		for(String p: parameters){
			String[] pair = p.split("=");
			if(pair.length == 2 && pair[0].trim().equals(key)){
				return pair[1];
			}
		}
		return null;
	}
	
	private static String getGeoString(String str){
		Pattern p = Pattern.compile(matchString, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		
		if(m.find()){
			Log.i(TAG, "m.group() = " + m.group());
			return m.group();
		}
		
		return null;
	}
	
	private static String getAddress(String str){
		
		String geo = getGeoString(str);
		
		if(geo == null){
			return null;
		}
		
		String s = str.replaceFirst(matchString, "");
		s = s.replace("loc:"+geo, "");
		s = s.replace("@"+geo, "");
		s = s.replace(geo+"+", "");
		s = s.replace(geo, "");
		s = s.trim();
		
		if(s.startsWith("(") && s.endsWith(")")){
			s = s.substring(1, s.length()-1);
		}
		if(s.startsWith("\"") && s.endsWith("\"")){
			s = s.substring(1, s.length()-1);
		}
		
		s = s.replace("+", " ");
		s = s.replace("%20", " ");
		
		return s;
	}
	
	private long[] translateLonlatFromWGS2TKY(double lon, double lat) {
		return new long[]{(long)(lon*256*3600), (long)(lat*256*3600)};
	}

	private void sendTrigger2MenuControl(int intentType) {
		NSTriggerInfo triggerInfo = new NSTriggerInfo();
		triggerInfo.SetTriggerID(NSTriggerID.UIC_MN_TRG_INTENT_CALL);
		triggerInfo.SetlParam1(intentType);
		MenuControlIF.Instance().TriggerForScreen(triggerInfo);
	}
	
	
	public static boolean isLaunch() {
		return (MenuControlIF.Instance() != null);
	}

	
	public static boolean isStarting(){
		return isLaunch() 
		&& !MenuControlIF.Instance().SearchWinscape(ADT_Main_Map_Navigation.class) 
		&& !MenuControlIF.Instance().GetCurrentActivity().getClass().equals(ADT_Main_Map_Navigation.class)
		&& !MenuControlIF.Instance().SearchWinscape(ADT_Top_Menu.class) 
		&& !MenuControlIF.Instance().GetCurrentActivity().getClass().equals(ADT_Top_Menu.class);
	}


	
}

