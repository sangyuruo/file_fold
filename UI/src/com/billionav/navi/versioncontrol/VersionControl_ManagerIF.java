package com.billionav.navi.versioncontrol;

import java.util.ArrayList;

import com.billionav.jni.W3JNI;
import com.billionav.navi.net.PNetLog;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestFactory;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetAllAPKUpdateInfos;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetCameraPara;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetCameraVer;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetMetaDataInfo;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetSrchTopDataVersion;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetTopDataVersion;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetUpdateLogInfo;
import com.billionav.navi.versioncontrol.Request.VersionControl_RequestGetVersionList;


public class VersionControl_ManagerIF {
	public static long INVALIDATE_REQUESTID = -1;
	private static VersionControl_ManagerIF m_sInstance = new VersionControl_ManagerIF();
	
	//apk update url
	private String m_strVersionServerUrl ;
	//map update url
	private String m_strDataVersionServerUrl;
	//srch update url
	private String m_strSrchDataVersionServerUrl;
	//camera version url
	private String m_strCameraVerServerUrl;
	//camera update url
	private String m_strCameraParaDownloadServerUrl;
	
	//Data
	private ArrayList<VersionControl_VersionDataFormat> m_cVersionList= new ArrayList<VersionControl_VersionDataFormat>();
	private VersionControl_VersionDataFormat	m_cLatestVersionInfo = new VersionControl_VersionDataFormat();
	private ArrayList<VersionControl_MetaDataFormat> m_cMetaDataList = new ArrayList<VersionControl_MetaDataFormat>();
	private String m_strTopDataVersion = "";
	private String m_strMinDataVersion = "";
	
	private String m_strSrchTopDataVersion = "";
	private String m_strSrchMinDataVersion = "";
	
	private int m_iVersionCompareResult = VersionControl_VersionComparator.VERSION_LOCAL_EQUAL;
	private String m_strReleaseUpdateLogInfos = "";
	private int m_ApkUpdateType;
	public int getApkUpdateType() {
		return m_ApkUpdateType;
	}
	public void setApkUpdateType(int type) {
		this.m_ApkUpdateType = type;
	}
	public static VersionControl_ManagerIF	Instance() {
		return m_sInstance;
	}
	
	private VersionControl_ManagerIF() {
		Init();
	}
	
	public void Init(){
		InitUrlFromIni();
	}
	
	
	private void InitUrlFromIni()
	{
		String requestRootUrl = W3JNI.getConfigValue("VersionServerUrl");
		//String requestRootUrl = "http://172.26.10.55:8080/api/update/3.0/ch/";
		if (requestRootUrl.length() == 0) {
			m_strVersionServerUrl = null;
			PNetLog.e("VersionControl_ManagerIF:: InitUrl error requestRootUrl=["
					+ requestRootUrl + "]\n");
		} else {
			m_strVersionServerUrl = requestRootUrl;
		}
		
		String requestDataVerUrl = W3JNI.getConfigValue("DataVersionServerUrl");
		if (requestDataVerUrl.length() == 0) {
			m_strDataVersionServerUrl = null;
			PNetLog.e("VersionControl_ManagerIF:: InitUrl error requestDataVerUrl=["
					+ requestDataVerUrl + "]\n");
		} else {
			m_strDataVersionServerUrl = requestDataVerUrl;
		}
		
		if (requestRootUrl.length() == 0) {
			m_strVersionServerUrl = null;
			PNetLog.e("VersionControl_ManagerIF:: InitUrl error srchRequestRootUrl=["
					+ requestRootUrl + "]\n");
		} else {
			m_strVersionServerUrl = requestRootUrl;
		}
		
		String srchRequestDataVerUrl = W3JNI.getConfigValue("SrchDataVersionServerUrl");
		if (srchRequestDataVerUrl.length() == 0) {
			m_strSrchDataVersionServerUrl = null;
			PNetLog.e("VersionControl_ManagerIF:: InitUrl error requestDataVerUrl=["
					+ srchRequestDataVerUrl + "]\n");
		} else {
			m_strSrchDataVersionServerUrl = srchRequestDataVerUrl;
		}
		
		String cameraVerUrl = W3JNI.getConfigValue("cameraver");
		if (cameraVerUrl.length() == 0) {
			m_strCameraVerServerUrl = null;
			PNetLog.e("VersionControl_ManagerIF:: InitUrl error cameraVerUrl=["
					+ cameraVerUrl + "]\n");
		} else {
			m_strCameraVerServerUrl = cameraVerUrl;
		}		
		
		String cameraParaUrl = W3JNI.getConfigValue("camerapara");
		if (cameraParaUrl.length() == 0) {
			m_strCameraParaDownloadServerUrl = null;
			PNetLog.e("VersionControl_ManagerIF:: InitUrl error cameraParaUrl=["
					+ cameraParaUrl + "]\n");
		} else {
			m_strCameraParaDownloadServerUrl = cameraParaUrl;
		}
	}
	
