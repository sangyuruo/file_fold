package com.billionav.navi.component.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.billionav.ui.R;
import com.billionav.navi.component.ViewHelp;

public class ActionbarItem extends ImageView implements ActionItemInterface{
	
	public ActionbarItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	public ActionbarItem(Context context, int resId) {
		super(context);
		setImageResource(resId);
		setBackgroundDrawable(getActionItemPressSelector());
		setLayoutParam();
		
		setScaleType(ScaleType.CENTER_INSIDE);
	}
	
	public ActionbarItem(Context context, int resId, OnClickListener l) {
		this(context, resId);
		setOnClickListener(l);
	}
	
	public ActionbarItem(Context context, Drawable drawable, OnClickListener l) {
		super(context);
		setImageDrawable(drawable);
		setBackgroundDrawable(getActionItemPressSelector());
		setLayoutParam();
		setOnClickListener(l);
	}
	
	public ActionbarItem(Context context, int normalid, int pressid) {
		super(context);
		
		setImageDrawable(ViewHelp.createDrawableListByImageID(getContext(), normalid, pressid));
		
		setLayoutParam();
		
		setScaleType(ScaleType.CENTER_INSIDE);
	}


	private void setLayoutParam() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 10;
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		setLayoutParams(lp);
	}
	public void addToLinearLayout(LinearLayout l) {
		l.addView(this);
	}
	private Drawable getActionItemPressSelector() {
		return ViewHelp.createDrawableListByImageID(getContext(), -1, R.drawable.navicloud_and_726_02b);
	}
}
