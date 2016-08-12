package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;
import java.util.Arrays;

import jp.pioneer.huddevelopkit.DataWayPoint;

public class BeanTarget {
	private int routeID;
	private int image;
	private double longitude;
	private double latitude;
	private int routeDistance;
	private ArrayList<DataWayPoint> wayPoints = new ArrayList<DataWayPoint>();
	
	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getRouteDistance() {
		return routeDistance;
	}

	public void setRouteDistance(int routeDistance) {
		this.routeDistance = routeDistance;
	}

	public void addWayPoint(int wayPointID, int image, double latitude, double longitude, int routeDistance) {
		wayPoints.add(new DataWayPoint(wayPointID, image, latitude, longitude, routeDistance));
	}
	
	public void addWayPoint(DataWayPoint[] wayts) {
		wayPoints.addAll(Arrays.asList(wayts));
	}
	
	public ArrayList<DataWayPoint> getWayPoints(){
		return wayPoints;
		
	}
}
