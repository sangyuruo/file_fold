package com.billionav.DRIR.jni;

 import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
 

public class jniDRIR_DispControl {
	public static native void 	DrawARView();
	public static native void	J2CAttach();
	public static native void	J2CDetach();
	
	//called by C lib
	public static boolean C2JCallTriggerForScreen(int iTriggerID,
		   	  long lparam1,
		   	  long lparam2,
		   	  long lparam3,
		   	  long lparam4)
	{
		//System.out.println("MenuControl-----C2JCallTriggerForScreen:iTriggerID[" +iTriggerID + "]["+lparam1+ "]["+lparam2+ "]["+lparam3+ "]["+lparam4+"]");
		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
		cTriggerInfo.m_iTriggerID = iTriggerID;
		cTriggerInfo.m_lParam1 = lparam1;
		cTriggerInfo.m_lParam2 = lparam2;
		cTriggerInfo.m_lParam3 = lparam3;
		cTriggerInfo.m_lParam4 = lparam4;	
		
		if (null == MenuControlIF.Instance())
		{
			return false;
		}
		return MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
	}	

}
