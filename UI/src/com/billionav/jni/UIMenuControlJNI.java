package com.billionav.jni;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;

public class UIMenuControlJNI {
    
	public static native void	J2CAttach();
	public static native void	J2CDetach();

	//called by C lib
	
	public static boolean C2JCallTriggerForScreen(int iTriggerID,
		   	  long lparam1,
		   	  long lparam2,
		   	  long lparam3,
		   	  long lparam4,
		   	  String sParam1){
		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
		cTriggerInfo.m_iTriggerID = iTriggerID;
		cTriggerInfo.m_lParam1 = lparam1;
		cTriggerInfo.m_lParam2 = lparam2;
		cTriggerInfo.m_lParam3 = lparam3;
		cTriggerInfo.m_lParam4 = lparam4;	
		cTriggerInfo.m_String1 = sParam1;
		if(null != MenuControlIF.Instance())
		{
			return MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
		}
		else
		{
			return false;
		}
	}
}
