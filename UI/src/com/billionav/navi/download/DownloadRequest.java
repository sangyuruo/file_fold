
package com.billionav.navi.download;


import com.billionav.navi.download.DownloadAttributes;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.SystemClock;
import android.util.Log;

import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PHost;
import com.billionav.navi.net.PNetLog;
import com.billionav.navi.net.TrustSSLSocketFactory;
import com.billionav.navi.download.DownloadAttributes;

public class DownloadRequest {	
	private final static int  EXTRACT_SIZE = 2048;
	private final static long CALLBACK_SLEEP_TIME = 1000;
	public final static int   DOWNLOAD_STATUS_NO_ERROR = 0;
	public final static int   DOWNLOAD_STATUS_FINISH = 1;
	public final static int   DOWNLOAD_STATUS_NETEXCEPTION = 2;
	public final static int   DOWNLOAD_STATUS_CANCEL = 3;
	public final static int   DOWNLOAD_STATUS_IOEXCEPTION = 4;	
	private int               m_statusCode = DOWNLOAD_STATUS_NO_ERROR;
	private static int        m_taskId = 0;
	private boolean           m_stopTask = false;	
	private long              m_contentLength = 1;
	private long              m_receivedCount = 0;
	private boolean           m_downloadResuming = false;
	private long              m_lastCount = 0;
	private long              m_beginTime = 0;
	private long              m_endTime = 0;
	private String            m_absUrlPath = "";
	private DownloadAttributes m_downloadAttributes = null;
		
	private Vector<DownloadRequestListener> m_downloadListeners = new Vector<DownloadRequestListener>();
	
	public DownloadRequest( DownloadAttributes downloadAttributes ) {		
		m_downloadAttributes = downloadAttributes;
		m_absUrlPath = makeAbsUrl( m_downloadAttributes.getDownloadPath() );
		m_downloadListeners.add( m_downloadAttributes.getDownloadRequestListener() );
	}
	
	public String getDownloadPath()
	{
		if( null != m_downloadAttributes )
		{
			return m_downloadAttributes.getDownloadPath();
		}
		return "";
	}

