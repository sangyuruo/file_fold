package com.billionav.navi.usercontrol.Response.SNS;

import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_ResponseInfoBlogSharing extends UserControl_ResponseSNSBase{

	public UserControl_ResponseInfoBlogSharing(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_BLOG_SHARING);
	}
	
	public boolean ParseResultInfo(byte[] inputData) {
		
		return true;
	}
}
