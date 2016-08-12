package com.billionav.navi.usercontrol.Request.SNS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;
import com.billionav.navi.usercontrol.UserControl_CommonVar;

public class UserControl_RequestGroupBuildGroup extends UserControl_RequestSNSBase{
	
	private String strGroupName = "";
	private int iGroupType = UserControl_CommonVar.CYCLE_TYPE_PUBLIC;
	private String strGroupDiscription = "";
	private String strGroupTags = "";
	
	public UserControl_RequestGroupBuildGroup(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendBuildGroupRequest() {
			
		if ("".equals(strGroupName)) {
			return false;
		}
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "circle/new";
		List<NameValuePair> params = new ArrayList<NameValuePair>();				
		params.add(new BasicNameValuePair("name",strGroupName));
		params.add(new BasicNameValuePair("type",iGroupType+""));
		params.add(new BasicNameValuePair("description",strGroupDiscription));
		params.add(new BasicNameValuePair("tags",strGroupTags));
		PostData post = new PostData();
		post.setPostData(params);
		SendRequestByPost(sRequestUrl,post,true);
		
		return true;
	}

	public String getStrGroupName() {
		return strGroupName;
	}

	public void setStrGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

	public int getiGroupType() {
		return iGroupType;
	}

	public void setiGroupType(int iGroupType) {
		this.iGroupType = iGroupType;
	}

	public String getStrGroupDiscription() {
		return strGroupDiscription;
	}

	public void setStrGroupDiscription(String strGroupDiscription) {
		this.strGroupDiscription = strGroupDiscription;
	}

	public String getStrGroupTags() {
		return strGroupTags;
	}

	public void setStrGroupTags(String strGroupTags) {
		this.strGroupTags = strGroupTags;
	}
	
}
