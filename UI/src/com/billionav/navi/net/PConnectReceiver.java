package com.billionav.navi.net;

import java.util.ArrayList;

import com.billionav.jni.NetJNI;
import com.billionav.jni.UIBaseConnJNI;
import com.billionav.navi.app.AndroidNaviAPP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PConnectReceiver extends 
BroadcastReceiver
{
	public interface NetConnectTypeListener {

		public void onReceive(int type);
	}
	
	public interface NetListener {

		public void onReceive(int type);
	}
	
	public final static int CONNECT_TYPE_NONE = 0;
	public final static int CONNECT_TYPE_WIFI = 1;
	public final static int CONNECT_TYPE_MOBILE = 2;

	@Override
	public void onReceive( Context context, Intent intent ) 
	{
		checkConnectType(context);
		
		synchronized(s_mSyncObj)
		{
			int count = s_mListenerList.size();
			for (int index = 0; index < count; ++index) {
				NetConnectTypeListener listener = s_mListenerList.get(index);
				if (listener != null) {
					listener.onReceive(UIBaseConnJNI.getNetConnStatus());
				}
			}
		}
	}
	
	public static void addListener(NetConnectTypeListener listener)
	{
		synchronized(s_mSyncObj)
		{
			s_mListenerList.add(listener);
		}
	}
	
	public static void removeListener(NetConnectTypeListener listener)
	{
		synchronized(s_mSyncObj)
		{
			s_mListenerList.remove(listener);
		}
	}
	
	
	public static void addListener(NetListener listener)
	{

	}
	
	public static void removeListener(NetListener listener)
	{

	}
	
	public static void initialize()
	{
		Context context = AndroidNaviAPP.getInstance().getApplicationContext();
		checkConnectType(context);
	}
	
	protected static void checkConnectType(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE ); 
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo(); 
		if ( activeNetInfo != null ) 
		{
			PNetLog.e("Active Network Type : " + activeNetInfo.getTypeName()+" "+activeNetInfo.isConnected());
			NetJNI.setNativeLog("Active Network Type : " + activeNetInfo.getTypeName()+" "+activeNetInfo.isConnected());
			if(activeNetInfo.getTypeName().equalsIgnoreCase("WIFI"))
			{
				setConnectType(CONNECT_TYPE_WIFI);
				
			} 
			else 
			{
				setConnectType(CONNECT_TYPE_MOBILE);
			}
		} else {
			setConnectType(CONNECT_TYPE_NONE);
			PNetLog.e("Active Network Type : NULL");
			NetJNI.setNativeLog("Active Network Type : NULL");
		}
	}

	public static int getConnectType()
	{
		return UIBaseConnJNI.getNetConnStatus();
	}
	
	public static void setConnectType(int type)
	{
		UIBaseConnJNI.setNetConnState(type);
	}
	
	private static ArrayList<NetConnectTypeListener> s_mListenerList = new ArrayList<NetConnectTypeListener>();
	private static Object s_mSyncObj = new Object();
	
	
} 