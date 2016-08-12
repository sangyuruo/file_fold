package com.billionav.navi.naviscreen.schedule;

import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Log;

public class ScheduleDataList {

	private static ScheduleDataList instance;
	private ArrayList<ScheduleModel> scheduleList;
	
	
	private static final String MSG_SPILT_TAG = "\n";
	private static final String ARG_SPILT_TAG = "##";
	private static final String EMPTY_TAG = "-";
	
	public int currentPosition = -1;
	
	public void readTestCode() {
		initScheduleList();
	}
	
	private ScheduleDataList() {
		scheduleList = new ArrayList<ScheduleModel>();
	}
	public static ScheduleDataList getInstance() {
		if(null == instance) {
			instance = new ScheduleDataList();
		}
		return instance;
	}
	
	public void addList(ScheduleModel schedule) {
		if(schedule != null) {
			scheduleList.add(schedule);
			saveScheduleList();
		}
	}
	
	public ScheduleModel getScheduleAtIndex(int idx) {
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
		saveScheduleList();
		return true;
	}
	
	public int getScheduleCount(){
		if(scheduleList != null) {			
			return scheduleList.size();
		}
		return 0;
	}
	
	public void initScheduleList() {
		new Thread(){
			@Override
			public void run() {
				if(null == scheduleList) {
					return;
				}
				scheduleList.clear();
				String msgs = ScheduleFileOperate.getInstance().readSchedule();
				if(null != msgs) {
					String[] Schedules = msgs.split(MSG_SPILT_TAG);
					if(null != Schedules) {
						for(int i = 0; i < Schedules.length; ++i) {
							ScheduleModel model = new ScheduleModel();
							String[] msg = Schedules[i].split(ARG_SPILT_TAG);
							if(msg != null && msg.length == ScheduleModel.ARG_NUM) {
								for(int j = 0; j < msg.length; ++j){
									Log.d("test", "msg[j]="+msg[j]);
									if(!EMPTY_TAG.equals(msg[j])) {
										String arg = msg[j];
										switch(j) {
											case ScheduleModel.ARG_LON:
												try{
													model.lon = Long.parseLong(arg);
												}catch (Exception e){
													
												}
												break;
											case ScheduleModel.ARG_LAT:
												try{
													model.lat = Long.parseLong(arg);
												}catch (Exception e){
													
												}
												break;
											case ScheduleModel.ARG_POINTNAME:
												model.pointName = arg;
												break;
											case ScheduleModel.ARG_SCHEDULE_TITLE:
												model.ScheduleTitle = arg;
												break;
											case ScheduleModel.ARG_EXCEPT_TO_ARRIVE_TIEM:
												model.exceptToArriveTime = arg;
												break;
												default:break;
										}
									}
								}
							}
							scheduleList.add(model);
						}
					}
				}
				for(int i=0; i<scheduleList.size(); ++i) {
					ScheduleModel mod = scheduleList.get(i);
					Log.d("HybridUS", "schedule "+mod.lon+" "+mod.lat+" "+mod.pointName+" "+mod.exceptToArriveTime+"!!!!");
				}
			}
		}.start();
		
	}
	
	public void saveScheduleList() {
		new Thread(){
			@Override
			public void run() {
				int size = scheduleList.size();
				if(size <= 0){
					ScheduleFileOperate.getInstance().deleteEmptyFile();
					return;
				}
				StringBuffer content = new StringBuffer();
				for(int i=0; i<size; ++i) {
					ScheduleModel model = scheduleList.get(i);
					if(null != model) {
						StringBuffer tmp = new StringBuffer();
						tmp.append(model.lon+ARG_SPILT_TAG);
						tmp.append(model.lat+ARG_SPILT_TAG);
						if(TextUtils.isEmpty(model.pointName)) {
							tmp.append(EMPTY_TAG  +ARG_SPILT_TAG);
						} else {
							tmp.append(model.pointName+ARG_SPILT_TAG);
						}
						if(TextUtils.isEmpty(model.ScheduleTitle)) {
							tmp.append(EMPTY_TAG + ARG_SPILT_TAG);
						} else {
							tmp.append(model.ScheduleTitle+ARG_SPILT_TAG);
						}
						if(TextUtils.isEmpty(model.exceptToArriveTime)) {
							tmp.append(EMPTY_TAG + ARG_SPILT_TAG);
						} else {
							tmp.append(model.exceptToArriveTime+ARG_SPILT_TAG);
						}
						tmp.append(MSG_SPILT_TAG);
						content.append(tmp);
					}
				}
				Log.d("HybridUS", "content = " + content.toString());
				ScheduleFileOperate.getInstance().saveSchedule(content.toString());
			}
		}.start();
		
	}
	
}
