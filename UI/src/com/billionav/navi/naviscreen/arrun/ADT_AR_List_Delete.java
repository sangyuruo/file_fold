package com.billionav.navi.naviscreen.arrun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.billionav.DRIR.Upload.UploadHandler;
import com.billionav.navi.component.listcomponent.ListViewNavi;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uicommon.UIC_ARVideoCommon;
import com.billionav.navi.uicommon.UIC_ARVideoCommon.VideoInfo;
import com.billionav.ui.R;

public class ADT_AR_List_Delete extends ActivityBase{

	private ListViewNavi list = null;
	private ArrayList<VideoInfo> videoInfoes_all = null;
	private DeleteAdapter adapter;
	private  static final int MAX_BITMAP_QUEUE_SIZE = 30;
	private final LinkedList<Bitmap> bitmapQueue = new LinkedList<Bitmap>();
	private boolean beAbleToLoadPic = true;
	private int count = 0;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_ar_list_delete);
		setTitle(R.string.STR_MM_05_02_01_12);
		setDefaultBackground();
		addBtnItem();
		list = (ListViewNavi) findViewById(R.id.list_01);
		videoInfoes_all = UIC_ARVideoCommon.getVideoInfoes(ADT_AR_List_Delete.this);

		if(videoInfoes_all.size()>0){
			
			adapter = new DeleteAdapter(this);
			list.setCacheColorHint(0);
			list.setAdapter(adapter);
			list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			list.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					switch (scrollState){
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						beAbleToLoadPic = true;
						adapter.notifyDataSetChanged();
						break;
					default:
						beAbleToLoadPic = false;
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		}
		
	}

	private void addBtnItem(){
		addActionItem3(R.string.STR_MM_01_03_03_01, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (list.getCheckedCount() > 0) {
					LockScreen();
					DeleteFileTask task = new DeleteFileTask();
					task.execute(0);
				}
				
			}
		});
		addActionItem3(R.string.STR_MM_01_03_03_02, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.setAllItemChecked(list.getCheckedCount() < list.getCount());
				if(list.getCheckedCount() < list.getCount()){
					UIC_ARVideoCommon.setIsAllSelected(false);
				}else{
					UIC_ARVideoCommon.setIsAllSelected(true);
				}
				
			}
		});
	}
	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_DRIR_UPLOAD:
			count--;
			if (iParams == UploadHandler.TRIGGER_FILE_DELETED_OK) {
				for (int i = 0; i < videoInfoes_all.size(); i++) {
					if (videoInfoes_all.get(i).getPath()
							.equals(triggerInfo.GetString1())) {
						videoInfoes_all.remove(i);
						list.setItemChecked(i, false);
						break;
					}
				}

			} else if (iParams == UploadHandler.TRIGGER_FILE_DELETED_NG) {

			}
			if (count == 0) {
				UnlockScreen();
				adapter.notifyDataSetChanged();
			}
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}
	}

	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
		 while(bitmapQueue.size()>0){
	    		Bitmap toDelete = bitmapQueue.removeFirst();
				if(toDelete != null){
					if(!toDelete.isRecycled()){
						toDelete.recycle();
					}
	     }
	     }
	}
	public class DeleteFileTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			List<Integer>  list1 = list.getCheckedItems();
			for(int i=list1.size()-1; i>=0; i--) {
				count++;
				UploadHandler.getInstance().deleteFile(videoInfoes_all.get(list1.get(i)).getPath(), videoInfoes_all.get(list1.get(i)).getType());
			}
			UIC_ARVideoCommon.setIsNeedReresh(true);
			if(list.getCheckedCount() < list.getCount()){
				UIC_ARVideoCommon.setIsAllSelected(false);
			}else{
				UIC_ARVideoCommon.setIsAllSelected(true);
			}
			publishProgress(1);
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	
	}
	public class DeleteAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private SimpleDateFormat sd;

		public DeleteAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			sd = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
		}
		@Override
		public int getCount() {
			return videoInfoes_all.size();
		}

		@Override
		public Object getItem(int position) {
			return videoInfoes_all.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHelper helper = null;
			if(convertView == null){
				convertView = inflater.inflate(
						R.layout.list_item_media_delete, parent, false);
				helper = new ViewHelper();
				helper.pic = (ImageView) convertView.findViewById(R.id.delete_image);
				helper.name = (TextView) convertView.findViewById(R.id.delete_name);
				helper.size = (TextView) convertView.findViewById(R.id.delete_size);
				convertView.setTag(helper);			
			}else{
				helper = (ViewHelper) convertView.getTag();
			}
			Bitmap bm = null;
			if(beAbleToLoadPic){
				if(2 == videoInfoes_all.get(position).getType()){
					bm = UIC_ARVideoCommon.getImageThumbnail(videoInfoes_all.get(position).getPath(),60,60);
				}else{
					bm = ThumbnailUtils.createVideoThumbnail(videoInfoes_all.get(position).getPath(), Images.Thumbnails.MICRO_KIND);
				}
			}
			if(bm == null){
				helper.pic.setImageResource(R.drawable.navicloud_and_592a);
			}else{
				helper.pic.setImageBitmap(bm);
			}
			if(bitmapQueue.size() < MAX_BITMAP_QUEUE_SIZE){
			}else{
				Bitmap toDelete = bitmapQueue.removeFirst();
				if(toDelete != null){
					if(!toDelete.isRecycled()){
						toDelete.recycle();
					}
				}
			}
			if(bm != null){
				bitmapQueue.add(bm);
			}
			String txt1 = sd.format(videoInfoes_all.get(position).getDate());
			helper.name.setText(txt1);
			String sizeStr = null;
			if((videoInfoes_all.get(position).getSize()/1024)>1024){
				sizeStr = String.valueOf(UIC_ARVideoCommon.b2m(videoInfoes_all.get(
						position).getSize()))+"MB";
			}else{
				sizeStr = String.valueOf(videoInfoes_all.get(
						position).getSize()/1024)+"KB";
			}
			helper.size.setText(sizeStr);
			return convertView;
		}
		
		private class ViewHelper{
			ImageView pic = null;
			TextView name = null;
			TextView size = null;
		}
	}
}
