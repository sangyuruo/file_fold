package com.billionav.navi.uitools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.ui.R;

public class RouteTool {
	private RouteTool() {}
	public static String substitutionTime(String s) {
		s = s.toLowerCase();
		s = s.trim();
		String[] temp = s.split("\\D+");
		StringBuffer tempString = new StringBuffer();
		switch(temp.length){
		case 1:
			while(temp[0].startsWith("00")){
				if(temp[0].length() > 1){
					temp[0] = temp[0].substring(1);
				}
			}
			if(s.endsWith("h")){
				tempString.append(temp[0] + NSViewManager.GetViewManager().getString(R.string.STR_MM_03_02_01_13));
			}else if(s.endsWith("m")){
				tempString.append(temp[0] + NSViewManager.GetViewManager().getString(R.string.STR_MM_03_02_01_12));
			}
			return tempString.toString();
		case 2:
			while(temp[0].length()>0 && temp[0].charAt(0)=='0'){
				temp[0] = temp[0].substring(1);
			}
			if(temp[0].length()>0){
				tempString.append(temp[0] + NSViewManager.GetViewManager().getString(R.string.STR_MM_03_02_01_13));
			}
			
			if (temp.length > 1) {
				if (temp[1].charAt(0) == '0') {
					if(temp[1].length() > 1){
						temp[1] = temp[1].substring(1);
					}
				}
				tempString.append(temp[1] + NSViewManager.GetViewManager().getString(R.string.STR_MM_03_02_01_12));
			}
			
			return tempString.toString();
		case 0:
			return "** " + NSViewManager.GetViewManager().getString(R.string.STR_MM_03_02_01_13);
			
			default:return s;
		}
		
	}
	
	public static String substitutionTime(int time) {
		int realTime = time/60;
		if(realTime <= 0) {
			return "0";
		}
		
		return realTime/60/24+"day "+realTime/60+"h "+realTime%60+"min";
	}
	
	public static String subStitutionTime(int time) {
		int realTime = time/60;
		if(realTime <= 0) {
			return "0";
		}
		
		return realTime/60+"h "+realTime%60+"min";
	}
	
	public static  String substitutionDistance(String s){
		s = s.toLowerCase();
		s = s.trim();
		while(!s.contains(".") && s.startsWith("00")){
			if(s.length() > 1){
				s = s.substring(1);
			}
		}
		if(s.endsWith("km")){
			return s.replace("km",NSViewManager.GetViewManager().getString(R.string.STR_COM_005));
		} else if(s.endsWith("m")){
			return s.replace("m",NSViewManager.GetViewManager().getString(R.string.STR_COM_004));
		} else {
			return s;
		}
		
	}
	
	public static String getDisplayDistance(long distance) {
		String strRet = "";

		//0-995m ,round with 10m. eg 975m ,shows 980m
		if ((distance>=0) && (distance <995)){
			distance = distance + 5;
			distance = (distance / 10) * 10;
			strRet = String.format("%dm",distance);
		}
		//995m-1km, round with 0.1km. eg 996m, shows 1.0km
		else if ((distance >= 995) && (distance < 1000))
		{
			distance = distance + 5;
			strRet = String.format("%.1fkm",((distance)* 1.0 )/ (1000));
		}
		//1km-99.95km, round with 0.1km. eg 12.675km, shows 12.7 km
		else if ((distance >= 1000) && (distance  < 99.95e3))
		{
			//when distance is 2.45km, it needs to show 2.5km. 
			if (50 ==  (distance % 100))
			{
				distance = distance + 50;
			}
			strRet = String.format("%.1fkm",((distance)* 1.0 )/ (1000));
		}
		//99.95km - 100km, round with 1km. eg 99.96km, show 100km
		else if ((distance >= 99.95e3) && (distance  < 100e3))
		{
			distance = distance + 50;
			distance = distance /1000;
			strRet = String.format("%dkm", distance);
		}
		//100km - (10000K-500), round with 1km
		else if ((distance  >= 100e3) && (distance < (10000e3 - 500)))
		{
			distance = distance + 500;
			distance = distance /1000;
			strRet = String.format("%dkm", distance);
		}
		//larger than 10000k-500, show ****
		else
		{
			strRet = ("****km");
		}

		return strRet;
	}
	
	public static String getDisplayDurition(int time) {
		String strRet = "";
		
		
		if(time <=0) {
			return 0+"min";
		}
		int dHour = 0;
		int dMin = 0;
		int dDay = 0;
		int dSec = 0;
		dHour = (time / 3600) %24;
		dMin = (time / 60)%60;
		dDay = time /3600 / 24;
		dSec = time %60;
		if(dDay > 0) {
			strRet += dDay+"day ";
		}
		if(dHour > 0) {
			strRet += dHour+"hour ";
		}
		if(dMin > 0) {
			strRet += dMin+"min";
		}
		if(dDay == 0 && dHour == 0 && dMin == 0) {
			strRet += dSec+"sec";
		}
		return strRet;
	}
	
