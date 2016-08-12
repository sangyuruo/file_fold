package com.billionav.navi.net;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.Time;

public class PNetTraceData {
	
	
	public  int m_iRequestID;
	public 	int m_iReqType;
	public int m_iDataSize;
	public int m_iUnzipSize;
	public int m_iStatus;
	public String m_cStartTime;
	public String m_cEndTime;
 
	 
	public PNetTraceData()
	{
		m_iRequestID = 0;
		m_iReqType = 0;
		m_iDataSize = 0;
		m_iUnzipSize = 0;
		m_iStatus = 0;
	}
	 
	public void setRequestID(int requestID)
	{
		m_iRequestID = requestID;
	}
	
	public int getRequestID()
	{
		return m_iRequestID;
	}
	
	public void setReqType(int reqType)
	{
		m_iReqType = reqType;
	}
	
	public int getReqType()
	{
		return m_iReqType;
	}
	
	public void setDataSize(int dataSize)
	{
		m_iDataSize = dataSize;
	}

	public int getDataSize()
	{
		return m_iDataSize;
	}
	
	public void setUnzipSize(int unzipSize)
	{
		m_iUnzipSize = unzipSize;
	}
	
	public int getUnzipSize()
	{
		return m_iUnzipSize;
	}
	
	public void setStartTime()
	{
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");   
        m_cStartTime = formatter.format(new Date());   
	}
	
	public String getStartTime()
	{
		return m_cStartTime;
	}
	
	public void setEndTime()
	{
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");   
        m_cEndTime = formatter.format(new Date());   
	}
	
	public String getEndTime()
	{
		return m_cEndTime;
	}
	 
	public void setStatus(int status)
	{
		m_iStatus = status;
	}
	
	public int getStatus()
	{
		return m_iStatus;
	}
	 
	public void clear()
	{
		m_iRequestID = 0;
		m_iReqType = 0;
		m_iDataSize = 0;
		m_iUnzipSize = 0;
		m_iStatus = 0;
		m_cStartTime = "";
		m_cEndTime = "";
	}
}
