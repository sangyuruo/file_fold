package com.billionav.navi.datasynccontrol;

import com.billionav.navi.menucontrol.ITriggerListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;

public class DataSyncControl_TriggerListener implements ITriggerListener{
	
	private static DataSyncControl_TriggerListener listener = null; 
	
	
	public static boolean OnTriggerForWinscape(NSTriggerInfo cTriggerInfo) {
		return DataSyncControl_TriggerListener.Instance().TriggerForWinscape(cTriggerInfo);
	}
	
	@Override
	public boolean TriggerForWinscape(NSTriggerInfo cTriggerInfo) {
		boolean bNotPass = false;
		//TODO 
		switch (cTriggerInfo.m_iTriggerID) {
		case DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_POIFILE_FINISH :
			DataSyncControl_ManagerIF.Instance().setCurrentStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_POIFILE_FINISH);
			long iNeedSync = cTriggerInfo.GetlParam1();
			if (DataSyncControl_CommonVar.NEED_SYNC == iNeedSync) {
				if (DataSyncControl_ManagerIF.Instance().getSyncRequestStatus() == DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NO_NEED_REQUEST) {
					//NO_NEED_REQUEST
					DataSyncControl_ManagerIF.Instance().setSyncRequestStatus(DataSyncControl_CommonVar.SYNCPOISTAYUS_NET_NOCANCEL);
//					jniPointControl_new.Instance().CancelSync();
//					jniPointControl_new.Instance().MergeSynchronizeFile(jniPointControl_new. PNT_UNKNOW_UUID,DataSyncControl_ManagerIF.Instance().getSyncType());

				} else {
//					String strFileName = jniPointControl_new.Instance()
//							.GetSyncFileName();
//					long requestID = DataSyncControl_ManagerIF.Instance().OnPOIRequest(strFileName);
//					if (DataSyncControl_ManagerIF.INVALIDATE_REQUESTID != requestID) {
//						// TODO LOG::send success
//						DataSyncControl_ManagerIF.Instance().setPoiSyncRequestID(requestID);
//					} else {
//						// TODO LOG::some string is empty
//					}
				}

			} else if (DataSyncControl_CommonVar.NOTNEED_SYNC == iNeedSync) {
				NSTriggerInfo cInfo = new NSTriggerInfo();
				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_NONEEDSYNC);
				MenuControlIF.Instance().TriggerForScreen(cInfo);
			}  else if (DataSyncControl_CommonVar.CANCEL == iNeedSync) {//cancel success
//				jniPointControl_new.Instance().CancelSync();
//				jniPointControl_new.Instance().MergeSynchronizeFile("",DataSyncControl_ManagerIF.Instance().getSyncType());
				NSTriggerInfo cInfo = new NSTriggerInfo();
				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
				MenuControlIF.Instance().TriggerForScreen(cInfo);
			} else {
				
			}
			bNotPass = true;
			break;
		case DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_MERGEFILE_FINISH:
			DataSyncControl_ManagerIF.Instance().setCurrentStatus(-1);
			long iMergeStatus = cTriggerInfo.GetlParam1();
			if (DataSyncControl_CommonVar.SYNCMERGESTATUS_SUCCESS == iMergeStatus) {
				NSTriggerInfo cInfo = new NSTriggerInfo();
				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_SUCCESS);
				cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
				MenuControlIF.Instance().TriggerForScreen(cInfo);
			} else if (DataSyncControl_CommonVar.SYNCMERGESTATUS_FAILESD == iMergeStatus) {
				NSTriggerInfo cInfo = new NSTriggerInfo();
				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_MERGE_ERROR);
				cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
				MenuControlIF.Instance().TriggerForScreen(cInfo);
			} if (DataSyncControl_CommonVar.SYNCMERGESTATUS_CANCEL == iMergeStatus) {
				NSTriggerInfo cInfo = new NSTriggerInfo();
				cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_POI_SYNC_RESULT);
				cInfo.SetlParam1(DataSyncControl_CommonVar.SYNCPOISTAYUS_CANCEL);
				cInfo.SetlParam3(DataSyncControl_ManagerIF.Instance().getSyncType());
				MenuControlIF.Instance().TriggerForScreen(cInfo);
			} else {
				
			}
			bNotPass = true;	
			break;
		}
		return bNotPass;
	}
	
	
	public static DataSyncControl_TriggerListener Instance() {
		if (null == listener) {
			listener = new DataSyncControl_TriggerListener();
			return listener;
		}
		return listener;
	}
	
	private DataSyncControl_TriggerListener() {
		
	}
	
	
}