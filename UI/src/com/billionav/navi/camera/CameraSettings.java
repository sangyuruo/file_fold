package com.billionav.navi.camera;


public class CameraSettings{
	private static CameraSettings sSettings;
	private int mZoomFactor = 0;
	private boolean mPortrait = false;
	
	public static synchronized CameraSettings instance() {
		if(null == sSettings){
			sSettings = new CameraSettings();
		}
		return sSettings;
	}
	
	public void setZoomFactor(int factor){
		mZoomFactor = factor;
	}
	
	public int getZoomFactor(){
		return mZoomFactor;
	}
	
	public boolean isPortrait() {
		return mPortrait;
	}

	public void setPortrait(boolean portrait) {
		mPortrait = portrait;
	}
	
}