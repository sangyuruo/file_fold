package com.billionav.navi.naviscreen.setting;



import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.jniSetupControl;
import com.billionav.navi.app.AndroidNaviAPP;
import com.billionav.navi.component.basiccomponent.ListPreference;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.auth.ADT_Auth_ChangePassword;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.naviscreen.open.OpeningCommon.AutoLoginResultListener;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.uicommon.UIC_SystemCommon;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;
import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;



public class ADT_Settings_AccountSynchronous_More extends PrefenrenceActivityBase implements AutoLoginResultListener{


	private ListPreference frequency = null;
	private PreferenceScreen bindWeibo = null;
	private PreferenceScreen defaultSet = null;
	private PreferenceScreen account = null;
	private PreferenceScreen passWord = null;
	private PreferenceCategory root = null;

	private boolean isbind = false;
	private String temp_token = "";
	private String temp_expires = "";

    private static final String CONSUMER_KEY = "2445655897";	   
	private static final String CONSUMER_SECRET = "9c1220859eed9e4efbca3ffb051750a8";
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_settings_account_synchronous);
		setTitle(R.string.STR_MM_06_01_01_01); 
		findViews();	 
		setClickEvent();
		if(!SystemTools.isCH()){
			root.removePreference(bindWeibo);
		}
	}

	private void setClickEvent() {
		frequency.setEnties(new String[] {
				getString(R.string.STR_MM_06_02_02_25),
				getString(R.string.STR_MM_06_02_02_26),
				getString(R.string.STR_MM_06_02_02_27) });
//	 frequency.seletedIndex(jniSetupControl
//				.get(jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY));
	 frequency.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
		
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// TODO Auto-generated method stub
//			if ((Integer)newValue == jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY_FIFTEEN) {
//				jniSetupControl
//						.set(jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY,
//								jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY_FIFTEEN);
//			} else if ((Integer)newValue == jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY_THRITY) {
//				jniSetupControl
//						.set(jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY,
//								jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY_THRITY);
//			} else if ((Integer)newValue == jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY_SIXTY) {
//				jniSetupControl
//						.set(jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY,
//								jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY_SIXTY);
//			}
			return true;
		}
	});
		defaultSet.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				showRecoverDialog();	
				return true;
			}
		});

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
	}

	private void findViews() {
		frequency = (ListPreference) getPreferenceManager().findPreference("frequency");
		 bindWeibo = (PreferenceScreen) getPreferenceManager().findPreference("weibo");
		 defaultSet = (PreferenceScreen) getPreferenceManager().findPreference("defaultvalue");
		 account = (PreferenceScreen) getPreferenceManager().findPreference("login");
		 passWord = (PreferenceScreen) getPreferenceManager().findPreference("password");
		 root = (PreferenceCategory) getPreferenceManager().findPreference("account");
	}
	
	private  boolean isNetEnable() {
		boolean netDisconntect = (PConnectReceiver.getConnectType() != PConnectReceiver.CONNECT_TYPE_NONE);
		if(!netDisconntect) {
			CustomToast.showToast(this, R.string.STR_MM_01_01_01_13, 2000);
			return false;
		}
		return true;
	}

	
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		 setOtherClickEvent();
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
			bindWeibo.setEnabled(true);		
			passWord.setEnabled(true);
			String token = userInfo.m_strWeiBoToken[0];
			String expires = userInfo.m_strWeiBoVaildPeriod[0];
			if (!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(expires)) {
				
				try{
					long temp_time = Long.valueOf(expires);
					if(System.currentTimeMillis() < temp_time){
						setAboutUnBindWeiBo();
					}else{
						UserControl_ManagerIF.Instance().SetStoredTokenInfo(UserControl_ManagerIF.WEIBO_TYPE_SINA, "", "");
						setAboutBindWeiBo();
					}
				}catch (Exception e) {
					// TODO: handle exception
					UserControl_ManagerIF.Instance().SetStoredTokenInfo(UserControl_ManagerIF.WEIBO_TYPE_SINA, "", "");
					setAboutBindWeiBo();
				}
				 
						
			}else{	
				UserControl_ManagerIF.Instance().SetStoredTokenInfo(UserControl_ManagerIF.WEIBO_TYPE_SINA, "", "");
				setAboutBindWeiBo();
			}

		} else {
			if(UserControl_ManagerIF.Instance().isLogging()){
				account.setSummary(R.string.STR_MM_09_01_01_14);
				account.setEnabled(false);
			}else {
				account.setSummary(R.string.STR_MM_06_02_02_39);
				account.setEnabled(true);
			}
			bindWeibo.setEnabled(false);
			passWord.setEnabled(false);
		}
	}

	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Utility.clearCookies(this);
			MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_IN_LEFT);
			ForwardKeepDepthWinChange(ADT_Settings_Main.class);
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}



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
//				SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0], lonlat[1]);
				SharedPreferenceData.OPEN_AUTO_LOGIN.setValue(false);
				CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
						getString(R.string.STR_MM_06_02_02_33), Toast.LENGTH_SHORT);
				account.setTitle(R.string.STR_MM_06_02_02_32);
				account.setSummary(R.string.STR_MM_06_02_02_39);
				bindWeibo.setEnabled(false);
				passWord.setEnabled(false);
				bindWeibo.setTitle(R.string.STR_MM_06_02_02_17);
			} else if (1 == iParams) {
				// failure
				CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
						getString(R.string.STR_MM_06_02_02_34), Toast.LENGTH_SHORT);
			} else if (2 == iParams) {

				CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
						getString(R.string.STR_MM_09_01_01_10), Toast.LENGTH_SHORT);
			}
			break;
		case NSTriggerID.UIC_MN_TRG_UC_DOWNLOAD_USERINFO:
			if(0 == (int) triggerInfo.GetlParam1()){
				if(UserControl_ManagerIF.Instance().HasLogin()&&UserControl_ManagerIF.Instance().GetStoredUserInfo()!=null){
					String tempNickName = UserControl_ManagerIF.Instance().GetStoredUserInfo().m_strNickName;
					if(tempNickName.length()>0){
						account.setSummary(tempNickName);
					}else{
						account.setSummary("");
					}
				}
			}
			break;
		case NSTriggerID.UIC_MN_TRG_UC_SET_ACCESS_TOKEN:
			bindWeibo.setEnabled(true);
			if (0 == (int) triggerInfo.GetlParam1()) {
				UserControl_ManagerIF.Instance().SetStoredTokenInfo(UserControl_ManagerIF.WEIBO_TYPE_SINA, temp_token, temp_expires);
				if (isbind) {
					setAboutBindWeiBo();
					CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
							R.string.STR_MM_06_02_02_36, Toast.LENGTH_SHORT);
				} else {
					setAboutUnBindWeiBo();
					CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
							R.string.STR_MM_06_02_02_35, Toast.LENGTH_SHORT);
				}

			} else if (1 == (int) triggerInfo.GetlParam1()) {
				CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
						R.string.STR_COM_013, Toast.LENGTH_SHORT);
			} else if (2 == (int) triggerInfo.GetlParam1()) {
				CustomToast.showToast(ADT_Settings_AccountSynchronous_More.this,
						R.string.STR_COM_014, Toast.LENGTH_SHORT);
			}
			break;
		default:
			super.OnTrigger(triggerInfo);
			break;
		}
		return bRet;
	}

	private void setAboutUnBindWeiBo() {
		isbind = true;
		bindWeibo.setTitle(R.string.STR_MM_06_02_02_21);
		bindWeibo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				showUnbindDialog();
				return true;
			}
		});
	}

	private void setAboutBindWeiBo() {
		isbind = false;
		bindWeibo.setTitle(R.string.STR_MM_06_02_02_17);
		bindWeibo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				try {
					bindWeiBo();
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		});
	}
	private void bindWeiBo() throws WeiboException {
		Weibo weibo = Weibo.getInstance();
        weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);
        weibo.setRedirectUrl("http://www.pioneersuntec.com");
        weibo.authorize(ADT_Settings_AccountSynchronous_More.this, new AuthDialogListener());
	}
	private void showUnbindDialog() {
		CustomDialog b = new CustomDialog(
				ADT_Settings_AccountSynchronous_More.this);
		b.setMessage(R.string.STR_MM_06_02_02_22);

		b.setTitle(R.string.STR_MM_06_02_02_21);
		b.setNegativeButton(R.string.STR_COM_001, null);
		
		b.setPositiveButton(R.string.STR_COM_003,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						bindWeibo.setEnabled(false);
						UserControl_ManagerIF.Instance().SetAccessToken("sina", "","");
						temp_token = "";
						temp_expires = "";
					}
				});
		b.show();
	}
	   class AuthDialogListener implements WeiboDialogListener {

	        @Override
	        public void onComplete(Bundle values) {
	            String token = values.getString("access_token");
	            String expires_in = values.getString("expires_in");
	            AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
	            accessToken.setExpiresIn(expires_in);
	            Weibo weibo = Weibo.getInstance();
	            weibo.setAccessToken(accessToken);
	            temp_token = token;
	            temp_expires = String.valueOf(weibo.getAccessToken().getExpiresIn()); 
	         UserControl_ManagerIF.Instance().SetAccessToken("sina", token,  String.valueOf(weibo.getAccessToken().getExpiresIn()));
	          Utility.clearCookies(ADT_Settings_AccountSynchronous_More.this);
	        }
	        @Override
	        public void onError(DialogError e) {
	            Toast.makeText(ADT_Settings_AccountSynchronous_More.this, getString(R.string.STR_MM_06_02_02_37),
	                    Toast.LENGTH_LONG).show();
	        }

	        @Override
	        public void onCancel() {
	        }

	        @Override
	        public void onWeiboException(WeiboException e) {
	        	 Toast.makeText(ADT_Settings_AccountSynchronous_More.this, getString(R.string.STR_MM_06_02_02_37),
		                    Toast.LENGTH_LONG).show();
	        }

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
							if (SystemTools.isCH()) {
								DRIRAppMain.setDRIRValue();
							}
							resetValues();
							
							 frequency.seletedIndex(jniSetupControl
										.get(jniSetupControl.STUPDM_SYNCHRONOUS_FREQUENCY));
								MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_NONE);
								ForwardKeepDepthWinChange(ADT_Settings_AccountSynchronous_More.class);
						}
					});
			b.show();
		}
	   protected void resetValues() {
//		   AndroidNaviAPP.getInstance().setLanguage();
//		   int mapColorValue = jniSetupControl.get(jniSetupControl.STUPDM_MAPCOLOR_CHANGE);
//	        if(mapColorValue == jniSetupControl.STUPDM_MAP_COLOR_CHANGE_BY_TIME){
//				if(UIC_SystemCommon.getIsDayStatus()){
//					UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_DAY);
//				}else{
//					UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_NIGHT);
//				}
//	        }else if(mapColorValue == jniSetupControl.STUPDM_MAP_COLOR_CHANGE_DAY){
//	        	UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_DAY);
//	        }else if(mapColorValue == jniSetupControl.STUPDM_MAP_COLOR_CHANGE_NIGHT){
//	        	UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_NIGHT);
//	        }
//	        UIMapControlJNI.SetFontSize(SharedPreferenceData.MAP_FONT_SIZE.getInt());
		
	}

	private void showLogOutDialog(){
		   CustomDialog b = new CustomDialog(this);
			b.setMessage(R.string.STR_MM_06_02_02_31);
			b.setTitle(R.string.STR_MM_06_02_02_30);
			b.setNegativeButton(R.string.STR_COM_001, null);
			b.setPositiveButton(R.string.STR_COM_003,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							account.setEnabled(false);
							UserControl_ManagerIF.Instance().LogOut();
						}
					});
			b.show();
	   }

	@Override
	public void reciveResult(boolean result) {
		// TODO Auto-generated method stub
		if(result){
			setOtherClickEvent();
		}
		
	}
	
}
