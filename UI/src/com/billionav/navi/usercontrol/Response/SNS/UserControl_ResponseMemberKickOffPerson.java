package com.billionav.navi.usercontrol.Response.SNS;

import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_ResponseMemberKickOffPerson extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMemberKickOffPerson(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_KICKOFF_PERSON);
	}
	
}
