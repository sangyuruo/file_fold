package com.billionav.navi.uitools;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.ui.R;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.route.ADT_Route_Profile;

public class WayPointPreviewController {
	
	
	public interface WayPointPreviewListener{
		
		public void onWayPointInfoSelected(WayPointInfo currentInfo,int index, boolean hasNext, boolean hasPrevious);
		public void onWayPointInfoCleared();
		public void onRoutePointSelected(int index);
	}
	
	public class WayPointInfo{
		public int point_Distance;
		public int point_Direction;
		public int point_PointID;
		public String point_StreetName;
		public long[] point_LonLat_Array;
		public String point_DistrictName;
		public int point_Kind;

		@Override
		public String toString() {
			return "lonlat=" + Arrays.toString(point_LonLat_Array)
					+ ",direction=" + String.valueOf(point_Direction)
					+ ", distance=" + String.valueOf(point_Distance);
		}
    }
	
	
	private WayPointPreviewListener wppListener;
	
	public WayPointPreviewListener getWppListener() {
		return wppListener;
	}

	public void setWppListener(WayPointPreviewListener wppListener) {
		this.wppListener = wppListener;
		if(isInPreviewMode){
			notifyCurrentWayPointInfo();
		}
		if(isInRoutePointSelectMode){
			notifySelectedRoutePoint();
		}
	}
	
	
	
	private ArrayList<WayPointInfo> wayPointList = new  ArrayList<WayPointInfo>();
	private UIGuideControlJNI guideControl = UIGuideControlJNI.getInstance();
	private static WayPointPreviewController instance = new WayPointPreviewController();
	
	private int selectedWayPointIndex = 0;
	
	private int selectRoutePointIndex = -1;
	public void setRoutePointSelectIndex(int index){
		selectRoutePointIndex = index;
	}
	public int getRoutePointSelectIndex(){
		return selectRoutePointIndex;
	}
	
	public static boolean isInPreviewMode = false;
	public static boolean isInRoutePointSelectMode = false;
	public static WayPointPreviewController Instance(){
		return instance;
	}
	
	public void notifyTriggerReceived(NSTriggerInfo info, ADT_Route_Profile activity){
		if (info.m_iTriggerID != NSTriggerID.UIC_MN_TRG_DIRECTIONLIST_CHANGE) {
			return;
		}
		
		if (info.m_lParam1 == UIGuideControlJNI.NAVI_SRV_GUD_MSG_DIRECTION_LIST_ELIMINATE) {
			//info cleared
			if(wppListener != null){
				isInPreviewMode = false;
				wppListener.onWayPointInfoCleared();
			}
		} else if (info.m_lParam1 == UIGuideControlJNI.NAVI_SRV_GUD_MSG_DIRECTION_LIST_REFRESH) {
			//info updated
			if (UIGuideControlJNI.NAVI_SRV_GUD_MSG_DIRECTION_LIST_GOTOPREVIEW == info.m_lParam3) {
				updatePointInfo();
				selectedWayPointIndex = 0;
				MapView.getInstance().resizeForAllMap(activity.getDisplayHeight(), activity.getDisplayWidth());
		    	exitPreviewMode();
//				notifyCurrentWayPointInfo();
			}
		}
	}
	
	public int initInfoList(){
		int totalNum = guideControl.GetDirectionListInfo_PointNum();
//		selectedWayPointIndex = 0;
		updatePointInfo();
		return totalNum;
	}
	
	public void exitPreviewMode(){
		selectedWayPointIndex = 0;
		selectRoutePointIndex = -1;
		isInPreviewMode = false;
		isInRoutePointSelectMode = false;
	}
	
	public boolean isInPreviewMode(){
		return isInPreviewMode;
	}
	public void notifyCurrentWayPointInfo(){
		if(selectedWayPointIndex < 0 || selectedWayPointIndex >= wayPointList.size()){
			return;
		}
		isInPreviewMode = true;
		isInRoutePointSelectMode = false;
		if(wppListener != null){
			boolean hasNext = (wayPointList.size() - 1) > selectedWayPointIndex;
			boolean hasPrevious = selectedWayPointIndex > 0;
			wppListener.onWayPointInfoSelected(wayPointList.get(selectedWayPointIndex), selectedWayPointIndex, hasNext, hasPrevious);
		} 
	}
	
	public void notifySelectedRoutePoint(){
		if(wppListener != null){
			wppListener.onRoutePointSelected(selectRoutePointIndex);
		}
	}
	
	public void setInRoutePointPreviewMode(){
		isInRoutePointSelectMode = true;
		isInPreviewMode = false;
	}
	
	public void requestNextWayPointInfo(){
		updatePointInfo();
		if(selectedWayPointIndex >= wayPointList.size()
				|| selectedWayPointIndex < 0)
		{
			return;
		}
		
		if(isInPreviewMode){
			selectedWayPointIndex += 1;
		}
		
		notifyCurrentWayPointInfo();
	}
	
