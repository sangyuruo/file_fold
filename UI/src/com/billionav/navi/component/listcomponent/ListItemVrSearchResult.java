package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billionav.navi.component.basiccomponent.CheckBoxCollect;
import com.billionav.navi.component.basiccomponent.CheckBoxCollect.OnStateChangedListener;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.ui.R;

public class ListItemVrSearchResult extends LinearLayout {

	private TextView textview01;
	private TextView textview02;
	private TextView textview03;

	public ListItemVrSearchResult(Context context,  String name,
			String address, int distance) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.adt_voice_search_result_item, this);
		findViews();

		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(RouteTool.getDisplayDistance(distance));
		
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	private void findViews() {
		textview01 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text01);
		textview02 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text02);
		textview03 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text03);
	}
	
	public void setInfo(String name, String address, String distance) {
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(distance);
	}
	
	
}
