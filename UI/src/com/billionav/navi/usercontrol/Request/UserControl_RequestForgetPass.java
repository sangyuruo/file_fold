package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PLogin;
import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseChangePass;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseForgetPass;

public class UserControl_RequestForgetPass extends UserControl_RequestBase {
	public UserControl_RequestForgetPass(int iRequestId) {
		super(iRequestId);
	}

	//to make an request url first, then ask for server
	public boolean ChangePassword(UserControl_UserInfo userInfo, String loginName, String newPassword, String verificationCode){		
		UserControl_ResponseBase cResponse = GetResponseBase();
		if(cResponse!=null){
		((UserControl_ResponseForgetPass)cResponse).SetTempPasWord(newPassword);
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "userauth/password/setnew";
			List<NameValuePair> params = ParamsFromUserinfo(userInfo);
			
			params.add(new BasicNameValuePair("token", verificationCode));
			if(UserControl_ManagerIF.Instance().IsValidEmail(loginName)){
				params.add(new BasicNameValuePair("email",loginName));
			}else if(UserControl_ManagerIF.Instance().IsValidPhoneNum(loginName)){
				params.add(new BasicNameValuePair("cellphone",loginName));
			}else{
				return false;
			}
			params.add(new BasicNameValuePair("newpwd",newPassword));	
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,true);
			return true;	
		}
		return false;
	}
	
}
