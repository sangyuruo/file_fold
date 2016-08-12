package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;

import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterGetPhoto;

public class UserControl_RequestPosterGetPhoto extends UserControl_RequestSNSBase{
	
	private String strPhotoID;
	//Image response List
	private static ArrayList<UserControl_ResponsePosterGetPhoto> m_cListImageListInfo = null;
	
	public UserControl_RequestPosterGetPhoto(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestGetPhoto() {
		if ("".equals(strPhotoID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/photo?photoid=" + strPhotoID;
		SendRequestByGet(sRequestUrl,true);
		
		ArrayList<UserControl_ResponsePosterGetPhoto> clist = getM_cListImageListInfo();
		clist.add((UserControl_ResponsePosterGetPhoto) GetResponseBase());
		
		setM_strPassParam01(strPhotoID);
		
		return true;
	}
	
	public static byte[] GetPhotoBytes(String strPhotoID) {
		for (UserControl_ResponsePosterGetPhoto photo : getM_cListImageListInfo()) {
			if (photo.getM_strParam01().equals(strPhotoID)) {
				return photo.getbImage();
			}
		}
		return null;
	}
	
	public static boolean DeletePhotoData(String strPhotoID) {
		UserControl_ResponsePosterGetPhoto cTempRequest = null;
		for (UserControl_ResponsePosterGetPhoto photo : getM_cListImageListInfo()) {
			if (photo.getM_strParam01().equals(strPhotoID)) {
				cTempRequest = photo;
			}
		}
		if (null != cTempRequest) {
			getM_cListImageListInfo().remove(cTempRequest);
		}
		return true;
	}

	public String getStrPhotoID() {
		return strPhotoID;
	}

	public void setStrPhotoID(String strPhotoID) {
		this.strPhotoID = strPhotoID;
	}
	
	public static ArrayList<UserControl_ResponsePosterGetPhoto> getM_cListImageListInfo() {
		if (null == m_cListImageListInfo) {
			m_cListImageListInfo = new ArrayList<UserControl_ResponsePosterGetPhoto>();
		}
		return m_cListImageListInfo;
	}

	
}
