package com.billionav.navi.naviscreen.debug;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.UILocationControlJNI;
import com.billionav.navi.app.AndroidNaviAPP;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.uicommon.UIC_DebugCommon;

public class ADT_Debug_LogRunSetting extends PrefenrenceActivityBase {
	private PreferenceScreen mRootPrefrence;
	private ListPreference fileSelect;
	private Preference logrunStart;
	
	private String ALBUMNAME = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + "/RW/SIM/";

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		mRootPrefrence = getPreferenceManager().createPreferenceScreen(this);
		setPreferenceScreen(mRootPrefrence);
		
		setTitle("LogRun Setting");
		
		initPreference();
		setListeners();
	}
	
	private void initPreference() {
		fileSelect = new ListPreference(this);
		mRootPrefrence.addPreference(fileSelect);
		fileSelect.setTitle("File Setting");
		fileSelect.setDialogTitle("File Setting");
		String fileName = UIC_DebugCommon.Instance().getFilename();
		fileSelect.setSummary("FileName:" + fileName);
		String[] fileList = getFileList();
		fileSelect.setEntries(fileList);
		fileSelect.setEntryValues(fileList);
		int index = Arrays.asList(fileList).indexOf(fileName);
		if(index >= 0) {
			fileSelect.setValueIndex(index);
		}
		
		logrunStart = new Preference(this);
		mRootPrefrence.addPreference(logrunStart);
		if (UIC_DebugCommon.Instance().isLogRunState()) {
			logrunStart.setTitle("LogRun Stop");
		} else {
			logrunStart.setTitle("LogRun Start");
		}

	}
	
	private void setListeners() {
		fileSelect.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				
				String fileName = newValue.toString();
				preference.setSummary("FileName:" + fileName);
				UIC_DebugCommon.Instance().setFilename(fileName);
				return true;
			}
		});
		
		logrunStart.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if(TextUtils.isEmpty(UIC_DebugCommon.Instance().getFilename())) {
					getDialogForNoFile().show();
				} else if(UIC_DebugCommon.Instance().isLogRunState()) {
					getDialogForStopLogRun().show();
				} else {
					getDialogForStartLogRun().show();
				}
				return true;
			}
		});
	}
	
	private String[] getFileList() {
		ArrayList<String> fileList = new ArrayList<String>();
		
		ALBUMNAME = getAbsolutePath();
		
		File file = new File(ALBUMNAME);
		if (file.exists()) {
			String[] files = file.list();
			for (int i = 0 ; i < files.length ; i++) {
				File subfile = new File(ALBUMNAME + files[i]);
				if (!subfile.isDirectory()) {
					fileList.add(subfile.getName());
				}
			}
		}
		
		return fileList.toArray(new String[0]);

	}
	
	private String getAbsolutePath() {
		return FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + getPath();
	}
	
	private String getPath() {
		return "RW/SIM/";
	}
	
	private AlertDialog getDialogForNoFile() {
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("caution")
				.setMessage("Read File Error!!")
				.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialoginterface, int i) {
						}
					}).create();
		dialog.setCanceledOnTouchOutside(false);
		return dialog;

	}
	
	private AlertDialog getDialogForStartLogRun() {
		return new AlertDialog.Builder(this).setTitle("LogRun Start")
			.setMessage("LogRun will start. Is it OK?")
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialoginterface, int i) {
//							jniVoicePlayIF.Instance().PlayBeep(
//									jniVoicePlayIF.BEEP_ID_TONE1);
							UILocationControlJNI jni = 	UILocationControlJNI.getInstance();
							if (jni.StartLogRun(0, getPath() + UIC_DebugCommon.Instance().getFilename())) {
								UIC_DebugCommon.Instance().setLogRunState(true);
								MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
							}
						}
					})
			.setNegativeButton("Cancel",	
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialoginterface, int i) {
							// do nothing
						}
					})
			.create();

		
	}
	
	private AlertDialog getDialogForStopLogRun() {
		return new AlertDialog.Builder(this).setTitle("LogRun Stop")
			.setMessage("LogRun will stop. Is it OK?")
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialoginterface, int i) {
							UILocationControlJNI jni = UILocationControlJNI.getInstance();
							if (jni.StopLogRun()) { 
								UIC_DebugCommon.Instance().setLogRunState(false);
								MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
							}
						}
					})
				.setNegativeButton("Cancel",	
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialoginterface, int i) {
//							jniVoicePlayIF.Instance().PlayBeep(
//									jniVoicePlayIF.BEEP_ID_TONE2);
							// do nothing
						}
					}).create();
		
	}

}
