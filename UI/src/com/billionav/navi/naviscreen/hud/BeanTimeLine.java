package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;

import jp.pioneer.huddevelopkit.DataTimeLine;

public class BeanTimeLine {
	private int routeId;
	private ArrayList<DataTimeLine> times = new ArrayList<DataTimeLine>();
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public ArrayList<DataTimeLine> getTimeLines() {
		return times;
	}
	
	public void addTimeLine(int startDistance, int endDistance, int color) {
		times.add(new DataTimeLine(startDistance, endDistance, EnumData.getTimeLineColor(color)));
	}

}
