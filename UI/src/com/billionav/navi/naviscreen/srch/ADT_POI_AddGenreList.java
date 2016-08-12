package com.billionav.navi.naviscreen.srch;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListItemMenu;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.SearchTools;
import com.billionav.ui.R;

public class ADT_POI_AddGenreList extends ActivityBase {
	private ListView listView;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private MyAdapter adapter;
	private int i =5 ;
	
	private CProgressDialog cprogressDialog;
	private boolean isInitRequest = true;
	private boolean forAddGenre = true;
	
	private int genreid ;
	private String genreName;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.src_search_area_select);
		findViews();
		
		setListeners();
		
		forAddGenre = getBundleNavi().getBoolean("forAddGenre");
		genreid = getBundleNavi().getInt("genre");
		genreName = getBundleNavi().getString("name");
		if(genreName == null) {
			genreName = "";
		}
		
		UISearchControlJNI.Instance().reqTraditionalItemList(genreid, genreName,UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
		isInitRequest = true;
		showProgressDialog();
	}

	private void setListeners() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long genreid = (Long)list.get(arg2).get("genre");
				String genreName = (String) list.get(arg2).get("name");
				boolean hasNext = (Boolean) list.get(arg2).get("hasNext");
				
				getBundleNavi().putInt("genre", (int)genreid);
				getBundleNavi().putString("name", genreName);
				getBundleNavi().putBoolean("hasNext", hasNext);
				Log.d("UIMsgControl", "setOnItemClickListener: hasNext = " + hasNext);
				if(hasNext) {
					Log.d("UIMsgControl", "setOnItemClickListener: forAddGenre = " + forAddGenre);
					if(forAddGenre) {
						getBundleNavi().putBoolean("forAddGenre", true);
					}
					ForwardWinChange(ADT_POI_AddGenreList.class);
				} else {
					if(forAddGenre) {
						UISearchControlJNI.Instance().addPOIGenre(arg2);
						isInitRequest = false;
						showProgressDialog();
					} else {
						UISearchControlJNI.Instance().OnSelectListItem(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY, 
								arg2,UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
						showProgressDialog();
					}
				}
				
			}
		});
		
	}
	
	private void showProgressDialog() {
		if(cprogressDialog ==null) {
			cprogressDialog = new CProgressDialog(this);
			cprogressDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction()==KeyEvent.ACTION_UP)){
						UISearchControlJNI.Instance().OnCancel(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
						if(isInitRequest) {
							BackWinChange();
						}
					}
					return false;
				}});
		}
		cprogressDialog.show();
	}
	
	private void dismissProgressDialog() {
		if(cprogressDialog!=null) {
			cprogressDialog.dismiss();
		}
	}

	private void initlize() {
		list.clear();
		UISearchResultJNI searchresult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
		int  count = (int) searchresult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
		for(int i=0; i<count; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>(2);
			map.put("name", searchresult.GetListItemNameAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE));
			map.put("genre", searchresult.GetListItemGenreIDAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE));
			map.put("hasNext", searchresult.GetListItemHasNextFlag(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE));
			list.add(map);
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		if(cTriggerInfo.m_iTriggerID == NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED) {
			if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE) {
				if(cTriggerInfo.m_lParam2 != 0) {
					if(cTriggerInfo.m_lParam2 == UISearchControlJNI.UIC_SCM_ERROR_TYPE_DATA){
						UISearchControlJNI.Instance().reqTraditionalItemList(genreid, genreName,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
						return true;
					}
					dismissProgressDialog();
					Log.d("UIMsgControl", "reqTraditionalItemList : error type = " 
							+ SearchTools.getErrorType((int)cTriggerInfo.m_lParam2));
					CustomToast.showToast(this, R.string.STR_MM_02_03_01_07, 1000);
					return true;
				}
				dismissProgressDialog();
				
				if(isInitRequest) {
					initlize();
				} 
				// for add poi genre, should be back;
				else {
					getBundleNavi().put("addGenre", true);
					BackSearchWinChange(ADT_POI_Main.class);
				}
			} else if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
				if(cTriggerInfo.m_lParam2 != 0) {
					dismissProgressDialog();
					CustomToast.showToast(this, "error", 1000);
					return true;
				}
				dismissProgressDialog();
				UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				if(searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) == 0) {
					Toast.makeText(this, R.string.MSG_02_01_01_01, Toast.LENGTH_LONG).show();
				} else {
					MenuControlIF.Instance().ForwardWinChange(ADT_POI_List.class);
				}

			}
			
		}
		return super.OnTrigger(cTriggerInfo);
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
			String str = (String) list.get(position).get("name");
			if(convertView == null){
				convertView = new ListItemMenu(ADT_POI_AddGenreList.this, str);
				convertView.setBackgroundResource(R.drawable.list_selector_background);
			} else {
				((ListItemMenu)convertView).setName(str);
			}
			return convertView;
		}
		
	}


}
