package com.billionav.jni;


import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PConnectReceiver;

public class UIPointControlJNI {
	//PntToUIResponse_PNT_RESULT 
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_UNKNOWN = 0;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_FAILURE = 1;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS = 2;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_PART_SUCCESS = 3;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_INITIALIZING = 4;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_SYNCING = 5;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_INVALID_INPUT = 6;
	public static final int PntToUIResponse_PNT_RESULT_PNT_RESULT_GET_PBPARAM = 7;
	
	public static final int PntToUIResponse_PNT_RESULT_PNT_LOG_STATUS_INVALID = 0;
	public static final int PntToUIResponse_PNT_RESULT_PNT_LOG_STATUS_LOGIN = 1;
	public static final int PntToUIResponse_PNT_RESULT_PNT_LOG_STATUS_LOGOUT = 2;
	
	public static final int RESULT_POINT_INVALID_ID = 65535;
	public static final int MAXIMUM_FAVORITE_POINTS = 1000;
	
	public static final int PntToUIResponse_PNT_POINT_TYPE_INVALID              = 0;
	public static final int PntToUIResponse_PNT_POINT_TYPE_REGPOINT             = 1;
	public static final int PntToUIResponse_PNT_POINT_TYPE_HISPOINT             = 2;
	public static final int PntToUIResponse_PNT_POINT_TYPE_HOME                 = 3;
	public static final int PntToUIResponse_PNT_POINT_TYPE_COMPANY              = 4;
	public static final int PntToUIResponse_PNT_POINT_TYPE_PRESET1              = 5;
	public static final int PntToUIResponse_PNT_POINT_TYPE_PRESET2              = 6;
	public static final int PntToUIResponse_PNT_POINT_TYPE_PRESET3              = 7;
	
	private boolean homeRequested;
	private boolean companyRequested;
	
    private static UIPointControlJNI instance;
    
	private UIPointControlJNI(){}
	
	public static UIPointControlJNI Instance()
	{
		if(instance == null){
			instance = new UIPointControlJNI();
		}
		return instance;
	}
	
	public UIPointData[] getBookmarkData(){
		final int count = getBookmarkCount();
		UIPointData[] bookmark = new UIPointData[count];
		for(int i = 0; i < count; i++){
			bookmark[i] = getBookmarkAtItem(i);
		}
		return bookmark;
	}
	
	public native void reqAddFavorite(UIPointData d);
	public native void reqAddHistroy(UIPointData d);
	public native void reqAddHome(UIPointData d);
	public native void reqAddCompany(UIPointData d);
	
	
	public native void reqGetFavorite(String str);
	public native void reqGetHistroy(String str);
	public native void reqGetHome();
	public native void reqGetCompany();
	
	public native int getBookmarkCount();
	public native UIPointData[] getFavoriteList();
	public native UIPointData[] getHistroyList();
	public native UIPointData getBookmarkAtItem(int index);
	public native UIPointData getHome();
	public native UIPointData getCompany();
	public native boolean isHomeExist();
	public native boolean isComapanyExist();
	
	public native void reqDeleteFavorites(int[] ids);
	public native void reqDeleteHistroies(int[] ids);
	
	public void reqDeleteFavorite(int id){
		reqDeleteFavorites(new int[]{id});
	}
	public void reqDeleteHistroy(int id){
		reqDeleteHistroies(new int[]{id});
	}
	public native UIPointData GetPointDataByRecordID(int iRecordID);
	
	public native void reqUpdatePoint(int seqId, UIPointData d);
	public native void reqFindPoints(int count, String[] name,long[] lon, long[] lat);
	public native int[] getPointSeqIds();
	public native void reqFindPoint(String name, long lon, long lat);
	
	public native void reqSyncFavorites();
	public native void reqSyncHistroies();
	public native void reqSyncHome();
	public native void reqSyncCompany();
	public native void reqPhotoPath();
	
	public void setHomeRequested() {
		homeRequested = true;
	}
	
	public void setCompanyRequested() {
		companyRequested = true;
	}
	
	public boolean isHomeRequested(){
		return homeRequested;
	}
	public boolean isCompanyRequested(){
		return companyRequested;
	}
	
	/**
	 * trigger operation
	 * 
	 * @param cTriggerInfo
	 */

	public final boolean isWifiEnable() {
		return (PConnectReceiver.getConnectType() == PConnectReceiver.CONNECT_TYPE_WIFI);
	}

}
