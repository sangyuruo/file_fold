package com.billionav.navi.component.basiccomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RelativeLayoutNoPress extends RelativeLayout {

	public RelativeLayoutNoPress(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RelativeLayoutNoPress(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RelativeLayoutNoPress(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(false);
	}

}
