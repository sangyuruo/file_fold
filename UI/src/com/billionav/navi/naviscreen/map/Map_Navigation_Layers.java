package com.billionav.navi.naviscreen.map;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.NaviUtil;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.GuideInfoBar;
import com.billionav.navi.component.guidebar.GuideInfoBar.IllustStatusListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.sync.AppLinkService;
import com.billionav.navi.sync.SyncUI;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.navi.uitools.getRouteInfoTask;
import com.billionav.navi.uitools.getRouteInfoTask.RouteInfoData;
import com.billionav.ui.R;

public class Map_Navigation_Layers extends RelativeLayout {

	private GuideInfoBar guideInfoBar;
	

	private View searchInput;
//	private View viewReference;
	
	private RelativeLayout demolayout;
	private ImageView speedometer;
	private ImageView stopDemo;
	private LinearLayout companyLayout;
	private LinearLayout homeLayout;
	private TextView homeText;
	private TextView companyText;
	
	private LinearLayout parking1Layout;
	private LinearLayout parking2Layout;
	private TextView parking1Text;
	private TextView parking2Text;
	
	private Button startSync;
	
	private int guideStatus;
	int[] imageIds = {R.xml.speed1_demo_selector, R.xml.speed2_demo_selector, R.xml.speed3_demo_selector};
	
	private IllustStatusListener illustListener;
	
	public void setIllustListener(IllustStatusListener illustListener) {
		this.illustListener = illustListener;
	}

	public Map_Navigation_Layers(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.src_map_navigation, this);
	    
		findViews();
		
		initialize();
		
		setListeners();

	}
	
	private void initialize() {
		UIMapControlJNI.SetCarPositonMode(true);
//		UIGuideControlJNI.getUIGuideControlJNI().DemoSpeedAdjust_SpeedAuto();
		adjustLayout() ;
		guideStatus = UIGuideControlJNI.getInstance().GetDispStatus() & UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO;
		demolayout.setVisibility(View.GONE);
		
		resetDemoMeter();
	}
	

	int meterState  = 0;
	private void resetDemoMeter() {
		speedometer.setBackgroundResource(imageIds[0]);
		meterState = 0;
	}
	private void setListeners() {
		searchInput.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
				
			}
		});
		stopDemo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NaviUtil.stopDemoDriving();
