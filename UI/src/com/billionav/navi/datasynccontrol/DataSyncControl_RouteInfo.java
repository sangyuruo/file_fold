package com.billionav.navi.datasynccontrol;

public class DataSyncControl_RouteInfo{
	private String strName;
	private String strPath;
	private int iNewDownload;
	private int iRouteStatus;
	
	
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStrPath() {
		return strPath;
	}
	public void setStrPath(String strPath) {
		this.strPath = strPath;
	}
	public int getiNewDownload() {
		return iNewDownload;
	}
	public void setiNewDownload(int iNewDownload) {
		this.iNewDownload = iNewDownload;
	}
	
	public int getRouteStatus() {
		return iRouteStatus;
	}
	public void setRouteStatus(int routeStatus) {
		iRouteStatus = routeStatus;
	}
	
	
}