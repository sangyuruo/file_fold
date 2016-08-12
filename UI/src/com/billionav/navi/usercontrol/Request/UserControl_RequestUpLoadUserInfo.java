package com.billionav.navi.usercontrol.Request;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PMultiPart;
import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestUpLoadUserInfo extends UserControl_RequestBase {
	public UserControl_RequestUpLoadUserInfo(int iRequestId) {
		super(iRequestId);
	}

	public boolean UploadUserInfomation(UserControl_UserInfo userInfo){
	
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl +="accounts/profile/update/";
			List<PMultiPart> params=PMultiPartFromUserinfo(userInfo);	
			PostData post = new PostData();
			post.setPostMultiData(params);
			SendRequestByPost(sRequestUrl,post,true);
			return true;

	}
	
}
