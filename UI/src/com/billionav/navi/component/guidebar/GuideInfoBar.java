package com.billionav.navi.component.guidebar;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.base.GuideInfoController;
import com.billionav.navi.component.guidebar.base.GuideInfoController.DataChangeListener;
import com.billionav.navi.component.mapcomponent.AnotherRoad;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.uitools.UILog;
import com.billionav.ui.R;

public class GuideInfoBar extends RelativeLayout implements DataChangeListener{
	private GIB_TurningInfo turningInfoView;
	private GIB_StreetInfo 	streetInfoView;
	private GIB_RouteDetail	routeDetailView;
	private GIB_LaneInfo	laneInfoView;
	private GIB_LimitIcon		limitIcon;
	private AnotherRoad 	anotherRoad;
	private ImageView		illustIcon;
	private IllustComponent	illustComponent;
	private boolean 		isIllustAutoShowing;
	private boolean			isGuideRefreshPaused;
	private GuideInfoController guideInfoController;
	private boolean illustEnable;
	
	public interface IllustStatusListener{
		public void onIllustViewStatusChange(boolean isIllustShowing);
	}
	
	private IllustStatusListener illustListener = null;
	public void setIllustStatusListener(IllustStatusListener l){
		illustListener = l;
	}
	
	public GuideInfoBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialize();
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
//		resizeLimitIcon();
		resizeLayout();
	}
	
	private void resizeLimitIcon() {
		RelativeLayout.LayoutParams lp = (LayoutParams) limitIcon.getLayoutParams();
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			lp.topMargin = (int) (50 * getResources().getDisplayMetrics().density);
		}else{
			lp.topMargin =   (int) (100 * getResources().getDisplayMetrics().density);
		}

		limitIcon.setLayoutParams(lp);
		
	}

	private void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.guidebar_base_container, this);
	    
	    findViews();
	    illustEnable = true;
		guideInfoController = GuideInfoController.createInstance();
		guideInfoController.addDataChangeListener(turningInfoView);
		guideInfoController.addDataChangeListener(streetInfoView);
		guideInfoController.addDataChangeListener(routeDetailView);
		guideInfoController.addDataChangeListener(laneInfoView);
		
		routeDetailView.setGuideInfoController(guideInfoController);
		
	    setListeners();
	    
	    if(!isInEditMode()) {
//		    initGuideInfo();
//		    updateTrafficLightButton();
//		    refreshSilenceButton();
	    }
	}
	
	private void findViews(){
		turningInfoView = (GIB_TurningInfo) findViewById(R.id.turninginfo);
		streetInfoView = (GIB_StreetInfo) findViewById(R.id.streetinfo);
		routeDetailView = (GIB_RouteDetail) findViewById(R.id.routedetail);
		laneInfoView = (GIB_LaneInfo) findViewById(R.id.laneinfo);
		limitIcon = (GIB_LimitIcon) findViewById(R.id.gib_main_limit);
		illustIcon = (ImageView) findViewById(R.id.gib_main_illust);
		illustComponent = (IllustComponent) findViewById(R.id.gib_main_illust_component);
		anotherRoad = (AnotherRoad)findViewById(R.id.map_button_another_road);
		turningInfoView.setTag(GuideInfoController.GIB_TURNING_INFO);
		streetInfoView.setTag(GuideInfoController.GIB_STREET_INFO);
		routeDetailView.setTag(GuideInfoController.GIB_ROUTE_INFO);
		laneInfoView.setTag(GuideInfoController.GIB_LANE_INFO);
//		resizeLimitIcon();
		
		//adjust illust position for cn version
	    if(SystemTools.isCH()){
	    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)illustComponent.getLayoutParams();
	    	params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
	    	params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
	    	illustComponent.setLayoutParams(params);
	    }
//		silenceButton = (ImageView) findViewById(R.id.gib_main_silence);
//		trafficLightButton = (ImageView) findViewById(R.id.gib_main_traffic_light);
//		trafficLightButton.setImageDrawable(ViewHelp.createDrawableListByImageID(getContext(), R.drawable.jaguar_and_031a,  R.drawable.jaguar_and_032a));
		
