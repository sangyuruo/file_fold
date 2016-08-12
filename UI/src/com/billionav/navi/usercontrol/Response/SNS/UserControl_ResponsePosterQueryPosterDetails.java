package com.billionav.navi.usercontrol.Response.SNS;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_PosterDetail;

public class UserControl_ResponsePosterQueryPosterDetails extends UserControl_ResponseSNSBase{

	private UserControl_PosterDetail m_cPosterDetails;
	
	public UserControl_ResponsePosterQueryPosterDetails(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_PM_QUERY_POSTER_DETAILS);
	}
	
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return;
		}
		
		NodeList items = element.getElementsByTagName("message");
		
		if (null == items) {
			return;
		}
		
		if (items.getLength() <= 0) {
			return;
		}
		
		Element ele = (Element)items.item(0);
		
		if (null == ele) {
			return ;
		}
		m_cPosterDetails = new UserControl_PosterDetail();
		
		String strPostID	= ele.getAttribute("postid");
		String strCategory 	= ele.getAttribute("category");
		String strLonLat	= ele.getAttribute("lon-lat");
		String strLocInfo	= ele.getAttribute("loc-info");
		String strText		= ele.getAttribute("text");
		String strCreateUserid = ele.getAttribute("create-userid");
		String strCreateNeckName = ele.getAttribute("create-nickname");
		String strStars		= ele.getAttribute("stars");
		String strPostTime	= ele.getAttribute("post_time");
		String strType		= ele.getAttribute("type");
		
		m_cPosterDetails.setStrPosterID(strPostID);
		m_cPosterDetails.setStrCategory(strCategory);
		m_cPosterDetails.setStrLonLat(strLonLat);
		m_cPosterDetails.setStrLocInfo(strLocInfo);
		m_cPosterDetails.setStrText(strText);
		m_cPosterDetails.setStrCreateUserid(strCreateUserid);
		m_cPosterDetails.setStrCreateNeckName(strCreateNeckName);
		m_cPosterDetails.setStrStars(strStars);
		m_cPosterDetails.setStrPostTime(strPostTime);
		m_cPosterDetails.setStrType(strType);
		
		NodeList listTags = ele.getElementsByTagName("tags");
		NodeList listRes  = ele.getElementsByTagName("resources");
		
		if (null != listRes && listRes.getLength() > 0) {
			Element eleRes = (Element)listRes.item(0);
			if (null != eleRes) {
				NodeList listResources = eleRes.getElementsByTagName("resource");
				if (null != listResources && listResources.getLength() > 0) {
					Element resource = (Element) listResources.item(0);
					if (null != resource) {
						String strURL = resource.getAttribute("url");
						m_cPosterDetails.setStrURL(strURL);
					}
				}
			}
		}
		
		
		if (null != listTags && listTags.getLength() > 0) {
			Element eleTag = (Element)listTags.item(0);
			NodeList listTag = eleTag.getElementsByTagName("tag");
			if (null != listTag && listTag.getLength() > 0) {
				Element eleTemp =  (Element)listTag.item(0);
				if (null != eleTemp) {
					String strValue = eleTemp.getAttribute("value");
					m_cPosterDetails.setStrValue(strValue);
				}
			}
		}
		
		UserControl_ManagerIF.Instance().setM_cPosterDetails(m_cPosterDetails);
	}

	public UserControl_PosterDetail getM_cPosterDetails() {
		return m_cPosterDetails;
	}

	public void setM_cPosterDetails(UserControl_PosterDetail m_cPosterDetails) {
		this.m_cPosterDetails = m_cPosterDetails;
	}
	
	

}
