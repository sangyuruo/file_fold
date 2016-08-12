package com.billionav.navi.component.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ActionBarGuide extends BaseActionBar {

	private ActionBarGuideItem guideItem;
	
	public ActionBarGuide(Context context) {
		super(context);
		initialize();
	}

	public ActionBarGuide(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	private void initialize(){
	}
	
	@Override
	protected void onPrepareActionView(ViewGroup decView) {
		decView.addView(guideItem = new ActionBarGuideItem(getContext()),1);
	}
	
	public void setGuideTitleText(String text){
		guideItem.setText(text);
	}
	
	public void setGuideSubTitleText(String text) {
		guideItem.setSubText(text);
	}
	
}
