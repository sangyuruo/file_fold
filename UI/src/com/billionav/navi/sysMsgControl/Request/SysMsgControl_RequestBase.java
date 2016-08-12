package com.billionav.navi.sysMsgControl.Request;

import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.net.PostData;
import com.billionav.navi.sysMsgControl.SysMsgControl_ManagerIF;
import com.billionav.navi.sysMsgControl.Response.SysMsgControl_ResponseBase;
import com.billionav.navi.sysMsgControl.Response.SysMsgControl_ResponseFactory;

public class SysMsgControl_RequestBase {

	private SysMsgControl_ResponseBase m_cResponse = null;
	private int m_iRequestId = 0;
	private String m_strServerUrl ; 
	protected boolean m_bAuth = false;
	private String m_StrPassParam01 = "";
	private long m_iUserRequestID;
	  
	public SysMsgControl_RequestBase(int iRequestId){
		m_iRequestId = iRequestId;
		if (null == m_cResponse) {
			m_cResponse = SysMsgControl_ResponseFactory.Instance().CreateResponse(iRequestId);
		}
		
		m_strServerUrl = SysMsgControl_ManagerIF.getInstance().getM_strMsgServerUrl();
	}
	private void PassParam() {
		m_cResponse.setM_StrPassParam01(m_StrPassParam01);
	}
	public void SendRequestByPost(String url,PostData postData,boolean HasSessionToken) {
		if (null != m_cResponse) {
			m_cResponse.setURL(url);
			m_cResponse.setMethod(PRequest.METHODS_POST);
			m_cResponse.setResDataType(PRequest.RESPONSE_DATA_BUF); 	
			m_cResponse.setAuthFlag(HasSessionToken);
			m_cResponse.setPostData(postData);
			PassParam();
			PThreadManager.instance().PostRequest(m_cResponse);
		}
	}

	public void SendRequestByGet(String url,boolean HasSessionToken)
	{
		if (null != m_cResponse) {
			m_cResponse.setURL(url);
			m_cResponse.setMethod(PRequest.METHODS_GET);
			m_cResponse.setResDataType(PRequest.RESPONSE_DATA_BUF); 
			m_cResponse.setAuthFlag(HasSessionToken);
			PassParam();
			PThreadManager.instance().PostRequest(m_cResponse);
		}
	}
	
	public String GetServerUrl() {
		return m_strServerUrl;
	}
	
	public int GetRequestId() {
		return m_iRequestId;
	}
	
	public SysMsgControl_ResponseBase GetResponseBase() {
		return m_cResponse;
	}

	public boolean isM_bAuth() {
		return m_bAuth;
	}

	public void setM_bAuth(boolean m_bAuth) {
		this.m_bAuth = m_bAuth;
	}

	public String getM_StrPassParam01() {
		return m_StrPassParam01;
	}

	public void setM_StrPassParam01(String m_StrPassParam01) {
		this.m_StrPassParam01 = m_StrPassParam01;
	}

	public long getM_iUserRequestID() {
		return m_cResponse.getM_lUserRequestID();
	}

	public void setM_iUserRequestID(long m_iUserRequestID) {
		this.m_iUserRequestID = m_iUserRequestID;
	}
	
	
}
