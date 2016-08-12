package com.billionav.navi.naviscreen.map;

import com.billionav.DRIR.DRIRAppMain;


public class ForwardARscreenControl {
	private int iLastMode;
	private int iCurrentMode;
	private static ForwardARscreenControl instance;
	public static ForwardARscreenControl getinstance(){
		if(instance == null){
			instance = new ForwardARscreenControl();
		}
		return instance;
	}
	public void setDRIRFunChangeState(int iLastMode, int iCurrentMode){
		this.iLastMode = iLastMode;
		this.iCurrentMode =  iCurrentMode;
	}
	public void DRIRFunChange(){
		DRIRAppMain.DRIRFunChange(iLastMode, iCurrentMode);
	}
}
