package com.billionav.navi.naviscreen.srch;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.basiccomponent.CheckBoxCollect;
import com.billionav.navi.component.dialog.CProgressDialog;
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
import com.billionav.navi.system.PLog;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.PointTools.PointDetailInfoListener;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.navi.uitools.SearchTools;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_POI_List extends ActivityBase implements OnScreenBackListener{
	
//	private TextView showMap;
	private TextView tip;
	private ListView listContainer;
	private int index;
//	private CustomDialog cdialog;
	private CustomDialog noLoginDialog = null;
	private CProgressDialog cprogressBarDialog = null;
	private CustomDialog noneSrchresultDialog = null;
	private SearchPointBean[] listDatas = new SearchPointBean[0];
	private MyAdapter adpter;
	private int[] seqId = {};
	private int searchNetType;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_search_list);
		
		findViews();
		
		initialize();
		
		initActionbar();
	}
	
	private void findViews() {
//		showMap = (TextView) findViewById(R.id.button_show_map);
		tip = (TextView) findViewById(R.id.text_tip);
		listContainer = (ListView) findViewById(R.id.list_container);
		listContainer.setAdapter(adpter = new MyAdapter());
		listContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchPointBean bean = listDatas[position];
				getBundleNavi().putInt("index", position);
				getBundleNavi().putInt("searchKind", UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				getBundleNavi().putBoolean("TouchFlag", true);
				getBundleNavi().put("SearchPointBean", bean);

				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Detail.class);
			}
		});
	}
	
	private void initialize() {
		UIC_SreachCommon.setCurListActivityclass(ADT_POI_List.class);
//		cdialog = new CustomDialog(this);
		cprogressBarDialog = new CProgressDialog(this);
		cprogressBarDialog.setText(R.string.STR_MM_02_02_01_01);
		setDefaultBackground();
		searchNetType = getBundleNavi().getInt("searchNetType");
		if(searchNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
			//check online search.
			addActionItem2(R.drawable.navicloud_and_429a, R.string.STR_MM_02_02_01_03 ,new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					UISearchControlJNI.Instance().OnSelectListItem(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY, 
							index,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
					searchNetType = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
					cprogressBarDialog.show();
					getActionBar2().getActionItem(0).setVisibility(View.GONE);
				}
			});
		}
		//check map.
		addActionItem2(R.drawable.navicloud_and_446a, R.string.STR_MM_02_02_02_05 ,new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getBundleNavi().putInt("genreIndex", index);
				SearchPointBean bean = listDatas[0];
				getBundleNavi().putBoolean("isCheakMap", true);
				getBundleNavi().put("SearchPointBean", bean);
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Result.class);
			}
		});
		
		if(SystemTools.EDITION_LUXGEN.equals(SystemTools.getApkEdition())) {
			//distance sort.
			addActionItem2(R.drawable.navicloud_and_446a, R.string.STR_MM_02_02_02_10 ,new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					UISearchControlJNI.Instance().OnSelectListItemNearby(index,searchNetType
							,UISearchControlJNI.SRCH_SORT_TYPE_DISTANCE);
					cprogressBarDialog.show();
				}
			});

			//distance sort.
			addActionItem2(R.drawable.navicloud_and_446a, R.string.STR_MM_02_02_02_11 ,new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					UISearchControlJNI.Instance().OnSelectListItemNearby(index,searchNetType
							,UISearchControlJNI.SRCH_NET_TYPE_ALPHABET);
					cprogressBarDialog.show();
				}
			});
		}
		
		String address = PointTools.getInstance().getPointName();
		if("".equals(address)) {
//			tip.setText(R.string.STR_COM_018);
			PointTools.getInstance().requestCenterName(new PointDetailInfoListener() {
				
				@Override
				public void onPointDetailInfoRequstFinished(String name,
						String address, String tel, long distance) {
					if(tip != null) {
						tip.setText(getString(R.string.STR_MM_02_02_02_01, name));
					}
					
				}
			});
		} else {
			tip.setText(getString(R.string.STR_MM_02_02_02_01, address));
		}
