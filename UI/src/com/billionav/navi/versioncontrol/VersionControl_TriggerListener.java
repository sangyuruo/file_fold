package com.billionav.navi.versioncontrol;

import android.util.Log;

import com.billionav.navi.menucontrol.ITriggerListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseBase;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetAllAPKUpdateInfos;
import com.billionav.navi.versioncontrol.Response.VersionControl_ResponseGetUpdateLogInfo;

public class VersionControl_TriggerListener implements ITriggerListener{
	private static VersionControl_TriggerListener listener = new VersionControl_TriggerListener(); 
	
	public static boolean OnTriggerForWinscape(NSTriggerInfo cTriggerInfo) {
		return VersionControl_TriggerListener.Instance().TriggerForWinscape(cTriggerInfo);
	}
	
	public static VersionControl_TriggerListener Instance() {
		return listener;
	}
	
	private VersionControl_TriggerListener() {
		
	}
	
	@Override
	public boolean TriggerForWinscape(NSTriggerInfo cTriggerInfo) {
		boolean bNotPass = false;
		long lResult = cTriggerInfo.GetlParam1();
		long lDetail = cTriggerInfo.GetlParam2();
		String strIndentifyKey = cTriggerInfo.GetString1();
		if (VersionControl_CommonVar.UIC_MN_TAG_UC_GET_UPDATE_LOG_INFO == cTriggerInfo.m_iTriggerID) {
			if (VersionControl_ResponseGetAllAPKUpdateInfos.IDENTIFY_KEY_APK_UPDATE_INFOS.equals(strIndentifyKey) ) {
				Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos --GetUpdateInfos Response iStatus = " + lResult + " iDetails=" +lDetail);
				NSTriggerInfo cInfo = new NSTriggerInfo();
				cInfo.SetTriggerID(VersionControl_CommonVar.UIC_MN_TAG_UC_GET_ALL_APK_UPDATE_INFOS);
				
				if ( VersionControl_ResponseBase.UC_RESPONES_SUC == lResult) {
					cInfo.SetlParam1(VersionControl_ManagerIF.Instance().getApkUpdateType());
					String strUpdateInfo = VersionControl_ManagerIF.Instance().GetReleaseUpdateInfos();
					VersionControl_VersionDataFormat format = VersionControl_ManagerIF.Instance().getM_cLatestVersionInfo();
					if (null != format) {
						format.setStrAPKReleaseInfo(strUpdateInfo);
					}
				} else {
					//if get the update Info not success but also set the lResult success 
					cInfo.SetlParam1(VersionControl_ResponseGetAllAPKUpdateInfos.VC_ERROR_CODE_HAVE_UPDATE);
					cInfo.SetlParam2(VersionControl_ResponseGetUpdateLogInfo.VC_ERROR_CODE_GET_UPDATE_INFO_FAILED);
				}
				MenuControlIF.Instance().TriggerForScreen(cInfo);
				Log.d("[VersionControl]", "[VersionControl]: GetAllAPKUpdateInfos Response iStatus = " + cInfo.GetlParam1() + " iDetails=" +cInfo.GetlParam2());
				bNotPass = true;
			}
		}
			
		return bNotPass;
	}

}
