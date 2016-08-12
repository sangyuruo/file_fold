package com.billionav.jni;


public class jniSNS_SensorLib {	
	
	/**
	 *SetAccelerometerInfo
	 *
	 * set sensor info to library
	 *
	 *@param 
	 *
	 *@return none         
	 */
	public native void SetSnsSensorInfo(int type,int accuracy,long time,double x,double y,double z);
		
	/**
	 *SetSnsLocationInfo
	 *
	 * set location info to library
	 *
	 *@param 
	 *
	 *@return none         
	 */
	public native void SetSnsGpsInfo(long time_gps, byte flag_gps,long time,int satnum,
										  double lat,double lon,double height,double speed,double head,
										  double accuracy);
		
	
}
