package com.billionav.jni;


public class UITrafficControlJNI {
	private UITrafficControlJNI() {
	}
	
	// TrafficIconDetailRes Status
	public static final int UI_TC_TIDR_STATUS_NOERROR			= 0;
	public static final int UI_TC_TIDR_STATUS_ERROROCCURED		= 1;
	
	// TrafficIconDetailRes
	public static class IconParam {
		public int			lon;       
		public int			lat;
		//don't delete or rename it, define for jni
		public IconParam(int lon, int lat) {
			this.lon				= lon;
			this.lat				= lat;
		}
	}
	
	// IconDetail
	public static class IconDetail {
		public int			symbol_id;
		public int			lon;       
		public int			lat;
		public String		start_time;
		public String		end_time;
		public String		detail_info;
		//don't delete or rename it, define for jni
		public IconDetail(int symbol_id, int lon, int lat,
				String start_time, String end_time, String detail_info) {
			this.symbol_id			= symbol_id;
			this.lon				= lon;
			this.lat				= lat;
			this.start_time			= start_time;	// Shallow copy
			this.end_time			= end_time;		// Shallow copy
			this.detail_info		= detail_info;	// Shallow copy
		}
	}
	
	// TrafficIconDetailRes
	public static class TrafficIconDetailRes {
		public int			status;				// TrafficIconDetailRes Status
		public long			error_code;
		public IconDetail	iconDetailItems[]	= null;
		//don't delete or rename it, define for jni
		public TrafficIconDetailRes(int status, long error_code, IconDetail[] iconDetailItems) {
			this.status				= status;
			this.error_code			= error_code;
			
			this.iconDetailItems	= new IconDetail[iconDetailItems.length];
			if (null != iconDetailItems) {
				// Shallow copy
				System.arraycopy(iconDetailItems, 0, this.iconDetailItems, 0, iconDetailItems.length);
			}
		}
	}
	
	public static native void setSystemInfo(String model, String osVersion, String sdkVersion, 
			String appVersion, String UUID, String packageName);
	
	public static native void TrafficIconDetailReq(IconParam[] iconParamItems);
	
	public static native TrafficIconDetailRes GetTrafficIconDetailRes();
}
