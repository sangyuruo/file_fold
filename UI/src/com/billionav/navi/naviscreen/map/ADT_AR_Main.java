package com.billionav.navi.naviscreen.map;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.DRIR.Utils.BatteryWatcher;
import com.billionav.DRIR.jni.jniDRIR_DRVEventMsgBox;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.navi.camera.CameraView;
import com.billionav.navi.component.bottombar.MenuBar;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.guidebar.GuideInfoBar;
import com.billionav.navi.component.mapcomponent.ARGLSurfaceView;
import com.billionav.navi.component.mapcomponent.BottomBar;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.dest.ADT_Top_Menu;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.misc.ADT_Voice_Recognition;
import com.billionav.navi.naviscreen.report.ADT_report_main;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;
//import com.billionav.voicerecog.VoiceRecognizer;

public class ADT_AR_Main extends ActivityBase {

	private GuideInfoBar guideBar;
	private BottomBar bottomArea;

    private ARGLSurfaceView surfaceView;
    private RelativeLayout arSetting;
    private RelativeLayout arSetWarningSensitivity;
    private RelativeLayout arSetAnglecorrection;
    private RelativeLayout arExperienceMode;
    private RelativeLayout arIntoTip;
    private CheckBox checkBox = null;
    private final static int tmpmessageType = 1;
    
    private MenuBar bottomBar;
    private RelativeLayout relayout_01;
    private GestureDetector gestureDetector;
    private boolean isAnglecorrection = false;
    private boolean isExperienceMode = false;
	private boolean animationLock = false;
    private TextView textView = null;
    private RelativeLayout warningSensitivity;
    private RelativeLayout warningSensitivityHigh;
    private RelativeLayout warningSensitivityMiddle;
    private RelativeLayout warningSensitivityLower;
    
    private RadioButton radioButtonHigh;
    private RadioButton radioButtonMiddle;
    private RadioButton radioButtonLower;
    
