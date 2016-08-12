package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.favorite.ADT_Favorite_CheakMap;
import com.billionav.navi.naviscreen.favorite.ADT_Favorite_Detail;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.TouchMapControl;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Detail;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Result;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;

public class PopupPOI extends RelativeLayout{
	private static final int POI_WIDTH_DIP = 250;
	private static final int POI_HEIGHT_DIP = 80;
	
	private ImageView imagebutton01;
	private TextView textview;
	private LinearLayout popupView;
	private ImageView nail;
	private long[] lonlat;
	private float[] centerLonlat;
	private boolean needKeepPopupInScreen;
	
	private int type;
	private PopListener listener;

	private static ModeTag certainModeTag = new ModeTag() {
		
		@Override
		public Class getmoveclass() {
			// TODO Auto-generated method stub
			return ADT_Srch_Detail.class;
		}
		
	};
	
	public PopupPOI(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public PopupPOI(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	public PopupPOI(Context context) {
		super(context);
		initialize();
	}
	
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.mapcomponent_popup_poi, this);
	    findViews();
	    setVisibility(View.GONE);
	    popupView.setVisibility(View.INVISIBLE);
	    nail.setVisibility(View.INVISIBLE);
	    setClickable(false);
	    setFocusable(false);
//	    setBackgroundColor(Color.BLACK);
	    setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	public void setPopListener(PopListener listener) {
		this.listener = listener;
	}
	
	public void setLonlat(long lon, long lat){
		centerLonlat = UIMapControlJNI.ConvertLonLatToDispPoint(UIMapControlJNI.GetCenterLonLat()[0], UIMapControlJNI.GetCenterLonLat()[1]);
		lonlat = new long[]{lon, lat};
		setRouteListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = textview.getText().toString();
				if(name.equals(getContext().getString(R.string.STR_COM_018))){
					name = getResources().getString(R.string.STR_MM_02_02_04_15);
				}
				if(RouteCalcController.instance().getRoutePointFindPurpose() == RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
					RouteCalcController.instance().rapidRouteCalculateWithData(name, lonlat[0], lonlat[1]);
				} else {
					RouteCalcController.instance().pointDataToPOI(name, lonlat);
					MenuControlIF.Instance().BackSearchWinChange(ADT_Route_Main.class);
				}
			}
		});
	}

	private void setBtnImage() {
		if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != RouteCalcController.instance().getRoutePointFindPurpose()){
			switch (RouteCalcController.instance().getRoutePointFindPurpose()) {
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START:
				imagebutton01.setImageResource(R.drawable.navicloud_and_570a_02);
				break;
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
				imagebutton01.setImageResource(R.drawable.navicloud_and_570a_01);
				break;
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
				imagebutton01.setImageResource(R.drawable.navicloud_and_570a_03);
				break;
			default:
				break;
			}
		}
		else{
			imagebutton01.setImageResource(R.drawable.navicloud_and_569a_01);
		}
	}
	
	public int getPopupType(){
		return isShowing() ? type : -1;
	}
	
	public final void refreshLoaction() {
		if(isShowing()) {
			float[] point = UIMapControlJNI.ConvertLonLatToDispPoint((int)lonlat[0], (int)lonlat[1]);
			setPosition((int)point[0], (int)point[1]);
			requestLayout();
		}
	}
	
	private final void setRouteListener(OnClickListener l) {
		imagebutton01.setOnClickListener(l);
	}

	public final void setPointName(String pointName){
		
		if(TextUtils.isEmpty(pointName)) {
			textview.setText(R.string.STR_MM_02_02_04_15);
		} else {
			textview.setText(pointName);
		}
	}
	
	public final void setGoDetailInfo(SearchPointBean bean,boolean issrch) {
		int index = bean.getIndex();
		int searchkind = bean.getSearchkind();
//		int record = jniPointControl_new.Instance().GetRecordIDbyNameAndLonLat(bean.getName(), (int)bean.getLonlat()[0], (int)bean.getLonlat()[1]);
//		textview.setOnClickListener(new DetailListener(index, searchkind,record,issrch));
		BundleNavi.getInstance().put("SearchPointBean", bean);
		Log.i("icon", "name "+bean.getName());
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(certainModeTag.getmoveclass());
			}
		});
	}
	
	public final void setGoDetailInfo(PointBean pointBean,boolean issrch){
		int index = pointBean.getIndex();
		int searchkind = pointBean.getType();
//		int record = jniPointControl_new.Instance().GetRecordIDbyNameAndLonLat(pointBean.getName(), (int)pointBean.getLonlat()[0], (int)pointBean.getLonlat()[1]);
//		textview.setOnClickListener(new DetailListener(index, searchkind,record,issrch));
	}
	private void setPopupInfo(SearchPointBean bean) {
		type = bean.getSearchkind();
		setPointName(bean.getName());
		setLonlat(bean.getLongitude(), bean.getLatitude());
		setGoDetailInfo(bean,true);
	}
	
	private void setPopupInfo(UIPointData pointData) {
		type = 0;
		setPointName(pointData.getName());
		setLonlat(pointData.getLonlat()[0], pointData.getLonlat()[1]);
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().BackSearchWinChange(ADT_Favorite_Detail.class);
				
			}
		});
	}
	
	public final void dismissPopupWithoutRemovePointListener(){
		setVisibility(View.GONE);
		popupView.setVisibility(View.INVISIBLE);
		nail.setVisibility(View.INVISIBLE);
		setClickable(false);
		clearPopupInfoWithoutRemovePointListener();
		if(listener != null) {
			listener.onDismiss();
		}
	}
	
	public final void clearPopupInfoWithoutRemovePointListener() {
		textview.setOnClickListener(null);
		setRouteListener(null);
		textview.setText("");
		lonlat = null;
	}
	
	private void setPopupInfo(final PointBean pointBean) {
		type = pointBean.getType();
		setPointName(pointBean.getName());
		setLonlat(pointBean.getLonlat()[0], pointBean.getLonlat()[1]);
		setGoDetailInfo(pointBean,false);
		BundleNavi.getInstance().put("SearchPointBean", SearchPointBean.obtainPointBeanToSearchBean(pointBean));
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Detail.class);
				
			}
		});
	}
	   
	private void findViews() {
		imagebutton01 = (ImageView) findViewById(R.id.mapcomponent_Popup_Poi_imagebutton01);
	    textview = (TextView) findViewById(R.id.mapcomponent_Popup_Poi_textview01);
	    nail = (ImageView)findViewById(R.id.mapcomponent_Popup_Poi_nail);
	    popupView = (LinearLayout)findViewById(R.id.relativeLayout1);
	}
	
	public final void addToViewGroup(RelativeLayout vg) {
		vg.addView(this);
	}
	
	
	public void setNailImage(int sourceId){
		nail.setImageResource(sourceId);
	}
	
	private final void setPosition(int x, int y){
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		
		
		LayoutParams l = (LayoutParams) getLayoutParams();
		l.topMargin = y - DensityUtil.dp2px(getContext(), POI_HEIGHT_DIP);
		l.leftMargin = x - DensityUtil.dp2px(getContext(), POI_WIDTH_DIP/2);
		l.rightMargin = screenWidth - x - DensityUtil.dp2px(getContext(), POI_WIDTH_DIP);
		l.bottomMargin = ScreenMeasure.getHeight() - ScreenMeasure.getHightOfStatusbar() - y;
    }
	
	
	public final void showPopup(boolean needShowAnimation) {
		setVisibility(View.VISIBLE);
		refreshLoaction();
		if(!needShowAnimation){
			return;
		}
		setNeedKeepPopupInScreen(true);
		popupView.setVisibility(View.INVISIBLE);
		nail.setVisibility(View.VISIBLE);
		Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.nail_drop);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				popupView.setVisibility(View.VISIBLE);
				PopupPOI.this.setClickable(true);
				adjustPosition();
			}
		});
		nail.setAnimation(anim);
		anim.start();
		if(listener != null) {
			listener.onShow();
		}
	}
	private void adjustPosition(){
		if(!keepPopupInScreen() || !isShowing()){
			return;
		}
		post(new Runnable() {
			
			@Override
			public void run() {
				boolean needAdjust = false;
				float currentScreenCenter[] = UIMapControlJNI.ConvertLonLatToDispPoint(UIMapControlJNI.GetCenterLonLat()[0], UIMapControlJNI.GetCenterLonLat()[1]);
				if((getLeft()+popupView.getLeft())<0){
					needAdjust = true;
					currentScreenCenter[0] -= (0 - (getLeft()+popupView.getLeft()));
				}else if(ScreenMeasure.getWidth() - getRight() + (getWidth() - popupView.getRight()) < 0){
					needAdjust = true;
					currentScreenCenter[0] -= (ScreenMeasure.getWidth() - getRight() + (getWidth() - popupView.getRight()));
				}
				if(getTop() - DensityUtil.dp2px(getContext(), 87)<0){
					needAdjust = true;
					currentScreenCenter[1] +=  ((getTop() - DensityUtil.dp2px(getContext(), 87)));
				}else if((ScreenMeasure.getHeight() - DensityUtil.dp2px(getContext(), 70) - getBottom()) < 0){
					needAdjust = true;
					currentScreenCenter[1] -= (ScreenMeasure.getHeight() - DensityUtil.dp2px(getContext(), 70) - getBottom());
				}
				if(needAdjust){
					int[] CenterLonLat = UIMapControlJNI.ConvertDispPointToLonLat(currentScreenCenter[0], currentScreenCenter[1]);
					if(NSViewManager.GetViewManager().getCurrentActivity() instanceof ADT_Main_Map_Navigation){
						UIMapControlJNI.SetCarPositonMode(false);
						((ADT_Main_Map_Navigation)NSViewManager.GetViewManager().getCurrentActivity()).showMain();
					}
//					UIMapControlJNI.AutoHeightMove(CenterLonLat[0], CenterLonLat[1]);
					//temp use current interface
					UIMapControlJNI.SetCenterInfo(CenterLonLat[0], CenterLonLat[1], UIMapControlJNI.GetHeight());
					
					centerLonlat[0] = CenterLonLat[0];
					centerLonlat[1] = CenterLonLat[1];
				}
				PopupPOI.this.requestLayout();
			}
		});
	}

	private boolean keepPopupInScreen() {
		return needKeepPopupInScreen;
	}

	public final void showPopup(SearchPointBean bean) {
		clearPopupInfo();
		setPopupInfo(bean);
		setBtnImage();
		showPopup(true);
		
		if(listener != null) {
			listener.onShow();
		}
	}
	
