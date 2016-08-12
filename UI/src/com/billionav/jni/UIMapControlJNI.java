package com.billionav.jni;

import android.util.Log;


public class UIMapControlJNI {
	
	public static short MAP_DIR_MODE_NORTHUP = 0;
	public static short MAP_DIR_MODE_HEADINGUP = 1;
	
	public static short MAP_VIEW_DIRECTION_PORTRAIT = 0;
	public static short MAP_VIEW_DIRECTION_LANDSCAPE = 1;
	
	public static int MAP_2D_NORTH_UP = 0;
	public static int MAP_2D_HEADING_UP = 1;
	public static int MAP_3D_SKY_VIEW = 2;
	public static int MAP_AUTO_VIEW = 3;
	
	public static int GetModelInfo(){return 0;}
	
	public static native void setCarMarkDisplay(boolean bDisplay);
	
	public static native void SetCarPositonMode(boolean bPosMode);
	public static native boolean GetCarPositonMode();
	
	public static native void SetMapViewDirection(short sMapViewDir);
	
//	public static final int MAP_COLOR_MODE_DAY = 0;
//	public static final int MAP_COLOR_MODE_NIGHT = 1;	
//	public static native void SetDayNightMode(int mode);
	
	public static final int MAP_TRAFFIC_STATE_OFF = 0x0; /* disable traffic */
	public static final int MAP_TRAFFIC_STATE_ALL_DISP = 0xFFFFFFFF; /* display all traffic line */
	public static final int MAP_TRAFFIC_STATE_JAMLINE_DISP = 0x1; /* �i�� */
	public static final int MAP_TRAFFIC_STATE_RUSHLINE_DISP = 0x2; /* ���j */
	public static final int MAP_TRAFFIC_STATE_SMOOTHLINE_DISP = 0x4; /* ��{ */
	public static final int MAP_TRAFFIC_STATE_REGLINE_DISP = 0x8; /*Ҏ�� */
	
	public static final int MAP_FONT_SIZE_BIG = 2;
	public static final int MAP_FONT_SIZE_MEDIUM= 1;
	public static final int MAP_FONT_SIZE_SMALL = 0;
	
	public static void SetTrafficState(boolean state)
	{
		if (state)
		{
			SetTrafficState(MAP_TRAFFIC_STATE_ALL_DISP);
		}
		else
		{
			SetTrafficState(MAP_TRAFFIC_STATE_OFF);
		}
		
	}
	public static void SetTrafficSwitcher(boolean state)
	{
		if (state)
		{
			SetTrafficSwitcher(MAP_TRAFFIC_STATE_ALL_DISP);
		}
		else
		{
			SetTrafficSwitcher(MAP_TRAFFIC_STATE_OFF);
		}
		
	}
	
	public static native void SetRegPlaceSwitch(int state);
	public static native void SetSNSMarkSwitch(int state);
	
	//map Screen info set
	public static native void SetTrafficState(int iState);
	public static native void SetSNSMarkState(int iState);
	public static native void SetGuidePointState(int iState);
	public static native void SetRegPlaceState(int iState);
	public static native void SetLogoMarkState(int iState);
	public static native void SetVicinityMarkState(int iState);
	public static native void SetOrbisMarkState(int iState);
	public static native void executeSets();
	
	
	//map switcher sets
	public static native void SetTrafficSwitcher(int isOpen);
	public static native int IsInCompassCircle(int iX, int iY);
	
	public static native int mapTapAction(int iX, int iY);
	
	 /**
     * Set map height
     * 
     * @param    long	:	map height you want to draw
     * @return   none
     */
	public static native void SetHeight(long dwHeight);
	
	/**
     * Get map height
     * 
     * @param    none
     * @return   long	:	map draw height
     */
	public static native long GetHeight();
	
	/**
     * Set Map Angle
     * 
     * @param    float	:	Map angle you want to display
     * @return   none
     */
	public static native void SetMapAngle(float fAngle);
	
