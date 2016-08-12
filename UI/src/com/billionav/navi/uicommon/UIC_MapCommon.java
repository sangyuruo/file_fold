/**
 * 
 */
package com.billionav.navi.uicommon;

import java.util.ArrayList;

import android.util.Log;

import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.naviscreen.NaviViewManager;

/**
 * @author Administrator
 * 
 */
public class UIC_MapCommon {

	/**
	 * 
	 */
	private UIC_MapCommon() {
	}

	
	private static int mapScaleValue;
	private static double maxDistanceX;
	private static double maxDistanceY;
	private static long[] mapCenterLonLat = new long[2];
	static final double distancePerPxInLv19 = 1.4;
	
	public static void calculateSrchPointArea(ArrayList<long[]> arrayList){
		long[] centerlonlat = arrayList.get(0);
		long lonmax=centerlonlat[0],lonmin=centerlonlat[0],latmax=centerlonlat[1],latmin=centerlonlat[1];
		int count = arrayList.size();
		if(count > 10){
			count = 10;
		}
		for(int i = 1; i < count; i++){
			long[] lonlat = arrayList.get(i);
			if(lonlat[0] > lonmax){
				lonmax = lonlat[0];
			}
			if(lonlat[0] < lonmin){
				lonmin = lonlat[0];
			}
			if(lonlat[1] > latmax){
				latmax = lonlat[1];
			}
			if(lonlat[1] < latmin){
				latmin = lonlat[1];
			}
		}
		double distance[] = new double[4];
		distance[0] = Math.abs(centerlonlat[0]-lonmax);
		distance[1] = Math.abs(centerlonlat[0]-lonmin);
		distance[2] = Math.abs(centerlonlat[1]-latmax);
		distance[3] = Math.abs(centerlonlat[1]-latmin);
		maxDistanceX = (distance[0] > distance[1] ?  distance[0] : distance[1]) * 2;
		maxDistanceY = (distance[2] > distance[3] ?  distance[2] : distance[3]) * 2;
	}
	
	public static boolean calculateAllRouteMapArea(){
		long[] lArea = new UIPathControlJNI().GetRouteArea(UIPathControlJNI.UIC_PT_FIND_OBJ_INVALID); 
		if(lArea != null && lArea.length >= 4){
			long point1Lon = (lArea[0]) ;
			long point1Lat = (lArea[1]) ;
			long point2Lon = (lArea[2]) ;
			long point2Lat = (lArea[3]) ;
			
			double lonOffset = Math.abs(point2Lon - point1Lon);
			double latOffset = Math.abs(point2Lat - point1Lat);
			
			maxDistanceX = lonOffset ;
			maxDistanceY = latOffset ;
			if(lonOffset > latOffset){
				double checkValueLon = 360;
				for(int checkNum = 0; checkNum < 20; checkNum++){
					checkValueLon /= 2;
					if(lonOffset > checkValueLon){
						mapScaleValue = checkNum - 1;
						break;
					}else if(Double.compare(lonOffset, checkValueLon) == 0){
						mapScaleValue = checkNum;
						break;
					}
				}
			}else{
				double checkValueLat = 180;
				for(int checkNum = 0; checkNum < 20; checkNum++){
					checkValueLat /= 2;
					if(latOffset > checkValueLat){
						mapScaleValue = checkNum - 1;
						break;
					}else if(latOffset == checkValueLat){
						mapScaleValue = checkNum;
						break;
					}
				}
			}
			
			mapCenterLonLat[0] = (lArea[0] + lArea[2]) / 2 ;
			mapCenterLonLat[1] = (lArea[1] + lArea[3]) / 2 ;
			return true;
		}else{
			return false;
		}
	}
	
	public static int getSuitableMapHeight(int mapHeight, int mapWidth) {
		double destScaleX = maxDistanceX / mapWidth;
		double destScaleY = maxDistanceY / mapHeight;
		double destScale = destScaleX > destScaleY ? destScaleX : destScaleY;
		return (int) ((destScale / distancePerPxInLv19) * 48);
	}
	
	public static int getAllRouteHeight(int mapHeight, int mapWidth) {
		double destScaleX = maxDistanceX / mapWidth;
		double destScaleY = maxDistanceY / mapHeight;
		double destScale = destScaleX > destScaleY ? destScaleX : destScaleY;
		double density = NaviViewManager.GetViewManager().getResources().getDisplayMetrics().density;
		return (int) ((destScale / distancePerPxInLv19) * 48 * density);
	}
	
	public static long[] getAllRouteMapCenterLonLat(){
		
		return mapCenterLonLat;
	}
	
}
