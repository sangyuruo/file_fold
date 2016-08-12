package com.billionav.navi.service;

import java.util.List;

import com.billionav.navi.menucontrol.NSViewManager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;

public class serviceHelper extends BroadcastReceiver{

	public static boolean startService() {
		if(!isNotificationServiceRunning()) {
			Context c = NSViewManager.GetViewManager();
			c.startService(new Intent(c, NotificationService.class));
			return true;
		}
		return false;
	}
	
	public static boolean isNotificationServiceRunning() {
		return isServiceRunning(NSViewManager.GetViewManager(), NotificationService.class.getName());
	}

	public static void addNotificationToService(int lon, int lat, long notificationTime, String pointName, String tag) {
		NotificateModel mode = new NotificateModel();
		addNotificationToService(mode);
	}
	
	public static void sendSyncNotificationBroadcast() {
		Bundle b = new Bundle();
		
		b.putInt(NotifyActionIDEnum.NOTIFICATION_ENUM_OPERATE_STR_TAG, NotifyActionIDEnum.NOTIFICATION_ENUM_REQ_NOTIFICATIONS);
		Intent i = new Intent();
		i.putExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, b);
		i.setAction(NotifyActionIDEnum.NOTIFICATION_SERVICE_ACTION);
		NSViewManager.GetViewManager().sendBroadcast(i);
	}
	
	public static void addNotificationToService(NotificateModel model) {
		Bundle b = new Bundle();
		b.putSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG, model);
		b.putInt(NotifyActionIDEnum.NOTIFICATION_ENUM_OPERATE_STR_TAG, NotifyActionIDEnum.NOTIFICATION_ENUM_ADD_NOTIFICATION);
		Intent i = new Intent();
		i.putExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, b);
		i.setAction(NotifyActionIDEnum.NOTIFICATION_SERVICE_ACTION);
		NSViewManager.GetViewManager().sendBroadcast(i);
	}
	
	public static void cancelNotificationToService(String tag) {
		Bundle b = new Bundle();
		b.putInt(NotifyActionIDEnum.NOTIFICATION_ENUM_OPERATE_STR_TAG, NotifyActionIDEnum.NOTIFICATION_ENUM_DEL_NOTIFICATION);
		b.putString(NotifyActionIDEnum.NOTIFICATION_ENUM_DEL_STR_TAG, tag);
		Intent i = new Intent();
		i.putExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, b);
		i.setAction(NotifyActionIDEnum.NOTIFICATION_SERVICE_ACTION);
		NSViewManager.GetViewManager().sendBroadcast(i);
	}
	

	public static void modifyNotificationToService(NotificateModel currentModel) {
		Bundle b = new Bundle();
		b.putSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG, currentModel);
		b.putInt(NotifyActionIDEnum.NOTIFICATION_ENUM_OPERATE_STR_TAG, NotifyActionIDEnum.NOTIFICATION_ENUM_MODIFY_NOTIFICATION);
		Intent i = new Intent();
		i.putExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, b);
		i.setAction(NotifyActionIDEnum.NOTIFICATION_SERVICE_ACTION);
		NSViewManager.GetViewManager().sendBroadcast(i);
	}
	
	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	private static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(50);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		String strAction = arg1.getAction();
		if("android.intent.action.BOOT_COMPLETED".equals(strAction)) {
			startService();
		} else if("android.intent.action.ACTION_SHUTDOWN".equals(strAction)) {
			
		}
		
		
	}


}
