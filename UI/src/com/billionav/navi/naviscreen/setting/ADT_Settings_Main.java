package com.billionav.navi.naviscreen.setting;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.AbsListView;
import android.widget.Toast;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIBaseConnJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.navi.component.basiccomponent.ListPreference;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.navi.gps.LocGpsListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Agreement;
import com.billionav.navi.naviscreen.auth.ADT_Auth_ChangePassword;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Lience;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.naviscreen.debug.ADT_Debug_MainMenu;
import com.billionav.navi.naviscreen.debug.ADT_Debug_Sensor;
import com.billionav.navi.naviscreen.debug.ADT_Settings_Debug_IRDR;
import com.billionav.navi.naviscreen.open.ADT_Opening_Disclaimer;
import com.billionav.navi.naviscreen.setting.DebugView.DebugOnListener;
import com.billionav.navi.naviscreen.version.ADT_Version_CheckVersionActivity;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.system.AplRuntime;
import com.billionav.navi.uicommon.UIC_SystemCommon;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.navi.uitools.SettingHelper;
import com.billionav.navi.uitools.SetupOptionValue;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;

public class ADT_Settings_Main extends PrefenrenceActivityBase {
	 
//	 private ListPreference voiceTip = null;
//	 private PreferenceScreen schedule = null;
	 private CheckBoxPreference cameraTip = null;
	 private ListPreference scheduletime = null;
	 private ListPreference mapColor = null;
	 private ListPreference mapFontSize = null;
	 
//	 private ListPreference showDistance = null;
//	 private ListPreference crashTime = null;
//	 private ListPreference carHighSet = null;

//	 private ListPreference videoMode = null;
//	 private ListPreference videoSaveMode = null;
//	 private ListPreference videoSpace = null;
//	 private CheckBoxPreference autoUpload = null;
//	 private CheckBoxPreference record = null;
	 
//	 private ListPreference languageSet = null;
//	 private CheckBoxPreference myFavorite = null;
//	 private CheckBoxPreference myHistory = null;
	 
	 private PreferenceScreen account = null;
	 private PreferenceScreen passWord = null;
//	 private PreferenceScreen moreInfo = null;
	 
	 private PreferenceScreen versionInfo = null;
	 private PreferenceScreen disclaimer = null;
	 private PreferenceScreen serviceInfo = null;
	 private PreferenceScreen licence = null;
	 private PreferenceScreen clearBuffer = null;
//	 private PreferenceScreen defaultSet = null;
	 
	 //cardle
	 private ListPreference gpsSetting = null;
	 
	 //HUD
	 private CheckBoxPreference poi = null;
	 private CheckBoxPreference trafficInfo = null;
	 private CheckBoxPreference trafficRestrict = null;
	 private CheckBoxPreference speedCamera = null;
	 private CheckBoxPreference timeFormat = null;
	 private PreferenceScreen logoMark = null;

	 private PreferenceScreen bluetooth = null;
//	 private BluetoothListPreference switchGps = null;
	 private PreferenceScreen cradleSet = null;
	 
	 private PreferenceScreen debug = null;
	 private PreferenceScreen irdrDebug = null;
	 private PreferenceScreen sensorDebug = null;
	 private PreferenceScreen debugOff = null;
	 
	 private PreferenceScreen rootPreference;
	 
//	 private PreferenceCategory DrivingSafe;
//	 private PreferenceCategory DrivingRecord;

	 private PreferenceCategory accountsynchronous;
	 private PreferenceCategory help;
	 private PreferenceCategory cradeCategory;
	 private PreferenceCategory debugCategory;
	 private PreferenceCategory hudCategory;
	 
	 
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setTitle(R.string.STR_MM_06_01_01_01);
		addPreferencesFromResource(R.xml.adt_settings_main);
		findViews();	
		setMapNavigation();	    
	    setDrivingSafe();
        setDriveRecord();
        setHUD();
        setAboutLanguage();
 		setAboutAccount();	
 		setOthers();	
 		setAboutDebug();
		setDebug();
		setDefaultvalue();

//		((PreferenceScreen) getPreferenceManager().findPreference(
//				"rootPreference")).removePreference(DrivingSafe);
//		((PreferenceScreen) getPreferenceManager().findPreference(
//				"rootPreference")).removePreference(DrivingRecord);
		debugCategory.removePreference(irdrDebug);
		String edition = SystemTools.getApkEdition(); 
		if(edition == null || !edition.equals(SystemTools.EDITION_CRADLE)) {
			rootPreference.removePreference(cradeCategory);
		}
		
