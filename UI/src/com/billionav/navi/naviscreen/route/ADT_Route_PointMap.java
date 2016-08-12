package com.billionav.navi.naviscreen.route;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.billionav.jni.UIMapControlJNI;
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
import com.billionav.navi.naviscreen.srch.ADT_POI_List;
import com.billionav.navi.naviscreen.srch.ADT_Srch_List;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.GestureDetector;
import com.billionav.navi.uitools.GestureDetector.OnGestureListener;
import com.billionav.navi.uitools.MapTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;

public class ADT_Route_PointMap extends ActivityBase implements OnScreenBackListener, MapScreen{
	
	private SingleBottomButton	okButton;
	private RelativeLayout		searchBar;
	private int					purpose;
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
		purpose = RouteCalcController.instance().getRoutePointFindPurpose();
		
		findViews();
		setListeners();
		
		okButton.setButtonShowStyle(getImageByPurpose(purpose));
		
		UIMapControlJNI.SetScreenID(SCRMapID.SCR_ID_ScrollMap);
		UIMapControlJNI.SetCarPositonMode(false);
    	if(purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST
    			|| purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA
    			||purpose == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START)
    	{
    		long lonlat[] = RouteCalcController.instance().getPointLonLatAtIndex(RouteCalcController.selectedIndexInEditMode);
    		if(lonlat[0]==0 && lonlat[1] == 0){
    			int []lonlat2 = UIMapControlJNI.GetCenterLonLat();
    			lonlat[0] = (long)lonlat2[0];
    			lonlat[1] = (long)lonlat2[1];
    		}
    		MapTools.setCenterLonlat((int)lonlat[0], (int)lonlat[1]);
    	}
	}
	
	 protected int onConnectedScreenId() {
		  return SCRMapID.ADT_ID_AddMapPoint;
	 }
	
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		
		POI_Mark_Control.forAddPointView();
		
		MapOverwriteLayer.getInstance().showMapElementRoutePoint();
		MapView.getInstance().setGestureListener(gestureListener);
		MapOverwriteLayer.getInstance().setTapPopupEnable(true);
	}
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		MapView.getInstance().setGestureListener(null);
		MapOverwriteLayer.getInstance().hideMapElementRoutePoint();
		MapOverwriteLayer.getInstance().setTapPopupEnable(false);
	}
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
	}
	
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		// TODO Auto-generated method stub
		if(cTriggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_PIN_NAME_DONE){
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	
	private void findViews() {
		// TODO Auto-generated method stub
		okButton = (SingleBottomButton) findViewById(R.id.rpm_ok_button);
		searchBar = (RelativeLayout) findViewById(R.id.rpm_srch_bar);
	}
	
	private void setListeners(){
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addPointToRoute();
			}
		});
		searchBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(UIC_SreachCommon.getCurListActivityclass() == ADT_Srch_List.class
						|| UIC_SreachCommon.getCurListActivityclass() == ADT_POI_List.class){
							UIC_SreachCommon.isUpdateBySearch = true;
						}
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
			
			}
		});
	}
	
	
	private void addPointToRoute(){
		RouteCalcController.instance().pointMapCenterToPOI();
		
		MenuControlIF.Instance().BackWinChange();
	}
	
	
	private void screenBack(){
		RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
	}
	
	
	private int getImageByPurpose(int purpose){
		int id = 0;
		switch (purpose) {
		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
			id = SingleBottomButton.BUTTON_STYLE_START_POINT;
			break;
		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
			int index = RouteCalcController.selectedIndexInEditMode;
			if (index == 1) {
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_1;
			} else if (index == 2) {
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_2;
			} else if (index == 3) {
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_3;
			} else if (index == 4){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_4;
			} else if (index == 5){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_5;
			} else if (index == 6){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_6;
			} else if (index == 7){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_7;
			} else if (index == 8){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_8;
			} else if (index == 9){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_9;
			} else if (index == 10){
				id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_10;
			}
			
			break;
		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
		case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
			id = SingleBottomButton.BUTTON_STYLE_END_POINT;
			break;
		default:
			id = SingleBottomButton.BUTTON_STYLE_WAY_POINT_3;
			break;
		}
		return id;
	}
	

	@Override
	public void onBack() {
		popup = MapOverwriteLayer.getInstance().getPopup();
		if(popup.isShowing()){
			popup.dismissPopup();
		}
		screenBack();
		MenuControlIF.Instance().BackWinChange();
	}
	
}
