package com.billionav.navi.component.guidebar;


import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.base.GuideInfoController.DataChangeListener;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class GIB_LaneInfo extends LinearLayout implements DataChangeListener{
	
	private int[] currentlaneInfo;
	private boolean isIllustShown = false;
	private boolean isLaneWidthModified = false;
	
	private static final int LANE_WIDTH_DIP = 46;
	private static final int LANE_HEIGHT_DIP = 46;
	private static final int LANE_BAR_BORDER_WIDTH_PX = 10;
	private static final int LANE_SPACE_WIDTH_PX = 2;
	
	private int LANE_NUM_BUFFER_SIZE = 6;

	public GIB_LaneInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		initialize(); 
	}

	private void initialize(){
	    initViews();
	}
	
	private void initViews() {
		setBackgroundResource(R.drawable.navicloud_and_071a);
		initInnerLane();
	}
	
	private void initInnerLane() {
		for(int i=0; i<LANE_NUM_BUFFER_SIZE; i++) {
			addView(new ImageView(getContext()), DensityUtil.dp2px(getContext(), LANE_WIDTH_DIP), DensityUtil.dp2px(getContext(), LANE_HEIGHT_DIP));
			if(i<LANE_NUM_BUFFER_SIZE-1){
				ImageView iv = new ImageView(getContext());
				iv.setImageResource(R.drawable.navicloud_and_069a);
				addView(iv, LANE_SPACE_WIDTH_PX, DensityUtil.dp2px(getContext(), LANE_HEIGHT_DIP));
			}
		}
		
		setVisibility(View.GONE);
	}
	
	private void checkCount(int count) {
		if(count > LANE_NUM_BUFFER_SIZE){
			for(int i=0; i< count - LANE_NUM_BUFFER_SIZE; i++) {
				ImageView iv = new ImageView(getContext());
				iv.setImageResource(R.drawable.navicloud_and_069a);
				addView(iv, LANE_SPACE_WIDTH_PX, DensityUtil.dp2px(getContext(), LANE_HEIGHT_DIP));
				
				addView(new ImageView(getContext()), DensityUtil.dp2px(getContext(), LANE_WIDTH_DIP), DensityUtil.dp2px(getContext(), LANE_HEIGHT_DIP));
			}
			LANE_NUM_BUFFER_SIZE = count;
		}
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		onDataChanged();
	}
	
	@Override
	public void setVisibility(int visibility) {
		if(isLaneWidthModified && visibility ==View.GONE){
			autoResizeLaneInfoGridWidth(0, true);
		}
		super.setVisibility(visibility);
	}
	private void refreshLanes(){
		if(isIllustShown){
			setVisibility(View.GONE);
			return;
		}
		setVisibility(View.VISIBLE);
		int[] imageRes = GuideInfoDataManager.Instance().getGuideLaneImageID();
		setLaneImages(imageRes);
	}
	
	private void setLaneImages(int[] imageRes){
		
		if(imageRes == null || imageRes.length <= 0 || isIllustShown){
			requestLayout();
			setVisibility(View.GONE);
			return;
		}else{
			setVisibility(View.VISIBLE);
		}
		checkCount(imageRes.length);
		currentlaneInfo = imageRes;
		
		int laneInfoBarWidth = imageRes.length*(DensityUtil.dp2px(getContext(), LANE_WIDTH_DIP) + LANE_SPACE_WIDTH_PX) -LANE_SPACE_WIDTH_PX;
		if(laneInfoBarWidth > ScreenMeasure.getWidth() - LANE_BAR_BORDER_WIDTH_PX *2){
			laneInfoBarWidth = imageRes.length*(autoResizeLaneInfoGridWidth(imageRes.length, false) +LANE_SPACE_WIDTH_PX) -LANE_SPACE_WIDTH_PX;
		}else{
			if(isLaneWidthModified){
				autoResizeLaneInfoGridWidth(0, true);
			}
		}
		for(int i=0; i<getChildCount(); i+=2) {
			ImageView lane = (ImageView) getChildAt(i);
			int index = i/2;
			if(index < imageRes.length) {
				int resId = imageRes[imageRes.length - index-1];
				lane.setBackgroundResource(resId);
			}
		}
		
		getLayoutParams().width = laneInfoBarWidth;
		requestLayout();
	}
	
	private int autoResizeLaneInfoGridWidth(int length, boolean isReset) {
		int gridWidth = 0;
		if(!isReset){
			isLaneWidthModified = true;
			gridWidth = ((ScreenMeasure.getWidth() - LANE_BAR_BORDER_WIDTH_PX *2) + LANE_SPACE_WIDTH_PX)/length - LANE_SPACE_WIDTH_PX ;
		}else{
			isLaneWidthModified = false;
			gridWidth = DensityUtil.dp2px(getContext(), LANE_WIDTH_DIP);
		}
		for(int i=0; i<getChildCount(); i+=2) {
			ImageView lane = (ImageView) getChildAt(i);
			lane.getLayoutParams().width = gridWidth;
		}
		return gridWidth;
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		refreshLanes();
	}
	public void notifyIllustShown(boolean illustShown){
		isIllustShown = illustShown;
		if(illustShown){
			this.setVisibility(View.GONE);
		}else{
			refreshLanes();
		}
	}

	@Override
	public void onRoutePointKindChanged() {
		// TODO Auto-generated method stub
		
	}
}