		if(edition == null || !edition.equals(SystemTools.EDITION_HUD)) {
			rootPreference.removePreference(hudCategory);
		}
		if(SystemTools.getApkEdition().equals(SystemTools.EDITION_LUXGEN)){
			rootPreference.removePreference(accountsynchronous);
			help.removePreference(licence);
		}
	}

	private void setHUD() {
//		
//		poi.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				logoMark.setEnabled((Boolean)newValue);
//				SharedPreferenceData.setValue(SharedPreferenceData.HUD_POI, (Boolean)newValue);
//				UIC_SystemCommon.setHUD();
//				return true;
//			}
//		});
//		
//		trafficInfo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				
//				SharedPreferenceData.setValue(SharedPreferenceData.HUD_TRAFFIC_INFO, (Boolean)newValue);
//				UIC_SystemCommon.setHUD();
//				return true;
//			}
//		});
//		
//		trafficRestrict.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				
//				SharedPreferenceData.setValue(SharedPreferenceData.HUD_TRAFFIC_RESTRICT, (Boolean)newValue);
//				UIC_SystemCommon.setHUD();
//				return true;
//			}
//		});
//		
//		speedCamera.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				
//				SharedPreferenceData.setValue(SharedPreferenceData.HUD_SPEEDCAMER, (Boolean)newValue);
//				UIC_SystemCommon.setHUD();
//				return true;
//			}
//		});
//		
//		timeFormat.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				
//				SharedPreferenceData.setValue(SharedPreferenceData.HUD_TIME_24, (Boolean)newValue);
//				UIC_SystemCommon.setHUD();
//				return true;
//			}
//		});
//		
//		logoMark.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().ForwardWinChange(ADT_Setting_HUD_LogoMark.class);
//				return true;
//			}
//		});
//		
	}

	private void setOtherClickEvent() {
		
		if (UserControl_ManagerIF.Instance().HasLogin()&&UserControl_ManagerIF.Instance().GetStoredUserInfo()!=null) {
			UserControl_UserInfo userInfo = UserControl_ManagerIF.Instance().GetStoredUserInfo();
			account.setTitle(R.string.STR_MM_06_02_02_30);
			if(userInfo.m_strNickName.length()>0){
				account.setSummary(userInfo.m_strNickName);
			}else{
				account.setSummary("");
			}
			account.setEnabled(true);
			passWord.setEnabled(true);

		} else {
			if(UserControl_ManagerIF.Instance().isLogging()){
				account.setSummary(R.string.STR_MM_09_01_01_14);
				account.setEnabled(false);
			}else {
				account.setSummary(R.string.STR_MM_06_02_02_39);
				account.setEnabled(true);
			}
			passWord.setEnabled(false);
		}
	}
	
	private void setAboutDebug() {
//		bluetooth.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO Auto-generated method stub
//				MenuControlIF.Instance().ForwardWinChange(ADT_Setting_Bluetooth.class);
//				return true;
//			}
//		});
//		cradleSet.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO Auto-generated method stub
//				MenuControlIF.Instance().ForwardWinChange(ADT_Setting_Cradle.class);
//				return true;
//			}
//		});
 		
		debug.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_MainMenu.class);
				return true;
			}
		});

		irdrDebug.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				MenuControlIF.Instance().ForwardWinChange(ADT_Settings_Debug_IRDR.class);
				return true;
			}
		});
//		sensorDebug.setEnabled(LocSnsListener.instance().IsSensorOK());
		sensorDebug.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						// TODO Auto-generated method stub
						MenuControlIF.Instance().ForwardWinChange(ADT_Debug_Sensor.class);
						return true;
					}
				});
		debugOff.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				offDebugItem();
				return true;
			}
		});
	}

	private void setOthers() {
		versionInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
//				CustomDialog dialog = new CustomDialog(ADT_Settings_Main.this);
//				dialog.setTitle(R.string.STR_MM_06_01_01_92);
//				String temp_mapInfo = getString(R.string.STR_MM_06_01_01_149);
//				dialog.setMessage(getString(R.string.STR_MM_06_01_01_131)+SystemTools.getVersionString()+"\n"+temp_mapInfo);
//				dialog.setNegativeButton(R.string.STR_COM_001, null);
//				dialog.show();
				ForwardWinChange(ADT_Version_CheckVersionActivity.class);
				return true;
			}
		});
		disclaimer.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				ForwardWinChange(ADT_Opening_Disclaimer.class);
				return true;
			}
		});
		serviceInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				ForwardWinChange(ADT_Auth_Agreement.class);
				return true;
			}
		});
		
		licence.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				ForwardWinChange(ADT_Auth_Lience.class);
				return true;
			}
		});
		
		clearBuffer.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				UISearchControlJNI.Instance().ReqDelInputHistory(-1);
				CustomToast.showToast(ADT_Settings_Main.this,getString(R.string.STR_MM_06_01_01_167),
						Toast.LENGTH_SHORT);
				return true;
			}
		});
