package com.billionav.navi.usercontrol.Response.SNS;

import org.w3c.dom.Element;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_ResponseMemberJoinGroup  extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMemberJoinGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_JOIN_GROUP);
	}
	
	/**
	 *  0 - already invited , wait for conform 			
	 *  1 - already post request , wait for conform
	 *  2 -	already conform	
	 *  3 -	refuse		
	 *
	 */
	protected void OnResponseSuccess(Element element) {
		m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
		String strCycleID = element.getAttribute("value");
		setM_iParam02(Integer.parseInt(strCycleID));
	}
	
}
