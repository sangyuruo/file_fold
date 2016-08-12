package com.billionav.navi.component.basiccomponent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.component.listcomponent.RoundedRectListView;

public class DeleteableListView extends RoundedRectListView {
	
	private WindowManager.LayoutParams mWindowParams;
	private boolean isLongClicked;

	private WindowManager mWindowManager;
	private Bitmap mDragBitmap;
	private ImageView mDragView;
	private int lastX;
	private int lastY;
	private int firstViewX;
	private int position;
	private OnDeleteListener deleteListener;
	private Handler handler;
	
	private float animX;
	private float animAlpha;
	
	private int targetX;
	private float targerAlpha;
	private int timerCount;
	
	private boolean needDelete = false;
	private final static int Msg_Wm_Tran = 1;
	private final static int TimerDelay = 30;
	private View itemView;
	private int statusBarHeight =-1;
	
	private VelocityTracker velocityTracker;
	public DeleteableListView(Context context)
	{
		super(context);
		initDeleteEvent();
	}
	public DeleteableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDeleteEvent();
	}

	public DeleteableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDeleteEvent();
	}
	
	private boolean isCanDeleteAble = true;

	public boolean isCanDeleteAble() {
		return isCanDeleteAble;
	}

	public void setDeleteListener(OnDeleteListener deleteListener) {
		this.deleteListener = deleteListener;
	}
	public void setCanDeleteAble(boolean isCanDeleteAble) {
		this.isCanDeleteAble = isCanDeleteAble;
		if(isCanDeleteAble){
			initDeleteEvent();
		}else{
			removeDeleteEvent();
		}
	}
	
	private void removeDeleteEvent(){
		isCanDeleteAble = false;
		deleteListener =null;
	}
	
	private int getStatusBarHeight(){
		Activity act =(Activity) getContext();
		Rect rect =new Rect();
		act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}

	private void initDeleteEvent(){
		this.setLongClickable(true);
		this.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(deleteListener == null) { 
					return false;
                }
				isLongClicked = true;
				view.setDrawingCacheEnabled(true);
				Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
				int[] location = new int[2];
				view.getLocationInWindow(location);

				firstViewX = location[0]-getPaddingLeft();
				startDragging(bitmap, location[0],location[1]);
				
				itemView = view;                         
				view.setVisibility(INVISIBLE);
				DeleteableListView.this.position = position;
				return true;
			}
			
		});
	}
	

	
	private void startDragging(Bitmap bm, int x, int y) {
		
		if(statusBarHeight ==-1){
			statusBarHeight = getStatusBarHeight();
		}
		
		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP;
		mWindowParams.x = 0;
		mWindowParams.y =  y -statusBarHeight;//dy-mDragPoint+mCoordOffset;
		
		Log.d("xx","start windowParams:x,y{"+mWindowParams.x+","+mWindowParams.y+"}");

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		ImageView v = new ImageView(getContext());
//		v.setBackgroundColor(backColor);
		v.setImageBitmap(bm);
		mDragBitmap = bm;

		mWindowManager = (WindowManager)getContext().getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
}

	private void dragView(int x, int y) {
		int width = mDragView.getWidth();
		Log.d("xx", "dragView:x,y:{"+x+","+y+"}");
		mWindowParams.x +=x;
		mWindowParams.alpha = 1 - Math.abs(mWindowParams.x-firstViewX)*2.0f/width;
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}
	
	private void playAnimation(int targerX,float targetAlpha,int duration){
		if(handler == null){
			handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case Msg_Wm_Tran:
						applyAnimotion();
						break;

					default:
						break;
					}
				}	
			};
		}
		
		this.targetX = targerX;
		this.targerAlpha = targetAlpha;
		timerCount = duration/TimerDelay;
		
		animX = (targerX-  mWindowParams.x)/timerCount;
		if( animX > 0 && animX<5) {
			animX =5;
		} else if(animX <0 && animX >-5) { 
			animX = -5;
        }
		animAlpha = (targetAlpha -mWindowParams.alpha)/timerCount;
		applyAnimotion();
	}
	
	private void onEndAnimation(){
		lastX = 0;
		lastY = 0;
		if (mDragView != null) {
			mDragView.setVisibility(INVISIBLE);
			mWindowManager.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
		isLongClicked = false;
		if(needDelete){
			deleteListener.onDeleteItem(position, this);
			needDelete = false;
		}
		itemView.setVisibility(VISIBLE);
	}
	
	private void applyAnimotion(){
		if(animX == 0 ){
			onEndAnimation();
			return;
		}
		mWindowParams.alpha += animAlpha;
		mWindowParams.x += animX;
		
		if((animX>0 && mWindowParams.x >= targetX )||  (animX <0 && mWindowParams.x <= targetX )){
			mWindowParams.alpha = targerAlpha;
			mWindowParams.x = targetX;
			mWindowManager.updateViewLayout(mDragView, mWindowParams);
			handler.removeMessages(Msg_Wm_Tran);
			onEndAnimation();
		}
		else{
			mWindowManager.updateViewLayout(mDragView, mWindowParams);
			handler.sendEmptyMessageDelayed(Msg_Wm_Tran,TimerDelay );
		}
		
		Log.d("xx", "applyAnimotion x,y:{"+mWindowParams.x+","+mWindowParams.y+"}");

	}
	
	private void cancelView(float velocity){
		Log.d("xx", "cancel valx:"+velocity);
		int distance =0;
		float alpha =1;
		if(Math.abs(mWindowParams.x -firstViewX) >= mDragView. getWidth()/2  || Math.abs(velocity) > 1000){
			if(velocity >1000){
				distance = mDragView.getWidth();
			}else if(velocity <-1000){
				distance = -mDragView.getWidth();
			}else{
			 distance = (mWindowParams.x-firstViewX >0)?mDragView.getWidth():-mDragView.getWidth();
			}
			needDelete = true;
			alpha = 0.0f;
		}else{
			 distance = firstViewX;
		}
		
		playAnimation(distance, alpha, 300);
		
	}
	
	private void trackTouchEvent(MotionEvent ev){
		if(velocityTracker == null){
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(ev);
		
		//TODO
        NaviLogUtil.debugTouch("DeleteableListView onTouchEvent: " + ev.getAction() );
		NaviLogUtil.debugTouch( String.format( "Event x=%s , y=%s" , ev.getX() , ev.getY() ));
        
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			Log.d("xx", "trackTouchEvent:x,y:{"+lastX+","+lastY+"}");

			if(lastX ==0 && lastY == 0){
				lastX =(int)ev.getRawX();
				lastY = (int)ev.getRawY();
				return;
			}
			int x = (int)ev.getRawX();
			int y = (int)ev.getRawY();
			dragView(x-lastX,y-lastY);
			lastX =x;
			lastY =y;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			velocityTracker.computeCurrentVelocity(1000);
			cancelView(velocityTracker.getXVelocity());
			velocityTracker.recycle();
			velocityTracker = null;
			break;
		default:
			break;
		}
	}
	
	public interface OnDeleteListener{
		void onDeleteItem(int position,ListView listView);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isLongClicked){
			trackTouchEvent(ev);
			return true;
		}
		return super.onTouchEvent(ev);
	}
}
