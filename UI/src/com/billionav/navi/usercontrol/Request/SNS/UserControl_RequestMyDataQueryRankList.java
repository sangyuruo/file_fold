package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_RequestMyDataQueryRankList extends UserControl_RequestSNSBase{
	private String m_strType;
	private int m_iLen = UserControl_CommonVar.POST_QUERY_DEFAULT_MAXLEN_20;
	private int m_iOffset = UserControl_CommonVar.POST_QUERY_DEFAULT_OFFLEN;
	private String m_strPriodEnd;
	private String m_strPriodStart;
	
	public UserControl_RequestMyDataQueryRankList(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestQueryRankList() {
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "mydata/travel/getsaferank";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();	
		
		if (UserControl_CommonVar.MYDATA_GETSAFERANK_TYPE_PERIOD.equals(m_strType) && "".equals(m_strPriodStart)) {
			return false;
		}
		
		params.add(new BasicNameValuePair("type", m_strType));
		params.add(new BasicNameValuePair("page_offset", "" + m_iOffset));
		params.add(new BasicNameValuePair("page_size",  m_iLen+""));
		params.add(new BasicNameValuePair("end",  m_strPriodEnd));
		params.add(new BasicNameValuePair("start",m_strPriodStart));
		
		PostData post = new PostData();
		post.setPostData(params);
		
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public String getM_strType() {
		return m_strType;
	}

	public void setM_strType(String m_strType) {
		this.m_strType = m_strType;
	}

	public int getM_iLen() {
		return m_iLen;
	}

	public void setM_iLen(int m_iLen) {
		this.m_iLen = m_iLen;
	}

	public String getM_strPriodEnd() {
		return m_strPriodEnd;
	}

	public void setM_strPriodEnd(String m_strPriodEnd) {
		this.m_strPriodEnd = m_strPriodEnd;
	}

	public String getM_strPriodStart() {
		return m_strPriodStart;
	}

	public void setM_strPriodStart(String m_strPriodStart) {
		this.m_strPriodStart = m_strPriodStart;
	}

	public int getM_iOffset() {
		return m_iOffset;
	}

	public void setM_iOffset(int m_iOffset) {
		this.m_iOffset = m_iOffset;
	}
	
	
	
}