/*		defaultSet.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				showRecoverDialog();	
				return true;
			}
		});*/
	}

	private void setAboutAccount() {
//		myFavorite.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_FAVORITES));
// 		myFavorite.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				if((Boolean)newValue){
//					jniSetupControl.setOn(jniSetupControl.STUPDM_FAVORITES);
//				}else{
//					jniSetupControl.setOff(jniSetupControl.STUPDM_FAVORITES);
//				}
//				return true;
//			}
//		});
// 		myHistory.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_SEARCH_HISTORY));
// 		myHistory.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				if((Boolean)newValue){
//					jniSetupControl.setOn(jniSetupControl.STUPDM_SEARCH_HISTORY);
//				}else{
//					jniSetupControl.setOff(jniSetupControl.STUPDM_SEARCH_HISTORY);
//				}
//				return true;
//			}
//		});
		passWord.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				ForwardWinChange(ADT_Auth_ChangePassword.class);
				return true;
			}
		});
		 account.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub

				if(UserControl_ManagerIF.Instance().HasLogin()){
					if(!isNetEnable()){
						return true;
					}
					showLogOutDialog();
				}else{
					ForwardWinChange(ADT_Auth_Login.class);
				}
				return true;
			}
		});
// 		moreInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO Auto-generated method stub
//				ForwardKeepDepthWinChange(ADT_Settings_AccountSynchronous_More.class);
//				return true;
//			}
//		});
//		 setOtherClickEvent();
	}
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		 setOtherClickEvent();
	}

	private  boolean isNetEnable() {
		boolean netDisconntect = (PConnectReceiver.getConnectType() != PConnectReceiver.CONNECT_TYPE_NONE);
		if(!netDisconntect) {
			CustomToast.showToast(this, R.string.STR_MM_01_01_01_13, 2000);
			return false;
		}
		return true;
	}
	
	private void setAboutLanguage(){
//		languageSet.setEnties(R.string.STR_MM_06_01_01_128,R.string.STR_MM_06_01_01_129,R.string.STR_MM_06_01_01_130);
//		languageSet.seletedIndex(0);
//		if(jniSetupControl.get(jniSetupControl.STUPDM_MENU_LANGUAGE)==0){
//			languageSet.seletedIndex(1);
//		}else if(jniSetupControl.get(jniSetupControl.STUPDM_MENU_LANGUAGE)==1){
//			languageSet.seletedIndex(0);
//		}else if(jniSetupControl.get(jniSetupControl.STUPDM_MENU_LANGUAGE)==2){
//			languageSet.seletedIndex(2);
//		}
//		languageSet.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				if((Integer)newValue == 0){
//					jniSetupControl.set(jniSetupControl.STUPDM_MENU_LANGUAGE, jniSetupControl.STUPDM_MENU_LANGUAGE_CHINESE);
////					jniVoicePlayIF.Instance().changeLanguage(jniVoicePlayIF.VP_LANGUAGE_CHN_MANDARIN);
//				}else if((Integer)newValue == 1){
//					jniSetupControl.set(jniSetupControl.STUPDM_MENU_LANGUAGE, jniSetupControl.STUPDM_MENU_LANGUAGE_ENGLISH);	
////					jniVoicePlayIF.Instance().changeLanguage(jniVoicePlayIF.VP_LANGUAGE_CHN_ENGLISH);
//				}else if((Integer)newValue == 2){
//					jniSetupControl.set(jniSetupControl.STUPDM_MENU_LANGUAGE, jniSetupControl.STUPDM_MENU_LANGUAGE_JAPANESE);
////					jniVoicePlayIF.Instance().changeLanguage(jniVoicePlayIF.VP_LANGUAGE_JPN_JAPANESE);
//				}
//				AndroidNaviAPP.getInstance().setLanguage();
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_DEFAULT);
//				ForwardKeepDepthWinChange(ADT_Settings_Main.class);
//				return true;
//			}
//		});
	}
	private void setDriveRecord() {
/*		videoMode.setEnties(R.string.STR_MM_06_01_01_49,R.string.STR_MM_06_01_01_50);
 
         videoMode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue+1 == jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_HQ){
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_RECMODE, jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_HQ);
					SharedPreferenceData.DRIR_SETTING_DRID_RECMODE.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_HQ);
				}else if((Integer)newValue+1 == jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_STD){
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_RECMODE, jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_STD);
					SharedPreferenceData.DRIR_SETTING_DRID_RECMODE.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_STD);
				}else if((Integer)newValue+1 == jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_LT){
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_RECMODE, jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_LT);
					SharedPreferenceData.DRIR_SETTING_DRID_RECMODE.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_LT);
				}
				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});
         
         videoSaveMode.setEnties(R.string.STR_MM_06_01_01_111,R.string.STR_MM_06_01_01_110);
        
         videoSaveMode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == jniDRIR_MainControl.DRIR_SETTING_DR_SDRECMODE_OVERWRITE){
 					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDRECMODE, jniDRIR_MainControl.DRIR_SETTING_DR_SDRECMODE_OVERWRITE);
 				}else if((Integer)newValue == jniDRIR_MainControl.DRIR_SETTING_DR_SDRECMODE_STOP){
 					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDRECMODE, jniDRIR_MainControl.DRIR_SETTING_DR_SDRECMODE_STOP);
 				}
 				SharedPreferenceData.DRIR_SETTING_DRID_SDRECMODE.setValue((Integer)newValue);
 				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});

         videoSpace.setEnties(R.string.STR_MM_06_01_01_113,R.string.STR_MM_06_01_01_114,R.string.STR_MM_06_01_01_115,R.string.STR_MM_06_01_01_116,R.string.STR_MM_06_01_01_118);
        
 		videoSpace.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				switch((Integer)newValue){
				case 0:
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDCAPACITY, jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP1);
					SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP1);
					break;
				case 1:
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDCAPACITY, jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP2);
					SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP2);
					break;
				case 2:
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDCAPACITY, jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP3);
					SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP3);
					break;
				case 3:
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDCAPACITY, jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP4);
					SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_SDCAP4);
					break;
				case 4:
				default:
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDCAPACITY, jniDRIR_MainControl.DRIR_SETTING_DR_MAX_SDCAPACITY);
					SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_MAX_SDCAPACITY);
					break;
				}
				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});
 		
 		autoUpload.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Boolean)newValue){
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_UPLOADFUC, jniDRIR_MainControl.DRIR_SETTING_DR_UPLOAD_ON);
					SharedPreferenceData.DRIR_SETTING_DRID_UPLOADFUC.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_UPLOAD_ON);
					}else{
						jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_UPLOADFUC, jniDRIR_MainControl.DRIR_SETTING_DR_UPLOAD_OFF);
						SharedPreferenceData.DRIR_SETTING_DRID_UPLOADFUC.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_UPLOAD_OFF);
					}
				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});
 		
 		record.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Boolean)newValue){
					jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_ONOFF, jniDRIR_MainControl.DRIR_SETTING_DR_ON);;
					SharedPreferenceData.DRIR_SETTING_DRID_ONOFF.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_ON);
					}else{
						jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_ONOFF, jniDRIR_MainControl.DRIR_SETTING_DR_OFF);
						SharedPreferenceData.DRIR_SETTING_DRID_ONOFF.setValue(jniDRIR_MainControl.DRIR_SETTING_DR_OFF);
					}
				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});*/
	}

	private void setDrivingSafe() {
	/*	showDistance.setEnties(R.string.STR_MM_06_01_01_103,R.string.STR_MM_06_01_01_104);
	    showDistance.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_CAR){
					jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_FRONTCARINFO, jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_CAR);
					SharedPreferenceData.DRIR_SETTING_IRID_FRONTCARINFO.setValue(jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_CAR);
				}else if((Integer)newValue == jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_TIME){
					jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_FRONTCARINFO, jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_TIME);
					SharedPreferenceData.DRIR_SETTING_IRID_FRONTCARINFO.setValue(jniDRIR_MainControl.DRIR_SETTING_IR_FRONTCARINFO_TIME);
				}
	              jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});
	  
	     crashTime.setEnties(R.string.STR_MM_06_01_01_25,R.string.STR_MM_06_01_01_26,R.string.STR_MM_06_01_01_27);
         crashTime.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_CRASHTIMEALARM, (Integer)newValue+1);
				SharedPreferenceData.DRIR_SETTING_IRID_CRASHTIMEALARM.setValue((Integer)newValue+1);
				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});
         carHighSet.setEnties(R.string.STR_MM_06_01_01_107,R.string.STR_MM_06_01_01_108);
         carHighSet.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_NORMAL){
					jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_CARMODEL, jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_NORMAL);
					SharedPreferenceData.DRIR_SETTING_IRID_CARMODEL.setValue(jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_NORMAL);
				}else if((Integer)newValue == jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_SUV){
					jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_CARMODEL, jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_SUV);
					SharedPreferenceData.DRIR_SETTING_IRID_CARMODEL.setValue(jniDRIR_MainControl.DRIR_SETTING_IR_CARMODE_SUV);
				}
				jniDRIR_MainControl.SettingCommit();
				return true;
			}
		});*/
	}
	
	
   private void showRecoverDialog() {
		CustomDialog b = new CustomDialog(this);
		b.setMessage(R.string.STR_MM_06_02_02_29);
		b.setTitle(R.string.STR_MM_06_02_02_28);
		b.setNegativeButton(R.string.STR_COM_001, null);
		b.setPositiveButton(R.string.STR_COM_003,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
//							jniSetupControl.getInstance().RestoreFactory();
						SharedPreferenceData.setDefaultValues();
						setDefaultvalue();
						if (SystemTools.isCH()) {
							DRIRAppMain.setDRIRValue();
						}
						resetValues();
						
					}
				});
		b.show();
	}
	protected void resetValues() {
		AplRuntime.Instance().initSettings(this);
		
	}

	private void setMapNavigation() {
//		voiceTip.setEnties(new String[] {
//				getString(R.string.STR_MM_06_01_01_04),
//				getString(R.string.STR_MM_06_01_01_05)});
//		voiceTip.seletedIndex(jniSetupControl.get(jniSetupControl.STUPDM_VOICE_NAVI));
//	    voiceTip.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				if((Integer)newValue == jniSetupControl.STUPDM_VOICE_NAVI_COMPLETION){
//					jniSetupControl.set(jniSetupControl.STUPDM_VOICE_NAVI, jniSetupControl.STUPDM_VOICE_NAVI_COMPLETION);
//				}else if((Integer)newValue == jniSetupControl.STUPDM_VOICE_NAVI_CONCISE){
//					jniSetupControl.set(jniSetupControl.STUPDM_VOICE_NAVI, jniSetupControl.STUPDM_VOICE_NAVI_CONCISE);				
//				}
//				return true;
//			}
//		});

	    cameraTip.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				
				SettingHelper.setCameraTip((Boolean)newValue);
				UIC_SystemCommon.setHUD();
				return true;
			}
		});
	    
	    scheduletime.setEnties(R.string.STR_COM_054,R.string.STR_COM_055,R.string.STR_COM_056);
	    
	    scheduletime.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == 0){
					SharedPreferenceData.setValue(SharedPreferenceData.SCHEDULE_START_TIME, HybridUSTools.START_TIME_TEN_MINUE);
				}else if((Integer)newValue == 1){
					SharedPreferenceData.setValue(SharedPreferenceData.SCHEDULE_START_TIME, HybridUSTools.START_TIME_HALF_HOUR);
				}else if((Integer)newValue == 2){
					SharedPreferenceData.setValue(SharedPreferenceData.SCHEDULE_START_TIME, HybridUSTools.START_TIME_AN_HOUR);
				}
				return true;
			}
		});
	    
	    mapColor.setEnties(R.string.STR_MM_06_01_01_133,R.string.STR_MM_06_01_01_134,R.string.STR_MM_06_01_01_135);
	   
	    mapColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == 0){
					SharedPreferenceData.setValue(SharedPreferenceData.DAY_NIGHT_SETTING, UIBaseConnJNI.DAY_OPTION_AUTO);
				}else if((Integer)newValue == 1){
					SharedPreferenceData.setValue(SharedPreferenceData.DAY_NIGHT_SETTING, UIBaseConnJNI.DAY_OPTION_ALWAYS_DAY);
				}else if((Integer)newValue == 2){
					SharedPreferenceData.setValue(SharedPreferenceData.DAY_NIGHT_SETTING, UIBaseConnJNI.DAY_OPTION_ALWAYS_NIGHT);
				}
				
				UIBaseConnJNI.setDayNightOption(SharedPreferenceData.getInt(SharedPreferenceData.DAY_NIGHT_SETTING));
				return true;
			}
		});
	    
	    mapFontSize.setEnties(R.string.STR_MM_06_01_01_139,R.string.STR_MM_06_01_01_138,R.string.STR_MM_06_01_01_137);
	    
	    mapFontSize.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if((Integer)newValue == 0){
					SharedPreferenceData.setValue(SharedPreferenceData.MAP_FONT_SIZE, UIMapControlJNI.MAP_FONT_SIZE_SMALL);
				}else if((Integer)newValue == 1){
					SharedPreferenceData.setValue(SharedPreferenceData.MAP_FONT_SIZE, UIMapControlJNI.MAP_FONT_SIZE_MEDIUM);
				}else if((Integer)newValue == 2){
					SharedPreferenceData.setValue(SharedPreferenceData.MAP_FONT_SIZE, UIMapControlJNI.MAP_FONT_SIZE_BIG);
				}
				UIMapControlJNI.setMapFontSize(SharedPreferenceData.getInt(SharedPreferenceData.MAP_FONT_SIZE));
				return true;
			}
		});
	    
