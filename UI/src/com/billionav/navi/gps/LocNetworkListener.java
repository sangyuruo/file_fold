package com.billionav.navi.gps;

import com.billionav.jni.jniLocInfor;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocNetworkListener implements LocationListener {
	
	// Debug target
	private static final String TAG = "LocNetworkListener";
	private static final boolean Debug = true;
	
	// Instance
	private LocNetworkListener() {}
	private static final LocNetworkListener mInstance = new LocNetworkListener();
	public static LocNetworkListener instance() {return mInstance;}
	
	// Location process
	private static LocationManager mLocManager = null;
	
	private boolean mbWorking = false;
	 
	public synchronized void intilaize(LocationManager locManager) {
		
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocNetworkListener intilaize");
		}

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
		
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocNetworkListener start");
		}
		
		// set location listener.the frequency of Location update is 1hz;
		mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
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
			Log.d(TAG, "[GPS] LocNetworkListener stop");
		}
		
		// Removes any current registration for location updates of the current
		// activity with the given LocationListener. Following this call,
		// updates will no longer occur for this listener.
		mLocManager.removeUpdates(this);
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
	
	private static final int NETWORK_POSITION_LENGTH = 3;

	// Implement functions
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Debug
		if (Debug) {
			Log.d(TAG, "[GPS] LocNetworkListener onLocationChanged" + location);
		}
		
		mLocation = location;
		mUpated = true;
		
		// Send position to native
		double[] position = new double[NETWORK_POSITION_LENGTH];
		position[0] = location.getLongitude();	// [deg]
		position[1] = location.getLatitude();	// [deg]
		if (location.hasAltitude()) {
			position[2] = location.getAltitude();
		}
		else {
			position[2] = 0.0f;
		}
		
		// Send network position
		mJniLocInfor.SendNetworkPosition(true, position, NETWORK_POSITION_LENGTH, true);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// Debug
		if (Debug) {
			Log.d(TAG, "[GPS] LocNetworkListener onProviderDisabled");
		}
		
		mUpated = false;
		
		double[] position = new double[NETWORK_POSITION_LENGTH];
		// Send network position
		mJniLocInfor.SendNetworkPosition(false, position, NETWORK_POSITION_LENGTH, true);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
