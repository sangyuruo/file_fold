package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.billionav.navi.component.basiccomponent.DateTimeButton;
import com.billionav.ui.R;

public class ListItemScheduleResult extends ListItemInterface {

	private ImageView imageview02;
//	private DateTimeButton imageview02;
	private TextView textview01;
	private TextView textview02;
	private TextView textview03;

	public ListItemScheduleResult(Context context, boolean ischeck, String name,
			String arrivalTime, String expectTime ,int icon2) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_schedule_dist, this);
		findViews();
		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(expectTime);
		textview03.setText(arrivalTime);
		
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	public ListItemScheduleResult(Context context,  String name,
			String arrivalTime, String expectTime, int icon2) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_schedule_dist, this);
		findViews();
		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(expectTime);
		textview03.setText(arrivalTime);
		
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	public ListItemScheduleResult(Context context, boolean ischeck, String name,
			String arrivalTime, String expectTime, int icon2,int checkImgId,int notcheckedImgId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_schedule_dist, this);
		findViews();
		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(expectTime);
		textview03.setText(arrivalTime);
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	private void findViews() {
		imageview02 = (ImageView) findViewById(R.id.list_Item_Schedule_Dist_image);
		textview01 = (TextView) findViewById(R.id.list_Item_Schedule_Point_name);
		textview02 = (TextView) findViewById(R.id.list_Item_Schedule_expected_time);
		textview03 = (TextView) findViewById(R.id.list_Item_Schedule_Point_time);
	}
	
	public void setClockListener(OnClickListener l){
		imageview02.setOnClickListener(l);
	}
	
	public void setInfo(String name, String arrivalTime ,String expectTime) {
		textview01.setText(name);
		textview02.setText(expectTime);
		textview03.setText(arrivalTime);
	}
	
	public void setTextColor(boolean isVaild){
		if(isVaild){			
			textview02.setTextColor(getResources().getColor(R.color.list_item_sub_text_color));
		}else{
			textview02.setTextColor(Color.GRAY);
		}
	}
	
	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		imageview02.setTag(tag);
	}
	
}
