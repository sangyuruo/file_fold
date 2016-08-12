package com.billionav.navi.naviscreen.route;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.app.ext.NaviUtil;
import com.billionav.navi.component.actionbar.ActionBarGuide;
import com.billionav.navi.component.bottombar.BottomBarRoutePreview;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.guidebar.base.GuideInfoController;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.component.mapcomponent.ButtonMapZoom;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer.onMapViewGestureListener;
import com.billionav.navi.component.mapcomponent.PopupWayPoint;
import com.billionav.navi.component.tab.RouteConditionTab;
import com.billionav.navi.component.tab.RouteConditionTab.onTabClickListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.POI_Mark_Control;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.service.NotificateModel;
import com.billionav.navi.service.serviceHelper;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.WayPointPreviewController;
import com.billionav.navi.uitools.WayPointPreviewController.WayPointInfo;
import com.billionav.navi.uitools.WayPointPreviewController.WayPointPreviewListener;
import com.billionav.ui.R;

public class ADT_Route_Profile extends ActivityBase implements WayPointPreviewListener, MapScreen{
	private final MapView mapView = MapView.getInstance();
//	private final jniSetupControl	setupCtl = new jniSetupControl();
	private final UIPathControlJNI	pathControl = new UIPathControlJNI();
	private final WayPointPreviewController previewCtl = WayPointPreviewController.Instance();
	
	private final static int MARK_SHOWN_STATE_OFF = 0;
	private final static int MARK_SHOWN_STATE_ON = 1;
	
	private static final int ALL_ROUTE_DISPLAY_AREA_TOP_MARGIN = 134;
	private static final int ALL_ROUTE_DISPLAY_AREA_BOTTOM_MARGIN = 110;
	
	private static final int ALL_ROUTE_DISPLAY_AREA_SIDE_MARGIN = 50;
	private CountDownTimer countDownTimer = null;
	
//	private int popupOffsetY;
	
	private BottomBarRoutePreview 	previewBottomBar;
	private ImageView				previewButton;
	private ImageView				nextViewButton;
	private RelativeLayout			displayroutelist;
	private RelativeLayout			startnavigation;
	private RelativeLayout          roadeditBtn;
	private ImageView			simulatenavigationBtn;
	private RelativeLayout  		avoidbtn;
//	private ImageView				goDemo;
//	private ImageView				deleteRoute;
	private RouteConditionTab		conditionTab;
	private ButtonMapZoom			scaleButton;
	
	private PopupWayPoint			popupWayPoint;
//	private PopupRoutePoint			popupRoutePoint;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		
		setContentView(R.layout.adt_route_profile, ActionBarGuide.class);
		
		
		RouteCalcController.instance().syncRouteInfoFromPath();
//		GuideInfoController.createInstance().syncAllGuideInfo();
		setTitleBackgroundBlack();
		initViews();
		
		UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_RouteResult);
		UIMapControlJNI.SetCarPositonMode(false);
    	mapView.resizeForAllMap(getDisplayHeight(), getDisplayWidth());
    	previewCtl.exitPreviewMode();
    	MapOverwriteLayer.getInstance().addExternalMapViewGesture(new onMapViewGestureListener() {
			
			@Override
			public void onScrollEnd() {
				resetCountDownTimer();
			}
			
			@Override
			public void onScroll() {
				resetCountDownTimer();
			}
			
			@Override
			public void onLonClick() {
				resetCountDownTimer();
			}
			
			@Override
			public void onClick() {
				resetCountDownTimer();
			}
			
			@Override
			public void on2FingerScale() {
				resetCountDownTimer();
			}

			@Override
			public void on2FingerParallelVerticalScroll() {
				// TODO Auto-generated method stub
				resetCountDownTimer();
			}

			@Override
			public void on2FingerParallelHorizontalScroll() {
				// TODO Auto-generated method stub
				resetCountDownTimer();
			}
		});
    	scaleButton.setZoomButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetCountDownTimer();
			}
		});