    private LinearLayout temp_layout01;
    private LinearLayout temp_layout02;
    private TextView lable;
    private boolean isNeed = true;
    private boolean isFirst = true;
    private Handler temHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(BatteryWatcher.CreateInstance(ADT_AR_Main.this).IsDeviceTempHigh()){
				UIVoiceControlJNI.getInstance().playGuide(0x1CF0110A);
				CustomToast.showToast(ADT_AR_Main.this, R.string.MSG_COM_00_00_02, 1000);
				removeSurfaceView();
				DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_AR_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
				if(MenuControlIF.Instance().SearchWinscape(ADT_Top_Menu.class)) {
					BackSearchWinChange(ADT_Top_Menu.class);
				}else{
					ForwardDefaultWinChange(ADT_Top_Menu.class);
				}
			}
		}
		
	};
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.scr_ar_main, false);
		findviewes();
		setInitCheckItem();
		gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				temp_layout01.getBackground().setAlpha(229);
				temp_layout02.getBackground().setAlpha(229);
				if(arSetting.getVisibility() == View.VISIBLE){
					closeSettingBoard();
					
					if(warningSensitivity.getVisibility() == View.VISIBLE){
						closeWarningSensitivityBoard();					
					}				
				}else{
					openSettingBoard();		
		
				}	
				return super.onSingleTapConfirmed(e);
			}

		});
		setOnClickEvent();
		guideBar.initGuideInfo();
    	guideBar.banIllust();
		if(getBundleNavi().getBoolean("AngleCorrection")){
			isAnglecorrection = true;
			checkBox.setChecked(true);
			jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_GSENSORADJUST, jniDRIR_MainControl.DRIR_SETTING_IR_GSENSORADJUST_ON);
		}else{
			checkBox.setChecked(false);
			jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_GSENSORADJUST, jniDRIR_MainControl.DRIR_SETTING_IR_GSENSORADJUST_OFF);
		}
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_DEMOMODE, jniDRIR_MainControl.DRIR_SETTING_IR_DEMOMODE_OFF);
		
		jniDRIR_MainControl.SettingCommit();
		//Whether the temperature too high?
		if(BatteryWatcher.CreateInstance(ADT_AR_Main.this).IsDeviceTempHigh()){
			UIVoiceControlJNI.getInstance().playGuide(0x1CF0110A);
			CustomToast.showToast(this, R.string.MSG_COM_00_00_01, 1000);
			temHandler.sendMessageDelayed(temHandler.obtainMessage(tmpmessageType), 10000);
		}
	}

	private void setInitCheckItem() {
		int value = SharedPreferenceData.DRIR_SETTING_IRID_ALTERLEVEL.getInt();
		if(value == 0){
			radioButtonHigh.setChecked(true);
			radioButtonLower.setChecked(false);
			radioButtonMiddle.setChecked(false);
		}else if(value == 1){
			radioButtonHigh.setChecked(false);
			radioButtonLower.setChecked(false);
			radioButtonMiddle.setChecked(true);
		}else if(value == 2){
			radioButtonHigh.setChecked(false);
			radioButtonLower.setChecked(true);
			radioButtonMiddle.setChecked(false);
		}
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_ALTERLEVEL, value);
		jniDRIR_MainControl.SettingCommit();
	
	}

	private void setOnClickEvent() {
		
	
		bottomBar.setReportClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeSurfaceView();
				isNeed = false;
				ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_AR_MODE, DRIRAppMain.DRIRAPP_CAMEEA_MODE);
				ForwardKeepDepthWinChange(ADT_report_main.class);
			}
		});
		arSetWarningSensitivity.setOnClickListener(new OnClickListener() {	
			
			@Override
			public void onClick(View v) {
				if (animationLock) {
					return;
				}
				if(warningSensitivity.getVisibility() !=View.VISIBLE){
					openWarningSensitivityBoard();
					temp_layout01.getBackground().setAlpha(204);
					temp_layout02.getBackground().setAlpha(229);
				}else{
					closeWarningSensitivityBoard();
					temp_layout01.getBackground().setAlpha(229);
					temp_layout02.getBackground().setAlpha(229);
				}
				
			}
		});
		arSetAnglecorrection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    isAnglecorrection = !isAnglecorrection;
			    if(isAnglecorrection){
			    	checkBox.setChecked(true);
			    	jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_GSENSORADJUST, jniDRIR_MainControl.DRIR_SETTING_IR_GSENSORADJUST_ON);
			    }else{
			    	checkBox.setChecked(false);
			    	jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_GSENSORADJUST, jniDRIR_MainControl.DRIR_SETTING_IR_GSENSORADJUST_OFF);
			    }
			    jniDRIR_MainControl.SettingCommit();
			  
			    if(warningSensitivity.getVisibility() == View.VISIBLE){
			    	 closeWarningSensitivityBoard();
			    }
			    closeSettingBoard();
			    temp_layout01.getBackground().setAlpha(229);
				temp_layout02.getBackground().setAlpha(229);
			   
			}
		});
		arExperienceMode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isExperienceMode = !isExperienceMode;
				
			    if(isExperienceMode){
			    	
			    	guideBar.setVisibility(View.GONE);
			    	bottomArea.setVisibility(View.GONE);
			    	textView.setText(R.string.STR_MM_04_02_01_04);
			    	jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_DEMOMODE, jniDRIR_MainControl.DRIR_SETTING_IR_DEMOMODE_ON);
			    }else{
			    	if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()){
						guideBar.setVisibility(View.VISIBLE);
					}else{
						guideBar.setVisibility(View.GONE);
					}
			    	bottomArea.setVisibility(View.VISIBLE);
			    	textView.setText(R.string.STR_MM_04_02_01_03);
			    	jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_DEMOMODE, jniDRIR_MainControl.DRIR_SETTING_IR_DEMOMODE_OFF);
			    }
			    jniDRIR_MainControl.SettingCommit();
			  
			    if(warningSensitivity.getVisibility() == View.VISIBLE){
			    	 closeWarningSensitivityBoard();
			    }
			    closeSettingBoard();
			    temp_layout01.getBackground().setAlpha(229);
			    temp_layout02.getBackground().setAlpha(229);
			   
			}
		});
		
		arIntoTip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeSurfaceView();
				DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_AR_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
				ForwardKeepDepthWinChange(ADT_AR_Main_Tip.class);
				
			}
		});
		warningSensitivityHigh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				radioButtonHigh.setChecked(true);
				radioButtonLower.setChecked(false);
				radioButtonMiddle.setChecked(false);
				closeWarningSensitivityBoard();
				closeSettingBoard();
				temp_layout01.getBackground().setAlpha(229);
				temp_layout02.getBackground().setAlpha(229);
				setWarningSensitivitySettingValue(jniDRIR_MainControl.DRIR_SETTING_IR_ALTERLEVEL_HIGH);
			}
		});
		
		warningSensitivityMiddle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				radioButtonHigh.setChecked(false);
				radioButtonLower.setChecked(false);
				radioButtonMiddle.setChecked(true);
				closeWarningSensitivityBoard();
				closeSettingBoard();
				temp_layout01.getBackground().setAlpha(229);
				temp_layout02.getBackground().setAlpha(229);
				setWarningSensitivitySettingValue(jniDRIR_MainControl.DRIR_SETTING_IR_ALTERLEVEL_MIDDLE);
			}
		});
		warningSensitivityLower.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				radioButtonHigh.setChecked(false);
				radioButtonLower.setChecked(true);
				radioButtonMiddle.setChecked(false);
				closeWarningSensitivityBoard();
				closeSettingBoard();
				temp_layout01.getBackground().setAlpha(229);
				temp_layout02.getBackground().setAlpha(229);
				setWarningSensitivitySettingValue(jniDRIR_MainControl.DRIR_SETTING_IR_ALTERLEVEL_LOW);
			}

		});
		
		bottomArea.setVoiceRecognitionButtonListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(VoiceRecognizer.instance().enterVrMode()){
//				}
				removeSurfaceView();
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_OUT_DELAYED);
				DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_AR_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
				ForwardKeepDepthWinChange(ADT_Voice_Recognition.class);
				return;
			}
		});
		bottomArea.setARonClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeSurfaceView();
				DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_AR_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_ALPHA_TRANSPARANT);
				if(MenuControlIF.Instance().SearchWinscape(ADT_Main_Map_Navigation.class)) {
					MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
				} else {
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Main_Map_Navigation.class);
				}
				
			}
		});
	}
	
	private void setWarningSensitivitySettingValue(int value){

		SharedPreferenceData.DRIR_SETTING_IRID_ALTERLEVEL.setValue(value);
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_ALTERLEVEL, value);
		jniDRIR_MainControl.SettingCommit();
	}
	private void openWarningSensitivityBoard() {
		warningSensitivity.setVisibility(View.VISIBLE);
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0); 
		Animation animation1 = new AlphaAnimation(0,1);	
		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);
		animation.setDuration(200);
		warningSensitivity.startAnimation(animation);
	}
	private void closeWarningSensitivityBoard() {
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0); 
		Animation animation1 = new AlphaAnimation(1,0);
		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				warningSensitivity.setVisibility(View.GONE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationStart(Animation animation) {
				
			}				
		});
		warningSensitivity.startAnimation(animation);
	}
	private void openSettingBoard() {
		arSetting.setVisibility(View.VISIBLE);
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0); 
		Animation animation1 = new AlphaAnimation(0,1);	
		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);
		animation.setDuration(200);
		if(!isExperienceMode){
		   openbottomBarBoard();
		}
		arSetting.startAnimation(animation);
	}

	private void closeSettingBoard() {
		animationLock = true;
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0);
		Animation animation1 = new AlphaAnimation(1,0);
		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				arSetting.setVisibility(View.GONE);//
				animationLock = false;
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationStart(Animation animation) {
				
			}				
		});
	    if(bottomBar.getVisibility()==View.VISIBLE){
	    	closeBottomBarBoard();
	    }
		arSetting.startAnimation(animation);
	}
	
	private void openbottomBarBoard() {
		bottomBar.setVisibility(View.VISIBLE);
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1, Animation.RELATIVE_TO_SELF,0); 
		Animation animation1 = new AlphaAnimation(0,1);	
		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);
		animation.setDuration(200);
		bottomBar.startAnimation(animation);
	}
	private void closeBottomBarBoard() {
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1);
		Animation animation1 = new AlphaAnimation(1,0);
		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				bottomBar.setVisibility(View.GONE);//
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationStart(Animation animation) {
				
			}				
		});
		bottomBar.startAnimation(animation);
	}
	private void findviewes(){

		guideBar = (GuideInfoBar)findViewById(R.id.guidebar);
		bottomArea = (BottomBar)findViewById(R.id.bottom_area);
		surfaceView = (ARGLSurfaceView) findViewById(R.id.surfaceview);
		arSetting = (RelativeLayout) findViewById(R.id.realative_01);
		arSetting.setVisibility(View.GONE);
		
		bottomBar = (MenuBar) findViewById(R.id.bottom_bar);
		bottomBar.setVisibility(View.GONE);
		relayout_01 = (RelativeLayout) findViewById(R.id.scr_ar_main);
	
		arSetWarningSensitivity = (RelativeLayout) findViewById(R.id.view_ar_setting_layout01);
		arSetAnglecorrection = (RelativeLayout) findViewById(R.id.view_ar_setting_layout02);
		arExperienceMode = (RelativeLayout) findViewById(R.id.view_ar_setting_layout03);
		textView = (TextView) findViewById(R.id.id_text);
		warningSensitivity = (RelativeLayout)findViewById(R.id.realative_02);
		warningSensitivity.setVisibility(View.GONE);
		checkBox = (CheckBox) findViewById(R.id.checkBox011);
		arIntoTip = (RelativeLayout) findViewById(R.id.view_ar_setting_layout04);
		warningSensitivityHigh = (RelativeLayout) findViewById(R.id.relayout_01);
		warningSensitivityMiddle = (RelativeLayout) findViewById(R.id.relayout_02);
		warningSensitivityLower = (RelativeLayout) findViewById(R.id.relayout_03);
		
		radioButtonHigh = (RadioButton) findViewById(R.id.adt_settings_ar_vedio_mode_high);
		radioButtonMiddle = (RadioButton) findViewById(R.id.adt_settings_ar_vedio_mode_std);
		radioButtonLower = (RadioButton) findViewById(R.id.adt_settings_ar_vedio_mode_long);
		

		lable = (TextView) findViewById(R.id.lable);
		
		
		temp_layout01 = (LinearLayout) findViewById(R.id.temp_layout_01);
		temp_layout02 = (LinearLayout) findViewById(R.id.temp_layout_02);
		
		if(SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_DRVEVT.getBoolean()){
			lable.setVisibility(View.VISIBLE);
		}else{
			lable.setVisibility(View.GONE);
		}
		
	}
	
	
    @Override
    public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
    	
    	guideBar.notifyTrigger(cTriggerInfo);
		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_DRIR_TEMPHIGH:
			UIVoiceControlJNI.getInstance().playGuide(0x1CF0110A);
			CustomToast.showToast(this, R.string.MSG_COM_00_00_01, 1000);
			temHandler.sendMessageDelayed(temHandler.obtainMessage(tmpmessageType), 10000);
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_TEMPNORMAL:
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_IRDRAW:
			if (surfaceView == null) { return true;
            }
			surfaceView.RequestDrawing();
			break;
		case NSTriggerID.UIC_MN_TRG_DRIR_DRVEVT:
			String strDrvEvt = "Type:";
			int type = Math.abs((int)cTriggerInfo.m_lParam1);
			boolean bStill = ( jniDRIR_DRVEventMsgBox.GetIsStill() == 1 );
			float fg1 = jniDRIR_DRVEventMsgBox.GetForwardG1();
			float fg2 = jniDRIR_DRVEventMsgBox.GetForwardG2();
			float cg1 = jniDRIR_DRVEventMsgBox.GetCrosswiseG1();
			float cg2 = jniDRIR_DRVEventMsgBox.GetCrosswiseG2();
			float hg = jniDRIR_DRVEventMsgBox.GetHoriG();
			double speed = jniDRIR_DRVEventMsgBox.GetSpeed() / 10.0;
			float dist = jniDRIR_DRVEventMsgBox.GetDistance();
			float fact = jniDRIR_DRVEventMsgBox.GetFact();
			switch(type){
			case 0:
				strDrvEvt += "N/A"; 
				break;
			case 1:
				strDrvEvt += "Speed UP";  	 
				break;
			case 2: 
				strDrvEvt += "Speed Down";				
				break;
			case 3:     
				strDrvEvt += "Turn";  				
				break; 
			case 4:
				strDrvEvt += " < 4m";
				break;
			case 5:
				strDrvEvt += " < 40m";
				break;
			case 6:
				strDrvEvt += "Front Car";
				break;
			}     
			strDrvEvt += " \nStill : " + bStill + " \nHori_G: " + hg + " \nCrosswiseG1: " + cg1 + " \nCrosswiseG2: " + cg2 + " \nForwardG1: " + fg1 + " \nForwardG2: " + fg2
					 + " \nSpeed: " + speed + "km/h \nDistance: " + dist + "m \nFact: " + fact;
			lable.setText(strDrvEvt);
			break;
		}
    	return super.OnTrigger(cTriggerInfo);
    }
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(arSetting.getVisibility() == View.VISIBLE){
				closeSettingBoard();
				if(warningSensitivity.getVisibility() == View.VISIBLE){
					closeWarningSensitivityBoard();					
				}
				temp_layout01.getBackground().setAlpha(229);
				temp_layout02.getBackground().setAlpha(229);
				return true;
			}
			if(isExperienceMode){
				if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()){
					guideBar.setVisibility(View.VISIBLE);
				}else{
					guideBar.setVisibility(View.GONE);
				}
				bottomArea.setVisibility(View.VISIBLE);
				textView.setText(R.string.STR_MM_04_02_01_03);
		    	jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_DEMOMODE, jniDRIR_MainControl.DRIR_SETTING_IR_DEMOMODE_OFF);
		    	jniDRIR_MainControl.SettingCommit();
		    	isExperienceMode = !isExperienceMode;
		    	return true;
			}
			if(isAnglecorrection){
				checkBox.setChecked(false);
				jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_GSENSORADJUST, jniDRIR_MainControl.DRIR_SETTING_IR_GSENSORADJUST_OFF);
				jniDRIR_MainControl.SettingCommit();
				isAnglecorrection = false;
				return true;
			}
			removeSurfaceView();
			DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_AR_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
			if(MenuControlIF.Instance().SearchWinscape(ADT_Top_Menu.class)) {
				BackSearchWinChange(ADT_Top_Menu.class);
			}else{
				ForwardDefaultWinChange(ADT_Top_Menu.class);
			}
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}

	private void removeSurfaceView() {
		if(surfaceView!=null){
			((ViewGroup)surfaceView.getParent()).removeView(surfaceView);
			surfaceView = null;
		}
	}
	
	
    @Override
    protected void OnResume() {
    	// TODO Auto-generated method stub
    	super.OnResume();
    	
    	RelativeLayout.LayoutParams l = (android.widget.RelativeLayout.LayoutParams) bottomArea.getLayoutParams();
    	RelativeLayout.LayoutParams l1 = (android.widget.RelativeLayout.LayoutParams) bottomBar.getLayoutParams();
		if(ScreenMeasure.isPortrait()){
			l.width = ScreenMeasure.getWidth();
			l1.width = ScreenMeasure.getWidth();
		}else{
			l.width = ScreenMeasure.getHeight();
			l1.width = ScreenMeasure.getHeight();
		}
		bottomBar.setEnableForAR();
    	new UIPathControlJNI().SetScreenAttribute(UIPathControlJNI.UIC_PT_FIND_OBJ_AUTOREROUTE, UIPathControlJNI.UIC_PT_SCREEN_ATTR_OK);
    	if(isFirst){
    		isFirst = false;
    	}else{
    		CameraView.Instance().Maximize();
    	}
    	UIMapControlJNI.SetScreenID(SCRMapID.SCR_ID_ArCamera);
		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		MapView.getInstance().onPause();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ForwardARscreenControl.getinstance().DRIRFunChange();
			}
		},1000);
    }
    
    	
    @Override
    public boolean OnTouchEvent(MotionEvent event) {
    	gestureDetector.onTouchEvent(event);
    	return true;
    }
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		 new UIPathControlJNI().SetScreenAttribute(UIPathControlJNI.UIC_PT_FIND_OBJ_AUTOREROUTE, UIPathControlJNI.UIC_PT_SCREEN_ATTR_NG);

		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		bottomBar.setImageAlphaForAR();
		if (surfaceView != null) {
			surfaceView.RequestDrawing();
		}
		
		
	}
	
	@Override
	protected void OnStop() {
		super.OnStop();
		if(isNeed){
			CameraView.Instance().Minimize();	
		}
		isNeed = true;
		MapView.getInstance().onResume();
	}
	@Override
	protected void OnDestroy() {
//		guideBar.enableIllust();
		guideBar.destory();
		bottomArea.destory();
		temHandler.removeMessages(tmpmessageType);
		temHandler = null;
		super.OnDestroy();
	}
	
}

