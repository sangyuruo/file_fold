package com.billionav.navi.uitools;

import java.io.Serializable;

import android.graphics.Point;
import android.util.Log;

import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.AL_LonLat;
import com.billionav.jni.UISearchDetailInfoResultJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.naviscreen.map.TouchMapControl;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;


public class SearchPointBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6617236751295518199L;
	
	private int searchkind;
	private int generID;
	private int index;
	private String name;
	private long longitude;
	private long latitude;
	private String telNo;
	private long distence;
	private String address;
	private String openTime;
	private String commentInfo;
	private int seqId;
	public int getSeqId() {
		return seqId;
	}

	public int getGenerID() {
		return generID;
	}
	
	public void setGenerID(int generID) {
		this.generID = generID;
	}
	
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	private SearchPointBean() {}
	
	public String getName() {
		return name;
	}
	
	public long getLongitude() {
		return longitude;
	}
	
	public long getLatitude() {
		return latitude;
	}
	
	public long[] getLonlat() {
		return new long[] {longitude, latitude};
	}
	
	
	public String getTelNo() {
		return telNo;
	}
	
	public int getSearchkind() {
		return searchkind;
	}
	
	public int getIndex() {
		return index;
	}
	
	public long getDistence() {
		return distence;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getOpenTime() {
		return openTime;
	}
	
	public String getCommentInfo() {
		return commentInfo;
	}
	
	public PointBean obtainPontBeanForPopup(){
		PointBean point = new PointBean();
		point.setName(name);
		point.setAddress(address);
		point.setIndex(index);
		point.setLonlat(new long[]{longitude, latitude});
		point.setType(TouchMapControl.getTouchTypeFromSearchType(searchkind));
		return point;
	}
	
	public static SearchPointBean obtainPointBeanToSearchBean(PointBean point){
		SearchPointBean bean = new SearchPointBean();
		bean.index = point.getIndex();
		bean.name = point.getName();
		bean.address = point.getAddress();
		bean.telNo = point.getTelNo();
		bean.longitude = point.getLonlat()[0];
		bean.latitude = point.getLonlat()[1];
		bean.searchkind = point.getType();
		return bean;
	}
	
	public Point getPositionInScreen() {
		float[] point = UIMapControlJNI.ConvertLonLatToDispPoint((int)longitude, (int)latitude);
		return new Point((int)point[0], (int)point[1]);
	}
	
	private static int getIndexFromLonLat(int lon, int lat, UISearchDetailInfoResultJNI mDetailResult) {
		AL_LonLat tap_Lonlat = SearchTools.createLonLat(lon, lat);
		long ret = UISearchControlJNI.Instance().OnTouchMap(tap_Lonlat);
		if (ret != UISearchControlJNI.UIC_SCM_SCREEN_NOWAIT) {
			return -1;
		}

		if (mDetailResult != null) {
			return (int)mDetailResult.GetTouchedPOIIndex();
		} else {
			return -1;
		}
		
	}
	
	public static SearchPointBean getSearchPointBeanByTapMap(float x, float y, int searchkind) {
		int[] lonlat = UIMapControlJNI.ConvertDispPointToLonLat(x, y);
		
		UISearchDetailInfoResultJNI mDetailResult = UISearchControlJNI.Instance().GetDetailInfoResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_CHECK_TOUCH_POINT);
		int index = getIndexFromLonLat(lonlat[0], lonlat[1], mDetailResult);
		
		if(index < 0){
			return null;
		}
		return createSearchPointBean(mDetailResult, index);
	}
	
	public static SearchPointBean createSearchPointBean(UISearchDetailInfoResultJNI detailResult, int index) {
		
		SearchPointBean bean = new SearchPointBean();
		bean.index = index;
		
		bean.searchkind = detailResult.GetType();
		
		int listid = (bean.searchkind == UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL) ? 
				UISearchControlJNI.UIC_SCM_LIST_ID_MAP_SYMBOL : UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI;
		
		if(bean.searchkind == UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT) {
			bean.name = detailResult.GetPinPointName();
			bean.telNo = detailResult.GetPinPointTel();
			bean.distence = detailResult.GetPinPointDistance();
			bean.address =detailResult.GetPinPointAddress();
			long[] lonlat = detailResult.GetPinPointLonLat();
			bean.longitude = lonlat[0];
			bean.latitude = lonlat[1];
			bean.searchkind = TouchMapControl.TYPE_LONGPRESS;
			
			Log.i("msg", "bean.name::"+bean.name+" bean.telNo::"+bean.telNo+" bean.distence::"+bean.distence+" lonlat::"+bean.longitude+" "+bean.latitude);
		} else {
			bean.name = detailResult.GetListItemNameAt(index, listid);
			bean.telNo = detailResult.GetPOIListItemTelNoAt(index, listid);
			long[] lonlat = detailResult.GetPOIListItemLonLatAt(index, listid);
			bean.longitude = lonlat[0];
			bean.latitude = lonlat[1];
			
			bean.distence = detailResult.GetPOIListItemDistanceAt(index, listid);
			bean.address  = detailResult.GetPOIListItemAddressAt(index, listid);
			bean.openTime = detailResult.GetPOIListItemOpeningTimeAt(index, listid);
			bean.commentInfo = detailResult.GetPOIListItemCommentAt(index, listid);
		}
		
		return bean;
	}

	public static SearchPointBean createSearchPointBean(UISearchResultJNI searchlResult, int index) {
		SearchPointBean bean = new SearchPointBean();
		bean.index = index;
		
		bean.searchkind = searchlResult.GetType();
		bean.name = searchlResult.GetListItemNameAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		bean.telNo = searchlResult.GetPOIListItemTelNoAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		long[] lonlat = searchlResult.GetPOIListItemLonLatAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		int genreID = (int)searchlResult.GetListItemGenreIDAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		bean.generID = genreID;
		bean.longitude = lonlat[0];
		bean.latitude = lonlat[1];
		bean.distence = searchlResult.GetPOIListItemDistanceAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		bean.address  = searchlResult.GetPOIListItemAddressAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		bean.openTime = searchlResult.GetPOIListItemOpeningTimeAt(index, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);

		return bean;
	}	
	
