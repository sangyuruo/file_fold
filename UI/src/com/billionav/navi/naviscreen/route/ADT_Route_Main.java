package com.billionav.navi.naviscreen.route;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.actionbar.BaseActionBar;
import com.billionav.navi.component.tab.RouteEditBroadDrive;
import com.billionav.navi.component.tab.RouteEditBroadDrive.OnBroadActionListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.menucontrol.NSWinchangeType;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.POI_Mark_Control;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;

public class ADT_Route_Main extends ActivityBase implements MapScreen, RouteCalcController.RouteDetailInfoUpdateListener{
	
	private BaseActionBar			actionBar;
	private RouteEditBroadDrive 	driveBroad;
//	private BottomBarRoute			startCalculate;
	
	private static final int		LIST_ITEM_HEIGHT_DIP = 48;
	private static final int 		BOTTOM_HEIGHT_DIP = 50;
	
	private RouteCalcController		calculateControl = RouteCalcController.instance();
	
	private boolean					willGoNavigation = false;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		
		setContentView(R.layout.adt_route_main);
		initActionBar();
		
		initTabBroad();
		initRoutePoint();
		m_bMapScreen = false;
		UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_RouteEdit);
		RouteCalcController.instance().setRouteDetailUpdateListener(this);
	}
	
	 protected int onConnectedScreenId() {
		  return SCRMapID.ADT_ID_RouteEdit;
	 }
	
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		
		POI_Mark_Control.forRouteEditView();
		
		driveBroad.LogMessage();

		if(MenuControlIF.Instance().GetWinchangeType() == NSWinchangeType.MN_WINCHANGE_TYPE_BACK
				||MenuControlIF.Instance().GetWinchangeType() == NSWinchangeType.MN_WINCHANGE_TYPE_BACK_SEARCH){
			
			int purpose = calculateControl.getRoutePointFindPurpose();
			if(purpose != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
				calculateControl.requestDetailLonLatName(calculateControl.selectedIndexInEditMode);
				calculateControl.updateRoutePointByPurposeAtIndex(RouteCalcController.selectedIndexInEditMode);
				if(purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA){
//					String tmp = new String(calculateControl.getPointNameAtIndex(calculateControl.getRoutePointNum() - 1));
					String tmp = NSViewManager.GetViewManager().getString(R.string.STR_MM_02_02_04_15);
					driveBroad.addWayPoint(tmp);
				}
				updateDisplayNameAtIndex(RouteCalcController.selectedIndexInEditMode);
				calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
//				willGoNavigation = true;
			}else{
			}
		}
		updateExchangeAndCalculatButton();

	}
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
	}
	
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		RouteCalcController.instance().removeRouteDetailUpdateListener();
		actionBar = null;
		driveBroad.setOnBroadActionListener(null);
		driveBroad.onDestory();
		driveBroad = null;
		PointTools.getInstance().removePointListener();
		calculateControl.endEdit();
		calculateControl = null;
		willGoNavigation = false;
		super.OnDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
