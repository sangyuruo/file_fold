package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemGuidePoi extends ListItemInterface {
	private ImageView imageview02;
	private TextView textview01;
	private TextView textview02;
	private TextView textview03;

	public ListItemGuidePoi(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListItemGuidePoi(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListItemGuidePoi(Context context, int icon1, String name,
			String address, String telephone, int icon2) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_guide_poi, this);
		findViews();

		imageview02.setImageResource(icon2);
		textview01.setText(name);
		textview02.setText(address);
		textview03.setText(telephone);
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
		imageview02 = (ImageView) findViewById(R.id.list_Item_Guide_Poi_image02);

		textview01 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text01);
		textview02 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text02);
		textview03 = (TextView) findViewById(R.id.list_Item_Guide_Poi_text03);
	}

}