//	public final void showPopup(jniUIC_PNT_PointData_new pointData) {
//		clearPopupInfo();
//		setPopupInfo(pointData);
//		setBtnImage();
//		showPopup(true);
//		
//		if(listener != null) {
//			listener.onShow();
//		}
//	}
	
	public final void showPopup(UIPointData pointData) {
		clearPopupInfo();
		setPopupInfo(pointData);
		setBtnImage();
		showPopup(true);
		
		if(listener != null) {
			listener.onShow();
		}
	}
	
	public final void showPopupWithoutAnim(SearchPointBean bean){
		clearPopupInfo();
		setPopupInfo(bean);
		showPopup(false);
		adjustPosition();
		if(listener != null) {
			listener.onShow();
		}
	}
	
	public final void showPopup(PointBean bean) {
		clearPopupInfo();
		setPopupInfo(bean);
		setBtnImage();
		showPopup(true);
		if(bean.getType() == TouchMapControl.TYPE_LONGPRESS) {
			BundleNavi.getInstance().put("pointBean", bean);
		}
		
		if(listener != null) {
			listener.onShow();
		}
		if(UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_TRAFFIC == bean.getType()){
			setClickable(false);
		}else{
			setClickable(true);
		}
	}
	
	public final void dismissPopup(){
		setVisibility(View.GONE);
		popupView.setVisibility(View.INVISIBLE);
		nail.setVisibility(View.INVISIBLE);
		setClickable(false);
		clearPopupInfo();
		if(listener != null) {
			listener.onDismiss();
		}
	}
	
	public final void clearPopupInfo() {
		PointTools.getInstance().removePointListener();
		textview.setOnClickListener(null);
		setRouteListener(null);
		textview.setText("");
		lonlat = null;
	}
	
	public final boolean isShowing() {
		return getVisibility() == VISIBLE;
	}
	
