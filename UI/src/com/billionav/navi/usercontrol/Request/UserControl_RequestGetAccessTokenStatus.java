package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;

public class UserControl_RequestGetAccessTokenStatus extends UserControl_RequestBase{
	
	public UserControl_RequestGetAccessTokenStatus(int iRequestId) {
		super(iRequestId);
	}

	public boolean GetAccessTokenStatus(){
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "accounts/getaccesstokenstatus/";			
		SendRequestByGet(sRequestUrl,true);
		return true;
		
	}
}
