package com.billionav.navi.uitools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.UIC_SCM_POIReqParam;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.navi.naviscreen.schedule.ScheduleModel;
import com.billionav.ui.R;

public class HybridUSTools implements TriggerListener{
	
	public static int START_TIME_TEN_MINUE = 0;
	public static int START_TIME_HALF_HOUR = 1;
	public static int START_TIME_AN_HOUR = 2;
	
	public static int TIME_TEN_MINUE = 10 * 60;
	public static int TIME_HALF_HOUR = 30 * 60;
	public static int TIME_AN_HOUR = 60 * 60;

	public static int ONE_MINUE = 60;
	public static int TIME_OFF_SET = 40;// The units are seconds
	public static int TIME_CRITICAL = TIME_OFF_SET - ONE_MINUE;
	
	public static int HANDLER_MESSAGE_CHECK_SCHEDULE = 0;
	public static int HANDLER_MESSAGE_INIT_ROUTE_ETA = 1;
	public static int HANDLER_MESSAGE_CONTINUE_CALC_ROUTE = 2;
	public static int HANDLER_MESSAGE_TEST_CODE = 3;
	
	public static int DIALOG_MESSAGE_TYPE_OFFSET_TIME = 0;
	public static int DIALOG_MESSAGE_TYPE_SHOULD_GO = 1;
	public static int DIALOG_MESSAGE_TYPE_OVERDUE = 2;
	public static int DIALOG_MESSAGE_TYPE_OHTER = 3;
	
	private static String CALENDAR_EVENT_URL = "content://com.android.calendar/events";
	
	public boolean isSyncCalendarData = false;
	
	ScheduleModel model = null;
	
	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static HybridUSTools instance = null;

	CustomDialog dialog = new CustomDialog(NSViewManager.GetViewManager());

	public static HybridUSTools getInstance() {
		if (null == instance) {
			instance = new HybridUSTools();
		}
		return instance;
	}
	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_INTENT_DEPARTURE_TIME_NOTIFY){
			//do 
		}
		
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED) {
//			Log.d("test", "trigger id = "+triggerInfo.m_iTriggerID+" return ");
			return false;
		}		
		Log.d("HybridUS",
				"receive search callback value search type = " + triggerInfo.m_lParam1 + "; error type = " + triggerInfo.m_lParam2);
		if(triggerInfo.m_lParam2 != 0) {
			if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI){
				currentSearchLocIndex ++;
				reqPinPointsLonLat();
			}
			Log.d("test", "OnPressBtn : error type = " + SearchTools.getErrorType((int)triggerInfo.m_lParam2));
		}
		
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		
		if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
			if(searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) == 0) {
				currentSearchLocIndex ++;
				reqPinPointsLonLat();
				Log.d("test", "OnPressBtn :request name find nothing");
				Log.d("HybridUS", "OnPressBtn :request name find nothing");
			} else {
				if(currentSearchLocIndex < 0) {
				} else {
					int size = ScheduleDataList.getInstance().getScheduleCount();
					if(currentSearchLocIndex < size){
						ScheduleModel model = ScheduleDataList.getInstance().getScheduleAtIndex(currentSearchLocIndex);
						long []lonlat = searchResult.GetPOIListItemLonLatAt(0,  UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
						Log.d("HybridUS",
								"receive search callback value currentSearchLocIndex : "
										+ currentSearchLocIndex + " lon = "
										+ lonlat[0] + " ; lat = " + lonlat[1]);
						model.lon = (int)lonlat[0];
						model.lat = (int)lonlat[1];
						ScheduleDataList.getInstance().saveScheduleList();
					}
					currentSearchLocIndex ++;
				}
				reqPinPointsLonLat();
			}
		}
			

		return true;
	}
	
	
	public void checkArrivalStates() {
		if(scheduleHandler.hasMessages(HANDLER_MESSAGE_CHECK_SCHEDULE)){
			scheduleHandler.removeMessages(HANDLER_MESSAGE_CHECK_SCHEDULE);
		}
		scheduleHandler.sendEmptyMessage(HANDLER_MESSAGE_CHECK_SCHEDULE);
		Log.d("test", "start call checkArrivalStates");
		Log.d("HybridUS", "start call checkArrivalStates");
	}
	
	private Handler scheduleHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int count = ScheduleDataList.getInstance().getScheduleCount();
			if (msg.what == HANDLER_MESSAGE_CHECK_SCHEDULE) {
				for (int i = 0; i < count; i++) {
					model = ScheduleDataList.getInstance()
							.getScheduleAtIndex(i);
					int actualToArrivalTime = model.actualToArrivalTime;
					String exceptToArriveTime = model.exceptToArriveTime;
					if (actualToArrivalTime > 0
							&& isNotEmpty(exceptToArriveTime)) {
						
						int type = isNeedPromptScheduleDialog(actualToArrivalTime,exceptToArriveTime);
						Log.d("HybridUS", "checkPromptScheduleDialog : i = " + i + " ; type = " + type);
						if (type == DIALOG_MESSAGE_TYPE_SHOULD_GO || type == DIALOG_MESSAGE_TYPE_OFFSET_TIME) {
//							if(scheduleHandler.hasMessages(HANDLER_MESSAGE_CHECK_SCHEDULE)){
//								scheduleHandler.removeMessages(HANDLER_MESSAGE_CHECK_SCHEDULE);
//							}
							Context c = NSViewManager.GetViewManager();
							String tempMessage = null;
							if(type == DIALOG_MESSAGE_TYPE_SHOULD_GO){
								tempMessage = c.getString(R.string.STR_COM_046);
							}else{
								tempMessage = c.getString(R.string.STR_COM_052);
							}
							String message = String.format(tempMessage, model.pointName);
							if (isBackground(c)) {
								sendNotification(c , message);
							}
							if(dialog.isShowing()){
								dialog.dismiss();
							}
							showScheduleDialog(R.string.STR_COM_045, message,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											RouteCalcController
													.instance()
													.rapidRouteCalculateWithData(
															model.pointName,
															model.lon,
															model.lat, null,
															null);
//											model.exceptToArriveTime = "";
//											scheduleHandler.sendEmptyMessage(HANDLER_MESSAGE_CHECK_SCHEDULE);
											ScheduleDataList.getInstance()
													.saveScheduleList();
										}
									}, null
							);
							break;
						} else if(type == DIALOG_MESSAGE_TYPE_OVERDUE){
							model.validTime = false;
						} else{
							//do nothing
						}
					}
				}
