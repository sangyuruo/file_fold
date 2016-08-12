package com.billionav.navi.component.mapcomponent;


import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIMapControlJNI.LogoMark;
import com.billionav.jni.UIMapControlJNI.MapSymbol;
import com.billionav.jni.UIMapControlJNI.MapTouchedMarkInfo;
import com.billionav.jni.UIMapControlJNI.Point;
import com.billionav.jni.UIMapControlJNI.Traffic;
import com.billionav.jni.UIMapControlJNI.UserReport;
import com.billionav.jni.UIMapControlJNI.Vicinity;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.jni.UITrafficControlJNI;
import com.billionav.jni.UIUserReportJNI;
import com.billionav.jni.UITrafficControlJNI.IconDetail;
import com.billionav.jni.UITrafficControlJNI.IconParam;
import com.billionav.jni.UITrafficControlJNI.TrafficIconDetailRes;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ADT_TouchPointList;
import com.billionav.navi.naviscreen.map.TouchMapControl;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.naviscreen.map.TouchMapControl.TmcPointBean;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.report.ADT_report_detail;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uicommon.UIC_SystemCommon;
import com.billionav.navi.uitools.GestureDetector;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;

public class MapOverwriteLayer extends RelativeLayout {
	private static MapOverwriteLayer instance;
	
	private ButtonMapZoom zoom;
	private final MapView mapView;

	
	private RelativeLayout leftLayout;  // contains my_location and calc route
	private ImageView myLocation;
	private ImageView calcRoute;
	
	protected final PopupPOI popuppoi;
	protected final PopupTmc popuptmc;
	private RelativeLayout rightLayout;
	private TextView crdl = null;
	private TextView gps = null;
	private TextView od = null;
	private TextView gy = null;
	private TextView gs = null;
	private TextView snd = null;
	private TextView prs = null;
	private boolean inoutFlg = false;
	private ModeTag certainModeTag = new ModeTag() {
		
		@Override
		public boolean isSuningSearchMode() {
			// TODO Auto-generated method stub
			return false;
		}
		
	};
	
//	private final PointList pointList;
	
	
//	private boolean needShowCursor = false;
	
	public static MapOverwriteLayer getInstance(){
		return instance;
	}
	
