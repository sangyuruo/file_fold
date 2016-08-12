package com.billionav.navi.update;

import com.billionav.jni.UIUpdateControlJNI;
import com.billionav.navi.dataupdate.DataUpdateManager;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;

import android.util.Log;


public class DataUpdate{
	public DataUpdate(){
	}
//	public static boolean isDataFormatOK(){
//		Log.d("test","testDataFormat");
//		return true;
//	}
	public static boolean isLatestVersion() {
		//get DATA version in apk
		int curVer = DataUpdateManager.getCurDataVersion();
		int latestVer = -1;
		try{
			//get DATA version in from response in server
			latestVer = Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strTopDataVersion());
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		if(latestVer >= 0){
			if(curVer < latestVer){
				return false;
			}
		}

		return true;
	}
	
	public static boolean isSrchDataLatestVersion() {
		//get Srch DATA version in apk
		int curVer = DataUpdateManager.getSearchDataVersion();
		int latestVer = -1;
		try{
			//get Srch DATA version in from response in server
			latestVer = Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strSrchTopDataVersion());
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		if(latestVer >= 0){
			if(curVer < latestVer){
				return false;
			}
		}

		return true;
	}
	public static boolean belowMinVersion() {
		int curVer = DataUpdateManager.getCurDataVersion();
		int minVer = -1;
		try{
			minVer = Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strMinDataVersion());
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		if(minVer > 0){
			if(curVer < minVer){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCurSrchDataVersionVailed() {
		int curVer = DataUpdateManager.getSearchDataVersion();
		int latestVer = -1;
		try{
			//get Srch DATA version in from response in server
			latestVer = Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strSrchTopDataVersion());
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		if(curVer < 0 && latestVer > 0) {
			DataUpdateManager.setSearchDataVersion(latestVer);
		}
		return true;
	}
	
	public static boolean SrchDataBelowMinVersion() {
		int curVer = DataUpdateManager.getSearchDataVersion();
		int minVer = -1;
		try{
			minVer = Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strSrchMinDataVersion());
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		if(minVer > 0){
			if(curVer < minVer){
				return true;
			}
		}
		return false;
	}
	public static void requestLatestDataVersionOnserver(){
		VersionControl_ManagerIF.Instance().GetTopDataVersionInfo(DataUpdateManager.getFormatVersion()+"");
		
		//should modify to get search data
		requestLatestSrchDataVersionOnServer();
	}

	public static void requestLatestSrchDataVersionOnServer(){
		//should modify to get search data
		VersionControl_ManagerIF.Instance().GetTopSearchDataVersionInfo(DataUpdateManager.getSearchFormatVersion()+"");
	}
	
	public static void informMapDataNeedUpdate(){
		try{
			//get DATA version in from response in server
			DataUpdateManager.requestClearDataForDataVersionChanged(Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strTopDataVersion()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void informSrchDataNeedUpdate() {
		int topVer = -1;
		try{
			topVer = Integer.parseInt(VersionControl_ManagerIF.Instance().getM_strSrchTopDataVersion());
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		DataUpdateManager.setSearchDataVersion(topVer);
		UIUpdateControlJNI.RequestClearSearchData(UIUpdateControlJNI.UI_UDC_CLEAR_SEARCH_DATA_DATA_VERSION);
		
	}

}
