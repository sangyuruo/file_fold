package com.billionav.navi.usercontrol.Request.SNS;

import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseSNSBase;

public class UserControl_RequestSNSBase extends UserControl_RequestBase{
	
	protected String m_strPassParam01;
	protected int	 m_iPassParam01;
	
	public UserControl_RequestSNSBase(int iRequestId) {
		super(iRequestId);
	}

	public void SendRequestByPost(String url,PostData postData,boolean HasSessionToken) {
		UserControl_ResponseSNSBase response = (UserControl_ResponseSNSBase)GetResponseBase();
		if (null != response) {
			response.setURL(url);
			response.setMethod(PRequest.METHODS_POST);
			response.setResDataType(PRequest.RESPONSE_DATA_BUF); 	
			response.setAuthFlag(HasSessionToken);
			response.setPostData(postData);
			response.setM_strPassParam01(m_strPassParam01);
			response.setM_iPassParam01(m_iPassParam01);
			
			UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_SEND, url);
			PThreadManager.instance().PostRequest(response);
		}
	}

	public void SendRequestByGet(String url,boolean HasSessionToken)
	{
		UserControl_ResponseSNSBase response = (UserControl_ResponseSNSBase)GetResponseBase();
		if (null != response) {
			response.setURL(url);
			response.setMethod(PRequest.METHODS_GET);
			response.setResDataType(PRequest.RESPONSE_DATA_BUF); 
			response.setAuthFlag(HasSessionToken);
			response.setM_strPassParam01(m_strPassParam01);
			response.setM_iPassParam01(m_iPassParam01);
			
			UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_SEND, url);
			PThreadManager.instance().PostRequest(response);
		}
	}
	
	public String getM_strPassParam01() {
		return m_strPassParam01;
	}

	public void setM_strPassParam01(String m_strPassParam01) {
		this.m_strPassParam01 = m_strPassParam01;
	}

	public int getM_iPassParam01() {
		return m_iPassParam01;
	}

	public void setM_iPassParam01(int m_iPassParam01) {
		this.m_iPassParam01 = m_iPassParam01;
	}
	
	
	

}
