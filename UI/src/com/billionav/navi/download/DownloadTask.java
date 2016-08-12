/**
 * 
 */
package com.billionav.navi.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
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

import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PHost;
import com.billionav.navi.net.PNetLog;
import com.billionav.navi.net.TrustSSLSocketFactory;

/**
 * 
 *
 */
public class DownloadTask {
	private int taskId = 0;
	private String url = "";
	private int threadCount = 5;
	private String localPath = "";
	
	private boolean acceptRanges = false;
//	private String ranges = "";
	private long contentLength = 0;
	private long receivedCount = 0;
	
	private long lastCount = 0;
	private long beginTime = 0;
	private long endTime = 0;
	
	private int extractSize = 2048;

	private long autoCallbackSleep = 1000;
	private boolean stopTask = false;
	
	public final static int STATUS_NO_ERROR = 0;
	public final static int STATUS_FINISH = 1;
	public final static int STATUS_NETEXCEPTION = 2;
	public final static int STATUS_CANCEL = 3;
	public final static int STATUS_IOEXCEPTION = 4;
	public static int s_taskId = 0;
	
	private int m_statusCode = STATUS_NO_ERROR;
	
	private Vector<DownloadTaskListener> listeners = new Vector<DownloadTaskListener>();
	
	private static boolean DEBUG = true;

	@Deprecated
	public DownloadTask(String url) {
		this(url,1);
	}
	
	@Deprecated
	public DownloadTask(String url, int threadCount) {
		this(url,"",threadCount);
	}

	public DownloadTask(String url, String localPath) {
		this(url,localPath,1);
	}

	public DownloadTask(String url, String localPath, int threadCount) {
		
		this.url = makeAbsUrl(url);
		this.threadCount = threadCount;
		this.localPath = localPath;
		this.taskId = ++s_taskId;
	}
	
