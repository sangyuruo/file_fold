package com.billionav.navi.naviscreen.dest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.download.ADT_Download_Map;
import com.billionav.navi.naviscreen.favorite.ADT_Favorite;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.route.ADT_Route_Profile;
import com.billionav.navi.naviscreen.setting.ADT_Notification_Disp_Activity;
import com.billionav.navi.naviscreen.setting.ADT_Schedule_Activity;
import com.billionav.navi.naviscreen.tools.ADT_Tools_Main;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;


public class ADT_Overflow_Menu extends ActivityBase {
	private static final int requestNone = 0;
	private static final int requestHome = 1;
	private static final int requestCompany = 2;
	
	private SimpleAdapter adapter;
	private boolean isEnableHome = false;
	private boolean isEnableCompany = false;
	private int reqId = requestNone;
	
	UIPointData homedata = null;
	UIPointData companydata = null;
//	private Class<?>[] items = {ADT_Favorite.class, ADT_Route_Detail.class, ADT_Download_Map.class,  ADT_Tools_Main.class};
	

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		
		ListViewNavi listView= new ListViewNavi(this);
		setContentView(listView);
		
		setTitleText(R.string.STR_MM_01_02_01_01);
		
		setDefaultBackground();
		
		setAdapter(listView);
		
//		if(UIPointControlJNI.Instance().isHomeRequested()){
//			isEnableHome = UIPointControlJNI.Instance().isHomeExist();
//		} else {
//			isEnableHome = false;
//			UIPointControlJNI.Instance().reqGetHome();
//			reqId = requestHome;
//		}
//		
//		if(UIPointControlJNI.Instance().isCompanyRequested()) {
//			isEnableCompany = UIPointControlJNI.Instance().isComapanyExist();
//		} else if(UIPointControlJNI.Instance().isHomeRequested()) {
//			isEnableCompany = false;
//			UIPointControlJNI.Instance().reqGetCompany();
//			reqId = requestCompany;
//		} else {
//			isEnableCompany = false;
//		}
		
