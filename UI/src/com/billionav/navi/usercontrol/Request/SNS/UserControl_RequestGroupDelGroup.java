package com.billionav.navi.usercontrol.Request.SNS;


public class UserControl_RequestGroupDelGroup extends UserControl_RequestSNSBase{
	private String m_strGroupID;
	
	public UserControl_RequestGroupDelGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendDelGroupRequest() {
		//TODO
		if ("".equals(m_strGroupID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/delete?id="+m_strGroupID;
		setM_strPassParam01(m_strGroupID);
		SendRequestByGet(sRequestUrl, true);
		return true;
	}

	public String getM_strGroupID() {
		return m_strGroupID;
	}

	public void setM_strGroupID(String m_strGroupID) {
		this.m_strGroupID = m_strGroupID;
	}
	
	

}
