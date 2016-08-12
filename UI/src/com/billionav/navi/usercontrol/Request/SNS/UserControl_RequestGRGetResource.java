package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_RequestGRGetResource extends UserControl_RequestSNSBase{
	
	private int 		m_iType;
	private String 		m_strID;
	private String		m_strEmail;
	private	String		m_strCellPhone;
	
	public UserControl_RequestGRGetResource(int iRequestId) {
		super(iRequestId);
	}
	/**
	 * @param m_iType	GET_RESOURCE_TYPE_POSTER_PHOTO : m_strID means photoid		//no use
	 * 					GET_RESOURCE_TYPE_POSTER_VOICE : m_strID means posterid
	 * 					GET_RESOURCE_TYPE_USER_PICTURE : m_strID means userid
	 * 
	 * @see				
	 * @return boolean 
	 */
	public boolean SendRequestGetResource() {
		if (0 == m_iType) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/getresource";
		sRequestUrl += "?type=" + m_iType;
		sRequestUrl += "&id=" + m_strID;
		
		if (UserControl_CommonVar.GET_RESOURCE_TYPE_USER_PICTURE == m_iType) {
			sRequestUrl += "&email=" + m_strEmail;
			sRequestUrl += "&cellphone=" + m_strCellPhone;
		}

		setM_strPassParam01(m_strID);
		setM_iPassParam01(m_iType);
		
		SendRequestByGet(sRequestUrl,true);
		return true;
	}
	

	public int getM_iType() {
		return m_iType;
	}

	public void setM_iType(int m_iType) {
		this.m_iType = m_iType;
	}

	public String getM_strID() {
		return m_strID;
	}

	public void setM_strID(String m_strID) {
		this.m_strID = m_strID;
	}

	public String getM_strEmail() {
		return m_strEmail;
	}

	public void setM_strEmail(String m_strEmail) {
		this.m_strEmail = m_strEmail;
	}

	public String getM_strCellPhone() {
		return m_strCellPhone;
	}

	public void setM_strCellPhone(String m_strCellPhone) {
		this.m_strCellPhone = m_strCellPhone;
	}
	
	

}
