package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class UserControl_RequestCheckRegisted extends UserControl_RequestBase {
	public UserControl_RequestCheckRegisted(int iRequestId) {
		super(iRequestId);
	}

	public boolean HasRegistered(String phoneNum, String email, String nikeName){
		

			if( ( 0 == phoneNum.compareTo("") ) && 
					( 0 == email.compareTo("") ) && 
						( 0 == nikeName.compareTo("") )){
				return false;
			}

			String sRequestUrl = GetAuthServerUrl();			
			sRequestUrl += "accounts/lookup";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if(0 != phoneNum.compareTo("")){
				params.add(new BasicNameValuePair("cellphone",phoneNum));
			}
			if(0 != email.compareTo("")){
				params.add(new BasicNameValuePair("email",email));
			}
			if(0 != nikeName.compareTo("")){
				params.add(new BasicNameValuePair("nickname",nikeName));
			}
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);	
			return true;
	}
	
}
