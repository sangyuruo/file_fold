package com.billionav.navi.naviscreen.auth;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.setting.ADT_Settings_Main;
import com.billionav.ui.R;

public class ADT_Auth_Lience extends ActivityBase {

	private WebView webView;
	private boolean isSetting = false;
	private static final String encoding = "utf-8";

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_auth_lience);
		setDefaultBackground();

		webView = (WebView) findViewById(R.id.webview01);

		setTitle(getString(R.string.STR_MM_06_02_07_01));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setDefaultTextEncodingName(encoding);
		webView.setBackgroundColor(0);
		webView.loadUrl("file:///android_asset/lience.html");
	}

}
