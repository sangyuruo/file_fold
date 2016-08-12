package com.billionav.jni;


import com.billionav.navi.naviscreen.hud.HudManager;

public class UIHudControl {
	private UIHudControl() {
	}
	public static void c2jHUDTriggerAction(int HUDAction, int param1, int param2){
		HudManager.getInstance().notifyHUDEvent(HUDAction, param1, param2);
	}
	private static final int PER_PAGE_SIZE = 400;
	public static native void notifyStartCommunicate();
	public static native void notifyStopCommunicate();
	public static native void attachJ();
	public static native void detatchJ();
	//spc
	public static native int getSpcTimeCarCondition();
	public static native int getSpcTimeCurrentState();
	public static native boolean getSpcMapColorSwitchingFlag();
	public static native int getSpccarcolor();
	public static native int getSpcwaypointcolor();
	public static native int getSpcdestinationcolor();
	public static native int getSpcgeodeticdatumtype();


	//car codition
	public static native double getCarLatitude();
	public static native double getCarLongitude();
	public static native float getCarSpeed();
	public static native float getCarBearing();
	public static native float getCarAcceleration();
	public static native float getCarAngularVelocity();
	public static native float getCarPitch();
	public static native int getCarGpsState();	 
	public static native int getCarSignalState();	 
	public static native boolean getCarOnRoadFlag();
	public static native boolean getCarOnRouteFlag();
	public static native boolean getCarInTunnelFlag();
	public static native boolean getCarNonSequentialityFlag(); 
	
	//current state
	public static native CurrentTime getCurrentTime();
	public static native int getCurrentMapColorMode();
	public static native int getCurrentTimeFormat();
	public static native int getCurrentUnitSystem();
	public static native int getCurrentBehaviorInBackground();
	public static native boolean getCurrentRouteSimulation();
	public static native int getCurrentGroupOfSpeedCamera();
	public static native Visibility getCurrentVisibility();
	public static native int getCurrentRouteId();
	public static native int getCurrentRouteDistance();
	public static native int getCurrentSpeedLimit();
	public static native boolean getCurrentSpeedOvered();
	public static native int getCurrentGuidepointDistanceCount();
	public static native void getCurrentGuidepointDistance(GuidepointDistance gDistance, int index);
	public static native int getCurrentDestinationTimeHour();
	public static native int getCurrentDestinationTimeMinute();
	public static native int getCurrentWaypointTimeCount();
	public static native void getCurrentWaypointTime(WaypointTime t, int index);
	
	//around POI
	public static native Object[] getAroundPOIs();
	
	//AR route
	public static native int getARRouteId(int tag);
	public static native int getARRouteGranularity(int tag);
	public static native DataARRoute[] getARRouteDatas(int start, int end, int tag);
	public static native int getARRouteDataSize(int tag);
	public static native void removeARRouteByTag();
	public static DataARRoute[] getARRouteDatas(int tag) {
		int size = getARRouteDataSize(tag);
		if(size < 0) {
			return null;
		}
		DataARRoute[] list = new DataARRoute[size];
		for(int i = 0; i < size; i += PER_PAGE_SIZE) {
			DataARRoute[] data = getARRouteDatas(i, (i + PER_PAGE_SIZE<size) ?
					(i + PER_PAGE_SIZE) : size, tag);
			for (int j=0; j < data.length; j++) {	
				list[i+j] = data[j];
			}
		}
		return list;
	}
	
	//alert
	public static native int getAlertKind();
	public static native int getAlertMarkType();
	public static native String getAlertDistStr();
	public static native int getAlertUnit();
	
	//timeline
	public static native int getTimeRouteId();
	public static native DataTimeLine[] getTimeLineInfo();
	
	
	//Target
	public static native int getTargetRouteID();
	public static native int getTargetImage();
	public static native double getTargetLatitude();
	public static native double getTargetLongitude();
	public static native int getTargetRouteDistance();
	public static native Object[] getTargetWayPoints();
	
	//junction
	public static native int getJunRouteId();
	public static native int getJunGuidePointId();
	public static native boolean getJunLeftSignBoard();
	public static native boolean getJunRightSignBoard();
	public static native int getJunSignboardType();
	public static native String getJunDirectionName();
	public static native DataJunction[] getJunctionInfo();
	
	//Around Speed Camera
	public static native DataAroundSpeedCamera[] getAroundSpeedCameraInfo();
	
	//Route Speed Camera
	public static native int getRouteSpeedCameraRouteId();
	public static native DataRouteSpeedCamera[] getRouteSpeedCameraInfo();
	
	//route traffic
	public static native int getRouteTrafficRouteId(); 
	public static native int getRouteTrafficCrowdColor(); 
	public static native int getRouteTrafficJamColor(); 
	public static native DataRouteTraffic[] getRouteTrafficInfo(); 
	// Restrict
	public static native int getRouteRestrictRouteId();
	public static native DataRestrict[] getRouteRestrictInfo();
	
