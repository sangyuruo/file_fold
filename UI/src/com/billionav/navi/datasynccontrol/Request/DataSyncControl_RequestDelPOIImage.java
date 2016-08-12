package com.billionav.navi.datasynccontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PMultiPart;
import com.billionav.navi.net.PostData;

public class DataSyncControl_RequestDelPOIImage extends DataSyncControl_RequestFileBase{
	private String strPOIurl = "";		//TODO SET THE POIURL
	private String strPOISerUrl = "";
	
	public DataSyncControl_RequestDelPOIImage(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean DelPOIImage(String strUUID, String strRecordKind) {
		String url = strPOISerUrl + strPOIurl;
		
		List<PMultiPart> listData = new ArrayList<PMultiPart>();
		
		PMultiPart cUUIDItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "uuid", strUUID, "text/plain");
		listData.add(cUUIDItem);
		PMultiPart cRecordItem = new PMultiPart(PMultiPart.MULTI_TYPE_STRING, "recordkind", strRecordKind, "text/plain");
		listData.add(cRecordItem);
		
		PostData post = new PostData();
		post.setPostMultiData(listData);

		setM_bAuth(true);
		SendRequestByPostResponseStream(url, post);
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
	
}
