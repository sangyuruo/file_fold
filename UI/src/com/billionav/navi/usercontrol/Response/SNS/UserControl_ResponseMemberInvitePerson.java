package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_InviteFailedUserInfo;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class UserControl_ResponseMemberInvitePerson extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMemberInvitePerson(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_INVITEPERSON);
	}
	
	protected void OnReponseFailed(Element root){
		ArrayList<UserControl_InviteFailedUserInfo> list = new ArrayList<UserControl_InviteFailedUserInfo>();
		NodeList listFailedUserPart = root.getElementsByTagName("failed-users");
		if (null != listFailedUserPart) {
			Element element = (Element) listFailedUserPart.item(0);
			if (null != element) {
				NodeList listFailedUsers = element.getElementsByTagName("user");
				if (null != listFailedUsers) {
					for (int i = 0; i <  listFailedUsers.getLength(); i++) {
						Element user = (Element) listFailedUsers.item(i);
						if (null != user) {
							String id = user.getAttribute("id");
							String email = user.getAttribute("email");
							String cellphone = user.getAttribute("cellphone");
							UserControl_InviteFailedUserInfo info = null;
							if (!"".equals(id)) {
								info = new UserControl_InviteFailedUserInfo();
								info.setM_iInfoType(UserControl_CommonVar.PERSON_INFO_TYPE_USER_ID);
								info.setM_strUserID(id);
							} else if (!"".equals(email)) {
								info = new UserControl_InviteFailedUserInfo();
								info.setM_iInfoType(UserControl_CommonVar.PERSON_INFO_TYPE_USER_EMAIL);
								info.setM_strUserEmail(email);
							} else if (!"".equals(cellphone)) {
								info = new UserControl_InviteFailedUserInfo();
								info.setM_iInfoType(UserControl_CommonVar.PERSON_INFO_TYPE_USER_CELLPHONE);
								info.setM_strUserCellPhone(cellphone);
							} else {
								
							}
							if (null != info) {
								list.add(info);
							}
						}
					}
					UserControl_ManagerIF.Instance().setM_cInviteFailedUserList(list);
				}
			}
		}
		
		SetErrorCode(root);
	}

}