//				new UIPathControlJNI().StopDemoDriving();
			}
		});
		
		guideInfoBar.setIllustStatusListener(new GuideInfoBar.IllustStatusListener() {
			
			@Override
			public void onIllustViewStatusChange(boolean isIllustShowing) {
				if(illustListener != null) {
					illustListener.onIllustViewStatusChange(isIllustShowing);
				}

			
				updateScalebar();

				
			}
		});
		speedometer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				switch (meterState) {
				case 0:
					UILocationControlJNI.setDemoSpeed(1);
					speedometer.setBackgroundResource(imageIds[1]);
					meterState=1;
					break;

				case 1:
					UILocationControlJNI.setDemoSpeed(2);
					speedometer.setBackgroundResource(imageIds[2]);
					meterState=2;
					break;

				case 2:
					UILocationControlJNI.setDemoSpeed(0);
					speedometer.setBackgroundResource(imageIds[0]);
					meterState=0;
					break;

				default:
					break;
				}
			}
		});
		homeLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(null != getRouteInfoTask.getInstance().gethomeData()){
					UIPointData homeData = getRouteInfoTask.getInstance().gethomeData().data;
					RouteCalcController.instance().rapidRouteCalculateWithData(homeData.getName(), homeData.getLon(), homeData.getLat(),
							homeData.getAddress(), homeData.getTelNo());
				}
				
			}
		});
		companyLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(null != getRouteInfoTask.getInstance().getCompanyData()){
					UIPointData compData = getRouteInfoTask.getInstance().getCompanyData().data;
					RouteCalcController.instance().rapidRouteCalculateWithData(compData.getName(), compData.getLon(), compData.getLat(),
							compData.getAddress(), compData.getTelNo());
				}
				
			}
		});
		parking1Layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(parkings != null &&  parkings.get(0) != null) {
					UIPointData data = parkings.get(0);
					RouteCalcController.instance().rapidGetRouteRequest(data.getName(),data.getLon(),data.getLat());
				}
				
			}
		});
		parking2Layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(parkings != null &&  parkings.get(1) != null) {
					UIPointData data = parkings.get(1);
					RouteCalcController.instance().rapidGetRouteRequest(data.getName(),data.getLon(),data.getLat());
				}
				
			}
		});
	}
	
	private void findViews() {
		guideInfoBar = (GuideInfoBar) findViewById(R.id.guideInfoBar);
		searchInput = findViewById(R.id.src_map_navigation_searchbar);
	
		homeLayout = (LinearLayout) findViewById(R.id.home);
		companyLayout = (LinearLayout) findViewById(R.id.company);

		homeText = (TextView) findViewById(R.id.home_text);
		companyText = (TextView) findViewById(R.id.company_text);
//		viewReference = findViewById(R.id.reference_view);
		
		demolayout = (RelativeLayout) findViewById(R.id.demoLayout);
		speedometer = (ImageView) findViewById(R.id.demo_speedometer);
		stopDemo = (ImageView) findViewById(R.id.demo_stop);
		
		parking1Layout = (LinearLayout) findViewById(R.id.parking1);
		parking2Layout = (LinearLayout) findViewById(R.id.parking2);
		parking1Text = (TextView) findViewById(R.id.parking1_text);
		parking2Text = (TextView) findViewById(R.id.parking2_text);
        
		//TODO 1216 enable link start
//		startSync = (Button) findViewById(R.id.startSync);
//		startSync.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				AppLinkService service = AppLinkService.getInstance();
//				if(null == service){
//					NaviViewManager.GetViewManager().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//					SyncUI.sharedInstance().propertiesUI();
//					startSync.setText("Stop");
//				}else if(!service.isServieStarted()){
//					NaviViewManager.GetViewManager().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//					SyncUI.sharedInstance().propertiesUI();
//					startSync.setText("Stop");
//				}else{
//					NaviViewManager.GetViewManager().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//					service.stopSync();
//					startSync.setText("Link");
//				        
//				}
//			}
//		});
		//TODO 1216 enable link end
		
        NaviViewManager.GetViewManager().sync.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppLinkService service = AppLinkService.getInstance();
				if(service != null){
					NaviViewManager.GetViewManager().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
					service.stopSync();
					NaviViewManager.GetViewManager().applinkLogo.setVisibility(View.GONE);
					startSync.setText("Link");
					//NaviViewManager.GetViewManager().unregisterReceiver();
				}
			}
		});
	}
	
	public void show() {

		UIMapControlJNI.SetCarPositonMode(true);
		UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_Navigation);
		homeLayout.setVisibility(View.GONE);
		companyLayout.setVisibility(View.GONE);
		parking1Layout.setVisibility(View.GONE);
		parking2Layout.setVisibility(View.GONE);
		UIGuideControlJNI.getInstance().requestSearchParkingPotNearby();
		guideInfoBar.initGuideInfo();
		setVisibility(View.VISIBLE);
		refreshNavigationScreen();
	}
	
	private void refreshNavigationScreen(){
		guideInfoBar.initGuideInfo();
		refreshDemoButton();
		if(0 != (guideStatus & UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO)){
//		if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()){
			searchInput.setVisibility(View.GONE);
			guideInfoBar.show();
		}else{
			getRouteInfoTask.getInstance().doTask();
			searchInput.setVisibility(View.VISIBLE);
			guideInfoBar.hide();
		}
		updateScalebar();
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		adjustLayout() ;

	}
	
	private void adjustLayout() {
//		setReferceViewHasHeight(ScreenMeasure.isPortrait());
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)speedometer.getLayoutParams();
		lp.setMargins(0, 0, 0, ScreenMeasure.isPortrait()?DensityUtil.dp2px(getContext(), 56):0);
	}
	
	private boolean illustExsit() {
		return guideInfoBar.isIllustViewShowing();
	}
	
	public boolean closeIllustView() {
		if(illustExsit()) {
			guideInfoBar.closeIllustView();
			return true;
		}
		
		return false;
	}