//		adjustLayout();
		driveBroad.adjustListViewHight();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		// TODO Auto-generated method stub
		return super.OnTrigger(cTriggerInfo);
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			onBackScreen();
			if(willGoNavigation || UIC_SreachCommon.isUpdateBySearch){
				UIC_SreachCommon.isUpdateBySearch = false;
				getBundleNavi().putBoolean("Navigation", true);
				MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
				return true;
			}
		}
		return super.OnKeyDown(keyCode, event);
	}
	
	private void onBackScreen(){
//		int purpose = calculateControl.getRoutePointFindPurpose();
//		calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
		
		calculateControl.deletePointAtIndex(RouteCalcController.selectedIndexInEditMode);
		calculateControl.endEdit();
//		switch (purpose) {
//		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
//			calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA);
//			break;
//		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
//			calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST);
//			break;
//		default:
//			break;
//		}
	}
	
	private void adjustLayout(){
		float scale = getResources().getDisplayMetrics().density;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		int tabHeight;
		int itemNum = calculateControl.getRoutePointNum();
		tabHeight = itemNum * (int)(LIST_ITEM_HEIGHT_DIP * scale + .5f) + (int)(BOTTOM_HEIGHT_DIP * scale + .5f);
		if(tabHeight > screenHeight){
			tabHeight = screenHeight;
		}
		driveBroad.getLayoutParams().height = tabHeight;
		driveBroad.requestLayout();
		findViewById(R.id.adt_route_main_background).requestLayout();
	}
	
	private void initRoutePoint() {
		int purpose = calculateControl.getRoutePointFindPurpose();
		if (purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE) {
			calculateControl.initRouteInfoForNewRoute();
		} else {
			calculateControl.initRouteInfoByPurpose(purpose);
			
//			if(!UIC_RouteCommon.Instance().isRouteExistAndGuideOn()){
//				if(purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA
//						|| purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA
//						|| purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_SHOW){
//					calculateControl.pointDefaultDestToPOI();
//					RouteCalcController.selectedIndexInEditMode = calculateControl.getRoutePointNum()-1;
//					calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST);
//				}
//				else{
//					calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START);
//				}
//			}
			
			
			int listNum = calculateControl.getRoutePointNum();
			int displayNum = driveBroad.getListNum();
			int diff = listNum - displayNum;
			for (int i = 0; i < diff; i++) {
				driveBroad.addWayPoint("");
			}
			for (int i = 0; i < listNum; i++) {
				updateDisplayNameAtIndex(i);
			}
		}
	}
	
	private void initActionBar() {
		actionBar = getActionBar2();
		actionBar.setText(getString(R.string.STR_MM_01_02_01_13));

	}
	
	private void initTabBroad(){
		driveBroad = (RouteEditBroadDrive) findViewById(R.id.rm_tabbroad);
		
		driveBroad.setOnBroadActionListener(new OnBroadActionListener() {
			
			@Override
			public void onStartCalculating() {
				// TODO Auto-generated method stub
				startCalculateRoute();
			}
			
			@Override
			public void onListItemClick(int listIndex) {
				// TODO Auto-generated method stub
//				findPointOnMapForIndex(listIndex);
			}

			@Override
			public void onWaypointAddClick() {
				// TODO Auto-generated method stub
				if(calculateControl.getRoutePointNum() == 0){
					calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START);
					RouteCalcController.selectedIndexInEditMode = 0;
				} else if(calculateControl.getRoutePointNum() == 1){
					calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST);
					RouteCalcController.selectedIndexInEditMode = driveBroad.getListNum() - 1;
				}else{
					calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA);
					RouteCalcController.selectedIndexInEditMode = driveBroad.getListNum() - 1;
				}
				MenuControlIF.Instance().ForwardWinChange(ADT_Route_PointMap.class);
			}

			@Override
			public void onListItemEditClicked(int index) {
				setPurposeAtIndex(index);
				RouteCalcController.selectedIndexInEditMode = index;
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
				
			}

			@Override
			public void onListItemDeleteClicked(int index) {
				// TODO Auto-generated method stub
				if(index == 0 || index == driveBroad.getListNum() - 1){
					//do nothing
				}else{
					deleteAtIndex(index);
				}
			}

			@Override
			public void onItemSwitch(int from, int to) {
				// TODO Auto-generated method stub
				calculateControl.switchDataWithIndex(from, to);
			}

			@Override
			public void onStartEndChangedClicked() {
				calculateControl.exchangeDestAndStart();
				
			}

			@Override
			public void onListItemSearchClicked(int index) {
				findPointOnMapForIndex(index);
				
			}
		});
	}
	
	
	private void updateExchangeAndCalculatButton(){
		
		int displayNum = driveBroad.getListNum();
		
		driveBroad.setStartCalculateEnabled(calculateControl.isCalcRouteEnable());
		driveBroad.setAddEnabled(displayNum < RouteCalcController.MAX_ROUTE_POINT_NUM);
		adjustLayout();
	}
	
	private void startCalculateRoute(){
		calculateControl.startCalculatingAfterEdit(this);
	}
	
	
	private void setPurposeAtIndex(int index){
		int realPontNum = calculateControl.getRoutePointNum();
		if(index == 0){
			if(realPontNum > 0){
				calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START);
			}else{
				calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START);
			}
		}else if(index == driveBroad.getListNum() - 1){
			if(realPontNum < 2){
				calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST);
			}else{
				calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST);
			}
		}else{
			calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA);
		}
	}
	
//	private void setCurrentPositionToIndex(int index){
//		calculateControl.pointCurrentPositionToPOI();
//		setPurposeAtIndex(index);
//		calculateControl.updateRoutePointByPurposeAtIndex(index);
//		calculateControl.setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
//		updateDisplayNameAtIndex(index);
//		updateExchangeAndCalculatButton();
//	}
	
	private void findPointOnMapForIndex(int index){
		setPurposeAtIndex(index);
		RouteCalcController.selectedIndexInEditMode = index;
		MenuControlIF.Instance().ForwardWinChange(ADT_Route_PointMap.class);
//		MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
	}
//	private void findPointInFavorForIndex(int index){
//		
//		
//	}
//	
//	private void findPointInContactForIndex(int index){
//		
//	}
	
	private void deleteAtIndex(int index){
		int result = calculateControl.deletePointAtIndex(index);
		
		switch (result) {
		case RouteCalcController.DELETE_RESULT_NORMAL:
		case RouteCalcController.DELETE_RESULT_DEST_TO_START:
		case RouteCalcController.DELETE_RESULT_WAYPOINT_TO_DEST:
		case RouteCalcController.DELETE_RESULT_WAYPOINT_TO_START:
			int listNum = calculateControl.getRoutePointNum();
			if(listNum < 2){
				if(listNum == 0){
					driveBroad.setDisplayNameAtIndex(0,"");
					driveBroad.setDisplayNameAtIndex(1,"");
				}else{
					updateDisplayNameAtIndex(0);
					driveBroad.setDisplayNameAtIndex(1,"");
				}
			}else{
				driveBroad.removeItemAtIndex(index);
				for(int i = 0; i < listNum; i++){
					updateDisplayNameAtIndex(i);
				}
			}
			break;
		case RouteCalcController.DELETE_RESULT_FAILED:
		default:
			break;
		}
		updateExchangeAndCalculatButton();
	}
	
	public void updateDisplayNameAtIndex(int index){
		String tmp = calculateControl.getPointNameAtIndex(index);
		driveBroad.setDisplayNameAtIndex(index, tmp);
	}

	@Override
	public void onRouteDetailInfoUpdate() {
		updateDisplayNameAtIndex(RouteCalcController.selectedIndexInEditMode);
	}
	
	
}
