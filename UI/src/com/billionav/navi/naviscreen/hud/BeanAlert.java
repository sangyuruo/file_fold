package com.billionav.navi.naviscreen.hud;

import jp.pioneer.huddevelopkit.HUDConstants.AlertKind;
import jp.pioneer.huddevelopkit.HUDConstants.DistanceUnit;

public class BeanAlert {
	private int alertKind;
	private int type;
	private String distStr;
	private int unit;
	public BeanAlert() {
	}
	public BeanAlert(int alertKind, int type, String distStr, int unit) {
		super();
		this.alertKind = alertKind;
		this.type = type;
		this.distStr = distStr;
		this.unit = unit;
	}
	public AlertKind getAlertKind() {
		return EnumData.getalertKind(alertKind);
	}
	public void setAlertKind(int alertKind) {
		this.alertKind = alertKind;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDistStr() {
		return distStr;
	}
	public void setDistStr(String distStr) {
		this.distStr = distStr;
	}
	public DistanceUnit getUnit() {
		return EnumData.getDistanceunit(unit);
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	

}
