package com.billionav.navi.naviscreen.favorite;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.billionav.jni.UIPointData;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.component.mapcomponent.PopupPOI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.map.POI_Mark_Control;
import com.billionav.navi.net.PostData;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.MapTools;
import com.billionav.ui.R;

public class ADT_Favorite_CheakMap extends ActivityBase implements OnScreenBackListener, MapScreen{
	private RelativeLayout showList;
	
	private PopupPOI popup;
	private UIPointData PointData = null;
	private int record = 0;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_poi_result);
		initialize();
		findViews();
		setListener();
		setActionbarInfo(record);
	}
	
	private void initialize() {
		record = getBundleNavi().getInt("record");
		PointData = (UIPointData) getBundleNavi().get("pointdata");
	}
	
	private void findViews() {
		showList = (RelativeLayout)findViewById(R.id.button_show_list);
		popup = MapOverwriteLayer.getInstance().getPopup();


	}
	
	private void setListener() {
		showList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismissPopup();
				UIC_SreachCommon.backToListScreen();
			}
		});
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		POI_Mark_Control.forFavoriteView();
		showPopupInCenter(PointData);
		MapOverwriteLayer.getInstance().showMapElement();
		MapOverwriteLayer.getInstance().setTapPopupEnable(true);
		MapOverwriteLayer.getInstance().hideMapElementSearchMapDetail();
	}
	
	@Override
	protected void OnPause() {
		super.OnPause();
		popup.dismissPopup();
		MapOverwriteLayer.getInstance().closeMapElement();
		MapOverwriteLayer.getInstance().setTapPopupEnable(false);
		MapOverwriteLayer.getInstance().showMapElementSearchMapDetail();
	}
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
		popup.dismissPopup();
	}
	
//	private void showPopupInCenter(jniUIC_PNT_PointData_new PointData) {
//		
//		MapTools.setCenterLonlat((int)PointData.GetLonLat()[0], (int)PointData.GetLonLat()[1]);
//		popup.showPopup(PointData);
//	}

	private void showPopupInCenter(final UIPointData pointData2) {
		
		MapTools.setCenterLonlat((int)pointData2.getLonlat()[0], (int)pointData2.getLonlat()[1]);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				popup.showPopup(pointData2);
			}
		}, 500);
	}
	
	

	private void setActionbarInfo(int genreIndex) {
		setTitle(PointData.getName());
	}
	
	@Override
	public void onBack() {
		popup.dismissPopup();
		MenuControlIF.Instance().BackWinChange();
		
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		int iTriggerID = triggerInfo.GetTriggerID();
		PLog.i("Trigger","Trigger");
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
			PLog.i("Trigger", "UIC_MN_TRG_MAP_DRAW_DONE");
			popup.refreshLoaction();
			return true;
		}
		return false;
	}
}
