package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_CycleInfo;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class UserControl_ResponseGroupQueryGroups extends UserControl_ResponseSNSBase{

	public UserControl_ResponseGroupQueryGroups(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_QUERY_GROUPS);
	}
	
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return ;
		}
		
		String strOffset = element.getAttribute("offset");
		String strID = "";
		String strName = "";
		String strOwner = "";
		String strType = "";
		String strDiscription = "";
		String strTags = "";
		
		UserControl_ManagerIF.Instance().setM_iCycleOffset(Integer.parseInt(strOffset));
		
		ArrayList< UserControl_CycleInfo> list = new ArrayList<UserControl_CycleInfo>();
		NodeList items = element.getElementsByTagName("item");
		
		if (null == items) {
			return ;
		}
		
		for (int index = 0; index < items.getLength(); index++) {
			UserControl_CycleInfo cInfo = new UserControl_CycleInfo();
			
			Element ele = (Element)items.item(index);
			strID = ele.getAttribute("id");
			strName = ele.getAttribute("name");
			strOwner = ele.getAttribute("owner");
			strType = ele.getAttribute("type");
			strDiscription = ele.getAttribute("description");
			
			cInfo.setM_strID(strID);
			cInfo.setM_strName(strName);
			cInfo.setM_strOwner(strOwner);
			cInfo.setM_strType(strType);
			cInfo.setM_strDiscription(strDiscription);
			
			NodeList tagItems = ele.getElementsByTagName("tags");
			if (null != tagItems && tagItems.getLength() > 0) {
				Element tagEle = (Element)tagItems.item(0);
				strTags = tagEle.getAttribute("value");
				cInfo.setM_strTags(strTags);
			}
			list.add(cInfo);
			
		}
		
		UserControl_ManagerIF.Instance().setM_cListCycleInfo(list);
	}
	
	
}
