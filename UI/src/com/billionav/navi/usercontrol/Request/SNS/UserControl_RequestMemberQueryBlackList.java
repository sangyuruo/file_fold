package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_RequestMemberQueryBlackList extends UserControl_RequestSNSBase{
	
	private int m_iOffset 	= UserControl_CommonVar.POST_QUERY_DEFAULT_OFFLEN;
	private int m_Maxlen 	= UserControl_CommonVar.POST_QUERY_DEFAULT_MAXLEN_20;
	
	public UserControl_RequestMemberQueryBlackList(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestQueryBlackList() {
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "accounts/queryblacks";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("offset",m_iOffset+""));
		params.add(new BasicNameValuePair("maxlen",m_Maxlen+""));
		PostData post = new PostData();
		post.setPostData(params);
		
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public int getM_iOffset() {
		return m_iOffset;
	}

	public void setM_iOffset(int m_iOffset) {
		this.m_iOffset = m_iOffset;
	}

	public int getM_Maxlen() {
		return m_Maxlen;
	}

	public void setM_Maxlen(int m_Maxlen) {
		this.m_Maxlen = m_Maxlen;
	}
	
	

}
