package com.billionav.navi.naviscreen.download;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListItemMapDataDownLoad;
import com.billionav.navi.download.OfflinePackageInfo;
import com.billionav.navi.naviscreen.base.OfflineMapDataManager;
import com.billionav.navi.naviscreen.base.UIOflLog;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.ui.R;

public class DownLoad_ListContainer extends TabHostListContainer{
	private ArrayList<OfflinePackageInfo> currentList = null;
	private boolean btnisenable = true;
	private int curindex;
	public DownLoad_ListContainer(Context context,boolean isexpandable) {
		super(context,isexpandable);
		// TODO Auto-generated constructor stub
		initialize();
		adaptData();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		if(PConnectReceiver.getConnectType() == PConnectReceiver.CONNECT_TYPE_WIFI){
			btnisenable = true;
		}
		else{
			btnisenable = false;
		}
	}


	@Override
	protected boolean listviewOnGroupClick(ExpandableListView parent, View v,
			final int groupPosition, long id) {
			super.listviewOnGroupClick(parent, v, groupPosition, id);
			if(btnisenable){
				//1200000000 is nationwide package of areacode.
				if(OfflineMapDataManager.getInstance().judgeisthereallworldpackage() != OfflineMapDataManager.IN_UNDOWNLOAD_LIST_STATUS
						||currentList.get(groupPosition).getAreaCode() == 1200000000){
					if(currentList.get(groupPosition).getAreaCode() == 1200000000 && currentList.get(groupPosition).getUpdateState() == OfflinePackageInfo.W3_US_DOWNLOADING){
						CustomDialog cDialog = new CustomDialog(getContext());
						cDialog.setMessage(getContext().getString(R.string.MSG_MM_01_04_01_02));
						cDialog.setTitle(R.string.STR_MM_01_04_02_90);
						cDialog.setPositiveButton(R.string.STR_MM_01_04_02_92,null);
						cDialog.setNegativeButton(R.string.STR_MM_01_04_02_91, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								OfflineMapDataManager.getInstance().DownloadOfflinePackageOnClick(groupPosition,currentList);
								refresh("");
							}
						});
						cDialog.show();
					}else{
						OfflineMapDataManager.getInstance().DownloadOfflinePackageOnClick(groupPosition,currentList);
						refresh("");
					}
				}
				else{
					CustomDialog cDialog = new CustomDialog(getContext());
					cDialog.setMessage(R.string.MSG_MM_01_04_01_01);
					cDialog.setTitle(R.string.STR_MM_01_04_02_90);
					cDialog.setPositiveButton(R.string.MSG_00_00_00_09, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OfflineMapDataManager.getInstance().DownloadOfflinePackageOnClick(0,OfflineMapDataManager.getInstance().getUndownloadArrayList());
							OfflineMapDataManager.getInstance().DownloadOfflinePackageOnClick(groupPosition,currentList);
							refresh("");
						}
					});
					cDialog.setNegativeButton(R.string.MSG_00_00_00_12, null);
					cDialog.show();
				}
			}
			return false;
	}

	@Override
	protected boolean listviewOnChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
			super.listviewOnChildClick(parent, v, groupPosition, childPosition, id);
		return true;
	}


	@Override
	public int getListGroupCount() {
		// TODO Auto-generated method stub
		if(currentList == null) { return 0;
        }
		return currentList.size();
	}

	@Override
	public View getListGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OfflinePackageInfo item = null;
		item = currentList.get(groupPosition);
		double a = item.getDownloadSize();
		double b = item.getPackageSize();
		int progress = (int)(a/b*100);
//		int progress = (int)(a/b*100*0.95);
		if(convertView == null){
			ListItemMapDataDownLoad dataDownLoad = new ListItemMapDataDownLoad(getContext(), item.getAreaName(), item.getPackageSize(), item.getUpdateState(),progress,null,btnisenable);
			return dataDownLoad;
		}
		((ListItemMapDataDownLoad)convertView).setChildInfo(item.getPackageSize(), item.getAreaName(), item.getUpdateState(), progress,btnisenable);
		return convertView;
	}


	public void closeallexpandedrefresh() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	protected void refreshTab1(String searchFiler) {
		// TODO Auto-generated method stub
		currentList = OfflineMapDataManager.getInstance().getUndownloadArrayList();
		super.refreshTab1(searchFiler);
	}

	@Override
	protected void refreshTab2(String searchFiler) {
		// TODO Auto-generated method stub
		currentList = OfflineMapDataManager.getInstance().getDownloadingList();
		super.refreshTab2(searchFiler);
	}

	@Override
	protected void refreshTab3(String searchFiler) {
		// TODO Auto-generated method stub
		super.refreshTab3(searchFiler);
		currentList = OfflineMapDataManager.getInstance().getDownloadedList();
	}

	public void setEnabledBtn(boolean b) {
		// TODO Auto-generated method stub
		btnisenable = b;
	}


	
}
