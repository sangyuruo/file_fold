package com.billionav.navi.naviscreen.route;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.navi.component.guidebar.base.GuideInfoController;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.WayPointPreviewController;
import com.billionav.navi.uitools.WayPointPreviewController.WayPointInfo;
import com.billionav.ui.R;

public class ADT_Route_DetailedInfo extends ActivityBase{
	
	private ListViewNavi	detailList;
	
	private int 	pointKindCount = 0;
	
	private final SparseIntArray pointIndexs = new SparseIntArray(3);
	
	private int		routeKind;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		
		setContentView(R.layout.adt_route_detailed_info);
		
		detailList = (ListViewNavi) findViewById(R.id.wpi_list);
		
		initListTitle();
		initActionBar();
		initListView();
	}
	
	@Override
	protected void OnResume() {
		WayPointPreviewController.Instance().initInfoList();
		routeKind = BundleNavi.getInstance().getInt("routeKind", routeKind);
		super.OnResume();
	}
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
	}
	
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	private void initListTitle(){
		
		String subString = RouteTool.substitutionDistance(GuideInfoDataManager.Instance().getRemainingDistanceString()) + " - " 
				+  RouteTool.substitutionTime(GuideInfoDataManager.Instance().getRemainingTimeString());
		String destName = GuideInfoDataManager.Instance().getDestinationName();
		String startName = GuideInfoDataManager.Instance().getStartName();
		if("".equals(startName)){
			startName = getString(R.string.STR_MM_03_01_01_06);
		}
	
		TextView title = (TextView) findViewById(R.id.wpi_txt_route_name);
		TextView subtitle = (TextView) findViewById(R.id.wpi_txt_route_info);
		
		title.setText(getString(R.string.STR_MM_03_02_01_06) + "  " + destName);
		subtitle.setText(subString);
	}
	


	private void initListView(){
		
		ArrayList<WayPointInfo> dataList = WayPointPreviewController.Instance().getWayPointList();
		DetailListAdapter dataAdapter = new DetailListAdapter(this,dataList);
		detailList.setAdapter(dataAdapter);
		detailList.setPadding(2, 8, 2, 8);
		detailList.setCacheColorHint(Color.TRANSPARENT);
		detailList.setOnItemClickListener(detailListItemClickListener);
		int position = WayPointPreviewController.Instance().getSelectedWayPointIndex();
		position = position>0?position:0;
		detailList.setSelection(position);
	}
	
	private class DetailListAdapter extends BaseAdapter{

		private final ArrayList<WayPointInfo> data;
		public DetailListAdapter(Context context, ArrayList<WayPointInfo> dataList) {
			data = dataList;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			
			return data.size();
		}
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (data.get(position).point_Kind >= -1 && data.get(position).point_Kind<= 4) {
				return -1;
			}
			return 1;
		}
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		private boolean isWaypoint(int position){
			return getItemViewType(position) == -1;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder1 holder1;
			if(convertView == null){
				holder1 = new ViewHolder1();
				if(isWaypoint(position)) {
					convertView = getLayoutInflater().inflate(R.layout.start_and_goal_point,null);
					holder1.detail1 = (TextView)convertView.findViewById(R.id.li_wp_detail2);
					holder1.icon1 = (ImageView) convertView.findViewById(R.id.li_wp_turningicon2);
					convertView.setTag(holder1);
				} else {
					convertView = getLayoutInflater().inflate(R.layout.list_item_waypoint, null);
					holder1.detail1 = (TextView)convertView.findViewById(R.id.li_wp_detail);
					holder1.distance1 = (TextView)convertView.findViewById(R.id.li_wp_distance);
					holder1.icon1 = (ImageView) convertView.findViewById(R.id.li_wp_turningicon);
					convertView.setTag(holder1);
				}
					
			}else{
				holder1 = (ViewHolder1) convertView.getTag();
			}
			WayPointInfo info = data.get(position);
			
			
			int imageId = 0;
			if(isWaypoint(position)) {
				imageId = getImageId(position, info);
			} else {
				imageId = GuideInfoController.getArrowIconByKindForDetail(
						info.point_Direction);
			}
			
			String turningInfo = getTurningInfo(info);
			
			String distance = RouteTool.getDisplayDistance(info.point_Distance);
			
			if(isWaypoint(position)) {
				holder1.detail1.setText(turningInfo);
				holder1.icon1.setImageResource(imageId);
			} else {
				holder1.detail1.setText(turningInfo);
				holder1.distance1.setText(distance);
				holder1.icon1.setImageResource(imageId);
			}
			return convertView;
		}
		
		private String getTurningInfo(WayPointInfo info) {
			String turningInfo;
			if(info.point_Kind == -1){
				if(TextUtils.isEmpty(info.point_StreetName) || info.point_StreetName.equals(getString(R.string.STR_MM_02_02_04_15))){
					turningInfo = getString(R.string.STR_MM_03_01_01_02);
				}else{ 
					turningInfo =  getString(R.string.STR_MM_03_01_01_02) + "-" + info.point_StreetName;
				}	
			}else if(info.point_Kind == UIGuideControlJNI.DPGUDEF_CMN_FLAG_GOAL){
				turningInfo = getString(R.string.STR_MM_03_01_01_03) + "-" + info.point_StreetName;
			}else{
				turningInfo = 
					WayPointPreviewController.Instance().getDirectionString(ADT_Route_DetailedInfo.this, info.point_Direction)
					+ getString(R.string.STR_MM_01_01_04_16)
					+ info.point_StreetName;
			}
			return turningInfo;
		}
		
		private int getImageId(int position, WayPointInfo info) {
			int imageId = 0;
			if (UIGuideControlJNI.DPGUDEF_CMN_FLAG_VIA_1 == info.point_Kind) {
				
				int pointKind = pointIndexs.get(position, -1);
				if (pointKind == -1) {
					pointIndexs.put(position, pointKindCount++);
				}
				pointKind = pointIndexs.get(position);

				if (pointKind == 0) {
					imageId = R.drawable.navicloud_and_009a_01;
				} else if (pointKind == 1) {
					imageId = R.drawable.navicloud_and_009a_02;
				} else if (pointKind == 2) {
					imageId = R.drawable.navicloud_and_009a_03;
				} else if (pointKind == 3) {
					imageId = R.drawable.navicloud_and_009a_04;
				} else if (pointKind == 4) {
					imageId = R.drawable.navicloud_and_009a_05;
				} else if (pointKind == 5) {
					imageId = R.drawable.navicloud_and_009a_06;
				} else if (pointKind == 6) {
					imageId = R.drawable.navicloud_and_009a_07;
				} else if (pointKind == 7) {
					imageId = R.drawable.navicloud_and_009a_08;
				} else if (pointKind == 8) {
					imageId = R.drawable.navicloud_and_009a_09;
				} else if (pointKind == 9) {
					imageId = R.drawable.navicloud_and_009a_10;
				} 
			} else if (UIGuideControlJNI.DPGUDEF_CMN_FLAG_VIA_2 == info.point_Kind) {
				imageId = R.drawable.navicloud_and_009a_02;
			} else if (UIGuideControlJNI.DPGUDEF_CMN_FLAG_VIA_3 == info.point_Kind) {
				imageId = R.drawable.navicloud_and_009a_03;
			} else if (UIGuideControlJNI.DPGUDEF_CMN_FLAG_GOAL == info.point_Kind) {
				imageId = R.drawable.navicloud_and_557a;
			}else if(-1 == info.point_Kind){
				imageId = R.drawable.navicloud_and_556a;
			}
			return imageId;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	private void initActionBar(){
		//add edit button
		addActionItem2(R.drawable.navicloud_and_444a, R.string.STR_MM_01_02_01_13, new OnClickListener() {
			public void onClick(View v) {
				// TODO change screen to guide
				RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_SHOW);
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_Main.class);
			}
		});
		
		addActionItem2(R.drawable.navicloud_and_446a, R.string.STR_MM_02_02_02_05, new OnClickListener() {
			public void onClick(View v) {
				// TODO change screen to guide
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_Profile.class);
			}
		});
		addActionItem2(R.drawable.navicloud_and_447a, R.string.STR_MM_03_02_01_07, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getBundleNavi().putBoolean("Navigation", true);
				MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
			}
		});
		getActionBar2().setText(getString(R.string.STR_MM_01_02_01_11));
	}
	


	
	private OnItemClickListener detailListItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			// TODO Auto-generated method stub
			new Handler(){
				public void handleMessage(Message msg) {
					if(msg.what != 0x118) { return;
                    }
					if(position < WayPointPreviewController.Instance().getWayPointList().size()){
						WayPointPreviewController.Instance().requestWayPointInfoAtIndex(position);
					}
					
				};
				
			}.sendEmptyMessageDelayed(0x118, 500);
			MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_Profile.class);
		}
	};
	  static class ViewHolder1 {
			  TextView distance1;
			  TextView detail1;
			  ImageView icon1;
	   }  

	
}
