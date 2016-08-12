package com.billionav.navi.naviscreen.download;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.billionav.navi.component.actionbar.ActionBarInputItem.OnSearchButtonListener;
import com.billionav.navi.component.actionbar.ActionbarInput;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.tab.TabHostItem;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.OfflineMapDataManager;
import com.billionav.navi.naviscreen.favorite.TabHostNavi;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PConnectReceiver.NetConnectTypeListener;
import com.billionav.ui.R;

public class ADT_Download_Map extends ActivityBase{
	private TabHostNavi tabhost;
	private TabHostListContainer listContainer = null;
	private ActionbarInput searchinput;
	private CProgressDialog cProgressDialog;
	private NetConnectTypeListener netListener;
	private OfflineMapDataManager offlineMapDataManager = null;
	private View deleteBtn = null;
	private CustomDialog customDialog;

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		//download offlinepackage list.
		if(OfflineMapDataManager.getInstance() == null || !OfflineMapDataManager.getInstance().isRequsetlistOK()){
			OfflineMapDataManager.createInstance(this).requestOfflineData();
			showProgressDialog();
		}
		initResource();
		setSearchinput();
		addActionItem3(R.string.STR_MM_01_04_02_01, new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ForwardWinChange(ADT_Download_Map_Delete.class);
		}
	});
		PConnectReceiver.addListener(netListener = new NetConnectTypeListener() {

			@Override
			public void onReceive(int type) {
				switch(type)
				{
				case PConnectReceiver.CONNECT_TYPE_MOBILE:
					((DownLoad_ListContainer)listContainer).setEnabledBtn(false);
					break;
				case PConnectReceiver.CONNECT_TYPE_NONE:
					((DownLoad_ListContainer)listContainer).setEnabledBtn(false);
					break;
				case PConnectReceiver.CONNECT_TYPE_WIFI:
					((DownLoad_ListContainer)listContainer).setEnabledBtn(true);
					break;
				}
				((DownLoad_ListContainer)listContainer).refresh("");
		}});
	}
	private void isdeleteBtnClick() {
		deleteBtn = getActionBar2().getActionItem(0);
		//none download data
		if(offlineMapDataManager.getDownloadedList().size() == 0 && offlineMapDataManager.getDownloadingList().size() == 0){
			deleteBtn.setEnabled(false);
		}
		else{
			deleteBtn.setEnabled(true);
		}
	}
	public void showProgressDialog(){
		if(cProgressDialog == null){
			cProgressDialog = new CProgressDialog(this); 
		}
		cProgressDialog.setText(getResources().getString(R.string.STR_COM_018));
		cProgressDialog.setCancelable(true);
		cProgressDialog.show();
		cProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				dismissProgressDialog();
				BackWinChange();
				return true;
			}
		});
	}
	
	public void dismissProgressDialog(){
		if(cProgressDialog!=null &&cProgressDialog.isShowing()){
			cProgressDialog.dismiss();
		}
	}
	
	private void setSearchinput() {
		searchinput = (ActionbarInput) getActionBar2();
		tabhost.setSearchinput(searchinput);
		searchinput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				offlineMapDataManager.fetchDataAndLayersItemList(s.toString());
				((DownLoad_ListContainer)listContainer).closeallexpandedrefresh();
				((DownLoad_ListContainer)listContainer).refresh(s.toString());
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
		searchinput.setSearchButtonListener(new OnSearchButtonListener() {
			@Override
			public void onSearchButtonClicked(View view, String inputText) {
				
				searchinput.closeInputKeyBoard();
			}
		});
	}
	
	private void initResource() {
		listContainer = new DownLoad_ListContainer(this,true);
		tabhost = new TabHostNavi(this, listContainer);
		offlineMapDataManager = OfflineMapDataManager.getInstance();
		int status = OfflineMapDataManager.getInstance().GetStatus();
		if(status != PConnectReceiver.CONNECT_TYPE_WIFI){
			((DownLoad_ListContainer)listContainer).setEnabledBtn(false);
		}
		addItems();
		setContentView(tabhost,ActionbarInput.class);
		setDefaultBackground();
		setTitle(getResources().getString(R.string.STR_MM_01_02_01_04)+"("+getResources().getString(R.string.STR_MM_01_04_01_91)+")");
	}
	
	private void addItems() {
		// TODO Auto-generated method stub
		TabHostItem item_download = new TabHostItem(this, 0, R.string.STR_MM_01_04_02_86,R.drawable.navicloud_and_766a,R.drawable.navicloud_and_766b);
		TabHostItem item_downloading = new TabHostItem(this, 0, R.string.STR_MM_01_04_02_87,R.drawable.navicloud_and_766a,R.drawable.navicloud_and_766b);
		TabHostItem item_downloaded = new TabHostItem(this, 0, R.string.STR_MM_01_04_02_84,R.drawable.navicloud_and_766a,R.drawable.navicloud_and_766b);
		tabhost.addItem(item_download);
		tabhost.addItem(item_downloading);
		tabhost.addItem(item_downloaded);
	}
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
//		PConnectReceiver.removeListener(netListener);
	}

	@Override
	protected void OnResume() {
		super.OnResume();
		offlineMapDataManager.fetchDataAndLayersItemList(searchinput.getText().toString());
		((DownLoad_ListContainer)listContainer).closeallexpandedrefresh();
		((DownLoad_ListContainer)listContainer).refresh("");
		searchinput.setDropdownInputbox(false);
		new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				searchinput.setDropdownInputbox(true);
			}
			
		}.sendEmptyMessageDelayed(0, 300);
		isdeleteBtnClick();
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(searchinput.getText().length() != 0){
				searchinput.clearEditText();
				return true;
			}
		}
		return super.OnKeyDown(keyCode, event);
	}

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		// TODO Auto-generated method stub
		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_OFFLINE_PACKAGE_INFO_LIST:
			((DownLoad_ListContainer)listContainer).refresh("");
			break;
		case NSTriggerID.UIC_MN_TRG_OFFLINE_PACKAGE_DOWNLOAD_STATUS:
			((DownLoad_ListContainer)listContainer).refresh("");
			break;
		case NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_INITIALIZED:
			dismissProgressDialog();
			if(cTriggerInfo.m_lParam1 == offlineMapDataManager.UI_OLPC_RLRC_NETWORK_ERROR){
				if(customDialog == null){
					customDialog = new CustomDialog(this);
					customDialog.setTitle(R.string.STR_MM_01_04_01_105);
					customDialog.setMessage(R.string.MSG_MM_01_04_01_03);
					customDialog.setPositiveButton(R.string.MSG_00_00_00_11, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							BackWinChange();
						}
					});
					customDialog.show();
					customDialog.setCancelable(false);
				}
				return true;
			}
			//update button state.
			int status = OfflineMapDataManager.getInstance().GetStatus();
			if(status != PConnectReceiver.CONNECT_TYPE_WIFI){
				((DownLoad_ListContainer)listContainer).setEnabledBtn(false);
			}else{
				((DownLoad_ListContainer)listContainer).setEnabledBtn(true);
			}
			//update screen
			((DownLoad_ListContainer)listContainer).refresh("");
			dismissProgressDialog();
			isdeleteBtnClick();
			break;
		case NSTriggerID.UIC_MN_TRG_DL_MAP_IS_DOWNLOAD_DATA:
			isdeleteBtnClick();
			break;
		}
		return super.OnTrigger(cTriggerInfo);
	}
}
