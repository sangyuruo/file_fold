/*
 * Copyright (C) 2011 by suntec
 * 
 * class MapGestureDetector create on 2011-2-1
 * @author liuzhaofeng
 * @version 19
 * 
 * */
package com.billionav.navi.uitools;

import java.util.ArrayList;

import android.content.Context;
import android.view.MotionEvent;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.uitools.GestureDetector.OnGestureListener;

/**
 * A class inherit MapScaler, and that class can analyse gesture and
 * operate map.
 * 
 * */
public class MapMotion implements OnGestureListener {
	
	private GestureDetector mMapGestureDetector;
	private GestureListener mOnGestureListener;
	
	
	private boolean is2FingerScrollEnable;
	private boolean is2FingerSingleTapEnable;
	private boolean isDoubleTapEnable;
	private boolean isScrollEnable;
	private boolean isSingleTapEnable;
	private boolean isLongPressEnable;
	private boolean is2FingerScaleEnble;
	private boolean is2FingerParallelVerticalScrollEnable;
	private boolean is2FingerParallelHorizontalScrollEnable;
	
	private MotionEvent perMotionEventForScroll;
	private MotionEvent perMotionEvent;
//    private static final double MAP_MAX_ANGLE = UIMapControlJNI.GetMaxMapAngle();
//    private static final double MAP_MIN_ANGLE = UIMapControlJNI.GetMinMapAngle();
    private static final double MAP_ANGLE_PER_STEP = (1.5/180.0) * Math.PI; //1.5 degree
	private double mapAngle;
	private double perVerticalDistance;
	private static final double coefficient = MAP_ANGLE_PER_STEP * 0.2;
	
    
	/**
	 * create a MapMotion with activity, mapview, zoom in button 
	 * and zoom out buttom, all the arguments must not be null. the default
	 * setting will enable all the gestures. if you don't want to use some
	 * gesture, you should call some method like <b>setXXXEnable(false)</b>
	 * to disable this gesture.
	 * */
	public MapMotion(Context context){
		
		mMapGestureDetector = new GestureDetector(context, this);
		is2FingerScrollEnable = true;
		is2FingerSingleTapEnable = true;
		isDoubleTapEnable = true;
		isScrollEnable = true;
		isSingleTapEnable = true;
		isLongPressEnable = true;
		is2FingerScaleEnble = true;
		is2FingerParallelVerticalScrollEnable = true;
		is2FingerParallelHorizontalScrollEnable = true;
		
		mOnGestureListener = new GestureListener();
		mOnGestureListener.addGestureListener(emptyListener);
	}
	
	public MapMotion(){
		
		mMapGestureDetector = new GestureDetector(this);
		is2FingerScrollEnable = true;
		is2FingerSingleTapEnable = true;
		isDoubleTapEnable = true;
		isScrollEnable = true;
		isSingleTapEnable = true;
		isLongPressEnable = true;
		is2FingerScaleEnble = true;
		is2FingerParallelVerticalScrollEnable = true;
		is2FingerParallelHorizontalScrollEnable = true;
		
		mOnGestureListener = new GestureListener();
		mOnGestureListener.addGestureListener(emptyListener);
	}
	
	/**
	 * 
	 * map will do something when gesture occur, if you want 
	 * do extra something when gesture occur, you can call 
	 * this method set listener.
	 * 
	 * @see OnGestureListener
	 * 
	 * */
//	public void setGestureListener(OnGestureListener onGestureListener){
//		mOnGestureListener = onGestureListener;
//	}
	
	
	/**
	 * 
	 * if you want use this class to analyse map gesture occur,
	 * you must call this method in method onTouchEvent() in your Activity
	 * 
	 * */
	public boolean onTouchEvent(MotionEvent ev){
		return mMapGestureDetector.onTouchEvent(ev);
	}
	
	
	/**
	 * allow or prohibit this gesture
	 * */
	public void set2FingerScrollEnable(boolean is2FingerScrollEnable) {
		this.is2FingerScrollEnable = is2FingerScrollEnable;
	}


	public void set2FingerSingleTapEnable(boolean is2FingerSingleTapEnable) {
		this.is2FingerSingleTapEnable = is2FingerSingleTapEnable;
	}


	public void setDoubleTapEnable(boolean isDoubleTapEnable) {
		this.isDoubleTapEnable = isDoubleTapEnable;
	}


	public void setScrollEnable(boolean isScrollEnable) {
		this.isScrollEnable = isScrollEnable;
	}


