package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_InviteDataFormat;
import com.billionav.navi.usercontrol.UserControl_InviteDataGroupFormat;
import com.billionav.navi.usercontrol.UserControl_InviteDataUserFormat;


public class UserControl_RequestMemberInvitePerson extends UserControl_RequestSNSBase{
	UserControl_InviteDataFormat m_cListInviteDataFormat = null;

	public UserControl_RequestMemberInvitePerson(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestInvitePerson(UserControl_InviteDataFormat cListInviteDataFormat){
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/inviteuser";
		
		if (null == m_cListInviteDataFormat) {
			return false;
		}
		
		//Build XML 
		String strSendString = BuildXMLFormat(cListInviteDataFormat);
		if (null == strSendString) {
			return false;
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("content",strSendString));
		PostData post = new PostData();
		post.setPostData(params);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}
	
	private String BuildXMLFormat(UserControl_InviteDataFormat cListInviteDataFormat) {
		ArrayList<UserControl_InviteDataGroupFormat> cListGroups = m_cListInviteDataFormat.getM_cInviteData();
		if (null == cListGroups) {
			return null;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		try {
			if (null == dbf) {
				return null;
			}
			db = dbf.newDocumentBuilder();
			if (null == db) {
				return null;
			}
			doc = db.newDocument();
			
			for (int i = 0; i < cListGroups.size(); i++) {
				UserControl_InviteDataGroupFormat cGroupItem = cListGroups.get(i);
				Element group = doc.createElement("invite");
				group.setAttribute("circle-id", cGroupItem.getStrCycleID());
				
				ArrayList<UserControl_InviteDataUserFormat> cUserItems = cGroupItem.getM_cListInvitaUsers();
				if (null != cUserItems) {
					for (int j = 0; j < cUserItems.size(); j ++) {
						UserControl_InviteDataUserFormat  cUserItem = cUserItems.get(j);
						
						Element userItem = doc.createElement("user");
						
						if (!"".equals(cUserItem.getM_strUserID())) {
							userItem.setAttribute("id", cUserItem.getM_strUserID());
						} else if (!"".equals(cUserItem.getM_strUserMail())) {
							userItem.setAttribute("email", cUserItem.getM_strUserMail());
						} else if (!"".equals(cUserItem.getM_strUserCellPhone())) {
							userItem.setAttribute("cellphone", cUserItem.getM_strUserCellPhone());
						}
						
						group.appendChild(userItem);
						
					}
				}
				doc.appendChild(group);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserControl_InviteDataFormat getM_cListInviteDataFormat() {
		return m_cListInviteDataFormat;
	}

	public void setM_cListInviteDataFormat(
			UserControl_InviteDataFormat m_cListInviteDataFormat) {
		this.m_cListInviteDataFormat = m_cListInviteDataFormat;
	}
	
	
}
