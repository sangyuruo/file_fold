package com.billionav.navi.uitools;

import android.util.SparseIntArray;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.AL_LonLat;
import com.billionav.ui.R;

public class SearchTools {
	private SearchTools(){}
	
	public static String getErrorType(int errorType){
		switch(errorType){
		case UISearchControlJNI.UIC_SCM_ERROR_TYPE_SERVER:
			return "UIC_SCM_ERROR_TYPE_SERVER";
		case UISearchControlJNI.UIC_SCM_ERROR_TYPE_NET:
			return "UIC_SCM_ERROR_TYPE_NET";
		case UISearchControlJNI.UIC_SCM_ERROR_TYPE_DATA:
			return "UIC_SCM_ERROR_TYPE_DATA";
		case UISearchControlJNI.UIC_SCM_ERROR_TYPE_OPER:
			return "UIC_SCM_ERROR_TYPE_OPER";
		case UISearchControlJNI.UIC_SCM_ERROR_TYPE_INNER:
			return "UIC_SCM_ERROR_TYPE_INNER";
		default:
			return "UIC_SCM_ERROR_TYPE_NO_ERROR";
		}
	}
	
	public static AL_LonLat getCenterLonlat() {
		int[] lonlat = UIMapControlJNI.GetCenterLonLat();
		return  new AL_LonLat(lonlat[0], lonlat[1]);
	}
	
	public static AL_LonLat createLonLat(int[] lonlat) {
		return  new AL_LonLat(lonlat[0], lonlat[1]);
	}

	public static AL_LonLat createLonLat(int lon, int lat) {
		return  new AL_LonLat(lon, lat);
	}
	
	public static SearchPointBean getSearchPointInScreen(int x, int y, int searchkind){
		return SearchPointBean.getSearchPointBeanByTapMap(x, y, searchkind);
	}
	
	public static int getMarkByCategoryCode(int code) {
		return markMap.get(code, R.drawable.navicloud_and_249a);
	}
	
	private static final SparseIntArray markMap;
	
	static {
		markMap = new SparseIntArray();
		markMap.put(16842752, R.drawable.navicloud_and_241a);
		markMap.put(252248064, R.drawable.navicloud_and_242a);
		markMap.put(83886080, R.drawable.navicloud_and_243a);
		markMap.put(134217728, R.drawable.navicloud_and_244a);
		markMap.put(100728832, R.drawable.navicloud_and_245a);
		markMap.put(167837696, R.drawable.navicloud_and_248a);
		markMap.put(268632064, R.drawable.navicloud_and_246a);
		markMap.put(151060480, R.drawable.navicloud_and_247a);
		markMap.put(1661009921, R.drawable.navicloud_and_239a);
		markMap.put(2130706433, R.drawable.navicloud_and_241a);
		markMap.put(2130706434, R.drawable.navicloud_and_242a);
		markMap.put(2130706435, R.drawable.navicloud_and_243a);
		markMap.put(2130706436, R.drawable.navicloud_and_244a);
		markMap.put(2130706437, R.drawable.navicloud_and_245a);
		markMap.put(2130706439, R.drawable.navicloud_and_246a);
		markMap.put(2130706440, R.drawable.navicloud_and_247a);
		markMap.put(2130706438, R.drawable.navicloud_and_248a);
		markMap.put(2130706441, R.drawable.navicloud_and_239a);
		
		markMap.put(-1, R.drawable.navicloud_and_249a);
	}
}
