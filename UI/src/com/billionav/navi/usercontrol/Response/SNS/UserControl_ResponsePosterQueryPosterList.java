package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_PosterListInfo;
import com.billionav.navi.usercontrol.UserControl_SimplePosterInfo;

public class UserControl_ResponsePosterQueryPosterList extends UserControl_ResponseSNSBase{

	public UserControl_ResponsePosterQueryPosterList(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_PM_QUERY_POSTER_LIST);
	}
	
	protected void OnResponseSuccess(Element element) {
		UserControl_PosterListInfo m_cPosterListInfo = new UserControl_PosterListInfo();
		if (null == element) {
			return ;
		}
		String strCount = element.getAttribute("count");
		String strOffset = element.getAttribute("offset");
		
		if (null != strCount && !"".equals(strCount)) {
			m_cPosterListInfo.setM_iCount(Integer.parseInt(strCount));
		}
		if (null != strOffset && !"".equals(strOffset)) {
			m_cPosterListInfo.setM_iOffset(Integer.parseInt(strOffset));
		}
		
		NodeList messageList = (NodeList) element.getElementsByTagName("message");
		if (null == messageList) {
			return ;
		}
		
		ArrayList<UserControl_SimplePosterInfo> cListPoster = new ArrayList<UserControl_SimplePosterInfo>();
		for (int i = 0; i < messageList.getLength(); i++) {
			UserControl_SimplePosterInfo Poster = new UserControl_SimplePosterInfo();
			Element ele = (Element)messageList.item(i);
			
			Poster.setStrCategory(ele.getAttribute("category"));
			Poster.setStrLonLat(ele.getAttribute("lon-lat"));
			Poster.setStrPosterID(ele.getAttribute("postid"));
			Poster.setStrType(ele.getAttribute("type"));
			Poster.setStrPostTime(ele.getAttribute("post_time"));
			
			cListPoster.add(Poster);
		}
		m_cPosterListInfo.setM_cListPoster(cListPoster);
		
		UserControl_ManagerIF.Instance().setM_cPosterListInfo(m_cPosterListInfo);
		
	}
	

}
