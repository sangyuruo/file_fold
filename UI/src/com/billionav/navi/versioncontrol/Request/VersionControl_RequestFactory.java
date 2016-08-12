package com.billionav.navi.versioncontrol.Request;

import com.billionav.navi.versioncontrol.VersionControl_RequestType;


public class VersionControl_RequestFactory {
private static VersionControl_RequestFactory m_cVersionControlRequestFactory = new VersionControl_RequestFactory();
	
	private VersionControl_RequestFactory(){
	}
	
	public static VersionControl_RequestFactory Instance(){
		return m_cVersionControlRequestFactory;
	}
	
	public VersionControl_RequestBase CreateRequest(int iRequesType){
		VersionControl_RequestBase request = null;
		switch (iRequesType) {
			case VersionControl_RequestType.UC_REQ_VC_GET_VERSION_LIST:
				request = new VersionControl_RequestGetVersionList(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_METADATA_INFO:
				request = new VersionControl_RequestGetMetaDataInfo(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_TOP_DATA_INFO:
				request = new VersionControl_RequestGetTopDataVersion(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_ALL_APK_UPDATE_INFOS:
				request = new VersionControl_RequestGetAllAPKUpdateInfos(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_UPDATE_LOG_INFO:
				request = new VersionControl_RequestGetUpdateLogInfo(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_SEARCH_TOP_DATA_INFO:
				request = new VersionControl_RequestGetSrchTopDataVersion(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_CAMERA_INFOS:
				request = new VersionControl_RequestGetCameraPara(iRequesType);
				break;
			case VersionControl_RequestType.UC_REQ_VC_GET_CAMERA_VER:
				request = new VersionControl_RequestGetCameraVer(iRequesType);
		}
		return request;
	}
}
