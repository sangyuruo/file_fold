package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;

public class UserControl_RequestSetAccessToken extends UserControl_RequestBase{
	
	public UserControl_RequestSetAccessToken(int iRequestId) {
		super(iRequestId);
	}

	public boolean SetAccessToken(String type,String token){
		return SetAccessToken(type,token ,"");
	}
	
	public boolean SetAccessToken(String type,String token,String validPeriod){
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "accounts/setaccesstoken";		
		List<NameValuePair> params = new ArrayList<NameValuePair>();	
		params.add(new BasicNameValuePair("type",type));	
		params.add(new BasicNameValuePair("token",token));	
		params.add(new BasicNameValuePair("valid-period", validPeriod));
		PostData post = new PostData();
		post.setPostData(params);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
		
	}
}
