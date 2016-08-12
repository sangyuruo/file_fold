package com.billionav.navi.component.listcomponent;

import com.billionav.navi.component.DensityUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CheckedRelativeLayout extends RelativeLayout implements Checkable{
	
	private boolean mChecked;
	
	private final CheckedImage imageView;
	
	public CheckedRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		imageView = new CheckedImage(context);
		addView(imageView);
		
		RelativeLayout.LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.rightMargin = DensityUtil.dp2px(context, 8);
		
	}
	
	public final void setImageResouce(int normal, int checked) {
		imageView.setImageResouce(normal, checked);
	}


	@Override
	public void setChecked(boolean checked) {
		mChecked = checked;
		imageView.setChecked(mChecked);
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}
	
	class CheckedImage extends ImageView implements Checkable {

		private boolean mChecked;
		private int normalImageId;
		private int checkedImage;
		
		public CheckedImage(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public CheckedImage(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public CheckedImage(Context context) {
			super(context);
		}
		
		public void setImageResouce(int normal, int checked) {
			normalImageId = normal;
			checkedImage = checked;
            if(mChecked) {
            	setImageResource(checkedImage);
            } else {
            	setImageResource(normalImageId);
            }
		}

		@Override
		public void setChecked(boolean checked) {
	        if (mChecked != checked) {
	            mChecked = checked;
	            if(mChecked) {
	            	setImageResource(checkedImage);
	            } else {
	            	setImageResource(normalImageId);
	            }
	        }
		}

		@Override
		public boolean isChecked() {
			return mChecked;
		}

		@Override
		public void toggle() {
			setChecked(!mChecked);
		}
		
	}

}

