package com.billionav.navi.versioncontrol.Request;

import android.util.Log;

public class VersionControl_RequestGetUpdateLogInfo extends VersionControl_RequestBase{
	private String strURL = "";
	String strIndentifyKey = "";
	
	public VersionControl_RequestGetUpdateLogInfo(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean getUpdateLogInfo(String LogInfoFileName) {
		setM_strPassedParam(strIndentifyKey);
		String strURL = getStrURL() + LogInfoFileName;//"http://172.26.183.34:80/api/update/1.0/cn/index.json";
		SendRequestByGet(strURL,false);
		Log.d("[VersionControl]", "[VersionControl]: getUpdateLogInfo Request strURL =" + strURL + "Indentify: "+ strIndentifyKey);
		return true;
	}

	public String getStrIndentifyKey() {
		return strIndentifyKey;
	}

	public void setStrIndentifyKey(String strIndentifyKey) {
		this.strIndentifyKey = strIndentifyKey;
	}

	public String getStrURL() {
		return strURL;
	}

	public void setStrURL(String strURL) {
		this.strURL = strURL;
	}
	
	
	
}
