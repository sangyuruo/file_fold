package com.billionav.navi.naviscreen.srch;


import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.actionbar.ActionBarSearchInput;
import com.billionav.navi.component.actionbar.ActionBarSearchInput.OnSearchFinishedListener;
import com.billionav.navi.component.basiccomponent.CheckBoxCollect;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListItemSearchResult;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.schedule.ScheduleDataList;
import com.billionav.navi.naviscreen.schedule.ScheduleModel;
import com.billionav.navi.naviscreen.setting.ADT_Schedule_Activity;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.PointTools.PointDetailInfoListener;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_Srch_List extends ActivityBase implements OnSearchFinishedListener, OnScreenBackListener{
	
	private ActionBarSearchInput actionbar;
    
	private TextView tip;
	private ListView listContainer;
	private CustomDialog noneSrchresultDialog = null;
	private CustomDialog noLoginDialog = null;
	private Handler handler = null;
	private SearchPointBean[] listDatas = new SearchPointBean[0];
	private SearchListAdapter adpter;
	private int[] seqId = {};

	
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_search_list, ActionBarSearchInput.class);
		
		findViews();
		
		initialize();
	}
	private void showNoneResultDialog(){
		if(noneSrchresultDialog == null){
			noneSrchresultDialog = new CustomDialog(this);
			noneSrchresultDialog.setEnterBackKeyAllowClose(true);
		}
		if(handler == null){
			handler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					noneSrchresultDialog.dismiss();
				}
				
			};
		}
		noneSrchresultDialog.setTitle(R.string.STR_MM_02_02_01_05);
		noneSrchresultDialog.setMessage(R.string.MSG_02_01_01_01);
		if(actionbar.getNetType() == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
			noneSrchresultDialog.setPositiveButton(R.string.STR_MM_02_02_01_03, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					actionbar.onlineSearch();
				}
			});
		}else{
			noneSrchresultDialog.setPositiveButton("",null);
		}
		noneSrchresultDialog.setNegativeButton(R.string.STR_MM_02_02_01_04, null);
		noneSrchresultDialog.show();
//		handler.sendEmptyMessageDelayed(1, 3000);
	}
	private void findViews() {
		actionbar = (ActionBarSearchInput) getActionBar2();
		tip = (TextView) findViewById(R.id.text_tip);
		listContainer = (ListView) findViewById(R.id.list_container);
		listContainer.setAdapter(adpter = new SearchListAdapter());
	}
	
	private void initialize() {
		UIC_SreachCommon.setCurListActivityclass(ADT_Srch_List.class);
//		cdialog = new CustomDialog(ADT_Srch_List.this);
		setDefaultBackground();
		if(getBundleNavi().getInt("inputHelpNetType") == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
			//check online search.
			addActionItem2(R.drawable.navicloud_and_429a, R.string.STR_MM_02_02_01_03 ,new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					actionbar.onlineSearch();
					getActionBar2().getActionItem(0).setVisibility(View.GONE);
				}
			});
		}
		//check map
		addActionItem2(R.drawable.navicloud_and_446a, R.string.STR_MM_02_02_02_05, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchPointBean bean = listDatas[0];
				getBundleNavi().putBoolean("isCheakMap", true);
				getBundleNavi().put("SearchPointBean", bean);
				actionbar.hideSoftInputFromWindow();
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Result.class);
				
			}
		});
			PointTools.getInstance().requestCenterName(new PointDetailInfoListener() {

				@Override
				public void onPointDetailInfoRequstFinished(String name,
						String address, String tel, long distance) {
					Log.d("UIMsgControl", "onPointDetailInfoRequstFinished : name = " + name);
					setTitle(getString(R.string.STR_MM_02_02_02_01, name));
				}
			});
		
		((View) tip.getParent()).setVisibility(View.GONE);
		
		actionbar.setOnSearchFinishedListener(this);
		listContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchPointBean bean = listDatas[position];
				getBundleNavi().putInt("index", position);
				getBundleNavi().putInt("searchKind", UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
				getBundleNavi().putBoolean("TouchFlag", true);
				getBundleNavi().put("SearchPointBean", bean);

				actionbar.hideSoftInputFromWindow();
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Detail.class);
			}
		});

