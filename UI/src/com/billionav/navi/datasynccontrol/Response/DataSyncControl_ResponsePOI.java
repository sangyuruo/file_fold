package com.billionav.navi.datasynccontrol.Response;

import java.io.File;

import android.util.Log;

import com.billionav.navi.datasynccontrol.DataSyncControl_CommonVar;
import com.billionav.navi.datasynccontrol.DataSyncControl_ManagerIF;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;

public class DataSyncControl_ResponsePOI extends DataSyncControl_ResponseFileBase{
	
	public DataSyncControl_ResponsePOI(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		if(DataSyncControl_ManagerIF.Instance().getSyncRequestStatus() == DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_CANCELFAILED){
//			jniPointControl_new.Instance().CancelSync();
//			int status = jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,getM_iSyncType());
//			if(status == jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT){
//				NSTriggerInfo cInfo = new NSTriggerInfo();
//				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
//				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
//				cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
//				MenuControlIF.Instance().TriggerForScreen(cInfo);
//			}
			DataSyncControl_ManagerIF.Instance().setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL);
			return;
		}
		DataSyncControl_ManagerIF.Instance().setCurrentStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_MERGEFILE_START);
		int iResCode = getResCode();
		String strResFileName = "";
		strResFileName = getM_strResFileName();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			DeleteFilePostToServer();
//			if(jniPointControl_new.PNT_REQUEST_STATUS_NOWAIT == CallJNIMergeFile(DataSyncControl_CommonVar.SYNCDOWNLOADSTATUS_SUCCESS, strResFileName, getM_iSyncType()))
//			{
//				//DataSyncControl_ManagerIF.Instance().setCurrentStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_MERGEFILE_START);
//				Log.d("icon", "SYNCPOISTAYUS_NET_CANCELFAILED----9999999");
//				NSTriggerInfo cInfo = new NSTriggerInfo();
//				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
//				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_DOWNLOAD_ERROR);
//				cInfo.SetlParam2(iResCode);
//				cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
//				MenuControlIF.Instance().TriggerForScreen(cInfo);
//			}
		} else {
//			CallJNIMergeFile(DataSyncControl_CommonVar.SYNCDOWNLOADSTATUS_FAILED, "", getM_iSyncType());
			//DataSyncControl_ManagerIF.Instance().setCurrentStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_MERGEFILE_START);
			NSTriggerInfo cInfo = new NSTriggerInfo();
			cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
			cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_DOWNLOAD_ERROR);
			cInfo.SetlParam2(iResCode);
			cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
			MenuControlIF.Instance().TriggerForScreen(cInfo);
		}
		DataSyncControl_ManagerIF.Instance().setPoiSyncRequestID(-1);
		
	} 
	
	private boolean DeleteFilePostToServer() {
		String strFilePath = getM_strFilePath();
		if ("".equals(strFilePath)) {
			return false;
		}
		File file = new File(strFilePath);
		if(file.exists()) {
			file.delete();
		}
		return true;
	}
	
//	public int CallJNIMergeFile(int iDownloadStatus, String fileName, int iSyncType){
//		return jniPointControl_new.Instance().MergeSynchronizeFile(fileName,iSyncType);
//	}
	
}
