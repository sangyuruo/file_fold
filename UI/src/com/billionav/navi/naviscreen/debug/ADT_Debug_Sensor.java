package com.billionav.navi.naviscreen.debug;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.billionav.jni.jniGpsSnsModule;
import com.billionav.ui.R;
import com.billionav.navi.component.DebugLayout;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.sensor.LocSnsListener;
import com.billionav.navi.uicommon.UIC_DebugCommon;

public class ADT_Debug_Sensor extends PrefenrenceActivityBase {

//	private CheckBoxPreference sensorGyro;
	private CheckBoxPreference sensorRawDataSave;
	private CheckBoxPreference sensorCardDataSave;
	private CheckBoxPreference sensorLogPlay;
//	private CheckBoxPreference sensorSnsFunction;
	private CheckBoxPreference sensorStopStateWatch;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_debug_senor);
		
		setTitle("Sensor Debug");
		
		findViews();
		
		initialize();
		
		setListeners();
	}

	private void setListeners() {
//		sensorGyro.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				UIC_DebugCommon.Instance().setOpenSensorGyro((Boolean)newValue);
//				if((Boolean)newValue) {
//					LocSnsListener.instance().OpenSensorGyro();
//				} else {
//					LocSnsListener.instance().CloseSensorGyro();
//				}
//				return true;
//			}
//		});
		sensorRawDataSave.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				jniGpsSnsModule.getGpsSnsModule().SetSaveRawSensorLog((Boolean)newValue);
				return true;
			}
		});
		sensorCardDataSave.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				jniGpsSnsModule.getGpsSnsModule().SetCardSnsLog((Boolean)newValue);
				return true;
			}
		});
		sensorLogPlay.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if((Boolean)newValue) {
					jniGpsSnsModule.getGpsSnsModule().StartSnsLogPlay();
				} else {
					jniGpsSnsModule.getGpsSnsModule().StopSnsLogPlay();
				}
				return true;
			}
		});
		
//		sensorSnsFunction.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniGpsSnsModule.getGpsSnsModule().SetSnsFunction((Boolean)newValue);
//				return true;
//			}
//		});
		sensorStopStateWatch.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				jniGpsSnsModule.getGpsSnsModule().SetStopStateWatch((Boolean)newValue);
				DebugLayout.instance().setSensorStateWatch((Boolean)newValue);
				return true;
			}
		});
	}

	private void initialize() {
//		sensorGyro.setChecked(UIC_DebugCommon.Instance().isOpenSensorGyro());
		sensorRawDataSave.setChecked(jniGpsSnsModule.getGpsSnsModule().GetSaveRawSensorLog());
		sensorCardDataSave.setChecked(jniGpsSnsModule.getGpsSnsModule().GetCardSnsLog());
		sensorLogPlay.setChecked(jniGpsSnsModule.getGpsSnsModule().GetSnsLogState());
//		sensorSnsFunction.setChecked(jniGpsSnsModule.getGpsSnsModule().GetSnsFunction());
		sensorStopStateWatch.setChecked(jniGpsSnsModule.getGpsSnsModule().GetStopStateWatch());
	}

	private void findViews() {
//		sensorGyro = (CheckBoxPreference) findPreference("sensorGyro");
		sensorRawDataSave = (CheckBoxPreference) findPreference("sensorRawDataSave");
		sensorCardDataSave = (CheckBoxPreference) findPreference("sensorCardDataSave");
		sensorLogPlay = (CheckBoxPreference) findPreference("sensorLogPlay");
//		sensorSnsFunction = (CheckBoxPreference) findPreference("sensorSnsFunction");
		sensorStopStateWatch = (CheckBoxPreference) findPreference("sensorStopStateWatch");
		
	}
}
