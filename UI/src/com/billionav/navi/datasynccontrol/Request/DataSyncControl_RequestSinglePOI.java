package com.billionav.navi.datasynccontrol.Request;

import com.billionav.navi.net.PostData;

public class DataSyncControl_RequestSinglePOI  extends DataSyncControl_RequestFileBase{

	private String strPOIurl = "";		//TODO SET THE POIURL
	private String strPOISerUrl = "";
	
	private String strFileFullName = "";
	private String strResFileName = "";
	private String strResFileFullName = "";
	private int iSyncType;
	
	public DataSyncControl_RequestSinglePOI(int iRequestId) {
		super(iRequestId);
	}
	

	public Boolean SendSyncSinglePOIData() {
		if ("".equals(strPOIurl) 
				|| "".equals(strPOISerUrl)
				|| "".equals(strFileFullName)
				|| "".equals(strResFileFullName)) {
			return false;
		}
		String url = strPOISerUrl + strPOIurl;
		PostData postData = new PostData();
		setM_bAuth(true);
		postData.setPostFile(strFileFullName);
		GetResponseBase().setResFName(strResFileFullName);
		
		setM_strFilePath(strFileFullName);
		setM_strResFileName(strResFileName);
		setM_iSyncType(iSyncType);
		
		SendRequestByPostResponseFile(url,postData);
		return true;
	}


	public String getStrPOIurl() {
		return strPOIurl;
	}


	public void setStrPOIurl(String strPOIurl) {
		this.strPOIurl = strPOIurl;
	}


	public String getStrPOISerUrl() {
		return strPOISerUrl;
	}


	public void setStrPOISerUrl(String strPOISerUrl) {
		this.strPOISerUrl = strPOISerUrl;
	}


	public String getStrFileFullName() {
		return strFileFullName;
	}


	public void setStrFileFullName(String strFileFullName) {
		this.strFileFullName = strFileFullName;
	}


	public String getStrResFileName() {
		return strResFileName;
	}


	public void setStrResFileName(String strResFileName) {
		this.strResFileName = strResFileName;
	}


	public String getStrResFileFullName() {
		return strResFileFullName;
	}


	public void setStrResFileFullName(String strResFileFullName) {
		this.strResFileFullName = strResFileFullName;
	}


	public int getiSyncType() {
		return iSyncType;
	}


	public void setiSyncType(int iSyncType) {
		this.iSyncType = iSyncType;
	}
	
	

}
