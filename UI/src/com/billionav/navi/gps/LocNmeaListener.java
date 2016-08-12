package com.billionav.navi.gps;

import com.billionav.jni.jniLocInfor;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocNmeaListener implements LocationListener {
	
	// Debug target
	private static final String TAG = "LocNmeaListener";
	private static final boolean Debug = false;
	
	// Instance
	private LocNmeaListener() {}
	private static final LocNmeaListener mInstance = new LocNmeaListener();
	public static LocNmeaListener instance() {return mInstance;}
	
	// Location process
	private static LocationManager mLocManager = null;
	
	private boolean mbWorking = false;
	 
	public synchronized void intilaize(LocationManager locManager) {

		mLocManager = locManager;
		// Get the instance of jniLocInfor;
		mJniLocInfor = jniLocInfor.getLocInfor();
		// Initialize the working flag 
		mbWorking = false;
	}
	
	public synchronized void start() {
		
		// Check the working flag
		if (!mbWorking) {
			mbWorking = true;
		}
		else {
			return;
		}
		
		// set location listener.the frequency of Location update is 1hz;
		// mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
		// add NMEA text Listener.
		mLocManager.addNmeaListener(mNmeaListener);
	}
	
	public synchronized void stop() {
		
		// Check the working flag
		if (mbWorking) {
			mbWorking = false;
		}
		else {
			return;
		}
		
		// Removes any current registration for location updates of the current
		// activity with the given LocationListener. Following this call,
		// updates will no longer occur for this listener.
		// mLocManager.removeUpdates(this);
		// Removes a GPS status listener.
		mLocManager.removeNmeaListener(mNmeaListener);
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
	
	// jniLocInfor used to send information
	private jniLocInfor mJniLocInfor = null;

	// Implement functions
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Debug
		if (Debug) {
			Log.d(TAG, "[LOC] Network onLocationChanged");
		}
	
		mLocation = location;
		mUpated = true;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
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
	 *  NMEA listener
	 */
	private GpsStatus.NmeaListener mNmeaListener = new GpsStatus.NmeaListener() {
		public void onNmeaReceived(long timestamp, String nmea) {
			
			// Get data and length
			final byte[] data = nmea.getBytes();
			final int length = nmea.length();
			
			// Write logs
			if(CLocationListener.Instance().getWriteLogsFile()) {
				// for log
				String strOutput = "";
				int iCnt = 0;
				for (iCnt = 0; iCnt < length; ++iCnt) {
					if (data[iCnt] != '\r' && data[iCnt] != '\n') {
						// ASCII to string
						strOutput += String.format("%C", data[iCnt]);
					}
				}
				// display all data
				CLocationListener.Instance().writeGpslogfile("INT_NMEA[" + String.valueOf(length) + "]:" + strOutput);
			}
			// Send NMEA information to SmartPhone
			mJniLocInfor.GetData(jniLocInfor.DATA_KIND_NMEA, data, length);
		}
	};
}
