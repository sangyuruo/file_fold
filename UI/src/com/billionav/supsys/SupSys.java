/**
 * 
 */
package com.billionav.supsys;

import com.billionav.supsys.DumpMode;
import com.billionav.supsys.JErrorReporter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author zhuangyao
 *
 */
public class SupSys {
	static private final JErrorReporter JR= new JErrorReporter();
	static private final CErrorReporter CR= new CErrorReporter();

	public static void init (Context context){
		if (DumpMode.isDumpModeOn()) {
			//start service for system crash dump
			Intent intent= new Intent();
			intent.setClass(context, SupportService.class);
			Log.i("Supsys", "start service");
			ComponentName name=context.startService(intent);
			if (name==null) {
				Log.e("Supsys", "service fail to start");
			}

			//initialize java first,to watch java crashes 
			JR.init(context);

			//initialize c crashes
			CR.init(context);
		}
	}
}