	public void setSingleTapEnable(boolean isSingleTapEnable) {
		this.isSingleTapEnable = isSingleTapEnable;
	}


	public void setLongPressEnable(boolean isLongPressEnable) {
		this.isLongPressEnable = isLongPressEnable;
	}


	public void set2FingerScaleEnble(boolean is2FingerScaleEnble) {
		this.is2FingerScaleEnble = is2FingerScaleEnble;
	}


	public void set2FingerParallelVerticalScrollEnable(
			boolean is2FingerParallelVerticalScrollEnable) {
		this.is2FingerParallelVerticalScrollEnable = is2FingerParallelVerticalScrollEnable;
	}

	public void set2FingerParallelHorizontalScrollEnable(
			boolean is2FingerParallelHorizontalScrollEnable) {
		this.is2FingerParallelHorizontalScrollEnable = is2FingerParallelHorizontalScrollEnable;
	}

	
	
	/**set all gestures enblable*/
	public void setGestureEnble(boolean is2FingerScrollEnable, 
			boolean is2FingerSingleTapEnable, 
			boolean isDoubleTapEnable, 
			boolean isScrollEnable, 
			boolean isSingleTapEnable, 
			boolean isLongPressEnable,
			boolean is2FingerScaleEnble,
			boolean is2FingerParallelScroll,
			boolean is2FingerParallelVerticalScrollEnable,
			boolean is2FingerParallelHorizontalScrollEnable)
	{
		this.is2FingerScrollEnable = is2FingerScrollEnable;
		this.is2FingerSingleTapEnable = is2FingerSingleTapEnable;
		this.isDoubleTapEnable = isDoubleTapEnable;
		this.isScrollEnable = isScrollEnable;
		this.isSingleTapEnable = isSingleTapEnable;
		this.isLongPressEnable = isLongPressEnable;
		this.is2FingerScaleEnble = is2FingerScaleEnble;
		this.is2FingerParallelVerticalScrollEnable = is2FingerParallelVerticalScrollEnable;
		this.is2FingerParallelVerticalScrollEnable = is2FingerParallelVerticalScrollEnable;
	}
	
	
	/**
	 * this method tell you that map just doing somethig.
	 * if you get true from this method, you should not fresh
	 * the map.
	 * 
	 * */
	public boolean isMotionning(){
		return true;
	}
	
