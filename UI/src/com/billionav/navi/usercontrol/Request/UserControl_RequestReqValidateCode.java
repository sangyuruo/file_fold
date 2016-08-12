package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestReqValidateCode extends UserControl_RequestBase {
	public UserControl_RequestReqValidateCode(int iRequestId) {
		super(iRequestId);
	}

	public boolean ReqValidateCode(String sCellPhoneNum) {

			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "accounts/signup/cellphone" ;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("cellphone",sCellPhoneNum));
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;

	}
	
}
