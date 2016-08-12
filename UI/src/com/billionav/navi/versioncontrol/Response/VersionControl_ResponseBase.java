package com.billionav.navi.versioncontrol.Response;

import com.billionav.navi.net.PASyncRequest;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;

public class VersionControl_ResponseBase extends PASyncRequest {
	public static final int UC_RESPONES_SUC = 0;		//suc
	public static final int UC_RESPONES_SRV_FAIL = 1;	// Service fail
	public static final int UC_RESPONES_LOC_FAIL = 2;	//Local fail
	public static final int UC_MAX_ERR_CODE_NUM = 10;//max number of error code allowed
	public static final int UC_DETAILS_XML_PRASE_ERROR = 30;	//XML Prase error
	public static final int UC_DETAILS_JSON_PRASE_ERROR = 31;	//JSON Prase error
	public static final int UC_DETAILS_SERVER_DATA_PRASE_ERROR = 32;	//JSON Prase error
	public static final int UC_DETAILS_NO_VERSION = 33; 	// No Latest Version 
	
	private int m_iRequestId = 0;
	private long m_lVersionControlRequestID = VersionControl_ManagerIF.INVALIDATE_REQUESTID;
	private int m_iPassedParam;
	private String m_strPassedParam;
	
	public VersionControl_ResponseBase(int iRequestId) {
		m_iRequestId = iRequestId;
	}
	
	public void onRecvData(){
		doResponse();
		onEndOfResponse();
	}
	
	public void doResponse() {
		
	}
	
	public void onEndOfResponse(){
		VersionControl_ResponseFactory.Instance().RemoveUserRequest(m_lVersionControlRequestID);
	}
	
	public int getM_iRequestId() {
		return m_iRequestId;
	}

	public void setM_iRequestId(int m_iRequestId) {
		this.m_iRequestId = m_iRequestId;
	}

	public long getM_lVersionControlRequestID() {
		return m_lVersionControlRequestID;
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
