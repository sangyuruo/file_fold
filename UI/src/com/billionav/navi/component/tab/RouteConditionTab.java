package com.billionav.navi.component.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class RouteConditionTab extends LinearLayout{

	//0: conditionTime
	//1: conditionDis
	//2: conditionRoad
	//3: conditionECO
	private TextView[]		conditionTabs;
	private onTabClickListener listener;
//	private boolean isVerForJP;
	private final int[][] backgroundResourcesCH = {{R.drawable.navicloud_and_781b, R.drawable.navicloud_and_784b, R.drawable.navicloud_and_752b}
										,{R.drawable.navicloud_and_781a, R.drawable.navicloud_and_784a, R.drawable.navicloud_and_752a}};
	private final int[][] backgroundResourcesJPOS = {{R.drawable.navicloud_and_781b, R.drawable.navicloud_and_753b, R.drawable.navicloud_and_751b,R.drawable.navicloud_and_784b}
										,{R.drawable.navicloud_and_781a, R.drawable.navicloud_and_753a,  R.drawable.navicloud_and_751a,R.drawable.navicloud_and_784a}};
	private int 			selectedIndex;
	
	public interface onTabClickListener{
		public void onTabClicked(int index);
	}
	
	
	public RouteConditionTab(Context context, AttributeSet attrs) {
		super(context, attrs); 
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(SystemTools.isJP() || SystemTools.isOS()){
			conditionTabs = new TextView[4];
			inflater.inflate(R.layout.tab_rp_condition_jp, this);
		}else{
			conditionTabs = new TextView[3];
			inflater.inflate(R.layout.tab_rp_condition, this);
		}
	    setBackgroundResource(R.drawable.navicloud_and_518a);
	    findViews();
	    
	    updateViewAtIndex(0);
	}
	private final int[] textViewResIds = {R.id.rp_tab_condition_time, R.id.rp_tab_condition_distance, R.id.rp_tab_condition_road, R.id.rp_tab_condition_eco};
	private void findViews(){
		for(int i=0; i<conditionTabs.length; i++){
			conditionTabs[i] = (TextView) findViewById(textViewResIds[i]);
			conditionTabs[i].setText(getContext().getString(getTabText(i)));
			conditionTabs[i].setOnClickListener(l);
		}
	}
	
	public void setOnTabClickedListener(onTabClickListener l){
		listener = l;
	}
	
	private OnClickListener l = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			selectedIndex = getSelectedIndexByList(v);
			updateViewAtIndex(selectedIndex);
			
			if(listener != null){
				listener.onTabClicked(selectedIndex);
			}
		}
	};
	
	public void setTabSelectIndex(int index){
		updateViewAtIndex(index);
	}
	
	protected int getSelectedIndexByList(View v) {
		int i=0;
		for(View temp: conditionTabs){
			if(v == temp){
				Log.d("test","selection = "+i);
				return i;
			}
			i++;
		}
		return 0;
	}
	public int getTabSelectIndex(){
		return selectedIndex;
	}

	private void updateViewAtIndex(int selection){
		for(int i=0; i< conditionTabs.length; i++){
			conditionTabs[i].setBackgroundResource(getTabbackgroundRes(i == selection, i));
			conditionTabs[i].setTextColor(getTabTextColor(i == selection));
			conditionTabs[i].setText(getTabText(i));
		}
	}
	private int getTabTextColor(boolean selected){
		return getContext().getResources().getColor(selected? R.color.navicloud_text_002:R.color.Royal_Blue);
	}
	private int getTabbackgroundRes(boolean selected, int index){
		int tabColor = selected?0:1;
		int tabStyle = 0;
		if(SystemTools.isJP() || SystemTools.isOS()){
			switch(index){
			default:
			case 0:
				tabStyle = 0;
				break;
			case 1:
				tabStyle = 1;
				break;
			case 2:
				tabStyle = 2;
				break;
			case 3:
				tabStyle = 3;
				break;
			}
			return backgroundResourcesJPOS[tabColor][tabStyle];
		}
		else{
			if(index == 0){
			}else if(index == conditionTabs.length-1){
				tabStyle = 1;
			}else{
				tabStyle = 2;
			}
			return backgroundResourcesCH[tabColor][tabStyle];
		}
	}
	private int getTabText(int index){
		switch(index){
			case 0: return R.string.STR_MM_03_04_01_04;
			case 1: return R.string.STR_MM_03_04_01_05;
			case 2: return R.string.STR_MM_03_04_01_06;
			case 3: return R.string.STR_MM_03_04_04_13;
			default:break;
		}
		return -1;
	}
}
