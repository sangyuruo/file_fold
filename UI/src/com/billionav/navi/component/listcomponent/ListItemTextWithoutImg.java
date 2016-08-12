package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemTextWithoutImg extends ListItemInterface{
	private final TextView text;

	public ListItemTextWithoutImg(Context context,String str) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View menuview = inflater.inflate(R.layout.list_item_text_without_img, this);
		text = (TextView)menuview.findViewById(R.id.ListItemTextWithoutImgLayout_Text);
		text.setText(str);
	}
	public ListItemTextWithoutImg(Context context,int strId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View menuview = inflater.inflate(R.layout.list_item_text_without_img, this);
		text = (TextView)menuview.findViewById(R.id.ListItemTextWithoutImgLayout_Text);
		text.setText(strId);
	}
	public void setText(String textdata) {
		text.setText(textdata);
	}
	public String getTextName()
	{
		return text.getText().toString();
	}
	
}