	public static String getDisplayTime(long time) {
		String strRet = new String();
		//get set up data operator pointer
		long dHour = 0;
		long dMin  = 0;
		String daynight = "";
		//translate route time to hour, minute
		dHour = time / 3600;
		dMin = (time -dHour * 3600)/60;
		//if route time has seconds, than minute add 1
		if ((time - dHour * 3600 - dMin * 60) > 0)
		{
			dMin = dMin + 1;
		}
		
		Calendar c = Calendar.getInstance();  
	    Date date = c.getTime();
		dHour = date.getHours() + dHour;
		dMin = date.getMinutes() + dMin;
		
		//if minute >60
		if (dMin >= 60)
		{
			dMin = dMin - 60;
			dHour = dHour + 1;
		}
		//get hour time of arrival time
		dHour = dHour % 24;
//		jniSetupControl SetupControl = new jniSetupControl();
//		int iStatus = SetupControl.GetInitialStatus(jniSetupControl.STUPDM_TIME_DISPLAY_PATTERN_TYPE);
//		if (jniSetupControl.STUPDM_TIME_DISPLAY_PATTERN_24H == iStatus)
//		{
//		}
//		//if time is 12H pattern
//		else if (jniSetupControl.STUPDM_TIME_DISPLAY_PATTERN_12H == iStatus)
//		{
//			if((dHour / 12) >= 1){
//				daynight = "pm";
//			}else{
//				daynight = "am";
//			}
//			dHour = dHour % 12;
//			//if hour =0 ,then hour = 12h
//			if (dHour == 0)
//			{
//				dHour = 12;
//			}
//		}
//		else
//		{
//			return null;
//		}
		//show arrival time
		strRet = String.format("%02d", dHour) + ":" + String.format("%02d", dMin) + daynight;
		
		return strRet;
	}
	
	public static boolean isValidTime(int index , String setTime){
		DateFormat df = HybridUSTools.df;
		Calendar c = Calendar.getInstance();
		try{
		    Date setData = df.parse(setTime);
		    Date currentData = df.parse(df.format(c.getTime()));
		    long diff = setData.getTime() - currentData.getTime();
		    long minites = diff / (1000 * 60);
		    long seconds = diff / 1000;
		    int actualToArrivalTime = ScheduleDataList.getInstance()
		    		.getScheduleAtIndex(index).actualToArrivalTime;
		    Log.d("HybridUS", "setTime = " + setTime + "; actualToArrivalTime = " + actualToArrivalTime);
		    if(minites > 0){
		    	Log.d("HybridUS", "currentDate = " + df.format(c.getTime()) + "; seconds = " + seconds);
		    	if(actualToArrivalTime > 0 && seconds > actualToArrivalTime){
		    		return true;
		    	}else{
		    		return false;
		    	}
		    }else{
		    	return false;
		    }
		}catch (Exception e){
			return false;
		}
	}
	
	public static long isValidTime(long routeTime , String setTime){
		DateFormat df = HybridUSTools.df;
		Calendar c = Calendar.getInstance();
		try{
		    Date setData = df.parse(setTime);
		    Date currentData = df.parse(df.format(c.getTime()));
		    long diff = setData.getTime() - currentData.getTime();
		    long minites = diff / (1000 * 60);
		    long seconds = diff / 1000;
		    Log.d("HybridUS", "isValidTime : setTime = " + setTime + "; currentDate = " + df.format(c.getTime()));
		    if(minites > 0){
		    	
		    	int scheduleSettingValue = SharedPreferenceData.getInt(
						SharedPreferenceData.SCHEDULE_START_TIME,
						HybridUSTools.START_TIME_TEN_MINUE);
		    	int leadTime = HybridUSTools.TIME_TEN_MINUE;
		    	if(scheduleSettingValue == HybridUSTools.START_TIME_HALF_HOUR){
		    		leadTime = HybridUSTools.TIME_HALF_HOUR;
		    	} else if(scheduleSettingValue == HybridUSTools.START_TIME_AN_HOUR){
		    		leadTime = HybridUSTools.TIME_AN_HOUR;
		    	}
		    	Log.d("HybridUS", "isValidTime : routeTime = " + routeTime + "; diffSeconds = " + seconds + " ; leadTime = " + leadTime);
		    	if(routeTime > 0 && seconds > routeTime + leadTime){
		    		long startTime = currentData.getTime() + (seconds - routeTime - leadTime) * 1000;
		    		Log.d("HybridUS", "isValidTime : startTime = " + startTime + "; after format = " + df.format(startTime));
		    		return startTime;
		    	}else{
		    		return -1;
		    	}
		    }else{
		    	return -1;
		    }
		}catch (Exception e){
			return -1;
		}
	}
	
	public static boolean isDemo() {
		return (new UIPathControlJNI().GetDemoStatus() == UIPathControlJNI.UIC_PT_DEMO_STATUS_ON);
	}


	public static void forwardToRoute(String name, long[] lonlat) {
		RouteCalcController.instance().pointDataToPOI(name, lonlat);
		
		if(RouteCalcController.instance().getRoutePointFindPurpose() == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
			RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST);
			MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
				
		} else {
			MenuControlIF.Instance().BackSearchWinChange(ADT_Route_Main.class);
		}
	}
	
	public static float getVelocity() {
		return 0;//UILocationControlJNI.GetVelocity()/10f;
	}

}