	@Override
	public boolean on2FingerScroll(boolean isFrist,MotionEvent down1, MotionEvent down2,
			MotionEvent move, float distanceMove, float distanceDown,
			float distanceDiff) {
		if (!is2FingerScrollEnable) {
			return false;
		}
		
		
		// add your code
		if(isFrist){
		}
		
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerScroll(isFrist, down1, down2, move, distanceMove, distanceDown, distanceDiff);
		}
		return is2FingerScrollEnable;
	}
	
	@Override
	public boolean on2FingerScrollEnd(MotionEvent down1, MotionEvent down2,
			MotionEvent up2) {
		if(!is2FingerScrollEnable){
			return false;
		}
		
		//add your code
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerScrollEnd(down1, down2, up2);
		}
		return is2FingerScrollEnable;
	}

	@Override
	public boolean on2FingerSingleTapConfirmed(MotionEvent e1, MotionEvent e2) {

		if(!is2FingerSingleTapEnable){
			return false;
		}
		
		//add your code
		UIMapControlJNI.ScaleUpDown(false);
		
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerSingleTapConfirmed(e1, e2);
		}
		return is2FingerSingleTapEnable;
	}

	@Override
	public boolean onDoubleTapConfirmed(MotionEvent e) {

		if(!isDoubleTapEnable){
			return false;
		}
		
		//add your code
		UIMapControlJNI.ScaleUpDown(true);
		if(mOnGestureListener!=null){
			mOnGestureListener.onDoubleTapConfirmed(e);
		}
		return isDoubleTapEnable;
	}

	@Override
	public boolean onScroll(boolean isFrist, MotionEvent down, MotionEvent move,
			float distanceX, float distanceY) {

		if(!isScrollEnable){
			return false;
		}
		
		//add your code
		if(isFrist){
			perMotionEventForScroll = MotionEvent.obtain(down);
			
			UIMapControlJNI.SetCarPositonMode(false);
		}
		UIMapControlJNI.MapMove(perMotionEventForScroll.getX(), perMotionEventForScroll.getY(), perMotionEventForScroll.getEventTime(), move.getX(), move.getY(), move.getEventTime());
		
		perMotionEventForScroll = MotionEvent.obtain(move);
		if(mOnGestureListener!=null){
			mOnGestureListener.onScroll(isFrist, down, move, distanceX, distanceY);
		}
		return isScrollEnable;
	}

	@Override
	public boolean onScrollEnd(MotionEvent down, MotionEvent up) {
		
		if(!isScrollEnable){
			return false;
		}
		
		//add your code
		UIMapControlJNI.StartInertiaScroll();
		if(mOnGestureListener!=null){
			mOnGestureListener.onScrollEnd(down, up);
		}
		return isScrollEnable;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {

		if(!isSingleTapEnable){
			return false;
		}
		
		//add your code
		if(mOnGestureListener!=null){
			mOnGestureListener.onSingleTapConfirmed(e);
		}
		return isSingleTapEnable;
	}

	@Override
	public boolean onLongPress(MotionEvent e) {
		if(!isLongPressEnable){
			return false;
		}
		
		//add your code
		if(mOnGestureListener!=null){
			mOnGestureListener.onLongPress(e);
		}
		return isLongPressEnable;
	}

	@Override
	public boolean on2FingerScale(boolean isFrist, MotionEvent down1, MotionEvent down2,
			MotionEvent move, float distanceMove, float distanceDown,
			float distanceDiff) {
		if(!is2FingerScaleEnble) {
			return false;
		}
		if(isFrist) {
			perMotionEvent = MotionEvent.obtain(down2);
		}
		
		
		UIMapControlJNI.MapRotateScale(perMotionEvent.getX(0), perMotionEvent.getY(0), perMotionEvent.getX(1), perMotionEvent.getY(1), move.getX(0), move.getY(0), move.getX(1), move.getY(1));
		
		perMotionEvent = MotionEvent.obtain(move);
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerScale(isFrist, down1, down2, move, distanceMove, distanceDown, distanceDiff);
		}

		return is2FingerScaleEnble;
	}

	@Override
	public boolean on2FingerScaleEnd(MotionEvent down1, MotionEvent down2,
			MotionEvent up2) {
		if(!is2FingerScaleEnble) {
			return false;
		}
		
		UIMapControlJNI.MapRotateScaleEnd();
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerScaleEnd(down1, down2, up2);
		}

		return is2FingerScaleEnble;
	}
	
	@Override
	public boolean on2FingerParallelVerticalScroll(boolean isFrist,
			MotionEvent down1, MotionEvent down2, MotionEvent move,
			float verticalDistance) {
		
		if(!is2FingerParallelVerticalScrollEnable) {
			return false;
		}
		
		if(isFrist) {
			mapAngle = UIMapControlJNI.GetMapAngle();
			perVerticalDistance = 0;
		}
		
		mapAngle = mapAngle - (verticalDistance-perVerticalDistance) * coefficient;
		
		mapAngle = Math.max(UIMapControlJNI.GetMinAngle(), Math.min(mapAngle, UIMapControlJNI.GetMaxAngle()));
		
		perVerticalDistance = verticalDistance;
		
		UIMapControlJNI.SetMapAngle((float)mapAngle);
		
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerParallelVerticalScroll(isFrist, down1, down2, move, verticalDistance);
		}
		
		return is2FingerParallelVerticalScrollEnable;
	}

	@Override
	public boolean on2FingerParallelVerticalScrollEnd(MotionEvent down1,
			MotionEvent down2, MotionEvent up2) {
		if(!is2FingerParallelVerticalScrollEnable) {
			return false;
		}
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerParallelVerticalScrollEnd(down1, down2, up2);
		}
		return is2FingerParallelVerticalScrollEnable;
	}

	@Override
	public boolean on2FingerParallelHorizontalScroll(boolean isFrist,
			MotionEvent down1, MotionEvent down2, MotionEvent move,
			float horizontalDistance) {
		if(!is2FingerParallelHorizontalScrollEnable) {
			return false;
		}
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerParallelHorizontalScroll(isFrist, down1, down2, move, horizontalDistance);
		}
		return is2FingerParallelHorizontalScrollEnable;
	}

	@Override
	public boolean on2FingerParallelHorizontalScrollEnd(MotionEvent down1,
			MotionEvent down2, MotionEvent up2) {
		if(!is2FingerParallelHorizontalScrollEnable) {
			return false;
		}
		if(mOnGestureListener!=null){
			mOnGestureListener.on2FingerParallelHorizontalScrollEnd(down1, down2, up2);
		}
		return is2FingerParallelHorizontalScrollEnable;
	}

	public void addGestureListener(OnGestureListener onGestureListener) {
		mOnGestureListener.addGestureListener(onGestureListener);
	}

	public void removeGestureListener(OnGestureListener onGestureListener) {
		mOnGestureListener.removeGestureListener(onGestureListener);
	}
	
	private OnGestureListener emptyListener = new GestureDetector.SimpleGestureListener();
	
	public void setGestureListener(OnGestureListener onGestureListener) {
		if(onGestureListener != null) {
			mOnGestureListener.setGestureListener(0, onGestureListener);
		} else {
			mOnGestureListener.setGestureListener(0, emptyListener);
		}
	}

}

