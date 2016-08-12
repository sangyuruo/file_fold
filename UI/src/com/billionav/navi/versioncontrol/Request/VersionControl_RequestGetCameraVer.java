package com.billionav.navi.versioncontrol.Request;

import android.util.Log;

public class VersionControl_RequestGetCameraVer extends VersionControl_RequestBase{
	public VersionControl_RequestGetCameraVer(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean getCameraInfos(String strURL) {
		SendRequestByGet(strURL,false);
		Log.d("[VersionControl]", "[VersionControl]: RequestGetCameraVer Request strURL =" + strURL);
		return true;
	}
}
