package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseChangePass;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseForgetPass;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseGetCode;

public class UserControl_RequestGetCode extends UserControl_RequestBase {
	public UserControl_RequestGetCode(int iRequestId) {
		super(iRequestId);
	}

	//to make an request url first, then ask for server
	public boolean getVerificationCode(UserControl_UserInfo userInfo, String name){		
		UserControl_ResponseBase cResponse = GetResponseBase();
		if(cResponse!=null){
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "userauth/password/forget";
			List<NameValuePair> params = ParamsFromUserinfo(userInfo);
			if(UserControl_ManagerIF.Instance().IsValidEmail(name)){
				params.add(new BasicNameValuePair("email",name));
			}else if(UserControl_ManagerIF.Instance().IsValidPhoneNum(name)){
				params.add(new BasicNameValuePair("cellphone",name));
			}else{
				return false;
			}
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,true);
			return true;	
		}
		return false;
	}
	
}
