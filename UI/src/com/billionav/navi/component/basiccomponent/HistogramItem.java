package com.billionav.navi.component.basiccomponent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.billionav.ui.R;
import com.billionav.navi.component.DensityUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistogramItem extends RelativeLayout {

	private TextView scoreTxt;
	private TextView dateTxt;
	private TextView distanceTxt;
	private ImageView imageView;
	private SimpleDateFormat sd;
	private final int ImageViewMaxHeight = DensityUtil.dp2px(getContext(), 112);
	public HistogramItem(Context context){
		super(context);
		init();
	}
	
	public HistogramItem(Context context ,AttributeSet attr){
		super(context, attr);
		init();
		
	}
	
	
	public void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.histogram_item, this);
		
		scoreTxt = (TextView)findViewById(R.id.sore_txt);
//		dateTxt = (TextView)findViewById(R.id.date);
//		distanceTxt = (TextView)findViewById(R.id.distance);
		imageView = (ImageView)findViewById(R.id.histo);
		
		sd =new SimpleDateFormat("MM-dd");
		registEvent();
	}
	
	private void registEvent(){
		
	}
	public void update(String distance ,String date,int score){
		distanceTxt.setText(distance);
		dateTxt.setText(date);
		scoreTxt.setText(getContext().getString(R.string.STR_MM_10_02_01_03, score));
		int height = score*ImageViewMaxHeight/100;
		
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
		lp.height = height;
		
		imageView.setLayoutParams(lp);
	}
	
	public void update(float distance ,Date date,int score){
		
		String disStr = new DecimalFormat("######.##").format(distance/1000)+"km";
		String dateStr = sd.format(date);
		
		update(disStr, dateStr, score);
	}
	public void update(float distance ,int[] dates,int score){
		String disStr = new DecimalFormat("######.##").format(distance/1000)+"km";
		String dateStr = String.format("%d/%d",dates[1],dates[0]);
		
		update(disStr, dateStr, score);
	}
	
	public void setDateTxt(TextView dateTxt) {
		this.dateTxt = dateTxt;
	}

	public void setDistanceTxt(TextView distanceTxt) {
		this.distanceTxt = distanceTxt;
	}
}
