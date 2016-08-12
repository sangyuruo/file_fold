package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;

public class UserControl_RequestMemberExitGroup extends UserControl_RequestSNSBase{
	String m_strCycleID = "";
	String m_strUser = "";
	
	public UserControl_RequestMemberExitGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestExitGroup() {
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/detachuser";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("circleid",m_strCycleID));
		params.add(new BasicNameValuePair("user",m_strUser));
		PostData post = new PostData();
		post.setPostData(params);
		setM_strPassParam01(m_strCycleID);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public String getM_strCycleID() {
		return m_strCycleID;
	}

	public void setM_strCycleID(String m_strCycleID) {
		this.m_strCycleID = m_strCycleID;
	}

	public String getM_strUser() {
		return m_strUser;
	}

	public void setM_strUser(String m_strUser) {
		this.m_strUser = m_strUser;
	}
	
	
}
