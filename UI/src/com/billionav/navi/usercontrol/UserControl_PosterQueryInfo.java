package com.billionav.navi.usercontrol;

public class UserControl_PosterQueryInfo {
	private String m_strCategory;
	private String m_strLeftTopLonLat;
	private String m_strRightBottomLonLat;
	private String m_strSearchText;
	private int m_iOffSet = UserControl_CommonVar.POST_QUERY_DEFAULT_OFFLEN;
	private int m_iMaxLen = UserControl_CommonVar.POST_QUERY_DEFAULT_MAXLEN_100;
	
	
	public String getM_strCategory() {
		return m_strCategory;
	}
	public void setM_strCategory(String m_strCategory) {
		this.m_strCategory = m_strCategory;
	}
	public String getM_strLeftTopLonLat() {
		return m_strLeftTopLonLat;
	}
	public void setM_strLeftTopLonLat(String m_strLeftTopLonLat) {
		this.m_strLeftTopLonLat = m_strLeftTopLonLat;
	}
	public String getM_strRightBottomLonLat() {
		return m_strRightBottomLonLat;
	}
	public void setM_strRightBottomLonLat(String m_strRightBottomLonLat) {
		this.m_strRightBottomLonLat = m_strRightBottomLonLat;
	}
	public String getM_strSearchText() {
		return m_strSearchText;
	}
	public void setM_strSearchText(String m_strSearchText) {
		this.m_strSearchText = m_strSearchText;
	}
	
	public int getM_iOffSet() {
		return m_iOffSet;
	}
	public void setM_iOffSet(int m_iOffSet) {
		this.m_iOffSet = m_iOffSet;
	}
	public int getM_iMaxLen() {
		return m_iMaxLen;
	}
	public void setM_iMaxLen(int m_iMaxLen) {
		this.m_iMaxLen = m_iMaxLen;
	}
	
	

}
