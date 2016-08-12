package com.billionav.navi.system;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.service.NotifyActionIDEnum;
import com.billionav.navi.uitools.BroadcastSender;
import com.billionav.navi.uitools.GroupIdEnum;
import com.billionav.navi.uitools.RouteCalcController;

public class BuleToothMessageQueue {

	
	public class BlueToothMessage{
		public int GroupID;
		public int OperateID;
		public String message;
	}

	private boolean bluetoothConnState = false;
	
	public boolean getBluetoothConnState() {
		return bluetoothConnState;
	}

	public void setBluetoothConnState(boolean bluetoothConnState) {
		this.bluetoothConnState = bluetoothConnState;
	}
	private Object syncObj = new Object();
	private static BuleToothMessageQueue instance;
	private ArrayList<BlueToothMessage> messageQueue;
	
	private BuleToothMessageQueue() {
		messageQueue = new ArrayList<BuleToothMessageQueue.BlueToothMessage>();
	}
	
	public static BuleToothMessageQueue getInstance() {
		if( null == instance ){
			instance = new BuleToothMessageQueue();
			
		}
		return instance;
	}
	
	public static void requestLatestBlueTooth() {
		BroadcastSender.getInstanc().queryBtState();
		
	}

	public void pushMessage(BlueToothMessage msg) {
		synchronized(syncObj){
			messageQueue.add(msg);
		}
	}
	private boolean flag_started = false;
	private Object m_cWaitObj = new Object();
	public Thread executeThread = new Thread(){
		public void run() {
			Log.d("test", "BuleToothMessageQueue run start");
			while(flag_started) {
				if(messageQueue.size() > 0) {
					BlueToothMessage msg = popMessage();
					processMessage(msg);
				} else {
					try {
//						synchronized(m_cWaitObj){
//							m_cWaitObj.wait();
//						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
			Log.d("test", "BuleToothMessageQueue run end");
		}

	};
	
	public void startTask() {
		Log.d("test", "BuleToothMessageQueue start task");
		flag_started = true;
		executeThread.start();
	}
	
	public void stopTask() {
		Log.d("test", "BuleToothMessageQueue stop task");
		flag_started = false;
	}
	
	private void processMessage(BlueToothMessage msg) {
		Log.d("test", "BuleToothMessageQueue processMessage ");
		switch(msg.GroupID){
			case GroupIdEnum.BROADCAST_GROUP_ID_INNER_BLUETOOTH:
				if("bt/connect".equals(msg.message)) {
//					UIBaseConnJNI.sendBtEvent(true);
				} else if("bt/disconnect".equals(msg.message)){
//					UIBaseConnJNI.sendBtEvent(false);
					NSTriggerInfo info = new NSTriggerInfo();
					info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_W3_BT_EVENT;
					MenuControlIF.Instance().TriggerForScreen(info);
				}
				break;
			case GroupIdEnum.BROADCAST_GROUP_ID_INNER_BTSTATE_MSG:
				switch (msg.OperateID) {
					case 0:setBluetoothConnState(false);
						break;
					case 1:setBluetoothConnState(true);
						break;
				}
				break;
			case GroupIdEnum.BROADCAST_GROUP_ID_INNER_NOTIFICATION_REROUTE:
				switch (msg.OperateID) {
					case 0:retroute(msg.message);
						break;
				}
			default:break;
		}
		
	};
	
	private void retroute(String message) {
		if(null == message ){
			Log.e("test", "blueToothMessageQueue msg = null");
			return;
		}
		final String[] poiMessage = message.split(NotifyActionIDEnum.SPILT_TAG);
		try{
			if(null != poiMessage && poiMessage.length == 3) {
				new Handler(Looper.getMainLooper()).post(new Runnable(){
					@Override
					public void run() {
						int orglat = Integer.parseInt(poiMessage[1]);
						int orglon = Integer.parseInt(poiMessage[0]);
						Log.d("test", "reroute lon lat poi:"+orglon+" "+orglat+" "+poiMessage[2]);
						RouteCalcController.instance().rapidRouteCalculateWithData(
								poiMessage[2],orglon, orglat);
					}
				});
			} else {
				Log.e("test", "POI Data ERROR1="+message);
			}
		} catch (Exception e){
			e.printStackTrace();	
			Log.e("test", "POI Data ERROR2="+message);
		} finally {
			
		}		
	}

	public void pushMessage(int groupID, int operateID, String msg) {
		BlueToothMessage btMsg = new BlueToothMessage();
		btMsg.GroupID = groupID;
		btMsg.OperateID = operateID;
		btMsg.message = msg;
		synchronized(syncObj){
			messageQueue.add(btMsg);
		}
		Log.d("test", "BuleToothMessageQueue pushMessage ");
	}
	
	public BlueToothMessage popMessage() {
		BlueToothMessage msg = null;
		synchronized(syncObj){
			if(messageQueue.size() > 0) {
				msg = messageQueue.get(0);
				messageQueue.remove(0);
			}
		}
		Log.d("test", "BuleToothMessageQueue popMessage ");
		return msg;
	}

}
