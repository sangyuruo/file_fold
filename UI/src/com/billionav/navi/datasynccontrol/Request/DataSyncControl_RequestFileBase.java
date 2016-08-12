package com.billionav.navi.datasynccontrol.Request;

import com.billionav.navi.datasynccontrol.Response.DataSyncControl_ResponseFileBase;
import com.billionav.navi.net.PRequest;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.net.PostData;

public class DataSyncControl_RequestFileBase extends
		DataSyncControl_RequestBase {
	String m_strFilePath = "";
	String m_strResFileName = "";
	int m_iSyncType;
	
	public DataSyncControl_RequestFileBase(int iRequestId) {
		super(iRequestId);
	}
	
	
	public void SendRequestByPost(String url,PostData postData,int RequestDataType) {
		DataSyncControl_ResponseFileBase cResponse= (DataSyncControl_ResponseFileBase)GetResponseBase();
		if (null != cResponse) {
			cResponse.setURL(url);
			cResponse.setMethod(PRequest.METHODS_POST);
			cResponse.setResDataType(RequestDataType); 
			cResponse.setAuthFlag(m_bAuth);
			cResponse.setM_strFilePath(getM_strFilePath());
			cResponse.setM_strResFileName(m_strResFileName);
			cResponse.setM_iSyncType(m_iSyncType);
			cResponse.setM_StrPassParam01(getM_StrPassParam01());
			cResponse.setPostData(postData);
			PThreadManager.instance().PostRequest(cResponse);
		}
	}

	public void SendRequestByGet(String url)
	{
		DataSyncControl_ResponseFileBase  cResponse= (DataSyncControl_ResponseFileBase)GetResponseBase();
		if (null != cResponse) {
			cResponse.setURL(url);
			cResponse.setMethod(PRequest.METHODS_GET);
			cResponse.setM_strFilePath(getM_strFilePath());
			cResponse.setResDataType(PRequest.RESPONSE_DATA_BUF); 
			cResponse.setM_StrPassParam01(getM_StrPassParam01());
			PThreadManager.instance().PostRequest(cResponse);
		}
	}

	public String getM_strFilePath() {
		return m_strFilePath;
	}

	public void setM_strFilePath(String m_strFilePath) {
		this.m_strFilePath = m_strFilePath;
	}


	public String getM_strResFileName() {
		return m_strResFileName;
	}

	public void setM_strResFileName(String m_strResFileName) {
		this.m_strResFileName = m_strResFileName;
	}


	public int getM_iSyncType() {
		return m_iSyncType;
	}

	public void setM_iSyncType(int m_iSyncType) {
		this.m_iSyncType = m_iSyncType;
	}
	
	
}
