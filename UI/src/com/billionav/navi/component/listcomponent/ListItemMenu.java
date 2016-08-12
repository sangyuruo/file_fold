package com.billionav.navi.component.listcomponent;

import com.billionav.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemMenu extends ListItemInterface {
	private ImageView iv;
	private TextView tv;
	public ListItemMenu(Context context, int imageid, int nameId, OnClickListener l) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.list_item_menu, this);
	    
	    iv = (ImageView) findViewById(R.id.item_image);
	    tv = (TextView) findViewById(R.id.item_text);
	    iv.setImageResource(imageid);
	    iv.setBackgroundResource(0);
	    tv.setText(nameId);
	    
	    setOnClickListener(l);
	}

	public ListItemMenu(Context context, int imageid, String name) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.list_item_menu, this);
	    
	    iv = (ImageView) findViewById(R.id.item_image);
	    tv = (TextView) findViewById(R.id.item_text);
	    
	    iv.setImageResource(imageid);
	    iv.setBackgroundResource(0);
	    tv.setText(name);
	}
	
	public ListItemMenu(Context context, String name) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.list_item_menu, this);
	    
	    iv = (ImageView) findViewById(R.id.item_image);
	    tv = (TextView) findViewById(R.id.item_text);
	    iv.setBackgroundResource(0);
	    iv.setVisibility(View.GONE);
	    tv.setText(name);
		
	}

	public void setName(String name){
		tv.setText(name);
	}
	
	public void setImage(int imageid){
		iv.setImageResource(imageid);
	}


}
