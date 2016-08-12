package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_RankListInfo;
import com.billionav.navi.usercontrol.UserControl_RankListItem;

public class UserControl_ResponseMyDataQueryRankList extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMyDataQueryRankList(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_MYDATA_GET_RANKLIST);
	}
	
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return ;
		}
		UserControl_RankListInfo info = new UserControl_RankListInfo();
		
		NodeList ranks = element.getElementsByTagName("rankings");
		
		if (null != ranks && ranks.getLength() > 0) {
			Element rank = (Element)ranks.item(0);
			if (null != rank) {
				
				String strPageSize = rank.getAttribute("page_size");
				String strOffset = rank.getAttribute("page_offset");
				String strType = rank.getAttribute("type");
				String strStart = rank.getAttribute("start");
				String strEnd = rank.getAttribute("end");

				
				info.setM_strPageSize(strPageSize);
				info.setM_strOffset(strOffset);
				info.setM_strType(strType);
				info.setM_strStart(strStart);
				info.setM_strEnd(strEnd);
				
				NodeList items = rank.getElementsByTagName("item");
				ArrayList<UserControl_RankListItem> list = new ArrayList<UserControl_RankListItem>();
				if (null != items) {
					for (int i = 0; i < items.getLength(); i++) {
						UserControl_RankListItem rankItem = new UserControl_RankListItem();
						Element item  = (Element)items.item(i);
						if (null != item) {
							String strUserID = item.getAttribute("user_id");
							String strUserNickName = item.getAttribute("user_nickname");
							String strUserStart = item.getAttribute("star");
							
							rankItem.setM_strUserID(strUserID);
							rankItem.setM_strUserNickName(strUserNickName);
							rankItem.setM_fStars(Float.parseFloat(strUserStart));
						}
						list.add(rankItem);
					}
				}
				info.setItems(list);
			}
		}
		UserControl_ManagerIF.Instance().setM_cRankListInfo(info);
	}

}
