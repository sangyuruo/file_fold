package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_PosterQueryInfo;

public class UserControl_RequestPosterQueryPosterList extends UserControl_RequestSNSBase{

	private UserControl_PosterQueryInfo info;
		
	public UserControl_RequestPosterQueryPosterList(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendQueryPosterList() {
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "sns/post/querylist";
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("category",info.getM_strCategory()));
		params.add(new BasicNameValuePair("lt-lonlat",info.getM_strLeftTopLonLat()));
		params.add(new BasicNameValuePair("rb-lonlat",info.getM_strRightBottomLonLat()));
		params.add(new BasicNameValuePair("srchtext",info.getM_strSearchText()));
		params.add(new BasicNameValuePair("offset",info.getM_iOffSet()+ ""));
		params.add(new BasicNameValuePair("maxlen",info.getM_iMaxLen() + ""));
		
		PostData post = new PostData();
		post.setPostData(params);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
	}

	public UserControl_PosterQueryInfo getInfo() {
		return info;
	}

	public void setInfo(UserControl_PosterQueryInfo info) {
		this.info = info;
	}
	
	
}
