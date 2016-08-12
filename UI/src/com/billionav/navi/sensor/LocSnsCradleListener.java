package com.billionav.navi.sensor;

import android.content.Context;
import com.billionav.jni.jniLocInfor;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.navi.gps.Timer;
import android.util.Log;

import android.location.Location;
import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.interfaces.LocationListener;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.PLocProviderKit.interfaces.SatelliteListener;
import com.pioneer.PLocProviderKit.util.SatelliteData;



public class LocSnsCradleListener implements RequiredListener,LocationListener,SatelliteListener{
    /** Called when the activity is first created. */	
	
	// jniLocInfor used to send information
	private jniLocInfor mJniLocInfor = null;	
    
    private PLocProviderKit m_ProviderKit = new PLocProviderKit();
    
    private int m_CradleConnectStatus = PLocProviderKit.CONNECT_STATE_NONE;
    
    
    int sns_type = 0;
    int sns_accuracy = 0;
	long sns_time = 0;
	double sns_x = 0.1;
	double sns_y = 0.0;
	double sns_z = 0.0;	
	long last_acc_time = 0;
	long last_gyro_time = 0;	
	
    
    private static final String TAG = "LocSnsCradleListener";	
    
    //==============for instance ========================
    //=====================================================//	
	private LocSnsCradleListener() {}
	private static LocSnsCradleListener m_Instance = new LocSnsCradleListener();
	public static LocSnsCradleListener instance() {return m_Instance;}
	//==============End for instance ====================================================// 
	
	//================for other model to construct LocSnsListener ======================// 
    public void initialize(Context cContext) {
    	
    	boolean ret = m_ProviderKit.startLocProvider(cContext);
    	
    	// Get the instance of jniLocInfor;
		mJniLocInfor = jniLocInfor.getLocInfor();

		m_CradleOK = false;
    }
    
    public void start() {
    	
    	m_ProviderKit.registerGpsStatusListener(this);
    	m_ProviderKit.registerLocationListener(this);
    	m_ProviderKit.registerSatelliteListener(this);
    	
    	// TODO Auto-generated method stub
		mUpated = false;
		// Stop the clear timer
		mGpsUpdateTimer.stopTimer();
		// Clear GpsInfo to invalid
		mJniLocInfor.ClearLocInfo();
		// And notify GpsEngine
		mJniLocInfor.makeAndSendInfo();
		
		m_ProviderKit.getListenerInfo();
		
    }
    
    public void stop() {
    	
    	m_ProviderKit.unregisterGpsStatusListener(this);
    	m_ProviderKit.unregisterLocationListener(this); 
    	m_ProviderKit.unregisterSatelliteListener(this);
    	// TODO Auto-generated method stub
		mUpated = false;
		// Stop the clear timer
		mGpsUpdateTimer.stopTimer();
		// Clear GpsInfo to invalid
		mJniLocInfor.ClearLocInfo();
		// And notify GpsEngine
		mJniLocInfor.makeAndSendInfo();
    	
    } 
    
    public void stopProvider(Context cContext) {
    	
    	m_ProviderKit.stopLocProvider(cContext);
    } 
    
 // Data manager
	private static Location mLocation = new Location("");
	private static boolean mUpated = false;
	private boolean m_CradleOK = false;

	public Location getLocation() {
		
		mUpated = false;
		return mLocation;
	}
	
	public boolean isUpdated() {
		
		return mUpated;
	}
	
	public boolean isCradleOK(){
		return m_CradleOK;
	}

	// Location information timer 2s
	private Timer mGpsUpdateTimer = new Timer(2000) {
		
		@Override
		public void onTimer() {
			// TODO Auto-generated method stub
			// Clear GpsInfo to invalid
			mJniLocInfor.ClearLocInfo();
			// And notify GpsEngine
			mJniLocInfor.makeAndSendInfo();
		}
	};
   
    
  //================End for other model to construct LocSnsListener ======================//  
 
