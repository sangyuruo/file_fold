package com.billionav.navi.component.listcomponent;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemDeleteDownloadMap extends ListItemInterface{
	private TextView textplace;
	private TextView textsize;
	private boolean ischeaked;
	public ListItemDeleteDownloadMap(Context context,boolean ischeck,String name,int size) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_delete_downloadmap, this);
		findViews();
		textsize.setText(new DecimalFormat("#########.##").format((double)size/1024d/1024d)+"M");
		textplace.setText(name);
	}
	private void findViews(){
		textplace = (TextView)findViewById(R.id.textName);
		textsize = (TextView)findViewById(R.id.textSize);
	}
	public void setPackageSize(int size){
		textsize.setText(new DecimalFormat("#########.##").format((double)size/1024d/1024d)+"M");
	}
	public void setPlaceName(String name){
		textplace.setText(name);
	}
	public boolean getCheacked(){
		return ischeaked;
	}
	public String getPlacename(){
		return textplace.getText().toString();
	}
}