	public void requestPreviousWayPointInfo(){
		if(selectedWayPointIndex > wayPointList.size()
				|| selectedWayPointIndex <= 0)
		{
			return;
		}
		
		selectedWayPointIndex -= 1;
		
		notifyCurrentWayPointInfo();
	}
	
	public void requestWayPointInfoAtIndex(int index){
		if(index > wayPointList.size()
				|| index < 0)
		{
			return;
		}
		selectedWayPointIndex = index;
		notifyCurrentWayPointInfo();
	}
	
	public void requestWayPointInfoAtDestination(){
		
	}
	
	public ArrayList<WayPointInfo> getWayPointList(){
		return wayPointList;
	}
	
	public String getPointKind(int point_kind) {
		Activity a =  MenuControlIF.Instance().GetCurrentActivity();
		if(a == null)
		{
			return "";
		}
		
		if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_GOAL) {
			return a.getString(R.string.STR_MM_01_01_04_13);
		} else if (point_kind > UIGuideControlJNI.DPGUDEF_CMN_FLAG_GOAL
				&& point_kind < UIGuideControlJNI.DPGUDEF_CMN_FLAG_GUIDEPOINT) {
			return a.getString(R.string.STR_MM_01_01_04_28);
		} else if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_GUIDEPOINT) {
			return a.getString(R.string.STR_MM_01_01_04_26);
		} else if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_ROUDABOUT) {
			return a.getString(R.string.STR_MM_01_01_04_27);
		} else if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_TOLL) {
			return a.getString(R.string.STR_MM_01_01_04_24);
		} else if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_FERRY) {
			return a.getString(R.string.STR_MM_01_01_04_25);
		} else if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_MAX) {
			return null;
		} else if (point_kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_NONE) {
			return null;
		}
		return null;
	}
	
	
	public String getDirectionString(Context context, int direction) {
		String temp_str = null;
		if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_STRAIGHT) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_03);

		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_R) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_04);

		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_L) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_05);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FR) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_06);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FL) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_07);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RR) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_08);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RL) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_09);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_10);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_R) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_11);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_L) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_10);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_R) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_06);
		} else if (direction == UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_L) {
			temp_str = context.getString(R.string.STR_MM_01_01_04_07);
		} else {//
			temp_str = context.getString(R.string.STR_MM_01_01_04_03);
		}
		return temp_str;
	}
	
	public long[] getCurrentPointLonLat(){
		if(!isInPreviewMode)
		{
			return null;
		}
		if(selectedWayPointIndex < 0 ){
			selectedWayPointIndex = 0;
		}else if(selectedWayPointIndex >= wayPointList.size()){
			selectedWayPointIndex = wayPointList.size() - 1;
		}
		return wayPointList.get(selectedWayPointIndex).point_LonLat_Array;	
	}
	public int getSelectedWayPointIndex(){
		return selectedWayPointIndex;
	}
	private void updatePointInfo() {
		wayPointList.clear();
		
		//add start point
		wayPointList.add(getStartPoint());
		
		int total_num = guideControl.GetDirectionListInfo_PointNum();
		for (int i = 0; i < total_num; i++) {
			wayPointList.add(getPointInfo(i));
		}
	}
	private WayPointInfo getStartPoint(){
		UIPathControlJNI pathControl = new UIPathControlJNI();
		WayPointInfo pointInfo = new WayPointInfo();
		pointInfo.point_Distance = 0;
		pointInfo.point_Direction = 1;
		pointInfo.point_StreetName = pathControl.GetPointName(UIPathControlJNI.UIC_PT_FIND_OBJ_UI, 0, true);
		pointInfo.point_PointID = 0;
		pointInfo.point_LonLat_Array = pathControl.GetCenterLonLat(UIPathControlJNI.UIC_PT_FIND_OBJ_UI, 0, true);
		pointInfo.point_Kind = -1;
		pointInfo.point_DistrictName = null;
		
		return pointInfo;
	}

	private WayPointInfo getPointInfo(int i) {
		WayPointInfo pointInfo = new WayPointInfo();
		pointInfo.point_Distance = guideControl.GetDirectionListInfo_PointDistance(i);
		pointInfo.point_Direction = guideControl.GetDirectionListInfo_PointDirection(i);
		pointInfo.point_StreetName = guideControl.GetDirectionListInfo_StreetName(i);
		pointInfo.point_PointID = guideControl.GetDirectionListInfo_PointID(i);
		pointInfo.point_LonLat_Array = guideControl.GetDirectionListInfo_LonLat(i);
		pointInfo.point_Kind = guideControl.GetDirectionListInfo_PointKind(i);
		pointInfo.point_DistrictName = null;
		return pointInfo;
	}



}
