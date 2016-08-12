package com.billionav.navi.net;

import com.billionav.jni.NetJNI;

import android.os.Process;

public class PThread extends Thread {
	
	private int m_iThreadID;
	private boolean bQuit = false;
	private boolean bNetTrace = true;
	private PRequest m_cPRequest = null;
	
	public PThread(int id)
	{
		m_iThreadID = id;
		this.setName("PNetThread:"+id);
	}
	
	public int getThreadID()
	{
		return m_iThreadID;
	}
	
	public void stopThread()
	{
		synchronized(this)
		{
			bQuit = true;

			notify();
		}
	}
	
	public void cancelRequest()
	{
		synchronized(this)
		{
			if(null != m_cPRequest)
			{
				m_cPRequest.cancelRequest();
			}
		}
	}
	
	
	@Override
	public void run() 
	{
		//int pri = getPriority();
		this.setPriority(4);
		//this.setName("PNetThread");
		while(true)
		{
				try {
					
					synchronized(this)
					{
						PThreadManager.instance().addIdleThread(this);
						PNetLog.d("PThread id:"+m_iThreadID+" wait.");
						wait();
						if(true == bQuit)
						{
							if(null != m_cPRequest)
							{
								m_cPRequest.setReceivebuf(null);
								m_cPRequest.setResCode(PResponse.RES_CODE_CANCEL);
							}
							break;
						}
					}

					PNetLog.d("PThread id:"+m_iThreadID+" Run.");
					if(null != m_cPRequest)
					{
						if(PConnectReceiver.getConnectType() == PConnectReceiver.CONNECT_TYPE_NONE)
						{
							PNetLog.e("PThread id:"+m_iThreadID+" Net is Closed");
							m_cPRequest.setReceivebuf(null);
							m_cPRequest.setResCode(PResponse.RES_CODE_CONNECT_EXCEPTION);
							m_cPRequest.onFinish(m_iThreadID);
						} 
						else 
						{
							m_cPRequest.execute(m_iThreadID);
							m_cPRequest.onFinish(m_iThreadID);	
							if((true == bNetTrace) && (PResponse.RES_CODE_CANCEL != m_cPRequest.getResCode()))
							{
								NetJNI.setNativeLog(m_cPRequest.getNetTraceInfo1());
								NetJNI.setNativeLog(m_cPRequest.getNetTraceInfo2());
							}
						}
					}
				}
				catch (Exception e)
				{
					PNetLog.e("PThread id:"+m_iThreadID+" Exception:"+ e.getMessage());
					e.printStackTrace();
					
					m_cPRequest.setReceivebuf(null);
					m_cPRequest.setResCode(PResponse.RES_CODE_CONNECT_EXCEPTION);
					m_cPRequest.onFinish(m_iThreadID);

					if(true == bNetTrace && (PResponse.RES_CODE_CANCEL != m_cPRequest.getResCode()))
					{
						NetJNI.setNativeLog(m_cPRequest.getNetTraceInfo1());
						NetJNI.setNativeLog(m_cPRequest.getNetTraceInfo2());
					}
				} 
				finally
				{
					synchronized(this)
					{
						m_cPRequest = null;
					}
				}
		}
	}
	
	public void notify(PRequest cPRequset)
	{
		synchronized(this)
		{
			m_cPRequest = cPRequset;
			notify();
		}
	}

}
