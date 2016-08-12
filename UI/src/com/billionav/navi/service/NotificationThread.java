package com.billionav.navi.service;

import java.util.ArrayList;
import java.util.Date;

import com.billionav.navi.component.dialog.CustomSelectableDialog;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.uitools.MessageTools;
import com.billionav.ui.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

public class NotificationThread extends Thread{

	
	private ArrayList<NotificateModel> notifyArray;
	private Object m_SyncObj;
	private boolean b_ThreadRunning = false;
	private Context context;
	public NotificationThread(){
		notifyArray = new ArrayList<NotificateModel>();
		m_SyncObj = new Object();
	}
	
	public void startThread() {
		b_ThreadRunning = true;
		start();
	}
	
	
	public boolean isRunning() {
		return b_ThreadRunning;
	}
	
	public ArrayList<NotificateModel> getNotifyArray() {
		return notifyArray;
	}
	
	public boolean addNotification(NotificateModel model) {
		if(null == model){
			return false;
		}
		boolean success = false;
		synchronized (m_SyncObj) {
			success = notifyArray.add(model);
		}
		Log.d("test", "add notification tag "+model.tag+" result " +success);
		return success;
	}
	
	public boolean updateNotification(NotificateModel model) {
		if(null == model){
			return false;
		}
		String tag = model.tag;
		if(TextUtils.isEmpty(tag)) {
			return false;
		}
		if(null == notifyArray) {
			return false;
		}
		int size = notifyArray.size();
		boolean success = false;
		NotificateModel toBeModify = null;
		synchronized (m_SyncObj) {
			for(int i = 0; i < size ; ++i) {
				NotificateModel temp = notifyArray.get(i);
				if( null == temp ) {
					continue;
				}
				if(tag.equals(temp.tag)) {
					toBeModify = temp;
					break;
				}
			}
			if(null != toBeModify) {
				toBeModify.copyFrom(model);
				success = true;
			} else {
				success= false;
			}
		}
		Log.d("test", "modify notification tag "+model.tag+" result " +success);
		return success;
	}
	
	public boolean removeNotification(String tag) {
		if(TextUtils.isEmpty(tag)) {
			return false;
		}
		if(null == notifyArray) {
			return false;
		}
		int size = notifyArray.size();
		NotificateModel toBedel = null; 
		for(int i = 0; i < size ; ++i) {
			NotificateModel model = notifyArray.get(i);
			if( null == model ) {
				return false;
			}
			if(tag.equals(model.tag)) {
				toBedel = model;
				break;
			}
		}
		if(null != toBedel) {
			notifyArray.remove(toBedel);
			return true;
		} else {
			return false;
		}
	}
	
	public void initlize(Context c) {
		context = c;
	}
	
	public void saveToFile() {
		
	}
	
	@Override
	public void run() {
		Log.d("test", "notification thread start b_ThreadRunning = "+b_ThreadRunning);
		while(b_ThreadRunning) {
			
			int size = notifyArray.size();
			if(size > 0) {
				long currentTime = System.currentTimeMillis();
				synchronized (m_SyncObj) {
					Log.d("test", "notification thread start loop");
					NotificateModel toBeDel = null;
					for(int i = 0; i < size; i++) {
						NotificateModel model = notifyArray.get(i);
						if(null == model) {
							continue;
						} else {
							long notifyTime = model.NotificateTime;
							Log.d("test", "notification thread start check current ,set"+currentTime+" "+notifyTime);
							if(currentTime > notifyTime) {
								notificationToScreen(model);
								toBeDel = model;
							}
						}
					}
					if(toBeDel != null) {
						notifyArray.remove(toBeDel);
					}
					Log.d("test", "notification thread finish loop");
				}
			}
			
			try {
				
				Thread.sleep(1000 * 30);
				Log.d("test", "notification thread finish then sleep");
			} catch (InterruptedException e) {
			}
		}
	}

	private final int notifyID = 0x1001;
	private static final String NOTIFICATION_STRING = "System Maintain Information";

	private void notificationToScreen(NotificateModel model) {
		Intent notificationIntent = new Intent(context,
				CustomSelectableDialog.class);
		Bundle data = new Bundle();

		data.putSerializable(
				NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG, model);
		// notificationIntent.putExtra(SYS_NOTIFICATION_KEY, path);
		notificationIntent.putExtra(
				NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, data);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		// notification.contentIntent = contentIntent;
		// mNotificationManager.notify(notifyID , notification);
		MessageTools.Create(context).setPendingIntent(contentIntent);
		String position = "";
		if(null != model) {
			position = model.pointName;
		}
		Date d = new Date(model.NotificateTime);
		String message = context.getString(
				R.string.MSG_DEMO_DIALOG_NOTIFICATION_DEPRATE_DIALOG,
				d.getHours()+":"+d.getMinutes(), position);
		MessageTools.Create(context).sendMessageToSystemBar(notifyID,
				TextUtils.isEmpty(message) ? NOTIFICATION_STRING : message);
		
		unlockPower(message);
	}
	
	private void unlockPower(String message) {
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE); 
		PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.FULL_WAKE_LOCK, "WakeLock"); 
		mWakelock.acquire();
	}

	
	
}
