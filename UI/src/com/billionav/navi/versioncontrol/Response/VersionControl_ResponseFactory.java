package com.billionav.navi.versioncontrol.Response;

import java.util.HashMap;

import android.util.Log;

import com.billionav.navi.versioncontrol.VersionControl_RequestType;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetSrchTopDataVersion;


public class VersionControl_ResponseFactory {
	private static VersionControl_ResponseFactory m_sVersionControlResFactory = new VersionControl_ResponseFactory();
	private static long m_sRequestID = 0;
	private static HashMap m_lVersionRequestMap = new HashMap<String,VersionControl_ResponseBase>();
	
	public static VersionControl_ResponseFactory Instance() {
		return m_sVersionControlResFactory;
	}
	
	public VersionControl_ResponseBase CreateResponse(int iRequestID) {
		VersionControl_ResponseBase response = null;
		switch (iRequestID) {
			case VersionControl_RequestType.UC_REQ_VC_GET_VERSION_LIST:
				response = new VersionControl_ResponseGetVersionList(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_METADATA_INFO:
				response = new VersionControl_ResponseGetMetaDataInfo(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_TOP_DATA_INFO:
				response = new VersionControl_ResponseGetTopDataVersion(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_ALL_APK_UPDATE_INFOS:
				response = new VersionControl_ResponseGetAllAPKUpdateInfos(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_UPDATE_LOG_INFO:
				response = new VersionControl_ResponseGetUpdateLogInfo(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_SEARCH_TOP_DATA_INFO:
				response = new VersionControl_ResponseGetSrchTopDataVersion(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_CAMERA_INFOS:
				response = new VersionControl_ResponseGetCameraPara(iRequestID);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_CAMERA_VER:
				response = new VersionControl_ResponseGetCameraVer(iRequestID);
		}
		response.setM_lVersionControlRequestID(GetRequestID());
		m_lVersionRequestMap.put(response.getM_lVersionControlRequestID() + "", response);
		Log.d("[VerSionControl]", "[VerSionControl]--UserRequestID:AddUserRequest RequestType: "+response.getM_iRequestId() +" UserRequestID: "+ response.getM_lVersionControlRequestID() + " To  Map");
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
		if(m_lVersionRequestMap.containsKey(key)) {
			VersionControl_ResponseBase request = (VersionControl_ResponseBase)m_lVersionRequestMap.get(key);
			return request.cancelRequest();
		}
		return false;
	}
	
	public void RemoveUserRequest(long iRequestID) {
		String key = iRequestID + "";
		if(m_lVersionRequestMap.containsKey(key)) {
			m_lVersionRequestMap.remove(key);
			Log.d("[VerSionControl]", "[VerSionControl]--UserRequestID:RemoveUserRequest " + key);
		}
	}
	
	public boolean CancleUserRequest(long iRequestID){
		if(CancleUserRequestBase(iRequestID)) {
			Log.d("[VerSionControl]", "[VerSionControl]--UserRequestID:CancleUserRequest success" + iRequestID);
			RemoveUserRequest(iRequestID);
			return true;
		} else {
			Log.d("[VerSionControl]", "[VerSionControl]--UserRequestID:CancleUserRequest Failed" + iRequestID);
			return false;
		}
		
	}
	
	public static long getM_sRequestID() {
		return m_sRequestID;
	}

	public static void setM_sRequestID(long m_sRequestID) {
		VersionControl_ResponseFactory.m_sRequestID = m_sRequestID;
	}
	
}
