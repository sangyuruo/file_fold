package com.billionav.navi.naviscreen.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.actionbar.BaseActionBar;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.menucontrol.ADT_PreferenceMenuBase;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;

public class PrefenrenceActivityBase extends ADT_PreferenceMenuBase {
	
	private BaseActionBar actionbar;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		BundleNavi.getInstance().updateBundle(this.getClass());
	}
	
	@Override
	public final void setContentView(int layoutResID) {
		actionbar = new BaseActionBar(this);
		actionbar.addViewToBelowViewForPreferemce(layoutResID);
//		actionbar.setBackgroundColor(Color.BLACK);
		actionbar.setBackgroundColor(Color.WHITE);
		super.setContentView(actionbar);
		getListView().setCacheColorHint(0);
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		BundleNavi.getInstance().updateBundle(this.getClass());
		UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_NoThing);
	}
	
	@Override
	public final boolean TriggerForWinscape(NSTriggerInfo triggerInfo) {
		if(GlobalTrigger.getInstance().onTrigger(triggerInfo)){
			return true;
		}
		triggerAction.sendMessage(triggerAction.obtainMessage(1, triggerInfo));
		return true;
	}
	private Handler triggerAction = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			if(msg.what==1 && msg.obj instanceof NSTriggerInfo) {
				NSTriggerInfo triggerInfo = (NSTriggerInfo) msg.obj;
				if(MapOverwriteLayer.getInstance() != null) {
					MapOverwriteLayer.getInstance().notifyTriggerReceived(triggerInfo);
				}
				
				if(actionbar!=null && actionbar.notifyTriggerReceived(triggerInfo)) {
					return;
				}
				
				PrefenrenceActivityBase.super.TriggerForWinscape(triggerInfo);
			}
		};
	};
	@Override
	public void setTitle(int titleId) {
		if(actionbar != null){
			actionbar.setText(titleId);
		} else {
			super.setTitle(titleId);
		}
	}
	
	@Override
	public void setTitle(CharSequence title) {
		if(actionbar != null){
			actionbar.setText(title);
		} else {
			super.setTitle(title);
		}
		
		
	}
	
	public final BaseActionBar getActionBar2() {
		return actionbar;
	}
	
	public final void showProgress() {
		actionbar.showProgress();
	}
	
	public final void dismissProgress() {
		actionbar.dismissProgress();
	}


}
