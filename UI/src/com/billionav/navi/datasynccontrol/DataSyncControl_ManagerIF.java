package com.billionav.navi.datasynccontrol;

import java.util.UUID;

import com.billionav.jni.W3JNI;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestAmeba;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestDelPOIImage;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestDownLoadPOIImage;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestPOI;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestRoute;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestSinglePOI;
import com.billionav.navi.datasynccontrol.Request.DataSyncControl_RequestUploadPOIImage;
import com.billionav.navi.datasynccontrol.Response.DataSyncControl_ResponseFactory;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PNetLog;
import com.billionav.navi.uitools.SystemTools;

public class DataSyncControl_ManagerIF{
	private static DataSyncControl_ManagerIF m_sInstance = null;
	private String m_strServerUrl = null; 
	private String m_strPOIServerUrl ;
	private DataSyncControl_RouteInfo m_cRouteInfo;
//	private static int m_syncType = jniPointControl_new.PNT_SYNC_TYPE_ALL;

	private DataSyncControl_AmebaData m_cAmebaData;
	public static String m_strMutiplePolyGonSplit = "|";
	
	public static long INVALIDATE_REQUESTID = -1;
	
	private long m_poiSyncRequestID = -1;
	private int m_SyncRequestStatus = DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL; // 0 request 1 cancel success 2 cancel failed
	
	private int m_currentStatus = -1; //0 poi file no finish 1 poi file finish 2 meger file finsih
	
	public void setCurrentStatus(int status){
		m_currentStatus = status;
	}
	public int getCurrentStatus(){
		return m_currentStatus;
	}
	public void setSyncRequestStatus(int status){
		m_SyncRequestStatus = status;
	}
	
	public int getSyncRequestStatus(){
		return m_SyncRequestStatus;
	}
	
	public void setPoiSyncRequestID(long id){
		m_poiSyncRequestID = id;
	}
	
	public long getPoiSyncRequestID(){
		return m_poiSyncRequestID;
	}
	public int getSyncType() {
		return 0;
	}
	
	public static DataSyncControl_ManagerIF Instance() {
		if (null == m_sInstance) {
			m_sInstance = new DataSyncControl_ManagerIF();
			return m_sInstance;
		}
		return m_sInstance;
	}
	
	private DataSyncControl_ManagerIF() {
		Init();
	}
	
	public void Init(){
		InitUrlFromIni();
	}
	
	
	//Add Cancel UserRequest IF
	public boolean CancleUserRequest(long lRequestID) {
		return DataSyncControl_ResponseFactory.Instance().CancleUserRequest(lRequestID);
	}
	
//------------------------------------------Ameba--------------------------------
		
	public long OnAmebaRequest(DataSyncControl_AmebaParam amebaParam) {
		DataSyncControl_RequestAmeba request =(DataSyncControl_RequestAmeba)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_AMEBA);
		request.setcAmebaParam(amebaParam);
		request.SendAmebaRequest();
		return request.getM_iUserRequestID();
	}
			
	
//------------------------------------------POI----------------------------------
	/**
	 * @Function OnUIPOIRequest
	 * @see the IF for UI 
	 * @param void
	 * @return void
	 * 
	 */
	public void OnUIPOIRequest(int SyncType) {
//		if(jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT == CallJNIReadyPOIFile(SyncType)){
//			NSTriggerInfo cInfo = new NSTriggerInfo();
//			cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
//			cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_DOWNLOAD_ERROR);
//			MenuControlIF.Instance().TriggerForScreen(cInfo);
//		}
	}
	