	/**
     * Get Map Angle
     * 
     * @param    none
     * @return   float	:	Map angle you want to display
     */
	public static native float GetMapAngle();
	
	public static native float GetMaxAngle();
	
	public static native float GetMinAngle();
	
	/**
     * Set Map Dir
     * 
     * @param    short	:	Map Dir
     * @return   none
     */
	public static native void SetMapDir(short iMapDir);
	
	/**
     * Get Map Dir
     * 
     * @param    none
     * @return   short	:	Car Dir
     */
	public static native short GetMapDir();
	
	/**
     * set map center point info
     * 
     * @param    int		: center point lon
     * @param    int		: center point lat
     * @param    long		: map height
     * @return   none
     */
	public static native void SetCenterInfo(int iLon, int iLat, long lHeight);
	
	/**
     * Get Center LonLat
     * 
     * @param    none
     * @return   int	:	map center lon&Lat
     */
	public static native int[] GetCenterLonLat();
	
	/**
     * Set Screen ID 
     * 
     * @param    long	:	your screen ID
     * @return   none
     */
	public static native void SetScreenID(long dwScreenID);
	
	/**
     * scale change button pushed
     * 
     * @param    boolean	:	+(true)/-(false)
     * @return   none
     */	
	public static native void ScaleUpDown(boolean bUp);
	
	public static native float GetMaxScale();
	
	public static native float GetMinScale();
	
	public static native float GetCurrentScale();
	
	public static native void SetScaleBarPos(int iX, int iY, int iUnitKind);
	
	/**
     * Map Move
     * 
     * @param    long	:	start and stop position of screen
     * @return   none
     */
	public static native void MapMove(float startX, float startY, long startTimer, float stopX, float stopY, long stopTimer);
	
	/**
     * Start InertiaScroll
     * 
     * @param    long	:	start and stop position of screen
     * @return   none
     */
	public static native void StartInertiaScroll();
	
	/**
     * auto height move
     * 
     * @param    long	:	lon
     * @param    long	:	lat
     * @return   none
     */
	public static native void AutoHeightMove(long lLon, long lLat);
	
	/**
     * Map Rotate Scale
     * 
     * @param    long	:	start rotate and scale
     * @return   none
     */
	public static native void MapRotateScale(float s1X, float s1Y, float s2X, float s2Y, float e1X, float e1Y, float e2X, float e2Y);
	
	public static native void MapRotateScaleEnd();
	
	/**
     * Convert disp point To lonLat
     * 
     * @param    float		: disp point x/y
     * @return   int		: lon/lat
     */
	public static native int[] ConvertDispPointToLonLat(float fX, float fY);
	
	/**
     * Convert lonLat To disp point
     * 
     * @param    int		: lon/lat
     * @return   float		: disp point x/y
     */
	public static native float[] ConvertLonLatToDispPoint(int lon, int lat);

	/**
     * Set Screen ID With Offset
     * 
     * @param    long	:	your screen ID
     * 			 float  : 	Screen Center Offset
     * @return   none
     */
	public static native void SetScreenIDWithOffset(long dwScreenID, float fOffsetScaleX, float fOffsetScaleY);

	public static double[] MapConvertLonlatToWorld(long lon, long lat){
		
		double[] lonlat = new double[2];
		lonlat[0] = lon/3600.0/256.0;
		lonlat[1] = lat/3600.0/256.0;
		return lonlat;
	}
	
	public static native void requestAreaName(long longitude, long latitude);
		
	public static native String getAreaName();
	
	public static native void setMapFontSize(int option);
	
	// MapReqTouchedPoint
	public static native void ReqMapTouchedPoint(int iLongitude, int iLatitude);
	
