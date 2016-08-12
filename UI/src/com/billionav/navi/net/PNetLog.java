package com.billionav.navi.net;

import android.util.Log;

public class PNetLog 
{
	
	public static final int LOG_LEVEL_ERROR = 0;
	public static final int LOG_LEVEL_DEBUG = 1;
	public static final int LOG_LEVEL_INFO 	= 2;
	public static final int LOG_LEVEL_VERBOSE = 3;
	public static final int LOG_LEVEL_WARN 	  = 4;
 
	private static int s_iLevel = -1;		
	private static String tag = "NET";
	
	public static void setLogLevel(int iLevel)
	{
		s_iLevel = iLevel;
	}
	
	//Send an debug log message.
	public static int	 d(String msg)
	{
		if(s_iLevel >= LOG_LEVEL_DEBUG)
		{
			return Log.d(tag,msg);
		}
		return -1;
	}
	
	//Send an ERROR log message.
	public static int	 e(String msg) 
	{
		if(s_iLevel >= LOG_LEVEL_ERROR)
		{
			return Log.e(tag, msg);
		}
		return -1;
	}

	//Send an INFO log message.
	public static int	 i(String msg)
	{
		if(s_iLevel >= LOG_LEVEL_INFO)
		{
			return Log.i(tag, msg);
		}
		return -1;
	}

	//Send a VERBOSE log message.
	public static int	 v(String msg) 
	{
		if(s_iLevel >= LOG_LEVEL_VERBOSE)
		{
			return Log.v(tag, msg);
		}
		return -1;
	}

}
