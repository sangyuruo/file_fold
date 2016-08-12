package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterGetPhoto;

public class UserControl_RequestPosterDelCollection extends UserControl_RequestSNSBase{
	
	private String strPostID = "";

	public UserControl_RequestPosterDelCollection(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendRequestDelFavorite() {
		if ("".equals(strPostID)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/deletefavorites";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("id",strPostID));
		PostData post = new PostData();
		post.setPostData(params);
		setM_strPassParam01(strPostID);
		
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public String getStrPostID() {
		return strPostID;
	}

	public void setStrPostID(String strPostID) {
		this.strPostID = strPostID;
	}

	
}
