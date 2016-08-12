package com.billionav.navi.usercontrol.Request.SNS;

import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;

public class UserControl_RequestMemberJoinGroup  extends UserControl_RequestSNSBase{
	String m_strCycleID = "";
	
	public UserControl_RequestMemberJoinGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestJoinGroup(){
		if ("".equals(m_strCycleID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/apply/join?id=" + m_strCycleID;
		setM_strPassParam01(m_strCycleID);
		
		SendRequestByGet(sRequestUrl,true);
		return true;
	}

	public String getM_strCycleID() {
		return m_strCycleID;
	}

	public void setM_strCycleID(String m_strCycleID) {
		this.m_strCycleID = m_strCycleID;
	}
	
	
}
