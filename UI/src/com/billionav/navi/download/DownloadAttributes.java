package com.billionav.navi.download;

import com.billionav.navi.net.PNetLog;


public class DownloadAttributes {	
	private String  m_downloadRootUrl = new String();
	private String  m_saveDirectory = new String();
	private String  m_fileName = new String();	
	private int     m_tryTimes = 1;
	private boolean m_onlyUseWifi = false;
	private boolean m_unzipFile   = true;
	private boolean m_downloadResuming = false;
	private DownloadRequestListener m_downloadRequestListener = null;
		
	public DownloadAttributes() {		
	}	
	
	public String getDownloadPath()	{
		return m_downloadRootUrl + m_fileName;
	}
	
	public void setDownloadRootUrl( String downloadRootUrl )	{
		m_downloadRootUrl = downloadRootUrl;
	}
	
	public String getSavePath()	{
		return m_saveDirectory + m_fileName;
	}
	
	public String getSaveDirectory()	{
		return m_saveDirectory;
	}
	
	public void setSaveDirectory( String saveDirectory )	{
		m_saveDirectory = saveDirectory;
	}
	
	public String getFileName()	{
		return m_fileName;
	}
	
	public void setFileName( String fileName )	{
		m_fileName = fileName;
	}
	
	public int getTryTimes()	{
		return m_tryTimes;
	}
	
	public void setTryTimes( int tryTimes )	{
		m_tryTimes = tryTimes;
	}
	
	public boolean getOnlyUseWifi()	{
		return m_onlyUseWifi;
	}
	
	public void setOnlyUseWifi( boolean onlyUseWifi )	{
		m_onlyUseWifi = onlyUseWifi;
	}
	
	public boolean isUnzipFile()	{
		return m_unzipFile;
	}
	
	public void setUnzipFile( boolean unzipFile )	{
		m_unzipFile = unzipFile;
	}	
	
	public boolean isDownloadResuming()	{
		return m_downloadResuming;
	}
	
	public void setDownloadResuming( boolean downloadResuming )	{
		m_downloadResuming = downloadResuming;
	}	
	
	public DownloadRequestListener getDownloadRequestListener()	{
		return m_downloadRequestListener;
	}
	
	public void setDownloadRequestListener( DownloadRequestListener downloadRequestListener )	{
		m_downloadRequestListener = downloadRequestListener;
	}
}
