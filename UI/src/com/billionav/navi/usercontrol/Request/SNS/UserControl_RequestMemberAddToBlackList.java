package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;

public class UserControl_RequestMemberAddToBlackList extends UserControl_RequestSNSBase{
	String m_strUserID = "";
	
	public UserControl_RequestMemberAddToBlackList(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestAddToBlackList() {

		if ("".equals(m_strUserID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "accounts/block";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("user",m_strUserID));
		PostData post = new PostData();
		post.setPostData(params);
		setM_strPassParam01(m_strUserID);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public String getM_strUserID() {
		return m_strUserID;
	}

	public void setM_strUserID(String m_strUserID) {
		this.m_strUserID = m_strUserID;
	}
	
	
}
