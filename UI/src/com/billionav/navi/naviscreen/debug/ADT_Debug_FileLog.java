package com.billionav.navi.naviscreen.debug;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.billionav.jni.UIDebugJNI;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.ui.R;

public class ADT_Debug_FileLog extends PrefenrenceActivityBase {
	
	private static boolean isRecord = false;

	private CheckBoxPreference record;
	private CheckBoxPreference replay;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.adt_debug_filelog);
		record = (CheckBoxPreference) findPreference("record");
		replay = (CheckBoxPreference) findPreference("replay");
		record.setChecked(isRecord);
		
		record.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean newstatus = (Boolean)newValue;
				if(newstatus) {
					UIDebugJNI.reqRecordStart();
					isRecord = true;
				} else {
					UIDebugJNI.reqRecordStop();
					isRecord = false;
				}
				return true;
			}
		});
		
		replay.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean newstatus = (Boolean)newValue;
				if(newstatus) {
					UIDebugJNI.reqGetPlayInfo();
					showProgressDialog();
				} else {
					UIDebugJNI.reqStopReplay();
				}
				
				return true;
			}
		});
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.UIC_DEBUG_RECV_LOG_STATUS) {
			dismissProgressDialog();
			if(cTriggerInfo.m_lParam1 == UIDebugJNI.LOG_STATUS_PLAY_INFO){
				UIDebugJNI.reqStartReplay(cTriggerInfo.m_lParam2, cTriggerInfo.m_lParam3);
			} else if(cTriggerInfo.m_lParam1 == UIDebugJNI.LOG_STATUS_WRITE_ERROR) {
				UIDebugJNI.reqRecordStop();
			}
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	
	private CProgressDialog dialog;
	private void showProgressDialog() {
		if(dialog == null) {
			dialog = new CProgressDialog(this);
			dialog.setCancelable(false);
		}
		
		dialog.show();
	}
	
	private void dismissProgressDialog(){
		if(dialog != null) {
			dialog.dismiss();
		}
	}
	
	
}
