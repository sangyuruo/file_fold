package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.ui.R;


public class ButtonMapZoom extends RelativeLayout {
	private ImageView zoomIn;
	private ImageView zoomOut;
	
	private int status = AnimationControl.OPENED;
	
	private OnClickListener onClickListener;

	public ButtonMapZoom(Context context) {
		super(context);
		initialize();
	}

	public ButtonMapZoom(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.button_map_zoom, this);
		
	    zoomIn = (ImageView) findViewById(R.id.map_button_zoom_in);
	    zoomOut = (ImageView) findViewById(R.id.map_button_zoom_out);
//	    Drawable backgroundDrawable = ViewHelp.createDrawableListByImageID(getContext(), R.drawable.navicloud_and_773a, R.drawable.navicloud_and_773c, -1, -1, R.drawable.navicloud_and_773b);
//	    Drawable backgroundDrawable2 = ViewHelp.createDrawableListByImageID(getContext(), R.drawable.navicloud_and_773a, R.drawable.navicloud_and_773c, -1, -1, R.drawable.navicloud_and_773b);
//	    zoomIn.setBackgroundDrawable(backgroundDrawable);
//	    zoomOut.setBackgroundDrawable(backgroundDrawable2);
	    if(!isInEditMode()) {
		    updateButtonStatus();
	    }
	    setListener();
	}

	private void setListener() {
		zoomIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				UIMapControlJNI.ScaleUpDown(true);
				if(onClickListener != null){
					onClickListener.onClick(v);
				}
			}
		});
		
		zoomOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				UIMapControlJNI.ScaleUpDown(false);
				if(onClickListener != null){
					onClickListener.onClick(v);
				}
			}
		});
		
	}
	
	public void setZoomButtonClickListener(OnClickListener l) {
		onClickListener = l;
	}
	
	public final void updateButtonStatus() {
		updateButtonStatus(getScale());
	}
	
	public final void updateButtonStatus(int scale){
		if(isMinLevel(scale)){
			zoomIn.setEnabled(true);
			zoomOut.setEnabled(false);
		} else if(isMaxLevel(scale)){
			zoomIn.setEnabled(false);
			zoomOut.setEnabled(true);
		} else {
			zoomIn.setEnabled(true);
			zoomOut.setEnabled(true);
		}
	}
	
	public int getStatus() {
		return status;
	}
	
	public void open() {
    	Animation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0); 
    	translate.setDuration(AnimationControl.durationTime);
    	translate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				status = AnimationControl.TURNING_TO_OPEN;
				setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				status = AnimationControl.OPENED;
			}
		});
    	startAnimation(translate);
    }
    
    public void close() {
    	Animation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0); 
    	translate.setDuration(AnimationControl.durationTime);
    	translate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				status = AnimationControl.TURNING_TO_CLOSE;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setVisibility(View.GONE);
				status = AnimationControl.CLOSED;
			}
    	});
    	startAnimation(translate);
    }


	private boolean isMinLevel(int scale) {
		return (scale <= UIMapControlJNI.GetMinScale());
	}
//	
	private boolean isMaxLevel(int scale) {
		return (scale >= UIMapControlJNI.GetMaxScale());
	}
	
	private int getScale() {
//		long height = UIMapControlJNI.GetHeight();
		return (int)UIMapControlJNI.GetCurrentScale();
	}
	
    public void closeZoomAtOnce(){
		setVisibility(View.GONE);
		status = AnimationControl.CLOSED;
    }

}
