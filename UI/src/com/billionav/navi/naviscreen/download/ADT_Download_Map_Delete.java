package com.billionav.navi.naviscreen.download;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.component.listcomponent.ListViewNavi.SimpleAdapterBuilder;
import com.billionav.navi.download.OfflinePackageInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.base.OfflineMapDataManager;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.base.UIOflLog;
import com.billionav.ui.R;

public class ADT_Download_Map_Delete extends ActivityBase implements OnScreenBackListener{
	private static final int DOWNLOAD_IDLE =0;
	private static final int DOWNLOAD_DELETING = 1;
	private static final int DOWNLOAD_CANCELING = 2;
	
	private ListViewNavi list = null;
	private ArrayList<OfflinePackageInfo> downloadingList;
	private ArrayList<OfflinePackageInfo> downloadedList;
	private ArrayList<OfflinePackageInfo> deletePackageList;
	private SimpleAdapterBuilder builder;
	private int deleteState = DOWNLOAD_IDLE; 
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_download_map_delete);
		setDefaultBackground();
		setTitle(getResources().getString(R.string.STR_MM_01_02_01_04)+"("+getResources().getString(R.string.STR_MM_01_04_01_91)+")");
		init();
		findViews();
		setListener();
		builder = new SimpleAdapterBuilder(R.layout.list_item_delete_downloadmap, R.id.textName, R.id.textSize);
		for(OfflinePackageInfo d: deletePackageList) {
			builder.put(d.getAreaName(), new DecimalFormat("#########.##").format((double)d.getPackageSize()/1024d/1024d)+"M");
		}
		list.setAdapter(builder.createPartcularSimpleAdapter(this));
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	private void init(){
		deletePackageList = new ArrayList<OfflinePackageInfo>(); 
		downloadingList =  OfflineMapDataManager.getInstance().getDownloadingList();
		downloadedList = OfflineMapDataManager.getInstance().getDownloadedList();
		deletePackageList.addAll(downloadedList);
		deletePackageList.addAll(downloadingList);
	}
	private void setListener(){
		addActionItem3(R.string.STR_MM_01_03_03_01, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(deleteState != DOWNLOAD_IDLE){
					return;
				}
				showProgress();
				deleteState = DOWNLOAD_DELETING;
				final List<Integer>  selectednum = list.getCheckedItems();
				final Handler h = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						if(msg.what == -1){
							dismissProgress();
							deleteState = DOWNLOAD_IDLE;
								if(list.getCount() == 0){
									onBack();
								}
			    			}
						else{
							list.setItemChecked(selectednum.get(msg.what), false);
			    			builder.getDataList().remove((int)selectednum.get(msg.what));
			    			builder.notifyDataSetChanged();
						}
					}
					
				};
				//delete thread
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for(int i = selectednum.size()-1; i >= 0; i--){
							if(DOWNLOAD_DELETING != deleteState){
								break;
							}
							h.sendEmptyMessage(i);
							OfflineMapDataManager.getInstance().deleteOfflinePackage(deletePackageList.get(selectednum.get(i)));
							OfflineMapDataManager.getInstance().disposeDelPackage(deletePackageList.get(selectednum.get(i)));
						}
						h.sendEmptyMessage(-1);
					}
				}).start();
			}
		});
		addActionItem3(R.string.STR_MM_01_03_03_02, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.setAllItemChecked(list.getCheckedCount() < list.getCount()-1);
			}
		});
	}
	private void findViews(){
		list = (ListViewNavi)findViewById(R.id.list);
	}
	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		if(DOWNLOAD_DELETING == deleteState){
			deleteState = DOWNLOAD_CANCELING;
		}else if(DOWNLOAD_CANCELING == deleteState){
		}else if(DOWNLOAD_IDLE == deleteState){
			BackWinChange();
		}else{
		}
	}
	
}