/*	    schedule.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				ForwardWinChange(ADT_Schedule_Activity.class);
				return true;
			}
		});*/
	    
	    if(gpsSetting != null) {
	    	gpsSetting.setEnties(R.string.STR_MM_06_01_01_152,R.string.STR_MM_06_01_01_153,R.string.STR_MM_06_01_01_154);

	  	    gpsSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	  			@Override
	  			public boolean onPreferenceChange(Preference preference, Object newValue) {
	  				if((Integer)newValue == SetupOptionValue.LOCATION_FUNC_INNER && !LocGpsListener.instance().isGpsOpen()){
	  					showGPSDialogs();
	  				}
	  				SharedPreferenceData.setValue(SharedPreferenceData.GPS_CONNECTION_MODE, (Integer)newValue);
	  				CLocationListener.Instance().SetCradleConnectStatus((Integer)newValue);
	  				return true;
	  			}

	  		});
	    }
	}
	
	private void showGPSDialogs() {
		 final CustomDialog dialog = new CustomDialog(this);
	        dialog.setTitle(R.string.MSG_01_00_01_01);
	        dialog.setMessage(R.string.MSG_COM_01_07);
			
	        dialog.setPositiveButton(R.string.STR_COM_035, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dg, int which) {
					startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}
			});
			
	        dialog.setNegativeButton(R.string.STR_COM_001, null);
		 	dialog.show();

		
	}

	private void findViews() {
//		voiceTip = (ListPreference) getPreferenceManager().findPreference("voicetip");
//		schedule = (PreferenceScreen) getPreferenceManager().findPreference("schedule");
		cameraTip = (CheckBoxPreference) getPreferenceManager().findPreference("cameratip");
		scheduletime = (ListPreference) getPreferenceManager().findPreference("scheduletime");
		mapColor = (ListPreference) getPreferenceManager().findPreference("mapcolor");
		mapFontSize = (ListPreference) getPreferenceManager().findPreference("mapfontsize");
		
/*		showDistance = (ListPreference) getPreferenceManager().findPreference("distanceshow");
		crashTime = (ListPreference) getPreferenceManager().findPreference("crashtime");
		carHighSet = (ListPreference) getPreferenceManager().findPreference("settingcarhigh");
		
		videoMode = (ListPreference) getPreferenceManager().findPreference("videomode");
		videoSaveMode = (ListPreference) getPreferenceManager().findPreference("videosavemode");
		videoSpace = (ListPreference) getPreferenceManager().findPreference("videospace");
		autoUpload = (CheckBoxPreference) getPreferenceManager().findPreference("autoupload");
		record = (CheckBoxPreference) getPreferenceManager().findPreference("record");*/
		
//		languageSet = (ListPreference) getPreferenceManager().findPreference("currentlanguage");
//		myFavorite = (CheckBoxPreference) getPreferenceManager().findPreference("myfavorite");
//		myHistory = (CheckBoxPreference) getPreferenceManager().findPreference("history");
		 account = (PreferenceScreen) getPreferenceManager().findPreference("login");
		 passWord = (PreferenceScreen) getPreferenceManager().findPreference("password");
//		moreInfo = (PreferenceScreen) getPreferenceManager().findPreference("moreinfo");
		
		versionInfo = (PreferenceScreen) getPreferenceManager().findPreference("versioninfo");
		disclaimer = (PreferenceScreen) getPreferenceManager().findPreference("disclaimer");
		serviceInfo = (PreferenceScreen) getPreferenceManager().findPreference("serviceinfo");
		licence = (PreferenceScreen) getPreferenceManager().findPreference("licence");
		clearBuffer = (PreferenceScreen) getPreferenceManager().findPreference("clearbuffer");
//		defaultSet = (PreferenceScreen) getPreferenceManager().findPreference("defaultvalue");
		
		gpsSetting = (ListPreference) getPreferenceManager().findPreference("switchgpssetting");
		//HUD
		poi = (CheckBoxPreference) getPreferenceManager().findPreference("hud_poi");
		trafficInfo = (CheckBoxPreference) getPreferenceManager().findPreference("hud_trafficInfo");
		trafficRestrict = (CheckBoxPreference) getPreferenceManager().findPreference("hud_trafficRestrict");
		timeFormat = (CheckBoxPreference) getPreferenceManager().findPreference("hud_time");
		speedCamera = (CheckBoxPreference) getPreferenceManager().findPreference("hud_speedcamera");
		logoMark = (PreferenceScreen) getPreferenceManager().findPreference("hud_logomark");
		
		bluetooth = (PreferenceScreen) getPreferenceManager().findPreference("bluetooth");
//		switchGps = (BluetoothListPreference) getPreferenceManager().findPreference("switchgps");
		cradleSet = (PreferenceScreen) getPreferenceManager().findPreference("cradlesetting");
		
		debug = (PreferenceScreen) getPreferenceManager().findPreference("debugset");
		irdrDebug = (PreferenceScreen) getPreferenceManager().findPreference("irdrdebug");
		sensorDebug = (PreferenceScreen) getPreferenceManager().findPreference("sensordebug");
		debugOff = (PreferenceScreen) getPreferenceManager().findPreference("debugoff");
	
		rootPreference = (PreferenceScreen) getPreferenceManager().findPreference("rootPreference");
//		DrivingSafe = (PreferenceCategory) getPreferenceManager().findPreference("drivingsafe");
//		DrivingRecord = (PreferenceCategory) getPreferenceManager().findPreference("drivingrecord");
		accountsynchronous = (PreferenceCategory) getPreferenceManager().findPreference("Accountsynchronous");
		help = (PreferenceCategory) getPreferenceManager().findPreference("help");
		cradeCategory = (PreferenceCategory) getPreferenceManager().findPreference("cradlepreferencecategory");
		debugCategory = (PreferenceCategory) getPreferenceManager().findPreference("debug");
		hudCategory   = (PreferenceCategory) getPreferenceManager().findPreference("HUD");
	}

	private void showLogOutDialog() {
		CustomDialog b = new CustomDialog(this);
		b.setMessage(R.string.STR_MM_06_02_02_31);
		b.setTitle(R.string.STR_MM_06_02_02_30);
		b.setNegativeButton(R.string.STR_COM_001, null);
		b.setPositiveButton(R.string.STR_COM_003,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						account.setEnabled(false);
						account.setTitle(R.string.STR_MM_06_02_02_40);
						UserControl_ManagerIF.Instance().LogOut();
					}
				});
		b.show();
	}
	private void setDebug() {
			
		if(SharedPreferenceData.getBoolean(SharedPreferenceData.STUPDM_DEBUG_DEVELOP_DBG)) {
			onDebugItem();

		} else {
			offDebugItem();
		}
		
		DebugView debugView = DebugView.addDebugView(getActionBar2());
		debugView.setDebugOnListener(new DebugOnListener() {
			
			@Override
			public void onDebugOn() {
				getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				onDebugItem();
				new Handler(){
					public void handleMessage(android.os.Message msg) {
						getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
					};
				}.sendEmptyMessage(1);
			}
		});


	}
	
	private void onDebugItem() {
		SharedPreferenceData.setValue(SharedPreferenceData.STUPDM_DEBUG_DEVELOP_DBG, true);
		if(rootPreference.findPreference("debug") == null){
			rootPreference.addPreference(debugCategory);
		}
		
	}
	
	private void offDebugItem() {
		SharedPreferenceData.setValue(SharedPreferenceData.STUPDM_DEBUG_DEVELOP_DBG, false);
		if(rootPreference.findPreference("debug") != null){
			rootPreference.removePreference(debugCategory);
		}

	}
	
	
	@Override
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		boolean bRet = false;
		int iTriggerID = triggerInfo.GetTriggerID();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN:
			setOtherClickEvent();
			break;
		case NSTriggerID.UIC_MN_TRG_UC_USER_LOGOUT:
			int iParams = (int) triggerInfo.GetlParam1();
			account.setEnabled(true);
			if (0 == iParams) {
				int[] lonlat = UIMapControlJNI.GetCenterLonLat();
				// SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0],
				// lonlat[1]);
				SharedPreferenceData.OPEN_AUTO_LOGIN.setValue(false);
				CustomToast.showToast(
						this,
						getString(R.string.STR_MM_06_02_02_33),
						Toast.LENGTH_SHORT);
				account.setTitle(R.string.STR_MM_06_02_02_32);
				account.setSummary(R.string.STR_MM_06_02_02_39);
				passWord.setEnabled(false);
			} else if (1 == iParams) {
				// failure
				account.setTitle(R.string.STR_MM_06_02_02_30);
				CustomToast.showToast(
						this,
						getString(R.string.STR_MM_06_02_02_34),
						Toast.LENGTH_SHORT);
			} else if (2 == iParams) {

				CustomToast.showToast(
						this,
						getString(R.string.STR_MM_09_01_01_10),
						Toast.LENGTH_SHORT);
			}
			break;
		case NSTriggerID.UIC_MN_TRG_UC_DOWNLOAD_USERINFO:
			if (0 == (int) triggerInfo.GetlParam1()) {
				if (UserControl_ManagerIF.Instance().HasLogin()
						&& UserControl_ManagerIF.Instance().GetStoredUserInfo() != null) {
					String tempNickName = UserControl_ManagerIF.Instance()
							.GetStoredUserInfo().m_strNickName;
					if (tempNickName.length() > 0) {
						account.setTitle(R.string.STR_MM_06_02_02_30);
						account.setSummary(tempNickName);
					} else {
						account.setSummary("");
					}
					passWord.setEnabled(true);
				}
			}
			break;
		case NSTriggerID.UIC_MN_TRG_CRADLE_SETUP_DATA_CHANGED:
			if(triggerInfo.GetlParam1() == SharedPreferenceData.getInt(SharedPreferenceData.GPS_CONNECTION_MODE)) {
				gpsSetting.seletedIndex((int)triggerInfo.GetlParam2());
			}
			break;
		default:
			super.OnTrigger(triggerInfo);
			break;
		}
		return bRet;
	}
	
	@Override
	protected void OnDestroy() {
//		switchGps.removeTriggerListener();
		super.OnDestroy();
	}
	
	private void setDefaultvalue(){
	    cameraTip.setChecked(SharedPreferenceData.getBoolean(SharedPreferenceData.CAMERA_TIP));
	    scheduletime.seletedIndex(SharedPreferenceData.getInt(SharedPreferenceData.SCHEDULE_START_TIME));
	    mapColor.seletedIndex(SharedPreferenceData.getInt(SharedPreferenceData.DAY_NIGHT_SETTING));
	    mapFontSize.seletedIndex(SharedPreferenceData.getInt(SharedPreferenceData.MAP_FONT_SIZE));
  	    int gpsSettingSelection = SharedPreferenceData.getInt(SharedPreferenceData.GPS_CONNECTION_MODE);
  	    gpsSetting.seletedIndex(gpsSettingSelection);
  	    poi.setChecked(SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_POI));
		trafficInfo.setChecked(SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_TRAFFIC_INFO));
		trafficRestrict.setChecked(SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_TRAFFIC_RESTRICT));
		speedCamera.setChecked(SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_SPEEDCAMER));
		timeFormat.setChecked(SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_TIME_24));
		logoMark.setEnabled(SharedPreferenceData.getBoolean(SharedPreferenceData.HUD_POI));