	// MarkType
	public static final int MAP_MARK_TYPE_LOGOMARK			= 0;
	public static final int MAP_MARK_TYPE_VICINITY			= 1;
	public static final int MAP_MARK_TYPE_TRAFFIC			= 2;
	public static final int MAP_MARK_TYPE_MAP_SYMBOL		= 3;
	public static final int MAP_MARK_TYPE_USER_REPORT		= 4;
	public static final int MAP_MARK_TYPE_POINT				= 5;
	
	// CATEGORY
	public static final int MAP_CATEGORY_TRAFFICJAM			= 1;
	public static final int MAP_CATEGORY_TRAFFICACCIDENT	= 2;
	public static final int MAP_CATEGORY_ROADCLOSE         	= 3;
	public static final int MAP_CATEGORY_TRAFFICCONTROL    	= 4;
	public static final int MAP_CATEGORY_ROADDAMAGE        	= 5;
	public static final int MAP_CATEGORY_WATERFROZEN       	= 6;
	public static final int MAP_CATEGORY_SPEEDCAMERA       	= 7;        
	public static final int MAP_CATEGORY_POLICE            	= 8;
	public static final int MAP_CATEGORY_OTHER             	= 9;
	
	// POINT_TYPE
	public static final int MAP_POINT_TYPE_INVALID			= 0;
	public static final int MAP_POINT_TYPE_REGPOINT			= 1;
	public static final int MAP_POINT_TYPE_HISPOINT			= 2;
	public static final int MAP_POINT_TYPE_HOME				= 3;
	public static final int MAP_POINT_TYPE_COMPANY			= 4;
	
	// MapLonLat
	public static class MapLonLat {
		public int		iLon;
		public int		iLat;
		//don't delete or rename it, define for jni
		public MapLonLat(int iLongitude, int iLatitude) {
			Log.i("LonLat","UIMapControlJNI:LonLat");
			this.iLon		= iLongitude;
			this.iLat		= iLatitude;
		}
		public MapLonLat(MapLonLat cLonLat) {
			Log.i("LonLat","UIMapControlJNI:MapLonLat");
			this.iLon		= cLonLat.iLon;
			this.iLat		= cLonLat.iLat;
		}
	}
	
	// LogoMark
	public static class LogoMark {
		public int			index;
		public int			type;
		public MapLonLat	lonlat;
		//don't delete or rename it, define for jni
		public LogoMark(int iIndex, int eMarkType, MapLonLat cLonLat) {
			Log.i("LonLat","UIMapControlJNI:LogoMark");
			this.index		= iIndex;
			this.type		= eMarkType;
			this.lonlat		= new MapLonLat(cLonLat);
		}
	}
	
	// Vicinity
	public static class Vicinity {
		public int			index;
		public int			type;
		public MapLonLat	lonlat;
		//don't delete or rename it, define for jni
		public Vicinity(int iIndex, int eMarkType, MapLonLat cLonLat) {
			Log.i("LonLat","UIMapControlJNI:Vicinity");
			this.index		= iIndex;
			this.type		= eMarkType;
			this.lonlat		= new MapLonLat(cLonLat);
		}
	}
	
	// Traffic
	public static class Traffic {
		public int			index;
		public int			type;
		public MapLonLat	lonlat;
		//don't delete or rename it, define for jni
		public Traffic(int iIndex, int eMarkType, MapLonLat cLonLat) {
			Log.i("LonLat","UIMapControlJNI:Traffic");
			this.index		= iIndex;
			this.type		= eMarkType;
			this.lonlat		= new MapLonLat(cLonLat);
		}
	}
	
	// MapSymbol
	public static class MapSymbol {
		public int			index;
		public int			type;
		public String		strname;
		public MapLonLat	lonlat;
		//don't delete or rename it, define for jni
		public MapSymbol(int iIndex, int eMarkType, String strName, MapLonLat cLonLat) {
			Log.i("LonLat","UIMapControlJNI:MapSymbol");
			this.index		= iIndex;
			this.type		= eMarkType;
			this.strname	= new String(strName);
			this.lonlat		= new MapLonLat(cLonLat);
		}
	}
	