//		silenceButton.setVisibility(View.INVISIBLE);
	}
	
	public void nodifyDemoState(boolean isDemo) {
		anotherRoad.nodifyDemoState(isDemo);
	}
	private void setListeners(){
		turningInfoView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(illustIcon.getVisibility() == View.VISIBLE){
					//change to illust view
					showIllustView();
				}
				guideInfoController.playNextTurningGuideVoice();
				
			}
		});
		illustComponent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(illustComponent.isIllustShowing()){
					closeIllustView();
				}
				
			}
		});
	}
	
	private boolean showIllustView(){
		if(illustEnable){
			Log.d("test","show Illust getPic"+System.currentTimeMillis());
			boolean isImageReady = illustComponent.prepareIllustImage();
			boolean isArrowReady = illustComponent.prepareIllustArrowImage();
			Log.d("test","show Illust getPicend"+System.currentTimeMillis());
			if(isImageReady || isArrowReady){
				illustComponent.showIllust();
				laneInfoView.notifyIllustShown(true);
				if(illustListener != null){
					illustListener.onIllustViewStatusChange(true);
				}
				isIllustAutoShowing = true;
				return true;
			}else{
				illustIcon.setVisibility(View.GONE);
				turningInfoView.illustShow(false);
				laneInfoView.notifyIllustShown(false);
				return false;
			}
		}
		return false;
		
	}
	
	private void closeIllust(){
		if(isIllustAutoShowing){
			Log.d("test","close Illust");
			UILog.d("close illust2");
			illustComponent.hideIllust();
			illustIcon.setVisibility(View.GONE);
			turningInfoView.illustShow(false);
			laneInfoView.notifyIllustShown(false);
			if(illustListener != null){
				illustListener.onIllustViewStatusChange(false);
			}
			isIllustAutoShowing = false;
			refreshIllustIcon();
		}
	}
	
	public void refreshIllustIcon(){
		
		if(guideInfoController.isIllustExist()){
			if(isIllustViewShowing()){
				illustIcon.setVisibility(View.GONE);
			}else{
				illustIcon.setVisibility(View.VISIBLE);
			}
			turningInfoView.illustShow(true);
		}else{
			illustIcon.setVisibility(View.GONE);
			turningInfoView.illustShow(false);
			closeIllust();
		}
	}
	
	/**
	 * update limitIcon from data
	 */
	private void updateLimitIcon(){
//		jniOrbisControl.Instance().PrepareObrisData();
//		if(jniOrbisControl.Instance().IsOrbisFind() 
//				&& (UIMapControlJNI.GetHeight() > jniSetupControl.get(jniSetupControl.STUPDM_SPEED_CAMERA_SCALE)))
//		{
//			long type = jniOrbisControl.Instance().GetOrbisType();
//			long limit = jniOrbisControl.Instance().GetOrbisSpeedLimit();
//			refreshLimitIcon(type, limit);
//		}else{
			hideLimitIcon();
//		}
	}
	
	private void hideLimitIcon(){
		limitIcon.setVisibility(View.GONE);
	}
	
	private void refreshLimitIcon(long type, long limit){
		if(type != 1 && type != 4 && type != 10){
			limitIcon.setShowStyle(GIB_LimitIcon.LIMIT_SHOW_STYLE_CAMERA);
		}else{
			limitIcon.setLimitSpeed((int)limit);
			limitIcon.setShowStyle(GIB_LimitIcon.LIMIT_SHOW_STYLE_LIMIT_INFO);
		}
		//adjust limit icon position for ch version
		if(SystemTools.isCH()){
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)limitIcon.getLayoutParams();
			if(ScreenMeasure.isPortrait()){
				param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
				param.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
				param.addRule(RelativeLayout.ALIGN_RIGHT, 0);
			}
			else{
				if(isIllustAutoShowing){
					param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
					param.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
					param.addRule(RelativeLayout.ALIGN_RIGHT, R.id.gib_main_illust_component);
				}
				else{
					param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
					param.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
					param.addRule(RelativeLayout.ALIGN_RIGHT, 0);
				}
			}
			limitIcon.setLayoutParams(param);
		}
	}
	
	/**
	 * Must called after findViewByID() or in onResume().
	 * This method will sync guide info to views.
	 */
	public void initGuideInfo(){
		guideInfoController.syncAllGuideInfo();
		refreshIllustIcon();
		updateLimitIcon();
		
		isGuideRefreshPaused = false;
		if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()){
			show();
		}else{
			hide();
		}
		resizeLayout();
		anotherRoad.onResume();
	}
	

	public void hide(){
		setVisibility(View.GONE);
	}
	public void show(){
		setVisibility(View.VISIBLE);
	}
	/**
	 * Receive GuideInfoChange and magView trigger.
	 * 
	 * @param info
	 * @return
	 */
	public boolean notifyTrigger(NSTriggerInfo info){
		if(anotherRoad.notifyTrigger(info)){
			return true;
		}
		if(isGuideRefreshPaused){
			return false;
		}
		
		if(info.m_iTriggerID == NSTriggerID.UIC_MN_TRG_DEMO_START || info.m_iTriggerID == NSTriggerID.UIC_MN_TRG_DEMO_STOP) {
		}
		
		if(info.m_iTriggerID == NSTriggerID.UIC_MN_TRG_MAGVIEW_SHOW){
			
			if(info.m_lParam1 == 0){
				closeIllust();
				isIllustAutoShowing = false;
				return false;
			}
			
//	 		if(info.m_lParam2 == UIGuideControlJNI.DPGUDEF_CMN_ILLUST_MK_AR){
				
//			} else if ((info.m_lParam2 >= UIGuideControlJNI.DPGUDEF_CMN_ILLUST_MK_NORMAL_BRANCH
//					&& info.m_lParam2 <= UIGuideControlJNI.DPGUDEF_CMN_ILLUST_MK_EXIT_BRANCH))
//			{
				isIllustAutoShowing = showIllustView();
//			}else{
//				if(!showIllustView()){
//					closeIllust();
//					isIllustAutoShowing = false;
//				}
//			}
			return false;
		}else  if(info.m_iTriggerID == NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE
				||info.m_iTriggerID == NSTriggerID.UIC_MN_TRG_DEMO_STOP)
		{
			updateLimitIcon();
			refreshIllustIcon();
			boolean returnValue = guideInfoController.notifyTrigger(info);
			if(illustComponent.isIllustShowing()){
				illustComponent.notifyTrigger();
			}
			return returnValue;
		}else{
			return false;
		}

	}
	
	/**
	 * 
	 * Need be called when you leave the guide info screen
	 */
	public void setGuideInfoPaused(){
		isGuideRefreshPaused = true;
	}
	
	
	/**
	 * show you whether the illustration is showing
	 * @return true if the illustration is showing.
	 */
	public boolean isIllustViewShowing(){
		return illustComponent.isIllustShowing();
	}
	
	/**
	 * 
	 * close the illustration manually
	 */
	public void closeIllustView(){
		closeIllust();
	}
	
	
	/**
	 * This is a call back method, can't called as a interface
	 */
	@Override
	public void onDataChanged() {
		refreshIllustIcon();
	}
	private boolean isPort = !ScreenMeasure.isPortrait();
	public void resizeLayout(){
		if(isPort != ScreenMeasure.isPortrait()){
			isPort = ScreenMeasure.isPortrait();
			findViewById(R.id.guideinfodisplayarea).getLayoutParams().height = DensityUtil.dp2px(getContext(), isPort?87:56);
			turningInfoView.resizeLayout();
			streetInfoView.resizeLayout();
			routeDetailView.resizeLayout();
		}
	}
	/**
	 * This is a call back method, can't called as a interface
	 */
	@Override
	public Object getTag() {
		// TODO Auto-generated method stub
		return GuideInfoController.GIB_INFO_ON_MAP;
	}

	public void banIllust(){
		illustEnable = false;
		if(illustComponent.isShown()){
			closeIllust();
		}
	}
	public void enableIllust(){
		illustEnable = true;
//		showIllustView();
	}

	public void showAnotherRoadButton() {
//		anotherRoad.setVisibility(View.GONE);
		
	}
	public void hideAnotherRoadButton(){
//		anotherRoad.setVisibility(View.GONE);
	}
	public void updateMapViewForIllust(){
		illustComponent.updateMapView();
	}

	@Override
	public void onRoutePointKindChanged() {
		// TODO Auto-generated method stub
		
	}
	
	public void destory() {
		guideInfoController.removeAllDataChangeListener();
	}
}

