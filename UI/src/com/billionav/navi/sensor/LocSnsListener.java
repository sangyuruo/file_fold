package com.billionav.navi.sensor;

import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

import com.billionav.jni.jniGpsSnsModule;
import com.billionav.jni.jniSNS_SensorLib;

import android.util.Log;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Looper;


public class LocSnsListener implements SensorEventListener,LocationListener{
    /** Called when the activity is first created. */
	//SensorManager instance
    private SensorManager m_sensorManager = null;  
    //accelerometer sensor
    private Sensor m_sensorAcc = null;
    //gyroscope sensor
    private Sensor m_sensorGyro = null;
    //magnetic sensor
    private Sensor m_sensorMmc = null;
    //jni interface 
    private jniSNS_SensorLib m_SensorLib = null;
    
    private static LocationManager mLocManager = null;
    private LocSnsGpsInfo m_locGpsInfo = null;	
    
    private boolean IsAccOK = false;
    private boolean IsGyroOK = false;
    
    int sns_type = 0;
    int sns_accuracy = 0;
	long sns_time = 0;
	double sns_x = 0.0;
	double sns_y = 0.0;
	double sns_z = 0.0;	
	long last_acc_time = 0;
	long last_gyro_time = 0;
	
	double gps_lon_temp = 121.461;
	double gps_lat_temp = 31.219;	
	
    
    private static final String TAG = "LocSnsListener";	
    
    //==============for instance ========================
    //=====================================================//	
	private LocSnsListener() {}
	private static LocSnsListener m_Instance = new LocSnsListener();
	public static LocSnsListener instance() {return m_Instance;}
	//==============End for instance ====================================================// 
	
	//================for other model to construct LocSnsListener ======================// 
    public void initialize(Context cContext) {        
        //create SensorManager instance
    	m_sensorManager = (SensorManager)cContext.getSystemService(Context.SENSOR_SERVICE);     	
    	//mLocManager = (LocationManager)cContext.getSystemService(Context.LOCATION_SERVICE);
    	//create jni interface
    	m_SensorLib = new jniSNS_SensorLib();
    	m_locGpsInfo = LocSnsGpsInfo.instance();
    	//gps_lon_temp = 121.461;
    	//gps_lat_temp = 31.219;
    }
    
    public void start() {
    	//SendStartLocSnsMsg();
    	OpenLocSensor();
    	
    }
    
    public void stop() {
    	//SendStopLocSnsMsg();
    	m_sensorManager.unregisterListener(this);
    	// invalid gyro and gsensor
    	jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GYRO, false);
    	jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GSNS, false);
    }
    
    public boolean IsSensorOK() {
    	if (IsAccOK && IsGyroOK) {    		
    		return true;
    	}
    	return false;
    }   
    
    //it need the only one SensorManager 
    public SensorManager GetSensorManager(){
    	return m_sensorManager;
    }
    
    // for open gyro start and stop
    public void OpenSensorGyro(){
    	
    	m_sensorGyro = m_sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    	if (null == m_sensorGyro){
    		return;
    	}
    	boolean ret = m_sensorManager.registerListener(this,m_sensorGyro,SensorManager.SENSOR_DELAY_NORMAL);
    	if (!ret)
    	{
    		Log.d(TAG,"[SNS] can not register gyroscope");
    		// invalid gyro
    		jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GYRO, false);
    	}
    	else {
    		// set gyro valid
    		jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GYRO, true);
    	}
    	
    }
    public void CloseSensorGyro(){
    	if (null != m_sensorGyro){
    		m_sensorManager.unregisterListener(this, m_sensorGyro);
    		m_sensorGyro = null;
    		// invalid gyro
    		jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GYRO, false);
    	}
    }
  //================End for other model to construct LocSnsListener ======================// 
    
    public void OpenLocAndSns() {
    	
    	// set location listener.the frequency of Location update is 1hz;
		mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
		// add GPS status Listener.
		mLocManager.addGpsStatusListener(statusListener); 		
    }
    public void OpenLocSensor() {
    	
    	m_sensorAcc = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	if (null == m_sensorAcc){
    		return;
    	}
    	boolean ret = m_sensorManager.registerListener(this,m_sensorAcc,SensorManager.SENSOR_DELAY_NORMAL);
    	if (!ret)
    	{
    		Log.d(TAG,"[SNS] can not register accelerometer");
    		// invalid gsensor
    		jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GSNS, false);
    	}
    	else {
    		// set gsensor valid
    		jniGpsSnsModule.getGpsSnsModule().SetSnsValid(jniGpsSnsModule.SNS_MODE_STATUS_BIT_GSNS, true);
    	}
  
