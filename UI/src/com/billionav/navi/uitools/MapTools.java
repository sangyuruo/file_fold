package com.billionav.navi.uitools;

import com.billionav.jni.UIMapControlJNI;

public class MapTools {
	private MapTools(){};
	
//	private static boolean isOld = false;
	
//	public static int getCurrentScale() {
//		long height = UIMapControlJNI.GetHeight();
//		return UIMapControlJNI.MapGetScaleFromHeight(height);
//	}
	
	public static void setCenterLonlat(int lon, int lat) {
//		if(isOld) {
//			MapEngineJni.SetCenterLonLat(lon, lat);
//		} else {
		UIMapControlJNI.SetCenterInfo(lon, lat, UIMapControlJNI.GetHeight());
//		}
	}
	
	/**
	 * object must be AL_LonLat, CLonLat, MapLonLat, int[], long[]
	 * 
	 * */
//	public static void setCenterLonlat(Object lonlat) {
//		int[] lonlat2 = PointFactory.getIntArrayLonLat(lonlat);
//		setCenterLonlat(lonlat2[0], lonlat2[1]);
//	}
	
	/**
	 * update map height
	 * 
	 * */
    public static void updateHeight(int height){
    	
    	int[] lonlat = UIMapControlJNI.GetCenterLonLat();
    	UIMapControlJNI.SetCenterInfo(lonlat[0], lonlat[1], height);
    }



}
