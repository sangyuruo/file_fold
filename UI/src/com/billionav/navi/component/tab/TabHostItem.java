package com.billionav.navi.component.tab;
import com.billionav.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabHostItem extends RelativeLayout{
	private boolean isSelected = false;
	private ImageView imageView = null;
	private TextView textView = null;
	private int pressimageid = 0;
	private int normalbackgroundId = 0;
	
	public TabHostItem(Context context, int imageId, int nameId)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_host_item, this);
		findViews();
		textView.setText(nameId);
		imageView.setImageResource(imageId);
	}

	public TabHostItem(Context context,int imageId)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_host_item, this);
		findViews();
		imageView.setImageResource(imageId);
	}
	
	public TabHostItem(Context context, int imageId, int nameId,int normalbackgroundId ,int pressedbackgroundId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_host_item, this);
		findViews();
		textView.setText(nameId);
		imageView.setImageResource(imageId);
		pressimageid = pressedbackgroundId;
		this.normalbackgroundId = normalbackgroundId;
		setBackgroundResource(normalbackgroundId);
		if(imageId == 0) {{
			imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		}
        }
	}
	
	public TabHostItem(Context context,int imageId,int normalbackgroundId ,int pressedbackgroundId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_host_item, this);
		findViews();
		textView.setText("");
		imageView.setImageResource(imageId);
		pressimageid = pressedbackgroundId;
		this.normalbackgroundId = normalbackgroundId;
		setBackgroundResource(normalbackgroundId);
		if(imageId == 0) {
			imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
	}
	
	private void findViews(){
		textView = (TextView)findViewById(R.id.tab_host_item_text);
		imageView = (ImageView)findViewById(R.id.tab_host_item_img);
	}
	
	public void setItemSelected(boolean selected){
		textView.setSelected(selected);
		isSelected = selected;
		setBackgroundResource(isSelected ? pressimageid : normalbackgroundId);
	}
	
	public boolean isItemSelected(){
		return isSelected;
	}
}
