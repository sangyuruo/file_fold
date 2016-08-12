package com.billionav.navi.trafficrealtime;


import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

/**
 * this class stores information of Flow_Item
 * @author wenjianjun
 *
 */
public class FlowItemInfo {

	private String ID;
	private String ebu_country_code;
	private String table_ID;
	private String location_ID;
	private String location_desc;
	private String rds_direction;
	private String rds_length;
	private String rds_length_units;
	private String lane_type;
	private ArrayList<TravelTimeInfo>travelTimeInfoList;
	private String jam_factor;
	private String jam_factor_trend;
	private String confidence;
	
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n ID = " + ID);
		buffer.append("\n ebu_country_code = " + ebu_country_code);
		buffer.append("\n table_ID = " + table_ID);
		buffer.append("\n location_ID = " + location_ID);
		buffer.append("\n location_desc = " + location_desc);
		buffer.append("\n rds_direction = " + rds_direction);
		buffer.append("\n rds_length = " + rds_length);
		buffer.append("\n rds_length_units = " + rds_length_units);
		buffer.append("\n lane_type = " + lane_type);
		buffer.append("\n jam_factor = " + jam_factor);
		buffer.append("\n jam_factor_trend = " + jam_factor_trend);
		buffer.append("\n confidence = " + confidence);
		
		Log.v(TrafficXmlParser.TagName,buffer.toString());
		
		Iterator it = travelTimeInfoList.iterator();
		while( it.hasNext() )
		{
			TravelTimeInfo item = (TravelTimeInfo)it.next();
			//buffer.append(item.toString());
			item.toString();
		}
		
		return buffer.toString();
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getEbu_country_code() {
		return ebu_country_code;
	}
	public void setEbu_country_code(String ebu_contry_code) {
		this.ebu_country_code = ebu_contry_code;
	}
	public String getTable_ID() {
		return table_ID;
	}
	public void setTable_ID(String table_ID) {
		this.table_ID = table_ID;
	}
	public String getLocation_ID() {
		return location_ID;
	}
	public void setLocation_ID(String location_ID) {
		this.location_ID = location_ID;
	}
	public String getLocation_desc() {
		return location_desc;
	}
	public void setLocation_desc(String location_desc) {
		this.location_desc = location_desc;
	}
	public String getRds_direction() {
		return rds_direction;
	}
	public void setRds_direction(String rds_direction) {
		this.rds_direction = rds_direction;
	}
	public String getRds_length() {
		return rds_length;
	}
	public void setRds_length(String rds_length) {
		this.rds_length = rds_length;
	}
	public String getRds_length_units() {
		return rds_length_units;
	}
	public void setRds_length_units(String rds_length_units) {
		this.rds_length_units = rds_length_units;
	}
	public String getLane_type() {
		return lane_type;
	}
	public void setLane_type(String lane_type) {
		this.lane_type = lane_type;
	}
	public ArrayList<TravelTimeInfo> getTravelTimeInfoList() {
		return travelTimeInfoList;
	}
	public void setTravelTimeInfoList(ArrayList<TravelTimeInfo> travelTimeInfoList) {
		this.travelTimeInfoList = travelTimeInfoList;
	}
	public String getJam_factor() {
		return jam_factor;
	}
	public void setJam_factor(String jam_factor) {
		this.jam_factor = jam_factor;
	}
	public String getJam_factor_trend() {
		return jam_factor_trend;
	}
	public void setJam_factor_trend(String jam_factor_trend) {
		this.jam_factor_trend = jam_factor_trend;
	}
	public String getConfidence() {
		return confidence;
	}
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	
	
	
}
