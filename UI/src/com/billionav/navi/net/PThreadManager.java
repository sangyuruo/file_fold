package com.billionav.navi.net;

import java.util.LinkedList;
import java.util.ListIterator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.billionav.jni.NetJNI;
import com.billionav.navi.app.AndroidNaviAPP;


 
public class PThreadManager extends Thread {
	


	private static PThreadManager 		s_cInstance = null;

	private boolean s_bNetConnect 		= false;
	
	private static int		 			MAX_THREAD_NUM 			= 1;
	private int 						HIGHEST_THREAD_NUM 		= 0;
	private int 						HIGH_THREAD_NUM 		= 0;
	private int 						NORAML_THREAD_NUM 		= 0;
	private int 						LOW_THREAD_NUM 			= 0;
	private int 						LOWER_THREAD_NUM 		= 0;
	private int 						LOWEST_THREAD_NUM 		= 0;
	
	private int							m_idelThreads			= 0;
	private int							m_iMaxThreads			= 0;
	private LinkedList<PThread>			m_IdleThreadPool		= null;
	private LinkedList<PThread>			m_ThreadPool			= null;
	 
	private LinkedList<PRequest>		m_PRequestHighest	= null;
	private LinkedList<PRequest>		m_PRequestHigh		= null;
	private LinkedList<PRequest>		m_PRequest			= null;
	private LinkedList<PRequest>		m_PRequestLow		= null;
	private LinkedList<PRequest>		m_PRequestLower		= null;
	private LinkedList<PRequest>		m_PRequestLowest		= null;
	 
	private boolean bQuit = false; 
	private Object m_cWaitObj		= new Object();

	private Object m_cAuthSyncObj		= new Object();
	
	public static synchronized PThreadManager instance() {
		if ( null == s_cInstance ) {
			s_cInstance = new PThreadManager(MAX_THREAD_NUM);
			s_cInstance.start();
		}
		return s_cInstance;
	}	 
	 
	private PThreadManager(int iMaxThreads) {
		
		this.setName("PNetThreadManager");
		
		m_iMaxThreads 		= iMaxThreads;
		m_IdleThreadPool		= new LinkedList<PThread>();
		m_ThreadPool		= new LinkedList<PThread>();
		
		createThreads();
		m_PRequest = new LinkedList<PRequest>();
		m_PRequestHigh = new LinkedList<PRequest>();
		m_PRequestHighest = new LinkedList<PRequest>();
		m_PRequestLow = new LinkedList<PRequest>();
		m_PRequestLower = new LinkedList<PRequest>();
		m_PRequestLowest = new LinkedList<PRequest>();
		
		/*
		Context context = AndroidNaviAPP.getInstance().getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE ); 
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo(); 
		if ( activeNetInfo != null ) 
		{ 
			PNetLog.e("Active Network Type : " + activeNetInfo.getTypeName()+" "+activeNetInfo.isConnected()); 
			if(activeNetInfo.getTypeName().equalsIgnoreCase("WIFI"))
			{
				PConnectReceiver.setConnectType(PConnectReceiver.CONNECT_TYPE_WIFI);
			} 
			else 
			{
				PConnectReceiver.setConnectType(PConnectReceiver.CONNECT_TYPE_MOBILE);
			}
		} else {
			PConnectReceiver.setConnectType(PConnectReceiver.CONNECT_TYPE_NONE);
			PNetLog.e("Active Network Type : NULL");
		}
		*/
	}
	
	private void createThreads()
	{
		for(int i =1; i<= m_iMaxThreads; i++)
		{
			PThread cPThread = new PThread(i);
			cPThread.start();
			m_ThreadPool.add(cPThread);
		}
	}
	
