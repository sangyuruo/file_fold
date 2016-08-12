package com.billionav.navi.versioncontrol.Request;

import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.net.PostData;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseBase;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseFactory;


public class VersionControl_RequestBase {
	
	private VersionControl_ResponseBase m_cResponse= null;

	private String m_strServerUrl;
	private int m_iRequestId = 0;
	private long m_lVersionControlRequestID;
	private int m_iPassedParam;
	private String m_strPassedParam;
	
	public VersionControl_RequestBase(int iRequestId){
		m_iRequestId = iRequestId;
		if (null == m_cResponse) {
			m_cResponse = VersionControl_ResponseFactory.Instance().CreateResponse(iRequestId);
		}

		m_strServerUrl = VersionControl_ManagerIF.Instance().getM_strVersionServerUrl();
	}
	
	private void PassParam() {
		m_cResponse.setM_iPassedParam(m_iPassedParam);
		m_cResponse.setM_strPassedParam(m_strPassedParam);
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

	public int getM_iRequestId() {
		return m_iRequestId;
	}

	public void setM_iRequestId(int m_iRequestId) {
		this.m_iRequestId = m_iRequestId;
	}

	public String getM_strServerUrl() {
		return m_strServerUrl;
	}

	public void setM_strServerUrl(String m_strServerUrl) {
		this.m_strServerUrl = m_strServerUrl;
	}

	public long getM_lVersionControlRequestID() {
		return m_cResponse.getM_lVersionControlRequestID();
	}

	public void setM_lVersionControlRequestID(long m_lVersionControlRequestID) {
		this.m_lVersionControlRequestID = m_lVersionControlRequestID;
	}

	public int getM_iPassedParam() {
		return m_iPassedParam;
	}

	public void setM_iPassedParam(int m_iPassedParam) {
		this.m_iPassedParam = m_iPassedParam;
	}

	public String getM_strPassedParam() {
		return m_strPassedParam;
	}

	public void setM_strPassedParam(String m_strPassedParam) {
		this.m_strPassedParam = m_strPassedParam;
	}

	
	
}
