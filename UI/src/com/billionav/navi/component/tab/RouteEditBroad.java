package com.billionav.navi.component.tab;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.billionav.ui.R;
import com.billionav.navi.component.tab.TabHostNavi.OnTabItemClickListener;
public class RouteEditBroad extends RelativeLayout{
	private TabHostNavi tabhost;
	
	private RouteEditBroadDrive		driveBroad;
	
	public static final int SUB_INDEX_CURRENT_POSITION = 0;
	public static final int SUB_INDEX_MAP_POINT = 1;
	public static final int SUB_INDEX_FAVORITE = 2;
	public static final int SUB_INDEX_CONTACT = 3;
	public static final int SUB_INDEX_DELETE = 4;
	
//	public interface OnBroadActionListener{
//		public void onStartAndGoalExchange();
//		public void onListItemClick(int listIndex);
//		public void onWaypointAddClick();
//		public void onListItemEditClicked(int index);
//		public void onListItemDeleteClicked(int index);
//		public void onItemSwitch(int from, int to);
//	}
//	
	public interface OnRouteEditItemClickListener{
		public void onItemClick(View v, int index);
	}
	
	
	public RouteEditBroad(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.route_edit_broad, this);
		findViews();
		addItems();
	}
	
	public RouteEditBroad(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.route_edit_broad, this);
		findViews();
		addItems();
		
	}
	
//	public void setDriveListener(OnBroadActionListener l){
//		driveBroad.setOnBroadActionListener(l);
//	}
	
	public int getDriveListNum(){
		return driveBroad.getListNum();
	}
	
	public void setDriveTextAtIndex(int index, String name){
		driveBroad.setDisplayNameAtIndex(index, name);
	}
	
	public void removeDriveItemAtIndex(int index){
		driveBroad.removeItemAtIndex(index);
	}
	
	public void setDriveExchangeEnabled(boolean enabled){
		driveBroad.setStartCalculateEnabled(enabled);
	}
	
	public void setDriveAddEnabled(boolean enabled){
		driveBroad.setAddEnabled(enabled);
	}
	
	public void addDriveWayPoint(String name){
		driveBroad.addWayPoint(name);
	}
	
	public void setTabClickedListener(OnTabItemClickListener l){
		tabhost.setOnTabItemClickListener(l);
	}
	
	private void addItems() {
		driveBroad = new RouteEditBroadDrive(getContext());
		tabhost.addContentView(driveBroad);
//		tabhost.setBackgroundResource(R.drawable.jaguar_and_711a);
//		tabhost.setTabItemImage(0,  R.drawable.jaguar_and_545a,  R.drawable.jaguar_and_545b);
//		tabhost.setTabItemImage(1,  R.drawable.jaguar_and_546a,  R.drawable.jaguar_and_546b);
//		tabhost.setTabItemImage(2, 	R.drawable.jaguar_and_547a,  R.drawable.jaguar_and_547b);
		
		tabhost.setItemEnable(0, false);
		tabhost.setItemEnable(1, false);
		tabhost.setItemEnable(2, false);
		
		
//		tabhost.setSelection(0);
	}

	private void findViews() {
		tabhost = (TabHostNavi)findViewById(R.id.route_edit_broad_tabhost);
//		tabhost.setBackgroundResource(R.drawable.jaguar_and_711a);
	}	

	public void onDestory(){
		tabhost.removeAllViews();
		driveBroad.onDestory();
		driveBroad = null;
	}
}
