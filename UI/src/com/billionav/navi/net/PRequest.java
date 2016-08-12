package com.billionav.navi.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpStatus;

import android.util.Log;

import com.billionav.jni.NetJNI;

public class PRequest {
	public static final int METHODS_INVALID = 0;	// INVALID
	public static final int METHODS_HEAD = 1;	// HEAD
	public static final int METHODS_GET  = 2;	// Get 
	public static final int METHODS_POST = 3;	// Post
	public static final int METHODS_CANCEL = 4;	// cancel

	public static final int REQ_PRIOR_HIGHEST = 0;
	public static final int REQ_PRIOR_HIGH = 1;
	public static final int REQ_PRIOR_NORMAL = 2;
	public static final int REQ_PRIOR_LOW = 3;
	public static final int REQ_PRIOR_LOWER = 4;
	public static final int REQ_PRIOR_LOWEST = 5;
	
	public static final int RESPONSE_DATA_NODATA = 0;
	public static final int RESPONSE_DATA_BUF  = 1;
	public static final int RESPONSE_DATA_FILE = 2;
	
	protected static final int REQUEST_TYPE_JAVA = 0;
	protected static final int REQUEST_TYPE_NATIVE = 1;
	
	protected static final int REQUEST_STATUS_INIT = 0;
	protected static final int REQUEST_STATUS_EXEC = 1;
	protected static final int REQUEST_STATUS_CANCEL = 2;
	protected static final int REQUEST_STATUS_ABORT = 3;
	
	public 	int m_iRequestID;
	public 	int m_iReqType;										// 0 java request 1 native requset
	private int m_iMethod		= METHODS_INVALID;				// http method
	
	private int m_iResDataType	= RESPONSE_DATA_BUF;			// response data type
	public 	int m_iResCode		= PResponse.RES_CODE_NO_ERROR;	// response code
	private byte[] m_bRecvbuf 	= null;
	private int m_iRecvbufSize 	= 0;
	private int m_iCanelStatus 	= REQUEST_STATUS_INIT;
	private int m_iPriority = REQ_PRIOR_NORMAL;
	private boolean m_bAuth = false;
	private static int s_iRequestId = 0;
	private PostData m_cPostData = null;		 	// post data
	private HttpRequestBase m_cRequest = null;  	// for abort
	private PRequestListener m_cListener = null;
	private Vector<String> m_vHttpHeader = new Vector<String>();
	 
	public String m_cStartTime;
	public String m_cEndTime;
	public String m_cReceiveTime;
	public String m_cCallBackTime;
	public int m_iZipSize = 0;
	public int m_iUnzipSize = 0;
	public int m_iDiffTime = 0;
	public long m_lStartTime = 0;
	public int m_iDiffTime1 = 0;
	public long m_lEndTime = 0;
	public int m_iDiffTime2 = 0;
	public long m_lReceiveTime = 0;


	
	public String m_cUrl = new String();
	private String m_sFName = new String();
	private String m_sRecvContentType = new String();
	
	/**
	 * construct
	 * @param int reqType 0 java request 1 native requset
	 */
	public PRequest(int reqType)
	{
		m_iReqType = reqType;
		
		if(m_iReqType == REQUEST_TYPE_NATIVE)
		{
			m_iRequestID = 0;
		} 
		else 
		{
			//AtomicInteger;
			m_iRequestID = ++s_iRequestId;
		}
	
	}
	
	/**
	 * construct of http get request
	 * @param int 		reqType 	0 java request 1 native requset
	 * @param String	url					
	 */
	public PRequest(int reqType, String url)
	{
		m_iReqType = reqType;
		
		if(m_iReqType == REQUEST_TYPE_NATIVE)
		{
			m_iRequestID = 0;
		} 
		else 
		{
			//AtomicInteger;
			m_iRequestID = ++s_iRequestId;
		}
		
		m_cUrl = url;
	
		m_iMethod = METHODS_GET;
		
		m_iResDataType = RESPONSE_DATA_BUF;
		
	}
	
	/**
	 * construct of http get request
	 * @param int 		reqType 	0 java request 1 native requset
	 * @param String	url
	 * @param String	filename	name of the file which receive data					
	 */
	public PRequest(int reqType, String url, String filename)
	{
		m_iReqType = reqType;
		
		if(m_iReqType == REQUEST_TYPE_NATIVE)
		{
			m_iRequestID = 0;
		} 
		else 
		{
			//AtomicInteger;
			m_iRequestID = ++s_iRequestId;
		}
		
		m_cUrl = url;
	
		m_iMethod = METHODS_GET;
		
		m_iResDataType = RESPONSE_DATA_FILE;
		
		m_sFName = filename;

	}

