package com.billionav.navi.naviscreen.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

import com.billionav.navi.component.basiccomponent.ListPreference;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.ui.R;

public class ADT_Setting_Cradle extends PrefenrenceActivityBase{

	private PreferenceScreen cradle_connect_setting = null;
	private CheckBoxPreference cradle_interna_sp_out = null;
	private ListPreference led_setting = null;
	private ListPreference asl_setting = null;
	private PreferenceScreen sensor_learning = null;
	private PreferenceScreen cradle_info = null;
//	private jniLocInfor mJniLocInfor = jniLocInfor.getLocInfor();
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_settings_cradle);
		setTitle("Cradle Setting");
		cradle_connect_setting = (PreferenceScreen) getPreferenceManager().findPreference("cradle_connect_setting");
		cradle_interna_sp_out = (CheckBoxPreference) getPreferenceManager().findPreference("cradle_interna_sp_out");
		led_setting = (ListPreference) getPreferenceManager().findPreference("led_setting");
		asl_setting = (ListPreference) getPreferenceManager().findPreference("asl_setting");
		sensor_learning = (PreferenceScreen) getPreferenceManager().findPreference("sensor_learning_initialize");
		cradle_info = (PreferenceScreen) getPreferenceManager().findPreference("cradle_info");
		
//		if(mJniLocInfor.GetCradleInsideSPOutState()==jniLocInfor.INALID){
//			cradle_interna_sp_out.setChecked(true);
//		}else if(mJniLocInfor.GetCradleInsideSPOutState()==jniLocInfor.STATE_OFF){
//			cradle_interna_sp_out.setChecked(false);
//		}else if(mJniLocInfor.GetCradleInsideSPOutState()==jniLocInfor.STATE_ON){
//			cradle_interna_sp_out.setChecked(true);
//		}else{
//			cradle_interna_sp_out.setChecked(true);
//		}
		led_setting.setEnties("off","RED","GREEN");
		asl_setting.setEnties("Off","Low","Mid","High");
		setLEDSelectedIndex();
     	setASLSelectedIndex();

		cradle_connect_setting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM);
				ForwardWinChange(com.billionav.navi.naviscreen.setting.ADT_Setting_Bluetooth.class);
				return true;
			}
		});
		
		cradle_interna_sp_out.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				int temp = mJniLocInfor.GetCradleInsideSPOutState();
//				if (temp == jniLocInfor.INALID) {
//					cradle_interna_sp_out.setChecked(true);
//					//show message or dialog
//					return false;
//				}
//				if (temp == jniLocInfor.STATE_OFF) {
//					// set jni SPOut on
//					if (mJniLocInfor.SetCradleInsideSPOutState(jniLocInfor.STATE_ON)) {
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//
////							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if (mJniLocInfor.GetCradleInsideSPOutState() != jniLocInfor.STATE_ON) {
//									cradle_interna_sp_out.setChecked(true);
//									//show dialog
//								}
//							}}, 300);
//					} else {
//						cradle_interna_sp_out.setChecked(false);
//						//show dialog
//					}
//				} else if (temp == jniLocInfor.STATE_ON){
//					// set jni SPOut off;
//					if (mJniLocInfor.SetCradleInsideSPOutState(jniLocInfor.STATE_OFF)) {
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//
////							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if (mJniLocInfor.GetCradleInsideSPOutState() != jniLocInfor.STATE_OFF) {
//									cradle_interna_sp_out.setChecked(true);
//									//show dialog
//								}
//							}}, 300);
//					} else {
//						cradle_interna_sp_out.setChecked(true);
//						//show dialog
//					}
//				} else {
//					cradle_interna_sp_out.setChecked(true);
//				}
				return false;
//
			}
		});
		led_setting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == 0){
//					if (mJniLocInfor.SetLedSettingValue(jniLocInfor.LED_SETTING_OFF)) {
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//
////							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if (mJniLocInfor.GetLedSettingValue() != jniLocInfor.LED_SETTING_OFF) {
//									//show dialog
//									setLEDSelectedIndex();
//								} else {
//								}
//							}}, 300);
//					} else {
//						//show dialog
					}

//				}else if((Integer)newValue == 1){
//					if (mJniLocInfor.SetLedSettingValue(jniLocInfor.LED_SETTING_RED)) {
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//
////							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if (mJniLocInfor.GetLedSettingValue() != jniLocInfor.LED_SETTING_RED) {
//									//show dialog
//									setLEDSelectedIndex();
//								} else {
//								}
//							}}, 300);
//					} else {
//						//show dialog
//					}

//				}else if((Integer)newValue == 2){
//					if (mJniLocInfor.SetLedSettingValue(jniLocInfor.LED_SETTING_GREEN)) {
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//
////							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if (mJniLocInfor.GetLedSettingValue() != jniLocInfor.LED_SETTING_GREEN) {
//									//show dialog
//									setLEDSelectedIndex();
//								} else {
//								}
//							}}, 300);
//					} else {
//						//show dialog
//					}
//
//				}else{
//					setLEDSelectedIndex();
//				}
				return false;
			}
		});
		asl_setting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//			if((Integer)newValue == 0){
