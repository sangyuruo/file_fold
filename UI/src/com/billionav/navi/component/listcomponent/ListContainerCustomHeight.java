package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ListContainerCustomHeight extends ListContainer {

	public ListContainerCustomHeight(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	public ListContainerCustomHeight(Context context) {
		super(context);
		setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

}
