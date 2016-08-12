package com.billionav.navi.system;

import android.os.PowerManager;

public class ScreenOffManager implements ProcStateListener{
	
	private boolean bRelease = true;
	private PowerManager.WakeLock mWL = null;

	public ScreenOffManager()
	{
		mWL = GlobalVar.getPowerManager().newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MY WL");
	}
	
	public void onProcStateChange(int state) {
		// TODO Auto-generated method stub
		
		if(state == ProcStateMonitor.FOREGROUND_STATE)
		{
			if( true == bRelease ) 
			{
				//System.out.println("=====================ScreenOffManager LOCK===================");
				mWL.acquire();
				bRelease = false;
			}
		} else {
			if( false == bRelease ) 
			{
				//System.out.println("=====================ScreenOffManager RELEASE===================");
				mWL.release();
				bRelease = true;
			}
		}
	}

}
