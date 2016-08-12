//package com.billionav.navi.naviscreen.debug;
//
//import android.os.Bundle;
//import android.preference.CheckBoxPreference;
//import android.preference.EditTextPreference;
//import android.preference.ListPreference;
//import android.preference.Preference;
//import android.preference.Preference.OnPreferenceChangeListener;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Toast;
//
//import com.billionav.jni.jniSetupControl;
//import com.billionav.ui.R;
//import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
//
//public class ADT_Debug_MyActoActivity extends PrefenrenceActivityBase {
//	
//	private ListPreference tollwayListPref;
//	private CheckBoxPreference threeNumberCheckBoxPref;
//	private CheckBoxPreference RVCheckBoxPref;
//	private CheckBoxPreference minivan1BoxCheckBoxPref;
//	
//	private EditTextPreference LengthScreenPref;
//	private EditTextPreference WidthScreenPref;
//	private EditTextPreference HeightScreenPref;
//	
//	private final jniSetupControl setupCTL = new jniSetupControl();
//	@Override
//	protected void OnCreate(Bundle savedInstanceState) {
//		super.OnCreate(savedInstanceState);
//		addPreferencesFromResource(R.xml.adt_debug_my_auto);
//		setTitle("My Automotive");
//		
//		findViews();
//		
//		setListeners();
//		
//		initialize();
//	}
//
//	private void findViews() {
//		tollwayListPref =  (ListPreference) getPreferenceManager().findPreference("my_automotive");
//		threeNumberCheckBoxPref =  (CheckBoxPreference) getPreferenceManager().findPreference("three_number");
//		RVCheckBoxPref =  (CheckBoxPreference) getPreferenceManager().findPreference("RV_checkbox");
//		minivan1BoxCheckBoxPref =  (CheckBoxPreference) getPreferenceManager().findPreference("minivan1_box");
//		LengthScreenPref =  (EditTextPreference) getPreferenceManager().findPreference("length");
//		WidthScreenPref =  (EditTextPreference) getPreferenceManager().findPreference("width");
//		HeightScreenPref =  (EditTextPreference) getPreferenceManager().findPreference("height");
//		
//	}
//	
//	private void initialize() {
//		int carType = setupCTL.GetCarTypeStatus();
//		if(carType == jniSetupControl.STUPDM_CAR_TYPE_MEDIUM) {
//			tollwayListPref.setSummary("Medium sized car");
//			tollwayListPref.setValueIndex(0);
//		} else if(carType == jniSetupControl.STUPDM_CAR_TYPE_NORMAL) {
//			tollwayListPref.setSummary("Ordinary car");
//			tollwayListPref.setValueIndex(1);
//		} else if(carType == jniSetupControl.STUPDM_CAR_TYPE_LIGHT) {
//			tollwayListPref.setSummary("Light motor vehicle");
//			tollwayListPref.setValueIndex(2);
//		}
//		
//		LengthScreenPref.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
//		WidthScreenPref.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
//		HeightScreenPref.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
//		
//		int length = setupCTL.GetCarSizeStatus(jniSetupControl.CAR_SIZE_LENGTH)  ;
//		int width =  setupCTL.GetCarSizeStatus(jniSetupControl.CAR_SIZE_WIDTH)  ;
//		int height = setupCTL.GetCarSizeStatus(jniSetupControl.CAR_SIZE_HEIGHT) ;
//		LengthScreenPref.setSummary(length + "cm");
//		WidthScreenPref.setSummary(width + "cm");
//		HeightScreenPref.setSummary(height + "cm");
//
//	}
//
//	
//	private void setListeners() {
//		tollwayListPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
//
//			@Override
//			public boolean onPreferenceChange(Preference preference,
//					Object newValue) {
//				if (newValue.equals("medium")) {
//					setupCTL.SetCarTypeStatus(jniSetupControl.STUPDM_CAR_TYPE_MEDIUM);
//					preference.setSummary("Medium sized car");
//					
//				}
//				if (newValue.equals("normal")) {
//					setupCTL.SetCarTypeStatus(jniSetupControl.STUPDM_CAR_TYPE_NORMAL);
//					preference.setSummary("Ordinary car");
//					
//				}
//				if (newValue.equals("light")) {
//					setupCTL.SetCarTypeStatus(jniSetupControl.STUPDM_CAR_TYPE_LIGHT);
//					preference.setSummary("Light motor vehicle");
//					
//				}
//				return true;
//			}});
//		threeNumberCheckBoxPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				byte CarLimits_Status = setupCTL.GetCarLimitStatus();
//				
//				if((Boolean)newValue) {
//					setupCTL.SetCarLimitStatus((byte)(CarLimits_Status | 0x80));				
//				} else {
//					setupCTL.SetCarLimitStatus((byte)(CarLimits_Status & 0x7F));
//				}
//				return true;
//			}
//		});
//		
//		RVCheckBoxPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				byte CarLimits_Status = setupCTL.GetCarLimitStatus();
//				
//				if((Boolean)newValue) {
//					setupCTL.SetCarLimitStatus((byte)(CarLimits_Status | 0x40));				
//				} else {
//					setupCTL.SetCarLimitStatus((byte)(CarLimits_Status & 0xBF));
//				}
//				return true;
//			}
//		});
//		minivan1BoxCheckBoxPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				byte CarLimits_Status = setupCTL.GetCarLimitStatus();
//				
//				if((Boolean)newValue) {
//					setupCTL.SetCarLimitStatus((byte)(CarLimits_Status | 0x20));				
//				} else {
//					setupCTL.SetCarLimitStatus((byte)(CarLimits_Status & 0xDF));
//				}
//				return true;
//			}
//		});
//		
//		LengthScreenPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int value = Integer.parseInt(newValue.toString());
//				if(value>=100 && value<=999) {
//					setupCTL.SetCarSizeStatus(
//							jniSetupControl.CAR_SIZE_LENGTH, value);
//					preference.setSummary(newValue + "cm");
//				}else {
//					Toast.makeText(ADT_Debug_MyActoActivity.this, "number not legal", 2000).show();
//				}
//				return true;
//			}
//		});
//		WidthScreenPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int value = Integer.parseInt(newValue.toString());
//				if(value>=100 && value<=999) {
//					setupCTL.SetCarSizeStatus(
//							jniSetupControl.CAR_SIZE_WIDTH, value);
//					preference.setSummary(newValue + "cm");
//				}else {
//					Toast.makeText(ADT_Debug_MyActoActivity.this, "number not legal", 2000).show();
//				}
//				return true;
//			}
//		});
//		HeightScreenPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int value = Integer.parseInt(newValue.toString());
//				if(value>=100 && value<=999) {
//					setupCTL.SetCarSizeStatus(
//							jniSetupControl.CAR_SIZE_HEIGHT, value);
//					preference.setSummary(newValue + "cm");
//				}else {
//					Toast.makeText(ADT_Debug_MyActoActivity.this, "number not legal", 2000).show();
//				}
//				return true;
//			}
//		});
//	}
//
//
//}
