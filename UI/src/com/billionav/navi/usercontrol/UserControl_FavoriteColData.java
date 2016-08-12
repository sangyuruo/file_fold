package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_FavoriteColData {
	private ArrayList<UserControl_FavoriteItem>  m_favoriteList = null;
	private int m_iCount;
	private int m_iOffset;
	
	public ArrayList<UserControl_FavoriteItem> getM_favoriteList() {
		return m_favoriteList;
	}
	public void setM_favoriteList(ArrayList<UserControl_FavoriteItem> m_favoriteList) {
		this.m_favoriteList = m_favoriteList;
	}
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
	
	
}
