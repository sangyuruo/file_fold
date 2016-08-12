package com.billionav.navi.naviscreen.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class ADT_Eco_Info extends ActivityBase {

	private static final int Timer_Trigger = 1000;
	private  final static  int Timer_Trigger_Msg = 1;
	private ListViewNavi listView;
	private TextView currentTime;
	private TextView sortTxt;
	private LinearLayout linear;
	private LinearLayout scrHeaderLinear;
	private RelativeLayout rl;
	
	private final String[] textValues = new String[]{"8","76%","81%","17%"};
	private String[] tempTexts = null;

	private BaseAdapter adapter;
	private Handler handler;
	private SimpleDateFormat sd;
	private RatingBar bar = null;
//	private final jniEcoControl ecoControl = new jniEcoControl();

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.scr_eco_info);
		setDefaultBackground();
		Log.d("test","oncreate");
		tempTexts = new String[]{getString(R.string.STR_MM_10_02_01_05),getString(R.string.STR_MM_10_02_01_06),getString(R.string.STR_MM_10_02_01_07),getString(R.string.STR_MM_10_02_01_08)};
		setTitle(R.string.STR_MM_10_02_01_09);
		findViews();
		addActionItem2(R.drawable.navicloud_and_434a ,R.string.STR_MM_10_02_01_11, new View.OnClickListener() {
			public void onClick(View v) {
				compareEcoClick();
			}
		});
		updateData(false);
		adapter = new EcoInfoAdapter(getApplicationContext());
		listView.setAdapter(adapter);
		
		sd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		ViewGroup.LayoutParams lp = currentTime.getLayoutParams();
		lp.width = ScreenMeasure.getWidth();
		currentTime.setLayoutParams(lp);
		registerEvent();
	}

	private void findViews() {
		linear = (LinearLayout)findViewById(R.id.eco_info_linear);
		scrHeaderLinear = (LinearLayout)findViewById(R.id.scr_header_linear);
		rl = (RelativeLayout)findViewById(R.id.rl);

		listView = (ListViewNavi)findViewById(R.id.eco_list);
		currentTime = (TextView)findViewById(R.id.current_time);
		sortTxt = (TextView)findViewById(R.id.sore_txt);
		bar = (RatingBar) findViewById(R.id.eco_bar);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		super.onConfigurationChanged(newConfig);
		resizeLayout();
	}
	private void resizeLayout(){
		ViewGroup.LayoutParams currentTimeLp = currentTime.getLayoutParams();
		ViewGroup.LayoutParams params = rl.getLayoutParams();
		if(ScreenMeasure.isPortrait()){
			linear.setOrientation(LinearLayout.VERTICAL);
			scrHeaderLinear.setOrientation(LinearLayout.HORIZONTAL);
			currentTimeLp.width = ScreenMeasure.getWidth();
			params.height = 300;
			params.width = ScreenMeasure.getWidth();
		}else{
			linear.setOrientation(LinearLayout.HORIZONTAL);
			scrHeaderLinear.setOrientation(LinearLayout.VERTICAL);
			currentTimeLp.width = (int)(ScreenMeasure.getWidth()*0.4);
			params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params.width = (int)(ScreenMeasure.getWidth()*0.4);
		}
		currentTime.setLayoutParams(currentTimeLp);
		rl.setLayoutParams(params);
	}
	
	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		Date date = new Date();
		currentTime.setText(sd.format(date));
		resizeLayout();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onTimerTrigger();			
			}
		},Timer_Trigger);
	}
	
	private void compareEcoClick(){
//		if(ecoControl.GetHistoryRecordNum() >0){
//			MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
//			MenuControlIF.Instance().ForwardWinChange(ADT_Eco_Compare.class);
//		}
	}
	
	private void registerEvent(){
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Timer_Trigger_Msg:
					onTimerTrigger();
					break;
				default:
					break;
				}
			}
		};
		
	}
	
	
	private void onTimerTrigger(){
		updateData(true);
		handler.sendMessageDelayed(handler.obtainMessage(Timer_Trigger_Msg),Timer_Trigger );
	}
	
	private void setNumStarts(int value){
		bar.setRating(value);
	}

	private void updateData(boolean isNotify){

//		float avgOilSpend = ecoControl.GetAvgFuelConsumption();
//		//don't know invalid value
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
//		int sore = getScore(eStart*100, ceStart*100, idleStart*100);
//		setNumStarts((int)(sore/20.0));
//	
//		sortTxt.setText(getApplication().getString(R.string.STR_MM_10_02_02_02, sore));
//		textValues[0] = new DecimalFormat("#########.##").format(avgOilSpend)+"km/L";
//		textValues[1] = (int)(eStart*100) +"%";
//		textValues[2] = (int)(ceStart*100) +"%";
//		textValues[3] = (int)(idleStart*100)+"%";
//
//		if(isNotify){
//			adapter.notifyDataSetChanged();
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
	
	@Override
	protected void OnPause() {
		handler.removeMessages(Timer_Trigger_Msg);
		super.OnPause();
	}
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
	}
	public static class ViewHolder {
		TextView txtView01;
		TextView txtView02;
	}
	public class EcoInfoAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public EcoInfoAdapter(Context context) {
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
				convertView = inflater.inflate(R.layout.list_item_txt2, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.txtView01 = (TextView) convertView
						.findViewById(R.id.list_txt01);
				viewHolder.txtView02 = (TextView) convertView
						.findViewById(R.id.list_txt02);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.txtView01.setText(tempTexts[position]);
			viewHolder.txtView02.setText(textValues[position]);
			convertView.setBackgroundResource(R.drawable.list_selector_background);
			return convertView;
		}
	}
}
