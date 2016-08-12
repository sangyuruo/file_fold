package com.billionav.navi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class NotificationService extends Service{

	
	private NotificationThread notificationThread;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d("test", "service on create");
		if(null == notificationThread || !notificationThread.isRunning()) {
			notificationThread = new NotificationThread();
			notificationThread.initlize(this);
			notificationThread.startThread();
		}
		registerReceiver(new NotificationReceiver(), new IntentFilter(NotifyActionIDEnum.NOTIFICATION_SERVICE_ACTION));
		super.onCreate();
	}
	
	class NotificationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Bundle data = arg1.getBundleExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG);
			
			if(data == null) {
				Log.d("test", "recevie bundle = null");
				return;
			}
			int action = data.getInt(NotifyActionIDEnum.NOTIFICATION_ENUM_OPERATE_STR_TAG);
			switch(action) 
			{
				case NotifyActionIDEnum.NOTIFICATION_ENUM_ADD_NOTIFICATION:
					NotificateModel model = (NotificateModel)data.getSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG);
					if(model instanceof NotificateModel){
						if(null != notificationThread) {
							notificationThread.addNotification(model);
							broadcastCurrentNotifications();
						}
					}
					break;
				case NotifyActionIDEnum.NOTIFICATION_ENUM_DEL_NOTIFICATION:
					String tobedel = data.getString(NotifyActionIDEnum.NOTIFICATION_ENUM_DEL_STR_TAG);
					if(!TextUtils.isEmpty(tobedel)) {
						if(null != notificationThread) {
							notificationThread.removeNotification(tobedel);
							broadcastCurrentNotifications();
						}
					}
					break;
				case NotifyActionIDEnum.NOTIFICATION_ENUM_REQ_NOTIFICATIONS:
					broadcastCurrentNotifications();

					break;
				case NotifyActionIDEnum.NOTIFICATION_ENUM_MODIFY_NOTIFICATION:
					Log.d("test", "receive modify command");
					NotificateModel modifyModel = (NotificateModel)data.getSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG);
					if(modifyModel instanceof NotificateModel){
						if(null != notificationThread) {
							if(notificationThread.updateNotification(modifyModel)) {
								broadcastCurrentNotifications();
							}
						}
					}
					break;
					default: break;
			}
		}
		
	}

	public void broadcastCurrentNotifications() {
		Bundle b = new Bundle();
		b.putSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_LIST_TAG, notificationThread.getNotifyArray());
		Intent i = new Intent();
		i.setAction(NotifyActionIDEnum.NOTIFICATION_SERVICE_REPLY_ACTION);
		i.putExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, b);
		sendBroadcast(i);
		
	}

}
