package com.billionav.navi.component.gridcomponent;


import com.billionav.navi.component.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.GridView;

public class RoundedRectGridView extends GridView {
	
	//unit: dip
	private static final float GRID_RADIUS_DEFAULT = 8;
	
	//unit: px
	private float radius;

	private Path mClip;

	public RoundedRectGridView(Context context) {
		super(context);
		init();
	}
	
	public RoundedRectGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}



	public RoundedRectGridView(Context context, AttributeSet attrs) {
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
		radius = DensityUtil.dp2px(getContext(), GRID_RADIUS_DEFAULT);
		
		GradientDrawable gd = new GradientDrawable();
		gd.setCornerRadius(radius);
		gd.setColor(Color.WHITE);
		setBackgroundDrawable(gd);
		setCacheColorHint(0);
		setVerticalFadingEdgeEnabled(false);
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
