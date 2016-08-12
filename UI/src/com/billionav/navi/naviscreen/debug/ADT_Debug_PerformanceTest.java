package com.billionav.navi.naviscreen.debug;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;

import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;

public class ADT_Debug_PerformanceTest extends PrefenrenceActivityBase {
	
	private PreferenceScreen mRootPrefrence;
	private CheckBoxPreference[] mainKindPreference;
	private final ArrayList<CheckBoxPreference[]> kindList = new ArrayList<CheckBoxPreference[]>();
	
	private static final int KIND_KEY_PARAMS = 1000;
	private static final int KEY_PLUS = 1;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		
		mRootPrefrence = getPreferenceManager().createPreferenceScreen(this);
		setPreferenceScreen(mRootPrefrence);
		
		setTitle("Performance Test");
		initPreference();
		
	}
	
	private void initPreference(){
//		int kindNum = jniPerformanceTest.GetPerformanceTestKindNum();
//		mainKindPreference = new CheckBoxPreference[kindNum];
//		for(int kindSeq = 0; kindSeq < kindNum; kindSeq++){
//			
//			mainKindPreference[kindSeq] = new CheckBoxPreference(this);
//			String kindName = jniPerformanceTest.GetPerformanceTestKindName(kindSeq);
//			
//			boolean isKindChecked = jniPerformanceTest.GetPerformanceTestKindStatus(kindSeq);
//			mainKindPreference[kindSeq].setOnPreferenceChangeListener(changeListener);
//			mainKindPreference[kindSeq].setKey(String.valueOf((kindSeq + KEY_PLUS) * KIND_KEY_PARAMS));
//			PreferenceCategory category = new PreferenceCategory(this);
//			category.setTitle(kindName);
//			mRootPrefrence.addPreference(category);
//			mRootPrefrence.addPreference(mainKindPreference[kindSeq]);
//			
//			int itemNum = jniPerformanceTest.GetPerformanceTestItemNum(kindSeq);
//			String allKindName = jniPerformanceTest.GetPerformanceTestItemName(kindSeq, itemNum);
//			mainKindPreference[kindSeq].setTitle(allKindName);
//			
//			CheckBoxPreference[] item = new CheckBoxPreference[itemNum];
//			for(int itemSeq = 0; itemSeq < itemNum; itemSeq++){
//				item[itemSeq] = new CheckBoxPreference(this);
//				String itemName = jniPerformanceTest.GetPerformanceTestItemName(kindSeq, itemSeq);
//				item[itemSeq].setTitle(itemName);
//				item[itemSeq].setChecked(jniPerformanceTest.GetPerformanceTestItemStatus(kindSeq, itemSeq));
//				item[itemSeq].setKey(String.valueOf(((kindSeq + KEY_PLUS) * KIND_KEY_PARAMS) + (itemSeq + KEY_PLUS)));
//				item[itemSeq].setOnPreferenceChangeListener(changeListener);
//				mRootPrefrence.addPreference(item[itemSeq]);
//			}
//			kindList.add(item);
//			
//			updateItemStatus(kindSeq,isKindChecked);
//			mainKindPreference[kindSeq].setChecked(isKindChecked);
//		}
//		
//		PreferenceCategory category = new PreferenceCategory(this);
//		category.setTitle("File Output");
//		mRootPrefrence.addPreference(category);
//		CheckBoxPreference fileOutput = new CheckBoxPreference(this);
//		fileOutput.setTitle("File Output");
//		fileOutput.setChecked(jniPerformanceTest.GetToFileStatus());
//		fileOutput.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniPerformanceTest.SetToFileStatus((Boolean)newValue);
//				return true;
//			}});
//		mRootPrefrence.addPreference(fileOutput);
	}
	
	private OnPreferenceChangeListener changeListener = new OnPreferenceChangeListener(){
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			
//			boolean isChecked = (Boolean)newValue;
//			
//			int key = Integer.valueOf(preference.getKey());
//			int kindNum = key/KIND_KEY_PARAMS;
//			int itemNum = key%KIND_KEY_PARAMS;
//			int realKindNum = kindNum - KEY_PLUS;
//			int realItemNum = itemNum - KEY_PLUS;
//			System.out.println("kind:"+realKindNum+",item:"+realItemNum+", is checked:"+isChecked);
//			if(itemNum > 0){
//				jniPerformanceTest.SetPerformanceTestItemStatus(realKindNum, realItemNum, isChecked);
//			}else if(itemNum == 0){
//				jniPerformanceTest.SetPerformanceTestKindStatus(realKindNum, isChecked);
//				updateItemStatus(realKindNum, isChecked);
//			}
//			
//			int kind = jniPerformanceTest.GetPerformanceTestKindNum();
//			for(int kindSeq = 0; kindSeq < kind; kindSeq++){
//				mainKindPreference[kindSeq].setChecked(jniPerformanceTest.GetPerformanceTestKindStatus(kindSeq));
//			}
			
			return true;
		}
		
	};
	
	private void updateItemStatus(int kindNum, boolean isChecked){
		CheckBoxPreference[] item = kindList.get(kindNum);
		if(item == null){
			return;
		}
		for (int itemNum = 0; itemNum < kindList.get(kindNum).length; itemNum++) {
			item[itemNum].setChecked(isChecked);
		}
	}

}
