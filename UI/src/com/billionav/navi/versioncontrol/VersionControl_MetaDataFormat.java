package com.billionav.navi.versioncontrol;

public class VersionControl_MetaDataFormat {
	private String strKey = "";
	private String strValue = "";
	
	public String getStrKey() {
		return strKey;
	}
	public void setStrKey(String strKey) {
		this.strKey = strKey;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	public String toString() {
		return "KEY: " + strKey + " VALUE: " +strValue;
	}
	
}