 // Implement functions
	@Override
	public void onExtDeviceConnectStateChanged(int connectState) {
		Integer state = connectState;		
		/*		
		if (connectState == m_CradleConnectStatus) {
			return;
		}*/
		m_CradleConnectStatus = connectState;
		
		switch (connectState) {
		case PLocProviderKit.CONNECT_STATE_NOT_CONNECT_WITH_SERVICE: 
			CLocationListener.Instance().CradleConnectDisable();
			m_CradleOK = false;
			break;
			
		case PLocProviderKit.CONNECT_STATE_CONNECTED:
			CLocationListener.Instance().CloseInternalGpsStatus();
			m_CradleOK = true;
			break;
			
		case PLocProviderKit.CONNECT_STATE_CONNECTING:
			break;
			
		case PLocProviderKit.CONNECT_STATE_NONE:
			CLocationListener.Instance().CradleConnectDisable();
			m_CradleOK = false;
			break;
			
		default:
			break;
		}
		
	}
	
// Implement functions
	@Override
	public void onReceiveLocationInfo(String nmeaData) {
		//Log.d(TAG,"[SNS_C] Location 1");
	}
    
 // Implement functions
	@Override
	public void onReceiveLocationInfo(Location location) {
		// TODO Auto-generated method stub	
		
		
		mLocation = location;
		mUpated = true;
		
		// the longitude of this fix.
		mJniLocInfor.lon = location.getLongitude();
		// the latitude of this fix.
		mJniLocInfor.lat = location.getLatitude();		
		
		//mJniLocInfor.lon = 122.4;//location.getLongitude();
		// the latitude of this fix.
		//mJniLocInfor.lat = 32.5;//location.getLatitude();		
		
		
		// in sec since January 1, 1970.
		mJniLocInfor.time = location.getTime();

		// true if this fix contains altitude information, false otherwise.
		mJniLocInfor.altitudeFlag = location.hasAltitude();
		// the altitude of this fix.
		mJniLocInfor.altitude = location.getAltitude();

		// true if this fix contains speed information, false otherwise.
		mJniLocInfor.speedFlag = location.hasSpeed();
		// speed of the device over ground in meters/second
		mJniLocInfor.speed = location.getSpeed();		
		

		// true if the provider is able to report bearing information, false
		// otherwise.
		mJniLocInfor.bearingFlag = location.hasBearing();
		// the direction of travel in degrees East of true North.
		mJniLocInfor.bearing = location.getBearing();

		// true if the provider is able to report accuracy information, false
		// otherwise.
		mJniLocInfor.accuracyFlag = location.hasAccuracy();
		// the accuracy of the fix in meters.
		mJniLocInfor.accuracy = location.getAccuracy();	
		
		
		
		// GPS signal update true
		mJniLocInfor.gpsUpdate = true;

		// make information and send it to GpsEngine.
		mJniLocInfor.makeAndSendInfo();
		
		// Start a timer to clear old data
		mGpsUpdateTimer.startTimer();
		
		
	}
	
// Implement functions
	@Override
	public void onRecive(SatelliteData[] satellites) {		
		
		int iSatelliteCnt = 0;
		
		int number = 0;
		
		for (iSatelliteCnt = 0; iSatelliteCnt < jniLocInfor.MAX_GPS_SATELLITE; iSatelliteCnt++) {
			
			if(iSatelliteCnt >= satellites.length) {
				return;
			}
			
			mJniLocInfor.SatelliteID[iSatelliteCnt] = satellites[iSatelliteCnt].getSatelliteId();
			
			// the azimuth of the satellite in degrees. The azimuth can
			// vary between 0 and 360.
			mJniLocInfor.Azimuth[iSatelliteCnt] = satellites[iSatelliteCnt].getAzimuth();
			
			// the elevation of the satellite in degrees. The elevation
			// can vary between 0 and 90.
			mJniLocInfor.ElevationAngle[iSatelliteCnt] = satellites[iSatelliteCnt].getElevation();			
			
			// the signal to noise ratio for the satellite.
			mJniLocInfor.SignalStrength[iSatelliteCnt] = satellites[iSatelliteCnt].getSnr();			
			
			// true if the satellite was used by the GPS engine when
			// calculating the most recent GPS fix.
			mJniLocInfor.usedInFix[iSatelliteCnt] = satellites[iSatelliteCnt].used();
			
			if (true == satellites[iSatelliteCnt].used()) {
				number++;
			}
		}
		
		mJniLocInfor.numSatellite = number;	
		
	}
	
	
}