//		refreshList();
	}
	
	
	
	
	@Override
	protected void OnResume() {
		super.OnResume();
		refreshList();
		actionbar.setDropdownInputbox(false);
		new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				actionbar.setDropdownInputbox(true);
			}
			
		}.sendEmptyMessageDelayed(0, 300);
	}

	private void refreshList() {
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		
		int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		//if not search data,then clear list.
//		if(count != 0){
		if(count == 0){
			showNoneResultDialog();
		}
		
		listDatas = new SearchPointBean[count];
		String[] names = new String[count];
		long[] lons = new long[count];
		long[] lats = new long[count];
		for(int i=0; i<count; i++) {
			final SearchPointBean bean = SearchPointBean.createSearchPointBean(searchResult, i);
			names[i] = bean.getName();
			lons[i] = bean.getLongitude();
			lats[i] = bean.getLatitude();
			listDatas[i] = bean;
		}
		if(UserControl_ManagerIF.Instance().HasLogin()){
			UIPointControlJNI.Instance().reqFindPoints(count,names,lons,lats);
		}else{
			adpter.notifyDataSetChanged();
		}
	}
	

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		UnlockScreen();
		int iTriggerID = cTriggerInfo.GetTriggerID();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_POINT_ADD_POINT:
//			if(cTriggerInfo.GetlParam1() == jniPointControl_new.UIC_PNT_TRG_SUCCESS){
				CustomToast.showToast(this, R.string.STR_MM_02_02_02_04, 500);
//			}
			break;
		case NSTriggerID.UIC_MN_TRG_POINT_DELETE_RECORD:
//			if(cTriggerInfo.GetlParam1() == jniPointControl_new.UIC_PNT_TRG_SUCCESS){
				CustomToast.showToast(this, R.string.STR_MM_02_02_04_22, 500);
