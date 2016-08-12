package com.billionav.navi.usercontrol.Request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.billionav.navi.net.PostData;

public class UserControl_RequestInviteUserIntoCircle extends UserControl_RequestBase{
	
	public UserControl_RequestInviteUserIntoCircle(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean AskUserIntoCircle(String circleID,String userID ,String email,String cellphone){
		
		String sRequestUrl = GetAuthServerUrl();
		sRequestUrl += "circle/inviteuser";
		StringBuffer sbuf=new StringBuffer();
		String content="";
		sbuf.append("<invite circle-id=\"");
		sbuf.append(circleID);
		sbuf.append("\">");
		if(0!=userID.compareTo(""))
		{
			sbuf.append("<user id=\"");
			sbuf.append(userID);
			sbuf.append("\"/>");
		}
		if(0!=email.compareTo(""))
		{
			sbuf.append("<user email=\"");
			sbuf.append(email);
			sbuf.append("\"/>");
		}
		if(0!=cellphone.compareTo(""))
		{
			sbuf.append("<user cellphone=\"");
			sbuf.append(cellphone);
			sbuf.append("\"/>");
		}
		sbuf.append("</invite>");
		content=sbuf.toString();		
		List<NameValuePair> params = new ArrayList<NameValuePair>();	
		params.add(new BasicNameValuePair("content",content));	
		PostData post = new PostData();
		post.setPostData(params);
		SendRequestByPost(sRequestUrl,post,true);
		return true;
		
	}

	
}
