package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_PosterListInfo {
	private int		m_iCount;
	private int		m_iOffset;
	private ArrayList<UserControl_SimplePosterInfo> m_cListPoster = null;
	
	public int getM_iCount() {
		return m_iCount;
	}
	public void setM_iCount(int m_iCount) {
		this.m_iCount = m_iCount;
	}
	public int getM_iOffset() {
		return m_iOffset;
	}
	public void setM_iOffset(int m_iOffset) {
		this.m_iOffset = m_iOffset;
	}
	public ArrayList<UserControl_SimplePosterInfo> getM_cListPoster() {
		return m_cListPoster;
	}
	public void setM_cListPoster(
			ArrayList<UserControl_SimplePosterInfo> m_cListPoster) {
		this.m_cListPoster = m_cListPoster;
	}
}


