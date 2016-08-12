package com.billionav.navi.uitools;

import com.billionav.navi.menucontrol.NSViewManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BroadcastSender {


	private static BroadcastSender instance;
	private Context context;
	public static BroadcastSender getInstanc(){
		if(null == instance) {
			instance = new BroadcastSender();
		}
		return instance;
	}
	
	private BroadcastSender() {
		this.context = NSViewManager.GetViewManager();
	}
	public void sendPointBroadcast(byte[] msg) {
		sendBroadcast(GroupIdEnum.BROADCAST_GROUP_ID_POINT, 0, msg);
	}
	
	public void queryBtState() {
		Intent intent = new Intent();
		intent.setAction( "com.suntec.iAuto.btspp.app2navibt" );
		intent.setType( "bt/request_status" );
		context.sendBroadcast(intent);
		Log.d("test","queryBtState sendBroadcast over");
	}
	
	public void sendBroadcast(int groupID, int commandID, String msg) {
		Bundle bundle = new Bundle();
		bundle.putInt( "module_id",  groupID );
        bundle.putInt( "operation_id",  commandID );
        bundle.putByteArray("bt_data", msg.getBytes()); 
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setAction( "com.suntec.iAuto.btspp.app2navibt" );
        intent.setType( "senddata/stream" );
        context.sendBroadcast(intent);
        Log.d("test","sendBroadcast over");
	}
	public void sendBroadcast(int groupID, int commandID, byte[] msg) {
		Bundle bundle = new Bundle();
		bundle.putInt( "module_id",  groupID );
        bundle.putInt( "operation_id",  commandID );
        bundle.putByteArray("bt_data", msg); 
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setAction( "com.suntec.iAuto.btspp.app2navibt" );
        intent.setType( "senddata/stream" );
        context.sendBroadcast(intent);
        Log.d("test","sendBroadcast over");
	}
	
}
