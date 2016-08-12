package com.billionav.navi.uicommon;

import com.billionav.navi.menucontrol.MenuControlIF;

public class UIC_SreachCommon {
	public static Class curListActivityclass;
	public static boolean isHasSearchOperation = false;
	public static boolean isUpdateBySearch = false;
	public static void setCurListActivityclass(Class listclass){
		curListActivityclass = listclass ;
	}
	public static Class getCurListActivityclass(){
		return curListActivityclass;
	}
	public static void backToListScreen(){
		if(UIC_SreachCommon.getCurListActivityclass() != null 
				&& MenuControlIF.Instance().SearchWinscape(UIC_SreachCommon.getCurListActivityclass()))
		{
			MenuControlIF.Instance().BackSearchWinChange(UIC_SreachCommon.getCurListActivityclass());
		} else {
			MenuControlIF.Instance().BackWinChange();
		}
	}
}
