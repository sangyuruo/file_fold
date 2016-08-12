package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;

public class Favorite_Delete_Item extends RelativeLayout{
	private ImageView cheakboxImage;
	private TextView textview01;
	private TextView textview02;
	private TextView textview03;
	private boolean ischeaked;
	private int index;
	public Favorite_Delete_Item(Context context, String name,String address, String telephone, boolean isdelete, int index ) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.favorite_delete_item, this);
		findViews();
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(telephone);
		this.index = index;
		setCheaked(isdelete);
		cheakboxImage.setEnabled(false);
		cheakboxImage.setFocusable(false);
	}
	private void findViews() {
		cheakboxImage = (ImageView) findViewById(R.id.list_Item_favorite_CheckBoxImage);
		textview01 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text01);
		textview02 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text02);
		textview03 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text03);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}
	public void setName(String name) {
		textview01.setText(name);
	}

	public void setAddress(String address) {
		textview02.setText(address);
	}

	public void setTelephoneNumber(String telephoneNum) {
		textview03.setText(telephoneNum);
	}
	public void setCheaked(boolean ischeaked){
		this.ischeaked = ischeaked;
		if(ischeaked){
		cheakboxImage.setImageResource(R.drawable.navicloud_and_506a);
		}
		else{
		cheakboxImage.setImageResource(R.drawable.navicloud_and_505a);
		}
	}
	public boolean getCheaked(){
		return ischeaked;
	}
}
