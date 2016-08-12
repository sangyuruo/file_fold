package com.billionav.navi.system;

import java.util.*;
import android.app.ActivityManager;
import android.os.Handler;
import android.os.Process;

public class ProcStateMonitor {
	
	static List<ProcStateListener> mListenerList = new ArrayList<ProcStateListener>();
	static private int mInterval = 0;
	static private int mProcState = ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY;
	static private boolean mQuitFlag = false;
	static public int BACKGROUND_STATE = 0;
	static public int FOREGROUND_STATE = 1;
	
	
	static private Handler m_handler = new Handler();
	 
	static private Runnable m_runnable = new Runnable() {  
	     
		public void run() {  
	    	 
			int myPid = Process.myPid();
			
			List<ActivityManager.RunningAppProcessInfo> lProcInfo = GlobalVar.getActivityManager().getRunningAppProcesses();
				
			for(int i = 0;i<lProcInfo.size();i++)
			{
				ActivityManager.RunningAppProcessInfo ProcInfo = lProcInfo.get(i);
				if(myPid == ProcInfo.pid)
				{
					//System.out.println("num:"+i+" "+ ProcInfo.processName+ " " + ProcInfo.importance+ " ");
					setState(ProcInfo.importance);
				}
			}
			
			if(false == mQuitFlag)
			{
				m_handler.postDelayed(this, mInterval); 
			}
		}  
	};
	
	static public void start(int time)
	{
		mQuitFlag = false;
		mInterval = time;
		mListenerList.clear();
		m_handler.postDelayed(m_runnable, mInterval);
	}
	
	static public void stop()
	{
		mQuitFlag = true;
	}
	
	static public void addStateChangeListener(ProcStateListener listener)
	{
		mListenerList.add(listener);
	}
	
	static public void removeStateChangeListener(ProcStateListener listener)
	{
		mListenerList.remove(listener);
		listener.onProcStateChange(0);
	}
	
	static protected void setState(int currentState)
	{
		boolean bChangeFlag = false;
		int state = 0;
		
		if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND != currentState
		&& ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND != currentState)
		{
			return;
		}
		
		if( ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND != mProcState
				  && ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == currentState)
		{
			bChangeFlag = true;
			state = FOREGROUND_STATE;
			
		} 
		else if( ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == mProcState
				&&  ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND != currentState) 
		{
			state = BACKGROUND_STATE;
			bChangeFlag = true;
		}
		else 
		{
			
		}
		
		if( true == bChangeFlag)
		{
			for(int i = 0;i< mListenerList.size();i++)
			{
				ProcStateListener listener = mListenerList.get(i);
				listener.onProcStateChange(state);
			}
			mProcState = currentState;
		}
	}
	
	/*
	protected int GetScreenOffTime()
	{
		int iScreenOffTime = 0;
		try {
			iScreenOffTime = Settings.System.getInt(GlobalVar.GetContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		}	
		catch (Settings.SettingNotFoundException e)
    	{
    		
    	}
		System.out.println("get Screen off time:"+ iScreenOffTime);
		return iScreenOffTime;
	}
	*/
	
	
}
