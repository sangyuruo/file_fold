package com.billionav.navi.gps;

import java.util.Iterator;

import com.billionav.jni.jniLocInfor;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocGpsListener implements LocationListener {

	// Debug target
	private static final String TAG = "LocGpsListener";
	private static final boolean Debug = false;
	
	// Instance
	private LocGpsListener() {}
	private static final LocGpsListener mInstance = new LocGpsListener();
	public static LocGpsListener instance() {return mInstance;}
	
	// Location process
	private static LocationManager mLocManager = null;
	
	private boolean mbWorking = false;
	
	private boolean m_bGpsListenerEnable = true;

	private boolean m_bGpsOK = false;
	 
	public synchronized void intilaize(LocationManager locManager) {
		
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocGpsListener intilaize");
		}

		mLocManager = locManager;
		// Get the instance of jniLocInfor;
		mJniLocInfor = jniLocInfor.getLocInfor();
		// Initialize the working flag 
		mbWorking = false;
		//can receive GPS data or not
		m_bGpsOK = false;
	}
	
	public synchronized void start() {
		
		// Check the working flag
		if (!mbWorking) {
			mbWorking = true;
		}
		else {
			return;
		}
		
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocGpsListener start");
		}
		
		// set location listener.the frequency of Location update is 1hz;
		mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
		// add GPS status Listener.
		mLocManager.addGpsStatusListener(statusListener);
		
		// TODO Auto-generated method stub
		mUpated = false;
		// Stop the clear timer
		mGpsUpdateTimer.stopTimer();
		// Clear GpsInfo to invalid
		mJniLocInfor.ClearLocInfo();
		// And notify GpsEngine
		mJniLocInfor.makeAndSendInfo();
	}
	
	public synchronized void stop() {
		
		// Check the working flag
		if (mbWorking) {
			mbWorking = false;
		}
		else {
			return;
		}
		
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocGpsListener stop");
		}
		
		// Removes any current registration for location updates of the current
		// activity with the given LocationListener. Following this call,
		// updates will no longer occur for this listener.
		mLocManager.removeUpdates(this);
		// Removes a GPS status listener.
		mLocManager.removeGpsStatusListener(statusListener);
		// Stop the clear timer
		mGpsUpdateTimer.stopTimer();
		// Clear GpsInfo to invalid
		mJniLocInfor.ClearLocInfo();
		// And notify GpsEngine
		mJniLocInfor.makeAndSendInfo();
	}
	
	public void SetGpsEnable(boolean enable) {
		
		//for cradle connected
		m_bGpsListenerEnable = enable;
	}
	
	// Data manager
	private static Location mLocation = new Location("");
	private static boolean mUpated = false;
	
	public Location getLocation() {
		
		mUpated = false;
		return mLocation;
	}
	
	public boolean isUpdated() {
		
		return mUpated;
	}
	
	public boolean isGpsOpen() {
		
		return mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public boolean isGpsOK() {

		return m_bGpsOK;
	}

	// jniLocInfor used to send information
	private jniLocInfor mJniLocInfor = null;
	
	// Location information timer 2s
	private Timer mGpsUpdateTimer = new Timer(2000) {
		
		@Override
		public void onTimer() {
			
			if (!m_bGpsListenerEnable) {
				
				return;
			}
			// TODO Auto-generated method stub
			// Clear GpsInfo to invalid
			mJniLocInfor.ClearLocInfo();
			// And notify GpsEngine
			mJniLocInfor.makeAndSendInfo();

			m_bGpsOK = false;
		}
	};

	// Implement functions
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Debug
		if (Debug) {
			Log.d(TAG, "[GPS] LocGpsListener onLocationChanged" + location);
		}
		
		if (!m_bGpsListenerEnable) {
			
			return;
		}
		
		mLocation = location;
		mUpated = true;

		m_bGpsOK = true;

		// the longitude of this fix.
		mJniLocInfor.lon = location.getLongitude();
		// the latitude of this fix.
		mJniLocInfor.lat = location.getLatitude();
		
		// in sec since January 1, 1970.
		mJniLocInfor.time = location.getTime() / 1000;

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
		
		// Close all net-work location listeners
		// To save net-work data
		CLocationListener.Instance().CloseNetworkListeners();
	}

	@Override
	public void onProviderDisabled(String provider) {
		
		// Debug
		if (Debug) {
			Log.d(TAG, "[GPS] LocGpsListener onProviderDisabled");
		}
		
		if (!m_bGpsListenerEnable) {
			
			return;
		}
		
		// TODO Auto-generated method stub
		mUpated = false;
		// Stop the clear timer
		mGpsUpdateTimer.stopTimer();
		// Clear GpsInfo to invalid
		mJniLocInfor.ClearLocInfo();
		// And notify GpsEngine
		mJniLocInfor.makeAndSendInfo();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	// Event sent when the GPS system has received its first fix since starting
	private static final int GPS_EVENT_FIRST_FIX = 3;
	// Event sent periodically to report GPS satellite status.
	private static final int GPS_EVENT_SATELLITE_STATUS = 4;
	
	/*
	 * GPS status listener
	 */
	private GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			
			if (!m_bGpsListenerEnable) {
				
				return;
			}
			// Retrieves information about the current status of the GPS engine
			GpsStatus gpsStatus = mLocManager.getGpsStatus(null);

			switch (event) {
			case GPS_EVENT_FIRST_FIX:
				// time to first fix in milliseconds
				mJniLocInfor.ttff = gpsStatus.getTimeToFirstFix();
				break;

			case GPS_EVENT_SATELLITE_STATUS:
				// counts of Satellite
				int iSatelliteCnt = 0;
				// Get an array of GpsSatellite objects, which represent the
				// current state of the GPS
				Iterable<GpsSatellite> allSatellites = gpsStatus.getSatellites();
				for (Iterator<GpsSatellite> iter = allSatellites.iterator(); iter.hasNext();iSatelliteCnt++) {

					// the max Value of Satellite is 32;
					if (iSatelliteCnt >= jniLocInfor.MAX_GPS_SATELLITE) {
						break;
					}

					// the current state of a GPS satellite
					GpsSatellite satellite = (GpsSatellite) iter.next();

					// the azimuth of the satellite in degrees. The azimuth can
					// vary between 0 and 360.
					mJniLocInfor.Azimuth[iSatelliteCnt] = satellite.getAzimuth();
					
					// the elevation of the satellite in degrees. The elevation
					// can vary between 0 and 90.
					mJniLocInfor.ElevationAngle[iSatelliteCnt] = satellite.getElevation();
					
					// the PRN (pseudo-random number) for the satellite.
					mJniLocInfor.SatelliteID[iSatelliteCnt] = satellite.getPrn();
					
					// the signal to noise ratio for the satellite.
					mJniLocInfor.SignalStrength[iSatelliteCnt] = satellite.getSnr();
					
					// true if the GPS engine has almanac data for the satellite.
					mJniLocInfor.hasAlmanac[iSatelliteCnt] = satellite.hasAlmanac();
					
					// true if the GPS engine has ephemeris data for the satellite.
					mJniLocInfor.hasEphemeris[iSatelliteCnt] = satellite.hasEphemeris();
					
					// true if the satellite was used by the GPS engine when
					// calculating the most recent GPS fix.
					mJniLocInfor.usedInFix[iSatelliteCnt] = satellite.usedInFix();
				}

				// record the total number of Satellite that used for fix;
				mJniLocInfor.numSatellite = iSatelliteCnt;				
				break;

			default:
				break;
			}
		}
	};
}