//			}
			break;
		case NSTriggerID.UIC_MN_TRG_PNT_DATA_PREPARE_FINISH:
			refreshList();
			break;
		case NSTriggerID.POINT_RESPONSE_FIND_POINTS:
			Log.d("UIMsgControl", "POINT_RESPONSE_FIND_POINTS  result = " + (int)cTriggerInfo.m_lParam1);
			if(cTriggerInfo.m_lParam1 == UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				seqId = UIPointControlJNI.Instance().getPointSeqIds();
				Log.d("UIMsgControl", "POINT_RESPONSE_FIND_POINTS  size = " 
						+ (int)cTriggerInfo.m_lParam2 + "; length = " + seqId.length);
			}
			adpter.notifyDataSetChanged();
			break;
		case NSTriggerID.POINT_RESPONSE_ADD_POINT:
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				CustomToast.showToast(this, R.string.MSG_02_02_04_04, 500);
				checkBox.setChecked(false);
			}else{
				CustomToast.showToast(this, R.string.STR_MM_02_02_02_04, 500);
				refreshList();
			}
			break;
		case NSTriggerID.POINT_RESPONSE_DELETE_POINT:
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				CustomToast.showToast(this, R.string.MSG_02_02_04_04, 500);
				checkBox.setChecked(true);
			} else {
				CustomToast.showToast(this, R.string.STR_MM_02_02_04_22, 500);
				refreshList();
			}
			break;
		case NSTriggerID.POINT_RESPONSE_TYPE_SYNC_POINT:
			Log.d("UIMsgControl", "POINT_RESPONSE_TYPE_SYNC_POINT  result = " + (int)cTriggerInfo.m_lParam1);
			if(cTriggerInfo.m_lParam1 == UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				refreshList();
			}
			break;
		case NSTriggerID.POINT_RESPONSE_GET_POINT_LIST:
			if(UIPointControlJNI.Instance().getBookmarkCount() >= UIPointControlJNI.MAXIMUM_FAVORITE_POINTS){
				checkBox.setChecked(false);
				CustomDialog cdialog = new CustomDialog(this);
				cdialog.setTitle(R.string.MSG_02_02_04_05);
			    cdialog.setMessage(R.string.MSG_02_02_04_06);
			    cdialog.setNegativeButton(null, null);
			    cdialog.setPositiveButton(R.string.STR_COM_003,null);
			    cdialog.show();
			}else{
				UIPointControlJNI.Instance().reqAddFavorite(point);
			}
			break;
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	private UIPointData point = new UIPointData();
	private CheckBoxCollect checkBox = null;

	@Override
	protected void OnDestroy() {
		PointTools.getInstance().removePointListener();
		super.OnDestroy();
		UISearchControlJNI.Instance().DestroySearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);

	}

	@Override
	public void onSearchFinished() {
		refreshList();
		listContainer.setSelection(0);
	}

	@Override
	public void onBack() {
		UISearchControlJNI.Instance().DestroySearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		MenuControlIF.Instance().BackWinChange();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshList();
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(ADT_Route_Main.class == MenuControlIF.Instance().GetHierarchyBelowWinscapeClass()){
				RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
			}
		}
		return super.OnKeyDown(keyCode, event);
	}
	
	class SearchListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listDatas.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listDatas[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SearchPointBean bean = listDatas[position];
			
			String name =  bean.getName();
			String address =  bean.getAddress();
			long dis = bean.getDistence();
			String dStr = RouteTool.getDisplayDistance(dis);

			if(convertView == null) {
//				Log.d("UIMsgControl", "convertView is null, position = " + position);
				if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != RouteCalcController.instance().getRoutePointFindPurpose())
				{
					switch (RouteCalcController.instance().getRoutePointFindPurpose()) {
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START:
						convertView = new ListItemSearchResult(ADT_Srch_List.this, name, address, dStr, R.drawable.navicloud_and_556a);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
						convertView = new ListItemSearchResult(ADT_Srch_List.this, name, address, dStr, R.drawable.navicloud_and_555a);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
						convertView = new ListItemSearchResult(ADT_Srch_List.this, name, address, dStr, R.drawable.navicloud_and_557a);
						break;
					default:
						break;
					}
					
					if(convertView != null) {
						convertView.setTag(bean);
						
					}
				}
				else{
					convertView = new ListItemSearchResult(ADT_Srch_List.this, name, address, dStr, R.drawable.navicloud_and_569a);
					convertView.setTag(bean);
					ListItemSearchResult item = (ListItemSearchResult) convertView;
					item.setVisibility();
					item.setIsEnabled(UserControl_ManagerIF.Instance().HasLogin());
					item.setClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							if(!UserControl_ManagerIF.Instance().HasLogin()){
								if(noLoginDialog == null){
									noLoginDialog = new CustomDialog(ADT_Srch_List.this);
								}
								noLoginDialog.setTitle(R.string.STR_MM_10_04_02_20);
								noLoginDialog.setMessage(R.string.MSG_02_02_04_01);
								noLoginDialog.setNegativeButton(R.string.STR_MM_10_04_02_22, null);
								noLoginDialog.setPositiveButton(R.string.STR_MM_10_04_02_23, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										ForwardWinChange(ADT_Auth_Login.class);
									}
								});
								noLoginDialog.show();
								return;
							}
							SearchPointBean searchbean = (SearchPointBean) v.getTag();
							point = new UIPointData(0,searchbean.getName(),searchbean.getAddress(), 
									searchbean.getTelNo(), searchbean.getLongitude(), searchbean.getLatitude());
							checkBox = ((CheckBoxCollect)v);
							if (((CheckBoxCollect)v).isChecked()) {
								UIPointControlJNI.Instance().reqGetFavorite("");
							} else {
								Log.d("UIPoint", "reqDeleteFavorite");
								UIPointControlJNI.Instance().reqDeleteFavorite(searchbean.getSeqId());
							}
						}
					});
					if(seqId.length > position){
						item.setCheaked(seqId[position] != UIPointControlJNI.RESULT_POINT_INVALID_ID);
					}
				}
				
				if(convertView != null){
					((ListItemSearchResult)convertView).setRouteListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != RouteCalcController.instance().getRoutePointFindPurpose()){
								SearchPointBean bean = (SearchPointBean) v.getTag();
								RouteCalcController.instance().searchPointToPOI(bean);
								if(!BackSearchWinChange(ADT_Route_Main.class)){
									ForwardKeepDepthWinChange(ADT_Route_Main.class);		
								}
							}
							else{
								
								SearchPointBean bean = (SearchPointBean) v.getTag();
								if(BackSearchWinChange(ADT_Schedule_Activity.class)){
									if (HybridUSTools.getInstance().isTheSamePoint(
											bean.getLonlat()[0],
											bean.getLonlat()[1])) {
										CustomDialog dialog = new CustomDialog(ADT_Srch_List.this);
										dialog.setTitle(getString(R.string.STR_COM_047));
										dialog.setMessage(getString(R.string.STR_COM_048));
										dialog.setPositiveButton(R.string.MSG_00_00_00_11, null);
										dialog.show();
										return;
									}
									ScheduleModel model = new ScheduleModel();
									model.lon = bean.getLonlat()[0];
									model.lat = bean.getLonlat()[1];
									model.pointName = bean.getName();
									ScheduleDataList.getInstance().addList(model);
									ForwardKeepDepthWinChange(ADT_Schedule_Activity.class);
								}else{
									RouteCalcController.instance().rapidRouteCalculateWithData(bean.getName(), bean.getLonlat()[0], bean.getLonlat()[1], bean.getAddress(), bean.getTelNo());
								}
							}
						}
					});
				}
			} else {
				ListItemSearchResult item = (ListItemSearchResult) convertView;
				if(seqId.length > position){
//					Log.d("UIMsgControl", "position = " + position + "; sedID = " + seqId[position]);
					item.setIsEnabled(UserControl_ManagerIF.Instance().HasLogin());
					bean.setSeqId(seqId[position]);
					item.setTag(bean);
					item.setCheaked(seqId[position] != UIPointControlJNI.RESULT_POINT_INVALID_ID);
				}
				item.setInfo(name, address, dStr);
				item.setTag(bean);
			}
			return convertView;
		}
	}

}