	/**
	 * construct of http post request
	 * @param int 		reqType 	0 java request 1 native requset
	 * @param String	url
	 * @param PostData	postData	data of upload					
	 */
	public PRequest(int reqType, String url, PostData postData)
	{
		m_iReqType = reqType;
		
		if(m_iReqType == REQUEST_TYPE_NATIVE)
		{
			m_iRequestID = 0;
		} 
		else 
		{
			//AtomicInteger;
			m_iRequestID = ++s_iRequestId;
		}
		
		m_cUrl = url;
	
		m_iMethod = METHODS_POST;
		
		m_iResDataType = RESPONSE_DATA_BUF;
		
		m_cPostData = postData;
		
	}
	
	/**
	 * Get the request type
	 * @param 
	 * @return int 0 java request 1 native requset					
	 */
	public int getReqType()
	{
		return m_iReqType;
	}

	/**
	 * Set priority of request
	 * @param int request
	 * @return 				
	 */
	public void setPriority(int priority)
	{
		m_iPriority = priority;
	}
	
	/**
	 * Get priority of request
	 * @param 
	 * @return int  				
	 */
	public int getPriority()
	{
		return m_iPriority;
	}
	
	public void setRequestId(int id)
	{
		m_iRequestID = id;
	}
	
	/**
	 * Get request id of request
	 * @param 
	 * @return int  request id			
	 */
	public int getRequestId()
	{
		return m_iRequestID;
	}
	
	/**
	 * Set method of request
	 * @param int method 
	 * 	 	METHODS_HEAD = 1;	// HEAD
	 *  	METHODS_GET  = 2;	// Get 
	 *		METHODS_POST = 3;	// Post
	 * @return 			
	 */
	public void setMethod(int iMethod)
	{
		m_iMethod = iMethod;
	}
	
	/**
	 * Get method of request
	 * @param 
	 * @return int   the method of request			
	 */
	public int getMethod()
	{
		return m_iMethod;
	}
	
	public void setListener(PRequestListener listener)
	{
		m_cListener = listener;
	}
	
	public void setPostData(PostData data)
	{
		m_cPostData = data;
	}
	
	public PostData getPostData()
	{
		return m_cPostData;
	}
	
	public void setURL(String sUrl)
	{
		m_cUrl = makeAbsUrl(sUrl);
	}
	
	public String getURL()
	{
		return m_cUrl;
	}
	
	public void addHttpHeader(String headname, String headvalue)
	{
		m_vHttpHeader.add(headname);
		m_vHttpHeader.add(headvalue);
	}
	
	public void setResDataType(int iResDataType)
	{
		m_iResDataType = iResDataType;
	}
	
	public int getResDataType()
	{
		return m_iResDataType;
	}
	
	public void setResFName(String sFName)
	{
		m_iResDataType = RESPONSE_DATA_FILE;
		m_sFName = sFName;
	}
	
	public String getResFName()
	{
		return m_sFName;
	}
	
	public void setResCode(int iResCode)
	{
		m_iResCode = iResCode;
	}
	
	public int getResCode()
	{
		return m_iResCode;
	}
	
	public void setReceivebuf(byte[] bData)
	{
		m_bRecvbuf = bData;
		if(null == bData)
		{
			m_iRecvbufSize = 0;
		} else {
			m_iRecvbufSize = bData.length;
		}
	}
	
	public byte[] getReceivebuf()
	{
		return m_bRecvbuf;
	}
	
	
	public void setRecvContentType(String type)
	{
		m_sRecvContentType = type;
	}
	
	public String getRecvContentType()
	{
		return m_sRecvContentType;
	}
	
	public int getReceivebufSize()
	{
		return m_iRecvbufSize;
	}
	
	public void setZipSize(int Size)
	{
		m_iZipSize = Size ;
	}
	
	public int getZipSize()
	{
		return m_iZipSize;
	}
	
	public void setUnzipSize(int Size)
	{
		m_iUnzipSize = Size ;
	}
	
	public int getUnzipSize()
	{
		return m_iUnzipSize;
	}
	
	public void setStartTime()
	{
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        Date date = new Date();
        m_lReceiveTime = m_lEndTime = m_lStartTime = date.getTime();
        m_cStartTime = formatter.format(date);
	}
	
	/*
	public String getStartTime()
	{
		return m_cStartTime;
	}
	*/
	
	public void setEndTime()
	{
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        Date date = new Date();
        m_lReceiveTime = m_lEndTime = date.getTime();
        m_iDiffTime = (int)(m_lEndTime - m_lStartTime);
        m_cEndTime = formatter.format(date);   
	}

