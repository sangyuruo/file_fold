package com.billionav.navi.usercontrol.Request.SNS;


public class UserControl_RequestMemberApprovalJoinGroup extends UserControl_RequestSNSBase{

	String m_strID;
	
	public UserControl_RequestMemberApprovalJoinGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestApprovalJoinGroup() {
		if ("".equals(m_strID)) {
			return false;
		}
		
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/approval/join?id=" + m_strID;
		setM_strPassParam01(m_strID);
		
		SendRequestByGet(sRequestUrl,true);
		return true;
	}

	public String getM_strID() {
		return m_strID;
	}

	public void setM_strID(String m_strID) {
		this.m_strID = m_strID;
	}
}
