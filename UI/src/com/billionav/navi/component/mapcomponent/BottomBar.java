package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIGuideControlJNI;
import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.navi.component.guidebar.GIB_GpsStatus;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.naviscreen.map.ADT_AR_Main;
import com.billionav.navi.naviscreen.map.ADT_AR_Main_Tip;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.misc.ADT_Voice_Recognition;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;
import com.billionav.voicerecogJP.VRJPManager;

public class BottomBar extends RelativeLayout implements TriggerListener{
	private TextView		currentRoad;
	private ImageView 		voiceRecognition;
	private ImageView 		ar;
	private GIB_GpsStatus	gps_status;
	private HorizontalScrollView hScrollView;
	private boolean isAutoScroll;
	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.bottom_blue_bar, this);
		init();
		setClickable(true);
		GlobalTrigger.getInstance().addTriggerListener(this);
		post(new Runnable() {
			
			@Override
			public void run() {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)getLayoutParams();
				if(ScreenMeasure.isPortrait()){
					lp.width = ScreenMeasure.getWidth();
				}else{
					lp.width = ScreenMeasure.getHeight();
				}
				requestLayout();
				
			}
		});
		
	}

	private void init() {
		
		findView();
	}
	public void resume(){
		gps_status.onDataChanged();
	}
	private void findView() {
		gps_status = (GIB_GpsStatus)findViewById(R.id.guidebar_gps_state);
		currentRoad = (TextView) findViewById(R.id.current_road);
		voiceRecognition = (ImageView) findViewById(R.id.guidebar_layer_voice);
		ar = (ImageView) findViewById(R.id.guidebar_layer_ar);
		updateCurrentRoadName();
		refreshARAndVoice();
		setListener();
	}
	public void refreshARAndVoice(){
		boolean isDemo = RouteTool.isDemo();
		int visiblity = View.INVISIBLE;//isDemo?View.INVISIBLE:View.VISIBLE;
		ar.setVisibility(visiblity);
		voiceRecognition.setVisibility(visiblity);
		ar.setEnabled(!isDemo);
		voiceRecognition.setEnabled(!isDemo);
	}
	
	private final Handler mHandler = new Handler();
	private Runnable ScrollRunnable = new Runnable() {
		@Override
		public void run() {
			int off = currentRoad.getMeasuredWidth() - hScrollView.getWidth();// �жϸ߶�
			if (off > 0) {
				hScrollView.scrollBy(4, 0);

				if (hScrollView.getScrollX() == off) {
					Thread.currentThread().interrupt();
					if(isAutoScroll){
						resetTextStatus();
					}
						
				} else {
					mHandler.postDelayed(this, 50);
				}
			}
		}
	};
	
	private void resetTextStatus(){
		isAutoScroll = false;
		hScrollView.scrollTo(0, 0);
	}
	private void startAutoScroll(){
		mHandler.removeCallbacks(ScrollRunnable);
		isAutoScroll = true;
		mHandler.postDelayed(ScrollRunnable, 200);
	}
	private void setListener() {
		hScrollView = (HorizontalScrollView) currentRoad.getParent();
		currentRoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(hScrollView.getScrollX()==0){
					mHandler.post(ScrollRunnable);
				} else {
					mHandler.removeCallbacks(ScrollRunnable);
					resetTextStatus();
				}
			}
		});
		if(SystemTools.isCH()){
		ar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
//						DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
//						MenuControlIF.Instance().ForwardWinChange(ADT_AR_Main.class);
				if(SharedPreferenceData.IS_NEED_AR_TIP.getBoolean()){
					MenuControlIF.Instance().ForwardWinChange(ADT_AR_Main_Tip.class);
				}else{
					ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_ALPHA_TRANSPARANT);
					MenuControlIF.Instance().ForwardWinChange(ADT_AR_Main.class);
				}
			}
		});
		}else{
//			ar.setBackgroundDrawable(null);
//			ar.setImageBitmap(null);
			ar.setVisibility(View.INVISIBLE);
			ar.setOnClickListener(null);
		}
		voiceRecognition.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(VRJPManager.isDocomoDemo){
					VRJPManager.Instance().startService(NaviViewManager.GetViewManager());
				}else{
//					if(VoiceRecognizer.instance().enterVrMode()) {
//					}
					MenuControlIF.Instance().ForwardWinChange(ADT_Voice_Recognition.class);
					
				}
			}
		});
	}
	private void updateCurrentRoadName(){
		String streetName = getStreetName();
		if(!currentRoad.getText().toString().equals(streetName)){
			currentRoad.setText(streetName);
			if(null != hScrollView && hScrollView.getScrollX() > 0){
				resetTextStatus();			
			}
			startAutoScroll();
		}
	}
	private String getStreetName() {
		UIMapControlJNI.requestAreaName(UILocationControlJNI.GetPosition(0)[0], UILocationControlJNI.GetPosition(0)[1]);
		String name = UIGuideControlJNI.getInstance().GetCurrentRoadData_StrStreetName();
		
		if (TextUtils.isEmpty(name)) {
			name = UIMapControlJNI.getAreaName();
			if(name != null && !"".equals(name)){
				String names[] = name.split(" ");
				name = getAreaName(names);
			}
			
		}
		
		return name;
	}
	
	private String getAreaName(String[] names){
		if(null != names && names.length > 0){
			if(names.length == 1){
				return names[0];
			}else if(names.length == 2){
				return names[0] + names[1];
			}else {
				return names[names.length - 2] + names[names.length - 1];
			}
		}else{
			return "";
		}
	}
	
	public boolean setARonClickListener(OnClickListener l){
		if (null == ar){
			return false;
		}
		ar.setImageResource(R.drawable.btn_ar_map);
		ar.setOnClickListener(l);
		return true;
	}
	public void setVoiceRecognitionButtonListener(OnClickListener l){
		voiceRecognition.setOnClickListener(l);
	}
	public void destory(){
		gps_status.destory();
		GlobalTrigger.getInstance().removeTriggerListener(this);
	}

	@Override
	public boolean onTrigger(NSTriggerInfo triggerInfo) {
		if(NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE == triggerInfo.GetTriggerID()){
			updateCurrentRoadName();
		}
		return false;
	}


}
