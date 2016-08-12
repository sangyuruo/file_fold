package com.billionav.navi.naviscreen.hud;

import jp.pioneer.huddevelopkit.HUDConstants.GpsState;
import jp.pioneer.huddevelopkit.HUDConstants.SignalState;

public class BeanCarCondition {
	
	private double latitude;
	private double longitude;
	private float speed;
	private float bearing;
	private float acceleration;
	private float angularVelocity;
	private float carPitch;
	private GpsState gpsState;	 
	private SignalState signalState;
	private boolean onRoadFlag;
	private boolean onRoute;
	private boolean inTunnelFlag;
	private boolean carNonSequentiality;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getBearing() {
		return bearing;
	}
	public void setBearing(float bearing) {
		this.bearing = bearing;
	}
	public float getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	public float getAngularVelocity() {
		return angularVelocity;
	}
	public void setAngularVelocity(float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
	public float getPitch() {
		return carPitch;
	}
	public void setPitch(float pitch) {
		this.carPitch = pitch;
	}
	public GpsState getGpsState() {
		return gpsState;
	}
	public void setGpsState(GpsState gpsState) {
		this.gpsState = gpsState;
	}
	public SignalState getSignalState() {
		return signalState;
	}
	public void setSignalState(SignalState signalState) {
		this.signalState = signalState;
	}
	public boolean isOnRoadFlag() {
		return onRoadFlag;
	}
	public void setOnRoadFlag(boolean onRoadFlag) {
		this.onRoadFlag = onRoadFlag;
	}
	public boolean isOnRoute() {
		return onRoute;
	}
	public void setOnRoute(boolean onRoute) {
		this.onRoute = onRoute;
	}
	public boolean isInTunnelFlag() {
		return inTunnelFlag;
	}
	public void setInTunnelFlag(boolean inTunnelFlag) {
		this.inTunnelFlag = inTunnelFlag;
	}
	public boolean isCarNonSequentiality() {
		return carNonSequentiality;
	}
	public void setCarNonSequentiality(boolean carNonSequentiality) {
		this.carNonSequentiality = carNonSequentiality;
	} 


}
