package com.billionav.navi.versioncontrol.Request;

import android.util.Log;

public class VersionControl_RequestGetVersionList extends VersionControl_RequestBase{
	private String strURL = "";
	public VersionControl_RequestGetVersionList(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean GetVersionInfoList(){
		String strURL = getStrURL() + "index.json";//"http://172.26.183.34:80/api/update/1.0/cn/index.json";
		SendRequestByGet(strURL,false);
		Log.d("[VersionControl]", "[VersionControl]: GetVersionInfoList Request strURL =" + strURL);
		return true;
	}

	public String getStrURL() {
		return strURL;
	}

	public void setStrURL(String strURL) {
		this.strURL = strURL;
	}
	
	

}