//	public static SearchPointBean createSearchPointBean(jniUIC_SCM_LogoMarkResult logomarkResult, int index) {
//		SearchPointBean bean = new SearchPointBean();
//		bean.index = index;
//		
//		bean.searchkind = logomarkResult.GetType();
//		bean.name = logomarkResult.GetListItemNameAt(index, UISearchControlJNI.UIC_SCM_LIST_ID_NORMAL);
//		bean.telNo = logomarkResult.GetPOIListItemTelNoAt(index, UISearchControlJNI.UIC_SCM_LIST_ID_NORMAL);
//		long[] lonlat = logomarkResult.GetPOIListItemLonLatAt(index, UISearchControlJNI.UIC_SCM_LIST_ID_NORMAL);
//		bean.longitude = lonlat[0];
//		bean.latitude = lonlat[1];
//		bean.distence = logomarkResult.GetPOIListItemDistanceAt(index, UISearchControlJNI.UIC_SCM_LIST_ID_NORMAL);
//		bean.address  = logomarkResult.GetPOIListItemAddressAt(index, UISearchControlJNI.UIC_SCM_LIST_ID_NORMAL);
//		bean.openTime = "";
//
//		return bean;
//	}
//	public static SearchPointBean createSearchPointBeanFromPOI(){
//		SearchPointBean bean = new SearchPointBean();
//		jniPOIMemoryData data = jniPOIMemoryControl.Instance().GetPOIMemoryData(jniPOIMemoryControl.UIC_PM_POIDISPPOINT);
//		bean.index = -1;
//		bean.name = data.GetDispName();
//		bean.telNo = data.GetTel();
//		bean.longitude = data.GetCenterLon();
//		bean.latitude = data.GetCenterLat();
//		bean.distence = getDistance(bean);
//		bean.address  = data.GetAddress();
//		bean.openTime = "";
//		return bean;
//	}
	private static long getDistance(SearchPointBean bean){
		long[] 	carLonlat = new long[2];
		
		carLonlat[0] = UILocationControlJNI.GetPosition(0)[0];
		carLonlat[1] = UILocationControlJNI.GetPosition(0)[1];
		return PointTools.calcDistance(new long[]{bean.longitude, bean.latitude}, carLonlat);
	}
}
