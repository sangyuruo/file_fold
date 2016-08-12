package com.billionav.navi.usercontrol;

import java.util.ArrayList;

public class UserControl_UserFeedBack {
	private String strTTS;
	private ArrayList<UserControl_PositionConfig> cConfigList = null;
	
	
	public String getStrTTS() {
		return strTTS;
	}
	public void setStrTTS(String strTTS) {
		this.strTTS = strTTS;
	}
	public ArrayList<UserControl_PositionConfig> getcConfigList() {
		return cConfigList;
	}
	public void setcConfigList(ArrayList<UserControl_PositionConfig> cConfigList) {
		this.cConfigList = cConfigList;
	}
	
	
}
