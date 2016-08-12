package com.billionav.navi.net;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Base64;
import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.NetJNI;
import com.billionav.jni.UIBaseConnJNI;


public class PHost {
	
	public static Map<String, String> hostmap = new HashMap();
	private static String		m_sAuthorization = null;
	private static String		m_sUUID = null;
	private static Object       s_cSyncObj		= new Object();
	private static Vector<String> m_vHttpHeader = new Vector<String>();
	
	public static void initialize()
	{
	
		String host_file = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH)+"host.xml";
		PNetLog.d("host.xml File path:"+ host_file);
       
        try {
        	FileInputStream inputStream = new FileInputStream(host_file);
        	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlPullParser parser = factory.newPullParser();
	        parser.setInput(inputStream, "UTF-8");
	        int eventType = parser.getEventType();
	       
	        while (eventType != XmlPullParser.END_DOCUMENT) {
	        	switch (eventType) {
	        	case XmlPullParser.START_DOCUMENT:
	        		break;
	        	case XmlPullParser.START_TAG:
	        		String name = parser.getName();
	        		if (name.equalsIgnoreCase("app"))
	        		{
	        			String hostname = parser.getAttributeValue(null, "id");
	        			String hostaddr = parser.getAttributeValue(null, "preferred-url");
	        			
	        			PHost.AddHost(hostname, hostaddr);
	        			PNetLog.d("SERVER ID:"+ hostname + " URL:"+ hostaddr);
	        		} 
	        		else if(name.equalsIgnoreCase("url-schema"))
	        		{

	        		}
	        		else if(name.equalsIgnoreCase("Authorization"))
	        		{
	        			String user = parser.getAttributeValue(null, "user");
	        			String passwd = parser.getAttributeValue(null, "passwd");
	        			String tmp = user+":"+passwd;
	        			byte[] buffer = tmp.getBytes();
	        			int length = buffer.length;
	        			String authorization = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT | Base64.NO_WRAP);
	        			addCommonHttpHeader("Authorization","Basic " + authorization);
	        		}
	        		else if(name.equalsIgnoreCase("NETLOG"))
	        		{
	        			int level = Integer.parseInt(parser.getAttributeValue(null, "level"));
	        			PNetLog.setLogLevel(level);
	        		}
	        		break;
	        	case XmlPullParser.END_TAG:
	        		break;
	        	}
	        	eventType = parser.next();
	        }
	        inputStream.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	
	public static String getHostAddrbyName(String hostname)
	{
		String hostaddr = hostmap.get(hostname);
		return hostaddr;
	}
	
	public static void AddHost(String hostname, String hostaddr)
	{
		hostmap.put(hostname, hostaddr);
	}
	
	public static void setUUID(String uuid)
	{
		m_sUUID = uuid;
		addCommonHttpHeader("uuid", uuid);
	}

	public static void addCommonHttpHeader(String headname, String headvalue)
	{
		NetJNI.addCommonHttpHeader(headname, headvalue);
		
		UIBaseConnJNI.addCommonHttpHeader(headname, headvalue);
		
		synchronized(s_cSyncObj) {
			int size = m_vHttpHeader.size()/2;
			for(int i = 0; i< size; i++)
			{
				if(m_vHttpHeader.get(i*2) == headname)
				{
					m_vHttpHeader.set(i*2+1, headvalue);
					return;
				}
			}
			m_vHttpHeader.add(headname);
			m_vHttpHeader.add(headvalue);
		}
		
	}
	
	public static Vector<String> getCommonHttpHeader()
	{
		synchronized(s_cSyncObj) {
			return m_vHttpHeader;
		}
	}
	
}
