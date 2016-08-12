package com.billionav.navi.download;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import com.billionav.navi.net.PNetLog;




public class DownloadTaskManager extends Thread {
	public enum DownloadDataType {  
		DOWNLOAD_OFFLINE_PACKAGE,
		DOWNLOAD_NDATA,
		DOWNLOAD_SEARCH_DATA     
	};
	
	private static Map<DownloadDataType, DownloadTaskManager> s_mapDownloadTaskManager = new HashMap<DownloadDataType, DownloadTaskManager>();
	
	private static DownloadTaskManager	s_cInstance = null;
	
	private Object m_cSyncObj 			= new Object();
	
	private LinkedList<DownloadRequest>	m_downloadTask	= new LinkedList<DownloadRequest>();
	private DownloadRequest	m_curRequestTask = null;	
	
	public static synchronized DownloadTaskManager instance( DownloadDataType downloadDataType ) {
		DownloadTaskManager downloadTaskManager = s_mapDownloadTaskManager.get( downloadDataType );
		if( null == downloadTaskManager )
		{
			downloadTaskManager = new DownloadTaskManager();
			s_mapDownloadTaskManager.put( downloadDataType, downloadTaskManager );
			downloadTaskManager.start();
		}		
		return downloadTaskManager;
	}
	
	private DownloadTaskManager() {		
	}
	
	public void postDownloadRequest( DownloadRequest downloadRequest ) {
		synchronized(this)
		{
			m_downloadTask.add( downloadRequest );
			notify();
		}
	}
	
	public boolean cancelDownload( DownloadRequest downloadRequest ) {
		synchronized(this)
		{			
			if( null != m_curRequestTask && !m_curRequestTask.checkQuit() && downloadRequest.getDownloadPath().equals( m_curRequestTask.getDownloadPath()))
			{
				m_curRequestTask.stopTask();
				PNetLog.d("DownloadManager cancel succ:"+downloadRequest.getDownloadPath());
				return true;
			}
			
			ListIterator<DownloadRequest> itCurRequest = m_downloadTask.listIterator();;
			while( itCurRequest.hasNext())
			{
				DownloadRequest nextRequest = itCurRequest.next();
				if( downloadRequest.getDownloadPath().equals( nextRequest.getDownloadPath() ) )
				{
					m_downloadTask.remove( nextRequest );
					PNetLog.d("DownloadManager cancel succ:"+downloadRequest.getDownloadPath());
					return true;
				}	
			}
		}
		return false;
	}
	
	@Override
	public void run()
	{
		while(true)
		{ 
			try
			{
				synchronized(this)
				{
					m_curRequestTask = null;
					ListIterator<DownloadRequest> itCurRequest = m_downloadTask.listIterator();;
					while( itCurRequest.hasNext() )
					{
						m_curRequestTask = itCurRequest.next();
						m_downloadTask.remove( m_curRequestTask );
					}
					
					if(null == m_curRequestTask )
					{
						wait();
					}
				}
				if( null != m_curRequestTask )
				{
					m_curRequestTask.DoAction();
				}
			} 
			catch (Exception e)
			{
				PNetLog.e("DownloadManager:"+ e.getMessage());
			}
		}
	}
	
}
