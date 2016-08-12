package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;


public class UserControl_RequestPosterReportPos extends UserControl_RequestSNSBase{
	
	String strLon;
	String strLat;
	
	public UserControl_RequestPosterReportPos(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestReportPosition() {
		if ("".equals(strLon) || "".equals(strLat)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/reportpos?lon=" + strLon + "&lat=" + strLat;
		SendRequestByGet(sRequestUrl, true);
		return true;
	}

	public String getStrLon() {
		return strLon;
	}

	public void setStrLon(String strLon) {
		this.strLon = strLon;
	}

	public String getStrLat() {
		return strLat;
	}

	public void setStrLat(String strLat) {
		this.strLat = strLat;
	}
	
	

}
