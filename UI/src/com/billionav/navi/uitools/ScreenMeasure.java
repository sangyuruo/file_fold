package com.billionav.navi.uitools;


import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;

public class ScreenMeasure {
	private Activity a;
	private int statusBar;
	
	private static ScreenMeasure instance;
	
	private ScreenMeasure(Activity a) {
		this.a = a;
		
		Rect frame = new Rect();
		a.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBar = frame.top;
	}
	
	public static ScreenMeasure create(Activity a){
		instance = new ScreenMeasure(a);
		return instance;
	}
	
	public static ScreenMeasure getInstance() {
		if(instance == null) {
			instance = new ScreenMeasure(NaviViewManager.GetViewManager());
		}
		return instance;
	}
	
	public static int getHightOfStatusbar() {
		return getInstance().statusBar;
	}
	
	public static int getHeightOfActionBar(ActivityBase a) {
		return a.getActionBar2()==null ? 0 : a.getActionBar2().getActionbarHeight();
	}
	
	public static boolean isPortrait() {
		return (getInstance().a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
	}
	
	public static int getHeight() {
		return getInstance().a.getResources().getDisplayMetrics().heightPixels;
	}

	public static int getWidth() {
		return getInstance().a.getResources().getDisplayMetrics().widthPixels;
	}

}
