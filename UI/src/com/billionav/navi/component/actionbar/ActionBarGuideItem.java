package com.billionav.navi.component.actionbar;

import com.billionav.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBarGuideItem extends RelativeLayout {
	
	private TextView textView1;
	private TextView textView2;

	public ActionBarGuideItem(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.actionbar_guide_item, this);
	    findViews();
		
	    setListener();
	}

	private void findViews() {
		textView1 = (TextView) findViewById(R.id.action_bar_guide_item_text1);
		textView2 = (TextView) findViewById(R.id.action_bar_guide_item_text2);
	}

	private void setListener() {
	}
	
	public void setText(String text){
		textView1.setText(text);
	}
	
	public void setSubText(String text){
		textView2.setText(text);
	}

}
