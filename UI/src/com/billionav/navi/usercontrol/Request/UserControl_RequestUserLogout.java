package com.billionav.navi.usercontrol.Request;

import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestUserLogout extends UserControl_RequestBase {
	public UserControl_RequestUserLogout(int iRequestId) {
		super(iRequestId);
	}

	public boolean LogOut() {
		
			String sRequestUrl = GetAuthServerUrl();
			//sRequestUrl += "/userauth/logout";
			sRequestUrl += "userauth/logout";
			SendRequestByGet(sRequestUrl,true);
			return true;
	}
	
}
