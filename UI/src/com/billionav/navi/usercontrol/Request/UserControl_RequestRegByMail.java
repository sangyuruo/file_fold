package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestRegByMail extends UserControl_RequestBase {
	public UserControl_RequestRegByMail(int iRequestId) {
		super(iRequestId);
	}

	public boolean RegisterByEmail(UserControl_UserInfo userInfo) {

			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "accounts/signup/email/";
			List<NameValuePair> params=ParamsFromUserinfo(userInfo);
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;

	}
	
}