//	private void setReferceViewHasHeight(boolean isPortrait){
//		viewReference.getLayoutParams().height = isPortrait ? DensityUtil.dp2px(getContext(), 54) : 0;
//	}

	public void hide() {
		closeIllustView();
		setVisibility(View.GONE);
//		guideInfoBar.hide();
//		
//		compass.setVisibility(View.GONE);		
//		viewReference.setVisibility(View.GONE);		
//		demolayout.setVisibility(View.GONE);
//		speedometer.setVisibility(View.GONE);
//		stopDemo.setVisibility(View.GONE);
	}
	
	public boolean isShowing() {
		return getVisibility() == View.VISIBLE;
	}
	
	public void updateScalebar() {
//		if(!isShowing()) {
//			return;
//		}
//		compass.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				int d = compass.getBottom();
//				MapEngineJni.SetScaleBarPos(DensityUtil.dp2px(getContext(), 6), d+25, 0);
//				
//			}
//		});
	}
	
	private boolean isDemo() {
		return (new UIPathControlJNI().GetDemoStatus() == UIPathControlJNI.UIC_PT_DEMO_STATUS_ON);
	}
	
	private void refreshDemoButton(){
		if(isDemo()) {
//			MapView.getInstance().setGestureEnable(false);
			demolayout.setVisibility(VISIBLE);
//			MapOverwriteLayer.getInstance().setTapPopupEnable(false);
		} else {
//			MapView.getInstance().setGestureEnable(true);
			demolayout.setVisibility(GONE);
//			MapOverwriteLayer.getInstance().setTapPopupEnable(true);
		}
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x111){
				if(isShowing()){
					//just refresh the state of map navigaiton layer style(searchBar or guideState)
					refreshNavigationScreen();
				}
			}
		};
	};
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		
		if(guideInfoBar.notifyTrigger(cTriggerInfo)){
		}
		

		
		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE:
			if(guideStatus != (UIGuideControlJNI.getInstance().GetDispStatus() & UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO)){
				guideStatus = (UIGuideControlJNI.getInstance().GetDispStatus()& UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO);
				if(handler.hasMessages(0x111)){
					handler.removeMessages(0x111);
				}
				handler.sendEmptyMessageDelayed(0x111, 700);
			}else{
//				if(handler.hasMessages(0x111)){
//					handler.removeMessages(0x111);
//				}
			}
			break;
		case NSTriggerID.UIC_MN_TRG_PATH_FIND_FINISH_PATH_INFO:
			updateHomeCompanyBtn();
			break;
		case NSTriggerID.UIC_MN_TRG_DEMO_START:
//			MapView.getInstance().setGestureEnable(false);
			demolayout.setVisibility(VISIBLE);
			POI_Mark_Control.forDemoRun();
			guideInfoBar.nodifyDemoState(isDemo());
//			MapOverwriteLayer.getInstance().setTapPopupEnable(false;
			break;
		case NSTriggerID.UIC_MN_TRG_DEMO_STOP:
//			MapView.getInstance().setGestureEnable(true);
			POI_Mark_Control.forNavigaitonView();
			demolayout.setVisibility(GONE);
			resetDemoMeter();
			guideInfoBar.nodifyDemoState(isDemo());
//			MapOverwriteLayer.getInstance().setTapPopupEnable(true);

			break;
		case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
			
			break;
