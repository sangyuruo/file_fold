package com.billionav.navi.naviscreen.debug;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.util.Log;

import com.billionav.jni.UIDebugJNI;
import com.billionav.jni.UIDebugJNI.DebugLogSettingItem;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;

public class ADT_Debug_LogSetting extends PrefenrenceActivityBase {
	
//	class iCheckBoxPreference extends CheckBoxPreference{
//		public iCheckBoxPreference(Context context) {
//			super(context);
//			// TODO Auto-generated constructor stub
//		}
//		public iCheckBoxPreference(Context context , AttributeSet attr){
//			super(context, attr);
//		}
//		int tag;
//		int getTag(){
//			return tag;
//		}
//		void setTag(int tag)
//		{
//			this.tag = tag;
//		}
//	}
	private CheckBoxPreference[] checkboxpreference;
//	private static final String[] titles = {
//			"Log Function", "Log OutputToFile", "Location Info", "Map Info", "Path Info",
//			"Guide Info", "Search Info", "VICS Info", "Highway Info", "UI Info",
//			"COM(RAS Info)", "Voice Info", "Voice Play Info", "APL Management Info", "APL System Info",
//			"Server Access Info", "Data Server Info", "Memory Info", "Memory Block Info", "Memory Usage Info",
//			"Memory Usage Display", "File Info", "Error Info", "Operation Info","Screen Info",
//			"Point Info", "VersionManager Info", "Authentication Info", "Disp Change Info", "Backup Memory Info",
//			"Shift Prco Memory", "Event Log", "Mark List", "SIP", "TTS Info",
//			"Symbol Info", "UnitControl Info", "MenuUI Log Info", "Map Performance Info", "Start Performance",
//			"Hybrid Performance Info", "ECO Log", "W3 Log Info", "Drive History Log Info", "Smart Loop Log Info",};
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		
		UIDebugJNI.reqDebugLogSettingState();
		
//		PreferenceScreen mRootPrefrence = getPreferenceManager().createPreferenceScreen(this);
//		setPreferenceScreen(mRootPrefrence);
//		
//		setTitle("Log Setting");
//		
//		for(int i=0; i<checkboxpreference.length; i++) {
//			checkboxpreference[i] = new CheckBoxPreference(this);
//			checkboxpreference[i].setTitle(titles[i]);
//			mRootPrefrence.addPreference(checkboxpreference[i]);
//		}
		
//		boolean isLogFuntionChecked = cLog.GetLogStatus();
//		checkboxpreference[0].setChecked(isLogFuntionChecked);
//		checkboxpreference[1].setChecked(cLog.GetLogOutputMedia() == jniAL_Log.AL_LOG_MEDIA_HDD);
//		for(int i=2; i<checkboxpreference.length; i++) {
//			checkboxpreference[i].setChecked(cLog.GetLogKindStatus(setKeys[i]));
//		}
		
//		setSubPerferenceEnable(isLogFuntionChecked);

//		checkboxpreference[0].setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				boolean value = (Boolean) newValue;
////				cLog.SetLogStatus(value);
//				setSubPerferenceEnable(value);
//				return true;
//			}
//		});
//		
//		checkboxpreference[1].setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				boolean value = (Boolean) newValue;
////				cLog.SetLogOutputMedia(value ? AL_LOG_MEDIA_HDD : AL_LOG_MEDIA_SIO);
////				cLog.SetLogKindStatus(AL_LOG_MEDIA_HDD, value);
//				return true;
//			}
//		});
		
//		for(int i=2; i<checkboxpreference.length; i++) {
//			checkboxpreference[i].setOnPreferenceChangeListener(new PreferenceChangeListener(setKeys[i]));
//		}
	}
	
	private void setSubPerferenceEnable(boolean enable) {
		for(int i=1; i<checkboxpreference.length; i++) {
			checkboxpreference[i].setEnabled(enable);
		}
	}
	
	private class PreferenceChangeListener implements OnPreferenceChangeListener{
		private final int key;
		private PreferenceChangeListener(int key){
			this.key = key;
		}
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			boolean value = (Boolean) newValue;
			UIDebugJNI.SetDevelopDebugOption(key, value);
			return true;
		}
		
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.UIC_DEBUG_RECV_DEVELOP_LOG_OPTIONS_STATES) {
			
			DebugLogSettingItem[] items = UIDebugJNI.getDebugLogSettingState();
			PreferenceScreen mRootPrefrence = getPreferenceManager().createPreferenceScreen(this);
			setPreferenceScreen(mRootPrefrence);
			CheckBoxPreference openLog = new CheckBoxPreference(this);
			openLog.setTitle("Open Log");
			openLog.setChecked(UIDebugJNI.isDevelopLogOpen());
			openLog.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					boolean value = (Boolean) newValue;
					UIDebugJNI.setDevelopDebugLogOutput(value);
					return true;
				}
			});
			mRootPrefrence.addPreference(openLog);
			
			
			setTitle("Log Setting");
			if(items != null && items.length > 0){
				checkboxpreference = new CheckBoxPreference[items.length + 2];
				for(int i=0; i<items.length; i++) {
					checkboxpreference[i] = new CheckBoxPreference(this);
					checkboxpreference[i].setTitle(items[i].logDispName);
					checkboxpreference[i].setChecked(items[i].selected);
					checkboxpreference[i].setOnPreferenceChangeListener(new PreferenceChangeListener(items[i].logID));
					mRootPrefrence.addPreference(checkboxpreference[i]);
				}
			}
			Log.d("test","aaaaaaaaaaaaaaaaaaa");
			return true;
		}
		return false;
	}
	
}