class GestureListener implements OnGestureListener{
	
	private ArrayList<OnGestureListener> gestureList = new ArrayList<OnGestureListener>();

	public void addGestureListener(OnGestureListener onGestureListener) {
		gestureList.add(onGestureListener);
	}

	public void removeGestureListener(OnGestureListener onGestureListener) {
		gestureList.remove(onGestureListener);
	}
	
	public void setGestureListener(int index, OnGestureListener onGestureListener) {
		gestureList.set(index, onGestureListener);
	}
	
	@Override
	public boolean onLongPress(MotionEvent e) {
		for(OnGestureListener l: gestureList) {
			l.onLongPress(e);
		}
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		for(OnGestureListener l: gestureList) {
			l.onSingleTapConfirmed(e);
		}
		return true;
	}

	@Override
	public boolean on2FingerScroll(boolean isFrist, MotionEvent down1,
			MotionEvent down2, MotionEvent move, float distanceMove,
			float distanceDown, float distanceDiff) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerScroll(isFrist, down1, down2, move, distanceMove, distanceDown, distanceDiff);
		}
		return true;
	}

	@Override
	public boolean on2FingerScrollEnd(MotionEvent down1, MotionEvent down2,
			MotionEvent up2) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerScrollEnd(down1, down2, up2);
		}
		return true;
	}

	@Override
	public boolean on2FingerScale(boolean isFrist, MotionEvent down1,
			MotionEvent down2, MotionEvent move, float distanceMove,
			float distanceDown, float distanceDiff) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerScale(isFrist, down1, down2, move, distanceMove, distanceDown, distanceDiff);
		}
		return true;
	}

	@Override
	public boolean on2FingerScaleEnd(MotionEvent down1, MotionEvent down2,
			MotionEvent up2) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerScaleEnd(down1, down2, up2);
		}
		return true;
	}

	@Override
	public boolean on2FingerParallelVerticalScroll(boolean isFrist,
			MotionEvent down1, MotionEvent down2, MotionEvent move,
			float verticalDistance) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerParallelVerticalScroll(isFrist, down1, down2, move, verticalDistance);
		}
		return true;
	}

	@Override
	public boolean on2FingerParallelVerticalScrollEnd(MotionEvent down1,
			MotionEvent down2, MotionEvent up2) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerParallelVerticalScrollEnd(down1, down2, up2);
		}
		return true;
	}

	@Override
	public boolean on2FingerParallelHorizontalScroll(boolean isFrist,
			MotionEvent down1, MotionEvent down2, MotionEvent move,
			float horizontalDistance) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerParallelHorizontalScroll(isFrist, down1, down2, move, horizontalDistance);
		}
		return true;
	}

	@Override
	public boolean on2FingerParallelHorizontalScrollEnd(MotionEvent down1,
			MotionEvent down2, MotionEvent up2) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerParallelHorizontalScrollEnd(down1, down2, up2);
		}
		return true;
	}

	@Override
	public boolean onDoubleTapConfirmed(MotionEvent e) {
		for(OnGestureListener l: gestureList) {
			l.onDoubleTapConfirmed(e);
		}
		return true;
	}

	@Override
	public boolean on2FingerSingleTapConfirmed(MotionEvent e1, MotionEvent e2) {
		for(OnGestureListener l: gestureList) {
			l.on2FingerSingleTapConfirmed(e1, e2);
		}
		return true;
	}

	@Override
	public boolean onScroll(boolean isFrist, MotionEvent down,
			MotionEvent move, float distanceX, float distanceY) {
		for(OnGestureListener l: gestureList) {
			l.onScroll(isFrist, down, move, distanceX, distanceY);
		}
		return true;
	}

	@Override
	public boolean onScrollEnd(MotionEvent down, MotionEvent up) {
		for(OnGestureListener l: gestureList) {
			l.onScrollEnd(down, up);
		}
		return true;
	}
	
}
