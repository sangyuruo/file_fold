package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemWayPoint  extends ListItemInterface{

	private ImageView menu2ImageView = null;
	private TextView menu2TextViewabove = null;
	private TextView menu2TextViewbelow = null;
	
	public ListItemWayPoint(Context context, int imageicon, String info,String distance) {
		super(context);
		findViews(context);
		
		menu2ImageView.setImageResource(imageicon);
		menu2TextViewabove.setText(info);
		menu2TextViewbelow.setText(distance);
	}

	public ListItemWayPoint(Context context, int imageicon, int nameId, int disID) {
		super(context);
		findViews(context);
		
		menu2ImageView.setImageResource(imageicon);
		menu2TextViewabove.setText(nameId);
	}
	
	private void findViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View menuview = inflater.inflate(R.layout.list_item_waypoint, this);
		menu2ImageView = (ImageView)menuview.findViewById(R.id.li_wp_turningicon);
		menu2TextViewabove = (TextView)menuview.findViewById(R.id.li_wp_detail);
		menu2TextViewbelow = (TextView)menuview.findViewById(R.id.li_wp_distance);
	}
	
	
	
}
