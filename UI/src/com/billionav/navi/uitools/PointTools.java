package com.billionav.navi.uitools;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.billionav.jni.UICalcDistanceJNI;
import com.billionav.jni.UICalcDistanceJNI.MapLonLat;
import com.billionav.jni.UICalcDistanceJNI.PreciseDistance;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchDetailInfoResultJNI;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.system.PLog;

/**
 * 
 * @author liuzhaofeng
 *
 */
public class PointTools implements TriggerListener{
	
	private static final int POI_BEAN_CACHE_SIZE = 10;
	private String pointPhotoPath = Environment.getExternalStorageDirectory().getPath()+"/";
	
	public interface PointDetailInfoListener{
		void onPointDetailInfoRequstFinished(String name, String address, String tel, long distance);
	}
	
	private class PointDetailInfoListenerObj{
		private int poilonlat[];
		private String listenerTag;
		private PointDetailInfoListener listener;
	}
	private ArrayList<PointDetailInfoListenerObj> pointDetailListenerList;
	private ArrayList<POIInfoBean> poiCacheList;
	private String pointName = "";
	
	private static final PointTools instance = new PointTools();
	
	public static PointTools getInstance(){
		return instance;
	}
	
	private PointTools(){
		pointDetailListenerList = new ArrayList<PointDetailInfoListenerObj>();
		poiCacheList = new ArrayList<POIInfoBean>();
		GlobalTrigger.getInstance().addTriggerListener(this);
//		Log.i("icon", "i have invoked reqPhotoPath() ... ");
//		UIPointControlJNI.Instance().reqPhotoPath();
	}
	
	public void requestPointName(long lon, long lat, final PointDetailInfoListener pointListener){
		if(null == pointListener) {
			return;
		}
		final POIInfoBean cacheBean = getCachedPoiInfo(lon, lat);
		if(null != cacheBean && !cacheBean.getName().equals("")) {
			Log.d("UIMsgControl", "requestPointName : POIInfoBean.name = " + cacheBean.getName());
			new Handler().post(new Runnable() {
				
				@Override
				public void run() {
					pointListener.onPointDetailInfoRequstFinished(cacheBean.getName(), 
							cacheBean.getAddress(), cacheBean.getTelNo(), cacheBean.getDistence());
					
				}
			});
			return;
		}
		
//		pointListener.getClass().toString();
//		this.pointDetailListener = pointListener;
//		pointName = "";
		addPointListner((int)lon, (int)lat, pointListener);
//		UISearchControlJNI.Instance().ReqPinPoint(SearchTools.createLonLat((int)lon, (int)lat));
		
		Log.i("LonLat","PointTools:requestPointName");
		Log.i("LonLat","lon="+(int)lon+"\tlat="+(int)lat);
		UIMapControlJNI.reqPinPoint((int)lon, (int)lat);

	}
	
	public void requestCenterName(PointDetailInfoListener pointListener){
		int[] lonlat = UIMapControlJNI.GetCenterLonLat();
		requestPointName(lonlat[0], lonlat[1], pointListener);
	}
	public void removePointListener(){
		
	}
	
	private void addPointListner(int lon, int lat, PointDetailInfoListener pointListener) {
		PointDetailInfoListenerObj obj = new PointDetailInfoListenerObj();
		obj.listenerTag = pointListener.getClass().toString();
		obj.listener = pointListener;
		obj.poilonlat = new int[2];
		obj.poilonlat[0] = lon;
		obj.poilonlat[1] = lat;
		pointDetailListenerList.add(obj);
	}
	public void removePointListener(PointDetailInfoListener pointListener) {
		if(null == pointListener ) {
			return;
		}
		PointDetailInfoListenerObj tempListener = null;
		do{
			String listenerTAG = pointListener.getClass().toString();
			tempListener = null;
			for(PointDetailInfoListenerObj listener :pointDetailListenerList) {
				if(listenerTAG.equals(listener.listenerTag)) {
					tempListener = listener;
					break;
				}
			}
			pointDetailListenerList.remove(tempListener);
		}while(tempListener != null);
	}
	
	public String getPointName() {
		return pointName;
	}
	
	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_POINT_ROOT_PHOTO_PATH){
			pointPhotoPath = triggerInfo.GetString1();
			return true;
		}
		if(triggerInfo.m_iTriggerID != NSTriggerID.SEARCH_RESPONSE_PINPOINT_INFO){
			return false;
		}
