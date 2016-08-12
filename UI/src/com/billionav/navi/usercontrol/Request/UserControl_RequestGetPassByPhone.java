package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestGetPassByPhone extends UserControl_RequestBase {
	public UserControl_RequestGetPassByPhone(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean GetPassByPhone(String phoneNumber) {
			
			String sRequestUrl = GetAuthServerUrl();		
			sRequestUrl += "userauth/password/reset";
			List<NameValuePair> params = new ArrayList<NameValuePair>();		
			params.add(new BasicNameValuePair("cellphone",phoneNumber));
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;

	}
}
