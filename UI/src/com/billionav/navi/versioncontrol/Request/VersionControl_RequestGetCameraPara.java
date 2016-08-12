package com.billionav.navi.versioncontrol.Request;

import android.util.Log;

public class VersionControl_RequestGetCameraPara extends VersionControl_RequestBase{
	public VersionControl_RequestGetCameraPara(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean getCameraInfos(String strURL) {
		SendRequestByGet(strURL,false);
		Log.d("[VersionControl]", "[VersionControl]: GetCameraParaList Request strURL =" + strURL);
		return true;
	}
}
