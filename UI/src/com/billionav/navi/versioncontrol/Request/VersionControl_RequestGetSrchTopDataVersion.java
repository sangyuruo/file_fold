package com.billionav.navi.versioncontrol.Request;

import android.util.Log;

public class VersionControl_RequestGetSrchTopDataVersion extends VersionControl_RequestBase{
	private String strDataFormatVer;
	private String strDataServerURL;
	private static String VERSION_PREFIX = "F";
	private static String VERSION_SUFFIX = ".json";
	
	public VersionControl_RequestGetSrchTopDataVersion(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean GetSrchTopDataVersion(String DataFormatVer) {
		String strURL = strDataServerURL + VERSION_PREFIX + DataFormatVer + VERSION_SUFFIX;
		SendRequestByGet(strURL,false);
		Log.d("[VersionControl]", "[VersionControl]: GetSrchTopDataVersion Request strURL =" + strURL);
		return true;
	}


	public String getStrDataFormatVer() {
		return strDataFormatVer;
	}
	public void setStrDataFormatVer(String strDataFormatVer) {
		this.strDataFormatVer = strDataFormatVer;
	}
	public String getStrDataServerURL() {
		return strDataServerURL;
	}

	public void setStrDataServerURL(String strDataServerURL) {
		this.strDataServerURL = strDataServerURL;
	}
	
}
