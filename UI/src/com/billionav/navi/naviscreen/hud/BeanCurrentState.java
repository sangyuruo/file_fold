package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;
import java.util.EnumSet;

import jp.pioneer.huddevelopkit.HUDConstants.DistanceUnit;
import jp.pioneer.huddevelopkit.HUDConstants.GuideTargetNum;
import jp.pioneer.huddevelopkit.HUDConstants.MapColor;
import jp.pioneer.huddevelopkit.HUDConstants.SettingBackground;
import jp.pioneer.huddevelopkit.HUDConstants.SpeedCameraGroupMask;
import jp.pioneer.huddevelopkit.HUDConstants.TimeFormat;
import jp.pioneer.huddevelopkit.HUDConstants.UnitTypeSystem;
import jp.pioneer.huddevelopkit.HUDConstants.WaypointNum;

public class BeanCurrentState {
	// time
	 int  	year;
	 int  	month;
	 int 	day;
	 int  	hour;
	 int  	minute;
	 int  	second;
	 
	 // setting
	 MapColor  	mapColorMode;	 
	 TimeFormat	timeFormat;	 
	 UnitTypeSystem  	unitSystem;	 
	 SettingBackground	behaviorInBackground;	

	 boolean routeSimulation;
	 EnumSet<SpeedCameraGroupMask>  groupOfSpeedCamera;
	 
	 //Visibility
	 boolean 		poi; 
	 boolean 		congestion;
	 boolean 		smooth;
	 boolean 		restriction; 
	 boolean 		speedcamera;
	 
	 //route
	 int		routeID;
	 int	  	distance; 
	 
	 //speed
	 int  	speedLimit;
	 boolean		overSpeed;
	 ArrayList<GuidepointDistance> guidePoints = new ArrayList<GuidepointDistance>();
	 
	 int DestinationHour;
	 int DestinationMinute;

	 ArrayList<WaypointTime> waypointTimes = new ArrayList<WaypointTime>();

	 
	public void addGuidePoint(GuideTargetNum target, int id, int iDistance,
			String cDistance, DistanceUnit distanceUnit) {
		GuidepointDistance point = new GuidepointDistance();
		point.target = target;
		point.id = id;
		point.iDistance = iDistance;
		point.cDistance = cDistance;
		point.distanceUnit = distanceUnit;
		guidePoints.add(point);
	}
	
	public ArrayList<GuidepointDistance> getGuidePoints() {
		return guidePoints;
	}

	public void addWaypointTime(WaypointNum target, int id, int hour, int minute) {
		WaypointTime point = new WaypointTime();
		point.target = target;
		point.id = id;
		point.hour = hour;
		point.minute = minute;
		waypointTimes.add(point);
	}
	
	public ArrayList<WaypointTime> getWaypointTimes() {
		return waypointTimes;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public MapColor getMapColorMode() {
		return mapColorMode;
	}

	public void setMapColorMode(MapColor mapColorMode) {
		this.mapColorMode = mapColorMode;
	}

	public TimeFormat getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(TimeFormat timeFormat) {
		this.timeFormat = timeFormat;
	}

	public UnitTypeSystem getUnitSystem() {
		return unitSystem;
	}

	public void setUnitSystem(UnitTypeSystem unitSystem) {
		this.unitSystem = unitSystem;
	}

	public SettingBackground getBehaviorInBackground() {
		return behaviorInBackground;
	}

	public void setBehaviorInBackground(SettingBackground behaviorInBackground) {
		this.behaviorInBackground = behaviorInBackground;
	}

	public boolean isRouteSimulation() {
		return routeSimulation;
	}

	public void setRouteSimulation(boolean routeSimulation) {
		this.routeSimulation = routeSimulation;
	}

	public EnumSet<SpeedCameraGroupMask>  getGroupOfSpeedCamera() {
		return groupOfSpeedCamera;
	}

	public void setGroupOfSpeedCamera(EnumSet<SpeedCameraGroupMask>  groupOfSpeedCamera) {
		this.groupOfSpeedCamera = groupOfSpeedCamera;
	}

	public boolean isPoi() {
		return poi;
	}

	public void setPoi(boolean poi) {
		this.poi = poi;
	}

	public boolean isCongestion() {
		return congestion;
	}

	public void setCongestion(boolean congestion) {
		this.congestion = congestion;
	}

	public boolean isSmooth() {
		return smooth;
	}

	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}

	public boolean isRestriction() {
		return restriction;
	}

	public void setRestriction(boolean restriction) {
		this.restriction = restriction;
	}

	public boolean isSpeedcamera() {
		return speedcamera;
	}

	public void setSpeedcamera(boolean speedcamera) {
		this.speedcamera = speedcamera;
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	public boolean isOverSpeed() {
		return overSpeed;
	}

	public void setOverSpeed(boolean overSpeed) {
		this.overSpeed = overSpeed;
	}

	public int getDestinationHour() {
		return DestinationHour;
	}

	public void setDestinationHour(int destinationHour) {
		DestinationHour = destinationHour;
	}

	public int getDestinationMinute() {
		return DestinationMinute;
	}

	public void setDestinationMinute(int destinationMinute) {
		DestinationMinute = destinationMinute;
	}

	static class GuidepointDistance{
		GuideTargetNum	target;
		 int 		id;
		 int 		iDistance;
		 String		cDistance;
		 DistanceUnit  	    distanceUnit;
	}

	static class WaypointTime
	{
		WaypointNum		target;	
		 int		id;
		 int		hour;
		 int		minute;
	}
	 

}
