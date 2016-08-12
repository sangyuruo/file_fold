package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_BlackList {
	private int m_iOffset;
	private int m_iCount;
	
	private ArrayList<String> m_cListUserID = null;

	public int getM_iOffset() {
		return m_iOffset;
	}

	public void setM_iOffset(int m_iOffset) {
		this.m_iOffset = m_iOffset;
	}

	public int getM_iCount() {
		return m_iCount;
	}

	public void setM_iCount(int m_iCount) {
		this.m_iCount = m_iCount;
	}

	public ArrayList<String> getM_cListUserID() {
		return m_cListUserID;
	}

	public void setM_cListUserID(ArrayList<String> m_cListUserID) {
		this.m_cListUserID = m_cListUserID;
	}
	
	
}