//	public void CancelPOISyncRequest(){
//		if(m_currentStatus == DataSyncControl_CommonVar.SYNCPOISTAYUS_POIFILE_NO_FINISH){
////			jniPointControl_new.Instance().CancelSync();
//			setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NO_NEED_REQUEST);
////			if(jniPointControl_new.Instance().CancelSync() == jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT){
////				NSTriggerInfo cInfo = new NSTriggerInfo();
////				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
////				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
////				MenuControlIF.Instance().TriggerForScreen(cInfo);
////				//setPoiSyncRequestID(-1);
////				setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL);
////			}else{
////				setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NO_NEED_REQUEST);
////			}
////			jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,DataSyncControl_ManagerIF.Instance().getSyncType());
//			
//		}else if(m_currentStatus == DataSyncControl_CommonVar.SYNCPOISTAYUS_POIFILE_FINISH){
//			if(getPoiSyncRequestID()>0){
//				if(DataSyncControl_ManagerIF.Instance().CancleUserRequest(getPoiSyncRequestID())==true){
////					jniPointControl_new.Instance().CancelSync();
////					int status = jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,DataSyncControl_ManagerIF.Instance().getSyncType());
////					if(status == jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT){
////						NSTriggerInfo cInfo = new NSTriggerInfo();
////						cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
////						cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
////						cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
////						MenuControlIF.Instance().TriggerForScreen(cInfo);
////						setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL);
////					}else{
////						setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_CANCELSUCCESS);
////					}
//				}else{
//					setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_CANCELFAILED);
//				}
//			}else{
////				jniPointControl_new.Instance().CancelSync();
////				int status =jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,DataSyncControl_ManagerIF.Instance().getSyncType());
////				if(status == jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT){
////					NSTriggerInfo cInfo = new NSTriggerInfo();
////					cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
////					cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
////					cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
////					MenuControlIF.Instance().TriggerForScreen(cInfo);
//				}
//				setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL);
//			}
//			setPoiSyncRequestID(-1);
//		}else if(m_currentStatus == DataSyncControl_CommonVar.SYNCPOISTAYUS_MERGEFILE_START){
//			jniPointControl_new.Instance().CancelSync();
//			int status =jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,DataSyncControl_ManagerIF.Instance().getSyncType());
//			if(status == jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT){
//				NSTriggerInfo cInfo = new NSTriggerInfo();
//				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
//				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
//				cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
//				MenuControlIF.Instance().TriggerForScreen(cInfo);
//			}
//			setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL);
//		}else{
//			jniPointControl_new.Instance().CancelSync();
//			jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,DataSyncControl_ManagerIF.Instance().getSyncType());
//		}
//		setCurrentStatus(-1);
//	}
	
	
//	public long OnPOIRequest(String strFileName) {
//		DataSyncControl_RequestPOI request =(DataSyncControl_RequestPOI)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_POI_ID);
//		String strUUID = UUID.randomUUID().toString();
// 
//		String strResFileFullName = jniPointControl_new.Instance().GetSyncFilePath() + strUUID +DataSyncControl_CommonVar.fileSurfix;		
//		String strFileFullName = jniPointControl_new.Instance().GetSyncFilePath() + strFileName;
//		
//		request.setStrFileFullName(strFileFullName);
//		request.setStrResFileFullName(strResFileFullName);
//		request.setStrResFileName(strUUID +DataSyncControl_CommonVar.fileSurfix);
//		request.setiSyncType(m_syncType);
//		request.setStrPOISerUrl(getM_strPOIServerUrl());
//		
//		if (SystemTools.isCH()) {
//			request.setStrPOIurl("point/sync");
//		} else if (SystemTools.isJP()) {
//			request.setStrPOIurl("searchservice/ver1.0/mydata/poi/sync");
//		} else if (SystemTools.isOS()) {
//			request.setStrPOIurl("searchservice/ver1.0/mydata/poi/sync");
//		}
//		
//		request.SendData();
//		return request.getM_iUserRequestID();
//	}
	
//	private int CallJNIReadyPOIFile(int SyncType){ 
//		String strFileName = UUID.randomUUID().toString();
//		String fileName = strFileName + DataSyncControl_CommonVar.fileSurfix;
//		m_syncType = SyncType;
//		
//		//Defult Store Path /mnt/sdcrad/USER/RW/POINT/SYNC/ Send The FileName to C++ Side
//		DataSyncControl_ManagerIF.Instance().setCurrentStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_POIFILE_NO_FINISH);
//		return jniPointControl_new.Instance().PrepareSynchronizeFile(fileName,SyncType);
//	}
	//On Sync Single POI 
	
//	public long OnSyncSinglePOIRequest(String strFileName) {
//		DataSyncControl_RequestSinglePOI request =(DataSyncControl_RequestSinglePOI)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_SINGLE_POI_ID);
//		String strUUID = UUID.randomUUID().toString();
// 
//		String strResFileFullName = jniPointControl_new.Instance().GetSyncFilePath() + strUUID +DataSyncControl_CommonVar.fileSurfix;		
//		String strFileFullName = jniPointControl_new.Instance().GetSyncFilePath() + strFileName;
//		
//		request.setStrFileFullName(strFileFullName);
//		request.setStrResFileFullName(strResFileFullName);
//		request.setStrResFileName(strUUID +DataSyncControl_CommonVar.fileSurfix);
//		request.setiSyncType(m_syncType);
//		request.setStrPOISerUrl(getM_strPOIServerUrl());
//		
//		
//		request.setStrPOIurl("point/sync");//TODO
//		
//		request.SendSyncSinglePOIData();
//		return request.getM_iUserRequestID();
//	}
	
