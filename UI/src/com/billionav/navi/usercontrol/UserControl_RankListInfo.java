package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_RankListInfo {
	private String m_strPageSize;
	private String m_strType;
	private String m_strStart;
	private String m_strEnd;
	private String m_strOffset;
	
	private ArrayList<UserControl_RankListItem> items = null;
	
	public String getM_strType() {
		return m_strType;
	}

	public void setM_strType(String m_strType) {
		this.m_strType = m_strType;
	}

	public String getM_strStart() {
		return m_strStart;
	}

	public void setM_strStart(String m_strStart) {
		this.m_strStart = m_strStart;
	}

	public String getM_strEnd() {
		return m_strEnd;
	}

	public void setM_strEnd(String m_strEnd) {
		this.m_strEnd = m_strEnd;
	}

	public ArrayList<UserControl_RankListItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<UserControl_RankListItem> items) {
		this.items = items;
	}

	public String getM_strPageSize() {
		return m_strPageSize;
	}

	public void setM_strPageSize(String m_strPageSize) {
		this.m_strPageSize = m_strPageSize;
	}

	public String getM_strOffset() {
		return m_strOffset;
	}

	public void setM_strOffset(String m_strOffset) {
		this.m_strOffset = m_strOffset;
	}
	
}
