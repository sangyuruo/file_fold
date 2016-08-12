package com.billionav.navi.usercontrol.Response;

import com.billionav.jni.UIBaseConnJNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PLogin;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

/**
 * 
 * @author ChenYong
 * @see the Request to Server is go to let server logger the logout time so 
 * 		it is no Important of the Response code 401 or 200 ,it is need to clean 
 * 		the Token and clean the Login Status. it is always success to UI(Triggle)  
 * 
 *
 */
public class UserControl_ResponseUserLogout extends UserControl_ResponseBase {	
	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
	
	public UserControl_ResponseUserLogout(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_USER_LOGOUT;
		int errCode = getResCode();
		
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			cTrigger.SetlParam1(m_iResultCode);
			cTrigger.SetlParam2(errCode);
		}
		else {
			cTrigger.SetlParam1(m_iResultCode);
		}
		
		UserControl_ManagerIF.Instance().CleanLoginSession();
		UIBaseConnJNI.notifyLogout();
		MenuControlIF.Instance().TriggerForScreen(cTrigger);
	}
	

}
