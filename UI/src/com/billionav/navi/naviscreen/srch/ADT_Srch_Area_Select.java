package com.billionav.navi.naviscreen.srch;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.AreaInfo;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.listcomponent.ListItemMenu;
import com.billionav.navi.component.listcomponent.ListItemTextWithoutImg;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.ui.R;

public class ADT_Srch_Area_Select extends ActivityBase{

	private ListView listView;
	
	private ArrayList<String> list = new ArrayList<String>();
	private MyAdapter adapter;
	private CProgressDialog cprogressBarDialog = null;
	
	private AreaInfo[] areaInfos;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_search_area_select);
		
		findViews();
		initialize();
		
		setListeners();
		
		
	}

	private void setListeners() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				AreaInfo info = areaInfos[position];
				if(info.nextlevelflag) {
					getBundleNavi().putString("AreaName", info.name);
					UISearchControlJNI.Instance().reqAreaList(info.areacode);
					showProgressDialog();
				} else {
					UISearchControlJNI.Instance().reqSaveAreaInfo(info.name, info.areacode, info.lon, info.lat);
					getBundleNavi().putString("AreaName", info.name);
					BackSearchWinChange(ADT_Srch_Map.class);
				}
			}
		});
	}
	
	private void showProgressDialog() {
		if(cprogressBarDialog ==null) {
			cprogressBarDialog = new CProgressDialog(this);
			cprogressBarDialog.setText(R.string.STR_MM_02_02_01_01);
			cprogressBarDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK ){
						UISearchControlJNI.Instance().OnCancel(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
					}
					return false;
				}
			});

		}
		
		cprogressBarDialog.show();
	}
	
	private void dismissProgressDialog() {
		if(cprogressBarDialog != null) {
			cprogressBarDialog.dismiss();
		}
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {	
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED
				&& cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_AREA) {
			dismissProgressDialog();
			ForwardWinChange(ADT_Srch_Area_Select.class);
		}
		return super.OnTrigger(cTriggerInfo);
	}

	private void initialize() {
		list.clear();
		areaInfos = UISearchControlJNI.Instance().getAreaInfoArrays();
		for(int i=0; i<areaInfos.length; i++) {
			
			String name = areaInfos[i].name;
			list.add(name);
		}
		
		adapter.notifyDataSetChanged();
		
		String name = getBundleNavi().getString("AreaName");
		if(TextUtils.isEmpty(name)) {
			name = "All";
		}
		setTitle(name);
	}

	private void findViews() {
		listView = (ListView) findViewById(R.id.list_container);
		listView.setAdapter(adapter = new MyAdapter());
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String str = list.get(position);
			if(convertView == null){
				convertView = new ListItemMenu(ADT_Srch_Area_Select.this, str);
				convertView.setBackgroundResource(R.drawable.list_selector_background);
			} else {
				((ListItemMenu)convertView).setName(str);
			}
			return convertView;
		}
		
	}

}
