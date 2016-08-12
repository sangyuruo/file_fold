package com.billionav.navi.menucontrol;

public interface IViewManager {
	public IMenuBase GetCurMenuBase();	
	public IMenuBase GetMenuBaseById(String id);
	public void CreateViewMenuBase(Class<?> cls,String id);
	public void InsertViewMenuBase(boolean bUponLast,int iAnimIn,int iAnimOut);
	public void DestroyViewMenuBase(String id);
}
