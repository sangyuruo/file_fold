package com.billionav.navi.datasynccontrol.Response;

import java.util.HashMap;

import android.util.Log;

import com.billionav.navi.datasynccontrol.DataSyncControl_RequestType;

public class DataSyncControl_ResponseFactory{
	private static DataSyncControl_ResponseFactory m_sDataSyncFactory = new DataSyncControl_ResponseFactory();
	private static long m_sRequestID = 0;
	private static HashMap m_lUserRequestMap = new HashMap<String,DataSyncControl_ResponseBase>();
	
	public static DataSyncControl_ResponseFactory Instance() {
		return m_sDataSyncFactory;
	}
	
	public DataSyncControl_ResponseBase CreateResponse(int iRequestID) {
		DataSyncControl_ResponseBase response = null;
		switch (iRequestID) {
		case DataSyncControl_RequestType.DS_REQ_POI_ID:
			response =  new DataSyncControl_ResponsePOI(iRequestID);
			break;
		case DataSyncControl_RequestType.DS_REQ_PATH_ID:
			response = new DataSyncControl_ResponseRoute(iRequestID);
			break;
		case DataSyncControl_RequestType.DS_REQ_AMEBA:
			response = new DataSyncControl_ResponseAmeba(iRequestID);
			break;
		case DataSyncControl_RequestType.DS_REQ_SINGLE_POI_ID:
			response = new DataSyncControl_ResponseSinglePOI(iRequestID);
			break;
		case DataSyncControl_RequestType.DS_REQ_DOWNLOAD_IMAGE:
			response = new DataSyncControl_ResponseDownloadPOIImage(iRequestID);
			break;
		case DataSyncControl_RequestType.DS_REQ_UPLOAD_IMAGE:
			response = new DataSyncControl_ResponseUpdatePOIImage(iRequestID);
			break;
		case DataSyncControl_RequestType.DS_REQ_DEL_IMAGE:
			response = new DataSyncControl_ResponseDelPOIImage(iRequestID);
			break;
		}
		response.setM_lUserRequestID(GetRequestID());
		m_lUserRequestMap.put(response.getM_lUserRequestID() + "", response);
		Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:AddUserRequest RequestType: "+response.GetRequestId() +" UserRequestID: "+ response.getM_lUserRequestID() + " To  Map");
		return response;
	}

	private synchronized long GetRequestID() {
		if(m_sRequestID >= Long.MAX_VALUE) {
			m_sRequestID = 0;
		}
		return ++m_sRequestID;
	}
	
	private boolean CancleUserRequestBase(long iRequestID) {
		String key = iRequestID + "";
		if(m_lUserRequestMap.containsKey(key)) {
			DataSyncControl_ResponseBase request = (DataSyncControl_ResponseBase)m_lUserRequestMap.get(key);
			return request.cancelRequest();
		}
		return false;
	}
	
	public void RemoveUserRequest(long iRequestID) {
		String key = iRequestID + "";
		if(m_lUserRequestMap.containsKey(key)) {
			m_lUserRequestMap.remove(key);
			Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:RemoveUserRequest " + key);
		}
	}
	
	public boolean CancleUserRequest(long iRequestID){
		if(CancleUserRequestBase(iRequestID)) {
			Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:CancleUserRequest success" + iRequestID);
			RemoveUserRequest(iRequestID);
			return true;
		} else {
			Log.d("[DataSyncControl]", "[DataSyncControl]--UserRequestID:CancleUserRequest Failed" + iRequestID);
			return false;
		}
		
	}
	
	public static long getM_sRequestID() {
		return m_sRequestID;
	}

	public static void setM_sRequestID(long m_sRequestID) {
		DataSyncControl_ResponseFactory.m_sRequestID = m_sRequestID;
	}
	
}