	// UserReport
	public static class UserReport {
		public int			index;
		public int			type;
		public String		postID;
		public MapLonLat	lonlat;
		public int			category; // CATEGORY
		//don't delete or rename it, define for jni
		public UserReport(int iIndex, int eMarkType, String postID, MapLonLat cLonLat, int category) {
			Log.i("LonLat","UIMapControlJNI:UserReport");
			this.index		= iIndex;
			this.type		= eMarkType;
			this.postID		= new String(postID);
			this.lonlat		= new MapLonLat(cLonLat);
			this.category	= category;
		}
	}
	
	// Point
	public static class Point {
		public int			index;
		public int			type;
		public String		strname;
		public MapLonLat	lonlat;
		public int			category; // POINT_TYPE
		public String		straddress;
		//don't delete or rename it, define for jni
		public Point(int iIndex, int eMarkType, String strName, MapLonLat cLonLat, int eCategory, String strAddress) {
			Log.i("LonLat","UIMapControlJNI:Point");
			this.index		= iIndex;
			this.type		= eMarkType;
			this.strname	= new String(strName);
			this.lonlat		= new MapLonLat(cLonLat);
			this.category	= eCategory;
			this.straddress	= new String(strAddress);
		}
	}
	
	// MapTouchedMarkInfo
	public static class MapTouchedMarkInfo {
		public LogoMark		LogoMarkItems[]		= null;
		public Vicinity		VicinityItems[]		= null;
		public Traffic		TrafficItems[]		= null;
		public MapSymbol	MapSymbolItems[]	= null;
		public UserReport	UserReportItems[]	= null;
		public Point		PointItems[]		= null;
		//don't delete or rename it, define for jni
		public MapTouchedMarkInfo(LogoMark[] cLogoMarkItems, Vicinity[] cVicinityItems,
				Traffic[] cTrafficItems, MapSymbol[] cMapSymbolItems, UserReport[] cUserReportItems, Point[] cPointItems) {
			this.LogoMarkItems	= new LogoMark[cLogoMarkItems.length];
			if (null != cLogoMarkItems) {
				// Shallow copy
				System.arraycopy(cLogoMarkItems, 0, this.LogoMarkItems, 0, cLogoMarkItems.length);
			}
			
			this.VicinityItems	= new Vicinity[cVicinityItems.length];
			if (null != cVicinityItems) {
				// Shallow copy
				System.arraycopy(cVicinityItems, 0, this.VicinityItems, 0, cVicinityItems.length);
			}
			
			this.TrafficItems	= new Traffic[cTrafficItems.length];
			if (null != cTrafficItems) {
				// Shallow copy
				System.arraycopy(cTrafficItems, 0, this.TrafficItems, 0, cTrafficItems.length);
			}
			
			this.MapSymbolItems	= new MapSymbol[cMapSymbolItems.length];
			if (null != cMapSymbolItems) {
				// Shallow copy
				System.arraycopy(cMapSymbolItems, 0, this.MapSymbolItems, 0, cMapSymbolItems.length);
			}
			
			this.UserReportItems = new UserReport[cUserReportItems.length];
			if (null != cUserReportItems) {
				// Shallow copy
				System.arraycopy(cUserReportItems, 0, this.UserReportItems, 0, cUserReportItems.length);
			}
			
			this.PointItems	= new Point[cPointItems.length];
			if (null != cPointItems) {
				// Shallow copy
				System.arraycopy(cPointItems, 0, this.PointItems, 0, cPointItems.length);
			}
		}
	}
	
	// MapReqTouchedPoint
	public static native MapTouchedMarkInfo GetMapTouchedMarkInfo();
	
	// MapScaleOperation
	public static native void MapScaleOperation(boolean bUpFlag);

	// MapOrbisSetting
	public static native void SetOrbisSwitch(boolean value);
	
	public static native void reqPinPoint(int lon, int lat);
}
