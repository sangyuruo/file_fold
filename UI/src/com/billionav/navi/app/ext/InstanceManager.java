package com.billionav.navi.app.ext;

import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;

public final class InstanceManager {
	ADT_Main_Map_Navigation.CurrentViewAction currentViewAction = null;
	public ADT_Main_Map_Navigation.CurrentViewAction getCurrentViewAction() {
		return currentViewAction;
	}

	public void setCurrentViewAction(
			ADT_Main_Map_Navigation.CurrentViewAction currentViewAction) {
		this.currentViewAction = currentViewAction;
	}

	static InstanceManager instance;
	static {
		instance = new InstanceManager();
	}

	private InstanceManager() {

	}

	public static InstanceManager getInstance() {
		return instance;
	}

}
