package com.billionav.navi.app.ext.sdl;

import java.util.List;

import android.view.MotionEvent;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.uitools.GestureDetector.OnGestureListener;
import com.billionav.navi.uitools.MapMotion;
import com.billionav.navi.uitools.RouteCalcController;
import com.smartdevicelink.proxy.rpc.OnTouchEvent;
import com.smartdevicelink.proxy.rpc.TouchEvent;
import com.smartdevicelink.proxy.rpc.enums.TouchType;

/**
 * 
 * @author sangjun
 *
 */
public class TouchEventHandler {
	static TouchEventHandler instance;
	MapMotion m_motionDetector = null;

	public void execute(OnTouchEvent notification) {
		List<TouchEvent> evs = notification.getEvent();
		TouchType type = notification.getType();
		if (type.equals(TouchType.BEGIN)) {
			this.begin(evs);
		} else if (type.equals(TouchType.MOVE)) {
			this.move(evs);
		} else if (type.equals(TouchType.END)) {
			this.end(evs);
		}
	}

	private void begin(List<TouchEvent> evs) {
		for (TouchEvent touchEvent : evs) {
			MotionEvent ev = transToMotionEvent(touchEvent, MotionEvent.ACTION_DOWN);
//			MotionEvent ev = MotionEvent.obtain(
//					System.currentTimeMillis(),
//					System.currentTimeMillis(), 
//					MotionEvent.ACTION_DOWN,
//					0f+getScreenX(touchEvent), 
//					0f+getScreenY(touchEvent),  touchEvent
//							.getTs().get(0).intValue() );
			ev.setAction(MotionEvent.ACTION_DOWN);
			m_motionDetector.onTouchEvent(ev);
		}
	}

	private int getScreenX(TouchEvent touchEvent) {
		return touchEvent.getC().get(0).getX();
		// return ScreenUtil.getInstance().getRealMoveWidth(
		// touchEvent.getC().get(0).getX());
	}

	private int getScreenY(TouchEvent touchEvent) {
		return touchEvent.getC().get(0).getY();
		// return ScreenUtil.getInstance().getRealMoveHeight(
		// touchEvent.getC().get(0).getY());
	}

	private void end(List<TouchEvent> evs) {
		for (TouchEvent touchEvent : evs) {
			MotionEvent ev = transToMotionEvent(touchEvent, MotionEvent.ACTION_UP);
					
//					MotionEvent.obtain(
//					System.currentTimeMillis(),
//					System.currentTimeMillis(), MotionEvent.ACTION_UP,
//					getScreenX(touchEvent), getScreenY(touchEvent), touchEvent
//							.getTs().get(0));
			ev.setAction(MotionEvent.ACTION_UP);
			m_motionDetector.onTouchEvent(ev);
		}
	}
	
	private MotionEvent transToMotionEvent(TouchEvent touchEvent, int action){
		return MotionEvent.obtain(System.currentTimeMillis(),
				System.currentTimeMillis(), 
				action,
				0f+ getScreenX(touchEvent), 
				0f+ getScreenY(touchEvent), 
				touchEvent.getTs().get(0).intValue());
	}

	private void move(List<TouchEvent> evs) {
		for (TouchEvent touchEvent : evs) {
			MotionEvent ev = transToMotionEvent(touchEvent, MotionEvent.ACTION_MOVE);
//			MotionEvent ev = MotionEvent.obtain(System.currentTimeMillis(),
//					System.currentTimeMillis(), MotionEvent.ACTION_MOVE,
//					getScreenX(touchEvent), getScreenY(touchEvent), touchEvent
//							.getTs().get(0));
			ev.setAction(MotionEvent.ACTION_MOVE);
			m_motionDetector.onTouchEvent(ev);
		}
	}

	static {
		instance = new TouchEventHandler();
	}

	public static TouchEventHandler getInstance() {
		return instance;
	}

	private TouchEventHandler() {
		m_motionDetector = new MapMotion();
		m_motionDetector.setGestureListener(new OnGestureListener() {
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onScrollEnd(MotionEvent down, MotionEvent up) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onScroll(boolean isFrist, MotionEvent down,
					MotionEvent move, float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
				float longpressX = e.getX();
				float longpressY = e.getY();
				
				int[] lonlat = UIMapControlJNI.ConvertDispPointToLonLat(longpressX, longpressY);
				
				RouteCalcController.instance().rapidRouteCalculateWithDataFromNavi("Point on Map", lonlat[0], lonlat[1],null,null);
				
				return false;
			}
			
			@Override
			public boolean onDoubleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerSingleTapConfirmed(MotionEvent e1, MotionEvent e2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerScrollEnd(MotionEvent down1, MotionEvent down2,
					MotionEvent up2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerScroll(boolean isFrist, MotionEvent down1,
					MotionEvent down2, MotionEvent move, float distanceMove,
					float distanceDown, float distanceDiff) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerScaleEnd(MotionEvent down1, MotionEvent down2,
					MotionEvent up2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerScale(boolean isFrist, MotionEvent down1,
					MotionEvent down2, MotionEvent move, float distanceMove,
					float distanceDown, float distanceDiff) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerParallelVerticalScrollEnd(MotionEvent down1,
					MotionEvent down2, MotionEvent up2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerParallelVerticalScroll(boolean isFrist,
					MotionEvent down1, MotionEvent down2, MotionEvent move,
					float verticalDistance) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerParallelHorizontalScrollEnd(MotionEvent down1,
					MotionEvent down2, MotionEvent up2) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean on2FingerParallelHorizontalScroll(boolean isFrist,
					MotionEvent down1, MotionEvent down2, MotionEvent move,
					float horizontalDistance) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
}
