package com.billionav.navi.component.tab;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.ViewHelp;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class RouteEditBroadDrive extends RelativeLayout{
	private Button addRoutePoint = null;
	private ImageView swapStartStop = null;
	private Button startCalculate = null;
	private final int TITLE_HIGHT = 26;
	private final int LISTVIEW_ITEM_HIGHT = 48;
	
	private ListView	list;
	private ArrayList<String> data = new  ArrayList<String>();
	private DataAdapter		dataAdapter;
	//the layout contain add way point button and calculate route button. 
	private LinearLayout buttonlayout = null;
	final static int TAG_EDIT = 1000;
	final static int TAG_SEARCH = 2000;
	final static int TAG_DELETE = 4000;
	
	private boolean isInDragMode = false;
	
	public interface OnBroadActionListener{
		public void onStartCalculating();
		public void onListItemClick(int listIndex);
		public void onWaypointAddClick();
		public void onListItemEditClicked(int index);
		public void onListItemDeleteClicked(int index);
		public void onItemSwitch(int from, int to);
		public void onStartEndChangedClicked();
		public void onListItemSearchClicked(int index);
	}
	
	private OnBroadActionListener actionListener;
	public void setOnBroadActionListener(OnBroadActionListener l){
		actionListener = l;
	}
	
	public void setDisplayNameAtIndex(int index, String name){
		if(index < 0 || index >= data.size()){
			return;
		}
		data.set(index, name);
		dataAdapter.notifyDataSetChanged();
	}
	
	public void removeItemAtIndex(int index){
		data.remove(index);
		dataAdapter.notifyDataSetChanged();
	}
	
	public void setStartCalculateEnabled(boolean enabled){
		startCalculate.setEnabled(enabled);
	}
	
	public void setAddEnabled(boolean enabled){
		addRoutePoint.setEnabled(enabled);
	}
	
	public RouteEditBroadDrive(Context context) {
		super(context);
		initialize();
	}
	
	public RouteEditBroadDrive(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	public void addWayPoint(String r){
		data.add("");
		data.set(data.size()-1, data.get(data.size()-2));
		data.set(data.size()-2, r);
		adjustListViewHight();
	}
	public void setWayPointName(String r, int position){
		data.set(position, r);
		dataAdapter.notifyDataSetChanged();
	}
	
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.route_edit_broad_drive, this);
		
		findViews();
		setListener();
		
		list.setCacheColorHint(Color.TRANSPARENT);
		//remove setPadding to design,modified by Jerry Chen
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(actionListener != null){
					actionListener.onListItemClick(position);
				}
			}
		});
		dataAdapter.notifyDataSetChanged();
	}

	private void setListener() {
		startCalculate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(actionListener != null){
					actionListener.onStartCalculating();
				}
			}

			
		});
		addRoutePoint.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(data.size() < RouteCalcController.MAX_ROUTE_POINT_NUM ){
					if(actionListener != null){
						actionListener.onWaypointAddClick();
					}
				}
			}
		});
		swapStartStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(actionListener != null){
					actionListener.onStartEndChangedClicked();
					exchangeStartAndEnd();
					dataAdapter.notifyDataSetChanged();
				}
				else{
					Log.d("test","actionListenr = null");
				}
			}
		});
	}

	private void findViews() {
		list = (ListView) findViewById(R.id.reb_touchlist);
		addRoutePoint = (Button)findViewById(R.id.routeeditbroaddrive_addimage);
		addRoutePoint.setText(R.string.STR_MM_03_04_04_10);
		startCalculate = (Button)findViewById(R.id.routeeditbroaddrive_exchangeimage);
		startCalculate.setText(R.string.STR_MM_03_01_02_04);
		swapStartStop = (ImageView)findViewById(R.id.routeeditbroaddrive_swapimage);
		buttonlayout = (LinearLayout)findViewById(R.id.reb_bottom_area);
		data.add("");
		data.add("");
		dataAdapter = new DataAdapter(getContext(),data);
		list.setAdapter(dataAdapter);
	}
	
	public void setSupportDragMode(boolean yes){
		isInDragMode = true;
		dataAdapter.notifyDataSetChanged();
	}
	
	public int getListNum(){
		return data.size();
	}
	/*
	 * ********************************about function*************************************
	 * modify a bug that about on small screen mobile phone will lead design display error.
	 * specific information is on small screen mobile phone when listview has 3 items,
	 * buttonlayout(contain add route button and obtain route button)will is cover.
	 * because if listview don't fill screen like that it will don't slipping. so lead to this bug.  
	 * now according to screen'height dynamically setting listview'height,let it can slip.
	 */
	public void adjustListViewHight(){
		/*
		 * listviewHeight mean listview's height, in dip.
		 * screenHeight mean screen's height, in dip.
		 * buttonlayoutHeight mean layout that contain add route button and obtain route button of height.
		 */
		int listviewHeight =  data.size()*LISTVIEW_ITEM_HIGHT;
		int screenHeight = DensityUtil.px2dp(getContext(), ScreenMeasure.getHeight())-TITLE_HIGHT;
		int buttonlayoutHeight = buttonlayout.getLayoutParams().height;
		if(listviewHeight >= screenHeight - buttonlayoutHeight){
			list.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,DensityUtil.dp2px(getContext(), screenHeight-buttonlayoutHeight)));
			dataAdapter.notifyDataSetChanged();
		}else{
			list.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			dataAdapter.notifyDataSetChanged();
		}

	}
	
	private String getDescriptionByIndex(int index){
		String description = "";
		if(index == 0){
			description = getContext().getString(R.string.STR_MM_03_01_02_02);
		}else if(index == data.size() - 1){
			description = getContext().getString(R.string.STR_MM_03_01_02_03);
		}else {
			description = getContext().getString(R.string.STR_MM_03_04_05_03);
		}
		return description;
	}
	
	private int getImageIdByIndex(int index){
		int id = 0;
		if(index == 0){
			id = R.drawable.navicloud_and_014a;
		}else if(index ==  data.size() - 1){
			id = R.drawable.navicloud_and_015a;
		}else if(index == 1){
			id = R.drawable.navicloud_and_009a_01;
		}else if(index == 2){
			id = R.drawable.navicloud_and_009a_02;
		}else if(index == 3){
			id = R.drawable.navicloud_and_009a_03;
		}else if(index == 4){
			id = R.drawable.navicloud_and_009a_04;
		}else if(index == 5){
			id = R.drawable.navicloud_and_009a_05;
		}else if(index == 6){
			id = R.drawable.navicloud_and_009a_06;
		}else if(index == 7){
			id = R.drawable.navicloud_and_009a_07;
		}else if(index == 8){
			id = R.drawable.navicloud_and_009a_08;
		}else if(index == 9){
			id = R.drawable.navicloud_and_009a_09;
		}else if(index == 10){
			id = R.drawable.navicloud_and_009a_10;
		}
		return id;
	}
	
	private void exchangeStartAndEnd() {
		if(data.size() >=2)
		{
			String start = data.get(0);
			String dest = data.get(data.size() -1);
			
			data.remove(start);
			data.remove(dest);
			data.add(0, dest);
			data.add(start);
		}
	}
	
	private class DataAdapter extends ArrayAdapter<String>{

		public DataAdapter(Context context,ArrayList<String> data) {
			super(context, R.layout.reb_list_item, data);
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(null == convertView){
				convertView = inflater.inflate(R.layout.reb_list_item, parent,
						false);
			}

			TextView name = (TextView) convertView.findViewById(R.id.reb_li_name);
			ImageView icon = (ImageView) convertView.findViewById(R.id.reb_li_route_icon);
			ImageView fristIcon = (ImageView) convertView.findViewById(R.id.reb_li_search_icon);
			ImageView secondIcon = (ImageView) convertView.findViewById(R.id.reb_li_delete);

			if (null != name) {
				name.setText(this.getItem(position));
				name.setFocusable(!isInDragMode);
				name.setTag(position+TAG_EDIT);
				name.setOnClickListener(l);
			}
			if (null != icon) {
				icon.setImageResource(getImageIdByIndex(position));
				icon.setTag(position+TAG_EDIT);
			}
			//whether has way point.
			if(!(RouteCalcController.instance().getRoutePointNum() > 2)){
				if(fristIcon != null){
					fristIcon.setVisibility(View.INVISIBLE);
				}
				if(secondIcon != null){
					secondIcon.setVisibility(View.VISIBLE);
					secondIcon.setTag(position+TAG_SEARCH);
					secondIcon.setOnClickListener(toMap);
					secondIcon.setImageResource(R.drawable.navicloud_and_204a);
				}
			}else{
				if(fristIcon != null){
					fristIcon.setVisibility(View.VISIBLE);
					fristIcon.setTag(position+TAG_SEARCH);
					fristIcon.setOnClickListener(toMap);
					secondIcon.setImageResource(R.drawable.navicloud_and_204a);
				}
				if(secondIcon != null){
					//this point whether is start point or end point.
					if(position == 0 || position == getCount() - 1){
						secondIcon.setVisibility(View.INVISIBLE);
					}
					else{
						secondIcon.setVisibility(View.VISIBLE);
						secondIcon.setTag(position + TAG_DELETE);
						secondIcon.setOnClickListener(delete);
						secondIcon.setEnabled(!isInDragMode);
						secondIcon.setImageResource(R.drawable.navicloud_and_205a);
					}
					ViewHelp.setTouchDelegate(secondIcon, -10, -10, 10, 10);
				}
			}
			
//			if(null != fristIcon){
//				fristIcon.setTag(position+TAG_SEARCH);
//				fristIcon.setOnClickListener(toMap);
//			}
//			if(secondIcon != null){
//				if(position == 0 || position == getCount() - 1){
//					secondIcon.setVisibility(View.INVISIBLE);
//				}else{
//					secondIcon.setVisibility(View.VISIBLE);
//				}
//				secondIcon.setTag(position + TAG_DELETE);
//				secondIcon.setOnClickListener(delete);
//				secondIcon.setEnabled(!isInDragMode);
//				ViewHelp.setTouchDelegate(secondIcon, -10, -10, 10, 10);
//			}
//			handler.sendMessageDelayed(new Message(), 1000);
			return convertView;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}
		
		private OnClickListener l = new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tag = ((Integer)v.getTag());
				if(actionListener != null){
					actionListener.onListItemEditClicked(tag - TAG_EDIT);
				}
			}
			
		};
		
		private OnClickListener delete = new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tag = ((Integer)v.getTag());
				if(actionListener != null){
					actionListener.onListItemDeleteClicked(tag - TAG_DELETE);
					adjustListViewHight();
				}
			}
			
		};
		private OnClickListener toMap = new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tag = ((Integer)v.getTag());
				if(actionListener != null){
					actionListener.onListItemSearchClicked(tag - TAG_SEARCH);
				}
			}
		};
		private OnClickListener move = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
			
		};
		
		public void onDestory(){
			move = null;
			delete = null;
			l = null;
		}
		
	}
	
	public void onDestory(){
		list = null;
		dataAdapter.clear();
		dataAdapter.onDestory();
		dataAdapter = null;
		actionListener = null;
		data.clear();
		data = null;
		startCalculate.setOnClickListener(null);
		addRoutePoint.setOnClickListener(null);
		addRoutePoint = null;
		startCalculate = null;
	}

	public void LogMessage() {
		Log.d("test","data size = "+data.size());
		
	}
	
}