//------------------------------------------Route----------------------------------	
	
	/**
	 * @Function Check for the Path Exist In WebSite
	 * @param	VOID
	 * @return	int 	0 	:	NoRoute In Server
	 * 					1	:	Have Route On Server and the Device haven't Download the Path Before
	 * 					2	:	Have Route On Server and the Device had DownLoad the path Before 
	 */
	
	public long GetTheWebRouteExistStatus(String strDeviceNo) {
		DataSyncControl_RequestRoute request =(DataSyncControl_RequestRoute)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_PATH_ID);
		String strURL = "mydata/travel/getwebpath";
		request.setUrl_getpath(strURL);
		request.setStrDeviceno(strDeviceNo);
		request.GetTheWebRouteStatus();
		return request.getM_iUserRequestID();
	}
	
	public long GetTheWebRoute(String strDeviceNo) {
		DataSyncControl_RequestRoute request =(DataSyncControl_RequestRoute)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_PATH_ID);
		String strURL = "mydata/travel/getwebpath";
		request.setUrl_getpath(strURL);
		request.setStrDeviceno(strDeviceNo);
		request.DataSyncControl_GetWebPath();
		return request.getM_iUserRequestID();
	}

//---------------------------------------DownLoad/Upload Image----------------------------------	
	
//	public long UploadPOIImage(String strUUID, String strRecordKind, String strUserID) {
//		DataSyncControl_RequestUploadPOIImage request = (DataSyncControl_RequestUploadPOIImage)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_UPLOAD_IMAGE);
//		String strURL = "point/uploadimage";//TODO mydata/travel/getwebpath
//		String strImgPath = jniPointControl_new.Instance().GetPhotoRootPath() + strUUID;//TODO
//		request.setStrPOISerUrl(getM_strPOIServerUrl());
//		request.setStrPOIurl(strURL);
//		request.setM_StrPassParam01(strUserID);
//		request.uploadImage(strUUID, strRecordKind, strImgPath);
//		return request.getM_iUserRequestID();
//	}
	
//	public long DownLoadPOIImage(String strUUID, String strRecordKind, String strUserID) {
//		DataSyncControl_RequestDownLoadPOIImage request = (DataSyncControl_RequestDownLoadPOIImage)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_DOWNLOAD_IMAGE);
//		String strURL = "point/downloadimage";//TODO
//		//String strImgPath = jniPointControl_new.Instance().GetPhotoRootPath() + strUUID;//TODO
//		String strStoredPath = jniPointControl_new.Instance().GetPhotoRootPath() + strUUID; //TODO
//		request.setStrPOISerUrl(getM_strPOIServerUrl());
//		request.setStrPOIurl(strURL);
//		request.setStrStoredPath(strStoredPath);
//		request.setM_strFilePath(strStoredPath);
//		request.setM_StrPassParam01(strUserID);
//		request.DownloadPOIImage(strUUID, strRecordKind);
//		return request.getM_iUserRequestID();
//	}
	
	public long DeletePOIImage(String strUUID, String strRecordKind, String strUserID) {
		DataSyncControl_RequestDelPOIImage request = (DataSyncControl_RequestDelPOIImage)DataSyncControl_RequestManager.Instance().CreateRequest(DataSyncControl_RequestType.DS_REQ_DEL_IMAGE);
		String strURL = "point/deleteimage";//TODO
		request.setStrPOISerUrl(getM_strPOIServerUrl());
		request.setStrPOIurl(strURL);
		request.setM_StrPassParam01(strUserID);
		request.DelPOIImage(strUUID, strRecordKind);
		return request.getM_iUserRequestID();
	}
	
	
//------------------------------------------Common----------------------------------		
	private void InitUrlFromIni()
	{
        String requestRootUrl = W3JNI.getConfigValue("POINTSERVERUrl");  
		if( requestRootUrl.length() == 0 )
		{
			m_strPOIServerUrl = null;
			PNetLog.e( "DataSyncControl_ManagerIF:: InitUrl error requestRootUrl=["+requestRootUrl + "]\n" );
		}
		else
		{
			m_strPOIServerUrl = requestRootUrl;
		}
		
		String requestUserUrl = W3JNI.getConfigValue("USERSRVURL");  
		if( requestUserUrl.length() == 0 )
		{
			m_strServerUrl = null;
			PNetLog.e( "DataSyncControl_ManagerIF:: InitUrl error requestUserUrl=["+requestUserUrl + "]\n" );
		}
		else
		{
			m_strServerUrl = requestUserUrl;
		}
		
	}
	
	
	public String GetSRVUrl() {
		return m_strServerUrl;
	}

	public String getM_strPOIServerUrl() {
		return m_strPOIServerUrl;
	}

	public void setM_strPOIServerUrl(String m_strPOIServerUrl) {
		this.m_strPOIServerUrl = m_strPOIServerUrl;
	}

	public DataSyncControl_RouteInfo getM_cRouteInfo() {
		return m_cRouteInfo;
	}

	public void setM_cRouteInfo(DataSyncControl_RouteInfo m_cRouteInfo) {
		this.m_cRouteInfo = m_cRouteInfo;
	}
	public DataSyncControl_AmebaData getM_cAmebaData() {
		return m_cAmebaData;
	}

	public void setM_cAmebaData(DataSyncControl_AmebaData m_cAmebaData) {
		this.m_cAmebaData = m_cAmebaData;
	}
	
	
	
}