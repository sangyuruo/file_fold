package com.billionav.navi.naviscreen.auth;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.setting.ADT_Settings_Main;
import com.billionav.ui.R;

public class ADT_Auth_Agreement extends ActivityBase {

	private WebView webView;
	private boolean isSetting = false;
	private static final String encoding = "utf-8";
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_auth_agreement);
		setDefaultBackground();
		
		webView = (WebView) findViewById(R.id.webview01);
		Class temp = MenuControlIF.Instance().GetHierarchyBelowWinscapeClass();
		if(temp!=null){
		  isSetting = temp.equals(ADT_Settings_Main.class);
		}
		if(!isSetting){
			setTitle(R.string.STR_MM_09_02_01_01);
			addActionItem3(R.string.STR_MM_09_02_01_02, new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BackWinChange();
				}
			});
			addActionItem3(R.string.STR_MM_09_02_01_03, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ForwardWinChange(ADT_Auth_Register.class);
				}
			});
		}else{
			setTitle(R.string.STR_MM_06_01_01_93);
		}
	
		
	        webView.getSettings().setJavaScriptEnabled(true);
	        webView.getSettings().setSupportZoom(false);
	        webView.getSettings().setBuiltInZoomControls(false);
	        webView.getSettings().setDefaultTextEncodingName(encoding);
	        webView.setBackgroundColor(0);
	        webView.loadUrl("file:///android_asset/contrat.html"); 
	}

}