	public String getURL()
	{
		return url;
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
	
	public void setAutoCallbackSleep(long autoCallbackSleep) {
		this.autoCallbackSleep = autoCallbackSleep;
	}
	
	public long getAutoCallbackSleep() {
		return this.autoCallbackSleep;
	}
	
	public static void setDebug(boolean debug){
		DEBUG = debug;
	}
	public static boolean getDebug(){
		return DEBUG;
	}
	
	public void setLocalPath(String localPath){
		this.localPath = localPath;
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
	    
        /////// Pool ////////
        //ConnPerRoute connPerRoute = new ConnPerRouteBean(12);
        //ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
        //ConnManagerParams.setMaxTotalConnections(params, 20);
	    
        /////// Some client params ////////
        //HttpClientParams.setRedirecting(params, false);
        
	 	SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		
		schemeRegistry.register(new Scheme("https", new TrustSSLSocketFactory(), 443));
		
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		return new DefaultHttpClient(cm, params);		
	}
	
	public void DoAction() throws Exception
	{
		HttpClient httpClient = CreateHttpClient();		

		try {
			getDownloadFileInfo(httpClient);
			startDownload(httpClient);
			
		} catch (Exception e) {
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	private void getDownloadFileInfo(HttpClient httpClient) throws IOException,
			ClientProtocolException, Exception {
		HttpHead httpHead = new HttpHead(url);
		HttpResponse response = httpClient.execute(httpHead);

		int statusCode = response.getStatusLine().getStatusCode();

		if(statusCode != 200) 
		{
			m_statusCode = DownloadTask.STATUS_NETEXCEPTION;
			throw new Exception("URL:"+ url + " Error:"+statusCode);
		}

		if(getDebug()){
			PNetLog.d("===Get header Info===:"+url);
			for(Header header : response.getAllHeaders()){
				PNetLog.d(header.getName()+":"+header.getValue());
			}
		}

		//Content-Length
		Header[] headers = response.getHeaders("Content-Length");
		if(headers.length > 0)
			contentLength = Long.valueOf(headers[0].getValue());
		//Accept-Ranges
//		headers = response.getHeaders("Accept-Ranges");
//		if(headers.length > 0)
//			acceptRanges = true;

		httpHead.abort();


		if(!acceptRanges){
			httpHead = new HttpHead(url);
			httpHead.addHeader("Range", "bytes=0-"+contentLength);
			response = httpClient.execute(httpHead);
			if(response.getStatusLine().getStatusCode() == 206){
				acceptRanges = true;
			} else {
				PNetLog.e("NOT Support Range:"+url);
			}
			httpHead.abort();
		}

	}
	
	public void addDownloadTaskListener(DownloadTaskListener listener){
		listeners.add(listener);
	}
	
	public void extractZipFile( String zipFilePath, String directoryPath ){
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
				if(parent != null && !parent.exists()){
					parent.mkdirs();
				}
				
				FileOutputStream fos = new FileOutputStream( file );
				BufferedOutputStream bos = new BufferedOutputStream( fos, extractSize );
				
				int count;
				byte[] array = new byte[extractSize];
				while( (!checkQuit()) && (count = bis.read(array, 0, extractSize))!=-1){
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
					
					for(DownloadTaskListener listener:listeners){
						listener.autoCallback( filePath, fileSize, DownloadTask.STATUS_FINISH );
					}
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


	private void startDownload(HttpClient httpClient) throws IOException {

		long startPosition = 0;
		long endPosition = contentLength;
		
		File file = new File(localPath);
		String filename = file.getAbsolutePath();		
	
		File tmpfile = new File(filename);
		
		boolean bExists = tmpfile.exists();
		
		if( !bExists || !acceptRanges)
		{
			tmpfile.createNewFile();

		} else {
			startPosition = tmpfile.length();
		}
		
		receivedCount = startPosition;
		lastCount = receivedCount;
		beginTime = System.currentTimeMillis();
		endTime = beginTime;

		if(DownloadTask.getDebug()){
			PNetLog.d("Start Download["+localPath+"]" + startPosition + "-" +endPosition);
		}
		
		monitor();
		
		int tryTime = 100;
		boolean wifiOpen = PConnectReceiver.CONNECT_TYPE_WIFI == PConnectReceiver.getConnectType();
		while( wifiOpen && receivedCount < contentLength && --tryTime > 0 )
		{	
			if( checkQuit() )						
			{				
				m_statusCode = DownloadTask.STATUS_CANCEL;
				if(DownloadTask.getDebug()){
					PNetLog.d("Quit Download:["+localPath+"]" + startPosition + "-" +endPosition);
				}				
				return;
			}
			if( null == httpClient )
			{
				httpClient = CreateHttpClient();
			}	
			
			RandomAccessFile outputStream = null;
			PNetLog.e("start download tryTime = " + tryTime );
			try {
				HttpGet httpGet = new HttpGet(url);
				if(acceptRanges){
					startPosition = receivedCount;
					httpGet.addHeader("Range", "bytes="+startPosition+"-"+endPosition);
				}
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
								
				if( statusCode == 206 || statusCode == 200 ){
					
					InputStream inputStream = response.getEntity().getContent();
					outputStream = new RandomAccessFile(tmpfile, "rw");

					outputStream.seek(startPosition);
					int count = 0;byte[] buffer=new byte[1024];
					int zeroCount = 0;
					while( ( receivedCount < contentLength ) && (count = inputStream.read(buffer, 0, buffer.length)) != -1 ){
						if( PConnectReceiver.CONNECT_TYPE_MOBILE == PConnectReceiver.getConnectType() )
						{
							stopTask();
						}
						if( checkQuit() )						
						{
							outputStream.close();
							m_statusCode = DownloadTask.STATUS_CANCEL;
							if(DownloadTask.getDebug()){
								PNetLog.d("Quit Download:["+localPath+"]" + startPosition + "-" +endPosition);
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
						receivedCount = receivedCount + count;						
					}
					outputStream.close();
					if( receivedCount == contentLength )
					{
						httpGet.abort();
						tmpfile.renameTo(file);
						
						PNetLog.e("download intfo:receivedCount=" + receivedCount + ",contentLength = " + contentLength );	
						
					}										
				}
				httpGet.abort();
				
			} catch (IOException e) {			
				if( null != outputStream ) {
					outputStream.close();
				}
				
				if( PConnectReceiver.CONNECT_TYPE_NONE == PConnectReceiver.getConnectType() )
				{
					while( PConnectReceiver.CONNECT_TYPE_WIFI != PConnectReceiver.getConnectType() 
							&& tryTime > 0 && !checkQuit() )
					{
						--tryTime;
						PNetLog.e("download try to test wifi time = " + tryTime );
						SystemClock.sleep( 5000 );				
					}				
				}		
				else if( PConnectReceiver.CONNECT_TYPE_MOBILE == PConnectReceiver.getConnectType() )
				{
					PNetLog.e("download ConnectType = CONNECT_TYPE_MOBILE" );
					tryTime = 0;
				}else{
					PNetLog.e("download ConnectType = " + PConnectReceiver.getConnectType() );
					SystemClock.sleep( 2000 );
				}
	
				PNetLog.e(url+" Download IOException:"+e.getMessage());
				if( tryTime <= 1 )
				{
					m_statusCode = DownloadTask.STATUS_NETEXCEPTION;
				}				
			} finally {
				if(DownloadTask.getDebug()){
					PNetLog.d("End Download:["+localPath+"]" + startPosition + "-" +endPosition + "tryTime:" + tryTime);
				}
				
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
			
		}
		
		if( receivedCount == contentLength )
		{
			String directoryPath = file.getPath().substring( 0, filename.lastIndexOf("/") + 1 );				
			extractZipFile( file.getPath(), directoryPath );
			if( !checkQuit() )
			{
				m_statusCode = DownloadTask.STATUS_FINISH;
			}
			else
			{
				m_statusCode = DownloadTask.STATUS_CANCEL;
			}
		}		
		else if(  DownloadTask.STATUS_NO_ERROR == m_statusCode )
		{			
			m_statusCode = DownloadTask.STATUS_NETEXCEPTION;
			PNetLog.e("download error:receivedCount=" + receivedCount + ",contentLength = " + contentLength );						
		}
		
		return;		
	}
	
	public void stopTask()
	{
		PNetLog.e(" to stopTask" );	
		stopTask = true;
	}
	
	public boolean checkQuit()
	{
		return stopTask;
	}
	
	private void monitor() {
		new Thread() {
			public void run() {
				
				while(DownloadTask.STATUS_NO_ERROR == m_statusCode) {
					showInfo();
					try {
						Thread.sleep(autoCallbackSleep);
					} catch (InterruptedException e) { }
				}
				if( DownloadTask.STATUS_FINISH != m_statusCode )
				{
					showInfo();
				}				
			}
		}.start();
	}

	private void showInfo() {
		long currentTime = System.currentTimeMillis();
		double realTimeSpeed = (receivedCount - lastCount) * 1.0 / ((currentTime - endTime) / 1000.0);
		double globalSpeed = receivedCount * 1.0 / ((currentTime - beginTime) / 1000.0);
		lastCount = receivedCount;
		endTime = currentTime;
		
		if(DownloadTask.getDebug()){
			int progess = (int)(receivedCount*100.0/contentLength);
			PNetLog.d( "taskId:" + taskId + "Monitor:"+progess+"% speed:"+formatSpeed(realTimeSpeed) + "stopTask:" + stopTask );
		}
		
		if(listeners.isEmpty()) return;
		
		for(DownloadTaskListener listener:listeners){
			listener.autoCallback( this.localPath, receivedCount, m_statusCode );
		}

		/* formatSpeed(realTimeSpeed), formatSpeed(globalSpeed) */
	};
	
	public void addTaskListener(DownloadTaskListener listener) {
		listeners.add(listener);
	}

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
