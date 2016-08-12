package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestRegByPhone extends UserControl_RequestBase {
	public UserControl_RequestRegByPhone(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean RegisterByPhone(UserControl_UserInfo userInfo, String strValidate) {

			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "accounts/cellphone/verify";
			List<NameValuePair> params = ParamsFromUserinfo(userInfo);
			params.add(new BasicNameValuePair("token",strValidate));
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;

	}
		
}
