package com.billionav.navi.uitools;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.GlobalTrigger;
import com.billionav.navi.naviscreen.base.TriggerListener;
import com.billionav.navi.uicommon.UIC_SystemCommon;

public class OpeningTriggerReceiver implements TriggerListener {

	private static OpeningTriggerReceiver instance;
	
	public static OpeningTriggerReceiver getInstance(){
		if(null == instance) {
			instance = new OpeningTriggerReceiver();
		}
		return instance;
	}
	
	
	private boolean FirstMapDrawDone = false;
	private boolean DlndataNetError = false;
	private boolean DlndataFinished = false;
	private int ndataCheckResult = -1;
	@Override
	public boolean onTrigger(NSTriggerInfo cTriggerInfo) {
		int iTriggerID = cTriggerInfo.GetTriggerID();
		switch(iTriggerID)
		{
			case NSTriggerID.UIC_MN_TRG_MAP_FIRST_DRAW_DONE:
			case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
				if ( FirstMapDrawDone == false )
				{
					FirstMapDrawDone = true;					
				}
				break;
//			case NSTriggerID.UIC_MN_TRG_MC_DAYNIGHT_CHANGE:{
//					UIC_SystemCommon.setIsDayStatus(0 == cTriggerInfo.GetlParam1());
////					if(jniSetupControl.get(jniSetupControl.STUPDM_MAPCOLOR_CHANGE) == jniSetupControl.STUPDM_MAP_COLOR_CHANGE_BY_TIME){
//						if(UIC_SystemCommon.getIsDayStatus()){
//							UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_DAY);
//						}else{
//							UIMapControlJNI.SetDayNightMode(UIMapControlJNI.MAP_COLOR_MODE_NIGHT);
//						}
////					}
//					break;
//				}
			case NSTriggerID.UIC_MN_TRG_DLNDATA_CHECK_FINISHED:
				ndataCheckResult = (int)cTriggerInfo.GetlParam1();
				break;
			case NSTriggerID.UIC_MN_TRG_DLNDATA_NET_ERROR:
				DlndataNetError = true;
				break;
			case NSTriggerID.UIC_MN_TRG_DLNDATA_DOWNLOAD_FINISHED:
				DlndataFinished = true;
				break;
			default:
				break;
		}
		return false;
	}
	
	public void removeNDataCheckResultFLag(){
		ndataCheckResult = -1;
	}
	public void removeFirstMapDrawDownFlag(){
		FirstMapDrawDone = false;
	}
	public void  removedataDownlaodFinishedFlag() {
		DlndataFinished = false;
	}
	public void removeNdataDownlaodNetErrorFlag() {
		DlndataNetError = false;
	}
	
	public boolean hasFirstMapDrawDoneReceived(){
		return FirstMapDrawDone;
	}
	public int getNDataCheckResult(){
		return ndataCheckResult;
	}
	public boolean hasNdataDownlaodFinished() {
		return DlndataFinished;
	}
	public boolean hasNdataDownlaodNetError() {
		return DlndataNetError;
	}
	
	public void addToGloble(){
		GlobalTrigger.getInstance().addTriggerListener(this);
	}
	public void removeFromGloble() {
		GlobalTrigger.getInstance().removeTriggerListener(this);
	}
}
