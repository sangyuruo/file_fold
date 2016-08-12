package com.billionav.navi.datasynccontrol.Request;

import com.billionav.navi.datasynccontrol.Response.DataSyncControl_ResponseBase;
import com.billionav.navi.datasynccontrol.Response.DataSyncControl_ResponseFactory;
import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class DataSyncControl_RequestBase{
	
	private DataSyncControl_ResponseBase m_cResponse = null;
	private int m_iRequestId = 0;
	private String m_strServerUrl ; 
	protected boolean m_bAuth = false;
	private String m_StrPassParam01 = "";
	private long m_iUserRequestID;
	  
	public DataSyncControl_RequestBase(int iRequestId){
		m_iRequestId = iRequestId;
		if (null == m_cResponse) {
			m_cResponse = DataSyncControl_ResponseFactory.Instance().CreateResponse(iRequestId);
		}
		
		m_strServerUrl = UserControl_ManagerIF.Instance().GetSRVUrl();
	}
	
	public void SendRequestByPostResponseFile(String url,PostData postData) {
		SendRequestByPost(url,postData,PRequest.RESPONSE_DATA_FILE);
	}
	
	public void SendRequestByPostResponseStream(String url,PostData postData) {
		SendRequestByPost(url,postData,PRequest.RESPONSE_DATA_BUF);
	}
	
	public void SendRequestByPost(String url,PostData postData,int RequestDataType) {
		if (null != m_cResponse) {
			m_cResponse.setURL(url);
			m_cResponse.setMethod(PRequest.METHODS_POST);
			m_cResponse.setResDataType(RequestDataType); 
			m_cResponse.setAuthFlag(m_bAuth);
			m_cResponse.setPostData(postData);
			m_cResponse.setM_StrPassParam01(m_StrPassParam01);
			PThreadManager.instance().PostRequest(m_cResponse);
		}
	}

	public void SendRequestByGet(String url)
	{
		if (null != m_cResponse) {
			m_cResponse.setURL(url);
			m_cResponse.setMethod(PRequest.METHODS_GET);
			m_cResponse.setResDataType(PRequest.RESPONSE_DATA_BUF); 
			m_cResponse.setAuthFlag(m_bAuth);
			m_cResponse.setM_StrPassParam01(m_StrPassParam01);
			PThreadManager.instance().PostRequest(m_cResponse);
		}
	}
	
	
	public String GetServerUrl() {
		return m_strServerUrl;
	}
	
	public int GetRequestId() {
		return m_iRequestId;
	}
	
	public DataSyncControl_ResponseBase GetResponseBase() {
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