//				if (mJniLocInfor.SetAslSettingValue(jniLocInfor.ASL_SETTING_OFF)) {
//					Handler handler = new Handler();
//					handler.postDelayed(new Runnable() {
//
////						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							if (mJniLocInfor.GetAslSettingValue() != jniLocInfor.ASL_SETTING_OFF) {
//								//show dialog
//								setASLSelectedIndex();
//							} 
//						}}, 300);
//				} else {
//					//show dialog
//				}
//
//			}else if((Integer)newValue == 1){
//				if (mJniLocInfor.SetAslSettingValue(jniLocInfor.ASL_SETTING_LOW)) {
//					Handler handler = new Handler();
//					handler.postDelayed(new Runnable() {
//
////						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							if (mJniLocInfor.GetAslSettingValue() != jniLocInfor.ASL_SETTING_LOW) {
//								//show dialog
//								setASLSelectedIndex();
//								
//							} 
//						}}, 300);
//				} else {
//					//show dialog
//				}
//
//			}else if((Integer)newValue == 2){
//				if (mJniLocInfor.SetAslSettingValue(jniLocInfor.ASL_SETTING_MID)) {
//					Handler handler = new Handler();
//					handler.postDelayed(new Runnable() {
//
////						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							if (mJniLocInfor.GetAslSettingValue() != jniLocInfor.ASL_SETTING_MID) {
//								//show dialog
//								setASLSelectedIndex();
//							} else {
//							}
//						}}, 300);
//				} else {
//					//show dialog
//				}
//
//			}else if((Integer)newValue == 3){
//				if (mJniLocInfor.SetAslSettingValue(jniLocInfor.ASL_SETTING_HIGH)) {
//					Handler handler = new Handler();
//					handler.postDelayed(new Runnable() {
//
////						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							if (mJniLocInfor.GetAslSettingValue() != jniLocInfor.ASL_SETTING_HIGH) {
//								//show dialog
//								setASLSelectedIndex();
//							} else {
//							}
//						}}, 300);
//				} else {
//					//show dialog
//				}
//
//			}else{
//				setASLSelectedIndex();
//			}
//				
//
			return true;
			}
		});
		sensor_learning.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				CustomDialog b = new CustomDialog(ADT_Setting_Cradle.this);
				b.setMessage("Initialize Sensor Learning. Is it OK?");
				b.setTitle("sensor_learning_initialize");
				b.setNegativeButton("Cancel", null);
				b.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
//								if (mJniLocInfor.ResetSnsLearning()) {
//									
//								} else {
//									//show dialog
//								CustomToast.showToast(ADT_Setting_Cradle.this, "Initialize Sensor Learning is failed!", 3000);
//								}

							}
						});
				b.show();
				return true;
			}
		});
		cradle_info.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM);
				ForwardWinChange(ADT_Setting_Cradle_Info.class);
				return true;
			}
		});
	}
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
//		if(CLocationListener.Instance().isCurrentExternalGpsOn()){
//		cradle_interna_sp_out.setEnabled(true);
//		led_setting.setEnabled(true);
//		asl_setting.setEnabled(true);
//		sensor_learning.setEnabled(true);
//		cradle_info.setEnabled(true);
//	}else{
//		cradle_interna_sp_out.setEnabled(false);
//		led_setting.setEnabled(false);
//		asl_setting.setEnabled(false);
//		sensor_learning.setEnabled(false);
//		cradle_info.setEnabled(false);
//	}
	}

	private void setASLSelectedIndex() {
//		int aslValue = mJniLocInfor.GetAslSettingValue();
//         switch(aslValue){
//     	case jniLocInfor.INALID:
//		case jniLocInfor.ASL_SETTING_OFF: {
//			asl_setting.seletedIndex(0);
//			break;
//		}
//		case jniLocInfor.ASL_SETTING_LOW: {
//			asl_setting.seletedIndex(1);
//			break;
//		}
//		case jniLocInfor.ASL_SETTING_MID: {
//			asl_setting.seletedIndex(2);
//			break;
//		}
//		case jniLocInfor.ASL_SETTING_HIGH: {
//			asl_setting.seletedIndex(3);
//			break;
//		}
//		default:
//			break;
//		}
	}


	private void setLEDSelectedIndex() {
//		int ledValue = mJniLocInfor.GetLedSettingValue();
//         switch(ledValue){
//         case jniLocInfor.LED_SETTING_OFF: {
//        	 led_setting.seletedIndex(0);
//				break;
//			}
////			case jniLocInfor.INALID:
////			case jniLocInfor.LED_SETTING_ECO: {
////				 led_setting.seletedIndex(1);
////				break;
////			}
//			case jniLocInfor.LED_SETTING_RED: {
//				 led_setting.seletedIndex(1);
//				break;
//			}
//			case jniLocInfor.LED_SETTING_GREEN: {
//				 led_setting.seletedIndex(2);
//				break;
//			}
//			default:
//				break;
//
//         }
	}
}
