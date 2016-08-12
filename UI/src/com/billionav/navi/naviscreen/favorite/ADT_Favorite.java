package com.billionav.navi.naviscreen.favorite;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.actionbar.ActionBarInputItem;
import com.billionav.navi.component.actionbar.ActionbarInput;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.tab.TabHostItem;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.download.TabHostListContainer;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uicommon.UIC_SreachCommon;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_Favorite extends ActivityBase{
	private static final int RequestFavorites = 1;
	private static final int RequestHistories = 2;
	
	private static final int RequestFavoritesSingle = 3;
	
	private static final int RequestSyncFavorites = 4;
	private static final int RequestSyncHistories = 5;
	
	private TabHostNavi tabhost;
	private Faverate_ListContainer faverate_listContainer = null;
	private ActionbarInput searchinput;
	private CustomDialog cdialog = null;
	private CustomDialog noLoginDialog = null;
	
	private int requestSates = 0; 
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		initializereSource();
		initializeLoad();	
		requestFavorites();
	}
	
	private void requestFavorites() {
		UIPointControlJNI.Instance().reqGetFavorite("");
		requestSates = RequestFavorites;
//		showProgress();
	}
	
	//search function
	private void setSearchinput() {
		getActionBar2().addView(searchinput);
		searchinput.setVisibility(View.INVISIBLE);
		searchinput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				faverate_listContainer.refresh(s.toString());
				if(s.length() != 0){
					faverate_listContainer.refreshNoResultHint(true);
				}else{
					faverate_listContainer.refreshNoResultHint(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		searchinput.setSearchButtonListener(new ActionBarInputItem.OnSearchButtonListener() {
			
			@Override
			public void onSearchButtonClicked(View view, String inputText) {
				// TODO Auto-generated method stub
				searchinput.closeInputKeyBoard();
			}
		});
	}
	private void initializereSource()
	{
		UIC_SreachCommon.setCurListActivityclass(ADT_Favorite.class);
		searchinput = new ActionbarInput(this);
		searchinput.setText(R.string.STR_MM_01_03_01_06);
		faverate_listContainer = new Faverate_ListContainer(this,false);
		tabhost = new TabHostNavi(this,faverate_listContainer,searchinput);
	}
	
	private void initializeLoad() {
		addItems();
		setContentView(tabhost);
		setTitleText(R.string.STR_MM_01_03_01_06);
		setDefaultBackground();
		setSearchinput();
		//search button
		addActionItem(R.drawable.navicloud_and_428a, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tapSearch();
			}
		});
		if(!SystemTools.getApkEdition().equals(SystemTools.EDITION_LUXGEN)){
			//updata button
			addActionItem(R.drawable.navicloud_and_427a, new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(UserControl_ManagerIF.Instance().HasLogin()){
						if(faverate_listContainer.getCurrentListState() == TabHostListContainer.MY_TAB_ONE){
							Log.d("UIPoint", "reqSyncFavorites()");
							UIPointControlJNI.Instance().reqSyncFavorites();
							requestSates = RequestSyncFavorites;
						} else {
							UIPointControlJNI.Instance().reqSyncHistroies();
							requestSates = RequestSyncHistories;
						}
						showProgress();
					}else{
						if(noLoginDialog == null){
							noLoginDialog = new CustomDialog(ADT_Favorite.this);
						}
						noLoginDialog.setTitle(R.string.STR_MM_10_04_02_20);
						noLoginDialog.setMessage(R.string.MSG_03_01_02_02_07);
						noLoginDialog.setNegativeButton(R.string.STR_MM_10_04_02_22, null);
						noLoginDialog.setPositiveButton(R.string.STR_MM_10_04_02_23, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ForwardWinChange(ADT_Auth_Login.class);
							}
						});
						noLoginDialog.show();
					}
				}
				
			});
		}
		//delete all
		addActionItem(R.drawable.navicloud_and_455a, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BundleNavi.getInstance().put("displayData", faverate_listContainer.getDisplayLists());
				BundleNavi.getInstance().put("searchFilerStr", searchinput.getText());
				BundleNavi.getInstance().putInt("currentListState", faverate_listContainer.getCurrentListState());
				ForwardWinChange(ADT_Favorite_Delete.class);
			}
		});
		faverate_listContainer.setOnItemClicklistener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchinput.closeInputKeyBoard();
				
			}
		});
	
	}
	
	private void addItems() {
		TabHostItem item_Faverate = new TabHostItem(this, 0, R.string.STR_MM_01_03_01_07,R.drawable.navicloud_and_766a,R.drawable.navicloud_and_766b);
		TabHostItem item_History = new TabHostItem(this, 0, R.string.STR_MM_06_01_01_121,R.drawable.navicloud_and_766a,R.drawable.navicloud_and_766b);
		tabhost.addItem(item_Faverate);
		tabhost.addItem(item_History);
	}
	//when exit delele select.
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		int currentIndex = faverate_listContainer.getCurrentListState();
		if(getBundleNavi().getBoolean("deleteback")) {
			UIPointData[] points = (UIPointData[]) getBundleNavi().get("deletedata");
			if(points != null) {
				if(currentIndex == TabHostListContainer.MY_TAB_ONE) {
					faverate_listContainer.setFavorites(points);
				} else {
					faverate_listContainer.setHistroys(points);
				}
			}
			faverate_listContainer.refresh(searchinput.getText());
		} else if(getBundleNavi().getBoolean("favoriteDetailBack")) {
			dismissProgress();
//			boolean isChanged = getBundleNavi().getBoolean("isChanged");
//			if(isChanged){
				UIPointControlJNI.Instance().reqGetFavorite("");
				if(currentIndex == TabHostListContainer.MY_TAB_ONE) {
					Log.d("UIPoint", "OnResume: single reqGetFavorite() ");
					requestSates = RequestFavoritesSingle;
				}else{
					Log.d("UIPoint", "OnResume: reqGetFavorite() and history");
					requestSates = RequestFavorites;
				}
//			}
		}
		//Synchronization 
	}
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		PLog.i("Trigger", "Trigger");
		
		switch(cTriggerInfo.m_iTriggerID){
		case NSTriggerID.POINT_RESPONSE_GET_POINT_LIST:
			if(requestSates == RequestFavorites) {
//				Log.d("UIPoint", "state is RequestHistories ; getFavoriteList()");
//				UIPointData[] FavoriteList= UIPointControlJNI.Instance().getFavoriteList();
				faverate_listContainer.setFavorites(UIPointControlJNI.Instance().getBookmarkData());
				faverate_listContainer.refresh(searchinput.getText());
				UIPointControlJNI.Instance().reqGetHistroy("");
				requestSates = RequestHistories;
			} else if(requestSates == RequestHistories) {
//				HistroyList = UIPointControlJNI.Instance().getHistroyList();
				faverate_listContainer.setHistroys(UIPointControlJNI.Instance().getBookmarkData());
				faverate_listContainer.refresh(searchinput.getText());
				dismissProgress();
				requestSates = 0;
			} else if(requestSates == RequestFavoritesSingle) {
//				Log.d("UIPoint", "state is RequestFavoritesSingle ; getFavoriteList()");
//				UIPointData[] FavoriteList= UIPointControlJNI.Instance().getFavoriteList();
				faverate_listContainer.setFavorites(UIPointControlJNI.Instance().getBookmarkData());
				faverate_listContainer.refresh(searchinput.getText());
				dismissProgress();
				requestSates = 0;
			}
			break;
		case NSTriggerID.POINT_RESPONSE_TYPE_SYNC_POINT:
			if(requestSates == RequestSyncFavorites || requestSates == RequestSyncHistories){
				if(cTriggerInfo.m_lParam1 == UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_RESULT_SUCCESS){
					CustomToast.showToast(this, R.string.MSG_01_03_01_11, 500);
					reqGetData();
				}else{
					CustomToast.showToast(this, R.string.MSG_01_03_01_12, 500);
					dismissProgress();
				}
			}
			break;
//		case NSTriggerID.POINT_RESPONSE_INIT_DONE:
//			if(cTriggerInfo.m_lParam1 == UIPointControlJNI.PntToUIResponse_PNT_RESULT_PNT_LOG_STATUS_LOGIN){
//				reqGetData();
//			}
//			break;
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	private void reqGetData(){
		if(requestSates == RequestSyncFavorites) {
			UIPointControlJNI.Instance().reqGetFavorite("");
			requestSates = RequestFavoritesSingle;
		} else if(requestSates == RequestSyncHistories) {
			UIPointControlJNI.Instance().reqGetHistroy("");
			requestSates = RequestHistories;
		} else {
			requestSates = 0;
		}
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(searchinput.getVisibility() == View.VISIBLE)
			{
				searchinput.setVisibility(View.INVISIBLE);
				searchinput.clearEditText();
				tabhost.setEnabled(true);
				faverate_listContainer.refresh("");
				return true;
			}
		}  
		else if(keyCode == KeyEvent.KEYCODE_SEARCH)
		{
			tapSearch();
			return true;
		}
		return super.OnKeyDown(keyCode, event);
	}
	private void tapSearch() {
		if (searchinput.getVisibility() == View.INVISIBLE) {
			searchinput.setVisibility(View.VISIBLE);
			searchinput.showKeyBoard();
			tabhost.setEnabled(false);
//				searchinput.setDropdownInputbox(true);
		} else {
			searchinput.setVisibility(View.INVISIBLE);
			tabhost.setEnabled(true);
		}
	}
	
}
