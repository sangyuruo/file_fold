package com.billionav.navi.component.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.billionav.navi.component.DensityUtil;

public class ActionBarItem3 extends Button  implements ActionItemInterface{
	public ActionBarItem3(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ActionBarItem3(Context context, String text) {
		super(context);
		setLayoutParam();
		setText(text);
	}
	public ActionBarItem3(Context context, int textID){
		this(context, context.getString(textID));
	}
	
	public ActionBarItem3(Context context, int textID, OnClickListener l) {
		this(context, textID);
		setOnClickListener(l);
	}
	
	public ActionBarItem3(Context context, String text, OnClickListener l) {
		this(context, text);
		setOnClickListener(l);
	}
	
	public void addToLinearLayout(LinearLayout l) {
		l.addView(this);
	}

	private void setLayoutParam() {
		Context context = getContext();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		lp.weight = 10;
		lp.setMargins(DensityUtil.dp2px(context, 3), DensityUtil.dp2px(context, 2), DensityUtil.dp2px(context, 3), DensityUtil.dp2px(context, 2));
		setLayoutParams(lp);
	}
}
