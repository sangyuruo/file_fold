package com.billionav.navi.sensor;

public class LocSnsGpsInfo {
	
	private static LocSnsGpsInfo m_instance = new LocSnsGpsInfo();
	public static LocSnsGpsInfo instance() {return m_instance;}
	
	public long   time_gps;		// the time of event 
	public byte 	flag_gps;		// the flag if the GPS is valid
	
	public double 	lat;    		//the latitude of this fix.
	public double 	lon;    		//the longitude of this fix.
	public long 	time;     		//in second since January 1, 1970.
	
	public boolean	altitudeFlag;   //true if this fix contains altitude information, false otherwise.
	public double 	altitude;		//the altitude of this fix.
	
	public boolean 	speedFlag;     	//true if this fix contains speed information, false otherwise.
	public double 	speed;			//speed of the device over ground in meters/second
	
	public boolean 	bearingFlag;	//true if the provider is able to report bearing information, false otherwise.
	public double 	bearing;		//the direction of travel in degrees East of true North.
	
	public boolean 	accuracyFlag;	//true if the provider is able to report accuracy information, false otherwise.
	public double 	accuracy;		//the accuracy of the fix in meters.
	
	public float 	ttff;			//time to first fix.(milliseconds)
	public int 		numSatellite;	// the number of the satellite used for fix;	
	
}