	public void stopThread()
	{
			
			for(int i = 0; i < m_ThreadPool.size(); i++)
			{
				m_ThreadPool.get(i).cancelRequest();
			}
			PNetLog.d("All net requset has abort");
			try
			{
				synchronized(m_cWaitObj)
				{
					synchronized(this)
					{
						bQuit = true;
						notify();
					}
					PNetLog.e("PThreadManager quit wait");
					m_cWaitObj.wait();
				}

				PNetLog.d("PThreadManager Thread Stoped (2)");
			} 
			catch (Exception e)
			{
				PNetLog.e("PThreadManager Thread Stoped Error Message:"+e.getMessage());
			}
			
			for(int i = 0; i < m_IdleThreadPool.size(); i++)
			{
				m_IdleThreadPool.get(i).stopThread();
			}
			
			PNetLog.d("PNet Threads Stoped (3)");
			
			cancelRequest(m_PRequestHighest);
			cancelRequest(m_PRequestHigh);
			cancelRequest(m_PRequest);
			cancelRequest(m_PRequestLow);
			cancelRequest(m_PRequestLower);
			cancelRequest(m_PRequestLowest);
			
			PNetLog.d("PNet requset Stoped (4)");
			
			PHttpClient.getInstance().getHttpClient().getConnectionManager().shutdown();
			

	}
	      
	public void addIdleThread(PThread cPthread)
	{
		synchronized(this)
		{
			m_idelThreads++;
			m_IdleThreadPool.add(cPthread);
			notify();
		}
	}
	
	private PThread getIdleThread()
	{	
		PThread cPthread = null;
		if(m_IdleThreadPool.size()>0)
		{
			cPthread = m_IdleThreadPool.getFirst();
			m_IdleThreadPool.removeFirst();
			m_idelThreads--;
			PNetLog.d("getIdleThread() return:IdleThreadID:"+ cPthread.getThreadID()+ " idleThreadNum:"+m_IdleThreadPool.size());
		}
		return cPthread;
	}
	
	public void PostRequest(PRequest cPRequest) 
	{	
		int priority = cPRequest.getPriority();;
		synchronized(this)
		{	
			if(true == bQuit)
			{
				cPRequest.setReceivebuf(null);
				cPRequest.setResCode(PResponse.RES_CODE_CANCEL);
				cPRequest.onFinish(0);
				return;
			}
			
			switch(priority)
			{
				case PRequest.REQ_PRIOR_HIGHEST:
					m_PRequestHighest.add(cPRequest);
					break;
				case PRequest.REQ_PRIOR_HIGH:
					m_PRequestHigh.add(cPRequest);	
					break;
				case PRequest.REQ_PRIOR_NORMAL:
					m_PRequest.add(cPRequest);
					break;
				case PRequest.REQ_PRIOR_LOW:
					m_PRequestLow.add(cPRequest);
					break;
				case PRequest.REQ_PRIOR_LOWER:
					m_PRequestLower.add(cPRequest);
					break;	
				case PRequest.REQ_PRIOR_LOWEST:
					m_PRequestLowest.add(cPRequest);
					break;
				default:
					PNetLog.e("PThreadManager add Request +ID failed:"+ cPRequest.getRequestId());
					break;
			}

			notify();
		}
	}
	
	public void PushRequest(PRequest cPRequest)
	{
		int priority = cPRequest.getPriority();;
		synchronized(this)
		{	
			switch(priority)
			{
				case PRequest.REQ_PRIOR_HIGHEST:
					m_PRequestHighest.addFirst(cPRequest);
					break;
				case PRequest.REQ_PRIOR_HIGH:
					m_PRequestHigh.addFirst(cPRequest);	
					break;
				case PRequest.REQ_PRIOR_NORMAL:
					m_PRequest.addFirst(cPRequest);
					break;
				case PRequest.REQ_PRIOR_LOW:
					m_PRequestLow.addFirst(cPRequest);
					break;
				case PRequest.REQ_PRIOR_LOWER:
					m_PRequestLower.addFirst(cPRequest);
					break;	
				case PRequest.REQ_PRIOR_LOWEST:
					m_PRequestLowest.addFirst(cPRequest);
					break;
				default:
					PNetLog.e("PThreadManager Push Request +ID failed:"+ cPRequest.getRequestId());
					break;
			}

			notify();
		}
	}
	
	protected PRequest getRequestFromList(LinkedList<PRequest> list)
	{
		PRequest cPRequest = null;

		if(list.size() > 0)
		{
			for(int i = 0;i<list.size();i++)
			{
				cPRequest = list.get(i);
				list.remove(i);
				return cPRequest;
				/*
				if(false == cPRequest.getAuthFlag())
				{
					list.remove(i);
					return cPRequest;
				// need auth
				} else {
					switch(PLogin.getLoginStatus())
					{
					case PLogin.PLOGIN_STATUS_SUCC:
						list.remove(i);
						return cPRequest;
					case PLogin.PLOGIN_STATUS_NO:
					case PLogin.PLOGIN_STATUS_ING:
					case PLogin.PLOGIN_STATUS_FAILED:
					case PLogin.PLOGIN_STATUS_OVERDUE:
						continue;
					default:
						PNetLog.e("PThreadManager check Auth status failed");
						break;
					}
				}
				*/
			}
		}

		return cPRequest;
	}
	
