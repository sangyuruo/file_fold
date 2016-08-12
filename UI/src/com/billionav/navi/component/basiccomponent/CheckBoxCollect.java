package com.billionav.navi.component.basiccomponent;

import com.billionav.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class CheckBoxCollect extends ImageView {

	private boolean isEnabled = true;
	private boolean isChecked = false;
	private int checkedImgId =  R.drawable.navicloud_and_568a;
	private int notcheckedImgId =  R.drawable.navicloud_and_568b;
	private OnStateChangedListener stateChangedListener;
	
	private OnClickListener l;
	
	public CheckBoxCollect(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setImageResource(notcheckedImgId);
		super.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isEnabled){
					setChecked(!isChecked);
				}
				if(l != null) {
					l.onClick(v);
				}
			}
		});
	}
	
	public void setCheakImage(int checkedImgId, int notcheckedImgId){
		this.checkedImgId = checkedImgId;
		this.notcheckedImgId = notcheckedImgId;
		
		if(isChecked) {
			setImageResource(checkedImgId);
		} else {
			setImageResource(notcheckedImgId);
		}

	}
	
	public final void setChecked(boolean checked){
		if(isChecked == checked){
			return;
		}
		
		isChecked = checked;
		if(checked) {
			setImageResource(checkedImgId);
		} else {
			setImageResource(notcheckedImgId);
		}
		
		if(stateChangedListener != null) {
			stateChangedListener.onStateChange(isChecked);
		}
	}
	
	public final void changeChecked(boolean checked){
		if(isChecked == checked){
			return;
		}
		isChecked = checked;
		if(checked) {
			setImageResource(checkedImgId);
		} else {
			setImageResource(notcheckedImgId);
		}
	}
	
	public final void setIsEnabled(boolean enable){
		if(isEnabled != enable){
			isEnabled = enable;
		}
	}
	
	public final boolean isChecked(){
		return isChecked;
	}
	
	public final void setOnStateChangedListener(OnStateChangedListener l) {
		this.stateChangedListener = l;
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	
	public interface OnStateChangedListener{
		void onStateChange(boolean newState);
	}
}