//				if (dialog == null) {
//					if(scheduleHandler.hasMessages(HANDLER_MESSAGE_CHECK_SCHEDULE)){
//						scheduleHandler.removeMessages(HANDLER_MESSAGE_CHECK_SCHEDULE);
//					}
					scheduleHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_CHECK_SCHEDULE, 30000);
//				} else if(!dialog.isShowing()){
//					if(scheduleHandler.hasMessages(HANDLER_MESSAGE_CHECK_SCHEDULE)){
//						scheduleHandler.removeMessages(HANDLER_MESSAGE_CHECK_SCHEDULE);
//					}
//					scheduleHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_CHECK_SCHEDULE, 30000);
//				}
			} else if (msg.what == HANDLER_MESSAGE_INIT_ROUTE_ETA) {
				if (count > 0) {
					if(getRouteInfoTask.getInstance().doScheduleTask(0) != 0){
						scheduleHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_INIT_ROUTE_ETA, 3000);
					}
				}
			} else if (msg.what == HANDLER_MESSAGE_CONTINUE_CALC_ROUTE) {
				
			} else if (msg.what == HANDLER_MESSAGE_TEST_CODE){
				if (isBackground(NSViewManager.GetViewManager())) {
					sendNotification(NSViewManager.GetViewManager() 
							,String.format(NSViewManager.GetViewManager().getString(R.string.STR_COM_046), "sunlong"));
				}
			}
		}
	};

	public boolean isNotEmpty(String exceptToArriveTime) {
		if (TextUtils.isEmpty(exceptToArriveTime)) {
			return false;
		} else if (exceptToArriveTime.equals("-")) {
			return false;
		}
		return true;
	}
	
	public static int[] getTimeFromString(String inputTime){
		int[] time = new int[5] ;
		String[] time1 = inputTime.split(" ");
		String[] time2 = time1[0].split("-");
		String[] time3 = time1[1].split(":");
		
		time[0] = Integer.parseInt(time2[0]);
		time[1] = Integer.parseInt(time2[1]);
		time[2] = Integer.parseInt(time2[2]);
		time[3] = Integer.parseInt(time3[0]);
		time[4] = Integer.parseInt(time3[1]);
		return time;
	}

	private void showScheduleDialog(int title, String message,
			DialogInterface.OnClickListener pos,
			DialogInterface.OnClickListener neg) {
		dialog.setTitle(NSViewManager.GetViewManager().getString(title));
		dialog.setMessage(message);
		dialog.setPositiveButton(R.string.MSG_00_00_00_11, pos);
		dialog.setNegativeButton(R.string.MSG_00_00_00_12, neg);
		dialog.show();
	}

	public void addListenerToGloble() {
//		ScheduleDataList.getInstance().initScheduleList();
//		Log.d("test", "get calendar data before : " + df.format(new Date()));
//		readCalendar(NSViewManager.GetViewManager() , false);
//		GlobalTrigger.getInstance().addTriggerListener(this);
//		reqPinPointsLonLat();
//		Log.d("test", "get calendar data after : " + df.format(new Date()));
		
//		scheduleHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_TEST_CODE, 10000);
	}

	int currentSearchLocIndex = -1;
	private void reqPinPointsLonLat() {
		Log.d("test", "~~~~~~~~~~~11111111");
		if(currentSearchLocIndex < 0){
			if(isSyncCalendarData){
				sendTrigger2Activity();
			}else{
				scheduleHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_INIT_ROUTE_ETA, 2000);
			}
			return;
		}
		Log.d("test", "~~~~~~~~~~~22222222");
		int size = ScheduleDataList.getInstance().getScheduleCount();
		Log.d("test", "~~~~~~~~~~~schedule list count = "+ size);
		for(; currentSearchLocIndex < size; ++currentSearchLocIndex) {
			ScheduleModel model = ScheduleDataList.getInstance().getScheduleAtIndex(currentSearchLocIndex);
			if(model!= null && model.lon == 0 && model.lat == 0) {
				Log.d("HybridUS", "reqCalendarPointLonLat : currentSearchLocIndex "
						+ currentSearchLocIndex + " ; pointName = "
						+ model.pointName);
				reqPinPointLonLat(model.pointName);
				break;
			}
		}
		if(currentSearchLocIndex == size) {
			currentSearchLocIndex = -1;
			if(isSyncCalendarData){
				sendTrigger2Activity();
			}else{
				scheduleHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_INIT_ROUTE_ETA, 2000);
			}
