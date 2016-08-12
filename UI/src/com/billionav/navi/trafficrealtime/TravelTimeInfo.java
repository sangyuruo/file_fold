package com.billionav.navi.trafficrealtime;

import android.util.Log;

public class TravelTimeInfo {

	private String type;
	private String duration;
	private String duration_units;
	private String average_speed;
	private String average_speed_units;
	
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n type = " + type);
		buffer.append("\n duration = " + duration);
		buffer.append("\n duration_units = " + duration_units);
		buffer.append("\n average_speed = " + average_speed);
		buffer.append("\n average_speed_units = " + average_speed_units);
		
		Log.v(TrafficXmlParser.TagName,buffer.toString());
		return buffer.toString();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getDuration_units() {
		return duration_units;
	}
	public void setDuration_units(String duration_units) {
		this.duration_units = duration_units;
	}
	public String getAverage_speed() {
		return average_speed;
	}
	public void setAverage_speed(String average_speed) {
		this.average_speed = average_speed;
	}
	public String getAverage_speed_units() {
		return average_speed_units;
	}
	public void setAverage_speed_units(String average_speed_units) {
		this.average_speed_units = average_speed_units;
	}
	
	
}
