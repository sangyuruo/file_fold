package com.billionav.navi.naviscreen.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import android.text.TextUtils;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchDetailInfoResultJNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.report.ADT_report_detail;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.navi.uitools.SearchTools;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class TouchMapControl {
	
	public static final int TYPE_LONGPRESS = -200;
	
//	public static Object tapMap(float x, float y) {
//		
//		int[] lanlat = UIMapControlJNI.ConvertDispPointToLonLat(x, y);
//		
//		UISearchControlJNI searchControl = UISearchControlJNI.Instance();
//		long statuscode = searchControl.OnTouchMap(SearchTools.createLonLat(lanlat));
//		
////		if(statuscode != jniSearchControl.UIC_SCM_SCREEN_NOWAIT && statuscode != jniSearchControl.UIC_SCM_SCREEN_WAIT) {
////			return null;
////		}
//		
//		UISearchDetailInfoResultJNI detailInfo = searchControl.GetDetailInfoResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_CHECK_TOUCH_POINT);
//		
//		if(detailInfo.GetTouchedMarkListCount() <= 0){
//			return null;
//		}
//		
//		if(detailInfo.GetTouchedMarkListCount() == 1) {
//			int type = detailInfo.GetTouchedMarkType(0);
//			
//			String name = detailInfo.GetTouchedMarkName(0);
//			int index = detailInfo.GetTouchedMarkIndex(0);
//			
//			int categroy = detailInfo.GetTouchedMarkCategory(0);
//			if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_MAP_SYMBOL && TextUtils.isEmpty(name)) {
//				UISearchControlJNI.Instance().OnUpdateDetailInfo(UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL, index);
//				name = NaviViewManager.GetViewManager().getString(R.string.STR_COM_018);
//			}else if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_TRAFFIC){
////				name = jniTrafficControl.instance().GetEventStringByIconIndex(index);
////				categroy = jniTrafficControl.instance().GetEventMarkIDByIconIndex(index);
//			}else if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_USER_REPORT) {
//				BundleNavi.getInstance().putInt("index", index);
//				MenuControlIF.Instance().ForwardWinChange(ADT_report_detail.class);
//				return null;
//			}
//			
//			long[] lonlat = detailInfo.GetTouchedMarkLonLat(0);
//			String address = detailInfo.GetTouchedMarkAddress(0);
//			String time = detailInfo.GetTouchedMarkTime(0);
//			
//			
//			PointBean point = new PointBean();
//			point.name = name;
//			point.lonlat = lonlat;
//			point.index = index;
//			point.type = type;
//			point.category = categroy;
//			point.address = address;
//			point.time = time;
//			return point;
//
//		} else {
//			int count = (int) detailInfo.GetTouchedMarkListCount();
//			
//			ArrayList<PointBean> list = new ArrayList<TouchMapControl.PointBean>(count);
//			
//			for(int i=0; i<count; i++) {
//				int type = detailInfo.GetTouchedMarkType(i);
//				String name = "";
//				String summary = "";
//				int categroy =  -1;
//				int index = detailInfo.GetTouchedMarkIndex(i);
//				if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_TRAFFIC){
////					name = jniTrafficControl.instance().GetEventStringByIconIndex(index)+" ";
////					summary = jniTrafficControl.instance().GetEventStartTimeByIconIndex(index)+"~"+jniTrafficControl.instance().GetEventEndTimeByIconIndex(index);
////					categroy = jniTrafficControl.instance().GetEventMarkIDByIconIndex(index);
////					int markid = jniTrafficControl.instance().GetEventMarkIDByIconIndex(i);
////					Log.d("icon","markid = "+markid);
//				}else{
//					name = detailInfo.GetTouchedMarkName(i);
//					categroy = detailInfo.GetTouchedMarkCategory(i);
//				}
//				
//				long[] lonlat = detailInfo.GetTouchedMarkLonLat(i);
//				String address = detailInfo.GetTouchedMarkAddress(i);
//				String time = detailInfo.GetTouchedMarkTime(i);
//				
//				PointBean point = new PointBean();
//				point.summary = summary;
//				point.name = name;
//				point.lonlat = lonlat;
//				point.index = index;
//				point.type = type;
//				point.category = categroy;
//				point.address = address;
//				point.time = time;
//				list.add(point);
//			}
//			
//			return list;
//
//		}
//		
//	}
	
//	private static int getSearchTypeFromMarkType(int marktype) {
//		switch(marktype) {
//		case jniSearchControl.UIC_SCM_TOUCHED_MARK_TYPE_FREEWORD:
//			return jniSearchControl.UIC_SCM_SRCH_TYPE_FREEWORD;
//		case jniSearchControl.UIC_SCM_TOUCHED_MARK_TYPE_VICINITY:
//			return jniSearchControl.UIC_SCM_SRCH_TYPE_NEARBY;
//		case jniSearchControl.UIC_SCM_TOUCHED_MARK_TYPE_MAP_SYMBOL:
//			return jniSearchControl.UIC_SCM_SRCH_TYPE_MAP_SYMBOL;
//		case jniSearchControl.UIC_SCM_TOUCHED_MARK_TYPE_USER_REPORT:
//			return jniSearchControl.UIC_SCM_TOUCHED_MARK_TYPE_USER_REPORT;
//		default:
//			return jniSearchControl.UIC_SCM_SRCH_TYPE_INVALID;
//		}
//	}
	
	public static int getSearchTypeFromTouchType(int marktype) {
		switch(marktype) {
		case UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_FREEWORD:
			return UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD;
		case UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_VICINITY:
			return UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY;
		case UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_MAP_SYMBOL:
			return UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL;
		default:
			return UISearchControlJNI.UIC_SCM_SRCH_TYPE_INVALID;
		}
		
	}
	
	public static int getTouchTypeFromSearchType(int marktype){
		switch(marktype) {
		case UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD:
			return UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_FREEWORD;
		case UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY:
			return UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_VICINITY;
		case UISearchControlJNI.UIC_SCM_SRCH_TYPE_MAP_SYMBOL:
			return UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_MAP_SYMBOL;
		case TYPE_LONGPRESS:
			return TYPE_LONGPRESS;
		default:
			return UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_INVALID;
		}
	}
	
	public static PointBean longPressMap(float x, float y) {
		int lonlat[] = UIMapControlJNI.ConvertDispPointToLonLat(x, y);
		
		UISearchControlJNI.Instance().ReqPinPoint(SearchTools.createLonLat(lonlat));
		
		PointBean point = new PointBean();
		point.lonlat = new long[]{lonlat[0], lonlat[1]};
		point.index = 0;
		point.type = TYPE_LONGPRESS;
		point.name = NaviViewManager.GetViewManager().getString(R.string.STR_COM_018);
		return point;
	}
	
	public static class PointBean implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7790578019869578609L;
		private String name;
		private String summary = "";
		private long[] lonlat;
		private int index;
		private int type;
		private int category;
		private String address;
		private String telNo;
		private String time;
		private String postID;
		public String getName() {
			return name;
		}
		public long[] getLonlat() {
			return lonlat;
		}
		public int getIndex() {
			return index;
		}
		public int getType() {
			return type;
		}
		
		public void setLonlat(long[] lonlat) {
			this.lonlat = lonlat;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public void setType(int type) {
			this.type = type;
		}
		public void setCategory(int category) {
			this.category = category;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public void setTelNo(String telNo){
			this.telNo = telNo;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public int getCategory() {
			return category;
		}
		
		public String getAddress() {
			return address;
		}
		
		public String getTelNo(){
			return telNo;
		}
		
		public String getTime() {
			return time;
		}
		
		@Override
		public String toString() {
			return TextUtils.isEmpty(name)? NaviViewManager.GetViewManager().getString(R.string.STR_COM_018) : name;
		}
		public void setSummary(String summary){
			this.summary = summary;
		}
		public String getSummary() {
			if(!TextUtils.isEmpty(summary)) {
				return summary;
			} else if(!TextUtils.isEmpty(address)){
				return address;
			} else if(!TextUtils.isEmpty(time)) {
				int index = time.indexOf(".");
				if(index>=0) {
					return SystemTools.getLocalTimeText(time).substring(0, index);
				} else {
					return SystemTools.getLocalTimeText(time);
				}
			} else {
				return "";
			}
		}
		public String getPostID() {
			return postID;
		}
		public void setPostID(String postID) {
			this.postID = postID;
		}
	}
	
	public static class TmcPointBean extends PointBean{
		
		private String startTime;
		private String endTime;
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		
	}

	public static SearchPointBean getLongPressBean(NSTriggerInfo cTriggerInfo) {
		if(cTriggerInfo.m_iTriggerID ==  NSTriggerID.SEARCH_RESPONSE_PINPOINT_INFO) {
			UISearchDetailInfoResultJNI detailInfo = UISearchControlJNI.Instance().GetDetailInfoResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT);
			return SearchPointBean.createSearchPointBean(detailInfo, 0);
		}
		
		return null;
	}

}
