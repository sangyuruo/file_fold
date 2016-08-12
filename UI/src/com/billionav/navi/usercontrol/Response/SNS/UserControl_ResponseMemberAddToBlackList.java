package com.billionav.navi.usercontrol.Response.SNS;

import java.util.List;

import org.apache.http.NameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_ResponseMemberAddToBlackList extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMemberAddToBlackList(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_ADDTOBLACKLIST);
	}
	
	
}
