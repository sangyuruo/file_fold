package com.billionav.navi.naviscreen.hud;

import jp.pioneer.huddevelopkit.HUDConstants.CarColor;
import jp.pioneer.huddevelopkit.HUDConstants.DestinationColor;
import jp.pioneer.huddevelopkit.HUDConstants.GeodeticDatumType;
import jp.pioneer.huddevelopkit.HUDConstants.WaypointColor;

public class BeanSpc {
	private CarColor carClor;
	private DestinationColor destinationColor;
	private WaypointColor waypointColor;
	private GeodeticDatumType geodeticDatumType;
	private boolean mapColorSwitchingFlag;
	private int timeCarCondition;
	private int timeCurrentState;
	
	public CarColor getCarClor() {
		return carClor;
	}
	public void setCarClor(CarColor carClor) {
		this.carClor = carClor;
	}
	public DestinationColor getDestinationColor() {
		return destinationColor;
	}
	public void setDestinationColor(DestinationColor destinationColor) {
		this.destinationColor = destinationColor;
	}
	public WaypointColor getWaypointColor() {
		return waypointColor;
	}
	public void setWaypointColor(WaypointColor waypointColor) {
		this.waypointColor = waypointColor;
	}
	public GeodeticDatumType getGeodeticDatumType() {
		return geodeticDatumType;
	}
	public void setGeodeticDatumType(GeodeticDatumType geodeticDatumType) {
		this.geodeticDatumType = geodeticDatumType;
	}
	public boolean getMapColorSwitchingFlag() {
		return mapColorSwitchingFlag;
	}
	public void setMapColorSwitchingFlag(boolean mapColorSwitchingFlag) {
		this.mapColorSwitchingFlag = mapColorSwitchingFlag;
	}
	public int getTimeCarCondition() {
		return timeCarCondition;
	}
	public void setTimeCarCondition(int timeCarCondition) {
		this.timeCarCondition = timeCarCondition;
	}
	public int getTimeCurrentState() {
		return timeCurrentState;
	}
	public void setTimeCurrentState(int timeCurrentState) {
		this.timeCurrentState = timeCurrentState;
	}

	
}
