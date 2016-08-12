package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestDeleteUser extends UserControl_RequestBase{
	public UserControl_RequestDeleteUser(int iRequestId) {
		super(iRequestId);
	}
	 
	public boolean DeleteUserByEmail(String email){

			String sRequestUrl = GetAuthServerUrl();		
			sRequestUrl += "accounts/deleteuser";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("email",email));
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;
	}
	
	public boolean DeleteUserByCellphone(String cellphone){
	
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "accounts/deleteuser";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("cellphone",cellphone));
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;
	}

}