//    	previewCtl.notifyCurrentWayPointInfo();
//    	popupOffsetY = (int) (44 * getResources().getDisplayMetrics().density +.5f);
	}
	
	 protected int onConnectedScreenId() {
		  return SCRMapID.ADT_ID_RouteResult;
	 }
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { 
			if(previewCtl.isInPreviewMode()){
				mapView.resizeForAllMap(getDisplayHeight(), getDisplayWidth());
				previewCtl.exitPreviewMode();
				popupWayPoint.setVisibility(View.GONE);
				initBottomBar();
				return true;
			} else {
				if(!MenuControlIF.Instance().BackWinChange()){
					getBundleNavi().putBoolean("Navigation", true);
					MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
				}
				return true;
			}
		}else{
			return super.OnKeyDown(keyCode, event);
		}
	}
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		
		POI_Mark_Control.forRoutePreviewView();
		mapView.resizeForAllMap(getDisplayHeight(), getDisplayWidth());
		previewCtl.setWppListener(this);
		setGuideTitle();
		resizeLayout();
		countDownTimer = new CountDownTimer(10000, 10) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.i("icon", "millisUntilFinished "+millisUntilFinished);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Log.i("icon", "onFinish... ");
				getBundleNavi().putBoolean("Navigation", true);
				if(!MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class)){
					MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
				}
			}
		};
		countDownTimer.start();
	}
	
	private void resizeLayout() {
		previewBottomBar.modifyPrevAndNextPosition();
		
	}

	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		previewCtl.setWppListener(null);
		MapView.getInstance().initSizeScaleBar();
		countDownTimer.cancel();
		countDownTimer = null;
		System.gc();
	}
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		resizeLayout();
//		onWayPointPopupConfigChanged();
//		onRoutePopupConfigChanged();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		// TODO Auto-generated method stub
		
		previewCtl.notifyTriggerReceived(triggerInfo, this);
		
		if(triggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE){
			
			refreshPopup();
			updateScaleButton();
		}else if(triggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE){
			setGuideTitle();
		}
		if(triggerInfo.m_lParam1 == UIGuideControlJNI.NAVI_SRV_GUD_MSG_DIRECTION_LIST_REFRESH){
			initBottomBar();
			popupWayPoint.setVisibility(View.GONE);
	    	previewCtl.exitPreviewMode();
		}
		
		return super.OnTrigger(triggerInfo);
	}

	
	
	private void updateScaleButton() {
		scaleButton.updateButtonStatus();
		
	}

	private void initViews(){
		previewBottomBar = (BottomBarRoutePreview) findViewById(R.id.rp_bottombar);
		scaleButton = (ButtonMapZoom) findViewById(R.id.map_zoom_button);
		
		previewButton = previewBottomBar.getBtnLeft();
		nextViewButton = previewBottomBar.getBtnRight();
		displayroutelist = previewBottomBar.getListdisplayBtn();
		startnavigation = previewBottomBar.getStartnavigationBtn();
		roadeditBtn = previewBottomBar.getRoadeditBtn();
		simulatenavigationBtn = previewBottomBar.getSimulatenavigationBtn();
		avoidbtn = previewBottomBar.getavoidbtn();
		
		previewButton.setOnClickListener(clickListener);
		nextViewButton.setOnClickListener(clickListener);
		displayroutelist.setOnClickListener(clickListener);
		startnavigation.setOnClickListener(clickListener);
		roadeditBtn.setOnClickListener(clickListener);
		simulatenavigationBtn.setOnClickListener(clickListener);
		avoidbtn.setOnClickListener(clickListener);
		
//		popupRoutePoint = (PopupRoutePoint) findViewById(R.id.rp_routepointpopup);
//		popupRoutePoint.setOnClickListener(clickListener);
		popupWayPoint	=  (PopupWayPoint) findViewById(R.id.rp_waypointpopup);
		popupWayPoint.setOnClickListener(clickListener);
		
//		popupRoutePoint.setVisibility(View.GONE);
		popupWayPoint.setVisibility(View.GONE);
		
		initBottomBar();
		
		initConditionTab();
	}
	
	private OnClickListener clickListener = new OnClickListener(){
		public void onClick(View v) {
			resetCountDownTimer();
			if(v == previewButton){
				
				onPreViewBtnClick();
			}else if( v == nextViewButton){				
				onNextViewBtnClick();
			}else if(v == displayroutelist){
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_DetailedInfo.class);
				BundleNavi.getInstance().putInt("routeKind", conditionTab.getTabSelectIndex());
			}else if(v == startnavigation){
				getBundleNavi().putBoolean("Navigation", true);
				if(!MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class)){
					MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
				}
			}else if(v == popupWayPoint){
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_DetailedInfo.class);
			}
			else if(v == roadeditBtn){
				RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_SHOW);
				if(!BackSearchWinChange(ADT_Route_Main.class)){
					MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
				}
			}
			else if(v == simulatenavigationBtn){
				gotoDemoMode();
			}
			else if(v == avoidbtn){
				getDateTimePickerDialog();
				if (countDownTimer != null) {
					countDownTimer.cancel();
				}
			}
		}
	};
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	
	private AlertDialog timeDialog;
	public AlertDialog getDateTimePickerDialog(){
		LinearLayout dateTimeLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.date_time_picker, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.DatePicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.TimePicker);
		timePicker.setIs24HourView(true);
		Calendar time = Calendar.getInstance();
		Date date = new Date();
		int scheduleSettingValue = SharedPreferenceData.getInt(
				SharedPreferenceData.SCHEDULE_START_TIME,
				HybridUSTools.START_TIME_TEN_MINUE);
    	int leadTime = HybridUSTools.TIME_TEN_MINUE;
    	if(scheduleSettingValue == HybridUSTools.START_TIME_HALF_HOUR){
    		leadTime = HybridUSTools.TIME_HALF_HOUR;
    	} else if(scheduleSettingValue == HybridUSTools.START_TIME_AN_HOUR){
    		leadTime = HybridUSTools.TIME_AN_HOUR;
    	}
		long displayTime = time.getTime().getTime() + pathControl.GetRouteTime(0 , 0) * 1000 + leadTime * 1000;
		date.setTime(displayTime);
		time.setTime(date);
		datePicker.init(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), null);
		
		timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
		
		timeDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.STR_COM_059)).setView(dateTimeLayout)
				.setPositiveButton(getString(R.string.STR_COM_058), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DateFormat df = HybridUSTools.df;
						datePicker.clearFocus();
						timePicker.clearFocus();
						Calendar c = Calendar.getInstance();
						c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
								timePicker.getCurrentHour(), timePicker.getCurrentMinute());
						
						long routeTime = pathControl.GetRouteTime(0 , 0);
						long startTime = RouteTool.isValidTime(routeTime , df.format(c.getTime()));
						if(startTime != -1){
							int num = RouteCalcController.instance().getRoutePointNum();
							String pointName = RouteCalcController.instance().getPointNameAtIndex(num - 1);
							long[] lonlat = RouteCalcController.instance().getPointLonLatAtIndex(num - 1);
							NotificateModel model = new NotificateModel();
							model.lon = (int)lonlat[0];
							model.lat = (int)lonlat[1];
							model.pointName = pointName;
							model.NotificateTime = startTime;
							model.EtaTime = routeTime * 1000;
							model.arrivalTime = c.getTimeInMillis();
							model.tag = Long.toString(System.currentTimeMillis());
							serviceHelper.addNotificationToService(model);
							CustomToast.showToast(ADT_Route_Profile.this, R.string.STR_COM_057, 1000);
						}else{
							showWarnIsInvalidTimeDialog();
						}
						countDownTimer.start();
					}
				}).setNegativeButton(getString(R.string.MSG_00_00_00_12), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						countDownTimer.start();
					}
				}).show();
		
		return timeDialog;
	}
	
	private void showWarnIsInvalidTimeDialog() {
		CustomDialog dialog = new CustomDialog(this);
		dialog.setTitle(this.getString(R.string.STR_MM_10_04_02_39));
		dialog.setMessage(this.getString(R.string.STR_COM_042));
		dialog.setPositiveButton(R.string.MSG_00_00_00_11, null);
		dialog.show();
	}
	
	private void initBottomBar(){
		
		int waypointNum = previewCtl.initInfoList();
		updateWayPointSelectButton(waypointNum > 0, false);
	}
	
	private void setGuideTitle(){
		GuideInfoController.createInstance().syncAllGuideInfo();
		ActionBarGuide guideTitle = (ActionBarGuide) getActionBar2();
		String subString = RouteTool.substitutionDistance(GuideInfoDataManager.Instance().getRemainingDistanceString()) + " - " 
				+  RouteTool.substitutionTime(GuideInfoDataManager.Instance().getRemainingTimeString());
		String destName = GuideInfoDataManager.Instance().getDestinationName();
		
		
		guideTitle.setGuideTitleText(getString(R.string.STR_MM_03_02_01_06) + "  " + destName);
		guideTitle.setGuideSubTitleText(subString);
		
		Log.d("test","subString="+subString);
		
	}

	
	private void initConditionTab(){
		conditionTab = (RouteConditionTab) findViewById(R.id.rp_route_condition_tab);
//		int value = setupCtl.GetInitialStatus(jniSetupControl.STUPDM_ROUTE_CONDITION);
//		conditionTab.setTabSelectIndex(value);
		pathControl.setRouteCondition(0);
		conditionTab.setOnTabClickedListener(new onTabClickListener() {
			@Override
			public void onTabClicked(int index) {
				// TODO Auto-generated method stub
//				setupCtl.SetInitialStatus(jniSetupControl.STUPDM_ROUTE_CONDITION, index);
				resetCountDownTimer();
				pathControl.setRouteCondition(index);
				previewCtl.exitPreviewMode();
				popupWayPoint.setVisibility(View.GONE);
				RouteCalcController.instance().rerouteCalculateAfterConditionChanged();
			}
		});
	}
	
	
	private void onPreViewBtnClick(){
		previewCtl.requestPreviousWayPointInfo();
	}
	
	private void onNextViewBtnClick(){
		previewCtl.requestNextWayPointInfo();
	}
	
	
	private void resetForAllMapDisplay(){
		mapView.resizeForAllMap(getDisplayHeight(), getDisplayWidth());
	}
	
	public int getDisplayHeight(){
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		float density = getResources().getDisplayMetrics().density;
		
		int displayHeight = screenHeight - (int)((ALL_ROUTE_DISPLAY_AREA_BOTTOM_MARGIN + ALL_ROUTE_DISPLAY_AREA_TOP_MARGIN)*density +0.5f);
		
		return displayHeight;
	}
	
	public int getDisplayWidth(){
		float density = getResources().getDisplayMetrics().density;
		return getResources().getDisplayMetrics().widthPixels - (int)(ALL_ROUTE_DISPLAY_AREA_SIDE_MARGIN*density +0.5f);
	}
	
	
	private void updateWayPointInfoPlate(WayPointInfo info){
//		MapTools.setCenterLonlat((int)info.point_LonLat_Array[0], (int)info.point_LonLat_Array[1]);
		if(UIMapControlJNI.GetHeight() != 772) {
			UIMapControlJNI.SetCenterInfo(UIMapControlJNI.GetCenterLonLat()[0], UIMapControlJNI.GetCenterLonLat()[1], 772);
		}
//		UIMapControlJNI.AutoHeightMove(info.point_LonLat_Array[0], info.point_LonLat_Array[1]);
		UIMapControlJNI.SetCenterInfo((int)info.point_LonLat_Array[0], (int)info.point_LonLat_Array[1], 772);
//		mapView.updateHeight(772);
		updateWayPointPopupByInfo(info);
	}
	
	private void updateWayPointPopupByInfo(WayPointInfo info){
		String turningInfo;
		
		if(info.point_Kind == -1){
			if(TextUtils.isEmpty(info.point_StreetName) || info.point_StreetName.equals(getString(R.string.STR_MM_02_02_04_15))){
				turningInfo = getString(R.string.STR_MM_03_01_01_02); 
			}else{
				turningInfo = getString(R.string.STR_MM_03_01_01_02) + "-" + info.point_StreetName;
			}
		}else if(info.point_Kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_GOAL){
			turningInfo = getString(R.string.STR_MM_03_01_01_03) + "-" + info.point_StreetName;
		}else{
			turningInfo = 
				previewCtl.getDirectionString(this, info.point_Direction)
				+ getString(R.string.STR_MM_01_01_04_16)
				+ info.point_StreetName
				;
		}
		popupWayPoint.setInfoText(turningInfo);
	}
	
	
	private void updateWayPointSelectButton(boolean hasNext, boolean hasPrevious){
		if(hasNext){
		}else{
		}
		if(hasPrevious){
		}else{
		}
		previewButton.setEnabled(hasPrevious);
		nextViewButton.setEnabled(hasNext);
	}
	
	
	private void gotoDemoMode(){
		NaviUtil.startDemoDriving();
//		pathControl.StartDemoDriving(1);
		previewCtl.exitPreviewMode();
		getBundleNavi().putBoolean("Navigation", true);
		if(!MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class)){
			MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
		}
	}
	
	
	private void updateRoutePointPlate(int index){
		
	}
	
	private void refreshPopup(){
		
		long[] lonlat = previewCtl.getCurrentPointLonLat();
		if(lonlat == null){
			return;
		}
		
		float[] point = UIMapControlJNI.ConvertLonLatToDispPoint((int)lonlat[0], (int)lonlat[1]);
		
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		float scale = getResources().getDisplayMetrics().density;
		int width = (int) (300*scale);
		
		RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) popupWayPoint.getLayoutParams();
		
		l.topMargin = (int) (point[1] - 98 * scale);
		l.bottomMargin = l.topMargin - getResources().getDisplayMetrics().heightPixels - (int)(72*scale);
		l.leftMargin = (int) (point[0] - width/2);
		l.width = width;
		l.rightMargin = (int) (screenWidth - point[0] - width);
		popupWayPoint.setLayoutParams(l);
		
		popupWayPoint.setVisibility(View.VISIBLE);
	}
	
	

	@Override
	public void onWayPointInfoSelected(WayPointInfo currentInfo, int index,
			boolean hasNext, boolean hasPrevious) {
		// TODO Auto-generated method stub
		updateWayPointInfoPlate(currentInfo);
		updateWayPointSelectButton(hasNext, hasPrevious);
	}

	@Override
	public void onWayPointInfoCleared() {
		// TODO Auto-generated method stub
		resetForAllMapDisplay();
	}


	@Override
	public void onRoutePointSelected(int index) {
		// TODO Auto-generated method stub
		updateRoutePointPlate(index);
	}

	private void resetCountDownTimer() {
		if(countDownTimer != null){
			countDownTimer.cancel();
			countDownTimer.start();
		}
	}


	
	
}

