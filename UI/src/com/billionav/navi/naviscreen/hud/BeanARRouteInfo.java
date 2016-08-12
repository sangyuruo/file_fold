package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;

import jp.pioneer.huddevelopkit.DataARRoute;
import jp.pioneer.huddevelopkit.HUDConstants.RouteGranularity;

public class BeanARRouteInfo {
	private int mRouteID;
	private RouteGranularity routeGranularity;
	private ArrayList<DataARRoute> list = new ArrayList<DataARRoute>();
	public int getmRouteID() {
		return mRouteID;
	}
	public void setmRouteID(int mRouteID) {
		this.mRouteID = mRouteID;
	}
	public RouteGranularity getRouteGranularity() {
		return routeGranularity;
	}
	public void setRouteGranularity(int routeGranularity) {
		this.routeGranularity = EnumData.getRouteGranularity(routeGranularity);
	}

	public ArrayList<DataARRoute> getList() {
		return list;
	}

	public void addDataARRoute(double startLatitude, double endLatitude, double startLongitude, double endLongitude, int color) {
		list.add(new DataARRoute(startLatitude, endLatitude, startLongitude, endLongitude, EnumData.getLineColor(color)));
	}


}
