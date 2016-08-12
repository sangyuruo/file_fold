package com.billionav.navi.uitools;

import java.util.LinkedList;

import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.photo_edit.BitmapTransportHelper;

import android.graphics.Bitmap;
import android.view.View;

public class BitmapCacheImage {
	//bitmap size shoulde be less than 70*70
	public static final int CACHE_SIZE = 300;
	private LinkedList<Bitmap>	bitmapCache = new LinkedList<Bitmap>();
	private LinkedList<Integer>	indexList = new LinkedList<Integer>();
	private LinkedList<LoadingTask> loadingQueue = new LinkedList<LoadingTask>();
	private boolean ThreadStarted = false;
	private callBack listener;
	private boolean quitThread = false;
	
	public void setOnLoadingFinishiedListener(callBack l){
		this.listener = l;
	}
	
	public void addLoadingTask(int position, long l, long m){
		
		loadingQueue.add(new LoadingTask(position, l, m));
		if(null  == loadQueueThread && !ThreadStarted){
			loadQueueThread = new LoadThread();
			loadQueueThread.start();
		}
	}
	
	public void push(Bitmap bm, int index){
		if(contains(index)){
			return;
		}
		if(getCacheSize() >= CACHE_SIZE){
			pop();
		}
		bitmapCache.add(bm);
		indexList.add(index);
	}
	
	public void pop(){
		Bitmap bm = bitmapCache.getFirst();
		if(null != bm && !bm.isRecycled()){
			bm.recycle();
		}
		bitmapCache.removeFirst();
		indexList.removeFirst();
		bm = null;
	}
	
	public int getCacheSize(){
		return bitmapCache.size();
	}
	
	public Bitmap getBitmapByIndex(int index){
		if(contains(index)){
			return bitmapCache.get(indexList.indexOf(index));
		}else {
			return null;
		}
	}
	
	public void destroy(){
		int gcCount = 0;
		while(!bitmapCache.isEmpty()){
			gcCount++;
			pop();
			if(gcCount % 15 == 0){
//				System.gc();
				gcCount = 0;
			}
		}
		quitThread = true;
		listener = null;
	}

	public boolean contains(int index) {
		return indexList.contains(index);
	}
	
	private Thread loadQueueThread;
	private class LoadThread extends Thread{
		public void run() {
			ThreadStarted = true;
			while(loadingQueue.size() > 0 && !quitThread){
				LoadingTask task = loadingQueue.getFirst();
				task.loadImageAtBackground();
				if(null != listener){
					try{
						listener.onLoadingFinished();
					}catch(Exception e){
					}
				}
				loadingQueue.removeFirst();
			}
			ThreadStarted = false;
			loadQueueThread = null;
		};
	};
	
	private class LoadingTask{
		private int position = 0;
		private long magic;
		private long parseId;
		public LoadingTask(int position, long magic, long parseId){
			this.position = position;
			this.magic = magic;
			this.parseId = parseId;
		}

		public void loadImageAtBackground(){
			if(BitmapCacheImage.this.contains(position)){
				return;
			}
			if(null != listener && !listener.isInRange(position)){
				return;
			}
			Bitmap bm = BitmapTransportHelper.getInstance().drawBitMap(magic, parseId, NSViewManager.GetViewManager());
			push(bm, position);
		}
	}
	
	public abstract class callBack{
		public abstract void onLoadingFinished();
		public abstract boolean isInRange(int position);
	}
}