package com.billionav.navi.naviscreen.srch;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.component.mapcomponent.PopupPOI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.POI_Mark_Control;
import com.billionav.navi.naviscreen.map.TouchMapControl;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.MapTools;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;

public class ADT_Srch_Result extends ActivityBase implements MapScreen{
	
	private RelativeLayout showList;
	private PopupPOI popup;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_srch_map);
		setTitleBackgroundBlack();
		findViews();
		setListeners();
	}
	
	private void findViews() {
		showList = (RelativeLayout) findViewById(R.id.show_list);
		popup = MapOverwriteLayer.getInstance().getPopup();
	}
	
	private void setListeners() {
		showList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismissPopup();
				UIC_SreachCommon.backToListScreen();
			}
		});
	}

	@Override
	protected void OnDestroy() {
		popup.dismissPopup();
		super.OnDestroy();
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		
		POI_Mark_Control.forPOISearchResultView();
		
		if(getBundleNavi().getBoolean("isCheakMap")) {
			if(getBundleNavi().getInt("searchType") == TouchMapControl.TYPE_LONGPRESS){
				PointBean bean = (PointBean) getBundleNavi().get("PointBean");
				showPopupInCenter(bean);
			} else {
				SearchPointBean bean = (SearchPointBean) getBundleNavi().get("SearchPointBean");
				showPopupInCenter(bean);
			}
		}
//		UIC_MapEngineFrameView.getInstance().setGestureListener(genstureListener);
		MapOverwriteLayer.getInstance().showMapElement();
		MapOverwriteLayer.getInstance().setTapPopupEnable(true);
//		MapOverwriteLayer.getInstance().needShowCursor(true);
		MapOverwriteLayer.getInstance().hideMapElementSearchMapDetail();
	}
	
	private void showPopupInCenter(final SearchPointBean bean) {
		MapTools.setCenterLonlat((int)bean.getLongitude(), (int)bean.getLatitude());
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				popup.showPopup(bean);
				
			}
		});
		setTitle(bean.getName());
	}

	private void showPopupInCenter(final PointBean bean) {
		
		MapTools.setCenterLonlat((int)bean.getLonlat()[0], (int)bean.getLonlat()[1]);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				popup.showPopup(bean);
				
			}
		}, 500);
		setTitle(bean.getName());
	}
	
	@Override
	protected void OnPause() {
		super.OnPause();
		MapOverwriteLayer.getInstance().closeMapElement();
		MapOverwriteLayer.getInstance().setTapPopupEnable(false);
		MapOverwriteLayer.getInstance().showMapElementSearchMapDetail();
	}
	
}
