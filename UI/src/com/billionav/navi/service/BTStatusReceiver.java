package com.billionav.navi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class BTStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("HybridCN", "BTStatusReceiver : onReceive :  type = "  + intent.getStringExtra("type"));
		Intent btIntent = new Intent();
		btIntent.setAction("com.suntec.iAuto.btspp.navibt2app");
		btIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		if(!TextUtils.isEmpty(intent.getStringExtra("type"))){
			btIntent.setType(intent.getStringExtra("type"));
			context.startActivity(btIntent);
		}
	}

}
