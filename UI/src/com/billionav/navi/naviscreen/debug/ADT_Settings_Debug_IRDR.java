package com.billionav.navi.naviscreen.debug;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.navi.component.basiccomponent.SeekBarPreference;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;

public class ADT_Settings_Debug_IRDR extends PrefenrenceActivityBase {
	
	//dr
//	private CheckBoxPreference drive_record;
	private ListPreference video_quality;
	private CheckBoxPreference auto_save;
	private CheckBoxPreference debugLog;
	private ListPreference frame_rate;
	private CheckBoxPreference record_calibrate;
	private CheckBoxPreference drvevt_lable;
	private ListPreference senseDigree;
	
	//ir
	private SeekBarPreference demo_run_speed;
	private CheckBoxPreference road_sign_detection;
	private SeekBarPreference ldw_threshold;
	private CheckBoxPreference IRReserve1;
	private CheckBoxPreference IRReserve2;
	private SeekBarPreference IRReserve3;
	private SeekBarPreference IRReserve4;
	private SeekBarPreference IRReserve5;
	
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_setting_irdr_debug);
		setTitle("DRIR Setting");
		findViews();
		initialize();
	}
	
	private void initialize() {
//		initCheckBoxPreference(drive_record, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_ONOFF, true);
		initListPreference(video_quality, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_RECDQUALITY, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_RECDQUALITY, true);
		initCheckBoxPreference(auto_save, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_GSNSFUC, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_GSNSFUC, true);
		initCheckBoxPreference(debugLog, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_LOG, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_LOG, true);
		initListPreference(frame_rate, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_FRAMERATE, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_FRAMERATE, true);
		initCheckBoxPreference(record_calibrate, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_RECORD_CALIBRATE, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_RECORDCALIBRATE, true);
		drvevt_lable.setChecked(SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_DRVEVT.getBoolean());
		drvevt_lable.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_DRVEVT.setValue((Boolean)newValue);
				return true;
			}
		});
		initListPreference(senseDigree, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_GSNSLEVEL, jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_GSNSLEVEL, true);
		initSeekBarPreference(demo_run_speed, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_DEMORUNSPPED, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_DEMORUNSPPED, false);
		initCheckBoxPreference(road_sign_detection, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_ROADSDET, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_ROADSDET, false);
		initSeekBarPreference(ldw_threshold, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_LDW, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_LDW, false);
		initCheckBoxPreference(IRReserve1, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE1, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE1, false);
		initCheckBoxPreference(IRReserve2, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE2, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE2, false);
		initSeekBarPreference(IRReserve3, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE3, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE3, false);
		initSeekBarPreference(IRReserve4, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE4, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE4, false);
		initSeekBarPreference(IRReserve5, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE5, jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE5, false);
	}

	private void findViews() {
		//dr
//		drive_record = (CheckBoxPreference) getPreferenceManager().findPreference("drive_record");
		video_quality = (ListPreference) getPreferenceManager().findPreference("video_quality");
		auto_save = (CheckBoxPreference) getPreferenceManager().findPreference("auto_save");
		debugLog = (CheckBoxPreference) getPreferenceManager().findPreference("debug_log");
		frame_rate = (ListPreference) getPreferenceManager().findPreference("frame_rate");
		record_calibrate = (CheckBoxPreference) getPreferenceManager().findPreference("record_calibrate");
		drvevt_lable = (CheckBoxPreference) getPreferenceManager().findPreference("drvevt_lable");
		senseDigree = (ListPreference) getPreferenceManager().findPreference("sensedegree");
		//ir
		demo_run_speed = (SeekBarPreference) getPreferenceManager().findPreference("demo_run_speed");
		demo_run_speed.setMin(-1);
		road_sign_detection = (CheckBoxPreference) getPreferenceManager().findPreference("road_sign_detection");
		ldw_threshold = (SeekBarPreference) getPreferenceManager().findPreference("ldw_threshold");
		IRReserve1 = (CheckBoxPreference) getPreferenceManager().findPreference("IRReserve1");
		IRReserve2 = (CheckBoxPreference) getPreferenceManager().findPreference("IRReserve2");
		IRReserve3 = (SeekBarPreference) getPreferenceManager().findPreference("IRReserve3");
		IRReserve4 = (SeekBarPreference) getPreferenceManager().findPreference("IRReserve4");
		IRReserve5 = (SeekBarPreference) getPreferenceManager().findPreference("IRReserve5");
	}
	
	private void initSeekBarPreference(SeekBarPreference prefrence, SharedPreferenceData key, int iSettingID, boolean dr){
		prefrence.setValue(key.getInt());
		prefrence.setSummary(""+key.getInt());
		prefrence.setPositiveButtonText(null);
		prefrence.setOnPreferenceChangeListener(new OnChangedLister(key, iSettingID, dr));
	}
	
	private void initCheckBoxPreference(CheckBoxPreference prefrence, SharedPreferenceData key, int iSettingID, boolean dr){
		prefrence.setChecked(key.getInt() == 1);
		prefrence.setOnPreferenceChangeListener(new OnChangedLister(key, iSettingID, dr));
	}
	
	private void initListPreference(ListPreference list, SharedPreferenceData key, int iSettingID, boolean dr) {
		list.setSummary(list.getEntries()[key.getInt()]);
		list.setValueIndex(key.getInt());
		list.setOnPreferenceChangeListener(new OnChangedLister(key, iSettingID, dr));
	}
	
	
	private static class OnChangedLister implements OnPreferenceChangeListener{
		private final SharedPreferenceData key; 
		private final int iSettingID; 
		private final boolean dr;
		
		public OnChangedLister(SharedPreferenceData key, int iSettingID,
				boolean dr) {
			super();
			this.key = key;
			this.iSettingID = iSettingID;
			this.dr = dr;
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			int value;
			if(preference instanceof ListPreference) {
				value = Integer.parseInt(newValue.toString());
				CharSequence text = ((ListPreference)preference).getEntries()[value];
				preference.setSummary(text);
			} else if(preference instanceof CheckBoxPreference){
				value = (Boolean)newValue ? 1 : 0;
			} else if(preference instanceof SeekBarPreference){
				value = Integer.parseInt(newValue.toString());
				preference.setSummary(""+value);
			} else {
				throw new RuntimeException();
			}
			
			key.setValue(value);
			if(dr) {
				jniDRIR_MainControl.SetDRDebugSetting(iSettingID, value);
			} else {
				jniDRIR_MainControl.SetIRDebugSetting(iSettingID, value);
			}
			jniDRIR_MainControl.SettingCommit();
			return true;
		}
		
	}
	

}