		adapter.notifyDataSetChanged();

	}
	
	private List<Map<String,Object>> getListItemData() {
		List<Map<String,Object>> poi_ListItems = new ArrayList<Map<String,Object>>(8);
		poi_ListItems.add(getDate(R.drawable.navicloud_and_462a, getString(R.string.STR_COM_037)));
		poi_ListItems.add(getDate(R.drawable.navicloud_and_456a, getString(R.string.STR_MM_01_02_01_02)));
		
		poi_ListItems.add(getDate(R.drawable.navicloud_and_448a, getString(R.string.STR_MM_01_02_01_12)));
		poi_ListItems.add(getDate(R.drawable.navicloud_and_449a, getString(R.string.STR_MM_01_02_01_13)));
		poi_ListItems.add(getDate(R.drawable.navicloud_and_450a, getString(R.string.STR_MM_01_02_01_14)));
		
		poi_ListItems.add(getDate(R.drawable.navicloud_and_457a, getString(R.string.STR_MM_01_02_01_04)));
		poi_ListItems.add(getDate(R.drawable.navicloud_and_458a, getString(R.string.STR_MM_01_02_01_10)));
		// go home 
		poi_ListItems.add(getDate(R.drawable.navicloud_and_460a, getString(R.string.STR_MM_01_02_01_15)));
		// go company
		poi_ListItems.add(getDate(R.drawable.navicloud_and_461a,getString(R.string.STR_MM_01_02_01_16)));
		
		return poi_ListItems;
	}
	
	private Map<String, Object> getDate(int imageId, String nameId){
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("image", imageId);
		map.put("name", nameId);
		return map;
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		reqId = requestHome;
		UIPointControlJNI.Instance().reqGetHome();
	}
	
	private void showDeleteDialog() {
		CustomDialog builder = new CustomDialog(this);
		builder.setTitle(R.string.MSG_03_01_02_02_03);
		builder.setMessage(R.string.MSG_03_01_02_02_04);
		builder.setNegativeButton(R.string.STR_COM_001, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BundleNavi.getInstance().putBoolean("Navigation", true);
				BackSearchWinChange(ADT_Main_Map_Navigation.class);
			}
		});
		builder.setPositiveButton(R.string.STR_COM_003, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RouteCalcController.instance().DeleteRoute();
				adapter.notifyDataSetChanged();
				BundleNavi.getInstance().putBoolean("Navigation", true);
				BackSearchWinChange(ADT_Main_Map_Navigation.class);
			}
		});
		builder.show();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {

		if(cTriggerInfo.m_iTriggerID == NSTriggerID.POINT_RESPONSE_GET_POINT_LIST){
			if(cTriggerInfo.m_lParam1 != UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS) {
				return true;
			}
			if(reqId == requestHome) {
//				UIPointControlJNI.Instance().setHomeRequested();
				isEnableHome = UIPointControlJNI.Instance().isHomeExist();
				if(isEnableHome){
					homedata = UIPointControlJNI.Instance().getHome();
				}else{
					homedata = null;
				}
				adapter.notifyDataSetChanged();
				reqId = requestCompany;
				UIPointControlJNI.Instance().reqGetCompany();
			} else if(reqId == requestCompany) {
//				UIPointControlJNI.Instance().setCompanyRequested();
				isEnableCompany = UIPointControlJNI.Instance().isComapanyExist();
				if(isEnableCompany){
					companydata = UIPointControlJNI.Instance().getCompany();
				}else{
					companydata = null;
				}
				reqId = requestNone;
				adapter.notifyDataSetChanged();
			}
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	private void setAdapter(ListView listView) {
		adapter = new SimpleAdapter(this, getListItemData(), R.layout.list_item_menu, new String[]{"image","name"}, new int[]{R.id.item_image, R.id.item_text}){
			@Override
			public boolean isEnabled(int position) {
				// TODO Auto-generated method stub
				switch(position) {
				case 2:
				case 4:
					return UIC_RouteCommon.Instance().isRouteExistAndGuideOn(); 
//				case 1:
				case 5:
				case 6:
					return false;
				case 7:
					return isEnableHome;
				case 8:
					return isEnableCompany;
					default:return super.isEnabled(position); 
				}
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				(v.findViewById(R.id.item_text)).setEnabled(isEnabled(position));
				((ImageView)(v.findViewById(R.id.item_image))).getDrawable().setAlpha(isEnabled(position)?255:100);
				 
				if(position == 7 && isEnabled(position)) {
//					UIPointData homedata = UIPointControlJNI.Instance().getHome();
					if(homedata != null){
						String address = TextUtils.isEmpty(homedata.getAddress())? getString(R.string.STR_MM_02_02_04_15) : homedata.getAddress();
						((TextView)v.findViewById(R.id.item_text)).append("("+address+")");
					}
				}
				
				if(position == 8 && isEnabled(position)) {
//					UIPointData companydata = UIPointControlJNI.Instance().getCompany();
					if(companydata != null){
						String address = TextUtils.isEmpty(companydata.getAddress())? getString(R.string.STR_MM_02_02_04_15): companydata.getAddress();
						((TextView)v.findViewById(R.id.item_text)).append("("+address+")");
					}                                                                                                        
				}
				return v;
			}
		}; 
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				switch(position){
				case 1:
					MenuControlIF.Instance().ForwardWinChange(ADT_Favorite.class);
					break;
				case 0:
					ForwardWinChange(ADT_Notification_Disp_Activity.class);
					break;
				case 2:
					MenuControlIF.Instance().ForwardWinChange(ADT_Route_Profile.class);
					break;
				case 3:
					RouteCalcController.instance().setRoutePointFindPurpose(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()? 
							RouteCalcController.ROUTE_POINT_FIND_PURPOSE_SHOW : RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NO_ROUTE_EDIT);
					MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
					break;
				case 4:
					showDeleteDialog();
					break;
				case 5:
					MenuControlIF.Instance().ForwardWinChange(ADT_Download_Map.class);
					break;
				case 6:
//					MenuControlIF.Instance().ForwardWinChange(ADT_Tools_Main.class);
					break;
				case 7:
//					homedata = UIPointControlJNI.Instance().getHome();
					if(null == homedata) {
						return;
					}
					String homename = getResources().getString(R.string.STR_MM_01_02_01_17);
					RouteCalcController.instance().rapidRouteCalculateWithData(homename, homedata.getLon(), homedata.getLat(), homedata.getAddress(), homedata.getTelNo());
					break;
				case 8:
					if(null == companydata) {
						return;
					}
//					companydata = UIPointControlJNI.Instance().getCompany();
					String companyname = getResources().getString(R.string.STR_MM_01_02_01_18);
					RouteCalcController.instance().rapidRouteCalculateWithData(companyname, companydata.getLon(), companydata.getLat(), companydata.getAddress(), companydata.getTelNo());
					break;
				default:
					break;
				}
//				MenuControlIF.Instance().ForwardWinChange(items[position]);
			}
		});
	}
}

