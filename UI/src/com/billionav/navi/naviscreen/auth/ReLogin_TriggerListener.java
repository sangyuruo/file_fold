package com.billionav.navi.naviscreen.auth;

import android.util.Log;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PConnectReceiver.NetConnectTypeListener;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.ui.R;

public class ReLogin_TriggerListener implements TriggerListener{

	private static ReLogin_TriggerListener listener = null; 
	
	private static ReLogin_TriggerListener Instance() {
		if (null == listener) {
			listener = new ReLogin_TriggerListener();
		}
		return listener;
	}
	
	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN) {
			return false;
		}
		
		String type = triggerInfo.GetString1();
		try{
			if(Integer.valueOf(type)!=UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN){
				return false;
			}
		}catch (Exception e) {
			return false;
		}
		if(triggerInfo.GetlParam1() != 0){	
			if(triggerInfo.GetlParam2() == 104 || triggerInfo.GetlParam2() == 107){
				CustomToast.showToast(NSViewManager.GetViewManager(), R.string.STR_COM_027, 1000);
				GlobalTrigger.getInstance().removeTriggerListener(this);	
			}else{
				PConnectReceiver.addListener( new NetConnectTypeListener() {
					
					@Override
					public void onReceive(int type) {
						// TODO Auto-generated method stub
						if(type != PConnectReceiver.CONNECT_TYPE_NONE){
							RefreshTheToken();
							PConnectReceiver.removeListener(this);
						}
					}
				});
			}
		}else{
			int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//			SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0], lonlat[1]);
		}

		return true;
	}
	
	public static void addReLoginListener() {
		GlobalTrigger.getInstance().addTriggerListener(ReLogin_TriggerListener.Instance());
	}
	
	public static void removeReLoginListener() {
		GlobalTrigger.getInstance().removeTriggerListener(ReLogin_TriggerListener.Instance());
	}

	private void RefreshTheToken() {
		UserControl_UserInfo[] cUserInfo = UserControl_ManagerIF.Instance().GetLoginObjectInfoFromFile();
		if(cUserInfo.length > 0 && null != cUserInfo[0]) {
			if ( UserControl_UserInfo.USER_LOGIN_TYPE_PHONE == cUserInfo[0].m_iLoginType) {
				UserControl_ManagerIF.Instance().LoginByPhone(cUserInfo[0].m_strPhoneNum,cUserInfo[0].m_strUserPassWord,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN);
			} else if (UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL == cUserInfo[0].m_iLoginType) {
				UserControl_ManagerIF.Instance().LoginByMail(cUserInfo[0].m_strEmail,cUserInfo[0].m_strUserPassWord ,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN);
			} else if (UserControl_UserInfo.USER_LOGIN_TYPE_NANE == cUserInfo[0].m_iLoginType) {
				UserControl_ManagerIF.Instance().LoginByName(cUserInfo[0].m_strNickName,cUserInfo[0].m_strUserPassWord ,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN);
			}  else if (UserControl_UserInfo.USER_LOGIN_TYPE_IMEI == cUserInfo[0].m_iLoginType){
				UserControl_ManagerIF.Instance().LoginByIMEI(SystemInfo.getDeviceNo());
			} else {
				Log.d("RefreshTheToken", "[RefreshTheToken]Login Type Null");
			}
		} else {
			Log.d("RefreshTheToken", "[RefreshTheToken]Task Running Error lenght = " + cUserInfo.length);
		}
	}
}