/*		videoMode.seletedIndex(SharedPreferenceData.DRIR_SETTING_DRID_RECMODE.getInt()-1);
        videoSaveMode.seletedIndex(SharedPreferenceData.DRIR_SETTING_DRID_SDRECMODE.getInt());
        if( SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.getInt() == 1024){
 			videoSpace.seletedIndex(0);
 		}else if(SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.getInt() == 2048){
 			videoSpace.seletedIndex(1);
 		}else if(SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.getInt() == 3072){
 			videoSpace.seletedIndex(2);
 		}else if(SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.getInt() == 4096){
 			videoSpace.seletedIndex(3);
 		}else if(SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.getInt() == jniDRIR_MainControl.DRIR_SETTING_DR_MAX_SDCAPACITY){
 			videoSpace.seletedIndex(4);
 		}
// 		autoUpload.setChecked(SharedPreferenceData.DRIR_SETTING_DRID_UPLOADFUC.getInt() ==1);
 		record.setChecked(SharedPreferenceData.DRIR_SETTING_DRID_ONOFF.getInt() == 1);
        carHighSet.seletedIndex(SharedPreferenceData.DRIR_SETTING_IRID_CARMODEL.getInt());
	    crashTime.seletedIndex(SharedPreferenceData.DRIR_SETTING_IRID_CRASHTIMEALARM.getInt()-1);
		showDistance.seletedIndex(SharedPreferenceData.DRIR_SETTING_IRID_FRONTCARINFO.getInt());*/
		if(UserControl_ManagerIF.Instance().HasLogin()) {
			SharedPreferenceData.setValue(SharedPreferenceData.OPEN_AUTO_LOGIN, true);
		}
	}

}
