package com.billionav.navi.usercontrol.Response.SNS;

import org.w3c.dom.Element;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_ResponseGroupBuildGroup extends UserControl_ResponseSNSBase{
	
	public UserControl_ResponseGroupBuildGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_BUILD_GROUP);
	}
	
	/**
	 * Triggle param01 : success 1 failed 0
	 * success :
	 * 			param02 : Cycle ID
	 * failes	:
	 * 			param02 : error ID param03 ....
	 */
	protected void OnResponseSuccess(Element element) {
		m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
		String strCycleID = element.getAttribute("circle-id");
		setM_strPassParam01(strCycleID);
	}
	
	
	
}
