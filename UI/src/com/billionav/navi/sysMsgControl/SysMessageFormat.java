package com.billionav.navi.sysMsgControl;

public class SysMessageFormat {

	private int m_intMsgID;
	private String m_StrTitle;	
	private String m_StrContent;
	private String m_StrStartDate;
	private String m_StrDueDate;
	public int getM_intMsgID() {
		return m_intMsgID;
	}
	public void setM_intMsgID(int m_intMsgID) {
		this.m_intMsgID = m_intMsgID;
	}
	public String getM_StrTitle() {
		return m_StrTitle;
	}
	public void setM_StrTitle(String m_StrTitle) {
		this.m_StrTitle = m_StrTitle;
	}
	public String getM_StrContent() {
		return m_StrContent;
	}
	public void setM_StrContent(String m_StrContent) {
		this.m_StrContent = m_StrContent;
	}
	public String getM_StrStartDate() {
		return m_StrStartDate;
	}
	public void setM_Strm_StrStartDate(String m_StrStartDate) {
		this.m_StrStartDate = m_StrStartDate;
	}
	public String getM_StrDueDate() {
		return m_StrDueDate;
	}
	public void setM_StrDueDate(String m_StrDueDate) {
		this.m_StrDueDate = m_StrDueDate;
	}
	@Override
	public String toString() {
		return m_StrTitle+"\n"+m_StrStartDate+"\n"+m_StrDueDate+"\n"+m_StrContent+"\n\n";
	}
}