	//Merge the GetAPKVersionListInfo and GetMetaDataInfo
	/**
	 * IF need update 
	 * 		then getMetaData and Get Release update info
	 * ELSE 
	 * 		not to get any infos
	 */
	public long GetAllAPKUpdateInfos(String strCurAPKVersion) {
		VersionControl_RequestGetAllAPKUpdateInfos request = (VersionControl_RequestGetAllAPKUpdateInfos) VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_ALL_APK_UPDATE_INFOS);
		request.setStrURL(getM_strVersionServerUrl());
		request.GetAllAPKUpdateInfos(strCurAPKVersion);
		return request.getM_lVersionControlRequestID();
	}
	
	//Get the camera infos Details
	public long GetCameraInfos() {
		VersionControl_RequestGetCameraPara request = (VersionControl_RequestGetCameraPara) VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_CAMERA_INFOS);
		request.getCameraInfos(m_strCameraParaDownloadServerUrl);
		return request.getM_lVersionControlRequestID();
	}
	
	public long getCameraServerVer() {
		VersionControl_RequestGetCameraVer request = (VersionControl_RequestGetCameraVer) VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_CAMERA_VER);
		request.getCameraInfos(m_strCameraVerServerUrl);
		return request.getM_lVersionControlRequestID();
	
	}
	
	//Get the APK Update Text infos Details
	public long getAPKUpdateLogInfos(String strLogInfoFileName) {
		VersionControl_RequestGetUpdateLogInfo request = (VersionControl_RequestGetUpdateLogInfo)VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_UPDATE_LOG_INFO);
		request.getUpdateLogInfo(strLogInfoFileName);
		return request.getM_lVersionControlRequestID();
	}
	
	//Compare Version return 
	/**
	 *  VersionControl_VersionComparator
	 * 	public static int VERSION_LOCAL_HIGHER 	= 1;
	 *	public static int VERSION_LOCAL_EQUAL 	= 0;
	 *	public static int VERSION_LOCAL_LOWER 	= -1;
	 *	public static int VERSION_FORMAT_ERROR 	= -2;
	 */
	public int CompareVersion(String strLocalVersion, String strLatestVersion) {
		return VersionControl_VersionComparator.VersionCompare(strLocalVersion, strLatestVersion);
	}
	
	
	//GetVersionListInfo
	public long GetAPIVersionListInfo() {
		VersionControl_RequestGetVersionList request =(VersionControl_RequestGetVersionList)VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_VERSION_LIST);
		request.setStrURL(getM_strVersionServerUrl());
		request.GetVersionInfoList();
		return request.getM_lVersionControlRequestID();
	}
	
	//Get MetaDataInfo 
	public long GetAPIMetaDataInfo(String strFileName) {
		VersionControl_RequestGetMetaDataInfo request = (VersionControl_RequestGetMetaDataInfo) VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_METADATA_INFO);
		request.setStrMetaDataServerURL(getM_strVersionServerUrl());
		request.GetMetaDataInfo(strFileName);
		return request.getM_lVersionControlRequestID();
	}
	
	//Get DataVersion
	public long GetTopDataVersionInfo(String DataFormatVer) {
		VersionControl_RequestGetTopDataVersion request = (VersionControl_RequestGetTopDataVersion) VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_TOP_DATA_INFO);
		request.setStrDataServerURL(m_strDataVersionServerUrl);
		request.GetTopDataVersion(DataFormatVer);
		return request.getM_lVersionControlRequestID();
	}
	
	//Get SrchDataVersion
	public long GetTopSearchDataVersionInfo(String srchDataFormateVer){
		VersionControl_RequestGetSrchTopDataVersion request = (VersionControl_RequestGetSrchTopDataVersion) VersionControl_RequestFactory.Instance().CreateRequest(VersionControl_RequestType.UC_REQ_VC_GET_SEARCH_TOP_DATA_INFO);
		request.setStrDataServerURL(m_strSrchDataVersionServerUrl);
		request.GetSrchTopDataVersion(srchDataFormateVer);
		return request.getM_lVersionControlRequestID();
	}
	
	public String GetTopDataVersion() {
		return m_strTopDataVersion;
	}
	
	

	public String getM_strVersionServerUrl() {
		return m_strVersionServerUrl;
	}

	public void setM_strVersionServerUrl(String m_strVersionServerUrl) {
		this.m_strVersionServerUrl = m_strVersionServerUrl;
	}

	public ArrayList<VersionControl_VersionDataFormat> getM_cVersionList() {
		return m_cVersionList;
	}

	public void setM_cVersionList(
			ArrayList<VersionControl_VersionDataFormat> m_cVersionList) {
		this.m_cVersionList = m_cVersionList;
	}

	public VersionControl_VersionDataFormat getM_cLatestVersionInfo() {
		return m_cLatestVersionInfo;
	}

	public void setM_cLatestVersionInfo(
			VersionControl_VersionDataFormat m_cLatestVersionInfo) {
		this.m_cLatestVersionInfo = m_cLatestVersionInfo;
	}

	public ArrayList<VersionControl_MetaDataFormat> getM_cMetaDataList() {
		return m_cMetaDataList;
	}

	public void setM_cMetaDataList(
			ArrayList<VersionControl_MetaDataFormat> m_cMetaDataList) {
		this.m_cMetaDataList = m_cMetaDataList;
	}

	public String getM_strDataVersionServerUrl() {
		return m_strDataVersionServerUrl;
	}

	public void setM_strDataVersionServerUrl(String m_strDataVersionServerUrl) {
		this.m_strDataVersionServerUrl = m_strDataVersionServerUrl;
	}

	public String getM_strTopDataVersion() {
		return m_strTopDataVersion;
	}

	public void setM_strTopDataVersion(String m_strTopDataVersion) {
		this.m_strTopDataVersion = m_strTopDataVersion;
	}
	
	public int GetVersionCompareResult() {
		return m_iVersionCompareResult;
	}
	
	public void SetVersionCompareResult(int iVersionCompareResult) {
		m_iVersionCompareResult = iVersionCompareResult;
	}
	
	public String GetReleaseUpdateInfos() {
		return m_strReleaseUpdateLogInfos;
	}
	
	public void SetReleaseUpdateInfos(String infos) {
		m_strReleaseUpdateLogInfos = infos;
	}

	public String getM_strMinDataVersion() {
		return m_strMinDataVersion;
	}

	public void setM_strMinDataVersion(String m_strMinDataVersion) {
		this.m_strMinDataVersion = m_strMinDataVersion;
	}

	public String getM_strSrchTopDataVersion() {
		return m_strSrchTopDataVersion;
	}

	public void setM_strSrchTopDataVersion(String m_strSrchTopDataVersion) {
		this.m_strSrchTopDataVersion = m_strSrchTopDataVersion;
	}

	public String getM_strSrchMinDataVersion() {
		return m_strSrchMinDataVersion;
	}

	public void setM_strSrchMinDataVersion(String m_strSrchMinDataVersion) {
		this.m_strSrchMinDataVersion = m_strSrchMinDataVersion;
	}
	
	
	
}
