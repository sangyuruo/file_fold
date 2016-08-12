package com.billionav.navi.download;

import java.util.LinkedList;
import java.util.ListIterator;

import com.billionav.navi.net.PNetLog;


public class DownloadManager extends Thread {
	
	private static DownloadManager	s_cInstance = null;
	
	private Object m_cSyncObj 			= new Object();
	
	private LinkedList<DownloadTask>	m_lstTask	= null;
	private DownloadTask	m_CurTask = null;
	
	public static synchronized DownloadManager instance() {
		if ( null == s_cInstance ) {
			s_cInstance = new DownloadManager();
			s_cInstance.start();
		}
		return s_cInstance;
	}
	
	private DownloadManager() {
		m_lstTask = new LinkedList<DownloadTask>();
	}
	
	public void postDownloadTask(DownloadTask task) {
		synchronized(this)
		{
			m_lstTask.add(task);
			notify();
		}
	}
	
	public boolean cancelDownload(DownloadTask task) {
		synchronized(this)
		{
			
			if(m_CurTask != null && !m_CurTask.checkQuit() && task.getURL().equals(m_CurTask.getURL()))
			{
				m_CurTask.stopTask();
				PNetLog.d("DownloadManager cancel succ:"+task.getURL());
				return true;
			}
			
			ListIterator<DownloadTask> iter = m_lstTask.listIterator();;
			while (iter.hasNext())
			{
				DownloadTask req = iter.next();
				if(task.getURL().equals(req.getURL()))
				{
					m_lstTask.remove(req);
					PNetLog.d("DownloadManager cancel succ:"+task.getURL());
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
					m_CurTask = null;
					ListIterator<DownloadTask> iter = m_lstTask.listIterator();;
					while (iter.hasNext())
					{
						m_CurTask = iter.next();
						m_lstTask.remove(m_CurTask);
					}
					
					if(null == m_CurTask)
					{
						wait();
					}
				}
				if(null != m_CurTask)
				{
					m_CurTask.DoAction();
				}
			} 
			catch (Exception e)
			{
				PNetLog.e("DownloadManager:"+ e.getMessage());
			}
		}
	}
	
}
