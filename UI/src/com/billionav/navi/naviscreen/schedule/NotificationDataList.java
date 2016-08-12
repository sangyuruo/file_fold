package com.billionav.navi.naviscreen.schedule;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.service.NotificateModel;
import com.billionav.navi.service.NotifyActionIDEnum;
import com.billionav.navi.service.serviceHelper;


public class NotificationDataList {

	private static NotificationDataList instance;
	private ArrayList<NotificateModel> scheduleList;
	private BroadcastReceiver receiver;
	
	
	public int currentPosition = -1;
	
	public void readTestCode() {
		syncScheduleList();
	}
	
	private NotificationDataList() {
		scheduleList = new ArrayList<NotificateModel>();
		receiver = new SyncNotificationReceiver();
		NSViewManager.GetViewManager().registerReceiver(receiver, new IntentFilter(NotifyActionIDEnum.NOTIFICATION_SERVICE_REPLY_ACTION));
	}
	public static NotificationDataList getInstance() {
		if(null == instance) {
			instance = new NotificationDataList();
		}
		return instance;
	}
	
	public void addList(NotificateModel schedule) {
		if(schedule != null) {
			scheduleList.add(schedule);
		}
	}
	
	public NotificateModel getScheduleAtIndex(int idx) {
		if(idx < 0 || idx >= scheduleList.size()) {
			return null;
		}
		return scheduleList.get(idx);
	}
	
	public boolean remoeScheduleAtIndex(int idx) {
		if(idx < 0 || idx >= scheduleList.size()) {
			return false;
		}
		scheduleList.remove(idx);
		return true;
	}
	
	public int getScheduleCount(){
		if(scheduleList != null) {			
			return scheduleList.size();
		}
		return 0;
	}
	
	public void deleteItems() {
		NSViewManager.GetViewManager().unregisterReceiver(receiver);
	}
	
	public void syncScheduleList() {
		
		serviceHelper.sendSyncNotificationBroadcast();
	}
	
	class SyncNotificationReceiver extends BroadcastReceiver{

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Bundle b = arg1.getBundleExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG);
			NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
			cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_NOTIFICATION_LIST_SYNC_RESULT;
			cTriggerInfo.m_lParam1 = 1;
			if(b == null) {
				Log.e("test","sync data form service error, bunlde = null");
			} else {
				ArrayList<NotificateModel> ser = (ArrayList<NotificateModel>)b.getSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_LIST_TAG);
				if(ser == null) {
					Log.e("test","sync data form service error, ser = null");
				} else if(!(ser instanceof ArrayList<?>)) {
					Log.e("test","sync data form service error, ser type error");
				} else {
					scheduleList = (ArrayList<NotificateModel>)ser;
					Log.d("test","sync data form service OK, data length = "+ scheduleList.size());
					cTriggerInfo.m_lParam1 = 0;
				}
			}
			MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
			
		}
		
	}
}
