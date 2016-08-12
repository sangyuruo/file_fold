package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import com.billionav.navi.net.PMultiPart;
import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_UploadData;
 

public class UserControl_RequestPosterUploadData extends UserControl_RequestSNSBase{
	
	private UserControl_UploadData	m_cData;
	
	public UserControl_RequestPosterUploadData(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean sendRequestUploadData() {
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/post/";
		
		List<PMultiPart> listData = new ArrayList<PMultiPart>();
		
		if (null != m_cData.getM_listPicturesPath()) {
			for ( int cnt = 0; cnt < m_cData.getM_listPicturesPath().size(); cnt++) {
				String strPhotoPath = m_cData.getM_listPicturesPath().get(cnt);
				PMultiPart cItem = new PMultiPart(PMultiPart.MULTI_TYPE_FILE, "photo", strPhotoPath, null);
				listData.add(cItem);
			}
		}
		
		if (!"".equals(m_cData.getM_strVoicePath())) {
			PMultiPart cItem = new PMultiPart(PMultiPart.MULTI_TYPE_FILE, "audio", m_cData.getM_strVoicePath(), null);
			listData.add(cItem);
		}
		
		if (null != m_cData.getM_strText()) {
			PMultiPart cItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "text", m_cData.getM_strText(), null);
			listData.add(cItem);
		}
		
		if (null != m_cData.getM_strLonLat()) {
			PMultiPart cItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "lon-lat", m_cData.getM_strLonLat(), null);
			listData.add(cItem);
		}
		
		{
			PMultiPart cCategoryItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "category", m_cData.getM_iCategory() + "", null);
			listData.add(cCategoryItem);
		}
		
		{
			PMultiPart cTypeItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "type", m_cData.getM_iType() + "", null);
			listData.add(cTypeItem);
		}
		
		PostData post = new PostData();
		post.setPostMultiData(listData);
		SendRequestByPost(sRequestUrl,post,true);
		
		if (null != m_cData) {
			UserControl_CommonVar.SNSLog("SNSUploadData", UserControl_CommonVar.SNS_LOG_STEP_SEND, sRequestUrl);
			UserControl_CommonVar.SNSLog("SNSUploadData", UserControl_CommonVar.SNS_LOG_STEP_SEND, m_cData.toString());
		}
		return true;
	}

	public UserControl_UploadData getM_cData() {
		return m_cData;
	}

	public void setM_cData(UserControl_UploadData m_cData) {
		this.m_cData = m_cData;
	}
	
	
	
	
}
