package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_PositionConfig;
import com.billionav.navi.usercontrol.UserControl_UserFeedBack;

public class UserControl_ResponsePosterUserFeedback extends UserControl_ResponseSNSBase{

	public UserControl_ResponsePosterUserFeedback(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_PM_USER_FEEDBACK);
	}
	
	protected void OnResponseSuccess(Element element) {
		
		if (null == element) {
			return ;
		}
		String strText = "";
		UserControl_UserFeedBack cUserFeedBack = new UserControl_UserFeedBack();
		
		NodeList TTSS =  element.getElementsByTagName("tts");
		if (null != TTSS && TTSS.getLength() > 0) {
			Element tts = (Element)TTSS.item(0);
			strText = tts.getAttribute("text");
			cUserFeedBack.setStrTTS(strText);
		}
		NodeList configs =  element.getElementsByTagName("config");
		if (null != configs && configs.getLength() > 0) {
			Element  config  = (Element)configs.item(0);
			if (null != config) {
				NodeList items = config.getElementsByTagName("item");
			
				ArrayList<UserControl_PositionConfig> cList = new ArrayList<UserControl_PositionConfig>();
				if (null != items){
					for (int i = 0; i < items.getLength(); i++) {
						String strName = "";
						String strValue = "";
						
						UserControl_PositionConfig cof = new UserControl_PositionConfig();
						Element item = (Element)items.item(i);
						if (null != item) {
							strName = item.getAttribute("name");
							strValue = item.getAttribute("value");
						}
						cof.setStrName(strName);
						cof.setStrValue(strValue);
						cList.add(cof);
					}
				}
				cUserFeedBack.setcConfigList(cList);
			}
		}
		
		UserControl_ManagerIF.Instance().setM_cUserFeedBack(cUserFeedBack);
	}
	
	protected void OnReponseFailed(Element root){
		String strText = "";
		UserControl_UserFeedBack cUserFeedBack = new UserControl_UserFeedBack();
		
		NodeList TTSS =  root.getElementsByTagName("tts");
		if (null != TTSS && TTSS.getLength() > 0) {
			Element tts = (Element)TTSS.item(0);
			strText = tts.getAttribute("text");
			cUserFeedBack.setStrTTS(strText);
		}
		
		SetErrorCode(root);
		UserControl_ManagerIF.Instance().setM_cUserFeedBack(cUserFeedBack);
	}
}
