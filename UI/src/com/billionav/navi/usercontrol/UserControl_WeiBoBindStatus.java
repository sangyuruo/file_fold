package com.billionav.navi.usercontrol;

public class UserControl_WeiBoBindStatus {
	public static final int  WEIBO_MAX_NUMBER = 6;
	private String[] strWeiBoNames	= new String[WEIBO_MAX_NUMBER];
	private String[] strWeiBoBindValues = new String[WEIBO_MAX_NUMBER];;
	private String[] strWeiBoTokens = new String[WEIBO_MAX_NUMBER];;
	private String[] strWeiBoValidPeriods = new String[WEIBO_MAX_NUMBER];;
	
	
	public String[] getStrWeiBoNames() {
		return strWeiBoNames;
	}
	public void setStrWeiBoNames(String[] strWeiBoNames) {
		this.strWeiBoNames = strWeiBoNames;
	}
	public String[] getStrWeiBoBindValues() {
		return strWeiBoBindValues;
	}
	public void setStrWeiBoBindValues(String[] strWeiBoBindValues) {
		this.strWeiBoBindValues = strWeiBoBindValues;
	}
	public String[] getStrWeiBoTokens() {
		return strWeiBoTokens;
	}
	public void setStrWeiBoTokens(String[] strWeiBoTokens) {
		this.strWeiBoTokens = strWeiBoTokens;
	}
	public String[] getStrWeiBoValidPeriods() {
		return strWeiBoValidPeriods;
	}
	public void setStrWeiBoValidPeriods(String[] strWeiBoValidPeriods) {
		this.strWeiBoValidPeriods = strWeiBoValidPeriods;
	}
	public void setStrWeiBoName(int index , String value) {
		strWeiBoNames[index] = value;
	}
	public void setStrWeiBoBindValue(int index , String value) {
		strWeiBoBindValues[index] = value;
	}
	public void setStrWeiBoTokens(int index, String value) {
		strWeiBoTokens[index] = value;
	}
	public void setStrWeiBoValidPeriods(int index, String value) {
		strWeiBoValidPeriods[index] = value;
	}
	
}
