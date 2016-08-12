package com.billionav.navi.component.listcomponent;

import java.text.DecimalFormat;

import com.billionav.ui.R;
import com.billionav.navi.download.OfflinePackageInfo;
import com.billionav.navi.naviscreen.download.MapDataDownLoadBtn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListItemMapDataDownLoad extends ListItemInterface{
	private TextView textgroupplace = null;
	private TextView textgroupsize = null;
	private TextView textchildplace = null;
	private TextView textchildsize = null;
	private TextView textprogress = null;
	private TextView texttitle = null;
	private ProgressBar progressBar = null;
	private ImageView open_List = null;
	private MapDataDownLoadBtn downloadBtn = null;
	private RelativeLayout downloadTitle = null;
	private RelativeLayout downloadGroup = null;
	private RelativeLayout downloadChild = null;
	private ImageView iscompleted = null;
	//child
	public ListItemMapDataDownLoad(Context context,String pointName,int size,int state,int progress,OnClickListener btnonClickListener,boolean isEnabled) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_map_data_download, this);
		findViews();
		setChildInfo(size, pointName, state, progress, isEnabled);
		
	}
	//Group
	public ListItemMapDataDownLoad(Context context,String pointName,int size,boolean isExpanded){
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_map_data_download, this);
		findViews();
		setGroupInfo(size, pointName, isExpanded);
		
	}
	//title
	public ListItemMapDataDownLoad(Context context,String titleName){
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_map_data_download, this);
		findViews();
		setTitle(titleName);
	}
	private void findViews() {
		textgroupplace = (TextView)findViewById(R.id.ListItemMapDataDownLoad_group_placetext);
		textgroupsize = (TextView)findViewById(R.id.ListItemMapDataDownLoad_group_sizetext);
		textchildplace = (TextView)findViewById(R.id.ListItemMapDataDownLoad_child_placetext);
		textchildsize = (TextView)findViewById(R.id.ListItemMapDataDownLoad_child_sizetext);
		texttitle = (TextView)findViewById(R.id.title_tv);
		downloadBtn = (MapDataDownLoadBtn)findViewById(R.id.ListItemMapDataDownLoad_btn);
		textprogress = (TextView)findViewById(R.id.ListItemMapDataDownLoad_progresstext);
		progressBar = (ProgressBar)findViewById(R.id.map_data_download_btnprogressBar);
		downloadTitle = (RelativeLayout)findViewById(R.id.downloadtitle);
		downloadGroup = (RelativeLayout)findViewById(R.id.downloadgroup);
		downloadChild = (RelativeLayout)findViewById(R.id.downloadchild);
		open_List = (ImageView)findViewById(R.id.open_List);
		iscompleted = (ImageView)findViewById(R.id.iscompleted);
	}
	public void setDownloadBtnEnabled(boolean enabled){
		downloadBtn.setMybtnEnabled(enabled);
	}
	private void setDownloadState(int state) {
		iscompleted.setVisibility(View.INVISIBLE);
		downloadBtn.setDownloadstate(state);
		switch (state) {
		default:
		case OfflinePackageInfo.W3_US_NO_PACKAGE:
			textprogress.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case OfflinePackageInfo.W3_US_COMPLETED:
			iscompleted.setVisibility(View.VISIBLE);
			textprogress.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case OfflinePackageInfo.W3_US_CAN_UPDATE :
			break;
		case OfflinePackageInfo.W3_US_PAUSE:
			textprogress.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			break;
		case OfflinePackageInfo.W3_US_DOWNLOADING:
			textprogress.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			break;
		case OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD:
			textprogress.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			break;
		}
	}
	private void setProgress(int progress)
	{
		textprogress.setText(progress+"%");
		progressBar.setProgress(progress);
	}
	private void setGroupSize(int size){
		if(size/1024/1024 > 0){
			textgroupsize.setText(size/1024/1024+"M");
		}
		else{
			textgroupsize.setText(new DecimalFormat("###").format(size/1024%1024)+"K");
		}
	}
	private void setGroupPlace(String place){
		textgroupplace.setText(place);
	}
	private void setChildSize(int size){
		if(size/1024/1024 > 0){
			textchildsize.setText(size/1024/1024+"M");
		}
		else{
			textchildsize.setText(new DecimalFormat("###").format(size/1024%1024)+"K");
		}
	}
	private void setChildPlace(String place)
	{
		textchildplace.setText(place);
	}
	public void setTitle(String titleName){
		downloadChild.setVisibility(View.GONE);
		downloadTitle.setVisibility(View.VISIBLE);
		downloadGroup.setVisibility(View.GONE);
		texttitle.setText(titleName);
	}
	public void setGroupInfo(int size, String place, boolean isExpanded){
		setGroupSize(size);
		setGroupPlace(place);
		downloadTitle.setVisibility(View.GONE);
		downloadChild.setVisibility(View.GONE);
		downloadGroup.setVisibility(View.VISIBLE);
		if(isExpanded){
			open_List.setImageResource(R.drawable.navicloud_and_502a);
		}
		else{
			open_List.setImageResource(R.drawable.navicloud_and_501a);
		}
	}
	public void setChildInfo(int size, String place, int state,int progress,boolean isEnabled){
		setChildSize(size);
		setChildPlace(place);
		setProgress(progress);
		setDownloadState(state);
		setDownloadBtnEnabled(isEnabled);
		downloadTitle.setVisibility(View.GONE);
		downloadChild.setVisibility(View.VISIBLE);
		downloadGroup.setVisibility(View.GONE);
	}
	public MapDataDownLoadBtn getMapDataDownLoadBtn(){
		return downloadBtn;
	}
}
