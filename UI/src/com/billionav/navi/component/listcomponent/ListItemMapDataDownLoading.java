package com.billionav.navi.component.listcomponent;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.billionav.ui.R;

public class ListItemMapDataDownLoading extends ListItemInterface{
	private TextView placeText = null;
	private TextView sizeText = null;
	private TextView speedText = null;
	private TextView degreeText = null;
	private ProgressBar downloadBar = null;
	private Button pauseBtn = null;
	private Button cancelBtn = null;
	private DecimalFormat myFormatter = null;
	public ListItemMapDataDownLoading(Context context,String mapname,float mapsize,float downloadspeed,float downloadDegreeFigure,int downloadDegreetBar,View.OnClickListener pauseBtnonClickListener,View.OnClickListener cancelBtnonClickListener) {
		super(context);
		myFormatter = new DecimalFormat(".00"); 
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_map_data_downloading, this);
		findViews();
		placeText.setText(mapname);
		sizeText.setText(myFormatter.format(mapsize)+"");
		speedText.setText(myFormatter.format(downloadspeed)+"");
		degreeText.setText(myFormatter.format(downloadDegreeFigure)+"");
		downloadBar.setProgress(downloadDegreetBar);
		pauseBtn.setOnClickListener(pauseBtnonClickListener);
		cancelBtn.setOnClickListener(cancelBtnonClickListener);
	}
	public ListItemMapDataDownLoading(Context context,int mapnameId,float mapsize,float downloadspeed,float downloadDegreeFigure,int downloadDegreetBar,View.OnClickListener pauseBtnonClickListener,View.OnClickListener cancelBtnonClickListener) {
		super(context);
		myFormatter = new DecimalFormat(".00"); 
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_map_data_downloading, this);
		findViews();
		placeText.setText(mapnameId);
		sizeText.setText(myFormatter.format(mapsize)+"");
		speedText.setText(myFormatter.format(downloadspeed)+"");
		degreeText.setText(myFormatter.format(downloadDegreeFigure)+"");
		downloadBar.setProgress(downloadDegreetBar);
		pauseBtn.setOnClickListener(pauseBtnonClickListener);
		cancelBtn.setOnClickListener(cancelBtnonClickListener);
	}
	public ListItemMapDataDownLoading(Context context,int mapnameId,int mapsize,int downloadspeed,float downdegree) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_map_data_downloading, this);
		findViews();
	}
	private void findViews() {
		placeText = (TextView)findViewById(R.id.ListItemMapDataDownLoading_placetext);
		sizeText = (TextView)findViewById(R.id.ListItemMapDataDownLoading_sizetext);
		speedText = (TextView)findViewById(R.id.ListItemMapDataDownLoading_speedtext);
		degreeText = (TextView)findViewById(R.id.ListItemMapDataDownLoading_degreetext);
		pauseBtn = (Button)findViewById(R.id.ListItemMapDataDownLoading_pausebtn);
		cancelBtn = (Button)findViewById(R.id.ListItemMapDataDownLoading_cancelbtn);
		downloadBar = (ProgressBar)findViewById(R.id.ListItemMapDataDownLoading_downloadbar);
	}
	public void setPlacetext(String text) {
		placeText.setText(text);
	}
	public void setSizetext(int size) {
		sizeText.setText(myFormatter.format(size)+"");
	}
	public void setSpeedtext(int speed) {
		speedText.setText(myFormatter.format(speed)+"");
	}
	public void setDegreetext(float degree) {
		degreeText.setText(myFormatter.format(degree)+"");
	}
	public void setDownloadbar(int downloadDegreet) {
		downloadBar.setProgress(downloadDegreet);
	}
	public void setPausebtnOnClickListener(View.OnClickListener onClickListener) {
		pauseBtn.setOnClickListener(onClickListener);
	}
	public void setCancelbtnOnClickListener(View.OnClickListener onClickListener) {
		cancelBtn.setOnClickListener(onClickListener);
	}
}
