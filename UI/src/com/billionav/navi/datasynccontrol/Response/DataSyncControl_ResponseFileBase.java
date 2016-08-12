package com.billionav.navi.datasynccontrol.Response;

public class DataSyncControl_ResponseFileBase extends
		DataSyncControl_ResponseBase {
	private String m_strFilePath;
	private String m_strResFileName;
	private int	   m_iSyncType;
	
	public DataSyncControl_ResponseFileBase(int iRequestId) {
		super(iRequestId);
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
