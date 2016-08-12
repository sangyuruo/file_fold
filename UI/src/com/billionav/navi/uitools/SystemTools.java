package com.billionav.navi.uitools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import jp.pioneer.huddevelopkit.PHUDConnectManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.system.AplRuntime;
import com.billionav.navi.system.StartRuntimeTask;

public class SystemTools {

	public static final String EDITION_TRUNK 	= "trunk";
	public static final String EDITION_SUNING 	= "suning";
	public static final String EDITION_LUXGEN 	= "luxgen";
	public static final String EDITION_CRADLE	= "cradle_hud";
	public static final String EDITION_HUD 		= "cradle_hud";
	
	private static StartRuntimeTask task = null;
	public static void exitSystem() {
		stopStartTask();
		AplRuntime.Instance().AplExit();
	}
	
	public static String getVersionString(){
	    try {
			ComponentName comp = NSViewManager.GetViewManager().getComponentName();
			PackageInfo packinfo = NSViewManager.GetViewManager().getPackageManager().getPackageInfo(comp.getPackageName(), 0);
			return packinfo.versionName;
			  
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return "0.0.0";
		}

	}
	public static String getApkEdition() {
		Context context = NaviViewManager.GetViewManager();
		String edition = "";
		try {
			
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
            		context.getPackageName(), PackageManager.GET_META_DATA);
            edition = info.metaData.getString("edition");
        } catch (Exception e) {
        }
		return edition;
	}
	public static String getAppSDKVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean isJP() {
		return (UIMapControlJNI.GetModelInfo() == 2);
	}
	
	public static boolean isCH() {
		return true;
	}
	
	public static boolean isOS() {
		return (UIMapControlJNI.GetModelInfo() == 1);
	}
	
	public static void resetLanguageVersion() {
		Resources resource = NSViewManager.GetViewManager().getResources();
		Configuration config = resource.getConfiguration();
		int langtype = UIMapControlJNI.GetModelInfo();
		switch(langtype){
		case 1: 
			config.locale = Locale.ENGLISH;
			break;
		case 0: 
			config.locale = Locale.SIMPLIFIED_CHINESE;
			break;
		case 2:
			config.locale = Locale.JAPANESE;
			break;
		default:
			break;
		}
		DisplayMetrics dm = resource.getDisplayMetrics();
		resource.updateConfiguration(config, dm);
		
	}
	
	private static long time = 0;
	public static void printDuration(String logTag){
		Log.d("DURATION","["+logTag+"]"+" "+(System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
	}
	

	public static void registerStartTask(StartRuntimeTask startRuntimeTask) {
		task = startRuntimeTask;
	}
	
	public static void unRegisterStartTask(){
		task = null;
	}
	
	private static void stopStartTask(){
		if(null != task){
			task.cancel(true);
			task = null;
		}
	}
    
	public static String getLocalTimeText(String networkDate) {
		String timeStr = "";
		java.text.DateFormat format1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			Calendar cal = getStringToCal(networkDate);
			timeStr = format1.format(new Date(cal.getTimeInMillis()));
		} catch (Exception e) {
			timeStr = networkDate;
		}
		return timeStr;
	}
	private static Calendar getStringToCal(String date) {
         final String year = date.substring(0, 4);
         final String month = date.substring(5, 7);
         final String day = date.substring(8, 10);
         final String hour = date.substring(11, 13);
         final String minute = date.substring(14, 16);
         final String second = date.substring(17, 19);
         final int millisecond = Integer.valueOf(date.substring(20, 23));
         Calendar result =
             new GregorianCalendar(Integer.valueOf(year),
                 Integer.valueOf(month) - 1, Integer.valueOf(day),
                 Integer.valueOf(hour), Integer.valueOf(minute),
                 Integer.valueOf(second));
         result.set(Calendar.MILLISECOND, millisecond);
         result.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
         return result;

     }
	public static String getFileContext(String filepath){
		return getFileContext(new File(filepath));
	}
	
	public static boolean writeFileContext(String filePath, String context){
		return writeFileContext(new File(filePath), context);
	}
	
	public static String getFileContext(File filepath) {
		if (filepath == null || "".equals(filepath.getPath())) {
			return null;
		}
		
		BufferedReader reader = null;
		String temp = null;
		StringBuffer InfoStr = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(filepath));
			while ((temp = reader.readLine()) != null) {
				InfoStr.append(temp);
				InfoStr.append("\n");
			}
			reader.close();
			return InfoStr.toString();
		} catch (Exception e) {
		}
		return null;
	}
	
	public static boolean writeFileContext(File filePath, String context){
		if(!filePath.exists()){
			Log.e("test","FILE DOSE NOT EXIST, CREATE");
			try {
				File parent = filePath.getParentFile();
				if(null != parent){
					parent.mkdirs();
				}
				filePath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(filePath);
			fw.write(context);
		} catch (IOException e) {
			Log.d("test","IOException writeFileContext");
			return false;
		}finally {
			if(null != fw){
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}
}
