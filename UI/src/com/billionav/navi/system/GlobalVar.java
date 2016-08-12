package com.billionav.navi.system;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.os.PowerManager;

public class GlobalVar {
	
	private static ActivityManager sActivityManager = null;
	private static ContentResolver sContentResolver = null;
	private static PowerManager sPowerManager = null;
	
	static public void setActivityManager(ActivityManager am)
	{
		sActivityManager = am;
	}
	
	static public ActivityManager getActivityManager()
	{
		return sActivityManager;
	}

	static public void setContentResolver(ContentResolver cs)
	{
		sContentResolver = cs;
	}
	
	static public ContentResolver getContentResolver()
	{
		return sContentResolver;
	}
	
	static public void setPowerManager(PowerManager pm)
	{
		sPowerManager = pm;
	}
	
	static public PowerManager getPowerManager()
	{
		return sPowerManager;
	}
	
}
