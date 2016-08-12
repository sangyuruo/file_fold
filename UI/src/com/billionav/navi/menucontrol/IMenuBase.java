package com.billionav.navi.menucontrol;

public interface IMenuBase {
	public void 	SetScreenInfo();	
	public void 	Initialize();
	public boolean 	IsProhibited();
	public void 	Prohibition(boolean bProhibited);
	public void 	Show();	
	public void 	Exit();
	
	public void OnSetScreenInfo();
	public void OnInitialize();
	public void OnProhibition(boolean bProhibited);
	public void OnShow();	
	public void OnExit(int eWinChangeInterruptOption);
	
}