	//guide point
	public static native void getGuidePointInfo(Object o);
	
	//static class
	public static class CurrentTime {
		public final int iYear;
		public final int iMonth;
		public final int iDay;
		public final int iHour;
		public final int iMinute;
		public final int iSecond;
		//don't delete it, define for jni
		public CurrentTime(int year, int mon, int day, int hour, int min, int sec) {
			iYear = year;
			iMonth = mon;
			iDay = day;
			iHour = hour;
			iMinute = min;
			iSecond = sec;
		}
	}

	public static class Visibility {
		public final boolean bPoi;
		public final boolean bCongestion;
		public final boolean bSmooth;
		public final boolean bRestriction;
		public final boolean bSpeedcamera;
		//don't delete it, define for jni
		public Visibility(boolean bPoi, boolean bCongestion, boolean bSmooth,
				boolean bRestriction, boolean bSpeedcamera) {
			this.bPoi = bPoi;
			this.bCongestion = bCongestion;
			this.bSmooth = bSmooth;
			this.bRestriction = bRestriction;
			this.bSpeedcamera = bSpeedcamera;
		}
		
	}

	public static class GuidepointDistance {
		public int iTarget;
		public int iID;
		public int iDistance;
		public String cDistance;
		public int iDistanceUnit;
		//don't delete or rename it, define for jni
		public void set(int iTarget, int iID, int iDistance,
				String cDistance, int iDistanceUnit) {
			this.iTarget = iTarget;
			this.iID = iID;
			this.iDistance = iDistance;
			this.cDistance = cDistance;
			this.iDistanceUnit = iDistanceUnit;
		}
		
	}

	public static class WaypointTime {
		public int iTarget;
		public int iID;
		public int iHour;
		public int iMinute;
		//don't delete or rename it, define for jni
		public void set(int iTarget, int iID, int iHour, int iMinute) {
			this.iTarget = iTarget;
			this.iID = iID;
			this.iHour = iHour;
			this.iMinute = iMinute;
		}
	}
	
	public static class DataARRoute {
		public double startLatitude, endLatitude, startLongitude, endLongitude;
		public int color;
		public DataARRoute(double startLatitude, double endLatitude,
				double startLongitude, double endLongitude, int color) {
			super();
			this.startLatitude = startLatitude;
			this.endLatitude = endLatitude;
			this.startLongitude = startLongitude;
			this.endLongitude = endLongitude;
			this.color = color;
		}
	}
	
	public static class DataTimeLine{
		public final int startDistance;
		public final int endDistance;
		public final int color;
		public DataTimeLine(int startDistance, int endDistance, int color) {
			this.startDistance = startDistance;
			this.endDistance = endDistance;
			this.color = color;
		}
	}
	
	public static class DataJunction{
		public final int dirction; 
		public final String roadNumber;
		public final int roadNumberType;
		public DataJunction(int dirction, String roadNumber, int roadNumberType) {
			this.dirction = dirction;
			this.roadNumber = roadNumber;
			this.roadNumberType = roadNumberType;
		}
	}
	
	public static class DataAroundSpeedCamera {
		public final double latitude;
		public final double longitude;
		public final int	type;
		public final int	displayGroup;
		public DataAroundSpeedCamera(double latitude, double longitude,
				int type, int displayGroup) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.type = type;
			this.displayGroup = displayGroup;
		}

	}
	
	public static class DataRouteSpeedCamera {
		public final double latitude;
		public final double longitude;
		public final int	distance;
		public final int	type;
		public final int	displayGroup;
		public DataRouteSpeedCamera(double latitude, double longitude,
				int	distance, int type, int displayGroup) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.distance = distance;
			this.type = type;
			this.displayGroup = displayGroup;
		}
	}
	
	public static class DataRouteTraffic{
		public final double startLatitude;
		public final double		startLongitude;
		public final int		startDistance;
		public final double		endLatitude;
		public final double		endLongitude;
		public final int		endDistance;
		public final int		routeTrafficType;
		public DataRouteTraffic(double startLatitude, double startLongitude,
				int startDistance, double endLatitude, double endLongitude,
				int endDistance, int routeTrafficType) {
			this.startLatitude = startLatitude;
			this.startLongitude = startLongitude;
			this.startDistance = startDistance;
			this.endLatitude = endLatitude;
			this.endLongitude = endLongitude;
			this.endDistance = endDistance;
			this.routeTrafficType = routeTrafficType;
		}
	}
	
	public static class DataRestrict{
		public double		latitude;	
		public double		longitude;	
		public int  	    startDistance;	
		public int		    sype;
		public DataRestrict(double latitude, double longitude,
				int startDistance, int sype) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.startDistance = startDistance;
			this.sype = sype;
		}	
		
	}
	
	public static native void HUDSetting(boolean bPoi, boolean bCongestion, boolean bRestriction,
			boolean bSpeedcamera, boolean bTimeFormat_24H, int[] LogoMarkItmes);
}
