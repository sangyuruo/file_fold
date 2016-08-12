package com.billionav.navi.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;




public class SDCardListener {

	static private IntentFilter mIntentFilter = null ;

	static private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("======MEDIA_EJECT==============");
		}
	};

	
	static public IntentFilter getIntentFilter() {
		if (null == mIntentFilter)
		{
			mIntentFilter = new IntentFilter();
			mIntentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
			mIntentFilter.addDataScheme("file");
		}
		return mIntentFilter;
	};
	
	static public BroadcastReceiver getBroadcastReceiver() {
		
		return mReceiver;
	};

}


