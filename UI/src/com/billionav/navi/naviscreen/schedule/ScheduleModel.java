package com.billionav.navi.naviscreen.schedule;

public class ScheduleModel {

	public static final int ARG_NUM = 5;
	
	public static final int ARG_LON = 0;
	public static final int ARG_LAT = 1;
	public static final int ARG_POINTNAME = 2;
	public static final int ARG_SCHEDULE_TITLE = 3;
	public static final int ARG_EXCEPT_TO_ARRIVE_TIEM = 4;
	
	public long lon;
	public long lat;
	public String pointName;
	public String ScheduleTitle;
	public String exceptToArriveTime;
	public int actualToArrivalTime;
	public int index;
	public boolean validTime = true;
}
