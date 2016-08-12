package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.navi.component.basiccomponent.CheckBoxCollect;
import com.billionav.navi.component.basiccomponent.CheckBoxCollect.OnStateChangedListener;
import com.billionav.ui.R;

public class ListItemSearchResult extends ListItemInterface {

	private CheckBoxCollect checkboxCollect;
	private ImageView imageview02;
	private TextView textview01;
	private TextView textview02;
	private TextView textview03;

	public ListItemSearchResult(Context context, boolean ischeck, String name,
			String address, String distance, int icon2) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_guilde_dist, this);
		findViews();
		checkboxCollect.setChecked(ischeck);
		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(distance);
		checkboxCollect.setCheakImage(R.drawable.navicloud_and_568a, R.drawable.navicloud_and_568b);
		
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	public ListItemSearchResult(Context context,  String name,
			String address, String distance, int icon2) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_guilde_dist, this);
		findViews();
		checkboxCollect.setVisibility(View.GONE);
		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(distance);
		
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	public ListItemSearchResult(Context context, boolean ischeck, String name,
			String address, String distance, int icon2,int checkImgId,int notcheckedImgId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_guilde_dist, this);
		findViews();
		checkboxCollect.setChecked(ischeck);
		checkboxCollect.setClickable(ischeck);
		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(distance);
		checkboxCollect.setCheakImage(checkImgId, notcheckedImgId);
		setBackgroundResource(R.drawable.list_selector_background);
	}
	
	private void findViews() {
		checkboxCollect = (CheckBoxCollect) findViewById(R.id.list_Item_Guide_Dist_image01);
		imageview02 = (ImageView) findViewById(R.id.list_Item_Guide_Dist_image02);
		textview01 = (TextView) findViewById(R.id.list_Item_Guide_Dist_text01);
		textview02 = (TextView) findViewById(R.id.list_Item_Guide_Dist_text02);
		textview03 = (TextView) findViewById(R.id.list_Item_Guide_Dist_text03);
	}
	
	public void setRouteListener(OnClickListener l){
		imageview02.setOnClickListener(l);
	}
	
	public void setFavorivateListener(OnStateChangedListener l){
		checkboxCollect.setOnStateChangedListener(l);
	}
	
	public void setCheaked(boolean ischeaked){
		checkboxCollect.changeChecked(ischeaked);
	}
	
	public void setVisibility(){
		checkboxCollect.setVisibility(View.VISIBLE);
	}
	
	public void setIsEnabled(boolean enable){
		checkboxCollect.setIsEnabled(enable);
	}
	
	public void setClickListener(OnClickListener l){
		checkboxCollect.setOnClickListener(l);
	}
	
	public void setInfo(String name, String address, String distance) {
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(distance);
	}
	
	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		checkboxCollect.setTag(tag);
		imageview02.setTag(tag);
	}
	
}
