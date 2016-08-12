package com.billionav.navi.naviscreen.hud;

import java.util.ArrayList;

import jp.pioneer.huddevelopkit.DataRouteTraffic;
import jp.pioneer.huddevelopkit.HUDConstants.TrafficStateColor;

public class BeanRouteTraffic {
	private int routeId;
	private int crowdColor;
	private int JamColor;
	private ArrayList<DataRouteTraffic> routeTrafficis = new ArrayList<DataRouteTraffic>();
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public TrafficStateColor getCrowdColor() {
		return EnumData.getTrafficStateColor(crowdColor);
	}
	public void setCrowdColor(int crowdColor) {
		this.crowdColor = crowdColor;
	}
	public TrafficStateColor getJamColor() {
		return EnumData.getTrafficStateColor(JamColor);
	}
	public void setJamColor(int jamColor) {
		this.JamColor = jamColor;
	}
	public ArrayList<DataRouteTraffic> getRouteTrafficis() {
		return routeTrafficis;
	}
	public void addRouteTrafficis(double startLatitude,
			double		dStartLongitude,
			int		iStartDistance,
			double		dEndLatitude,
			double		dEndLongitude,
			int		iEndDistance,
			int		iRouteTrafficType) {
		routeTrafficis.add(new DataRouteTraffic(startLatitude, dStartLongitude, iStartDistance, dEndLatitude, dEndLongitude, iEndDistance, 
				EnumData.getRouteTrafficType(iRouteTrafficType)));
	}

}
