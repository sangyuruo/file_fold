package com.billionav.navi.naviscreen.open;


import android.app.Activity;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.auth.ReLogin_TriggerListener;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.dest.ADT_Top_Menu;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;

public class OpeningCommon {
	public static interface AutoLoginResultListener{
		void reciveResult(boolean result);
	}
	
	private static class LoginListener implements TriggerListener{

		@Override
		public boolean onTrigger(NSTriggerInfo triggerInfo) {
			if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN) {
				return false;
			}
			
			String type = triggerInfo.GetString1();
			try{
				if(Integer.valueOf(type)!=UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_AUTO){
					return false;
				}
			}catch (Exception e) {
				// TODO: handle exception
				return false;
			}
			
			if(triggerInfo.GetlParam1() == 0){
				  int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//				    SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0], lonlat[1]);
				CustomToast.showToast(NSViewManager.GetViewManager(), R.string.STR_COM_019, 1000);
				ReLogin_TriggerListener.addReLoginListener();
				Activity a = MenuControlIF.Instance().GetCurrentActivity();
				if(a instanceof AutoLoginResultListener) {
					((AutoLoginResultListener) a).reciveResult(true);
				}
				
			}else{
				SharedPreferenceData.OPEN_AUTO_LOGIN.setValue(false);
				if(triggerInfo.GetlParam2() == 104 || triggerInfo.GetlParam2() == 107){
					CustomToast.showToast(NSViewManager.GetViewManager(), R.string.STR_COM_027, 1000);
				}else{
					CustomToast.showToast(NSViewManager.GetViewManager(), R.string.STR_COM_028, 1000);
				}
				Activity a = MenuControlIF.Instance().GetCurrentActivity();
				if(a instanceof AutoLoginResultListener) {
					((AutoLoginResultListener) a).reciveResult(false);
				}
			}
			
			GlobalTrigger.getInstance().removeTriggerListener(LoginListener.this);
			MenuControlIF.Instance().TriggerForScreen(triggerInfo);
			return true;
		}
		

		public static void StartLogin() {
			boolean  autoLogin = SharedPreferenceData.OPEN_AUTO_LOGIN.getBoolean();
			if(!autoLogin){//don't need auto login progress , go to login screen.
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Auth_Login.class);
				return;
			}
			
			if(UserControl_ManagerIF.Instance().isLogging()){
				return;
			}
			
			
			UserControl_UserInfo[] userInfo = UserControl_ManagerIF.Instance().GetLoginInfoListFromFile();
		
			if( userInfo == null ||userInfo.length==0||userInfo[0]==null){
				
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Auth_Login.class);
			} else {
				long loginResut = -1;
				if(userInfo[0].m_iLoginType ==UserControl_UserInfo.USER_LOGIN_TYPE_PHONE) {
					loginResut = UserControl_ManagerIF.Instance().LoginByPhone(userInfo[0].m_strPhoneNum, userInfo[0].m_strUserPassWord,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_AUTO);
				} else if(userInfo[0].m_iLoginType ==UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL) {
					loginResut= UserControl_ManagerIF.Instance().LoginByMail(userInfo[0].m_strEmail,userInfo[0].m_strUserPassWord,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_AUTO);
				} else if(userInfo[0].m_iLoginType ==UserControl_UserInfo.USER_LOGIN_TYPE_NANE) {
					loginResut= UserControl_ManagerIF.Instance().LoginByName(userInfo[0].m_strNickName,userInfo[0].m_strUserPassWord,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_AUTO);
				}
				
				if(loginResut == -1 ){
					
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Auth_Login.class);
				} else {
					LoginListener l = new LoginListener();
					GlobalTrigger.getInstance().addTriggerListener(l);
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Top_Menu.class);
				}
			}
			
		}
		
	}

	public static void checkLoginStatus(){
		LoginListener.StartLogin();

	}

	public static void loginByIMEI() {
		UserControl_ManagerIF.Instance().LoginByIMEI(SystemInfo.GetDeviceNumber());
	}
	
}
