package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_FavoritePosterItem {
	private String strPosterID;
	private String strText;
	private String strLonlat;		
	private String strLocinfo;		
	private String strPosttime;		

	private ArrayList<String> cListPhotosPath = null;
	
	public String getStrPosterID() {
		return strPosterID;
	}
	public void setStrPosterID(String strPosterID) {
		this.strPosterID = strPosterID;
	}
	public String getStrText() {
		return strText;
	}
	public void setStrText(String strText) {
		this.strText = strText;
	}
	public ArrayList<String> getcListPhotosPath() {
		return cListPhotosPath;
	}
	public void setcListPhotosPath(ArrayList<String> cListPhotosPath) {
		this.cListPhotosPath = cListPhotosPath;
	}
	public String getStrLonlat() {
		return strLonlat;
	}
	public void setStrLonlat(String strLonlat) {
		this.strLonlat = strLonlat;
	}
	public String getStrLocinfo() {
		return strLocinfo;
	}
	public void setStrLocinfo(String strLocinfo) {
		this.strLocinfo = strLocinfo;
	}
	public String getStrPosttime() {
		return strPosttime;
	}
	public void setStrPosttime(String strPosttime) {
		this.strPosttime = strPosttime;
	}
	
	
}
