package com.billionav.navi.net;


public class PASyncRequest extends PRequest {

	public PASyncRequest()
	{
		super(PRequest.REQUEST_TYPE_JAVA);
	}
	
	protected void onRecvData()
	{
		
	}
	
	protected boolean isSync()
	{
		return false;
	}
}
