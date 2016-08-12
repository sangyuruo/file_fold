package com.billionav.navi.naviscreen.dest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.route.ADT_Route_Profile;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;

public class ADT_Route_Detail extends ActivityBase {
	
	private ListViewNavi listView;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(listView = new ListViewNavi(this));
		
		setTitleText(R.string.STR_MM_01_02_01_11);
		setDefaultBackground();
		
//		int padding = DensityUtil.dp2px(this, 6);
//		getActionBar2().getBelowLayout().setPadding(padding, padding, padding, padding);
		
		
		SimpleAdapter adapter = new SimpleAdapter(this, getListItemData(), R.layout.list_item_menu ,new String[]{"image","name"}, new int[]{R.id.item_image, R.id.item_text}); 
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0) {
					MenuControlIF.Instance().ForwardWinChange(ADT_Route_Profile.class);
				} else if(position == 1) {
					RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_SHOW);
					MenuControlIF.Instance().ForwardWinChange(ADT_Route_Main.class);
				} else {
					showDeleteDialog();
				}
			}
		});

	}
	
	private List<Map<String,Object>> getListItemData() {
		List<Map<String,Object>> poi_ListItems = new ArrayList<Map<String,Object>>(3);
		poi_ListItems.add(getDate(R.drawable.navicloud_and_448a, getString(R.string.STR_MM_01_02_01_12)));
		poi_ListItems.add(getDate(R.drawable.navicloud_and_449a, getString(R.string.STR_MM_01_02_01_13)));
		poi_ListItems.add(getDate(R.drawable.navicloud_and_450a, getString(R.string.STR_MM_01_02_01_14)));
		
		return poi_ListItems;
	}
	
	private Map<String, Object> getDate(int imageId, String nameId){
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("image", imageId);
		map.put("name", nameId);
		return map;
	}

	
	private void showDeleteDialog() {
		CustomDialog builder = new CustomDialog(this);
		builder.setTitle(R.string.MSG_03_01_02_02_03);
		builder.setMessage(R.string.MSG_03_01_02_02_04);
		builder.setNegativeButton(R.string.STR_COM_001, null);
		builder.setPositiveButton(R.string.STR_COM_003, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new UIPathControlJNI().DeleteRoute();
				BackSearchWinChange(ADT_Main_Map_Navigation.class);
			}
		});
		builder.show();
	}

}