/*		case NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED: 
			Log.d("test", "UIC_MN_TRG_POINT_SEARCH_FINISHED ");
			UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
			long count = searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
			if((count <= 0) ||cTriggerInfo.m_lParam2 != 0 || cTriggerInfo.m_lParam1 != UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI)
			{
				Log.e("test", "find nearest parking failed");
				break;
			}
			if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()) {
				Log.d("test", "find nearest parking success, show dialog");
				showParkingInfo();
			}
			break;*/
		case NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED: {
				Log.i("test", "recv nearest parking info");
				UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				Log.i("SDL", "ADT_Srch_Map.class onSearchFinished 6");
				long count = searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
				
				if((count <= 0) ||cTriggerInfo.m_lParam2 != 0 || cTriggerInfo.m_lParam1 != UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI)
				{	
					Log.i("test",  "search poi failed:\ncount=" 
						+ count + "\tParam2=" + cTriggerInfo.m_lParam2 + "\tParam1=" + cTriggerInfo.m_lParam1);	
					NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR, "search poi failed:\ncount=" 
							+ count + "\tParam2=" + cTriggerInfo.m_lParam2 + "\tParam1=" + cTriggerInfo.m_lParam1);
					if (null != AppLinkService.getInstance()) {
						AppLinkService.getInstance().sendPOISearchFailed();
					}
				}else{
					
					long genreID = searchResult.GetListItemGenreIDAt(0, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
					Log.d("test", "generID = " + genreID);
					//SearchPointBean testBean = SearchPointBean.createSearchPointBean(searchResult, 0);
					//Log.d("test", "test bean.generID = " + testBean.getGenerID());
					parkings =new ArrayList<UIPointData>();
							
//					if(testBean.getGenerID() == 3){
						parkings.clear();
						for(int i=0; i< count; ++i) {
							SearchPointBean bean = SearchPointBean.createSearchPointBean(searchResult, i);
							UIPointData data = new UIPointData();
							data.setName(bean.getName());
							data.setLon(bean.getLongitude());
							data.setLat(bean.getLatitude());
							data.setAddress(bean.getAddress());
							data.setTelNo(bean.getTelNo());
							data.setDistance(bean.getDistence());
							parkings.add(data);
						}
//					}
					if (null != AppLinkService.getInstance()) {
						AppLinkService.getInstance().sendPOISearchSuccessed(parkings);
					}
//					showParkingInfo();
				}
				break;
			}
	
		default:
			break;
		}
		
		return false;
	}
	
	private ArrayList<UIPointData> parkings;
	private void showParkingInfo() {
		Log.d("test", "111111111");
		if(parkings != null) {
			Log.d("test", "2222222");
			if(parkings.size() > 0) {
				Log.d("test", "33333333");
				if(parkings.get(0) != null) {
					Log.d("test", "444444"+parkings.get(0).getName()+ "\n" +RouteTool.getDisplayDistance(parkings.get(0).getDistance()));
					parking1Layout.setVisibility(View.VISIBLE);
					parking1Text.setText(parkings.get(0).getName()+ "\n" +RouteTool.getDisplayDistance(parkings.get(0).getDistance()));
				}
			}
			/*if(parkings.size() > 1) {
				Log.d("test", "55555");
				if(parkings.get(1) != null) {
					Log.d("test", "666666"+parkings.get(1).getName()+ "\n" +RouteTool.getDisplayDistance(parkings.get(1).getDistance()));
					parking1Layout.setVisibility(View.VISIBLE);
					parking1Text.setText(parkings.get(1).getName()+ "\n" +RouteTool.getDisplayDistance(parkings.get(1).getDistance()));
				}
			}*/
		}
	
	}


	private void updateHomeCompanyBtn() {
		RouteInfoData homeData = getRouteInfoTask.getInstance().gethomeData();
		if(homeData != null && homeData.hasData) {
			homeLayout.setVisibility(View.VISIBLE);
			homeText.setText(getFormatText(homeData.ETA_distance, homeData.ETA_time));
		} else {
			homeLayout.setVisibility(View.GONE);
		}
		
		RouteInfoData compData = getRouteInfoTask.getInstance().getCompanyData();
		
		if(compData != null && compData.hasData) {
			companyLayout.setVisibility(View.VISIBLE);
			companyText.setText(getFormatText(compData.ETA_distance, compData.ETA_time));
		} else {
			companyLayout.setVisibility(View.GONE);
		}
	}

	private String getFormatText(int distance, int time) {
		String strDist = RouteTool.getDisplayDistance(distance);
		String strTime = RouteTool.getDisplayDurition(time);
		return "distance:"+strDist+" duration:"+strTime;
	}
	public void onResume(){
		adjustLayout();
		guideInfoBar.nodifyDemoState(isDemo());
		guideInfoBar.updateMapViewForIllust();
		guideInfoBar.refreshIllustIcon();
	}

	public void destory() {
		guideInfoBar.destory();
		
	}

}
