package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;

public class UserControl_RequestGroupQueryGroups extends UserControl_RequestSNSBase{
	
	String m_strGroupID = "";
	String m_strGroupName = "";
	String m_strGroupOwner = "";
	String m_strGroupType = "";
	String m_strGroupTags = "";
	String m_strGroupOffset = "";
	String m_strGroupMaxLen = "";
	
	
	public UserControl_RequestGroupQueryGroups(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendQueryGroupsRequest() {
		
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/query";
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("id",m_strGroupID));
		params.add(new BasicNameValuePair("name",m_strGroupName));
		params.add(new BasicNameValuePair("owner",m_strGroupOwner));
		params.add(new BasicNameValuePair("type",m_strGroupType));
		params.add(new BasicNameValuePair("tags",m_strGroupTags));
		params.add(new BasicNameValuePair("offset",m_strGroupOffset));
		params.add(new BasicNameValuePair("maxlen",m_strGroupMaxLen));
		
		setM_strPassParam01(m_strGroupID);
		PostData post = new PostData();
		post.setPostData(params);
		SendRequestByPost(sRequestUrl,post,true);
		
		return true;
	}

	public String getM_strGroupID() {
		return m_strGroupID;
	}

	public void setM_strGroupID(String m_strGroupID) {
		this.m_strGroupID = m_strGroupID;
	}

	public String getM_strGroupName() {
		return m_strGroupName;
	}

	public void setM_strGroupName(String m_strGroupName) {
		this.m_strGroupName = m_strGroupName;
	}

	public String getM_strGroupOwner() {
		return m_strGroupOwner;
	}

	public void setM_strGroupOwner(String m_strGroupOwner) {
		this.m_strGroupOwner = m_strGroupOwner;
	}

	public String getM_strGroupType() {
		return m_strGroupType;
	}

	public void setM_strGroupType(String m_strGroupType) {
		this.m_strGroupType = m_strGroupType;
	}

	public String getM_strGroupTags() {
		return m_strGroupTags;
	}

	public void setM_strGroupTags(String m_strGroupTags) {
		this.m_strGroupTags = m_strGroupTags;
	}

	public String getM_strGroupOffset() {
		return m_strGroupOffset;
	}

	public void setM_strGroupOffset(String m_strGroupOffset) {
		this.m_strGroupOffset = m_strGroupOffset;
	}

	public String getM_strGroupMaxLen() {
		return m_strGroupMaxLen;
	}

	public void setM_strGroupMaxLen(String m_strGroupMaxLen) {
		this.m_strGroupMaxLen = m_strGroupMaxLen;
	}

	
}
