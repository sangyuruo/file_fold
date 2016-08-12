package com.billionav.navi.naviscreen.base;

import java.util.HashSet;
import java.util.Set;

import com.billionav.navi.menucontrol.NSTriggerInfo;


public final class GlobalTrigger {
	private static final GlobalTrigger instance = new GlobalTrigger();
	
	private final Set<TriggerListener> triggerListenerList;
	
	private GlobalTrigger(){
		triggerListenerList = new HashSet<TriggerListener>();
	}
	
	public static GlobalTrigger getInstance() {
		return instance;
	}

	/*package*/ boolean onTrigger(NSTriggerInfo triggerInfo){
		for(TriggerListener l : triggerListenerList){
			if(l.onTrigger(triggerInfo)){
				return true;
			}
		}
		return false;
	}
	
	public void addTriggerListener(TriggerListener l){
		triggerListenerList.add(l);
	}
	
	public void removeTriggerListener(TriggerListener l) {
		triggerListenerList.remove(l);
	}
}
