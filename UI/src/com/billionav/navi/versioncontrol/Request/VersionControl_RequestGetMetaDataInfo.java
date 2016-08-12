package com.billionav.navi.versioncontrol.Request;

import android.util.Log;

public class VersionControl_RequestGetMetaDataInfo extends VersionControl_RequestBase{
	String strMetaDataServerURL = "";
	
	public VersionControl_RequestGetMetaDataInfo(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean GetMetaDataInfo(String strFileName) {
		String strURL = strMetaDataServerURL + strFileName;
		SendRequestByGet(strURL,false);
		Log.d("[VersionControl]", "[VersionControl]: GetMetaDataInfo Request strURL =" + strURL);
		return true;
	}

	public String getStrMetaDataServerURL() {
		return strMetaDataServerURL;
	}

	public void setStrMetaDataServerURL(String strMetaDataServerURL) {
		this.strMetaDataServerURL = strMetaDataServerURL;
	}
	
	

}
