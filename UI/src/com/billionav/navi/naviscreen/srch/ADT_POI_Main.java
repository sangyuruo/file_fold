package com.billionav.navi.naviscreen.srch;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.PointTools.PointDetailInfoListener;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SearchTools;
import com.billionav.ui.R;

public class ADT_POI_Main extends ActivityBase{
	
	private GridView poi_Gridview = null;
	private TextView pointnametv = null;
	private List<Map<String,Object>> poi_ListItems = null;
	private SimpleAdapter poi_SimpleAdapter = null;
	private CProgressDialog cprogressBarDialog = null;
	private CustomDialog noneSrchresultDialog = null;
	private CustomDialog addGenrePOIDialog = null;
	private Handler handler = null;
	
	private int defaultGenreCount;
	//save current search index in order to prevent search off-line failed then search on-line again.
	private int currentSearchIndex = 0;
	private int searchNetType = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_poi_main);
		setActionbar();
		initResource();
		setListener();
		requestLogomark();
	}
	private void showNoneResultDialog(final int searchType){
		if(noneSrchresultDialog == null){
			noneSrchresultDialog = new CustomDialog(this);
			noneSrchresultDialog.setEnterBackKeyAllowClose(true);
		}
		noneSrchresultDialog.setTitle(R.string.STR_MM_02_02_01_05);
//		if(searchType == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE){
//			if(genreNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
//				//need modify String ID
//				noneSrchresultDialog.setMessage("No off-line data, whether or not to continue the online search.");
//			}else if(genreNetType == UISearchControlJNI.SRCH_NET_TYPE_ONLINE){
//				noneSrchresultDialog.setMessage(R.string.MSG_02_01_01_01);
//			}else{
//				
//			}
//			genreNetType = UISearchControlJNI.SRCH_NET_TYPE_NONE;
//		}else{
//			noneSrchresultDialog.setMessage(R.string.MSG_02_01_01_01);
//		}
		noneSrchresultDialog.setMessage(R.string.MSG_02_01_01_01);
		if(searchNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
			noneSrchresultDialog.setPositiveButton(R.string.STR_MM_02_02_01_03, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(searchType == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE){
						UISearchControlJNI.Instance().StartSearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY,
								UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
						cprogressBarDialog.show();
					}else if(searchType == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI){
						UISearchControlJNI.Instance().OnSelectListItem(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY, 
								currentSearchIndex,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
						searchNetType = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
						cprogressBarDialog.show();
					}else{
						
					}
				}
			});
		}else{
			noneSrchresultDialog.setPositiveButton("", null);
		}
		noneSrchresultDialog.setNegativeButton(R.string.STR_MM_02_02_01_04, null);
		noneSrchresultDialog.show();
	}
	private void setActionbar()
	{
		//move Activity
		addActionItem(R.drawable.navicloud_and_428a, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Srch_Map.class);
			}
		});
		
	}
	private void initResource()
	{
		setDefaultBackground();
		setTitle(R.string.STR_MM_01_01_01_03);
		PointTools.getInstance().requestCenterName(new PointDetailInfoListener() {
			@Override
			public void onPointDetailInfoRequstFinished(String name,
					String address, String tel, long distance) {
				pointnametv.setText(getString(R.string.STR_MM_02_02_02_01, name));
			}
		});
		cprogressBarDialog = new CProgressDialog(this);
		cprogressBarDialog.setText(R.string.STR_MM_02_02_01_01);
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1) {
					cprogressBarDialog.dismiss();
					UISearchControlJNI.Instance().OnCancel(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
					CustomToast.showToast(ADT_POI_Main.this, R.string.STR_COM_013, 1000);
				}
				else if(msg.what == 2){
					noneSrchresultDialog.dismiss();
				}
				else{
					
				}
			}
			
		};
		poi_Gridview = (GridView)findViewById(R.id.adt_poi_main_grid);
		pointnametv = (TextView)findViewById(R.id.adt_poi_main_tv);
		poi_ListItems = new ArrayList<Map<String,Object>>();
		poi_SimpleAdapter = new SimpleAdapter(this, poi_ListItems,R.layout.poi_grid_view ,new String[]{"background","image","name"}, new int[]{R.id.grid_image_background,R.id.grid_image,R.id.grid_text}); 
		poi_Gridview.setAdapter(poi_SimpleAdapter);
		initGridviewNumColumns();
	}

	private void setListener() {
		poi_Gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// click last one, add item
				if(position == poi_Gridview.getCount()-1){
//			       getBundleNavi().putBoolean("forMoreList", true);
					//operate genre list from search.
			       //getBundleNavi().putBoolean("forAddGenre", true);
			       //ForwardWinChange(ADT_POI_AddGenreList.class);
					if(addGenrePOIDialog == null){
						addGenrePOIDialog = new CustomDialog(ADT_POI_Main.this);
					}
					addGenrePOIDialog.setTitle(R.string.STR_MM_02_03_01_04);
					addGenrePOIDialog.setEditText("");
					addGenrePOIDialog.setEditTextMaxLength(20);
					addGenrePOIDialog.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							if(keyCode == KeyEvent.KEYCODE_BACK) {
								if(addGenrePOIDialog.isShowing()){
									addGenrePOIDialog.dismiss();
								}
								return true;
							}
							return false;
						}
					});
					addGenrePOIDialog.setPositiveButton(R.string.STR_MM_02_03_01_05, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String genreName = ((CustomDialog)dialog).getEditText().toString();
							if(!TextUtils.isEmpty(genreName)){
								UISearchControlJNI.Instance().addCustomPOIGenre(genreName);
							}
						}
					});
					addGenrePOIDialog.show();
				}