 	protected PRequest getRequest()
	{
		int idleThreadCnt = m_IdleThreadPool.size();

		PRequest cPRequest = null;
		
		if(idleThreadCnt == 0)
		{
			return cPRequest;
		}
		
		if(HIGHEST_THREAD_NUM < idleThreadCnt)
		{
			cPRequest = getRequestFromList(m_PRequestHighest);
			if(null != cPRequest)
			{
				PNetLog.d("PThreadManager get request from Highest:"+idleThreadCnt);
				return cPRequest;
			}
		}
		
		if(HIGH_THREAD_NUM < idleThreadCnt)
		{
			cPRequest = getRequestFromList(m_PRequestHigh);
			if(null != cPRequest)
			{
				PNetLog.d("PThreadManager get request from High:"+idleThreadCnt);
				return cPRequest;
			}
		}
		 
		if(NORAML_THREAD_NUM < idleThreadCnt)
		{
			cPRequest = getRequestFromList(m_PRequest);
			if(null != cPRequest)
			{
				PNetLog.d("PThreadManager get request from Normal:"+idleThreadCnt);
				return cPRequest;
			}
		}
		  
		if(LOW_THREAD_NUM < idleThreadCnt)
		{
			cPRequest = getRequestFromList(m_PRequestLow);
			if(null != cPRequest)
			{
				PNetLog.d("PThreadManager get request from Low:"+idleThreadCnt);
				return cPRequest;
			}
		}

		if(LOWER_THREAD_NUM < idleThreadCnt)
		{
			cPRequest = getRequestFromList(m_PRequestLower);
			if(null != cPRequest)
			{
				PNetLog.d("PThreadManager get request from Lower:"+idleThreadCnt);
				return cPRequest;
			}
		}
		 
		if(LOWEST_THREAD_NUM < idleThreadCnt)
		{
			cPRequest = getRequestFromList(m_PRequestLowest);
			if(null != cPRequest)
			{
				PNetLog.d("PThreadManager get request from Lowest:"+idleThreadCnt);
				return cPRequest;
			}
		}
			
		return cPRequest;
	}
	
	protected void cancelRequest(LinkedList<PRequest> list)
	{
		for(int i = 0;i<list.size();i++)
		{
			PRequest cPRequest = list.get(i);
			cPRequest.setReceivebuf(null);
			cPRequest.setResCode(PResponse.RES_CODE_CANCEL);
			cPRequest.onFinish(0);
		}
	}
 	
 	
	@Override
	public void run()
	{
		this.setPriority(4);
		//this.setName("PNetThreadManager");
		while(true)
		{ 
				try
				{
					PRequest cPRequest = null;
					synchronized(this)
					{
						if(true == bQuit) {
							if(m_iMaxThreads == m_IdleThreadPool.size())
							{
								synchronized(m_cWaitObj)
								{
									m_cWaitObj.notify();
									break;
								}
							}
							
						} else {
							cPRequest = getRequest();
							if(cPRequest == null)
							{
								PNetLog.d("PThreadManager wait...");
								wait();
								PNetLog.d("PThreadManager Run = Request Size:"+ m_PRequestHighest.size()
										+ " " + m_PRequestHigh.size()
										+ " " + m_PRequest.size()
										+ " " + m_PRequestLow.size()
										+ " " + m_PRequestLower.size()
										+ " " + m_PRequestLowest.size()
										+ " IDThreadSize:"+m_IdleThreadPool.size());
		 					}
							else {
								PNetLog.d("PThreadManager notify... ID:" + cPRequest.getRequestId());
								getIdleThread().notify(cPRequest);
								cPRequest = null;
							}
						}
					}
					
				} 
				catch (Exception e)
				{
					PNetLog.e("PThreadManager:"+ e.getMessage());
				}
			}
	}
}