	protected String makeAbsUrl(String url)
	{
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
	
	public HttpClient CreateHttpClient()
	{
		HttpParams params = new BasicHttpParams();

		/////// Protocol ////////
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(params, true);

		ComponentName comp = NSViewManager.GetViewManager().getComponentName();
		try {
			PackageInfo packinfo = NSViewManager.GetViewManager().getPackageManager().getPackageInfo(comp.getPackageName(), 0);
			HttpProtocolParams.setUserAgent(params, "billionav/" + packinfo.versionName);
		} 
		catch (NameNotFoundException e)
		{
			PNetLog.e("Can't get package name:"+ comp.getPackageName());
		}
		
		/////// Connection ////////
		// Set the timeout in milliseconds until a connection is established.  
	    //HttpConnectionParams.setConnectionTimeout(params, 10*1000);
	    // Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(params, 10*1000);
        
	    HttpConnectionParams.setStaleCheckingEnabled(params, false);
        
        HttpConnectionParams.setSocketBufferSize(params, 8192);	    
        
        
	 	SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		
		schemeRegistry.register(new Scheme("https", new TrustSSLSocketFactory(), 443));
		
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		return new DefaultHttpClient(cm, params);		
	}
	
	public void DoAction() throws Exception
	{
		boolean onData = true;
		HttpClient httpClient = CreateHttpClient();		

		try {
			getDownloadFileInfo(httpClient);
			onData = false;
			startDownload(httpClient);
			
		} catch (Exception e) {
			if( onData )
			{
				m_statusCode = DownloadRequest.DOWNLOAD_STATUS_NETEXCEPTION;
				for(DownloadRequestListener listener: m_downloadListeners ){					
					listener.onDownloadData( m_downloadAttributes.getSavePath(), m_contentLength, m_receivedCount, m_statusCode );
				}
			}
			
			
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	private void getDownloadFileInfo(HttpClient httpClient) throws IOException,
			ClientProtocolException, Exception {
		HttpHead httpHead = new HttpHead( m_absUrlPath );
		HttpResponse response = httpClient.execute(httpHead);

		int statusCode = response.getStatusLine().getStatusCode();

		if(statusCode != 200) 
		{
			m_statusCode = DownloadRequest.DOWNLOAD_STATUS_NETEXCEPTION;
			throw new Exception("URL:"+ m_absUrlPath + " Error:"+statusCode);
		}
        /*
		if(getDebug()){
			PNetLog.d("===Get header Info===:"+url);
			for(Header header : response.getAllHeaders()){
				PNetLog.d(header.getName()+":"+header.getValue());
			}
		}*/

		//Content-Length
		Header[] headers = response.getHeaders("Content-Length");
		if(headers.length > 0)
			m_contentLength = Long.valueOf(headers[0].getValue());
		//Accept-Ranges
//		headers = response.getHeaders("Accept-Ranges");
//		if(headers.length > 0)
//			acceptRanges = true;

		httpHead.abort();


		if(!m_downloadResuming && m_downloadAttributes.isDownloadResuming()){
			httpHead = new HttpHead( m_absUrlPath );
			httpHead.addHeader( "Range", "bytes=0-"+ m_contentLength );
			response = httpClient.execute(httpHead);
			if( response.getStatusLine().getStatusCode() == 206 ){
				m_downloadResuming = true;
			} else {	
				PNetLog.e("NOT Support Range:" + m_absUrlPath );
			}
			httpHead.abort();
		}

	}
		
	public void extractZipFile( String zipFilePath, String directoryPath, Vector<String> fileName ){
		try{	
			PNetLog.e( "zipFilePath = [" + zipFilePath + "]" );
			PNetLog.e( "directoryPath = [" + directoryPath + "]" );
			ZipFile zipFile = new ZipFile( zipFilePath );
			Enumeration enu = zipFile.entries();			
			
			while( (!checkQuit()) && enu.hasMoreElements()){
				ZipEntry zipEntry = (ZipEntry)enu.nextElement();
				if(zipEntry.isDirectory()){
					new File( directoryPath + zipEntry.getName() ).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream( zipFile.getInputStream( zipEntry ) );
				File file = new File( directoryPath + zipEntry.getName() + ".tmp" );
				File parent = file.getParentFile();
				if( parent != null && !parent.exists() ){
					parent.mkdirs();
				}
				
				FileOutputStream fos = new FileOutputStream( file );
				BufferedOutputStream bos = new BufferedOutputStream( fos, EXTRACT_SIZE );
				
				int count;
				byte[] array = new byte[EXTRACT_SIZE];
				while( (!checkQuit()) && (count = bis.read(array, 0, EXTRACT_SIZE))!=-1){
					bos.write(array, 0, count);
				}			
				
				bos.flush();
				bos.close();
				bis.close();
				
				if( !checkQuit() )
				{
					String filePath = directoryPath + zipEntry.getName();
					long fileSize = file.length();
					file.renameTo( new File( filePath ) );
					PNetLog.e( "filePath = [" + filePath + "]" );
					/*
					for(DownloadRequestListener listener: m_downloadListeners ){
						listener.onDownloadData( String filePath, long totalSize, long downloadSize, int returnCode )
						listener.autoCallback( filePath, fileSize, DownloadRequest.DOWNLOAD_STATUS_FINISH );
					}	
					*/				
				}
				else
				{
					file.delete();
				}
				
			}
			if( !checkQuit() )
			{
				File deleteFile =new File( zipFilePath );
				deleteFile.delete();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean netSupportDownload()
	{
		boolean netSupportDownload = true;
		if( m_downloadAttributes.getOnlyUseWifi() )
		{
			netSupportDownload = PConnectReceiver.CONNECT_TYPE_WIFI == PConnectReceiver.getConnectType();
		}
		return netSupportDownload; 
	}

    
	private void startDownload(HttpClient httpClient) throws IOException {

		long startPosition = 0;
		long endPosition = m_contentLength;
		
		File saveFile = new File( m_downloadAttributes.getSavePath() );
				
		File tmpFile = new File( saveFile.getAbsolutePath() + ".tmp" );
		
		boolean bExists = tmpFile.exists();
		
		if( !bExists || !m_downloadResuming )
		{
			tmpFile.createNewFile();

		} else {
			startPosition = tmpFile.length();
		}
		
		m_receivedCount = startPosition;
		m_lastCount = m_receivedCount;
		m_beginTime = System.currentTimeMillis();
		m_endTime = m_beginTime;

		if(DownloadTask.getDebug()){
			//PNetLog.d("Start Download["+m_localPath+"]" + startPosition + "-" +endPosition);
		}
		
		monitor();
		
		int tryTime = m_downloadAttributes.getTryTimes();
		
		while( netSupportDownload() && m_receivedCount < m_contentLength && tryTime-- > 0 )
		{	
			if( checkQuit() )						
			{				
				m_statusCode = DownloadRequest.DOWNLOAD_STATUS_CANCEL;								
				return;
			}
			
			if( null == httpClient )
			{
				httpClient = CreateHttpClient();
			}	
			
			RandomAccessFile outputStream = null;
			PNetLog.e("start download tryTime = " + tryTime );
			try {
				HttpGet httpGet = new HttpGet( m_absUrlPath );
				if( m_downloadResuming ){
					startPosition = m_receivedCount;
					httpGet.addHeader("Range", "bytes="+startPosition+"-"+endPosition);
				}
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
								
				if( statusCode == 206 || statusCode == 200 ){
					
					InputStream inputStream = response.getEntity().getContent();
					outputStream = new RandomAccessFile(tmpFile, "rw");

					outputStream.seek(startPosition);
					int count = 0;byte[] buffer=new byte[1024];
					int zeroCount = 0;
					while( ( m_receivedCount < m_contentLength ) && (count = inputStream.read(buffer, 0, buffer.length)) != -1 ){
						if( !netSupportDownload() )
						{
							stopTask();
						}
						if( checkQuit() )						
						{
							outputStream.close();
							m_statusCode = DownloadRequest.DOWNLOAD_STATUS_CANCEL;
							if(DownloadTask.getDebug()){
								//PNetLog.d("Quit Download:["+localPath+"]" + startPosition + "-" +endPosition);
							}
							httpGet.abort();
							httpClient.getConnectionManager().shutdown();
							return;
						}
						
						if( 0 == count )
						{
							++zeroCount;
							if( 100 == zeroCount ){
								break;
							}
							else{
								continue;
							}						
						}else{
							tryTime = 100;
							zeroCount = 0;
						}
						outputStream.write(buffer, 0, count);
						m_receivedCount = m_receivedCount + count;						
					}
					outputStream.close();
					if( m_receivedCount == m_contentLength )
					{
						httpGet.abort();
						if( saveFile.exists() )
						{
							saveFile.deleteOnExit();
						}
						tmpFile.renameTo( saveFile );
						
						PNetLog.e("download intfo:receivedCount=" + m_receivedCount + ",contentLength = " + m_contentLength );	
						
					}										
				}
				httpGet.abort();
				
			} catch (IOException e) {			
				if( null != outputStream ) {
					outputStream.close();
				}
				
				if( PConnectReceiver.CONNECT_TYPE_NONE == PConnectReceiver.getConnectType() )
				{
					while( !netSupportDownload() && tryTime > 0 && !checkQuit() )
					{
						--tryTime;
						Log.e( "DSP", "download try to test wifi time = " + tryTime );
						SystemClock.sleep( 5000 );				
					}				
				}		
				else if( PConnectReceiver.CONNECT_TYPE_MOBILE == PConnectReceiver.getConnectType() )
				{
					PNetLog.e("download ConnectType = CONNECT_TYPE_MOBILE" );
					tryTime = 0;
				}else if( tryTime > 0 ){
					PNetLog.e("download ConnectType = " + PConnectReceiver.getConnectType() );
					SystemClock.sleep( 2000 );
				}
	
			    Log.e( "DSP", m_absUrlPath + " Download IOException:"+e.getMessage());
				if( tryTime <= 1 )
				{
					m_statusCode = DownloadRequest.DOWNLOAD_STATUS_NETEXCEPTION;
				}				
			} finally {
				if(DownloadTask.getDebug()){
					//PNetLog.d("End Download:["+localPath+"]" + startPosition + "-" +endPosition + "tryTime:" + tryTime);
				}
				
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
			
		}
		
		if( m_receivedCount == m_contentLength )
		{
			Vector<String> fileName = new Vector<String>();
			if( m_downloadAttributes.isUnzipFile() )
			{				
				String saveDirectory = m_downloadAttributes.getSaveDirectory();				
				extractZipFile( saveFile.getPath(), saveDirectory, fileName );					
			}			
			
			for(DownloadRequestListener listener: m_downloadListeners ){					
				listener.onDownloadData( saveFile.getPath(), m_contentLength, m_receivedCount, DownloadRequest.DOWNLOAD_STATUS_FINISH );
			}
			
			if( !checkQuit() )
			{
				m_statusCode = DownloadRequest.DOWNLOAD_STATUS_FINISH;
			}
			else
			{
				m_statusCode = DownloadRequest.DOWNLOAD_STATUS_CANCEL;
			}
		}		
		else if(  DownloadRequest.DOWNLOAD_STATUS_NO_ERROR == m_statusCode )
		{			
			m_statusCode = DownloadRequest.DOWNLOAD_STATUS_NETEXCEPTION;
			PNetLog.e("download error:receivedCount=" + m_receivedCount + ",contentLength = " + m_contentLength );						
		}
		
		return;		
	}
	
	
	public void stopTask()
	{
		PNetLog.e(" to stopTask" );	
		m_stopTask = true;
	}
	
	public boolean checkQuit()
	{
		return m_stopTask;
	}
	
	private void monitor() {
		new Thread() {
			public void run() {
				
				while(DownloadRequest.DOWNLOAD_STATUS_NO_ERROR == m_statusCode ) {
					showInfo();
					try {
						Thread.sleep( CALLBACK_SLEEP_TIME );
					} catch (InterruptedException e) { }
				}
				if( DownloadRequest.DOWNLOAD_STATUS_FINISH != m_statusCode )
				{
					showInfo();
				}				
			}
		}.start();
	}
    
	private void showInfo() {
		
		long currentTime = System.currentTimeMillis();
		double realTimeSpeed = ( m_receivedCount - m_lastCount ) * 1.0 / ((currentTime - m_endTime ) / 1000.0);
		double globalSpeed = m_receivedCount * 1.0 / ((currentTime - m_beginTime) / 1000.0);
		m_lastCount = m_receivedCount;
		m_endTime = currentTime;
		
		if(DownloadTask.getDebug()){
			int progess = (int)( m_receivedCount*100.0/ m_contentLength );
			PNetLog.d( "taskId:" + m_taskId + "Monitor:"+progess+"% speed:"+formatSpeed(realTimeSpeed) + "stopTask:" + m_stopTask );
		}
		
		if( m_downloadListeners.isEmpty()) return;
		
		for(DownloadRequestListener listener: m_downloadListeners ){					
			listener.onDownloadData( m_downloadAttributes.getSavePath(), m_contentLength, m_receivedCount, m_statusCode );
		}
		// formatSpeed(realTimeSpeed), formatSpeed(globalSpeed) 		 
	};
	

	private String formatSpeed(double speed){
		DecimalFormat format = new DecimalFormat("#,##0.##");
		if(speed<1024){
			return format.format(speed)+" B/s";
		}
		
		speed /= 1024;
		if(speed<1024){
			return format.format(speed)+" K/s";
		}
		
		speed /= 1024;
		if(speed<1024){
			return format.format(speed)+" M/s";
		}
		
		speed /= 1024;
		if(speed<1024){
			return format.format(speed)+" G/s";
		}
		
		speed /= 1024;
		if(speed<1024){
			return format.format(speed)+" T/s";
		}
		
		return format.format(speed) + "B/s";
	}

}
