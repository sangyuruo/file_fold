package com.billionav.navi.trafficrealtime;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;
/**
 * this class stores information of Roadway_Flow_Item
 * @author wenjianjun
 *
 */
public class RoadwayFlowItemInfo {

	private String timestamp;
	private String roadwayID;
	private String description;
	private String flowItemsDirection;
	private ArrayList<FlowItemInfo>flowItemInfoList;
	
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getRoadwayID() {
		return roadwayID;
	}
	public void setRoadwayID(String raodwayID) {
		this.roadwayID = raodwayID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlowItemsDirection() {
		return flowItemsDirection;
	}
	public void setFlowItemsDirection(String flowItemsDirection) {
		this.flowItemsDirection = flowItemsDirection;
	}
	public ArrayList<FlowItemInfo> getFlowItemInfoList() {
		return flowItemInfoList;
	}
	public void setFlowItemInfoList(ArrayList<FlowItemInfo> flowItemInfoList) {
		this.flowItemInfoList = flowItemInfoList;
	}
	
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n timestamp = " + timestamp);
		buffer.append("\n roadwayID = " + roadwayID);
		buffer.append("\n description = " + description);
		buffer.append("\n flowItemsDirection = " + flowItemsDirection);
		Log.v(TrafficXmlParser.TagName,"++++++++++++++++++++++++++++++++");
		Log.v(TrafficXmlParser.TagName,buffer.toString());
		
		Iterator it = flowItemInfoList.iterator();
		while( it.hasNext())
		{
			FlowItemInfo item = (FlowItemInfo)it.next();
			//buffer.append(item.toString());
			item.toString();
		}
		
		return buffer.toString();
	}
}
