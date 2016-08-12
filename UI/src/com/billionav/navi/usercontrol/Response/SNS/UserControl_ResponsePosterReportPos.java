package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_PositionConfig;
import com.billionav.navi.usercontrol.UserControl_PositionMessage;
import com.billionav.navi.usercontrol.UserControl_PositionResData;

public class UserControl_ResponsePosterReportPos extends UserControl_ResponseSNSBase{

	public UserControl_ResponsePosterReportPos(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_PM_REPORTPOS);
	}
	
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return ;
		}
		NodeList messages = element.getElementsByTagName("messages");
		if (null == messages) {
			return ;
		}
		
		UserControl_PositionResData cResData = new UserControl_PositionResData();
		

		String strPostID = "";
		String strText = "";
		String strLonLat = "";
		String strLocInfo = "";
		String strPostTime = "";
		String photoURL = "";
		
		ArrayList<UserControl_PositionMessage> cListMessage = new ArrayList<UserControl_PositionMessage>();
		for (int i = 0; i < messages.getLength(); i++) {
			UserControl_PositionMessage msg = new UserControl_PositionMessage();
			Element message = (Element)messages.item(i);
			strPostID = message.getAttribute("postid");
			strText = message.getAttribute("text");
			strLonLat = message.getAttribute("lon_lat");
			strLocInfo = message.getAttribute("loc_info");
			strPostTime = message.getAttribute("post_time");
			
			msg.setStrPostID(strPostID);
			msg.setStrText(strText);
			msg.setStrLonLat(strLonLat);
			msg.setStrLocInfo(strLocInfo);
			msg.setStrPostTime(strPostTime);
			
			NodeList photoList = message.getElementsByTagName("photo");
			if(null != photoList) {
				Element photo = (Element) photoList.item(0);
				photoURL = photo.getAttribute("url");
				msg.setStrURL(photoURL);
			}
			cListMessage.add(msg);
		}
		cResData.setM_cListMessage(cListMessage);
		
		NodeList configs = element.getElementsByTagName("config");
		if (null == configs) {
			return ;
		}
		
		ArrayList<UserControl_PositionConfig> cListConfig = new ArrayList<UserControl_PositionConfig>();
		if (configs.getLength() > 0) {
			Element config = (Element)configs.item(0);
			NodeList Items = config.getElementsByTagName("item");
			if (null != Items) {
				for (int i = 0; i < Items.getLength(); i++) {
					UserControl_PositionConfig conf = new UserControl_PositionConfig();
					String strName = ((Element)Items.item(i)).getAttribute("name");
					String strValue = ((Element)Items.item(i)).getAttribute("name");
					conf.setStrName(strName);
					conf.setStrValue(strValue);
					cListConfig.add(conf);
				}
			}
		}
		
		cResData.setM_cListMessage(cListMessage);
		cResData.setM_cListConfig(cListConfig);
		
		UserControl_ManagerIF.Instance().setM_cPositionResData(cResData);
	}
	
}
