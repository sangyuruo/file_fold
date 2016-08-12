package com.billionav.navi.versioncontrol.Response;

import org.json.JSONObject;

import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.versioncontrol.VersionControl_CommonVar;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;

public class VersionControl_ResponseGetSrchTopDataVersion  extends VersionControl_ResponseBase{
	private int iStatus = UC_RESPONES_SUC;
	private int iDetails = 0;
	public static final String TAG = "srch";
	
	private static String TOP_VERSION_KEY = "srch_top_data_version";
	private static String MIN_VERSION_KEY = "srch_min_data_version";
	
	public VersionControl_ResponseGetSrchTopDataVersion(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			try{
				byte[] receiveData = getReceivebuf();
				String strData = new String(receiveData);
				JSONObject jObject;
				jObject = new JSONObject(strData);
				String strTopVersion = "";
				String strMinVersion = "";
				if (jObject.has(TOP_VERSION_KEY)) {
					strTopVersion = jObject.getString(TOP_VERSION_KEY);
				}
				if (jObject.has(MIN_VERSION_KEY)) {
					strMinVersion = jObject.getString(MIN_VERSION_KEY);
				}
				VersionControl_ManagerIF.Instance().setM_strSrchMinDataVersion(strMinVersion);
				VersionControl_ManagerIF.Instance().setM_strSrchTopDataVersion(strTopVersion);
				Log.d("[VersionControl]", "[VersionControl]: GetSrchTopDataVersion strTopVersion : " + strTopVersion + "  strMinVersion: " + strMinVersion);
			}catch(Exception ex) {
				iStatus =  UC_RESPONES_LOC_FAIL;
				iDetails = UC_DETAILS_JSON_PRASE_ERROR;
				Log.d("[VersionControl]", "[VersionControl]: GetSrchTopDataVersion Response Exception");
			}
		} else {
			iStatus = UC_RESPONES_SRV_FAIL;
			iDetails = iResCode;
		}
		
		Log.d("[VersionControl]", "[VersionControl]: GetSrchTopDataVersion Response iStatus = " + iStatus + " iDetails=" +iDetails);
		NSTriggerInfo cInfo = new NSTriggerInfo();
		cInfo.SetTriggerID(VersionControl_CommonVar.UIC_MN_TAG_UC_GET_TOP_DATA_VERSION);
		cInfo.SetlParam1(iStatus);
		cInfo.SetlParam2(iDetails);
		cInfo.SetString1(TAG);
		MenuControlIF.Instance().TriggerForScreen(cInfo);
	}
}
