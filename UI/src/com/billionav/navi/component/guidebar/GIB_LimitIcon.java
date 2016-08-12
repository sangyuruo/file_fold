package com.billionav.navi.component.guidebar;

import com.billionav.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GIB_LimitIcon extends RelativeLayout{
	public static final int LIMIT_SHOW_STYLE_INVISIBLE = 0x100;
	public static final int LIMIT_SHOW_STYLE_CAMERA = 0x101;
	public static final int LIMIT_SHOW_STYLE_LIMIT_INFO = 0x102;
	
	private ImageView limitCamera;
	private TextView limitSpeed;
	public GIB_LimitIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	public GIB_LimitIcon(Context context) {
		super(context);
		initialize();
	}
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.guidebar_limit_icon, this);
	    
	    findViews();
		
	}
	private void findViews() {
		limitCamera = (ImageView)findViewById(R.id.gib_limit_icon_camera);
		limitSpeed = (TextView)findViewById(R.id.gib_limit_icon_limit_info);
		limitCamera.setVisibility(View.GONE);
		limitSpeed.setVisibility(View.GONE);
	}

	public void setShowStyle(int style) {
		setVisibility(View.VISIBLE);
		switch (style) {
		case LIMIT_SHOW_STYLE_INVISIBLE:
			setVisibility(View.GONE);
			break;
		case LIMIT_SHOW_STYLE_CAMERA:
			limitCamera.setVisibility(View.VISIBLE);
			limitSpeed.setVisibility(View.GONE);
			break;
		case LIMIT_SHOW_STYLE_LIMIT_INFO:
			limitCamera.setVisibility(View.GONE);
			limitSpeed.setVisibility(View.VISIBLE);
			break;
		default:
			break;

		}
	}
	public void setLimitSpeed(int speed){
		limitSpeed.setText(String.valueOf(speed));
	}

}
