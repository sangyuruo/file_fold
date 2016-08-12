package com.billionav.navi.luxgenbranch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class setActivityReplaceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("test", "setActivityReplaceReceiver");
//		MenuControlIF.Instance().addChileReplaceParentPair(ADT_Top_Menu_Luxgen.class, ADT_Top_Menu.class);
//		MenuControlIF.Instance().addChileReplaceParentPair(ADT_Openning_Luxgen.class, ADT_Openning.class);
	}

	
}
