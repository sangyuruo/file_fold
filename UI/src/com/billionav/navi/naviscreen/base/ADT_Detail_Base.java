package com.billionav.navi.naviscreen.base;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.basiccomponent.CheckBoxCollect;
import com.billionav.navi.component.basiccomponent.CheckBoxCollect.OnStateChangedListener;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.favorite.ADT_Favorite;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public abstract class ADT_Detail_Base extends ActivityBase{
	protected TextView name;
	protected TextView address;
	protected TextView distance;
	protected TextView telNo;
	protected TextView openTime;
	
	protected CheckBoxCollect checkboxCollect;
	protected View editpoint;
	protected View savehome;
	protected View savecommpany;
	protected LinearLayout detail_info_layout;
	protected LinearLayout detail_info_2_layout;
	
	protected LinearLayout operate_layout;
	protected ImageView picture;
	
	protected boolean isCheakmapEnable = true;
	protected boolean isDestpointEnable = true;
	protected boolean isStartpointEnable = true;
	protected boolean isPassbylocationEnable = true;
	
	protected GridView gridView;
	protected SimpleAdapter grid_SimpleAdapter;
	
	protected boolean addorupdata;
	protected UIPointData pointdata = null;
	protected boolean isDataChanged = false;
	private CustomDialog cdialog = null;
	private CustomDialog noLoginDialog = null;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_srch_info);
		initdata();
		findViews();
		resetlayout();
		addBottomBar();
		setListener();
		
	}
	
	@Override
	protected void OnResume(){
		super.OnResume();
		if(UserControl_ManagerIF.Instance().HasLogin()){
			checkboxCollect.setIsEnabled(true);
		}else{			
			checkboxCollect.setIsEnabled(false);
		}
	}
	
	private void initdata() {
		// TODO Auto-generated method stub
		cdialog = new CustomDialog(this);
	}
	private void resetlayout() {
		((ViewGroup)operate_layout.getParent()).removeView(operate_layout);
		
		if(ScreenMeasure.isPortrait()) {
			((LinearLayout)detail_info_layout.getParent()).setOrientation(LinearLayout.VERTICAL);
			((ViewGroup)detail_info_layout.getParent()).addView(operate_layout);
		} else {
			((LinearLayout)detail_info_layout.getParent()).setOrientation(LinearLayout.HORIZONTAL);
			detail_info_layout.addView(operate_layout);
		}
		
		if(ScreenMeasure.isPortrait()) {
			((LinearLayout.LayoutParams)detail_info_layout.getLayoutParams()).weight = 0;
			((LinearLayout.LayoutParams)detail_info_2_layout.getLayoutParams()).weight = 1;
		} else {
			((LinearLayout.LayoutParams)detail_info_layout.getLayoutParams()).weight = 11;
			((LinearLayout.LayoutParams)detail_info_2_layout.getLayoutParams()).weight = 9;
		}
		
		if(ScreenMeasure.isPortrait()) {
			detail_info_layout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		} else {
			detail_info_layout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
		}

	}
	private void findViews() {
		name = (TextView) findViewById(R.id.name_text);
		address = (TextView) findViewById(R.id.address_text);
		distance = (TextView) findViewById(R.id.distance_text);
		telNo = (TextView) findViewById(R.id.tel_no_text);
		openTime = (TextView) findViewById(R.id.open_time_text);
		
		checkboxCollect = (CheckBoxCollect) findViewById(R.id.check_box_collect);
		picture = (ImageView)findViewById(R.id.imageView1);
		
		gridView = (GridView)findViewById(R.id.src_srch_info_grid);
		
		checkboxCollect.setCheakImage(R.drawable.navicloud_and_568a, R.drawable.navicloud_and_568b);
		List<Map<String,Object>> grid_ListItems = new ArrayList<Map<String,Object>>();
		Map<String, Object> listItem = new HashMap<String, Object>(2);
		listItem.put("image", R.drawable.navicloud_and_558a);
		listItem.put("name", getResources().getString(R.string.STR_MM_02_02_04_11));
		grid_ListItems.add(listItem);
		listItem = new HashMap<String, Object>(2);
		listItem.put("image", R.drawable.navicloud_and_556a);
		listItem.put("name", getResources().getString(R.string.STR_MM_03_01_02_02));
		grid_ListItems.add(listItem);
		listItem = new HashMap<String, Object>(2);
		listItem.put("image", R.drawable.navicloud_and_557a);
		listItem.put("name", getResources().getString(R.string.STR_MM_03_01_02_03));
		grid_ListItems.add(listItem);
		listItem = new HashMap<String, Object>(2);
		listItem.put("image", R.drawable.navicloud_and_555a);
		listItem.put("name", getResources().getString(R.string.STR_MM_03_04_05_03));
		grid_ListItems.add(listItem);
		grid_SimpleAdapter = new SimpleAdapter(this, grid_ListItems,R.layout.poi_grid_view ,new String[]{"image","name"}, new int[]{R.id.grid_image,R.id.grid_text}){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView != null){
					TextView t = null;
					t = (TextView)convertView.findViewById(R.id.grid_text);
					if(t != null){
						switch (position) {
						case 0:
							t.setEnabled(isCheakmapEnable);
							break;
						case 1:
							t.setEnabled(isStartpointEnable);
							break;
						case 2:
							t.setEnabled(isDestpointEnable);
							break;
						case 3:
							t.setEnabled(isPassbylocationEnable);
							break;
						default:
							break;
						}
					}
				}
				return super.getView(position, convertView, parent);
			}

			@Override
			public boolean isEnabled(int position) {
				switch(position){
				case 0:
					return isCheakmapEnable;
				case 1:
					return isStartpointEnable;
				case 2:
					return isDestpointEnable;
				case 3:
					return isPassbylocationEnable;
				default:
					return super.isEnabled(position);
				}
			}
			
		}; 
		gridView.setAdapter(grid_SimpleAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position){
				case 0:
					setCheakmapDisposal();
					break;
				case 1:
					setStartpointDisposal();
					break;
				case 2:
					setDestpointDisposal();
					break;
				case 3:
					setPassbylocationDisposal();
					break;
				default:
					break;
				}
				
			}
		});
		
		detail_info_layout = (LinearLayout) findViewById(R.id.detail_info_layout);
		detail_info_2_layout = (LinearLayout) findViewById(R.id.detail_info_2_layout);
		operate_layout = (LinearLayout)findViewById(R.id.operate_layout);
	}
	private void setBtnEnable(boolean ishavepurpose){
		savecommpany.setEnabled(ishavepurpose);
		savehome.setEnabled(ishavepurpose);
		checkboxCollect.setEnabled(ishavepurpose);
		if(checkboxCollect.isChecked()){
			setEditPointEnabled(ishavepurpose);
		}
	}
	
	protected void OnResumeUpdata()
	{
		//must is favorite point refresh.
		if(checkboxCollect.isChecked()){
			boolean isEditBack = BundleNavi.getInstance().getBoolean("editback");
			Log.i("icon", "isEditBack "+isEditBack);
			if(isEditBack){
				Log.i("icon", "BundleNavi.getInstance().getBoolean ...");
				getBundleNavi().putBoolean("isChanged", true);
				isDataChanged = true;
				pointdata = (UIPointData) getBundleNavi().get("pointData");
				long[] 	sStartLonLat = new long[2];
				sStartLonLat[0] = UILocationControlJNI.GetPosition(0)[0];
				sStartLonLat[1] = UILocationControlJNI.GetPosition(0)[1];
				setTitle(pointdata.getName());
				name.setText(pointdata.getName());
				address.setText(pointdata.getAddress());
				distance.setText(RouteTool.getDisplayDistance(PointTools.calcDistance(sStartLonLat, pointdata.getLonlat())));
				telNo.setText(pointdata.getTelNo());
				UIPointControlJNI.Instance().reqGetFavorite("");
				//update picture
				Uri uri = PointTools.getInstance().returnFaveratePhoto(pointdata.getUuid());
				picture.setImageResource(R.drawable.navicloud_and_591a);
				if(null != uri){
					picture.setImageURI(uri);
				}
			}

		}
			
			
	}
	
	protected void updataRouteAction(){
		int purpose = RouteCalcController.instance().getRoutePointFindPurpose();
		if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != purpose)
		{
			setBtnEnable(false);
			addorupdata = false;
			switch (RouteCalcController.instance().getRoutePointFindPurpose()) {
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START:
				isStartpointEnable = true;
				isDestpointEnable = false;
				isPassbylocationEnable = false;
				break;
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
				isStartpointEnable = false;
				isDestpointEnable = false;
				isPassbylocationEnable = true;
				break;
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
			case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
				isStartpointEnable = false;
				isDestpointEnable = true;
				isPassbylocationEnable = false;
				break;
			default:
				isStartpointEnable = true;
				isDestpointEnable = true;
				isPassbylocationEnable = true;
				break;
			}
		}
		else{
			setBtnEnable(true);
			addorupdata = true;
		}
		if(!RouteCalcController.instance().isEnableToAddWayPoint()){
			isPassbylocationEnable = false;
		}
		new Handler(){@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				grid_SimpleAdapter.notifyDataSetChanged();
			}}.sendMessageDelayed(new Message(), 100);
	}
	
	private boolean checkLogin(int msgId){
		if(UserControl_ManagerIF.Instance().HasLogin()){
			return true;
		}else{
			if(noLoginDialog == null){
				noLoginDialog = new CustomDialog(this);
			}
			noLoginDialog.setTitle(R.string.STR_MM_10_04_02_20);
			noLoginDialog.setMessage(msgId);
			noLoginDialog.setNegativeButton(R.string.STR_MM_10_04_02_22, null);
			noLoginDialog.setPositiveButton(R.string.STR_MM_10_04_02_23, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ForwardWinChange(ADT_Auth_Login.class);
				}
			});
			noLoginDialog.show();
			return false;
		}
	}
	
	private void addBottomBar() {
		
		addActionItem2(R.drawable.navicloud_and_553a,R.string.STR_MM_02_02_04_17, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkLogin(R.string.MSG_02_02_04_02)){
					setHomeDisposal();
				}
			}
		});
		
		addActionItem2(R.drawable.navicloud_and_554a,R.string.STR_MM_02_02_04_18, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkLogin(R.string.MSG_02_02_04_02)){
					setCompanyDisposal();
				}
			}
		});
		
		addActionItem2(R.drawable.navicloud_and_552a,R.string.STR_MM_02_02_04_16, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setEditDisposal();
			}
		});
		editpoint = getActionBar2().getActionItem(2);
		savehome = getActionBar2().getActionItem(0);
		savecommpany = getActionBar2().getActionItem(1);
	}
	
	public void setEditPointEnabled(boolean isEnable){
		Log.d("UIPoint", "ADT_Detail_Base : setEditPointEnabled : isEnable = " + isEnable);
		if(UserControl_ManagerIF.Instance().HasLogin() && isEnable){
			editpoint.setEnabled(true);
		}else{
			editpoint.setEnabled(false);
		}
	}
	
	private void setListener(){
		checkboxCollect.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!checkboxCollect.isChecked()){
					checkLogin(R.string.MSG_02_02_04_01);
				}
			}
		});
		checkboxCollect.setOnStateChangedListener(new OnStateChangedListener(){
			@Override
			public void onStateChange(boolean newState) {
				if(newState){
					//if size > 1000
					if(UIPointControlJNI.Instance().getBookmarkCount() >= UIPointControlJNI.MAXIMUM_FAVORITE_POINTS){
						cdialog.setTitle(R.string.MSG_02_02_04_05);
						cdialog.setMessage(R.string.MSG_02_02_04_06);
						cdialog.setNegativeButton(null, null);
						cdialog.setPositiveButton(R.string.STR_COM_003,null);
						cdialog.show();
						checkboxCollect.changeChecked(!newState);
						return;
					}
				}
				setCheckboxCollectDisposal(newState);
			}
			
		});
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		resetlayout();
	}
	
	protected abstract void setHomeDisposal();
	protected abstract void setCompanyDisposal();
	protected abstract void setEditDisposal();
	protected abstract void setCheckboxCollectDisposal(boolean newState);
	protected abstract void setCheakmapDisposal();
	protected abstract void setStartpointDisposal();
	protected abstract void setDestpointDisposal();
	protected abstract void setPassbylocationDisposal();
}
