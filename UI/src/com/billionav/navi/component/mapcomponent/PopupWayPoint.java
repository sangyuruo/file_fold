package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;

public class PopupWayPoint extends RelativeLayout{

	private final TextView			info;
	
	public PopupWayPoint(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.waypoint_popup, this);
	    info = (TextView) findViewById(R.id.popup_wp_info);
	}
	
	public void setInfoText(String pointInfo){
		info.setText(pointInfo);
	}
	

}

