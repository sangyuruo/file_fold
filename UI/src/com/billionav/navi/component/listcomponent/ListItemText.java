package com.billionav.navi.component.listcomponent;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemText extends ListItemInterface{
	private ImageView menu2ImageView = null;
	private TextView menu2TextView = null;
	public ListItemText(Context context, int imageicon, String itemName) {
		super(context);
		findViews(context);
		
		menu2ImageView.setImageResource(imageicon);
		menu2TextView.setText(itemName);
	}

	public ListItemText(Context context, int imageicon, int nameId) {
		super(context);
		findViews(context);	
		menu2ImageView.setImageResource(imageicon);
		menu2TextView.setText(nameId);
	}
	public void setImageIsShow(boolean isShow){
		if(isShow){
		  menu2ImageView.setVisibility(View.VISIBLE);
		}else{
			menu2ImageView.setVisibility(View.GONE);
		}
	}
	public void setItemText(int nameId){
		menu2TextView.setText(nameId);
	}
	public void setItemText(String itemName){
		menu2TextView.setText(itemName);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		menu2TextView.setEnabled(enabled);
		menu2ImageView.setEnabled(enabled);
	}
	
	public void setItemImage(int resId) {
		menu2ImageView.setImageResource(resId);
	}
	
	private void findViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View menuview = inflater.inflate(R.layout.list_item_text, this);
		menu2ImageView = (ImageView)menuview.findViewById(R.id.menu2_imageView);
		menu2TextView = (TextView)menuview.findViewById(R.id.menu2_textView);
	}
	


}
