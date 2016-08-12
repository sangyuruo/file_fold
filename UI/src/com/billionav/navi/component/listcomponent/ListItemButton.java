package com.billionav.navi.component.listcomponent;

import com.billionav.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;

public class ListItemButton extends ListItemInterface{

	private Button cancelButton = null;
	private Button confirmButton = null;
	public ListItemButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListItemButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListItemButton(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_button, this);
		
		cancelButton = (Button) findViewById(R.id.list_item_button_01);
		confirmButton = (Button) findViewById(R.id.list_item_button_02);
	}

}
