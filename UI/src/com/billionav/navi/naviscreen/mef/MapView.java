package com.billionav.navi.naviscreen.mef;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.billionav.jni.MapEngineJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uicommon.UIC_MapCommon;
import com.billionav.navi.uitools.GestureDetector.OnGestureListener;
import com.billionav.navi.uitools.MapMotion;
import com.billionav.navi.uitools.ScreenMeasure;

public class MapView extends EglView {

    private static MapView instance;
    private boolean whetherAdaptOutsideSetting = false;

	public static MapView createInstance(Context context) {
		return (instance != null) ? instance : (instance = new MapView(context));
	}

	public static MapView getInstance() {
		return instance;
	}

//	private double dDefLon = 0.888043, dLon = dDefLon;
//    private double dDefLat = 0.393761, dLat = dDefLat;
	protected MapMotion mapMotion;
    

//    private int iDefDir = 0x0000, iDir = iDefDir;
//    private double cameraAngle = -(1.0/2.5)*Math.PI;
//    private int iHeight = 772;
    
    
//    private final static double ANGLE_MODULUS  = (65535/(2* Math.PI));
//
//    private static final double MAP_MAX_ANGLE = -0.333f * Math.PI;
//    private static final double MAP_MIN_ANGLE = -0.5f * Math.PI;
//    private static final double MAP_ANGLE_PER_STEP = (1.5/180.0) * Math.PI; //1.5 degree
    
    private MapView(Context context) {
        super(context);
        
        onResume();
        //MapEngineJNI.init();
        MapEngineJNI.SetScreenDPI((int)SystemInfo.getDpi()[0], (int)SystemInfo.getDpi()[1]);
        mapMotion = new MapMotion(context);
        resizeScaleBar();

    }
    @Override
    public void setLayoutParams(LayoutParams params) {
    	// TODO Auto-generated method stub
    	super.setLayoutParams(params);
    	resizeScaleBar(0-((RelativeLayout.LayoutParams)params).leftMargin, 0);
    }
    public void resizeForAllMap(int viewHeight,int viewWidth){
    	boolean isCalculateSuccess = UIC_MapCommon.calculateAllRouteMapArea();
    	
   
    	if(isCalculateSuccess){
        	long[] lonlat = UIC_MapCommon.getAllRouteMapCenterLonLat();
    		int height = UIC_MapCommon.getAllRouteHeight(viewHeight,viewWidth);
    		UIMapControlJNI.SetCenterInfo((int)lonlat[0], (int)lonlat[1], height);

//    		UIMapControlJNI.SetCompassView(UIMapControlJNI.MAP_2D_NORTH_UP); 
//    		UIMapControlJNI.SetMapAngle(UIMapControlJNI.GetMinMapAngle());
        	
    	}
    	
    }
    
    public void resizeForAllMap(long[]lonlat,int height){
    	UIMapControlJNI.SetCenterInfo((int)lonlat[0], (int)lonlat[1], height);
//    	UIMapControlJNI.SetCompassView(UIMapControlJNI.MAP_2D_NORTH_UP); 
    }
    
    public void resizeScaleBarForOutsideAct(int offsetX, int offsetY){
    	resizeScaleBar(offsetX, offsetY);
    	whetherAdaptOutsideSetting = true;
    }
    
    public void initSizeScaleBar(){
    	whetherAdaptOutsideSetting = false;
    	resizeScaleBar(0, 0);
    }
    
    private void resizeScaleBar(){
    	resizeScaleBar(0, 0);
    }
    private void resizeScaleBar(int offsetX, int offsetY){
    	int posY = ScreenMeasure.getHeight() - DensityUtil.dp2px(getContext(), 140);
    	int posX = DensityUtil.dp2px(getContext(), 6);
    	if(!ScreenMeasure.isPortrait()){
    		posY += DensityUtil.dp2px(getContext(), 90);
    	}
    	UIMapControlJNI.SetScaleBarPos(posX + offsetX, posY + offsetY, 0);
    }
    
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	if(whetherAdaptOutsideSetting){
    		whetherAdaptOutsideSetting = false;
    		return;
    	}
    	resizeScaleBar();
    	
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mapMotion.onTouchEvent(event);
		return true;
	}

	public void setGestureListener(OnGestureListener onGestureListener) {
		mapMotion.setGestureListener(onGestureListener);
	}

	public void setGestureEnable(boolean enable) {
		mapMotion.setGestureEnble(enable, enable, enable, enable, enable, enable, enable, enable, enable, enable);
	}

	public void addGestureListener(OnGestureListener onGestureListener) {
		mapMotion.addGestureListener(onGestureListener);
	}

	public void removeGestureListener(OnGestureListener onGestureListener) {
		mapMotion.removeGestureListener(onGestureListener);
	}
    
    
    
}
