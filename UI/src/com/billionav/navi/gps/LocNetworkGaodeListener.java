package com.billionav.navi.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;


import com.amap.mapapi.location.LocationManagerProxy;
import com.amap.mapapi.location.LocationProviderProxy;
import com.billionav.jni.jniLocInfor;


public class LocNetworkGaodeListener implements LocationListener {
	
	// Debug target
	private static final String TAG = "LocNetworkGaodeListener";
	private static final boolean Debug = true;
	
	// jniLocInfor used to send information
	private jniLocInfor mJniLocInfor = null;
	
	private static final int NETWORK_POSITION_LENGTH = 3;
	
	private static LocationManagerProxy locationManager  = null;
	
	private static LocNetworkGaodeListener mInstance = new LocNetworkGaodeListener();
	
	// Data manager
	private static Location mLocation = new Location("");
	private static boolean mUpated = false;
	
	// Instance
	private LocNetworkGaodeListener(){
		
	}
	
	public static LocNetworkGaodeListener instance(){
		return mInstance;
	}
	
	private boolean mbWorking = false;
	
	public synchronized void intilaize(Context context) {
		
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocNetworkGaodeListener intilaize");
		}
		
		locationManager = LocationManagerProxy.getInstance(context);
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
			Log.d(TAG, "[GPS] LocNetworkGaodeListener start");
		}
		
		// set location listener.the frequency of Location update is 1hz;
		locationManager.requestLocationUpdates(LocationProviderProxy.MapABCNetwork, 1000, 0, this);
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
			Log.d(TAG, "[GPS] LocNetworkGaodeListener stop");
		}
		
		// Removes any current registration for location updates of the current
		// activity with the given LocationListener. Following this call,
		// updates will no longer occur for this listener.
		locationManager.removeUpdates(this);
	}
	
	public Location getLocation() {
		
		mUpated = false;
		return mLocation;
	}
	
	public boolean isUpdated() {
		
		return mUpated;
	}

	// Implement functions
	@Override
	public void onLocationChanged(Location location) {
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocNetworkGaodeListener onLocationChanged" + location);
		}
		
		if(location == null || location.getLongitude() == 0.0 || location.getLatitude() == 0.0)
		{
			return;
		}
		
		double[] position = new double[NETWORK_POSITION_LENGTH];
		position[0] = location.getLongitude();	// [deg]
		position[1] = location.getLatitude();	// [deg]
		position[2] = 0.0f;
		
		// Send network position
		mJniLocInfor.SendNetworkPosition(true, position, NETWORK_POSITION_LENGTH, false);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// Debug
		if(Debug) {
			Log.d(TAG, "[GPS] LocNetworkGaodeListener onProviderDisabled");
		}
		
		mUpated = false;
		
		double[] position = new double[NETWORK_POSITION_LENGTH];
		// Send network position
		mJniLocInfor.SendNetworkPosition(false, position, NETWORK_POSITION_LENGTH, false);
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
