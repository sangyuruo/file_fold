package com.billionav.navi.usercontrol.Response.SNS;

import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_ResponseInfoGroupSharing  extends UserControl_ResponseSNSBase{

	public UserControl_ResponseInfoGroupSharing(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_GROUP_SHARING);
	}
	
	public boolean ParseResultInfo(byte[] inputData) {
		
		return true;
	}
}
