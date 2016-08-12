package com.billionav.navi.component.gridcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class GridItemInterface extends RelativeLayout{
	
	private int index = -1;
	
	public GridItemInterface(Context context) {
		super(context);
	}

	public GridItemInterface(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GridItemInterface(Context context, AttributeSet attrs, int defStyle) {
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