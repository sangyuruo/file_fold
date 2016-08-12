package com.billionav.navi.datasynccontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.datasynccontrol.DataSyncControl_CommonVar;
import com.billionav.navi.net.PostData;

public class DataSyncControl_RequestRoute extends DataSyncControl_RequestBase{
	String url_getpath = "";
	String strDeviceno = "";
	
	public DataSyncControl_RequestRoute(int iRequestId) {
		super(iRequestId);
		setUrl_getpath("");//TODO
	}
	
	public boolean DataSyncControl_GetWebPath() {
		String url = GetServerUrl() + getUrl_getpath();
		
		if ("".equals(url_getpath) || "".equals(strDeviceno)) {
			return false;
		}
		PostData postData = new PostData();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("deviceno",strDeviceno));
		params.add(new BasicNameValuePair("query",DataSyncControl_CommonVar.SYNC_PATH_MEHTOD_GETPATH));//Y means Query , N means get
		setM_StrPassParam01(DataSyncControl_CommonVar.SYNC_PATH_MEHTOD_GETPATH);
		postData.setPostData(params);
		setM_bAuth(true);
		SendRequestByPostResponseStream(url, postData);
		return true;
	}
	
	public boolean GetTheWebRouteStatus() {
		String url = GetServerUrl() + getUrl_getpath();
		PostData postData = new PostData();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("deviceno",strDeviceno));
		params.add(new BasicNameValuePair("query",DataSyncControl_CommonVar.SYNC_PATH_MEHTOD_GETSTATUS));//Y means Query , N means get
		setM_StrPassParam01(DataSyncControl_CommonVar.SYNC_PATH_MEHTOD_GETSTATUS);
		postData.setPostData(params);
		setM_bAuth(true);
		SendRequestByPostResponseStream(url, postData);
		return true;
	}
	
	

	public String getStrDeviceno() {
		return strDeviceno;
	}

	public void setStrDeviceno(String strDeviceno) {
		this.strDeviceno = strDeviceno;
	}

	public String getUrl_getpath() {
		return url_getpath;
	}

	public void setUrl_getpath(String url_getpath) {
		this.url_getpath = url_getpath;
	}
	
	
	
}