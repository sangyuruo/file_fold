package com.billionav.navi.naviscreen.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.ViewHelp;
import com.billionav.navi.component.actionbar.BaseActionBar;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.menucontrol.ADT_MenuBase;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.uitools.NetTools;
import com.billionav.ui.R;

public class ActivityBase extends ADT_MenuBase {
	
	private BaseActionBar actionbar;
	private boolean hasTitle = true;
	
	
	public ActivityBase() {
		BundleNavi.getInstance().forceUpdateBundle(this.getClass());
	}
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		BundleNavi.getInstance().updateBundle(this.getClass());
		if(MapOverwriteLayer.getInstance() != null) {
			MapOverwriteLayer.getInstance().notifyScreenChanged();
		}
		
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		BundleNavi.getInstance().updateBundle(this.getClass());
		
		if(isNeedSetScreenId()) {
			UIMapControlJNI.SetScreenID(onConnectedScreenId());
		}
		
	}
	
	protected int onConnectedScreenId() {
		return SCRMapID.ADT_ID_NoThing;
	}
	
	protected boolean isNeedSetScreenId(){
		return true;
	}
	
	public final void setContentView(int layoutResID, boolean needActionbar){
		if(needActionbar){
			super.setContentView(actionbar = new BaseActionBar(this));
			actionbar.addViewToBelowView(layoutResID);

			if(!hasTitle) {
				setNoTitle();
			}
		}
		else{
			super.setContentView(layoutResID);
		}
	}
	
	@Override
	public boolean BackWinChange() {
		// TODO Auto-generated method stub
		return super.BackWinChange();
	}
	
	@Override
	public final void setContentView(int layoutResID) {
		super.setContentView(actionbar = new BaseActionBar(this));
		actionbar.addViewToBelowView(layoutResID);

		if(!hasTitle) {
			setNoTitle();
		}
		
	}

	public final void setContentView(View view, boolean needActionbar){
		if(needActionbar){
			super.setContentView(actionbar = new BaseActionBar(this));
			actionbar.addViewToBelowView(view);
			if(!hasTitle){
				setNoTitle();
			}
		}
		else{
			super.setContentView(view);
		}
	}
	
	
	@Override
	public final void setContentView(View view) {
		setContentView(view, true);
	}
	
	public final void setContentView(int layoutResID, Class<? extends BaseActionBar> actionbarClass) {
		super.setContentView(actionbar = getActionBarInstace(actionbarClass));
		actionbar.addViewToBelowView(layoutResID);
	}
	
	public final void setContentView(View v, Class<? extends BaseActionBar> actionbarClass) {
		super.setContentView(actionbar = getActionBarInstace(actionbarClass));
		actionbar.addViewToBelowView(v);
	}
	
	public final boolean isNetEnable() {
//		boolean netDisconntect = NetTools.isNetEnable();
//		if(!netDisconntect) {
//			CustomToast.showToast(this, R.string.STR_MM_01_01_01_13, 2000);
//			return false;
//		}
		
		return true;
	}
	
	public final boolean isWifiEnable() {
		return (PConnectReceiver.getConnectType() == PConnectReceiver.CONNECT_TYPE_WIFI);
	}
	
	@Override
	public final View findViewById(int id) {
		if(actionbar != null){
			return actionbar.getBelowLayout().findViewById(id);
		} else {
			return super.findViewById(id);
		}
	}
	
	private BaseActionBar getActionBarInstace(Class<? extends BaseActionBar> actionbarClass) {
		try {
			Constructor<? extends BaseActionBar> contructor = actionbarClass.getConstructor(Context.class);
			return contructor.newInstance(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return new BaseActionBar(this);
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
				
				ActivityBase.super.TriggerForWinscape(triggerInfo);
			}
		};
	};
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK) && (this instanceof OnScreenBackListener)){
			((OnScreenBackListener)this).onBack();
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}
	
	
	public final BaseActionBar getActionBar2() {
		return actionbar;
	}
	
	@Override
	protected void OnPause() {
		if(actionbar != null) {
			closeInputKeyBoard(actionbar);
		}
		if(!BundleNavi.getInstance().getCurrentActivityClass().equals(getClass())) {
			if(MapOverwriteLayer.getInstance() != null) {
				MapOverwriteLayer.getInstance().notifyScreenChanged();
			}
		}
		super.OnPause();
	}
	
	
	/**
	 * you can get previous screen data, and put data for next screen
	 * 
	 * @return
	 */
	public final BundleNavi getBundleNavi() {
		return BundleNavi.getInstance();

	}
	
	/**
	 * if you don't need title, call this method before setContentView
	 * */
	protected final void setNoTitle(){
		if(actionbar == null){
			hasTitle = false;
		} else {
			actionbar.setNoTitle();
		}
	}
	
	public final void addViewToOverActionbar(View v) {
		actionbar.addView(v);
	}
	
	/** add item for action bar */
	public final void addActionItem(int imageId, OnClickListener l){
		actionbar.addActionItem(imageId, l);
	}
	/** add item for action bar */
	public final void addActionItem2(int imageId, int textid, OnClickListener l){
		actionbar.addActionItem2(imageId, textid, l);
	}
	/** add item for action bar */
	public final void addActionItem3(int textid, OnClickListener l){
		actionbar.addActionItem3(textid, l);
	}
	
	public final void addActionItem3(String text, OnClickListener l){
		actionbar.addActionItem3(text, l);
	}

	/**
	 * set default background.
	 * */
	public final void setDefaultBackground() {
		actionbar.setDefaultBackground();
	}
	
	/**
	 * set background, you need supply image id for portrait and landscape
	 * */
	public final void setBackgroundResource(int portImage, int LandImage) {
		actionbar.setBackgroundResource(portImage, LandImage);
	}
	
	
	/**
	 * set action bar text, after setContextView
	 * @param resId
	 */
	public final void setTitleText(int resId){
		actionbar.setText(resId);
	}
	
	@Override
	public void setTitle(int titleId) {
		if(actionbar != null){
			setTitleText(titleId);
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
	public void setTitleRight(CharSequence title){
		if(actionbar != null){
			actionbar.setTextRight(title);
		}
	}
	public void setTitleRight(int title){
		if(actionbar != null){
			actionbar.setTextRight(title);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ViewHelp.REQUEST_CODE_VOICE_RECOGNIZER && resultCode == RESULT_OK) {
    		ArrayList<String> resStrList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		String resStr = resStrList.get(0);
    		onVoiceRecognizerFinished(resStr);
    	} else {
        	super.onActivityResult(requestCode, resultCode, data);
    	}
    	
	}
	
	private final void closeInputKeyBoard(ViewGroup vg) {
		int count = vg.getChildCount();
		
		for(int i=0; i<count; i++) {
			View child = vg.getChildAt(i);
			
			if(child instanceof EditText) {
				ViewHelp.closeInputKeyBoard((EditText)child);
			} else if(child instanceof ViewGroup) {
				closeInputKeyBoard((ViewGroup) child);
			}
		}
	}
	
	
	public final void showProgress() {
		actionbar.showProgress();
	}
	
	public final void dismissProgress() {
		actionbar.dismissProgress();
	}
	
	public final void lockForClickListener() {
		LockScreen();
		unLockHandler.sendEmptyMessageDelayed(1, 300);
	}
	
	private Handler unLockHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == 1) {
				UnlockScreen();
			}
		};
	};
	
	/**
	 * when you call ViewHelp.startVoiceRecognizer(), 
	 * you can override this method, get string.
	 * 
	 * */
	protected void onVoiceRecognizerFinished(String str){
		
	}
	public void setTitleBackgroundBlack(){
		actionbar.setTitleBackgroundBlack();
	}
}
