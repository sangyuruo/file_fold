package com.billionav.navi.component.listcomponent;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;
public class Favorite_Item extends RelativeLayout {
	private ImageView imageview;
	private TextView textview01;
	private TextView textview02;
	private TextView textview03;
	private int index;
	public Favorite_Item(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Favorite_Item(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Favorite_Item(Context context, String name,
			String address, String telephone, int index ,int imageId,OnClickListener moveIconListener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_guide_poi, this);
		findViews();
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(telephone);
		this.index = index;
		imageview.setImageResource(imageId);
		imageview.setOnClickListener(moveIconListener);
	}
	
	public void setImage(int imageId){
		imageview.setImageResource(imageId);
	}
	
	public void setComputeBtnOnClickListener(OnClickListener moveIconListener)
	{
		imageview.setOnClickListener(moveIconListener);
	}
	public void setTextview01(String nameStr) {
		this.textview01.setText(nameStr);
	}


	public void setTextview02(String addressStr) {
		this.textview02.setText(addressStr);
	}

	public void setTextview03(String telStr) {
		this.textview03.setText(telStr);
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
	private void findViews() {
		imageview = (ImageView) findViewById(R.id.list_Item_Guide_Poi_image02);
		textview01 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text01);
		textview02 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text02);
		textview03 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text03);
	}

	
}
