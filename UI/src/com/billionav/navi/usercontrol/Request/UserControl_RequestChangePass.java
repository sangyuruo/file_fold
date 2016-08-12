package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseChangePass;

public class UserControl_RequestChangePass extends UserControl_RequestBase {
	public UserControl_RequestChangePass(int iRequestId) {
		super(iRequestId);
	}

	//to make an request url first, then ask for server
	public boolean ChangePassword(String oldPassword, String newPassword){		
		UserControl_ResponseBase cResponse = GetResponseBase();
		if(cResponse!=null){
		((UserControl_ResponseChangePass)cResponse).SetTempPasWord(newPassword);
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "userauth/password/update";
			List<NameValuePair> params = new ArrayList<NameValuePair>();				
			params.add(new BasicNameValuePair("oldpwd",oldPassword));
			params.add(new BasicNameValuePair("newpwd",newPassword));	
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,true);
			return true;	
		}
		return false;
	}
	
}