	public void onTrigger(NSTriggerInfo info){	
		switch(info.m_iTriggerID) {
			case NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED: 
				Log.d("test", "UIC_MN_TRG_POINT_SEARCH_FINISHED ");
				UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				long count = searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
				if((count <= 0) ||info.m_lParam2 != 0 || info.m_lParam1 != UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI)
				{
					Log.e("test", "find nearest parking failed");
					return;
				}
				if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()) {
					Log.d("test", "find nearest parking success, show dialog");
					showRerouteToParkingDialog();
				}
				return;
		}
	}
	CustomDialog reroutingDialog;
	private void showRerouteToParkingDialog() {
		if(reroutingDialog != null && reroutingDialog.isShowing()) {
			return;
		}
		reroutingDialog = new CustomDialog(getContext());
		reroutingDialog.setTitle(R.string.MSG_DEMO_DIALOG_CONFIRM_PARKING_REROUTE_TITLE);
		reroutingDialog.setMessage(R.string.MSG_DEMO_DIALOG_CONFIRM_PARKING_REROUTE_CONTENT);
		reroutingDialog.setPositiveButton(R.string.MSG_00_00_00_11, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				
				
				long count = searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
				Log.d("HybridUS", "count = " + count);
				if(count != 0) {
					SearchPointBean bean = SearchPointBean.createSearchPointBean(searchResult, 0);
					Log.d("HybridUS", "bean.name = " + bean.getName());
					RouteCalcController.instance().rapidGetRouteRequest(bean.getName(),bean.getLongitude(),bean.getLatitude());
				}
			}
		});
		reroutingDialog.setNegativeButton(R.string.MSG_00_00_00_12, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		reroutingDialog.show();
		
	}
	private ArrayList<PointBean> pointBeanList = null;
	private boolean isPopupWait = false;
	private void paseMarkInfo(){
		isPopupWait = false;
		MapTouchedMarkInfo markInfo = UIMapControlJNI.GetMapTouchedMarkInfo();
		LogoMark LogoMarkItems[] = markInfo.LogoMarkItems;
		Vicinity VicinityItems[] = markInfo.VicinityItems;
		Traffic TrafficItems[] = markInfo.TrafficItems;
		MapSymbol MapSymbolItems[] = markInfo.MapSymbolItems;
		UserReport UserReportItems[] = markInfo.UserReportItems;
		Point PointItems[] = markInfo.PointItems;
//		System.out.println("LogoMarkItems:"+LogoMarkItems.length+"  vi:"+VicinityItems.length
//		 +"   tr:"+TrafficItems.length+"  map:"+MapSymbolItems.length+"   user:"+UserReportItems.length+"  po:"+PointItems.length);
		Log.d("UIMap","LogoMarkItems:"+LogoMarkItems.length+"  vi:"+VicinityItems.length
				 +"   tr:"+TrafficItems.length+"  map:"+MapSymbolItems.length+"   user:"+UserReportItems.length+"  po:"+PointItems.length);
		 pointBeanList = new ArrayList<TouchMapControl.PointBean>();

		if (LogoMarkItems != null) {
			for (LogoMark mark : LogoMarkItems) {
				UISearchResultJNI searchResult = UISearchControlJNI.Instance()
						.GetSearchResult(mark.type);
				SearchPointBean p = SearchPointBean.createSearchPointBean(
						searchResult, mark.index);
				p.obtainPontBeanForPopup();
				if (p != null) {
					
					pointBeanList.add(p.obtainPontBeanForPopup());
				}
				
			}
		}

		if (VicinityItems != null) {
			for (Vicinity vici : VicinityItems) {
				UISearchResultJNI searchResult = UISearchControlJNI.Instance()
						.GetSearchResult(vici.type);
				SearchPointBean p = SearchPointBean.createSearchPointBean(
						searchResult, vici.index);
				if (p != null) {
					
					pointBeanList.add(p.obtainPontBeanForPopup());
				}
				
			}
		}

		if (MapSymbolItems != null) {
			for (MapSymbol p : MapSymbolItems) {
				if("".equals(p.strname)){
					continue;
				}
				PointBean point = new PointBean();
				point.setName(p.strname);
				long lonlat[] = { p.lonlat.iLon, p.lonlat.iLat };
				point.setLonlat(lonlat);
				point.setIndex(p.index);
				point.setType(p.type);
				point.setAddress("");
				point.setTelNo("");
				pointBeanList.add(point);
				
			}
		}
		
//		if (UserReportItems != null) {
//			for (UserReport p : UserReportItems) {
//				PointBean point = new PointBean();
//				switch(p.category){
//				case UIUserReportJNI.SNS_CATEGORY_TRAFFICJAM:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_05));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_TRAFFICACCIDENT:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_04));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_ROADCLOSE:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_11));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_TRAFFICCONTROL:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_10));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_ROADDAMAGE:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_09));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_WATERFROZEN:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_12));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_SPEEDCAMERA:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_07));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_POLICE:
//					point.setName(getResources().getString(R.string.STR_MM_08_01_01_03));
//					break;
//				case UIUserReportJNI.SNS_CATEGORY_OTHER:
//					point.setName(getResources().getString(R.string.STR_MM_08_05_01_01));
//					break;
//				default:
//					point.setName(getResources().getString(R.string.STR_MM_08_05_01_01));
//				}
//				long lonlat[] = { p.lonlat.iLon, p.lonlat.iLat };
//				point.setCategory(p.category);
//				point.setLonlat(lonlat);
//				point.setIndex(p.index);
//				point.setType(p.type);
//				point.setPostID(p.postID);
//				point.setAddress("");
//				point.setTelNo("");
//				pointBeanList.add(point);
//			}
//		}

		if (PointItems != null) {
			for (Point p : PointItems) {
				PointBean point = new PointBean();
				point.setName(p.strname);
				long lonlat[] = { p.lonlat.iLon, p.lonlat.iLat };
				point.setLonlat(lonlat);
				point.setIndex(p.index);
				point.setType(p.type);
				point.setAddress(p.straddress);
				point.setTelNo("");
				pointBeanList.add(point);
			}
		}
		
//		if(TrafficItems != null){
//			if (TrafficItems.length >0) {
//				isPopupWait = true;
//				IconParam params[] = new IconParam[TrafficItems.length];
//				for(int i = 0; i < TrafficItems.length; i++){
//					params[i] = new IconParam(TrafficItems[i].lonlat.iLon, TrafficItems[i].lonlat.iLat);
//				}
//				UITrafficControlJNI.TrafficIconDetailReq(params);
//			}
//		}
		
		if (!isPopupWait) {
			showPopup();
		}

	}
	
	private String getString(int sTR_MM_08_01_01_04) {
		// TODO Auto-generated method stub
		return null;
	}

	private void paseTrafficInfo() {
		TrafficIconDetailRes trafficDetail = UITrafficControlJNI.GetTrafficIconDetailRes();
		if(trafficDetail != null){
			IconDetail iconDetailItems[] = trafficDetail.iconDetailItems;
			if (iconDetailItems != null) {
				for (IconDetail p : iconDetailItems) {
					TmcPointBean point = new TmcPointBean();
					point.setName(p.detail_info);
					long lonlat[] = { p.lon, p.lat };
					point.setLonlat(lonlat);
					point.setType(UIMapControlJNI.MAP_MARK_TYPE_TRAFFIC);
					point.setCategory(p.symbol_id);
					point.setAddress("");
					point.setStartTime(p.start_time);
					point.setEndTime(p.end_time);
					pointBeanList.add(point);
				}
			}
		}
		
		if (isPopupWait) {
			showPopup();
		}
		
	}
	
	private void showPopup() {
//		Log.d("UIMap","pointBeanList size = " + pointBeanList.size());
		if (pointBeanList.size() == 1) {
			disposeShowPopup(pointBeanList.get(0));
		}
		if (pointBeanList.size() > 1) {
			if (ADT_Main_Map_Navigation.class.isAssignableFrom(MenuControlIF.Instance().GetCurrentWinscapeClass())) {
				BundleNavi.getInstance().put("PointList", pointBeanList);
				MenuControlIF.Instance().ForwardWinChange(ADT_TouchPointList.class);
			}
		}
		
	}

	protected void disposeShowPopup(PointBean bean){
		int type = bean.getType();
		if(certainModeTag.isSuningSearchMode()){
			if(bean.getType() == UIMapControlJNI.MAP_MARK_TYPE_VICINITY){
				popuppoi.showPopup(bean);
				popuptmc.dismissPopup();
			}
		}else{
			if (bean.getType() == UIMapControlJNI.MAP_MARK_TYPE_TRAFFIC) {
				popuptmc.showPopup(bean);
				popuppoi.dismissPopup();
			}else if (bean.getType() == UIMapControlJNI.MAP_MARK_TYPE_USER_REPORT) {
				BundleNavi.getInstance().putString("postID", bean.getPostID());
				MenuControlIF.Instance().ForwardWinChange(ADT_report_detail.class);
			}else{
				popuppoi.showPopup(bean);
				popuptmc.dismissPopup();
			}
			
		}

	}
	
	public MapOverwriteLayer(Context context) {
		super(context);
		instance = this;
		mapView = MapView.createInstance(context);
		RelativeLayout rlv = (RelativeLayout)mapView.getParent();
		if(null != rlv){
			rlv.removeView(mapView);
		}
		addView(mapView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.map_overwrite_layer, this);
	    
	    popuppoi = new PopupPOI(context);
	    popuptmc = new PopupTmc(context);
	    popuppoi.addToViewGroup(this);
	    popuptmc.addToViewGroup(this);
//	    pointList = new PointList(context);
	    
	    findViews();
	    
	    setListener();
	    
	    adjustLayout();
	    
	    closeMapElement();
	    
	}
	private onMapViewGestureListener mapGestureListener = null;
	private MotionEvent m_singleTapMotionEvent = null;

	public void addExternalMapViewGesture(onMapViewGestureListener mapViewGesture){
		this.mapGestureListener = mapViewGesture;
	}
	private void setListener() {
		calcRoute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RouteCalcController.instance().rapidRouteCalculateToMapCenter();
				
			}
		});
		myLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BundleNavi.getInstance().putBoolean("Navigation", true);
				MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
			}
		});
		
		mapView.addGestureListener(new GestureDetector.SimpleGestureListener(){
			
			
			
			@Override
			public boolean on2FingerParallelVerticalScroll(boolean isFrist,
					MotionEvent down1, MotionEvent down2, MotionEvent move,
					float verticalDistance) {
				// TODO Auto-generated method stub
				if(mapGestureListener != null){
					mapGestureListener.on2FingerParallelVerticalScroll();
				}
				return super.on2FingerParallelVerticalScroll(isFrist, down1, down2, move,
						verticalDistance);
			}
			@Override
			public boolean on2FingerParallelHorizontalScroll(boolean isFrist,
					MotionEvent down1, MotionEvent down2, MotionEvent move,
					float horizontalDistance) {
				// TODO Auto-generated method stub
				if(mapGestureListener != null){
					mapGestureListener.on2FingerParallelHorizontalScroll();
				}
				return super.on2FingerParallelHorizontalScroll(isFrist, down1, down2, move,
						horizontalDistance);
			}
			@Override
			public boolean onScrollEnd(MotionEvent down, MotionEvent up) {
				
				int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//				if(!SnsControl.Instance().IsInRange(lonlat[0], lonlat[1])) {
//					SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0], lonlat[1]);
//				}
				if(mapGestureListener != null){
					mapGestureListener.onScrollEnd();
				}
				return true;
			}
			@Override
			public boolean onScroll(boolean isFrist, MotionEvent down,
					MotionEvent move, float distanceX, float distanceY) {
				DontKeepPopupInScreen();
				if(mapGestureListener != null){
					mapGestureListener.onScroll();
				}
				return super.onScroll(isFrist, down, move, distanceX, distanceY);
			}
			@Override
			public boolean on2FingerScale(boolean isFrist, MotionEvent down1,
					MotionEvent down2, MotionEvent move, float distanceMove,
					float distanceDown, float distanceDiff) {
				DontKeepPopupInScreen();
				if(mapGestureListener != null){
					mapGestureListener.on2FingerScale();
				}
				return super.on2FingerScale(isFrist, down1, down2, move, distanceMove,
						distanceDown, distanceDiff);
			}
			@Override
			public boolean onLongPress(MotionEvent e) {
				Log.i("icon", "onLongPress");
				if(mapGestureListener != null){
					mapGestureListener.onLonClick();
				}
				return disposeOnLongPress(e);
			}
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if(null != mapGestureListener) {
					mapGestureListener.onClick();
				}
				popuppoi.dismissPopup();
				popuptmc.dismissPopup();
				int IsInCompassCircleReturnValue = UIMapControlJNI.IsInCompassCircle((int)e.getX(), (int)e.getY());
				Log.d("UI_Map", "IsInCompassCircleReturnValue = " + IsInCompassCircleReturnValue);
				m_singleTapMotionEvent = e;
//				if (1 == IsInCompassCircleReturnValue) {
//					
//				}else if(0 == IsInCompassCircleReturnValue) {
//					if (tapPopupEnable && !RouteTool.isDemo()) {
//						System.out.println("onsingle11:"+(int) e.getX()+"    "+(int) e.getY());
//						UIMapControlJNI.ReqMapTouchedPoint((int) e.getX(),(int) e.getY());
//					}
//				}
				
				return true;
			}
		});
	}
	

	
	protected boolean disposeOnLongPress(MotionEvent e){
//		if(tapPopupEnable && !RouteTool.isDemo() && !certainModeTag.isSuningSearchMode()) {
//			popuppoi.clearPopupInfo();			
//			PointBean bean = TouchMapControl.longPressMap(e.getX(), e.getY());
//			popuppoi.setClickable(true);
//			popuppoi.showPopup(bean);
////			popuptmc.dismissPopup();
//		}
		return true;
	}
	
	public void showPopupTmc(PointBean bean2) {
		popuptmc.showPopup(bean2);
		popuppoi.dismissPopup();
	}
	
	public void setTapPopupEnable(boolean tapPopupEnable) {
		this.tapPopupEnable = tapPopupEnable;
	}

	public void setNailOfPopupImage(int SourceSrId){
		if(popuppoi != null){
			popuppoi.setNailImage(SourceSrId);
		}
	}
	
	
	protected boolean tapPopupEnable = false;
	
	private void findViews() {
		zoom = (ButtonMapZoom) findViewById(R.id.map_button_zoom);
//		cursor = (Cursor) findViewById(R.id.map_cursor);
	
		leftLayout = (RelativeLayout) findViewById(R.id.map_overwite_layer_left);
		myLocation = (ImageView) findViewById(R.id.map_overwite_my_loction);
		calcRoute = (ImageView) findViewById(R.id.map_overwite_calc_route);
		
		rightLayout = (RelativeLayout) findViewById(R.id.map_overwite_layer_cradle_info);
		crdl = (TextView) findViewById(R.id.map_overwite_Crdl);
		gps = (TextView) findViewById(R.id.map_overwite_GPS);
		od = (TextView) findViewById(R.id.map_overwite_OD);
		gy = (TextView) findViewById(R.id.map_overwite_GY);
		gs = (TextView) findViewById(R.id.map_overwite_GS);
		snd = (TextView) findViewById(R.id.map_overwite_SND);
		prs = (TextView) findViewById(R.id.map_overwite_Prs);
		
		prs.setVisibility(View.GONE);
		snd.setVisibility(View.GONE);
		crdl.setText("Crdl");
		gps.setText("GPS");
		od.setText("0D");
		gy.setText("GY");
		gs.setText("GS");
		crdl.setTextColor(Color.RED);
		gps.setTextColor(Color.RED);
		od.setTextColor(Color.RED);
		gy.setTextColor(Color.RED);
		gs.setTextColor(Color.RED);
	
	    switchCradleInfo();
		
		
	}

	public void switchCradleInfo() {
//		boolean isOn = jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_CRADLE_INFO_OUTPUT);
//		if(isOn){
//			rightLayout.setVisibility(View.VISIBLE);
//		}else{
//			rightLayout.setVisibility(View.GONE);
//		}
	}
	
	private void adjustLayout() {
		final int top = DensityUtil.dp2px(getContext(), ScreenMeasure.isPortrait() ? 180 : 122);
		((RelativeLayout.LayoutParams)zoom.getLayoutParams()).topMargin = top;
		((RelativeLayout.LayoutParams)leftLayout.getLayoutParams()).topMargin = top;
	}
	
	public void showMapElement() {
		zoom.setVisibility(VISIBLE);
		leftLayout.setVisibility(VISIBLE);
	}

	public void showMapElementRoutePoint(){
		zoom.setVisibility(VISIBLE);
	}
	
	public void hideMapElementRoutePoint(){
		zoom.setVisibility(GONE);
	}
	
	public void showMapElementRouteProfile(){
		zoom.setVisibility(VISIBLE);
	}
	
	public void hideMapElementRouteProfile(){
		zoom.setVisibility(GONE);
	}
	public void showMapElementSearchMapDetail(){
		calcRoute.setVisibility(VISIBLE);
	}
	public void hideMapElementSearchMapDetail(){
		calcRoute.setVisibility(GONE);
	}
	public void closeMapElement() {
		zoom.setVisibility(GONE);

		leftLayout.setVisibility(GONE);
//		cursor.setVisibility(GONE);
	}
	
	public void notifyScreenChanged() {
		popuppoi.dismissPopup();
		popuptmc.dismissPopup();
	}
	
	public void notifyTriggerReceived(NSTriggerInfo triggerInfo) {
		
		// cradle info 
		setCradleInfo(triggerInfo);

		if(triggerInfo.m_iTriggerID == NSTriggerID.SEARCH_RESPONSE_PINPOINT_INFO) {
			if(popuppoi.isShowing() && popuppoi.getPopupType()==TouchMapControl.TYPE_LONGPRESS) {
//				String name = TouchMapControl.getPOIName(triggerInfo);
//				if(name != null) {
//					popuppoi.setPointName(name);
//				}
				
				SearchPointBean longPressBean = TouchMapControl.getLongPressBean(triggerInfo);
				if(longPressBean != null) {
				
					popuppoi.showPopupWithoutAnim(longPressBean);
				}
			}
			return;
		}
		
		if(triggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_MC_DAYNIGHT_CHANGE){
			UIC_SystemCommon.setIsDayStatus(0 == triggerInfo.GetlParam1());
//			if(jniSetupControl.get(jniSetupControl.STUPDM_MAPCOLOR_CHANGE) == jniSetupControl.STUPDM_MAP_COLOR_CHANGE_BY_TIME){
//				if(UIC_SystemCommon.getIsDayStatus()){
//					UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_DAY);
//				}else{
//					UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_NIGHT);
//				}
//			}
			return ;
		}
		
		if(triggerInfo.GetTriggerID() == NSTriggerID.MAPOPERATION_RESPONSE_FOR_IS_IN_COMPASSCIRCLE){
			if(triggerInfo.m_lParam1 == 0) {
				if(null != m_singleTapMotionEvent) {
					MotionEvent e = m_singleTapMotionEvent;
					m_singleTapMotionEvent = null;
					if (tapPopupEnable && !RouteTool.isDemo()) {
					System.out.println("onsingle11:"+(int) e.getX()+"    "+(int) e.getY());
					UIMapControlJNI.ReqMapTouchedPoint((int) e.getX(),(int) e.getY());
					}
				}
			} else {
				//map has implemented
//				UIMapControlJNI.SetMapDir((short) Math.abs(UIMapControlJNI.GetMapDir()-1));
			}
		}
		if(triggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_MAP_TOUCHED_MARK_INFO){
				System.out.println("NSTriggerID.UIC_MN_TRG_MAP_TOUCHED_MARK_INFO");
				paseMarkInfo();
		}
		
		if(triggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_TRAFFIC_ICON_DETAIL_RES){
			//paseTrafficInfo();
		}
		
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE){
			return;
		}
		
		if(popuppoi.isShowing()) {
			popuppoi.refreshLoaction();
		}
		
		if(popuptmc.isShowing()) {
			popuptmc.refreshLoaction();
		}
		
		if(zoom.getVisibility() == VISIBLE){
			zoom.updateButtonStatus();
		}
		
		
		
		
