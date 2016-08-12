package com.billionav.navi.usercontrol.Response.SNS;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_FavoriteColData;
import com.billionav.navi.usercontrol.UserControl_FavoriteItem;
import com.billionav.navi.usercontrol.UserControl_FavoritePosterItem;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;

public class UserControl_ResponsePosterQueryColList extends UserControl_ResponseSNSBase{

	public UserControl_ResponsePosterQueryColList(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_PM_QUERY_COLLECTION_LIST);
	}
	/**
	 * 
	 * 
	 */
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return;
		}
		
		UserControl_FavoriteColData data = new UserControl_FavoriteColData();
		
		String strCount = element.getAttribute("count");
		String strOffset = element.getAttribute("offset");
		if (!"".equals(strCount)) {
			data.setM_iCount(Integer.parseInt(strCount));
		}
		
		if (!"".equals(strOffset)) {
			data.setM_iOffset(Integer.parseInt(strOffset));
		}
		
		NodeList favolateItems = element.getElementsByTagName("favorite");
		if (null != favolateItems) {
			ArrayList<UserControl_FavoriteItem>  cfavoriteList = new ArrayList<UserControl_FavoriteItem>();
			
			for (int i = 0; i < favolateItems.getLength(); i++) {
				UserControl_FavoriteItem item = new UserControl_FavoriteItem();
				Element favolateItem = (Element)favolateItems.item(i);
				if (null != favolateItem) {
					String favolateID = favolateItem.getAttribute("id");
					String favolatetype = favolateItem.getAttribute("type");
					item.setStrFavoriteID(favolateID);
					
					if (!"".equals(favolatetype)) {
						item.setStrFavoriteType(Integer.parseInt(favolatetype));
					}
					
					NodeList messageItems = favolateItem.getElementsByTagName("message");
					if (null != messageItems && messageItems.getLength() > 0) {
						UserControl_FavoritePosterItem posterItem = new UserControl_FavoritePosterItem();
						Element messageItem = (Element)messageItems.item(0);
						String strPosterID = messageItem.getAttribute("postid");
						String strText = messageItem.getAttribute("text");
						String strLonlat = messageItem.getAttribute("lon_lat");
						String strLocInfo = messageItem.getAttribute("loc_info");
						String strPostTime = messageItem.getAttribute("post_time");
						
						posterItem.setStrPosterID(strPosterID);
						posterItem.setStrText(strText);
						posterItem.setStrLonlat(strLonlat);
						posterItem.setStrLocinfo(strLocInfo);
						posterItem.setStrPosttime(strPostTime);
						
						NodeList photos = messageItem.getElementsByTagName("photo");
						
						if (null != photos) {
							ArrayList<String> cListPhotosPath = new ArrayList<String>();
							
							for (int x = 0; x < photos.getLength(); x++) {
								Element photo = (Element)photos.item(x);
								String strURL = photo.getAttribute("url");
								cListPhotosPath.add(strURL);
							}
							
							posterItem.setcListPhotosPath(cListPhotosPath);
						}
						item.setItem(posterItem);
					}
				}
				cfavoriteList.add(item);
			}
			
			data.setM_favoriteList(cfavoriteList);
		}
		
		UserControl_ManagerIF.Instance().setM_cFavoriteCollectionData(data);
	}
	
}
