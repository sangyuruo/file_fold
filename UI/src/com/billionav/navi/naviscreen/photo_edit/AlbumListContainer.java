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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.listcomponent.ListItemAlbum;
import com.billionav.navi.component.listcomponent.ListItemInterface;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.uitools.BitmapCacheImage;
import com.billionav.ui.R;

public class AlbumListContainer extends RelativeLayout {
	//data of AlbumView
	protected ArrayList<CatalogThumbItem> layersItemList =  new ArrayList<CatalogThumbItem>();
	protected MyAdapter myAdapter = new MyAdapter();
	protected GridView gridView;
	private boolean isFetching = false;
	private boolean beAbleToLoadPic = true;
	private Thread loadThread;
	private CProgressDialog dialog;
	private BitmapCacheImage cacheImage = new BitmapCacheImage();

	public AlbumListContainer(Context context) {
		super(context);
		initialize(context);

	}
	public AlbumListContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	//query data from SqlLite
	private void fetchAllData(Context context) {
		isFetching = true;
		try {
			//start load Image until winchange over
			Thread.sleep(600);
		} catch (InterruptedException e) {
		}
		String[] proj = { MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, 
				MediaStore.Images.Media.MINI_THUMB_MAGIC };
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
		String temp = "";
		CatalogThumbItem catalogItem = null;
		handler.sendEmptyMessageDelayed(1, 200);
		while (isFetching &&!cursor.isAfterLast()) {
			if (cursor.isBeforeFirst()) {
				cursor.moveToNext();
			} else {
				int column_bucket = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
				String bucketName = cursor.getString(column_bucket);
				if(temp.equals(bucketName)){
					if(null != catalogItem){
						catalogItem.addCount();
					}
				}else{
					temp = bucketName;
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
					
					catalogItem = new CatalogThumbItem();
					catalogItem.setParseId(parseId);
					catalogItem.setTextview(bucketName);
					catalogItem.setId(id);
					catalogItem.setImagepath(data);
					catalogItem.setMagic(magic);
					layersItemList.add(catalogItem);
					catalogItem.addCount();
					
				}
				cursor.moveToNext();
			}

		}
		
		cursor.close();
		handler.removeMessages(0);
		handler.sendEmptyMessage(0);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		for(int i=0; i<layersItemList.size() && i < BitmapCacheImage.CACHE_SIZE && isFetching;i++){
			CatalogThumbItem item = layersItemList.get(i);
			if(!cacheImage.contains(i)){
				cacheImage.push(BitmapTransportHelper.getInstance().drawBitMap(item.getMagic(), item.getParseId(), getContext()), i);
			}
			if(i%10 ==9){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		}
		isFetching = false;
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
	public void setSize(int width, int height) {
		gridView.getLayoutParams().width = width;
		gridView.getLayoutParams().height = height;
		gridView.requestLayout();
	}

	public int getCount() {
		return layersItemList.size();
	}
	
	//init listView, call query function
	private void initialize(final Context context) {
		createWaitDialog();
		loadThread = new Thread(){
			
			@Override
			public void run() {
				fetchAllData(context);
				
			}
		};
		loadThread.start();
				
		gridView = getGridView();
		gridView.setNumColumns(GridView.AUTO_FIT);
		gridView.setColumnWidth((int)(100*getResources().getDisplayMetrics().density+0.5f));
		addView(gridView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		
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

			}
		});
	}
	
	
	private GridView getGridView() {
		GridView GridView = new GridView(getContext());
		GridView.setAdapter(myAdapter);
		GridView.setCacheColorHint(0);
		GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CatalogThumbItem listItem = (CatalogThumbItem) arg0.getAdapter().getItem(arg2);
				
				String floderName = listItem.getTextView();
				BundleNavi.getInstance().putString("floderName", floderName);
				MenuControlIF.Instance().ForwardWinChange(ADT_Photo_Browser.class);
			}
		});
		return GridView;
	}

	
	public void addItem(CatalogThumbItem item) {
		layersItemList.add(item);
	}
	
	public void refresh(){
		myAdapter.notifyDataSetChanged();
	}
	

	public void removeItem(CatalogThumbItem item) {
		layersItemList.remove(item);
	}
	
	public void removeAllItem() {
		layersItemList.clear();
		myAdapter.notifyDataSetChanged();
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		gridView.setOnScrollListener(l);

	}
	
	public int getIndexOfItem(CatalogThumbItem item){
		return layersItemList.indexOf(item);
	}

	
	//linked Adapter to data list
	protected class MyAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {
			return true;
		}

		@Override
		public int getCount() {
			return layersItemList.size();
		}
		
		@Override
		public Object getItem(int position) {
			return layersItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CatalogThumbItem ci = layersItemList.get(position);
			if(null == convertView){
				convertView = new ListItemAlbum(getContext(), ci);
			}
			else{
				
				((ListItemAlbum)convertView).setFileName(ci.getTextView());
				((ListItemAlbum)convertView).setPicCount(ci.getPicCount());
			}
			if(cacheImage.contains(position)){
				Bitmap bm = cacheImage.getBitmapByIndex(position);
				if(null != bm){
					((ListItemAlbum)convertView).setpic(bm);
				}else{
					((ListItemAlbum)convertView).setImageResource(R.drawable.navicloud_and_591a);
				}
			}else{
				if(beAbleToLoadPic){
					Bitmap bm = BitmapTransportHelper.getInstance().drawBitMap(ci.getMagic(), ci.getParseId(), getContext());
					((ListItemAlbum)convertView).setpic(bm);
					cacheImage.push(bm, position);
				}else{
					((ListItemAlbum)convertView).setImageResource(R.drawable.navicloud_and_591a);
				}
			}
			return convertView;

		}
		
		
	}
	public GridView getlistView() {
		return gridView;
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

