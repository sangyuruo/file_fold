package com.billionav.navi.naviscreen.open;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.InitlizationScreen;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.setting.ADT_Settings_Main;
import com.billionav.navi.system.AplRuntime;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class ADT_Opening_Disclaimer extends ActivityBase implements InitlizationScreen{
	
	private CheckBox checkbox;
	private LinearLayout checkboxLayout;
	private boolean isSetting = false;
	
	private WebView webView;
	private static final String encoding = "utf-8";
	
	private boolean FirstMapDrawDone = false;
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_open_disclaimer);
		
		setTitle(R.string.STR_MM_01_00_02_05);
		setDefaultBackground();
		webView = (WebView) findViewById(R.id.textView1);
		checkbox = (CheckBox) findViewById(R.id.checkbox_imageview);
		checkboxLayout = (LinearLayout) findViewById(R.id.checkboxLayout);
	
		checkbox.setChecked(false);
		Class<?> temp = MenuControlIF.Instance().GetHierarchyBelowWinscapeClass();
		if(temp!=null){
		    isSetting = temp.equals(ADT_Settings_Main.class);
		}
		
		if(!isSetting){
			addActionItem3(R.string.STR_MM_01_00_02_03, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SystemTools.exitSystem();
					
				}
			});
			addActionItem3(R.string.STR_MM_01_00_02_04, new OnClickListener() {
				
				@Override
				public void onClick(View v) {	
					SharedPreferenceData.OPEN_DISCAIMER_SETTING.setValue(checkbox.isChecked());
					getBundleNavi().putBoolean("FirstMapDrawDone", FirstMapDrawDone);
					MenuControlIF.Instance().setWinchangeWithoutAnimation();
					ForwardKeepDepthWinChange(ADT_Openning.class);
					
					
					
				}
			});
		}else{
			checkboxLayout.setVisibility(View.GONE);
		}
//		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setDefaultTextEncodingName(encoding);
		webView.setBackgroundColor(0);
		webView.loadUrl("file:///android_asset/declare.html"); 
//		if(isSetting) {
//			new Thread(){
//				public void run() {
//					webView.loadUrl("file:///android_asset/declare.html"); 
//				};
//			}.start();
//		}
		
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){

			if(isSetting){
				BackWinChange();
			} else {
				SystemTools.exitSystem();
			}
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		int iTriggerID = cTriggerInfo.GetTriggerID();
		switch(iTriggerID)
		{
			case NSTriggerID.UIC_MN_TRG_MAP_FIRST_DRAW_DONE:
			case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
				if ( FirstMapDrawDone == false )
				{
//					FirstMapDrawDone = true;					
//					AplRuntime.Instance().AplInitMainThrd();
//					NaviViewManager.GetViewManager().initGlobalInfo();

				}
				return true;
		}
		
		return false;
	}
	
	
}
