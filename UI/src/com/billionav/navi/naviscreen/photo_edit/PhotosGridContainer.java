package com.billionav.navi.naviscreen.photo_edit;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.gridcomponent.gridViewItemAlbum;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.uitools.BitmapCacheImage;
import com.billionav.ui.R;

public class PhotosGridContainer extends RelativeLayout{
	private ArrayList<CatalogItem> mList_AllItem = new ArrayList<CatalogItem>();
	private BitmapCacheImage cacheImage = new BitmapCacheImage();
	protected MyAdapter myAdapter = new MyAdapter();
	protected GridView gridView;
	private boolean beAbleToLoadPic = true;
	private Thread loadThread;
	private CProgressDialog dialog;
	private boolean isFetching = false;
	private int firstVisibleItem;
	private int visibleItemCount;

	public PhotosGridContainer(Context context) {
		super(context);
		initlization();
	}
	public PhotosGridContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initlization();
	}
	private void initlization() {
		createWaitDialog();
		gridView = getGridView();
		addView(gridView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		loadThread = new Thread(){
			
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				fetchData();
			}
		};
		loadThread.start();
		cacheImage.setOnLoadingFinishiedListener(cacheImage.new callBack(){
			@Override
			public void onLoadingFinished() {
				refresh();
			}
			@Override
			public boolean isInRange(int position) {
				if(position < firstVisibleItem || position > firstVisibleItem + visibleItemCount){
					return false;
				}else{
					return true;
				}
			}
		});
	}
	//Use RoundedRect background
	private GridView getGridView() {
		GridView gridView = new GridView(getContext());
		gridView.setAdapter(myAdapter);
		gridView.setCacheColorHint(0);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				CatalogItem item = (CatalogItem) arg0.getAdapter().getItem(arg2);
				BundleNavi.getInstance().putString("returnImage",item.getImagepath());
				MenuControlIF.Instance().BackSearchWinChange(ADT_Photo_Edit.class);
			}
		});
		gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					beAbleToLoadPic = true;
					refresh();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					beAbleToLoadPic = true;
					refresh();
					break;
				default:
					beAbleToLoadPic = false;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				PhotosGridContainer.this.firstVisibleItem = firstVisibleItem;
				PhotosGridContainer.this.visibleItemCount = visibleItemCount;
			}
		});
		return gridView;
	}
	
	private Handler handler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0 || msg.what == 1){
				refresh();
				if(isFetching){
					handler.sendEmptyMessageDelayed(msg.what, 500);
				}
			}
			if(msg.what == 1){
				if(null != dialog && dialog.isShowing()){
					dialog.dismiss();
				}
			}
		};
	};
	
	//get selected data from SqlLite
	private void fetchData() {
		isFetching = true;
		try {
			//start load Image until winchange over
			Thread.sleep(600);
		} catch (InterruptedException e) {
		}
		String[] proj = { 
//				MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, 
				MediaStore.Images.Media.MINI_THUMB_MAGIC };
		String selection = ""+MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"=?";
		String fileName = BundleNavi.getInstance().getString("floderName")+"";
		Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				proj, selection, new String[]{fileName}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
		CatalogItem catalogItem = null;
		handler.sendEmptyMessageDelayed(1, 200);
		while (!cursor.isAfterLast() && isFetching) {
			if (cursor.isBeforeFirst()) {
				cursor.moveToNext();
			} else {
				catalogItem = new CatalogItem();
				int column_id = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
				int column_data = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				int column_magic = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.MINI_THUMB_MAGIC);
				String id = cursor.getString(column_id);
				String data = cursor.getString(column_data);
				long magic = cursor.getLong(column_magic);
				Uri mUri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
				long parseId = ContentUris.parseId(mUri);
				
				catalogItem.setParseId(parseId);
				catalogItem.setId(id);
				catalogItem.setImagepath(data);
				catalogItem.setMagic(magic);
				
				mList_AllItem.add(catalogItem);
				cursor.moveToNext();
			}
		}
		cursor.close();
		handler.removeMessages(0);
		handler.sendEmptyMessage(0);
		isFetching = false;
	}
	
	
	//Set columns of GridView
	public void setNumColumns(int i) {
		gridView.setNumColumns(i);
		
	}
	public void setColumnWidth(int columnWidth){
		gridView.setColumnWidth(columnWidth);
	}
	public int getCount(){
		return mList_AllItem.size();
	}
	public void addItem(CatalogItem item){
		mList_AllItem.add(item);
	}
	public void refresh(){
		post(new Runnable() {
			
			@Override
			public void run() {
				myAdapter.notifyDataSetChanged();
				
			}
		});
	}
	public void removeAllItem(){
		for(int i=0; i<mList_AllItem.size(); i++){
			mList_AllItem.remove(i);
		}
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList_AllItem.size();
		}

		@Override
		public Object getItem(int position) {
			return mList_AllItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(null == convertView){
				convertView = new gridViewItemAlbum(getContext());
			}else{
			}
			if(cacheImage.contains(position)){
				Bitmap bm = cacheImage.getBitmapByIndex(position);
				if(null != bm){
					((gridViewItemAlbum)convertView).setPic(bm);
				}else{
					((gridViewItemAlbum)convertView).setPic(R.drawable.navicloud_and_591a);
				}
			}else{
				if(beAbleToLoadPic){
//					Bitmap bm = BitmapTransportHelper.getInstance().drawBitMap(mList_AllItem.get(position).getMagic(), mList_AllItem.get(position).getParseId(), getContext());
//					cacheImage.push(bm, position);
//					((gridViewItemAlbum)convertView).setPic(bm);
					cacheImage.addLoadingTask(position, mList_AllItem.get(position).getMagic(), mList_AllItem.get(position).getParseId());
				}else{
				}
				((gridViewItemAlbum)convertView).setPic(R.drawable.navicloud_and_591a);
			}
			return convertView;
		}
		
	}
	
	public void destory(){
		if(isFetching){
			isFetching = false;
		}
		new Thread(){
			@Override
			public void run() {
				cacheImage.destroy();
			}
		}.start();
	}
	private void createWaitDialog() {
		dialog = new CProgressDialog(getContext());
		dialog.setCancelable(false);
		dialog.setText(R.string.STR_MM_02_02_01_01);
		dialog.show();
	}
}
