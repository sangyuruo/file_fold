package com.billionav.navi.usercontrol;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UserControl_UploadData {
	
	private ArrayList<String> m_listPicturesPath = null;
	private String m_strVoicePath = "";
	private ArrayList<String> m_listText = null;
	private String m_strLon	= "";
	private String m_strLat = "";
	private int	m_iCategory;
	private int m_iType;
	
	public ArrayList<String> getM_listPicturesPath() {
		return m_listPicturesPath;
	}
	public void setM_listPicturesPath(ArrayList<String> m_listPicturesPath) {
		this.m_listPicturesPath = m_listPicturesPath;
	}
	public String getM_strVoicePath() {
		return m_strVoicePath;
	}
	public void setM_strVoicePath(String m_strVoicePath) {
		this.m_strVoicePath = m_strVoicePath;
	}
	public ArrayList<String> getM_listText() {
		return m_listText;
	}
	public void setM_listText(ArrayList<String> m_listText) {
		this.m_listText = m_listText;
	}
	public String getM_strLon() {
		return m_strLon;
	}
	public void setM_strLon(String m_strLon) {
		this.m_strLon = m_strLon;
	}
	public String getM_strLat() {
		return m_strLat;
	}
	public void setM_strLat(String m_strLat) {
		this.m_strLat = m_strLat;
	}
	
	public String getM_strText() {
		if (null != m_listText) {
			String strTextTemp = "";
			for (int cnt = 0; cnt < m_listText.size(); cnt++) {
				String str = m_listText.get(cnt);
				strTextTemp += str;
				if (cnt != m_listText.size() - 1) {
					strTextTemp += "CRLF";
				}
			}
			
			return strTextTemp;
		}
		return null;
	}
	public String getM_strLonLat() {
		if (!"".equals(m_strLon) && !"".equals(m_strLon)) {
			return m_strLon + "," + m_strLat;
		}
		return null;
	}
	
	public int getM_iCategory() {
		return m_iCategory;
	}
	public void setM_iCategory(int m_iCategory) {
		this.m_iCategory = m_iCategory;
	}
	public int getM_iType() {
		return m_iType;
	}
	public void setM_iType(int m_iType) {
		this.m_iType = m_iType;
	}
	
	public String toString() {
		StringBuffer strParam = new StringBuffer();
		
		if (null != m_listPicturesPath) {
			strParam.append("[Pictures]:");
			for (int i = 0; i < m_listPicturesPath.size(); i++) {
				strParam.append(m_listPicturesPath.get(i) + "	");
			}
		}
		String strText = getM_strText();
		if (null != strText) {
			strParam.append("[Text]:	");
			strParam.append(strText + "	");
		}
		
		strParam.append("[lon-lat]:	");
		strParam.append(m_strLon + "	");
		strParam.append(m_strLat + "	");
		
		strParam.append("[category]:	");
		strParam.append(m_iCategory + " ");
		strParam.append("[Type]:	");
		strParam.append(m_iType);
		return strParam.toString();
	}
	
}
