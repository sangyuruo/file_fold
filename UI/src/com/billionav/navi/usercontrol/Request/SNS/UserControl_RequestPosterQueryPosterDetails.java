package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;

import com.billionav.navi.usercontrol.UserControl_PosterDetail;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterQueryPosterDetails;


public class UserControl_RequestPosterQueryPosterDetails  extends UserControl_RequestSNSBase{

	String strPosterID;
//	private static ArrayList<UserControl_ResponsePosterQueryPosterDetails> m_cListPostDetailsInfo = null;
	
	public UserControl_RequestPosterQueryPosterDetails(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestQueryPostDetails() {
		if ("".equals(strPosterID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/post/query?postid="+strPosterID;
		setM_strPassParam01(strPosterID);
		
//		ArrayList<UserControl_ResponsePosterQueryPosterDetails> clist = getM_cListPostDetailsInfo();
//		clist.add((UserControl_ResponsePosterQueryPosterDetails) GetResponseBase());
		
		SendRequestByGet(sRequestUrl, true);
		return true;
	}
	
//	public static UserControl_PosterDetail getPosterDetail(String strPosterID) {
//		for (UserControl_ResponsePosterQueryPosterDetails detail : getM_cListPostDetailsInfo()) {
//			if (detail.getM_strParam01() == strPosterID) {
//				return detail.getM_cPosterDetails();
//			}
//		}
//		return null;
//	}
//	
//	public static  boolean deletePostData(String strPosterID) {
//		UserControl_ResponsePosterQueryPosterDetails cTempRequest = null;
//		for (UserControl_ResponsePosterQueryPosterDetails detail : getM_cListPostDetailsInfo()) {
//			if (detail.getM_strParam01() == strPosterID) {
//				cTempRequest = detail;
//			}
//		}
//		
//		if (null != cTempRequest) {
//			getM_cListPostDetailsInfo().remove(cTempRequest);
//		}
//		return true;
//	}

	public String getStrPosterID() {
		return strPosterID;
	}

	public void setStrPosterID(String strPosterID) {
		this.strPosterID = strPosterID;
	}

//	public static ArrayList<UserControl_ResponsePosterQueryPosterDetails> getM_cListPostDetailsInfo() {
//		if (null == m_cListPostDetailsInfo) {
//			m_cListPostDetailsInfo = new ArrayList<UserControl_ResponsePosterQueryPosterDetails>();
//		}
//		return m_cListPostDetailsInfo;
//	}
	
	
}
