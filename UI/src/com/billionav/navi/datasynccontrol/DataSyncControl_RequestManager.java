package com.billionav.navi.datasynccontrol;

import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestAmeba;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestBase;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestDelPOIImage;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestDownLoadPOIImage;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestPOI;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestRoute;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestSinglePOI;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestUploadPOIImage;

public class DataSyncControl_RequestManager{
	private static DataSyncControl_RequestManager m_cDataSyncControlRequestMng = null;
	
	private DataSyncControl_RequestManager(){
		
	}
	
	public static DataSyncControl_RequestManager Instance(){
		if (null == m_cDataSyncControlRequestMng) {
			m_cDataSyncControlRequestMng = new DataSyncControl_RequestManager();
			return m_cDataSyncControlRequestMng;
		}
		return m_cDataSyncControlRequestMng;
	}
	
	public DataSyncControl_RequestBase CreateRequest(int iRequesType){
		DataSyncControl_RequestBase request = null;
		switch (iRequesType) {
		case DataSyncControl_RequestType.DS_REQ_POI_ID:
			request = new DataSyncControl_RequestPOI(iRequesType);
			break;
		case DataSyncControl_RequestType.DS_REQ_PATH_ID:
			request = new DataSyncControl_RequestRoute(iRequesType);
			break;
		case DataSyncControl_RequestType.DS_REQ_AMEBA:
			request = new DataSyncControl_RequestAmeba(iRequesType);
			break;
		case DataSyncControl_RequestType.DS_REQ_SINGLE_POI_ID:
			request = new DataSyncControl_RequestSinglePOI(iRequesType);
			break;
		case DataSyncControl_RequestType.DS_REQ_DOWNLOAD_IMAGE:
			request = new DataSyncControl_RequestDownLoadPOIImage(iRequesType);
			break;
		case DataSyncControl_RequestType.DS_REQ_UPLOAD_IMAGE:
			request = new DataSyncControl_RequestUploadPOIImage(iRequesType);
			break;
		case DataSyncControl_RequestType.DS_REQ_DEL_IMAGE:
			request = new DataSyncControl_RequestDelPOIImage(iRequesType);
			break;
		}
		return request;
	}
}