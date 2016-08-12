package com.billionav.navi.naviscreen.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.billionav.navi.naviscreen.base.ActivityBase;

public class ADT_Debug_SIMInfo extends ActivityBase {
	private final String UNKNOWN = "Unknown";
	
	private ListView listView;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		
		setContentView(listView = new ListView(this));
		
		setTitle("SIM Info");
		
		getWindow().getDecorView().setBackgroundColor(Color.BLACK);
//		listView.setBackgroundColor(Color.BLACK);
		listView.setCacheColorHint(0);
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		fullList(list);
		SimpleAdapter adapter = new SimpleAdapter(this, list, 
				android.R.layout.simple_list_item_2, 
				new String[]{"title", "content"}, 
				new int[] { android.R.id.text1, android.R.id.text2 });
		listView.setAdapter(adapter);
	}
	
	private void fullList(List<Map<String, String>> list) {
		putItem(list, "Phone Type", Build.MODEL);
		
		String mNetworkCountryIso = getTelephonyManager().getNetworkCountryIso();
		putItem(list, "NetworkCountryIso", getContext(mNetworkCountryIso));

		String mNetworkOperator = getTelephonyManager().getNetworkOperator();
		putItem(list, "NetworkOperator", getContext(mNetworkOperator));
		
		String mNetworkOperatorName = getTelephonyManager().getNetworkOperatorName();
		putItem(list, "NetworkOperatorName", getContext(mNetworkOperatorName));
		
		int mNetworkType = getTelephonyManager().getNetworkType();
		putItem(list, "NetworkType", getNetWorkType(mNetworkType));

		int mPhoneType = getTelephonyManager().getPhoneType();
		putItem(list, "PhoneType", getPhoneType(mPhoneType));
		
		String networkRoaming = getTelephonyManager().isNetworkRoaming() ? "NetworkRoaming" : "No NetworkRoaming";
		putItem(list, "NetworkRoaming", networkRoaming);
		
		String mDeviceId = getTelephonyManager().getDeviceId();
		putItem(list, "DeviceId", getContext(mDeviceId));
		
		String mDeviceSoftwareVersion = getTelephonyManager().getDeviceSoftwareVersion();
		putItem(list, "DeviceSoftwareVersion", getContext(mDeviceSoftwareVersion));

		String mSubscriberId = getTelephonyManager().getSubscriberId();
		putItem(list, "SubscriberId", getContext(mSubscriberId));

	}
	
	private String getContext(String context) {
		return TextUtils.isEmpty(context) ? UNKNOWN : context;
	}
	
	private Map<String, String> getNewItem(List<Map<String, String>> list) {
		Map<String, String> map = new HashMap<String, String>();
		list.add(map);
		return map;
		
	}
	
	private void putItem(List<Map<String, String>> list, String title, String content) {
		Map<String, String> map = getNewItem(list);
		map.put("title", title);
		map.put("content", content);
	}
	
	private TelephonyManager getTelephonyManager() {
		return (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
	}
	
	private String getNetWorkType(int networkType) {
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "EVDO_0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "EVDO_A";
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "1xRTT";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		default:
			return UNKNOWN;
		}
	}
	
	private String getPhoneType(int mPhoneType) {
		switch (mPhoneType) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.PHONE_TYPE_GSM:
			return "GSM";
		case TelephonyManager.PHONE_TYPE_NONE:
			return "NONE";
		default:
			return "UNKNOWN";
		}

	}

}
