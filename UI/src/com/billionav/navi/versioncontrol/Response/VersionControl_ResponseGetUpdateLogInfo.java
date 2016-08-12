package com.billionav.navi.versioncontrol.Response;

import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.versioncontrol.VersionControl_CommonVar;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;

public class VersionControl_ResponseGetUpdateLogInfo extends VersionControl_ResponseBase{
	private int iStatus = UC_RESPONES_SUC;
	private int iDetails = 0;
	
	public static final int VC_ERROR_CODE_GET_UPDATE_INFO_SUCCESS = 201;
	public static final int VC_ERROR_CODE_GET_UPDATE_INFO_FAILED = 202;
	
	public VersionControl_ResponseGetUpdateLogInfo(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			byte[] receiveData = getReceivebuf();
			String strData = "";
			if (null != receiveData) {
				strData = new String(receiveData);
				Log.d("[VersionControl]", "[VersionControl]: getUpdateLogInfo TextData : " + strData);
			} else {
				Log.d("[VersionControl]", "[VersionControl]: getUpdateLogInfo TextData is null ");
			}
			VersionControl_ManagerIF.Instance().SetReleaseUpdateInfos(strData);
		} else {
			iStatus = UC_RESPONES_SRV_FAIL;
			iDetails = iResCode;
		}
		
		Log.d("[VersionControl]", "[VersionControl]: getUpdateLogInfo Response iStatus = " + iStatus + " iDetails=" +iDetails);
		NSTriggerInfo cInfo = new NSTriggerInfo();
		cInfo.SetTriggerID(VersionControl_CommonVar.UIC_MN_TAG_UC_GET_UPDATE_LOG_INFO);
		cInfo.SetlParam1(iStatus);
		cInfo.SetlParam2(iDetails);
		cInfo.SetString1(getM_strPassedParam());
		
		MenuControlIF.Instance().TriggerForScreen(cInfo);
	}

}
