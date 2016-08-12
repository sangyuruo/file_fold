package com.billionav.navi.usercontrol.Response;

import com.billionav.navi.net.PASyncRequest;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class UserControl_ResponseBase extends PASyncRequest {
	public static final int UC_RESPONES_SUC = 0;		//suc
	public static final int UC_RESPONES_SRV_FAIL = 1;	// Service fail
	public static final int UC_RESPONES_LOC_FAIL = 2;	//Local fail
	public static final int UC_MAX_ERR_CODE_NUM = 10;//max number of error code allowed
	private long m_lUserRequestID = UserControl_ManagerIF.INVALIDATE_REQUESTID;
	
	private int m_iPassedParam;
	private int m_strPassedParam;

	private int m_iRequestId = 0;	  
	public UserControl_ResponseBase(int iRequestId) {
		m_iRequestId = iRequestId;
	}
	public void onRecvData(){
		doResponse();
		onEndOfResponse();
	}
	
	private void onEndOfResponse() {
		UserControl_ResponseFactory.Instance().RemoveUserRequest(m_lUserRequestID);
	}
	
	public void doResponse() {
	}

	public int getUserRequestId() {
		return m_iRequestId;
	}
	public int getM_iPassedParam() {
		return m_iPassedParam;
	}
	public void setM_iPassedParam(int m_iPassedParam) {
		this.m_iPassedParam = m_iPassedParam;
	}
	public int getM_strPassedParam() {
		return m_strPassedParam;
	}
	public void setM_strPassedParam(int m_strPassedParam) {
		this.m_strPassedParam = m_strPassedParam;
	}
	public long getM_lUserRequestID() {
		return m_lUserRequestID;
	}
	public void setM_lUserRequestID(long m_lUserRequestID) {
		this.m_lUserRequestID = m_lUserRequestID;
	}
}
