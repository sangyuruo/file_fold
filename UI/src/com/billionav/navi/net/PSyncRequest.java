package com.billionav.navi.net;

public class PSyncRequest extends PRequest{
	
	public PSyncRequest()
	{
		super(PRequest.REQUEST_TYPE_JAVA);
	}
	
	protected void onRecvData()
	{
		notifyResponse();
	} 
	
	public void waitResponse()
	{
		synchronized(m_cObj)
		{
			try
			{
				m_cObj.wait();
			} 
			catch(Exception e)
			{
				PNetLog.e("PSyncRequest wait exception"+e.getMessage());
			}
		}
	}
	
	protected void notifyResponse()
	{
		PNetLog.d("PSyncRequest nofity:"+this.getRequestId());
		synchronized(m_cObj)
		{
			m_cObj.notify();
		}
	}
	
	protected boolean isSync()
	{
		return true;
	}
	
	private Object m_cObj = new Object();
}
