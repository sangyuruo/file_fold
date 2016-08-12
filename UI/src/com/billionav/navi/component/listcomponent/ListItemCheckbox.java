package com.billionav.navi.component.listcomponent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemCheckbox extends ListItemInterface{
	private ImageView menuImageView = null;
	private TextView menuTextView = null;
	private ImageView menuCheckBox = null;
	private boolean isChecked = false;
	public ListItemCheckbox(Context context,int imageId,String itemName,boolean isChecked) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_check_box, this);
		findViews();
		menuImageView.setImageResource(imageId);
		menuTextView.setText(itemName);
		this.isChecked = isChecked;
		setCheckboxSelecet();
	}

	public ListItemCheckbox(Context context,int imageId, int itemNameId,boolean isChecked) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_check_box, this);
		findViews();
		menuImageView.setImageResource(imageId);
		menuTextView.setText(itemNameId);
		this.isChecked = isChecked;
		setCheckboxSelecet();
	}

	private void findViews() {
		menuImageView = (ImageView)findViewById(R.id.menu1_imageView);
		menuTextView = (TextView)findViewById(R.id.menu1_textView);
		menuCheckBox = (ImageView)findViewById(R.id.meuu1_checkBox);
	}

	private void setCheckboxSelecet() {
		if (isChecked) {
			menuCheckBox.setImageResource(R.drawable.navicloud_and_506a);
		} else {
			menuCheckBox.setImageResource(R.drawable.navicloud_and_505a);
		}
	}
	
	public void setChecked(boolean checked) {
		this.isChecked = checked;
		setCheckboxSelecet();
		super.notifyItemClick();
	}
	
	public boolean isChecked(){
		return isChecked;
	}
	public void setIconIsShow(boolean isShow){
		if(isShow){
			this.menuImageView.setVisibility(View.VISIBLE);
		}else{
			this.menuImageView.setVisibility(View.GONE);
		}
		
	}
	@Override
	protected void notifyItemClick() {
		isChecked = !isChecked;
		setCheckboxSelecet();
		super.notifyItemClick();
	}
}
