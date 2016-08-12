package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;

public class UserControl_RequestGroupEditGroup extends UserControl_RequestSNSBase{
	String m_StrGroupID = "";
	String m_StrGroupName = "";
	String m_StrGroupDiscription = "";
	String m_StrGroupTags = "";
	
	public UserControl_RequestGroupEditGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendUpdateGroupRequest() {
		
		if("".equals(m_StrGroupID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/update";
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("id",m_StrGroupID));
		params.add(new BasicNameValuePair("name",m_StrGroupName));
		params.add(new BasicNameValuePair("description",m_StrGroupDiscription));
		params.add(new BasicNameValuePair("tags",m_StrGroupTags));
		PostData post = new PostData();
		post.setPostData(params);
		setM_strPassParam01(m_StrGroupID);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public String getM_StrGroupID() {
		return m_StrGroupID;
	}

	public void setM_StrGroupID(String m_StrGroupID) {
		this.m_StrGroupID = m_StrGroupID;
	}

	public String getM_StrGroupName() {
		return m_StrGroupName;
	}

	public void setM_StrGroupName(String m_StrGroupName) {
		this.m_StrGroupName = m_StrGroupName;
	}

	public String getM_StrGroupDiscription() {
		return m_StrGroupDiscription;
	}

	public void setM_StrGroupDiscription(String m_StrGroupDiscription) {
		this.m_StrGroupDiscription = m_StrGroupDiscription;
	}

	public String getM_StrGroupTags() {
		return m_StrGroupTags;
	}

	public void setM_StrGroupTags(String m_StrGroupTags) {
		this.m_StrGroupTags = m_StrGroupTags;
	}
	
	
}