/*	
    	//get the list of available sensors of a certain type
    	List<Sensor> sensors = m_sensorManager.getSensorList(Sensor.TYPE_ALL);     	
    	if (sensors.isEmpty())
    	{
    		Log.d(TAG,"[SNS] can not access all kinds of sensors");
    		return;
    	}
    	
    	for (Sensor s:sensors){
    		// access and register all kinds of sensors 
    		switch(s.getType()) {
    		case Sensor.TYPE_ACCELEROMETER:
    		{
    			// register listener for accelerometer
    			m_sensorAcc = s;
    			IsAccOK = true;
    			boolean  ret = m_sensorManager.registerListener(this,m_sensorAcc,SensorManager.SENSOR_DELAY_NORMAL);
    	    	if (!ret)
    	    	{
    	    		Log.d(TAG,"[SNS] can not register accelerometer");
    	    	}  
    	    	break;
    		}    		
    		
    		case Sensor.TYPE_GYROSCOPE:
    		{
    			// register listener for gyroscope
    			m_sensorGyro = s;
    			IsGyroOK = true;
    			boolean ret = m_sensorManager.registerListener(this,m_sensorGyro,SensorManager.SENSOR_DELAY_NORMAL);
    	    	if (!ret)
    	    	{
    	    		Log.d(TAG,"[SNS] can not register gyroscope");
    	    	}
    	    	break;
    		}    		
	
    		case Sensor.TYPE_MAGNETIC_FIELD:
    		{
    			// register listener for magnetic
    			m_sensorMmc = s;
    			boolean ret = m_sensorManager.registerListener(this,m_sensorMmc,SensorManager.SENSOR_DELAY_FASTEST);
    	    	if (!ret)
    	    	{
    	    		Log.d(TAG,"[SNS] can not register magnetic");
    	    	}
    	    	break;
    		}    		
    
    		default: 
    			break;
    		}
    	} 
*/    	
    }
    
    
    public void CloseLocAndSns() {
    	m_sensorManager.unregisterListener(this);
    	mLocManager.removeUpdates(this);
		// Removes a GPS status listener.
		mLocManager.removeGpsStatusListener(statusListener);
    }
    
   

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
       
    }
   

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
    	sns_type = event.sensor.getType();
    	Integer type = 0;
    	Long time = 0l;
    	Float x = 0.0f;
    	Float y = 0.0f;
    	Float z = 0.0f;
    	switch(sns_type){
    	case Sensor.TYPE_ACCELEROMETER:  
    		// access data from event     		
    		sns_accuracy = event.accuracy;
    		sns_time = event.timestamp;
    		sns_x = event.values[0];
    		sns_y = event.values[1];
    		sns_z = event.values[2]; 
    		
    		Long diff = event.timestamp - last_acc_time ;
    		last_acc_time = event.timestamp;
    		if (diff == 0){
    			return;
    		}
    		
    		//send accelerometer data to sensor library		
    		m_SensorLib.SetSnsSensorInfo(sns_type,sns_accuracy, sns_time, sns_x, sns_y, sns_z);
    		
 /*
    		type = sns_type;
    		time = event.timestamp;
    		x = event.values[0];
    		y = event.values[1];
    		z = event.values[2];
    		
    		
    		Log.d(TAG,"[Sensor] ACC type:" + type.toString() + "time : " + time.toString() + "X: " + 
    				x.toString()+ "Y: " + y.toString() + "Z: " 
    				+ z.toString() + "diff : " + diff.toString());
    		if (diff < 0 ){
    			Log.d(TAG,"[Sensor] the sensor acc abnormal");
    		}    		
*/	
    		break;
    	case Sensor.TYPE_GYROSCOPE:    	
    		// access data from event     		
    		sns_accuracy = event.accuracy;
    		sns_time = event.timestamp;
    		sns_x = event.values[0];
    		sns_y = event.values[1];
    		sns_z = event.values[2]; 
    		
    		Long diff2 = event.timestamp - last_gyro_time ;
    		last_gyro_time = event.timestamp;
    		if (diff2 == 0) {
    			return;
    		}
    		
    		//send accelerometer data to sensor library		
    		m_SensorLib.SetSnsSensorInfo(sns_type,sns_accuracy, sns_time, sns_x, sns_y, sns_z);
    		
/*
    		type = sns_type;
    		time = event.timestamp;
    		x = event.values[0];
    		y = event.values[1];
    		z = event.values[2];
  		
    		
    		Log.d(TAG,"[Sensor]_Gyro type:" + type.toString() + "time : " + time.toString() + "X: " + 
    				x.toString()+ "Y: " + y.toString() + "Z: " 
    				+ z.toString() + "diff : " + diff2.toString());
    		if (diff2 < 0 ){
    			Log.d(TAG,"[Sensor] the sensor gyro abnormal");
    		}
 */ 
            break;     	
 
    	default:
    		break;
      }
    }
    
 // Implement functions
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub			
		
		m_locGpsInfo.flag_gps = 1;
		
		// the longitude of this fix.
		m_locGpsInfo.lon = location.getLongitude();
		// the latitude of this fix.
		m_locGpsInfo.lat = location.getLatitude();
		
		// in sec since January 1, 1970.
		m_locGpsInfo.time = location.getTime();

		// true if this fix contains altitude information, false otherwise.
		m_locGpsInfo.altitudeFlag = location.hasAltitude();
		// the altitude of this fix.
		m_locGpsInfo.altitude = location.getAltitude();

		// true if this fix contains speed information, false otherwise.
		m_locGpsInfo.speedFlag = location.hasSpeed();
		// speed of the device over ground in meters/second
		m_locGpsInfo.speed = location.getSpeed();

		// true if the provider is able to report bearing information, false
		// otherwise.
		m_locGpsInfo.bearingFlag = location.hasBearing();
		// the direction of travel in degrees East of true North.
		m_locGpsInfo.bearing = location.getBearing();

		// true if the provider is able to report accuracy information, false
		// otherwise.
		m_locGpsInfo.accuracyFlag = location.hasAccuracy();
		// the accuracy of the fix in meters.
		m_locGpsInfo.accuracy = location.getAccuracy();
		
		m_SensorLib.SetSnsGpsInfo(m_locGpsInfo.time_gps, m_locGpsInfo.flag_gps, 
				m_locGpsInfo.time,m_locGpsInfo.numSatellite, m_locGpsInfo.lat, 
				m_locGpsInfo.lon,m_locGpsInfo.altitude, m_locGpsInfo.speed, 
				m_locGpsInfo.bearing,m_locGpsInfo.accuracy);
		
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		m_locGpsInfo.flag_gps = 0;		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * GPS status listener
	 */
	private GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			// Retrieves information about the current status of the GPS engine
			GpsStatus gpsStatus = mLocManager.getGpsStatus(null);

			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				{
					// time to first fix in milliseconds
					m_locGpsInfo.ttff = gpsStatus.getTimeToFirstFix();
				}
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				{
					// counts of Satellite
					m_locGpsInfo.numSatellite = gpsStatus.getMaxSatellites();
				}				
				break;

			default:
				break;
			}
		}
	};
	
	/*=================================================================================*/
	/*
	/* Open and close GPS or Bluetooth in Asynchronous
	/* 
	/* declare location listener handler class
	/*/
	/*=================================================================================*/
	
	public static class LocSnsListenerHandler extends Handler {
		public LocSnsListenerHandler() {
			super(Looper.getMainLooper());
		}

		public void handleMessage(Message msg) {
			LocSnsListener.instance().handleMessage(msg);
		}
	}
	
	// handler message object
	private static LocSnsListenerHandler m_cLocSnsListenerHandler = new LocSnsListenerHandler();

	// CLocationListenerHandler_MSG_ID
	private static final int MSG_StartLocSns 	= 816;
	private static final int MSG_StopLocSns 	= 817;


	// handler singleton object new
	private LocSnsListenerHandler GetHandler() {
		return m_cLocSnsListenerHandler;
	}
	
	// send message for open GPS and Sensor
	public void SendStartLocSnsMsg() {		
		LocSnsListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_StartLocSns);
		handler.sendMessage(mgs);
	}

	// send message for close GPS and Sensor
	public void SendStopLocSnsMsg() {		
		LocSnsListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_StopLocSns);
		handler.sendMessage(mgs);
	}
	
	// handle message process
	public void handleMessage(Message msg) {
		
		switch (msg.what) {
		case MSG_StartLocSns:
		{
			OpenLocAndSns();
		}
		break;
		
		case MSG_StopLocSns:
		{
			CloseLocAndSns();
		}
		break;
		
		default:
			break;
		}
	}
	
}
