package com.billionav.navi.datasynccontrol.Response;
import com.billionav.navi.datasynccontrol.DataSyncControl_ManagerIF;
import com.billionav.navi.net.PASyncRequest;

public class DataSyncControl_ResponseBase extends PASyncRequest {
	public static final int UC_RESPONES_SUC = 0;		//suc
	public static final int UC_RESPONES_SRV_FAIL = 1;	// Service fail
	public static final int UC_RESPONES_LOC_FAIL = 2;	//Local fail
	public static final int UC_MAX_ERR_CODE_NUM = 10;//max number of error code allowed
	public static final int UC_DETAILS_XML_PRASE_ERROR = 30;	//XML Prase error
	private long m_lUserRequestID = DataSyncControl_ManagerIF.INVALIDATE_REQUESTID;
	
	private int m_iRequestId = 0;
	private String m_StrPassParam01	= "";
	
	public DataSyncControl_ResponseBase(int iRequestId) {
		m_iRequestId = iRequestId;
	}
	public void onRecvData(){
		doResponse();
		onEndOfResponse();
	}
	
	public void doResponse() {
		
	}
	
	public void onEndOfResponse(){
		DataSyncControl_ResponseFactory.Instance().RemoveUserRequest(m_lUserRequestID);
	}

	public int GetRequestId() {
		return m_iRequestId;
	}
	
	public String getM_StrPassParam01() {
		return m_StrPassParam01;
	}
	public void setM_StrPassParam01(String m_StrPassParam01) {
		this.m_StrPassParam01 = m_StrPassParam01;
	}
	public long getM_lUserRequestID() {
		return m_lUserRequestID;
	}
	public void setM_lUserRequestID(long m_lUserRequestID) {
		this.m_lUserRequestID = m_lUserRequestID;
	}
	
}