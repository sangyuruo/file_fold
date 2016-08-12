package com.billionav.navi.menucontrol;

import android.content.res.Configuration;

public interface IMenuControl {	
	public boolean CheckQuit();
	public boolean IsProhibited();
	public boolean IsScreenLocked();
	public void LockScreen(boolean bLock);
	
	public boolean IsWinChangeLocked();
	
	public int GetWinchangeType();	
	public Class<?> GetCurrentWinscapeClass();
	public Class<?> GetNextWinscapeClass();	
	public Class<?> GetLastWinscapeClass();	
	public Class<?> GetHierarchyBelowWinscapeClass();	
	public Class<?> GetDefaultWinscapeClass();
	
	public boolean SearchWinscape(Class<?> wincls);
	public boolean SearchWinscapeExceptCurrent(Class<?> wincls);		
	
	public void SetWinchangeAnimation(int iAnimaID);
	
	public boolean BackWinChange();
	public boolean BackSearchWinChange(Class<?> wincls);	
	public boolean BackDefaultWinChange();		
	public boolean ForwardWinChange(Class<?> wincls);
	public boolean ForwardWinChangeByInsertWinsToStackBelowTop(Class<?>[] jumpwincls,Class<?> wincls);
	public boolean ForwardWinChangeByInsertWinsToStackBelowTopDeleteCurWin(Class<?>[] jumpwincls,Class<?> wincls);
	public boolean ForwardSearchWinChange(Class<?> wincls);	
	public boolean ForwardKeepDepthWinChange(Class<?> wincls);	
	public boolean ForwardDefaultWinChange(Class<?> wincls);	
	public boolean ForwardKeepDefaultWinChange(Class<?> wincls);
	
	
	//public void JustPushWinscape();
	public void OnConfigurationChanged(Configuration newConfig);
}
