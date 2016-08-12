package com.billionav.jni;

public class UIDebugJNI {
	
	public static final int LOG_STATUS_RECORD_START = 1;
	public static final int LOG_STATUS_RECORD_STOP = 2;
	public static final int LOG_STATUS_PLAY_START = 3;
	public static final int LOG_STATUS_PLAY_INFO = 4;
	public static final int LOG_STATUS_PLAY_STOP = 5;
	public static final int LOG_STATUS_DISK_FULL = 6;
	public static final int LOG_STATUS_TIME_INVALID = 7;
	public static final int LOG_STATUS_WRITE_ERROR = 8;
	public static final int LOG_STATUS_PLAY_CRTTIME = 9;

	
	private UIDebugJNI(){}
	
	
	/**
	 * start record
	 * */
	public static native void reqRecordStart();
	
	/**
	 * end record
	 * */
	public static native void reqRecordStop();

	/**
	 * 
	 * call this method, will receive a trigger:UIC_DEBUG_RECV_LOG_STATUS,
	 * then call {@link reqStartReplay} for start replay.
	 * 
	 * trigger Id:UIC_DEBUG_RECV_LOG_STATUS
	 * parameter 1: UIDebugJNI.LOG_STATUS_PLAY_INFO
	 * parameter 2: start time
	 * parameter 3: end time
	 * */
	public static native void reqGetPlayInfo();
	
	/**
	 * start replay, but
	 * call this method must after call method {@link reqGetPlayInfo},
	 * and must after you receive trigger:UIC_DEBUG_RECV_LOG_STATUS 
	 * 
	 * */
	public static native void reqStartReplay(long startTime, long endTime);
	
	
	public static native DebugLogSettingItem[] getDebugLogSettingState();
	/**
	 * stop replay
	 * */
	public static native void reqStopReplay();
	
	
	/**
	 * request Debug Setting from base group
	 * return triggerID UIC_DEBUG_RECV_DEVELOP_LOG_OPTIONS_STATES
	 */
	public static native void reqDebugLogSettingState();
	public static native void SetDevelopDebugOption(int id, boolean selected);
	public static native boolean isDevelopLogOpen();
	public static native void setDevelopDebugLogOutput(boolean value);
	
	//called by cpp
	public static class DebugLogSettingItem{
		public int logID;
		public String logDispName = "";
		public boolean selected;
		public DebugLogSettingItem(int id, String dispName, boolean isChecked){
			this.logID = id;
			this.logDispName = dispName;
			this.selected = isChecked;
		}
	}
	

}
