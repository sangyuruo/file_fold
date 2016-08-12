package com.billionav.navi.sysMsgControl.Request;

import com.billionav.navi.net.PostData;

public class SysMsgControl_RequestGetSysMsg extends SysMsgControl_RequestBase{

	public SysMsgControl_RequestGetSysMsg(int iRequestId) {
		super(iRequestId);
	}

	public boolean sendSysMsgRequest(){
		String sRequestUrl = GetServerUrl();
		sRequestUrl += "CloudManagementAPI/GetSystemMessage";
//		sRequestUrl = "http://172.26.181.164:8080/CloudManagementAPI/GetSystemMessage";
		PostData post = new PostData();
		SendRequestByPost(sRequestUrl,post,false);
		
		return false;
	}
}
