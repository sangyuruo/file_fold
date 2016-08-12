package com.billionav.navi.component.mapcomponent;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class AnimationControl{
	public static final int durationTime = 300;
	
	
	public static final int TURNING_TO_CLOSE = 1;
	public static final int TURNING_TO_OPEN = 2;
	public static final int OPENED = 3;
	public static final int CLOSED = 4;
	
	private static final float tanslateDistance = 1f;
	
	private View bottomView;
	
//	private View bottomLeft;
//	private View bottomRight;
	
//	private View referenceView;
	
	private int status = OPENED;
	
	public AnimationControl(View bottomView) {
		super();
		this.bottomView = bottomView;
//		this.bottomLeft = bottomLeft;
//		this.bottomRight = bottomRight;
//		this.referenceView = referenceView;
	}

	/**
	 * returns the status for animation
	 * @return One of the {@link #TURNING_TO_CLOSE}, {@link #TURNING_TO_OPEN}, {@link #OPENED}, or {@link #CLOSED}
	 * 
	 * */
	public int getStatus() {
		return status;
	}
		
	/**
	 * open the view with animation
	 * */
	public void openWithAnimation() {
		if(status != CLOSED) {
			return;
		}
		openBottomView();
//		openBottomSymbolView();
	}
	
	/**
	 * close the view with animation
	 * */
	public void closeWithAnimation() {
		if(status != OPENED) {
			return;
		}
		closeBottomView();
//		closeBottomSymbolView();
	}
	
	/**
	 * open the view without animation
	 * */
	public void openWithoutSysmbolMove() {
		if(status != CLOSED) {
			return;
		}
		openBottomView();
	}
	
	/**
	 * close the view without animation
	 * */
	public void closeWithoutSysmbolMove() {
		if(status != OPENED) {
			return;
		}
		closeBottomView();
	}
	
    public void openBottomView() {
    	Animation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, tanslateDistance, Animation.RELATIVE_TO_SELF, 0); 
    	translate.setDuration(durationTime);
    	translate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				status = TURNING_TO_OPEN;
				bottomView.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				status = OPENED;
			}
		});
    	bottomView.startAnimation(translate);
    }
    
    public void closeBottomView() {
    	Animation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,tanslateDistance); 
    	translate.setDuration(durationTime);
    	translate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				status = TURNING_TO_CLOSE;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bottomView.setVisibility(View.GONE);
				status = CLOSED;
			}
			
    	});
    	bottomView.startAnimation(translate);
    }
    
    public void closeButtonViewAtOnce(){
		bottomView.setVisibility(View.GONE);
		status = CLOSED;
    }
    
}