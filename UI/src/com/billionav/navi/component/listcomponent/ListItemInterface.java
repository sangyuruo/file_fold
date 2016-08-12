package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class ListItemInterface extends RelativeLayout{
	
	private int index = -1;
	
	public ListItemInterface(Context context) {
		super(context);
	}

	public ListItemInterface(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ListItemInterface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	private OnClickListener l;
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}
	
	protected void notifyItemClick() {
		
		if(l != null){
			l.onClick(this);
		}
	}

	public final int getIndex() {
		return index;
	}

	/*package*/void setIndex(int index) {
		this.index = index;
	}
	
}
