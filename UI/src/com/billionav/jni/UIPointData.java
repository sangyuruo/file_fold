package com.billionav.jni;

import java.io.Serializable;

import android.util.Log;

public class UIPointData implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3745140413453334646L;

	protected int id;
	
	private String name;
	private String telNo;
	private String address;
	private long lon;
	private long lat;
	private String uuid;
	private long distance;
	
	public UIPointData(){
	}

	public UIPointData(int recordID){
		this.id = recordID;
	}

	public UIPointData(String address){
		this.address = address;
	}
	
	public UIPointData(int recordID, String name, String address, String telNo, long lon, long lat){
		this.id = recordID;
		this.name = name;
		this.address = address;
		this.telNo = telNo;
		this.lon = lon;
		this.lat = lat;
	}
	
	public UIPointData(int recordID, String name, String address, String telNo, long lon, long lat ,String uuid){
		this.id = recordID;
		this.name = name;
		this.address = address;
		this.telNo = telNo;
		this.lon = lon;
		this.lat = lat;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getLon() {
		return lon;
	}

	public void setLon(long lon) {
		this.lon = lon;
	}

	public long getLat() {
		return lat;
	}
	
	public long[] getLonlat() {
		return new long[]{lon, lat};
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public int getID() {
		return id;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public UIPointData clone(){
		try {
			return (UIPointData) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("never goto this case");
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setDistance(long distance) {
		this.distance = distance;
		
	}
	public long getDistance() {
		return distance;
	}
	
}