	/*
	public String getEndTime()
	{
		return m_cEndTime;
	}
	*/

	public void setReceiveTime()
	{
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        Date date = new Date();
        m_lReceiveTime = date.getTime();
        m_iDiffTime1 = (int)(m_lReceiveTime - m_lEndTime);
        m_cReceiveTime = formatter.format(date);   
	}

	
	public void setCallBackTime()
	{
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        Date date = new Date();
        m_iDiffTime2 = (int)(date.getTime() - m_lReceiveTime);
        m_cCallBackTime = formatter.format(date); 
	}
	
	
	public void setAuthFlag(boolean flag)
	{
		m_bAuth = flag;
	}

	public boolean getAuthFlag()
	{
		return m_bAuth;
	}
	
	public void sendRequest()
	{
		PThreadManager.instance().PostRequest(this);
	}
	
	public boolean abortRequset()
	{
		synchronized(this)
		{
			if(REQUEST_STATUS_INIT == m_iCanelStatus)
			{
				m_iCanelStatus = REQUEST_STATUS_ABORT;
				return true;
			} 
			else if (REQUEST_STATUS_EXEC == m_iCanelStatus)
			{
				if(null != m_cRequest)
				{
					PNetLog.e("ReqID:" + m_iRequestID+ " abort abort");
					m_cRequest.abort();
					m_iCanelStatus = REQUEST_STATUS_ABORT;
					return true;
				} 
				else 
				{
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	public boolean cancelRequest()
	{
		synchronized(this)
		{
			if(REQUEST_STATUS_INIT == m_iCanelStatus)
			{
				m_iCanelStatus = REQUEST_STATUS_CANCEL;
				return true;
			} 
			else if (REQUEST_STATUS_EXEC == m_iCanelStatus)
			{
				if(null != m_cRequest)
				{
					PNetLog.e("ReqID:" + m_iRequestID+ "cancel abort");
					m_cRequest.abort();
					m_iCanelStatus = REQUEST_STATUS_CANCEL;
					return true;
				} 
				else 
				{
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	protected boolean isCancel()
	{
		if(REQUEST_STATUS_INIT == m_iCanelStatus)
		{
			m_iCanelStatus = REQUEST_STATUS_EXEC;
			return false;
		} 
		else if (REQUEST_STATUS_EXEC == m_iCanelStatus)
		{
			return false;
		} else {
			return true;
		}
	}
	
	protected void onFinish(int iThreadID)
	{
		// native
		if(getReqType() == PRequest.REQUEST_TYPE_NATIVE )
		{
			PNetLog.d("PThread id:"+iThreadID+" ReqID:"+m_iRequestID+" Response Native ResCode:"+m_iResCode);
		} 
		else 
		{
			PNetLog.d("PThread id:"+iThreadID+" ReqID:"+ m_iRequestID+ " Response Java ResCode:"+ m_iResCode);
		}
		
		if(REQUEST_TYPE_JAVA == m_iReqType && REQUEST_STATUS_CANCEL == m_iCanelStatus && false == isSync())
		{
			return;
		} 
		else 
		{
			onRecvData();
		}
		setCallBackTime();
	}
	
	protected void onRecvData()
	{
		
	}
	
	protected String makeAbsUrl(String url)
	{
		if(null == url) {
			return "";
		}
		int indexS = url.indexOf("(");
		int indexE = url.indexOf(")");
		if(-1 != indexS  && -1 != indexE )
		{
			String hostname = url.substring(indexS+1, indexE);
			String hostaddr = PHost.getHostAddrbyName(hostname);
			if(null != hostaddr)
			{
				String s1 = url.substring(0, indexS);
				String s2 = url.substring(indexE+1);
				String absUrl = s1+hostaddr+s2;
				return absUrl;
			} 
			else 
			{
				return url;
			}
		}
		return url;
	}
	
	/*
	ResponseHandler<byte[]> m_cHandler = new ResponseHandler<byte[]>() 
	{
		public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException 
		{
			HttpEntity entity = response.getEntity();
			PNetLog.d("response length:"+ entity.getContentLength());
			if (entity != null) {
				return EntityUtils.toByteArray(entity);
			} else {
				return null;
			}
		}
	};
	*/
	
    static class MonitorTimer extends java.util.TimerTask{
    	
    	public MonitorTimer(PRequest pcRequest, int time)
    	{
    		m_pcRequset = pcRequest;
    		m_iTotalCount = time;
    	}
    	
    	public void stop()
    	{
    		synchronized(this)
    		{
    			m_pcRequset = null;
    		}
    	}
    	
        @Override
        public void run() {
        	
        	synchronized(this)
        	{
	            // TODO Auto-generated method stub
        		if(null == m_pcRequset)
        		{
        			return;
        		}
        		
	        	m_iCount++;
	        	
	        	if(m_iCount >= m_iTotalCount)
	        	{
	        		m_pcRequset.abortRequset();
	        		PNetLog.d("ReqID:"+ m_pcRequset.getRequestId()+" Timer time out.");
	        	} else {
		        	if(m_pcRequset.isCancel())
		        	{	
		        		m_pcRequset.cancelRequest();
		        		PNetLog.d("ReqID:"+ m_pcRequset.getRequestId()+" Timer cancel.");
		        	}
	        	}
        	}
        }
        
        private int 	 m_iCount = 0;
        private int 	 m_iTotalCount = 0;
        private PRequest m_pcRequset = null;
        	
    }
    
	
	
	 
	public void execute(int iThreadID)
	{
		if(m_iMethod == METHODS_INVALID || m_cUrl.length() == 0)
		{
			PNetLog.e("PThread id:"+iThreadID+" ReqID:" + m_iRequestID+ " Method:"+ m_iMethod + " URL:" + m_cUrl);
			setReceivebuf(null);
			setResCode(PResponse.RES_CODE_REQUEST_ERROR);
			return;
		}
		
		HttpClient cHttpClient = PHttpClient.getInstance().getHttpClient();
		HttpRequestBase cRequest = null;

		switch(m_iMethod)
		{
		case METHODS_HEAD:
			cRequest = new HttpHead(m_cUrl);
			break;
		case METHODS_GET:
			cRequest = new HttpGet(m_cUrl);
			break;
		case METHODS_POST:
			HttpPost postRequest = new HttpPost(m_cUrl);
			if(null != m_cPostData)
			{
				postRequest.setEntity(m_cPostData.getPostData());
				
				if (PostData.POSTDATA_TYPE_MULTIDATA == m_cPostData.getPostDataType())
				{
					postRequest.addHeader("Content-Type", "multipart/form-data; boundary=" + m_cPostData.getBoundary());
				}
			}
			cRequest = postRequest;
			break;
		default:
			cRequest = null;
			PNetLog.e("PThread id:"+ iThreadID +" ReqID:" + m_iRequestID+ " create request failed");
			setReceivebuf(null);
			setResCode(PResponse.RES_CODE_REQUEST_ERROR);
			return;
		}
		
		cRequest.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		cRequest.addHeader("Connection", "close");
		//cRequest.addHeader("Connection", "Keep-Alive");
		
		if(true == getAuthFlag())
		{
			cRequest.addHeader("SESSION-TOKEN", PLogin.getSessionToken());
		}
		
		Vector<String> vCommonHttpHeader = PHost.getCommonHttpHeader();
		int common_header_size = vCommonHttpHeader.size()/2;
		for(int i = 0; i < common_header_size; i++)
		{
			cRequest.addHeader((String)vCommonHttpHeader.get(i*2),(String) vCommonHttpHeader.get(i*2+1));
		}
		
		int header_size = m_vHttpHeader.size()/2;
		for(int i = 0; i < header_size; i++)
		{
			cRequest.addHeader((String)m_vHttpHeader.get(i*2),(String) m_vHttpHeader.get(i*2+1));
		}
		
		Timer timer = new Timer();
		MonitorTimer mt = new MonitorTimer(this, 50);
 		try {
 			
			synchronized(this)
			{
				if(isCancel())
				{
					PNetLog.d("PThread id:"+iThreadID+" cancel ReqID:"+ m_iRequestID+" URL:" + m_cUrl);
					setReceivebuf(null);
					setResCode(PResponse.RES_CODE_CANCEL);
					return;
				}
				m_cRequest = cRequest;
			    PNetLog.d("PThread id:"+iThreadID+" ReqID:" + m_iRequestID+ " execute URL: "+cRequest.getURI()+ " Method:"+ m_iMethod);
				setStartTime();
				timer.schedule(mt, 1000, 1000);
			}
			
			HttpResponse cResponse = cHttpClient.execute(cRequest);

			setEndTime();
			
			if(HttpStatus.SC_OK == cResponse.getStatusLine().getStatusCode())
			{
				//PNetLog.d("PThread id:"+iThreadID+" ReqID:" + m_iRequestID+ " response code:"+ cResponse.getStatusLine().getStatusCode());
			} else {
				PNetLog.e("PThread id:"+iThreadID+" ReqID:" + m_iRequestID+ " response code:"+ cResponse.getStatusLine().getStatusCode()+ ":"+ cResponse.getStatusLine().getReasonPhrase()+ " execute URL"+cRequest.getURI());
			}
			/*
			HeaderIterator  it = cResponse.headerIterator();
			while (it.hasNext()) {
				PNetLog.d(it.next().toString());
			}
			*/
			setResCode(cResponse.getStatusLine().getStatusCode());
			onResponse(cResponse, iThreadID);
			setReceiveTime();
			mt.stop();
			timer.cancel();
			
			synchronized(this)
			{
				m_cRequest = null;
			}
			
		} 
		catch (Exception e)
		{
			timer.cancel();
			PNetLog.e("PThread id:"+iThreadID+" ReqID:"+ m_iRequestID + " error:"+ e.getMessage());
			setReceivebuf(null);
			setResCode(PResponse.RES_CODE_CONNECT_EXCEPTION);
			return;
		} 
		return ;
		
	}
	
	private void onResponse(HttpResponse cResponse, int iThreadID)
	{ 
		HttpEntity cEntity = cResponse.getEntity();
		setZipSize((int)cEntity.getContentLength());
				
		/*
		HeaderIterator itor = m_cResponse.headerIterator();
		while (itor.hasNext())
		{
			PNetLog.e("header:"+ itor.next());
			
		}
		*/
		setRecvContentType(cEntity.getContentType().getValue());
		
		InputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		ByteArrayOutputStream byteOutputStream = null;
		int type = getResDataType();
		
		switch(type)
		{
		case RESPONSE_DATA_BUF:
			byteOutputStream = new ByteArrayOutputStream();
			outputStream  = new BufferedOutputStream(byteOutputStream);
			break;
		case RESPONSE_DATA_FILE:
			try {
				outputStream  = new BufferedOutputStream(new FileOutputStream(getResFName(), false));
			} 
			catch (FileNotFoundException e)
			{
				setReceivebuf(null);
				setResCode(PResponse.RES_CODE_WRITEFILE_EXCEPTION);
				return;
			}
			break;
		case RESPONSE_DATA_NODATA:
			setResCode(m_iResCode);
			return;
		}
		
		try 
		{
			if(true == cResponse.containsHeader("Content-Encoding"))
			{
				GZIPInputStream gZipInputStream = new GZIPInputStream(cEntity.getContent());
				inputStream = gZipInputStream;
			} 
			else 
			{
				BufferedInputStream bufferInputStream = new BufferedInputStream(cEntity.getContent());
				inputStream = bufferInputStream;
			}
			
			byte[] tmpBuf = new byte[8192];
			int readLength = 0;
			int unZipLength = 0;
			while (-1 != (readLength =inputStream.read(tmpBuf))) {
				outputStream.write(tmpBuf, 0, readLength);
				unZipLength = unZipLength + readLength;
				if(null != m_cListener)
				{
					m_cListener.onReceive(unZipLength, m_iZipSize);
				}
			}
			setUnzipSize(unZipLength);
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} 
		catch (IOException e)
		{
			PNetLog.e("PThread id:"+iThreadID+" ReqID:"+ m_iRequestID + " Read Timeout error:"+ e.getMessage());
			e.printStackTrace();
			setReceivebuf(null);
			setResCode(PResponse.RES_CODE_CONNECT_EXCEPTION);
			return;
		}
		
		if(RESPONSE_DATA_BUF == type)
		{
			byte[] arrData = byteOutputStream.toByteArray();
			setReceivebuf(arrData);
			setResCode(m_iResCode);
			// RESPONSE_DATA_FILE
		} else {
			
		}
			
		try {
			cEntity.consumeContent();
		}
		catch (Exception e)
		{
			
		}
	}
	
	protected boolean isSync()
	{
		return false;
	}
	
	public String getNetTraceInfo1()
	{
		String info = String.format("ID:%08d Size: %8d %8d URL:%s"
				, m_iRequestID
				, m_iZipSize
				, m_iUnzipSize
				, m_cUrl);
		return info;
	}
	
	public String getNetTraceInfo2()
	{
		String info = String.format("ID:%08d %5d,%5d,%5d [%s/%s/%s/%s] Type:%d Code:%3d"
					, m_iRequestID
					, m_iDiffTime
					, m_iDiffTime1
					, m_iDiffTime2
					, m_cStartTime
					, m_cEndTime
					, m_cReceiveTime
					, m_cCallBackTime
					, m_iReqType
					, m_iResCode);
		
		return info;
	}




}
