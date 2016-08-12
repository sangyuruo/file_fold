package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseDownLoadUserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseUserLogin;

//there are three ways to get user infomation ,it is by nickName,phoneNum,email
//what this class need to do is send request by url
public class UserControl_RequestDownLoadUserInfo extends UserControl_RequestBase {
	public UserControl_RequestDownLoadUserInfo(int iRequestId) {
		super(iRequestId);
	}

	public boolean DownloadByName(String nickName,boolean iSDownloadMyself) {
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl+="accounts/profile/querydetail";		
			List<NameValuePair> params = new ArrayList<NameValuePair>();	
			params.add(new BasicNameValuePair("nickName",nickName));	
			PostData post = new PostData();
			post.setPostData(params);
			//we should known whose imformation we need ,myself or other pepole
			UserControl_ResponseDownLoadUserInfo ur=(UserControl_ResponseDownLoadUserInfo)( GetResponseBase() );
			ur.setDownloadMyself(iSDownloadMyself);
			SendRequestByPost(sRequestUrl,post,true);
			return true;
	}


	public boolean DownloadByMail(String email,boolean iSDownloadMyself) {
		

		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl+="accounts/profile/querydetail";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();	
		params.add(new BasicNameValuePair("email",email));
		
		PostData post = new PostData();
		post.setPostData(params);
		UserControl_ResponseDownLoadUserInfo ur=(UserControl_ResponseDownLoadUserInfo)( GetResponseBase() );
		ur.setDownloadMyself(iSDownloadMyself);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}
	
	public boolean DownloadByPhone(String phoneNum,boolean iSDownloadMyself) {
		
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl+="accounts/profile/querydetail?";
			sRequestUrl+="cellphone="+phoneNum;
			//we should known whose imformation we need ,myself or other pepole
			UserControl_ResponseDownLoadUserInfo ur=(UserControl_ResponseDownLoadUserInfo)( GetResponseBase() );
			ur.setDownloadMyself(iSDownloadMyself);
			SendRequestByGet(sRequestUrl,true);
			return true;

	}

}
