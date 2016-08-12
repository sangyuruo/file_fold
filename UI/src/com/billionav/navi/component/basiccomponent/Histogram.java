package com.billionav.navi.component.basiccomponent;

import com.billionav.ui.R;
import com.billionav.navi.component.DensityUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Histogram extends LinearLayout {

	private final int size ;
	private HistogramItem[] items;
	public Histogram(Context context,int num){
		super(context);
		this.size = num;
		init();
	}
	
	public Histogram(Context context ,AttributeSet attr,int num){
		super(context, attr);
		this.size = num;
		init();
		
	}
		
	private void init(){
		this.setOrientation(LinearLayout.VERTICAL);
		
		// 
		LinearLayout dates = new LinearLayout(getContext());
		dates.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout distances = new LinearLayout(getContext());
		distances.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout histos   =  new LinearLayout(getContext());
		histos.setOrientation(LinearLayout.HORIZONTAL);
		
		items = new HistogramItem[size];
		for(int i =0;i<size;i++){
			HistogramItem hist = new HistogramItem(getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,DensityUtil.dp2px(getContext(), 140.5f));
			lp.weight =1;
			
			items[i] = hist;
			histos.addView(hist, lp);

			
			//dates add date text label
			TextView date = new TextView(getContext());
			date.setGravity(Gravity.CENTER);
			date.setTextSize(12);
			date.setTextColor(Color.WHITE);
//			date.setBackgroundColor(Color.RED);
			LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			dlp.weight =1;
			dates.addView(date, dlp);
			
			hist.setDateTxt(date);
			//distances add text view 
			TextView distance = new TextView(getContext());
			distance.setGravity(Gravity.CENTER);
			distance.setTextSize(12);
			distance.setTextColor(Color.rgb(110, 110, 110));
			LinearLayout.LayoutParams distanceLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			distanceLp.weight = 1;
			distances.addView(distance, distanceLp);
			hist.setDistanceTxt(distance);
			
		}
		
		LinearLayout.LayoutParams histosLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		histosLp.setMargins(DensityUtil.dp2px(getContext(), 8),0, DensityUtil.dp2px(getContext(), 8),0);
		this.addView(histos,histosLp);
		
		LinearLayout.LayoutParams datesLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		datesLp.setMargins(DensityUtil.dp2px(getContext(), 8),DensityUtil.dp2px(getContext(), 4), DensityUtil.dp2px(getContext(), 8),DensityUtil.dp2px(getContext(), 6));
		dates.setBackgroundResource(R.drawable.navicloud_and_526a);
		
		this.addView(dates,datesLp);
	
		LinearLayout.LayoutParams distancesLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		distancesLp.setMargins(DensityUtil.dp2px(getContext(), 8),0, DensityUtil.dp2px(getContext(), 8),DensityUtil.dp2px(getContext(), 4));

		this.addView(distances,distancesLp);
		
//		this.setBackgroundResource(R.drawable.navicloud_and_727a);
	}
	
	public  HistogramItem[] getItems(){
		return items;
	}
}