//		if(MapEngineJni.GetCarPositonMode()) {
//			calcRoute.setVisibility(GONE);
//		} else {
//			calcRoute.setVisibility(VISIBLE);
//		}
		
	}

	private void setCradleInfo(NSTriggerInfo triggerInfo) {
//		CLocationListener cloca = CLocationListener.Instance();
//		if (NSTriggerID.UIC_MN_TRG_LOC_DEBUG_INFO == triggerInfo.m_iTriggerID) {
//    		long temp = triggerInfo.GetlParam1();
//    		//CradleConnected
//    		if (jniLocInfor.getLocInfor().GetCertificateRet()){
//    			crdl.setTextColor(Color.BLUE);
//    		}
//    		else {
//    			crdl.setTextColor(Color.RED);
//    		}
//    		crdl.setText("Crdl");

    		//GPSConnected
//    		if (cloca.isCurrentInternalGpsOn() == true) {
//    			inoutFlg = true;
//    		}
//    		else {
//    			inoutFlg = false;
//    		}
//
//    		if (2 == (temp & 0x0002)) {
//        		if (inoutFlg == true) {
//        			gps.setTextColor(Color.GREEN);
//        		}
//        		else {
//        			gps.setTextColor(Color.BLUE);
//        		}
//    		}
//    		else {
//    			gps.setTextColor(Color.RED);
//    		}
//    		
//    		//GpsDimension
//    		if (12 == (temp & 0x000C)) {
//    			//3D
//    			if (inoutFlg == true) {
//    				od.setTextColor(Color.GREEN);
//    			}
//    			else {
//    				od.setTextColor(Color.BLUE);
//    			}
//    			od.setText("3D");
//    		}
//    		else if (8 == (temp & 0x0008)) {
//    			//2D
//    			if (inoutFlg == true) {
//    				od.setTextColor(Color.GREEN);
//    			}
//    			else {
//    				od.setTextColor(Color.BLUE);
//    			}
//    			od.setText("2D");
//    		}
//    		else if (4 == (temp & 0x0004)) {
//    			//1D
//    			if (inoutFlg == true) {
//    				od.setTextColor(Color.GREEN);
//    			}
//    			else {
//    				od.setTextColor(Color.BLUE);
//    			}
//    			od.setText("1D");
//    		}
//    		else {
//    			//0D
////    			if (inoutFlg == true) {
//    			od.setTextColor(Color.RED);
//    			od.setText("0D");
//    		}
//    		
//    		//GyroLearnStatus
//    		if (16 == (temp & 0x0010)) {
//    			if (inoutFlg == true) {
//    				gy.setTextColor(Color.RED);
//    			}
//    			else {
//    				gy.setTextColor(Color.BLUE);
//    			}
//    		}
//    		else {
//    			gy.setTextColor(Color.RED);
//    		}
//    		
//    		//GsnsLearnStatus
//    		if (32 == (temp & 0x0020)) {
//    			if (inoutFlg == true) {
//    				gs.setTextColor(Color.RED);
//    			}
//    			else {
//    				gs.setTextColor(Color.BLUE);
//    			}
//    		}
//    		else {
//    			gs.setTextColor(Color.RED);
//    		}
//			
//    	} else if (NSTriggerID.UIC_MN_TRG_DUMMY_SOUND_STATE == triggerInfo.m_iTriggerID) {
////    		if (snd == null) return ;
////			if (triggerInfo.GetlParam1() == 1) {
////				snd.setTextColor(Color.BLUE);
////			} else {
////				snd.setTextColor(Color.RED);
////			}
//    	} else if (NSTriggerID.UIC_MN_TRG_ECL_CERTIFICATE_FINISHED == triggerInfo.m_iTriggerID) {
//    		if (triggerInfo.GetlParam1() == 1) {
//    			crdl.setTextColor(Color.BLUE);
//    		} else {
//    			crdl.setTextColor(Color.RED);
//    		}
//    	}
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		adjustLayout();
	}
	
	public ButtonMapZoom getButtonZoom() {
		return zoom;
	}
	
	public void DontKeepPopupInScreen(){
		popuppoi.setNeedKeepPopupInScreen(false);
	}
	public PopupPOI getPopup() {
		return popuppoi;
	}
	
	public interface ModeTag{
		public boolean isSuningSearchMode();
	}
	
	public void setModeTag(ModeTag tag) {
		if(null == tag) {
			return;
		}
		
		this.certainModeTag = tag;
	}
	
	public boolean isSuningSearchMode() {
		return certainModeTag.isSuningSearchMode();
	}
	public interface onMapViewGestureListener{
		public void onScroll();
		public void onClick();
		public void onLonClick();
		public void on2FingerScale();
		public void onScrollEnd();
		public void on2FingerParallelVerticalScroll();
		public void on2FingerParallelHorizontalScroll();
	}
}
