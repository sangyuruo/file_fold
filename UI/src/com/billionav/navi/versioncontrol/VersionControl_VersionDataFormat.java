package com.billionav.navi.versioncontrol;

import java.util.Map;

import org.json.JSONObject;

public class VersionControl_VersionDataFormat {
	public static String APIKEY = "package";
	public static String METADATAKEY = "metadata";
	public static String UPDATEINFOKEY = "updateinfo";
	public static String INVALIDATE_VALUE = "";
	public static String SPLIT_STRING = "\\.";
	public static String DAILY_BUILD_STRING = "daily";
	public static String WEEKLY_BUILD_STRING = "build";
	public static String RELEASE_UPDATE_LOG_INFO_SUFFIX = ".updateinfo";
	
	private String strVerSion = INVALIDATE_VALUE;
	private String strAPKName = INVALIDATE_VALUE;
	private String strMetaData = INVALIDATE_VALUE;
	private String strUpdateInfo = INVALIDATE_VALUE;
	
	private JSONObject jsonMetaData = null;
	private Map<String, String> cMetaDataMap = null;
	private String strAPKReleaseInfo = INVALIDATE_VALUE;
	
	private String strAPKDownLoadURL = INVALIDATE_VALUE;
	private String strMetaDataDownLoadURL = INVALIDATE_VALUE;
	private String strUpdateInfoURL = INVALIDATE_VALUE;
	
	public String getStrVerSion() {
		return strVerSion;
	}
	public void setStrVerSion(String strVerSion) {
		this.strVerSion = strVerSion;
	}
	public String getStrAPKName() {
		return strAPKName;
	}
	public void setStrAPKName(String strAPKName) {
		this.strAPKName = strAPKName;
	}
	public String getStrMetaData() {
		return strMetaData;
	}
	public void setStrMetaData(String strMetaData) {
		this.strMetaData = strMetaData;
	}
	public String getStrAPKDownLoadURL() {
		return strAPKDownLoadURL;
	}
	public void setStrAPKDownLoadURL(String strAPKDownLoadURL) {
		this.strAPKDownLoadURL = strAPKDownLoadURL;
	}
	public String getStrMetaDataDownLoadURL() {
		return strMetaDataDownLoadURL;
	}
	public void setStrMetaDataDownLoadURL(String strMetaDataDownLoadURL) {
		this.strMetaDataDownLoadURL = strMetaDataDownLoadURL;
	}
	public Map<String, String> getcMetaDataMap() {
		return cMetaDataMap;
	}
	public void setcMetaDataMap(Map<String, String> cMetaDataMap) {
		this.cMetaDataMap = cMetaDataMap;
	}
	public String getStrAPKReleaseInfo() {
		return strAPKReleaseInfo;
	}
	public void setStrAPKReleaseInfo(String strAPKReleaseInfo) {
		this.strAPKReleaseInfo = strAPKReleaseInfo;
	}
	public JSONObject getJsonMetaData() {
		return jsonMetaData;
	}
	public void setJsonMetaData(JSONObject jsonMetaData) {
		this.jsonMetaData = jsonMetaData;
	}
	public String getStrUpdateInfo() {
		return strUpdateInfo;
	}
	public void setStrUpdateInfo(String strUpdateInfo) {
		this.strUpdateInfo = strUpdateInfo;
	}
	public String getStrUpdateInfoURL() {
		return strUpdateInfoURL;
	}
	public void setStrUpdateInfoURL(String strUpdateInfoURL) {
		this.strUpdateInfoURL = strUpdateInfoURL;
	}
	
}
