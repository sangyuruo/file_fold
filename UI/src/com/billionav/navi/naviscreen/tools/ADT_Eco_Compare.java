package com.billionav.navi.naviscreen.tools;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.basiccomponent.Histogram;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class ADT_Eco_Compare extends ActivityBase {
	private final static int Timer_Trigger_Msg = 1;
	private static final int Timer_Trigger = 1000;
	private  final static int Timer_Trigger_LayOut_Mgs =2;

	
	private ListViewNavi listView;
	private LinearLayout contain;

	private float[] lastTextValues ;
	private float[] textValues ;

	private String[] tempTexts ;
	private final String[] textValuesUnit = new String[]{"km/L","%","%","%"};
	private BaseAdapter adapter;
	private Handler handler;
	private Histogram histogram;
	private LinearLayout header;
	private RelativeLayout scroll;
//	private final jniEcoControl ecoControl = new jniEcoControl();
//	int size = ecoControl.GetHistoryRecordNum();
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.scr_eco_compare);
	

		tempTexts = new String[]{getString(R.string.STR_MM_10_02_02_20),getString(R.string.STR_MM_10_02_02_21),getString(R.string.STR_MM_10_02_02_22),getString(R.string.STR_MM_10_02_02_23)};
		setDefaultBackground();
		contain = (LinearLayout)findViewById(R.id.linear);
		scroll = (RelativeLayout)findViewById(R.id.scroll);
		setTitle(R.string.STR_MM_10_02_01_09);
		
		listView = (ListViewNavi)findViewById(R.id.eco_list);
		
		initHeaderView();
		
		initData();
		
		adapter =new EcoCompareAdapter(getApplicationContext());
		listView.setAdapter(adapter);
		registerEvent();
	}
	
	
	private void initData(){
		updateHistogram();
		updateData(false);
		updateLastData();
		ResizeLayout() ;
	}
	
	private void initHeaderView(){
		
		header = new LinearLayout(getApplicationContext());
		header.setLayoutParams( new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		header.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams hlp =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		hlp.leftMargin=hlp.rightMargin=hlp.topMargin = DensityUtil.dp2px(getApplicationContext(), 8);
//		histogram = new Histogram(getApplicationContext(),size);
		header.addView(histogram, hlp);
		
		

		TextView txtView = new TextView(this);
	
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,DensityUtil.dp2px(getApplicationContext(), 40));
		lp.leftMargin = DensityUtil.dp2px(getApplicationContext(), 18.6f);
		header.addView(txtView,lp);	
	
		contain.addView(header, 0);	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		ResizeLayout() ;
		
	}
	
	private void ResizeLayout() {
		ViewGroup.LayoutParams currentTimeLp = header.getLayoutParams();
		if(ScreenMeasure.isPortrait()){
			contain.setOrientation(LinearLayout.VERTICAL);
			currentTimeLp.width = ScreenMeasure.getWidth();
		}else{
			contain.setOrientation(LinearLayout.HORIZONTAL);
			currentTimeLp.width = (int)(ScreenMeasure.getWidth()*0.4);
		}
		header.setLayoutParams(currentTimeLp);
		
	}


	public void changeLayout(){
		ViewGroup.LayoutParams  listLp = listView.getLayoutParams();
		int windowHeigth = getWindowManager().getDefaultDisplay().getHeight();
		int windowWidth  = getWindowManager().getDefaultDisplay().getWidth();
		int scrollheight =  scroll.getWidth()+scroll.getHeight()-windowWidth;
		if(scrollheight >  windowHeigth){
			scrollheight = scroll.getHeight();
		}
		int headerHeight = header.getHeight();

		int restHeight =  scrollheight - headerHeight;
		int listHeigth = (tempTexts.length+1)*DensityUtil.dp2px(this, 40);
		int listbottomMargin = DensityUtil.dp2px(this, 8);
		if(restHeight < listHeigth+ listbottomMargin){
			listLp.height = listHeigth ;
		}else{
			listLp.height = restHeight - listbottomMargin ;
		}
		listView.setLayoutParams(listLp);
	}
	
	
	@Override
	protected void OnResume() {
		super.OnResume();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onTimerTrigger();

			}
		}, Timer_Trigger);
	}
	
	private void registerEvent(){
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Timer_Trigger_Msg:
					onTimerTrigger();
					break;
				case Timer_Trigger_LayOut_Mgs:
					changeLayout();
					break;

				default:
					break;
				}
			}
		};
		
		handler.sendMessageDelayed(handler.obtainMessage(Timer_Trigger_LayOut_Mgs), 10);
	}
	
	private void updateHistogram(){
//		for(int i=0;i<size;i++){
//			int[] md = ecoControl.GetHistoryDate(i);
//			histogram.getItems()[i].update(ecoControl.GetHistoryMiles(i), md, getScore(ecoControl.GetHistoryEStartRate(i)*100, ecoControl.GetHistoryConstantSpeedRate(i)*100, ecoControl.GetHistoryIdleRate(i))*100);
//		}
		
	}
	private int getScore(float eStart,float ceStart,float idleStart){
		if(eStart > 50){
			eStart = 50;
		}
		if(ceStart > 60){
			ceStart = 60;
		}
		int sore = (int)(0.8f*eStart + 0.66f*ceStart+(0.2f*(100-idleStart)));
		return sore;
	}
	private void updateLastData(){
		lastTextValues = new float[4];
//		float avgOilSpend = ecoControl.GetHistoryAvgFuelConsumption(0);
//		float eStart = ecoControl.GetHistoryEStartRate(0);
//		if(eStart < 0) {eStart = 0;
//        }
//		if(eStart > 1) {eStart = 1;
//        }
//		
//		float ceStart = ecoControl.GetHistoryConstantSpeedRate(0);
//		if(ceStart < 0) {ceStart = 0;
//        }
//		if(ceStart > 1) {ceStart = 1;
//        }
//		
//		float idleStart = ecoControl.GetHistoryIdleRate(0);
//		if(idleStart < 0) {idleStart = 0;
//        }
//		if(idleStart > 1) {idleStart = 1;
//        }
//		
//		lastTextValues[0] = (float) (((avgOilSpend*10+0.5))/10);
//		lastTextValues[1] = (int)(eStart*100) ;
//		lastTextValues[2] = (int)(ceStart*100) ;
//		lastTextValues[3] = (int)(idleStart*100);
	}
	
	//update list data
	private void updateData(boolean isNotify){
		
//		if(textValues == null){
//			textValues = new float[4];
//		}
//		
//		float avgOilSpend = ecoControl.GetAvgFuelConsumption();
//		float eStart = ecoControl.GetEStartRate();
//		if(eStart < 0) {eStart = 0;
//        }
//		if(eStart > 1) {eStart = 1;
//        }
//		
//		float ceStart = ecoControl.GetConstantSpeedRate();
//		if(ceStart < 0) {ceStart = 0;
//        }
//		if(ceStart > 1) {ceStart = 1;
//        }
//		
//		float idleStart = ecoControl.GetIdleRate();
//		if(idleStart < 0) {idleStart = 0;
//        }
//		if(idleStart > 1) {idleStart = 1;
//        }
//		textValues[0] = (float) (((avgOilSpend*10+0.5))/10.0);
//		textValues[0] = (float)Float.valueOf(new DecimalFormat("#########.##").format(avgOilSpend));
//		textValues[1] = (int)(eStart*100);
//		textValues[2] = (int)(ceStart*100);
//		textValues[3] = (int)(idleStart*100);
//		
//		if(isNotify){
//			adapter.notifyDataSetChanged();
//		}
	}
	
	private void onTimerTrigger(){
		updateData(true);
		handler.sendMessageDelayed(handler.obtainMessage(Timer_Trigger_Msg),Timer_Trigger );
	}
	
	

	
	@Override
	protected void OnPause() {

		handler.removeMessages(Timer_Trigger_Msg);
		super.OnPause();

	}
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}
	
	public static class ViewHolder {
		TextView txtView01;
		TextView txtView02;
		TextView txtView03;
		ImageView compResult;
	}

	public class EcoCompareAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public EcoCompareAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return tempTexts.length;
		}

		@Override
		public Object getItem(int position) {
			return 0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		
		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item_txt3, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.txtView02 = (TextView) convertView
						.findViewById(R.id.list_txt01);
				viewHolder.txtView01 = (TextView) convertView
						.findViewById(R.id.list_txt02);
				viewHolder.txtView03 = (TextView) convertView
						.findViewById(R.id.list_txt03);
				viewHolder.compResult = (ImageView) convertView.findViewById(R.id.list_compare_result_arror);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
				DecimalFormat df = new DecimalFormat("#########");
				if(position == 1){
					df = new DecimalFormat("#########.#");
				}
				viewHolder.compResult.setVisibility(View.VISIBLE);
				viewHolder.txtView01.setText(tempTexts[position ]);
				viewHolder.txtView02.setText(df.format(lastTextValues[position ])+textValuesUnit[position]);
				viewHolder.txtView03.setText(df.format(textValues[position ])+textValuesUnit[position]);
				viewHolder.compResult.setImageResource(getResultPicRes(position));
				viewHolder.txtView02.setTextColor(Color.rgb(51, 102, 153));
				viewHolder.txtView02.setTextSize(14.6f);
				viewHolder.txtView03.setTextColor(Color.rgb(51, 102, 153));
				viewHolder.txtView03.setTextSize(14.6f);
			convertView.setBackgroundResource(R.drawable.list_selector_background);
			return convertView;

		}
		private int getResultPicRes(int curPosition){
			if(textValues[curPosition ]>lastTextValues[curPosition]){
				return R.drawable.navicloud_and_522a;
			}else if(textValues[curPosition ] == lastTextValues[curPosition]){
				return R.drawable.navicloud_and_521a;
			}
			return R.drawable.navicloud_and_523a;
		}

	}
}
