package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_BlackList;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class UserControl_ResponseMemberQueryBlackList extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMemberQueryBlackList(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_QUERY_BLACKLIST);
	}
	
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return ;
		}
		String strCount = element.getAttribute("count");
		String strOffset = element.getAttribute("offset");
		String strUserID = "";
		
		NodeList items = element.getElementsByTagName("black");
		ArrayList<String> cListUserID = new ArrayList<String>();
		
		if (null == items) {
			return ;
		}
		
		for (int index = 0; index < items.getLength(); index++) {
			Element ele = (Element)items.item(index);
			strUserID = ele.getAttribute("user_id");
			cListUserID.add(strUserID);
		}
		
		UserControl_BlackList cBlackList = new UserControl_BlackList();
		cBlackList.setM_iCount(Integer.parseInt(strCount));
		cBlackList.setM_iOffset(Integer.parseInt(strOffset));
		cBlackList.setM_cListUserID(cListUserID);
		
		UserControl_ManagerIF.Instance().setM_cBlackList(cBlackList);
		
	}
	

}
