package com.billionav.navi.uitools;

import java.io.Serializable;


public class SchedulePointBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6617236751295518199L;
	
	private int index;
	private String name;
	private long longitude;
	private long latitude;
	private int arrivalTime;
	private String expectTime;

	public SchedulePointBean(String name , long longitude, long latitude){
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getName() {
		return name;
	}
	
	public long getLongitude() {
		return longitude;
	}
	
	public long getLatitude() {
		return latitude;
	}
	
	public long[] getLonlat() {
		return new long[] {longitude, latitude};
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getArrivalTime(){
		return arrivalTime;
	}
	
	public String getExpectTime(){
		return expectTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setExpectTime(String expectTime) {
		this.expectTime = expectTime;
	}
	
	
}
