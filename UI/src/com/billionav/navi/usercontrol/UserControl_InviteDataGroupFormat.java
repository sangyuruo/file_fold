package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_InviteDataGroupFormat {
	
	private String strCycleID;
	
	ArrayList<UserControl_InviteDataUserFormat> m_cListInvitaUsers = null;

	public ArrayList<UserControl_InviteDataUserFormat> getM_cListInvitaUsers() {
		return m_cListInvitaUsers;
	}

	public void setM_cListInvitaUsers(
			ArrayList<UserControl_InviteDataUserFormat> m_cListInvitaUsers) {
		this.m_cListInvitaUsers = m_cListInvitaUsers;
	}

	public String getStrCycleID() {
		return strCycleID;
	}

	public void setStrCycleID(String strCycleID) {
		this.strCycleID = strCycleID;
	}

	
	
}
