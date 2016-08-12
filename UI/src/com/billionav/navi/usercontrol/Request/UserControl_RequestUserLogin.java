package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.uitools.AES;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseUserLogin;

public class UserControl_RequestUserLogin extends UserControl_RequestBase {
	public UserControl_RequestUserLogin(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean LoginByName(String nickName, String password){
		return LoginByName(nickName,password,"1",null);
	}
	public boolean LoginByPhone(String phoneNum, String password){
		return LoginByPhone(phoneNum,password,"1",null);
	}
	public boolean LoginByMail(String email, String password){
		return LoginByMail(email,password,"1",null);
	}
	
	public boolean LoginByName(String nickName, String password, String DeviceNo){
		return LoginByName(nickName,password,"1", DeviceNo);
	}
	public boolean LoginByPhone(String phoneNum, String password, String DeviceNo){
		return LoginByPhone(phoneNum,password,"1", DeviceNo);
	}
	public boolean LoginByMail(String email, String password, String DeviceNo){
		return LoginByMail(email,password,"1", DeviceNo);
	}
	public boolean LoginByIMEI(String IMEI) {
		return LoginByIMEI(IMEI, "2");
	}
	public boolean LoginByIMEI(String IMEI, String type) {
		UserControl_ResponseBase cResponse = GetResponseBase();
		
		if(null != cResponse) {
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl += "auth/thirdLogin/";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if(null != IMEI) {
				String encodeIMEI = "";
				try {
					encodeIMEI = 
					AES.Encrypt(IMEI, AES.cKey);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				params.add(new BasicNameValuePair("udid", encodeIMEI));
			}
			if(null != type) {
				params.add(new BasicNameValuePair("type", type));
			}
			PostData post = new PostData();
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;
		}
		
		return false;
	}
	public boolean LoginByName(String nickName, String password,String type, String deviceNo) {
		UserControl_ResponseBase cResponse = GetResponseBase();
		
		if (null != cResponse) {
			UserControl_UserInfo userInfo = new UserControl_UserInfo();
			userInfo.m_strNickName = nickName;
			userInfo.m_strUserPassWord = password;
			
			((UserControl_ResponseUserLogin)cResponse).SetUserInfo(userInfo);
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl +="userauth/login/";
			List<NameValuePair> params = new ArrayList<NameValuePair>();	
			params.add(new BasicNameValuePair("user",nickName));
			params.add(new BasicNameValuePair("pwd",password));	
			params.add(new BasicNameValuePair("type",type));
			if (!"".equals(deviceNo) && null != deviceNo) {
				params.add(new BasicNameValuePair("deviceno", deviceNo));
			}
			params.add(new BasicNameValuePair("mask", "1"));
			PostData post = new PostData();	
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;
		}

		return false;
	}
	
	public boolean LoginByPhone(String phoneNum, String password,String type, String deviceNo) {
		UserControl_ResponseBase cResponse = GetResponseBase();
		
		if (null != cResponse) {
			UserControl_UserInfo userInfo = new UserControl_UserInfo();
			userInfo.m_strPhoneNum = phoneNum;
			userInfo.m_strUserPassWord = password;
			((UserControl_ResponseUserLogin)cResponse).SetUserInfo(userInfo);
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl +="userauth/login/";
			List<NameValuePair> params = new ArrayList<NameValuePair>();	
			params.add(new BasicNameValuePair("user",phoneNum));
			params.add(new BasicNameValuePair("pwd",password));	
			params.add(new BasicNameValuePair("type",type));
			if (!"".equals(deviceNo) && null != deviceNo) {
				params.add(new BasicNameValuePair("deviceno", deviceNo));
			}
			params.add(new BasicNameValuePair("mask", "1"));
			PostData post = new PostData();	
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;
		}

		return false;
	}

	
	public boolean LoginByMail(String email, String password, String type, String deviceNo) {
		UserControl_ResponseBase cResponse = GetResponseBase();	
		if (null != cResponse) {
			UserControl_UserInfo userInfo = new UserControl_UserInfo();
			userInfo.m_strEmail = email;
			userInfo.m_strUserPassWord = password;	
			((UserControl_ResponseUserLogin)cResponse).SetUserInfo(userInfo);
			String sRequestUrl = GetAuthServerUrl();
			sRequestUrl +="userauth/login/";
			List<NameValuePair> params = new ArrayList<NameValuePair>();	
			params.add(new BasicNameValuePair("user",email));
			params.add(new BasicNameValuePair("pwd",password));	
			params.add(new BasicNameValuePair("type",type));
			if (!"".equals(deviceNo) && null != deviceNo) {
				params.add(new BasicNameValuePair("deviceno", deviceNo));
			}
			params.add(new BasicNameValuePair("mask", "1"));
			PostData post = new PostData();	
			post.setPostData(params);
			SendRequestByPost(sRequestUrl,post,false);
			return true;
		}

		return false;
	}
	
}
