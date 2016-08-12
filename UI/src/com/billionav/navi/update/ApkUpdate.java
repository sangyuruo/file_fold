package com.billionav.navi.update;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.net.PHost;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;
import com.billionav.navi.versioncontrol.VersionControl_VersionComparator;
import com.billionav.navi.versioncontrol.VersionControl_VersionDataFormat;


public class ApkUpdate{
	private final static String APK_VERSION = "APL_VERSION";
	private static boolean ApkUpdated = false;
	private static boolean hasChecked = false;
	public ApkUpdate(){
		
	}
	
	public static void requestApkVersionOnserver() {
		// wait for interface supported by Control(Yanyexing)
		Log.d("test","Apk Version list request sent by UI");
//		VersionControl_ManagerIF.Instance().GetAPIVersionListInfo();// old function
		String currentAplVer = null;
		try { 
			currentAplVer = NSViewManager.GetViewManager().getPackageManager().getPackageInfo(NSViewManager.GetViewManager().getPackageName(), PackageManager.GET_RECEIVERS).versionName; 
		} catch (NameNotFoundException e) { 
			e.printStackTrace(); 
		} 
		if(null != currentAplVer && !currentAplVer.equals("")){
			VersionControl_ManagerIF.Instance().GetAllAPKUpdateInfos(currentAplVer);
		}
	}

	protected static boolean isLatestVersion() {
		// wait for interface supported by Control(Yanyexing)
		VersionControl_VersionDataFormat verNode = VersionControl_ManagerIF.Instance().getM_cLatestVersionInfo();
		String latestAplVer = verNode.getStrVerSion();
		String currentAplVer = null;
		try { 
			currentAplVer = NSViewManager.GetViewManager().getPackageManager().getPackageInfo(NSViewManager.GetViewManager().getPackageName(), PackageManager.GET_RECEIVERS).versionName; 
		} catch (NameNotFoundException e) { 
			e.printStackTrace(); 
		} 
		int compValue = VersionControl_ManagerIF.Instance().CompareVersion(currentAplVer, latestAplVer);
		boolean returnValue = (null != currentAplVer) && (null != latestAplVer)
				&& !(VersionControl_VersionComparator.VERSION_LOCAL_LOWER == compValue);
		Log.d("[VersionControl]","[VersionControl]: result = "+compValue+"    local="+currentAplVer+"    server="+latestAplVer);
		currentAplVer = null;
		latestAplVer = null;
		verNode = null;
		return returnValue;
	}
	
	protected static boolean hasAPKVersionUpdated(Context c){
		if(hasChecked){
			Log.d("test","apkudated = "+"ApkUpdated");
			return ApkUpdated;
		}
		hasChecked = true;
		SharedPreferences sp = c.getSharedPreferences(c.getPackageName(), 0);	
		String savedAplVer = sp.getString(APK_VERSION, APK_VERSION);
		String curAplVer = null;
		try { 
			curAplVer = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_RECEIVERS).versionName; 
		} catch (NameNotFoundException e) { 
			e.printStackTrace(); 
		} 
		if((null != curAplVer) && (null != savedAplVer) && savedAplVer.contentEquals(curAplVer)){
			ApkUpdated = false;
		}else{
			ApkUpdated = true;
			Editor edit = sp.edit();
			edit.putString(APK_VERSION, curAplVer);
			edit.commit();
		}
		savedAplVer = null;
		curAplVer = null;
		sp = null;
		Log.d("test","apkudated = "+"ApkUpdated");
		return ApkUpdated;
	}

	public static void requestApkUpdate() {
		VersionControl_VersionDataFormat versionNode = VersionControl_ManagerIF.Instance().getM_cLatestVersionInfo();
		Uri uri = Uri.parse(makeAbsUrl(versionNode.getStrAPKDownLoadURL())); 
//		uri = Uri.parse("http://analytics.163.com/ntes_dwld?prod=3gmarket&id=2949664&pf=2&cg=CA8MTH1D1V28&sign=563ecbecf343bad19bb56bcc42265822&url=http%3A%2F%2Ffile.m.163.com%2Fapp%2Ffree%2F201306%2F03%2Ftv.acfun.activity_6.apk"); 

		try{
			Intent intent =new Intent(Intent.ACTION_VIEW, uri);
			NSViewManager.GetViewManager().startActivity(intent);
			versionNode = null;
		}catch (Exception e){
			Log.e("test", "error downloadURL = "+uri);
		}
	}
	
	protected static String makeAbsUrl(String url)
	{
		if(url == null){
			return "";
		}
				
		int indexS = url.indexOf("(");
		int indexE = url.indexOf(")");
		if(-1 != indexS  && -1 != indexE )
		{
			String hostname = url.substring(indexS+1, indexE);
			String hostaddr = PHost.getHostAddrbyName(hostname);
			if(null != hostaddr)
			{
				String s1 = url.substring(0, indexS);
				String s2 = url.substring(indexE+1);
				String absUrl = s1+hostaddr+s2;
				return absUrl;
			} 
			else 
			{
				return url;
			}
			
		}
		return url;
	}
}
