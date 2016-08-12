package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.billionav.ui.R;
import com.billionav.navi.component.DensityUtil;

public class RoundedRectListView extends ListViewNavi {
	
	//unit: dip
	private static final float RADIUS_DEFAULT = 8;
	
	//unit: px
	private float radius;

	private Path mClip;

	public RoundedRectListView(Context context) {
		super(context);
		init();
	}
	
	public RoundedRectListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}



	public RoundedRectListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	
	/**
	 * @param radius unit dip
	 * */
	public void setRadius(float radius){
		this.radius = DensityUtil.dp2px(getContext(), radius);
	}
	
	private void init() {
		radius = DensityUtil.dp2px(getContext(), RADIUS_DEFAULT);
		
		GradientDrawable gd = new GradientDrawable();
		gd.setCornerRadius(radius);
		gd.setColor(Color.WHITE);
		setBackgroundDrawable(gd);
		setCacheColorHint(0);
		setVerticalFadingEdgeEnabled(false);
		setDivider(getResources().getDrawable(R.drawable.navicloud_and_741a));
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mClip = new Path();
		RectF rect = new RectF(0, 0, w, h);
		mClip.addRoundRect(rect, radius, radius, Direction.CW);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.clipPath(mClip);
		super.dispatchDraw(canvas);
		canvas.restore();
	}
	
}