//		UISearchDetailInfoResultJNI detailInfo = UISearchControlJNI.Instance().GetDetailInfoResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT);
		pointName = triggerInfo.m_String1;//detailInfo.GetPinPointName();
		Log.d("test", "point name = " + pointName);
//		long lonlat[] = detailInfo.GetPinPointLonLat();
//		pushPoiSearchResult(detailInfo);
		PointDetailInfoListenerObj toBeDel = null;
		for(PointDetailInfoListenerObj obj:pointDetailListenerList) {
//			if(obj != null){
//				if(obj.poilonlat[0] ==lonlat[0] && obj.poilonlat[1] ==lonlat[1]) {
					//obj.listener.onPointDetailInfoRequstFinished(pointName, detailInfo.GetPinPointAddress(), detailInfo.GetPinPointTel(), detailInfo.GetPinPointDistance());
					obj.listener.onPointDetailInfoRequstFinished(pointName, null, null, 0);
					toBeDel = obj;
					break;
//				}			
//			}
		}
		if(toBeDel != null) {
			pointDetailListenerList.remove(toBeDel);
		}
		
		return false;
	}
	private void pushPoiSearchResult(UISearchDetailInfoResultJNI detailInfo) {
		if(null == detailInfo) {
			return;
		}
		if(poiCacheList.size() == POI_BEAN_CACHE_SIZE) {
			poiCacheList.remove(0);
		}
		poiCacheList.add(inflatePoiData(detailInfo));
	}
	
	private POIInfoBean getCachedPoiInfo(long requestLon, long requestLat) {
		for(POIInfoBean bean :poiCacheList) {
			if(bean.getLongitude() == requestLon && bean.getLatitude() == requestLat) {
				return bean;
			}
		}
		return null;
	}
	
	private POIInfoBean inflatePoiData(UISearchDetailInfoResultJNI detailInfo) {
		if(null == detailInfo) {
			return null;
		}
		POIInfoBean bean = new POIInfoBean();
		long []lonlat = detailInfo.GetPinPointLonLat();
		bean.setLongitude(lonlat[0]);
		bean.setLatitude(lonlat[1]);
		bean.setName(detailInfo.GetPinPointName());
		bean.setDistence(detailInfo.GetPinPointDistance());
		bean.setTelNo(detailInfo.GetPinPointTel());
		bean.setAddress(detailInfo.GetPinPointAddress());
		return bean;
	}
	
	public static long calcDistance(long[] startLonLat, long[] endLonLat){
		Log.i("LonLat","PointTools:calcDistance");
		
		MapLonLat sStartLonLat = new MapLonLat();
		MapLonLat sEndLonLat = new MapLonLat();
		
		sStartLonLat.Longitude = startLonLat[0];
		sStartLonLat.Latitude = startLonLat[1];	
		
		sEndLonLat.Longitude = endLonLat[0];
		sEndLonLat.Latitude = endLonLat[1];
		
		PreciseDistance pd = new PreciseDistance();
		UICalcDistanceJNI.CalcPreciseDistance(sStartLonLat, sEndLonLat, pd);
		
		return pd.mDistance;
	}


	public void createFaveratePhoto(String uuid ,Bitmap bitmap) {
		File dir = new File(pointPhotoPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(pointPhotoPath+uuid);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
		      bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		       out.flush();
		       out.close();
		}
		catch (Exception e) {
			PLog.i("Exception", "create photo file failed");
		}
	}
 	
	public boolean deleteFaveratePhoto(String uuid){
		File file = new File(pointPhotoPath+uuid);
		if (file.exists()) {
			boolean b = file.delete();
			return b;
		}
		else{
			return false;
		}
	}
 	
//	public boolean deleteFaveratePhoto(int uuid){
//		File file = new File(pointPhotoPath+uuid);
//		if (file.exists()) {
//			boolean b = file.delete();
//			return b;
//		}
//		else{
//			return false;
//		}
//	}
 	
	public Uri returnFaveratePhoto(String uuid){
		File f = new File(pointPhotoPath+uuid);
		if(f.exists()){
			return Uri.parse(pointPhotoPath+uuid);
		}else{
			return null;
		}
		}
//	
	
	
	
}
