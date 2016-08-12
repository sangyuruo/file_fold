package com.billionav.navi.net;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.billionav.navi.menucontrol.NSViewManager;

public class PHttpClient {
	
	private static PHttpClient s_Instance = null;

	private DefaultHttpClient m_cHttpClient = null; 
	
	public static PHttpClient getInstance()
	{
		if(null == s_Instance)
		{
			s_Instance = new PHttpClient();
		}
		
		return s_Instance;
	}
	
	HttpRequestRetryHandler m_cRetryHandler = new HttpRequestRetryHandler() 
	{
		public boolean retryRequest(IOException exception,int executionCount,HttpContext context) {
			
			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			PNetLog.e("Exception retry times:"+executionCount+" ["+exception.getMessage()+"]:"+request.getRequestLine().getUri());
			if (executionCount >= 3) 
			{
				PNetLog.e("execute Exception: retry times > 3"+" "+request.getRequestLine().getUri());
				return false;
			}
			/*
			if (exception instanceof NoHttpResponseException) 
			{
				PNetLog.d("execute Exception: lost connection");
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				PNetLog.d("execute Exception: ssl Exception");
				return false;
			}
			
			
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				return true;
			}
			*/
			return true;
		}
	};
	
	ConnectionKeepAliveStrategy m_ConnectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() 
	{
		public long getKeepAliveDuration(HttpResponse response, HttpContext context) 
		{
			HeaderElementIterator it = new BasicHeaderElementIterator(
			response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) 
				{
					try 
					{
						return Long.parseLong(value) * 1000;
					} 
					catch(NumberFormatException ignore) 
					{
					}
				}
			}
			HttpHost target = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			PNetLog.d("KeepAliveDuration HostName:"+target.getHostName());
			return 30 * 1000;
		}
	};
	
	PHttpClient() 
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
	    HttpConnectionParams.setSoTimeout(params, 30*1000);
        
	    HttpConnectionParams.setStaleCheckingEnabled(params, false);
        
        HttpConnectionParams.setSocketBufferSize(params, 8192);
	    
        /////// Pool ////////
        ConnPerRoute connPerRoute = new ConnPerRouteBean(12);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
        ConnManagerParams.setMaxTotalConnections(params, 20);
	    
        /////// Some client params ////////
        //HttpClientParams.setRedirecting(params, false);
        
	 	SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		
		//SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();   
		//socketFactory.setHostnameVerifier((X509HostnameVerifier)org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		schemeRegistry.register(new Scheme("https", new TrustSSLSocketFactory(), 443));
		
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		m_cHttpClient = new DefaultHttpClient(cm, params);
		
		//m_cHttpClient.setKeepAliveStrategy(m_ConnectionKeepAliveStrategy);
		
		//m_cHttpClient.setHttpRequestRetryHandler(m_cRetryHandler);		
 
	}
	
	public DefaultHttpClient getHttpClient()
	{
		return m_cHttpClient;
	}
	

}
