package com.billionav.navi.naviscreen.map;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class Map_Main_Layer2 extends RelativeLayout {
	private View searchInput;

	
	private RelativeLayout layerLeftLayout; 
	private ImageView myLoction;
	private ImageView goRoute;
	
	public Map_Main_Layer2(Context context) {
		super(context);
		
		inflateLayout(context);
	    
		findViews();
		
		setListener();
		
		initialize();

	}

	protected void inflateLayout(Context context){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.adt_map_layers, this);
	}
	
	protected void findViews() {
		searchInput = (RelativeLayout)findViewById(R.id.adt_map_search_bar);
		
		layerLeftLayout = (RelativeLayout) findViewById(R.id.map_layer_left);
		myLoction = (ImageView) findViewById(R.id.my_loction);
		goRoute = (ImageView) findViewById(R.id.calc_route);
		
		
	}
	
	private void initialize() {
		initilizeLayout();
	}

	protected void setListener() {
		searchInput.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
				
			}
		});
		
		goRoute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RouteCalcController.instance().rapidRouteCalculateToMapCenter();
			}
		});
		
	}
	
	public void showLeftComponents() {
		if(layerLeftLayout.getVisibility() != VISIBLE) {
			layerLeftLayout.setVisibility(VISIBLE);
		}
	}
	
	public void hideLeftComonents() {
		layerLeftLayout.setVisibility(GONE);
	}
	
	public void show() {
		UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_MainMap);
		setVisibility(VISIBLE);

		if(UIMapControlJNI.GetCarPositonMode()) {
			hideLeftComonents();
		} else {
			showLeftComponents();
		}
		
		if(isDemoRun()) {
			searchInput.setVisibility(GONE);
			goRoute.setVisibility(View.GONE);
		} else {
			searchInput.setVisibility(VISIBLE);
			goRoute.setVisibility(View.VISIBLE);
		}
		
//		compass.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				int d = compass.getBottom();
//				MapEngineJni.SetScaleBarPos(DensityUtil.dp2px(getContext(), 6), d+25, 0);
//				
//			}
//		});

	
		
		initilizeLayout();
	}
	
	public void showWithoutAnimation() {
		UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_MainMap);
		setVisibility(VISIBLE);
		hideLeftComonents();
	}

	public void hide() {
		setVisibility(GONE);
	}
	
	public boolean isShowing() {
		return getVisibility() == VISIBLE;

	}
	
	public void setMyLocationClickListener(OnClickListener l) {
		myLoction.setOnClickListener(l);
	}
	
	protected boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
			return true;
		case NSTriggerID.UIC_MN_TRG_DEMO_START:
			searchInput.setVisibility(GONE);
			goRoute.setVisibility(GONE);
			return true;
		case NSTriggerID.UIC_MN_TRG_DEMO_STOP:
			searchInput.setVisibility(VISIBLE);
			goRoute.setVisibility(VISIBLE);
			return true;
		}
		return false;
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		adjustLayout();
	}
	
	private void initilizeLayout() {
		adjustLayout();
	}

	private void adjustLayout() {
		
		int top = DensityUtil.dp2px(getContext(), ScreenMeasure.isPortrait() ? 180 : 0);
		((RelativeLayout.LayoutParams)layerLeftLayout.getLayoutParams()).topMargin = top;
	}
	
	private boolean isDemoRun() {
		return (new UIPathControlJNI().GetDemoStatus() == UIPathControlJNI.UIC_PT_DEMO_STATUS_ON);
	}

}
