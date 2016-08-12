package com.billionav.navi.net;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.billionav.jni.UIBaseConnJNI;

public class PLogin {
	
	public static final int PLOGIN_STATUS_NO = 0;
	public static final int PLOGIN_STATUS_SUCC = 1;
	public static final int PLOGIN_STATUS_ING = 2;
	public static final int PLOGIN_STATUS_FAILED = 3;
	public static final int PLOGIN_STATUS_OVERDUE = 4;
	
	private static int s_iLoginStatus = PLogin.PLOGIN_STATUS_NO;
	private static String s_sToken = null;
	
	//private static List<NameValuePair> hosts = null; 
	
	public static void setLoginStatus(int status)
	{
		s_iLoginStatus = status;
	}
	
	public static int getLoginStatus()
	{
		return s_iLoginStatus;
	}
	
	public static void setSessionToken(String Token)
	{
		s_sToken = Token;
		UIBaseConnJNI.setSessionToken(Token);
	}
	
	public static void notifyUserLogIn(String userId,String token) {
		UIBaseConnJNI.notifyLogin(userId, token);
	}
	
	public static void notifyUserLogout() {
		UIBaseConnJNI.notifyLogout();
	}
	
	public static String getSessionToken()
	{
		return s_sToken;
	}
	
	public static void setLoginResult(byte[] buf)
	{
    	ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
    	
        try {
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
	        		if(name.equalsIgnoreCase("result")) {
	        			String ret = parser.getAttributeValue(null, "code");
	        			PNetLog.d("RESULT CODE:"+ ret);
	        			if(ret.equalsIgnoreCase("0"))
	        			{
	        				s_iLoginStatus = PLOGIN_STATUS_SUCC;
	        			} else {
	        				s_iLoginStatus = PLOGIN_STATUS_FAILED;
	        			}
	        		}
	        		else if(name.equalsIgnoreCase("token"))
	        		{
	        			String token = parser.getAttributeValue(null, "value");
	        			PNetLog.d("TOKEN:"+ token);
	        			setSessionToken(token);
	        		} 
	        		else if (name.equalsIgnoreCase("app"))
	        		{
	        			String hostname = parser.getAttributeValue(null, "id");
	        			String hostaddr = parser.getAttributeValue(null, "preferred-url");
	        			
	        			PHost.AddHost(hostname, hostaddr);
	        			PNetLog.d("SERVER ID FROM HOST:"+ hostname + " URL:"+ hostaddr);
	        		} 
	        		else if(name.equalsIgnoreCase("url-schema"))
	        		{
	        			//hosts = null;
	        			//hosts = new ArrayList<NameValuePair>();
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
}