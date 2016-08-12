package com.billionav.navi.naviscreen.map;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ViewFlipper;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;

public class ADT_AR_Main_Tip extends ActivityBase{

	private ViewFlipper viewFlipper = null;

	private Button button_01 = null;
	private Button button_02 = null;
	private CheckBox check = null;
	private float touchDownX; 
    private float touchUpX; 
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.scr_ar_main_tip, false);
		findViews();
		setClickEvent();
		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	private void setClickEvent() {
		viewFlipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
			       if (event.getAction() == MotionEvent.ACTION_DOWN) { 
			            touchDownX = event.getX(); 
			            return true; 
			        } else if (event.getAction() == MotionEvent.ACTION_UP) { 
			            touchUpX = event.getX(); 
			            if (touchUpX - touchDownX > 100) { 
			                if(viewFlipper.getDisplayedChild() == 0){
			            		viewFlipper.stopFlipping();
			            		return false;
			            	}else{
				                viewFlipper.setInAnimation(inFromLeftAnimation()); 
				                viewFlipper.setOutAnimation(outToRightAnimation()); 
				                viewFlipper.showPrevious(); 
			            	}
			            } else if (touchDownX - touchUpX > 100) { 
			           
			               if(viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1){
			            		viewFlipper.stopFlipping();
			            		return false;
			            	}else{
			                viewFlipper.setInAnimation(inFromRightAnimation()); 
			                viewFlipper.setOutAnimation(outToLeftAnimation()); 
			                viewFlipper.showNext(); 
			            	} 
			            } 
			            return true; 
			        } 
			        return false; 
			}
		});
		
		button_01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				forwardARActivity(true);
			}
		});
		button_02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				forwardARActivity(false);
			}
		});
	}
	
	private void forwardARActivity(boolean angleCorrection){
		if(check.isChecked()){
			SharedPreferenceData.IS_NEED_AR_TIP.setValue(false);
		}else{
			SharedPreferenceData.IS_NEED_AR_TIP.setValue(true);
		}
		ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
		getBundleNavi().putBoolean("AngleCorrection", angleCorrection);
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		ForwardKeepDefaultWinChange(ADT_AR_Main.class);		
	}
	
	private void findViews() {
		viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
		button_01 = (Button) findViewById(R.id.button01);
		button_02 = (Button) findViewById(R.id.button02);
		check = (CheckBox) findViewById(R.id.checkBox1);
		check.setChecked(false);
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			forwardARActivity(false);
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}
	public static Animation inFromRightAnimation(){
		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,+1.0f,Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f);
		inFromRight.setDuration(350);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}
	public static Animation outToLeftAnimation(){
		Animation outToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,-1.0f,Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f);
		outToLeft.setDuration(350);
		outToLeft.setInterpolator(new AccelerateInterpolator());
		return outToLeft;
	}
	public static Animation inFromLeftAnimation(){
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f,Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f);
		inFromLeft.setDuration(350);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	public static Animation outToRightAnimation(){
		Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,+1.0f,Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT,0.0f);
		outToRight.setDuration(350);
		outToRight.setInterpolator(new AccelerateInterpolator());
		return outToRight;
	}
	

}
