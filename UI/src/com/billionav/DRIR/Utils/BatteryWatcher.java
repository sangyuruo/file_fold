package com.billionav.DRIR.Utils;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryWatcher {
	
	private static final float TEMPRATURE_WARNING = (float) 59.0;
	//private static final int MSG_DISPLAY = 0;
	private static BatteryWatcher sInstance = null;
	
	private static boolean bTempHigh = false;
	
	public static BatteryWatcher CreateInstance(Context context)
	{
		if (null == sInstance)
		{ 
			sInstance = new BatteryWatcher(context);
		}
		 
		return sInstance;
	}
	
	public void RegisterBatteryWatcher()
	{
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(receiver, filter);//注册BroadcastReceiver
        
        //StartDisplayTimer();
	}
	
	public void UnRegisterBatteryWatcher()
	{
		//StopDisplayTimer();
		mContext.unregisterReceiver(receiver);
	}
	
	private Context mContext = null;
	private BatteryReceiver receiver=null;
	//private Timer mTimer = null;
	//private final Handler mHandler = new MainHandler();
	private int mBaPercent = -1;
	private float mTemperature = (float) -999.9;
	private String mBatteryStatus = null;
	private BatteryWatcher(Context context)
	{
		mContext = context;
		receiver=new BatteryReceiver();
	}
	
//	private void StartDisplayTimer()
//	{
//		if (null == mTimer)
//		{
//			mTimer = new Timer();
//			mTimer.schedule(new MyTimerTask(), 200, 3 * 1000);
//		}
//	}
//	
//	private void StopDisplayTimer()
//	{
//		if (null != mTimer)
//		{
//			mTimer.cancel();
//			mTimer = null;
//		}
//	}
//	
//	private void Display()
//	{
//		if ((-1 != mBaPercent)
//				&& (-999.9 != mTemperature)
//				&& (null != mBatteryStatus))
//		{
//			CustomToast.showToast(mContext, "Battery: [" + mBaPercent + "%] "
//					+ "Temp: [" + mTemperature + "] " + mBatteryStatus, Toast.LENGTH_SHORT);
//		}
//	}
	
	public boolean IsDeviceTempHigh()
	{
		return (mTemperature >= TEMPRATURE_WARNING);
	}
	
	private class BatteryReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int current=intent.getExtras().getInt("level");//获得当前电量
            int total=intent.getExtras().getInt("scale");//获得总电量
            mBaPercent = current*100/total;
            mTemperature = (float) ((float)intent.getIntExtra("temperature", 0) / 10.0);               
            
            switch (intent.getIntExtra("status",
                    BatteryManager.BATTERY_STATUS_UNKNOWN)) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
            	mBatteryStatus = "CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
            	mBatteryStatus = "DISCHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            	mBatteryStatus = "NOT_CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
            	mBatteryStatus = "FULL";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
            	mBatteryStatus = "UNKNOWN";
                break;
            }
            if( mBatteryStatus == "CHARGING"){
            	if( !bTempHigh && (mTemperature >= TEMPRATURE_WARNING) ){
                	Log.i("TEMPRATURE", "The temp (" + mTemperature + " ) is higher than " + TEMPRATURE_WARNING);
                	//Send trigger
            		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
            		cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DRIR_TEMPHIGH;
            		cTriggerInfo.m_lParam1 = (long) (mTemperature * 10);
            		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo); 
            		bTempHigh = true;
                }  else if( bTempHigh && (mTemperature < TEMPRATURE_WARNING) ){
                	
            		Log.i("TEMPRATURE", "The temperature become normal");
                	//Send trigger
            		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
            		cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DRIR_TEMPNORMAL;
            		cTriggerInfo.m_lParam1 = (long) (mTemperature * 10);
            		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo); 
            		bTempHigh = false;
                	
                }	
            }            
        }
    }
	
//	private class MyTimerTask extends TimerTask
//	{
//
//		@Override
//		public void run() {
//			mHandler.sendEmptyMessage(MSG_DISPLAY);
//		}
//		
//	}
//	
//	private class MainHandler extends Handler{
//
//		@Override
//		public void handleMessage(Message msg) {
//			
//			switch (msg.what)
//			{
//			case MSG_DISPLAY:
//				Display();
//				break;
//			default:
//				super.handleMessage(msg);
//				break;
//			}
//			
//		}
//	}
}