//		refreshList();
	}

	
	private void initActionbar() {
		index = getBundleNavi().getInt("index");
		setActionbarInfo(index);
	}
	
	private void setActionbarInfo(int genreIndex) {
		int srch_type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY;
		UISearchResultJNI srchResult = UISearchControlJNI.Instance().GetSearchResult(srch_type);
		String name = srchResult.GetListItemNameAt(genreIndex, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
		setTitle(name);
	}

	
	private void refreshList() {
		
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
		
		int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
		if(count > UISearchControlJNI.MAX_SEARCH_RESULT_SIZE){
			count = UISearchControlJNI.MAX_SEARCH_RESULT_SIZE;
		}
		//if not search data,then clear list.
		listDatas = new SearchPointBean[count];
		String[] names = new String[count];
		long[] lons = new long[count];
		long[] lats = new long[count];
		
		for(int i=0; i<count; i++) {
			SearchPointBean bean = SearchPointBean.createSearchPointBean(searchResult, i);
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
	protected void OnDestroy() {
		PointTools.getInstance().removePointListener();
		super.OnDestroy();
		UISearchControlJNI.Instance().DestroySearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
	}

	@Override
	protected void OnResume() {
		super.OnResume();
		refreshList();
	}

	@Override
	public void onBack() {
		UISearchControlJNI.Instance().OnBack(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
		MenuControlIF.Instance().BackWinChange();
	}

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		UnlockScreen();
		int iTriggerID = cTriggerInfo.GetTriggerID();
		switch (iTriggerID) {
			case NSTriggerID.UIC_MN_TRG_UISR_SEARCH_OK:
				PLog.i("Trigger", "Back Trigger");
				MenuControlIF.Instance().BackWinChange();
				break;
			case NSTriggerID.UIC_MN_TRG_UISR_SEARCH_ERROR:
				break;
			case NSTriggerID.UIC_MN_TRG_UISR_SEARCH_EXCEPTION:
				break;
			case NSTriggerID.UIC_MN_TRG_POINT_ADD_POINT:
//				if(cTriggerInfo.GetlParam1() == jniPointControl_new.UIC_PNT_TRG_SUCCESS) {
					CustomToast.showToast(this, R.string.STR_MM_02_02_02_04, 500);
//                }
//				refreshList();	
				break;
			case NSTriggerID.UIC_MN_TRG_POINT_DELETE_RECORD:
//				if(cTriggerInfo.GetlParam1() == jniPointControl_new.UIC_PNT_TRG_SUCCESS){
					CustomToast.showToast(this, R.string.STR_MM_02_02_04_22, 500);
//				}
				PLog.i("icon", "UIC_PNT_TRG_SUCCESS");
//				refreshList();
				break;
			case NSTriggerID.UIC_MN_TRG_PNT_DATA_PREPARE_FINISH:
				refreshList();
				break;
			case NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED:
				cprogressBarDialog.dismiss();
				if(cTriggerInfo.m_lParam2 != 0) {
					Log.d("UIMsgControl", "continue OnSelectListItem : error type = " 
							+ SearchTools.getErrorType((int)cTriggerInfo.m_lParam2));
					CustomToast.showToast(this, R.string.STR_MM_02_03_01_07, 1000);
					return true;
				}
				if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI){
					UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
					int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
					if(count != 0){
						refreshList();
					}else{
						if(noneSrchresultDialog == null){
							noneSrchresultDialog = new CustomDialog(this);
							noneSrchresultDialog.setEnterBackKeyAllowClose(true);
						}
						noneSrchresultDialog.setTitle(R.string.STR_MM_02_02_01_05);
						noneSrchresultDialog.setMessage(R.string.MSG_02_01_01_01);
						noneSrchresultDialog.setNegativeButton(R.string.STR_MM_02_02_01_04, null);
						noneSrchresultDialog.show();
					}
				}
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
					refreshList();
					CustomToast.showToast(this, R.string.STR_MM_02_02_02_04, 500);
				}
				break;
			case NSTriggerID.POINT_RESPONSE_DELETE_POINT:
				if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
					CustomToast.showToast(this, R.string.MSG_02_02_04_04, 500);
					checkBox.setChecked(true);
				} else {
					refreshList();
					CustomToast.showToast(this, R.string.STR_MM_02_02_04_22, 500);
				}
				break;
			case NSTriggerID.POINT_RESPONSE_TYPE_SYNC_POINT:
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
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		refreshList();
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listDatas.length;
		}

		@Override
		public Object getItem(int position) {
			return listDatas[position];
		}

		@Override
		public long getItemId(int position) {
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
						convertView = new ListItemSearchResult(ADT_POI_List.this, name, address, dStr, R.drawable.navicloud_and_556a);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
						convertView = new ListItemSearchResult(ADT_POI_List.this, name, address, dStr, R.drawable.navicloud_and_555a);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
						convertView = new ListItemSearchResult(ADT_POI_List.this, name, address, dStr, R.drawable.navicloud_and_557a);
						break;
					default:
						break;
					}
					
					if(convertView != null) {
						convertView.setTag(bean);
						
					}
				}
				else{
					convertView = new ListItemSearchResult(ADT_POI_List.this, name, address, dStr, R.drawable.navicloud_and_569a);
					convertView.setTag(bean);
					ListItemSearchResult item = (ListItemSearchResult) convertView;
					item.setVisibility();
					item.setIsEnabled(UserControl_ManagerIF.Instance().HasLogin());
					item.setClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							if(!UserControl_ManagerIF.Instance().HasLogin()){
								if(noLoginDialog == null){
									noLoginDialog = new CustomDialog(ADT_POI_List.this);
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
								UIPointControlJNI.Instance().reqDeleteFavorite(searchbean.getSeqId());
							}
						}
					});
					if(seqId.length > position){
						item.setCheaked(seqId[position] != UIPointControlJNI.RESULT_POINT_INVALID_ID);
					}
				}
				if(convertView != null){
					((ListItemSearchResult) convertView).setRouteListener(new OnClickListener() {
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
								RouteCalcController.instance().rapidRouteCalculateWithData(bean.getName(), bean.getLonlat()[0], bean.getLonlat()[1], bean.getAddress(), bean.getTelNo());
							}
						}
					});
				}
			} else {
				ListItemSearchResult item = (ListItemSearchResult) convertView;
				if(seqId.length > position){
//					Log.d("UIMsgControl", "POI position = " + position + "; sedID = " + seqId[position]);
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
