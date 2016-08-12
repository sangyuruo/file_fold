package com.billionav.navi.component.listcomponent;


import com.billionav.ui.R;

import android.content.Context;
import android.util.AttributeSet;

public class MultiChoiceLayout extends CheckedRelativeLayout {

	public MultiChoiceLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setImageResouce(R.drawable.navicloud_and_505a, R.drawable.navicloud_and_506a);
	}
	public void setVisableCheckedBox(boolean isvisable){
		if(isvisable){
			setImageResouce(R.drawable.navicloud_and_505a, R.drawable.navicloud_and_506a);
		}else{
			setImageResouce(0, 0);
		}
	}
}