//			GlobalTrigger.getInstance().removeTriggerListener(this);
		}
		
	}
	private void reqPinPointLonLat(String s) {
		Log.d("test", "req pin lon lat by name");
		UIC_SCM_POIReqParam param = new UIC_SCM_POIReqParam();
		param.type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD;
		param.btn_id = UISearchControlJNI.UIC_SCM_BTN_ID_SEARCH;
		param.act_id = UISearchControlJNI.UIC_SCM_ACT_ID_NORMAL;
//		if(UIMapControlJNI.GetCarPositonMode()){
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CAR;
//		}else{
//			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CURSOR;
//		}
		param.nettype = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
//		param.nettype = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
		param.keyword = s.trim();
		
		
		Log.d("test", "serach poi lonlat :"+param.keyword);
		UISearchControlJNI.Instance().OnPressBtn(param);
		
	}
	
	public void readCalendar(Context context , boolean sync) {
		Cursor eventCursor = context.getContentResolver().query(
				Uri.parse(CALENDAR_EVENT_URL), null, null, null, null);
		if(null != eventCursor) {
			int count = eventCursor.getCount();
			while (count > 0 && eventCursor.moveToNext()) {
				int m_titleIndex = eventCursor.getColumnIndex("title");
				int m_lonIndex = eventCursor.getColumnIndex("longitude");
				int m_latIndex = eventCursor.getColumnIndex("latitude");
				int m_eventLocationIndex = eventCursor.getColumnIndex("eventLocation");
				
				int m_dtstartIndex = eventCursor.getColumnIndex("dtstart");
				int m_dtendIndex = eventCursor.getColumnIndex("dtend");
				
				String eventLocation = null;

				if(m_eventLocationIndex >= 0) {
					eventLocation = eventCursor.getString(m_eventLocationIndex);
				} 
				if(eventLocation == null && m_titleIndex >=0) {
					eventLocation = eventCursor.getString(m_titleIndex);
				}
//				else {
//					Log.d("test", "name & loc all null 1, drop");
//					continue;
//				}
				if(null == eventLocation) {
					Log.d("test", "name & loc all null 2, drop");
					continue;	
				}
				long lon = 0;
				if(m_lonIndex >= 0) {
					lon = eventCursor.getLong(m_lonIndex);
				} else {
					
				}
				long lat = 0;
				if(m_latIndex >= 0) {
					lat = eventCursor.getLong(m_latIndex);
				} else {
					
				} 
				if(lat == 0 && lon == 0) {
					currentSearchLocIndex = 0;
				}
				
				String calendarDate = null;
				if(m_dtstartIndex >= 0){
					calendarDate = getFormatDate(eventCursor.getString(m_dtstartIndex));
				}
				
				if(TextUtils.isEmpty(calendarDate)){
					if(m_dtendIndex >= 0){
						calendarDate = getFormatDate(eventCursor.getString(m_dtendIndex));
					}
				}
				
				Log.d("HybridUS", "position = " + eventCursor.getPosition()
						+ " ; eventTitle = " + eventLocation + " ; lon = " + lon
						+ " ; lat = " + lat + " ; calendarDate = " + calendarDate);
				if(!TextUtils.isEmpty(eventLocation)){
					if(!isTheSamePoint(lon , lat, eventLocation)){
						ScheduleModel schedule = new ScheduleModel();
						schedule.pointName = eventLocation;
						schedule.lon = lon;
						schedule.lat = lat;
						if(isVaildDate(calendarDate)){
							schedule.exceptToArriveTime = calendarDate;
						}
						ScheduleDataList.getInstance().addList(schedule);
					}
				}
			}
			if(sync){
				GlobalTrigger.getInstance().addTriggerListener(this);
				isSyncCalendarData = sync;
				reqPinPointsLonLat();
			}
			eventCursor.close();
		}
	}
	
	private void sendTrigger2Activity() {
		NSTriggerInfo triggerInfo = new NSTriggerInfo();
		triggerInfo.SetTriggerID(NSTriggerID.UIC_MN_TRG_INTENT_SYNC_CALENDAR_CALLBACK);
		MenuControlIF.Instance().TriggerForScreen(triggerInfo);
	}
	
	private boolean isVaildDate(String calendarDate){
		if(TextUtils.isEmpty(calendarDate)){
			return false;
		}
		Calendar c = Calendar.getInstance();
		try {
			Date setData = df.parse(calendarDate);
			Date currentData = df.parse(df.format(c.getTime()));
			long diff = setData.getTime() - currentData.getTime();
			if(diff > 0){
				return true;
			}else{
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String getFormatDate(String time){
		if(TextUtils.isEmpty(time)){
			return "";
		}
		Calendar c = Calendar.getInstance(); 
		Date date = new Date();
		date.setTime(Long.parseLong(time));
		c.setTime(date);
		return df.format(c.getTime());
	}
	
	private boolean isTheSamePoint(long lon , long lat, String name){
		if(TextUtils.isEmpty(name) ){
			return true;
		}

		int count = ScheduleDataList.getInstance().getScheduleCount();

		for (int i = 0; i < count; i++) {
			ScheduleModel mode = ScheduleDataList.getInstance().getScheduleAtIndex(i);
			if(name.equals(mode.pointName)){
				return true;
			}
//			if(mode.lon == lon && mode.lat == lat){
//				return true;
//			}

		}
		return false;
	}

	public boolean isTheSamePoint(long lon, long lat) {
		int count = ScheduleDataList.getInstance().getScheduleCount();
		for (int i = 0; i < count; i++) {
			ScheduleModel mode = ScheduleDataList.getInstance().getScheduleAtIndex(i);
			if(mode.lon == lon && mode.lat == lat){
				return true;
			}
		}
		return false;
	}
	
	public int isNeedPromptScheduleDialog(int actualToArrivalTime,
			String exceptToArriveTime) {
		Calendar c = Calendar.getInstance();
		try {
			Date setData = df.parse(exceptToArriveTime);
			Date currentData = df.parse(df.format(c.getTime()));
			long diff = setData.getTime() - currentData.getTime();
			long seconds = diff / 1000;
			Log.d("HybridUS", "actualToArrivalTime = "
					+ actualToArrivalTime + "; diff seconds = " + seconds);
			long diffSeconds = seconds - actualToArrivalTime;
			if (diffSeconds < TIME_CRITICAL) {
				return DIALOG_MESSAGE_TYPE_OVERDUE;
			} else if(diffSeconds >= TIME_CRITICAL && diffSeconds < TIME_OFF_SET){
				return DIALOG_MESSAGE_TYPE_SHOULD_GO;
			} else if(diffSeconds >= TIME_OFF_SET && diffSeconds < TIME_OFF_SET + ONE_MINUE){
				return DIALOG_MESSAGE_TYPE_OFFSET_TIME;
			} else {
				return DIALOG_MESSAGE_TYPE_OHTER;
			}
		} catch (Exception e) {
			return DIALOG_MESSAGE_TYPE_OHTER;
		}
	}

	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.d("test", "Backstage : " + appProcess.processName);
					return true;
				} else {
					Log.i("test", "Reception : " + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	public static void sendNotification(Context context , String message) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification n = new Notification();
		n.icon = R.drawable.navicloud_and_002b;
		n.tickerText = message;
		n.when = System.currentTimeMillis();
		n.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(context, NaviViewManager.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		n.setLatestEventInfo(context, "Schedule", message, pi);
		notificationManager.notify(1, n);
		
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE); 
		PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.FULL_WAKE_LOCK, "WakeLock"); 
		mWakelock.acquire();
	}




}