//	private class DetailListener implements OnClickListener {
//		private final int index;
//		private final int type;
//		private boolean issrch;
//		private int record = -1;
//		//touchorsearch true is touch,false is search.
//		private DetailListener(int index, int type, int record,boolean issrch) {
//			this.index = index;
//			this.type = type;
//			this.record = record;
//			this.issrch = issrch;
//		}
//		@Override
//		public void onClick(View v) {
//			String name = textview.getText().toString();
//			if(!name.equals(getContext().getString(R.string.STR_COM_018))){
//					BundleNavi.getInstance().putInt("index", index);
//				if(issrch){
//					BundleNavi.getInstance().putBoolean("TouchFlag", true);
//					if(type == TouchMapControl.TYPE_LONGPRESS || type == UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT) {
//					    BundleNavi.getInstance().putInt("searchKind", UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT);
//					} else {
//					    BundleNavi.getInstance().putInt("searchKind", type);
//
//					}
//					disposeToSrchDetail();
//				}
//				else{
//					if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_USER_REPORT) {
//						MenuControlIF.Instance().ForwardWinChange(ADT_report_detail.class);
//						return;
//					}
//					if(type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_TRAFFIC){
//						return;
//					}
//					if (type == UISearchControlJNI.UIC_SCM_TOUCHED_MARK_TYPE_POINT) {
//						BundleNavi.getInstance().putBoolean("TouchFlag", true);
//						BundleNavi.getInstance().putInt("record", record);
//						disposeToFavoriteDetail();
//					} else {
//						BundleNavi.getInstance().putBoolean("TouchFlag", true);
//						if(type == TouchMapControl.TYPE_LONGPRESS || type == UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT) {
//						    BundleNavi.getInstance().putInt("searchKind", UISearchControlJNI.UIC_SCM_SRCH_TYPE_PIN_POINT);
//						} else {
//						    BundleNavi.getInstance().putInt("searchKind", TouchMapControl.getSearchTypeFromTouchType(type));
//
//						}
//						disposeToSrchDetail();
//					}
//				}
//				
//			}
//		}
//		
//	}
	
	private void disposeToSrchDetail() {
		if(RouteCalcController.instance().getRoutePointFindPurpose() != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
			if(ADT_Srch_Result.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())
			|| 	ADT_Favorite_CheakMap.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())){
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Srch_Detail.class);
			}
			else{
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Detail.class);
			}
		}
		else{
			if (!MenuControlIF.Instance().BackSearchWinChange(ADT_Srch_Detail.class)) {
				if(ADT_Srch_Result.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())
				|| 	ADT_Favorite_CheakMap.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())){
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Srch_Detail.class);
				}
				else{
					MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Detail.class);
				}
			}
		}
	}
	private void disposeToFavoriteDetail() {
		if(RouteCalcController.instance().getRoutePointFindPurpose() != RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE){
			if(ADT_Srch_Result.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())
			|| 	ADT_Favorite_CheakMap.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())){
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Favorite_Detail.class);
			}
			else{
				MenuControlIF.Instance().ForwardWinChange(ADT_Favorite_Detail.class);
			}
		}
		else{
			if (!MenuControlIF.Instance().BackSearchWinChange(ADT_Favorite_Detail.class)) {
				if(ADT_Srch_Result.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())
				|| 	ADT_Favorite_CheakMap.class.equals(MenuControlIF.Instance().GetCurrentWinscapeClass())){
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Favorite_Detail.class);
				}
				else{
					MenuControlIF.Instance().ForwardWinChange(ADT_Favorite_Detail.class);
				}
			}
		}
	}
	
	public void setNeedKeepPopupInScreen(boolean value){
		this.needKeepPopupInScreen = value;
	}
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	public interface PopListener{
		void onShow();
		void onDismiss();
	}
	
	public interface ModeTag{
		public Class getmoveclass();
	}
	
	public static void setModeTag(ModeTag tag) {
		if(null == tag) {
			return;
		}
		
		certainModeTag = tag;
	}
	
}
