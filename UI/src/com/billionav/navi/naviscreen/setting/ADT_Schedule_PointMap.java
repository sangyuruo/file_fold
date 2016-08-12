package com.billionav.navi.naviscreen.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.component.mapcomponent.PopupPOI;
import com.billionav.navi.component.mapcomponent.SingleBottomButton;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.map.POI_Mark_Control;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.navi.naviscreen.schedule.ScheduleModel;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.uitools.GestureDetector;
import com.billionav.navi.uitools.GestureDetector.OnGestureListener;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.ui.R;

public class ADT_Schedule_PointMap extends ActivityBase implements OnScreenBackListener, MapScreen{
	
	private SingleBottomButton	okButton;
	private RelativeLayout		searchBar;
//	private int					purpose;
	private PopupPOI popup;	
	
	private OnGestureListener gestureListener = new GestureDetector.SimpleGestureListener(){
		@Override
		public boolean onScroll(boolean isFrist, MotionEvent down,
				MotionEvent move, float distanceX, float distanceY) {
			if(isFrist) {
//				dismissPinInfo();
//				pinButton.setVisibility(View.GONE);
			}
			return true;
		}
	};
		
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.adt_route_pointmap);
//		purpose = RouteCalcController.instance().getRoutePointFindPurpose();
		
		findViews();
		setListeners();
		okButton.setButtonShowStyle(SingleBottomButton.BUTTON_STYLE_LIST);
		UIMapControlJNI.SetScreenID(SCRMapID.SCR_ID_ScrollMap);
		UIMapControlJNI.SetCarPositonMode(false);
	}
	
	 protected int onConnectedScreenId() {
		  return SCRMapID.ADT_ID_AddMapPoint;
	 }
	
	@Override
	protected void OnResume() {
		super.OnResume();
		
		POI_Mark_Control.forAddPointView();
		
		MapOverwriteLayer.getInstance().showMapElementRoutePoint();
		MapView.getInstance().setGestureListener(gestureListener);
		MapOverwriteLayer.getInstance().setTapPopupEnable(true);
	}
	
	@Override
	protected void OnPause() {
		super.OnPause();
		MapView.getInstance().setGestureListener(null);
		MapOverwriteLayer.getInstance().hideMapElementRoutePoint();
		MapOverwriteLayer.getInstance().setTapPopupEnable(false);
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}
	
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		if(cTriggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_PIN_NAME_DONE){
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	
	private void findViews() {
		okButton = (SingleBottomButton) findViewById(R.id.rpm_ok_button);
		searchBar = (RelativeLayout) findViewById(R.id.rpm_srch_bar);
	}
	
	private void setListeners(){
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addPointToSchedule();
			}
		});
		searchBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
			}
		});
	}
	
	private void addPointToSchedule(){
		if (HybridUSTools.getInstance().isTheSamePoint(
				UIMapControlJNI.GetCenterLonLat()[0],
				UIMapControlJNI.GetCenterLonLat()[1])) {
			CustomDialog dialog = new CustomDialog(this);
			dialog.setTitle(getString(R.string.STR_COM_047));
			dialog.setMessage(getString(R.string.STR_COM_048));
			dialog.setPositiveButton(R.string.MSG_00_00_00_11, null);
			dialog.show();
			return;
		}
		
		ScheduleModel model = new ScheduleModel();
		model.lon = UIMapControlJNI.GetCenterLonLat()[0];
		model.lat = UIMapControlJNI.GetCenterLonLat()[1];
		model.pointName = this.getString(R.string.STR_MM_02_02_04_15);
		ScheduleDataList.getInstance().addList(model);
		
		Log.i("LonLat","ADT_Schedule_PointMap:addPointToSchedule");
		UIMapControlJNI.reqPinPoint((int)model.lon, (int)model.lat);
		
		MenuControlIF.Instance().BackWinChange();
	}
	
	@Override
	public void onBack() {
		popup = MapOverwriteLayer.getInstance().getPopup();
		if(popup.isShowing()){
			popup.dismissPopup();
		}
		MenuControlIF.Instance().BackWinChange();
	}
	
}
