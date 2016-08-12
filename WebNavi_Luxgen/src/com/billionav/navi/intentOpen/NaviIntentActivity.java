package com.billionav.navi.intentOpen;


import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.service.NotificateModel;
import com.billionav.navi.service.NotifyActionIDEnum;
import com.billionav.navi.system.BuleToothMessageQueue;
import com.billionav.navi.uitools.GroupIdEnum;

public class NaviIntentActivity extends Activity{
	private static final String TAG = "IntentCall";
	private PowerManager.WakeLock wakeLock;
	private PowerManager pm;
	private KeyguardLock mKeyguardLock = null; 
	KeyguardManager mKeyguardManager = null;   

	@Override
	protected void onResume() {
		super.onResume();
		Intent i = getIntent();
		String type = i.getType();
		if(!TextUtils.isEmpty(type)) {
			try{
//				if(type.equals("bt/disconnect")) {
//					//获取电源的服务
//					try{
//						pm = (PowerManager) getSystemService(POWER_SERVICE);
//						//获取系统服务
//						mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//						wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
//						wakeLock.acquire();
//						Log.i("Log : ", "------>mKeyguardLock");
//						//初始化键盘锁，可以锁定或解开键盘锁
//						mKeyguardLock = mKeyguardManager.newKeyguardLock("");   
//						//禁用显示键盘锁定
//						mKeyguardLock.disableKeyguard();   
//					} catch (Exception e) {
//					}
//					
//					Intent intent = new Intent(NaviIntentActivity.this, com.billionav.navi.naviscreen.NaviViewManager.class);
//					startActivity(intent);
//				} 
				processMessage(i);
			} catch (Exception e){
				new CustomDialog(this).setMessage("URI ERROR, the program can not start");
				Log.e(TAG, "intent error");
			} finally {
				this.finish();
				if(wakeLock != null) {
					wakeLock.release();
				}
			}
		}
	}

	
	public void startAcitvity() {
		try{
			pm = (PowerManager) getSystemService(POWER_SERVICE);
			//获取系统服务
			mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
			wakeLock.acquire();
			Log.i("Log : ", "------>mKeyguardLock");
			//初始化键盘锁，可以锁定或解开键盘锁
			mKeyguardLock = mKeyguardManager.newKeyguardLock("");   
			//禁用显示键盘锁定
			mKeyguardLock.disableKeyguard();   
		} catch (Exception e) {
		}
		
		Intent intent = new Intent(NaviIntentActivity.this, com.billionav.navi.naviscreen.NaviViewManager.class);
		startActivity(intent);
	}
	
	
	private void processMessage(Intent intent) {
		String type = intent.getType();
		Log.d("test", "receive intent process Message");
		if(!TextUtils.isEmpty(type)) {
			Log.d("test","intent type = " + type);
			if(type.equals("bt/disconnect")) {
				startAcitvity();
				BuleToothMessageQueue.getInstance().pushMessage(GroupIdEnum.BROADCAST_GROUP_ID_INNER_BLUETOOTH, 0, type);
				BuleToothMessageQueue.getInstance().pushMessage(GroupIdEnum.BROADCAST_GROUP_ID_INNER_BTSTATE_MSG, 0,type);
			} 
			else if (type.equals("bt/connect")) {
				BuleToothMessageQueue.getInstance().pushMessage(GroupIdEnum.BROADCAST_GROUP_ID_INNER_BTSTATE_MSG, 1,type);
			} else if(type.equals("bt/respond_connect")) {
				BuleToothMessageQueue.getInstance().pushMessage(GroupIdEnum.BROADCAST_GROUP_ID_INNER_BTSTATE_MSG, 1,type);
			} else if(type.equals("bt/respond_disconnect")) {
				BuleToothMessageQueue.getInstance().pushMessage(GroupIdEnum.BROADCAST_GROUP_ID_INNER_BTSTATE_MSG, 0,type);
			} else if (type.equals("receiveddata/stream")) {
				
			} else if (type.equals("notify/requestPath")) {
				startAcitvity();
				Bundle b = intent.getBundleExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG);
				NotificateModel model = null;
				if(null != b) {
					model = (NotificateModel)b.getSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG);
				}
				String ret = null;
				if(null != model) {
					ret = model.lon + NotifyActionIDEnum.SPILT_TAG + model.lat + NotifyActionIDEnum.SPILT_TAG + model.pointName;
				}
				if(null != ret) {
					BuleToothMessageQueue.getInstance().pushMessage(GroupIdEnum.BROADCAST_GROUP_ID_INNER_NOTIFICATION_REROUTE, 0,ret);
				} else {
					Log.e("test", "notification data is null, program will not reroute");
				}
			}
		} else {
			Log.e("test", "type is null");
		}
	}
}
