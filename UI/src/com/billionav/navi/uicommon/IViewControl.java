package com.billionav.navi.uicommon;

import com.billionav.navi.menucontrol.NSTriggerInfo;

public interface IViewControl {
	public boolean receiveTrigger(NSTriggerInfo trigger);
	public void onResume();
}
