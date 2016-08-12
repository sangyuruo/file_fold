package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;

public class UserControl_RequestMyDataQueryUserStyle extends UserControl_RequestSNSBase{
	
	private String m_strStartData;
	private String m_strSummryType;
	private String m_strEndData;
	
	public UserControl_RequestMyDataQueryUserStyle(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestGetUserStyleInfo() {
		if ( (!"".equals(m_strStartData) && !"".equals(m_strSummryType)) 
				|| ( !"".equals(m_strStartData) && !"".equals(m_strEndData)) ){	
			
			String sRequestUrl = GetServerUrl();
			sRequestUrl += "mydata/travel/getsafereport";
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();	
			
			params.add(new BasicNameValuePair("period_lb", m_strStartData));
			params.add(new BasicNameValuePair("period_type",  m_strSummryType));
			params.add(new BasicNameValuePair("period_ub",  m_strEndData));
			
			PostData post = new PostData();
			post.setPostData(params);
	
			SendRequestByPost(sRequestUrl,post,true);
			return true;
		}
		return false;
	}

	public String getM_strStartData() {
		return m_strStartData;
	}

	public void setM_strStartData(String m_strStartData) {
		this.m_strStartData = m_strStartData;
	}

	public String getM_strSummryType() {
		return m_strSummryType;
	}

	public void setM_strSummryType(String m_strSummryType) {
		this.m_strSummryType = m_strSummryType;
	}

	public String getM_strEndData() {
		return m_strEndData;
	}

	public void setM_strEndData(String m_strEndData) {
		this.m_strEndData = m_strEndData;
	}
	
	
}