//				else if(position == poi_Gridview.getCount()-2) {
//			       getBundleNavi().putBoolean("forAddGenre", true);
//			       ForwardWinChange(ADT_POI_AddGenreList.class);
//				}
				// click other, search
				else{
					if(m_iCurStatus != MENUBASE_STATUS.RESUME) {
						return;
					}
					cprogressBarDialog.show();
					currentSearchIndex = position;
				    UISearchControlJNI.Instance().OnSelectListItem(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY, 
				    		currentSearchIndex,UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
				    searchNetType = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
				    getBundleNavi().putInt("index", currentSearchIndex);
				    handler.sendEmptyMessageDelayed(1, 60000);
				}
		    
			}
		});
		poi_Gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				//To determine whether a default item
				
				if(position>=defaultGenreCount && position<poi_ListItems.size()-1) {
					final CustomDialog cdialog =  new CustomDialog(ADT_POI_Main.this);
					   cdialog.setTitle(R.string.STR_MM_02_03_01_08);
				       cdialog.setMessage(getString(R.string.STR_MM_01_03_03_01) + "  \""+poi_ListItems.get(position).get("name")+"\""+"  ?");
				       cdialog.setPositiveButton(R.string.STR_MM_01_03_03_01, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								UISearchControlJNI.Instance().deletePOIGenre(position);
							}
						});
				       cdialog.setNegativeButton(R.string.STR_COM_001, null);
				       cdialog.show();
				}
				return true;
			}
		});
		cprogressBarDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK ){
					UISearchControlJNI.Instance().OnCancel(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
				}
				return false;
			}
		});
	}
	private void requestLogomark() {
		UISearchControlJNI.Instance().StartSearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY,
				UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
//		genreNetType = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
		showProgress();
	}

	private void updataScreen(){
		int srch_type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY;
		UISearchResultJNI srchResult = UISearchControlJNI.Instance().GetSearchResult(srch_type);
		long count = srchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
		defaultGenreCount = srchResult.getDefaultGenreCount();
		if(count == 0){
//			showNoneResultDialog(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
			CustomToast.showToast(this, R.string.STR_COM_043, 1000);
		}
		poi_ListItems.clear();
		for(int i = 0; i < count; i++){
			String name = srchResult.GetListItemNameAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
			Map<String, Object> listItem = new HashMap<String, Object>();
			final int genreId = (int)srchResult.GetListItemGenreIDAt(i, 
					UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
//			Log.d("UIMsgControl", "genre id = " + genreId);
			int imageId = SearchTools.getMarkByCategoryCode(genreId);
			listItem.put("image", imageId);
			String tempName = "";
			try {
				tempName = new String(name.toString().trim().getBytes("GBK"), "ISO8859_1");
				if(tempName.length() > 8){
					tempName = tempName.substring(0, 8);
				}
				tempName = new String(tempName.toString().getBytes("ISO8859_1"), "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			listItem.put("name", tempName);
//			if(i<defaultGenreCount) {
				listItem.put("background", R.drawable.poi_image_selector);
//			} else {
//				listItem.put("background", R.drawable.navicloud_and_240a);
//			}
			poi_ListItems.add(listItem);
		}
		
		addDefaultItem();
	}
	
	private void addDefaultItem(){
		//add add item.
		Map<String, Object> listItem = new HashMap<String, Object>();
		listItem.put("image", R.drawable.navicloud_and_240a);
		listItem.put("background", null);
		listItem.put("name", " ");
		poi_ListItems.add(listItem);
		
		//add more item.
//		Map<String, Object> listItem2 = new HashMap<String, Object>();
//		listItem2.put("image", SearchTools.getMarkByCategoryCode(-1));
//		listItem2.put("background", R.drawable.poi_image_selector);
//		listItem2.put("name", "More");
//		poi_ListItems.add(listItem2);
		
		poi_SimpleAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		int iTriggerID = cTriggerInfo.GetTriggerID();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED: 
			Log.d("UIMsgControl", "receive UIC_MN_TRG_POINT_SEARCH_FINISHED; param1 = " + cTriggerInfo.m_lParam1);
			handler.removeMessages(1);
			cprogressBarDialog.dismiss();
			onSearchOKTriggerReceived(cTriggerInfo);
			return true;	

		}
		return false;
	}

	public void onSearchOKTriggerReceived(NSTriggerInfo cTriggerInfo)
	{
		PLog.i("Trigger", "Trigger Received");
		
		
		// getList, when request list, add item and delete item, get list again
		if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE) {
			if(cTriggerInfo.m_lParam2 != 0) {
//				if(cTriggerInfo.m_lParam2 == UISearchControlJNI.UIC_SCM_ERROR_TYPE_DATA){
//					UISearchControlJNI.Instance().StartSearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY,
//							UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
//				}else{
					dismissProgress();
//					showNoneResultDialog(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
					CustomToast.showToast(this, this.getString(R.string.STR_COM_044) + " " + cTriggerInfo.m_lParam2, 1000);
					poi_ListItems.clear();
					addDefaultItem();
//				}
//				if(genreNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
//					UISearchControlJNI.Instance().StartSearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY,
//							UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
//					genreNetType = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
//				}else{
//					showNoneResultDialog(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_GENRE);
//					poi_ListItems.clear();
//					addDefaultItem();
//				}
				return;
			}
			dismissProgress();
			updataScreen();
		}
		// get search result.
		else if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
			if(cTriggerInfo.m_lParam2 != 0) {
//				if(cTriggerInfo.m_lParam2 == UISearchControlJNI.UIC_SCM_ERROR_TYPE_DATA){
//					showNoneResultDialog(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
					CustomToast.showToast(this, R.string.STR_COM_044 + " " + cTriggerInfo.m_lParam2, 1000);
					return;
//				}
//				Log.d("UIMsgControl", "OnSelectListItem : error type = " + SearchTools.getErrorType((int)cTriggerInfo.m_lParam2));
//				return;
			}
			UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY);
			if(searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) == 0) {
//				showNoneResultDialog(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI);
				CustomToast.showToast(this, R.string.STR_COM_043, 1000);
			} else {
				BundleNavi.getInstance().putInt("currentSearchIndex", currentSearchIndex);
				BundleNavi.getInstance().putInt("searchNetType", searchNetType);
				MenuControlIF.Instance().ForwardWinChange(ADT_POI_List.class);
			}
		}
	}


	@Override
	protected void OnResume() {
		// TODO Auto-generated method stub
		super.OnResume();
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		boolean genre = getBundleNavi().getBoolean("addGenre");
		Log.d("SRCH", "OnResume getBundleNavi genre = " + genre);
		if(null != addGenrePOIDialog && addGenrePOIDialog.isShowing()) {
			addGenrePOIDialog.requestSoftKeyboard(this);
		}
		if(genre) {
			updataScreen();
		}
	}
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		NSViewManager.GetViewManager().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	protected void OnDestroy() {
		super.OnDestroy();

	}
	public void initGridviewNumColumns(){
        if(ScreenMeasure.isPortrait()){
	        poi_Gridview.setNumColumns(4);
        }
        else{
	        poi_Gridview.setNumColumns(6);
        }
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initGridviewNumColumns();
	}
	
	private void jump2Map() {
		MenuControlIF.Instance().BackWinChange();
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
}
