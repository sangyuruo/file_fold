/*
 * Copyright (C) 2011 by suntec
 * 
 * class GestureDetector create on 2011-2-1
 * @author liuzhaofeng
 * @version 12
 * 
 * */
package com.billionav.navi.uitools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.billionav.navi.app.ext.ReadPixelControl;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.sync.AppLinkService;

/**
 * Detects gestures and events with {@link MotionEvent}s. The
 * {@link OnGestureListener} callback will notify users when a particular motion
 * event has occurred.
 * */
public class GestureDetector {

	/**
	 * the listener that is used to notify when gestures occur. you should
	 * implement this interface then you can listen all this gestures.
	 * */
	public static interface OnGestureListener {

		/**
		 * Notified when a long press occurs with the initial on down
		 * {@link MotionEvent} that trigged it.
		 *
		 * @param e
		 *            The initial on down motion event that started the
		 *            longpress.
		 * @return true if the event is consumed, else false
		 */
		public boolean onLongPress(MotionEvent e);

		/**
		 * Notified when a single-tap occurs.
		 *
		 * @param e
		 *            The down motion event of the single-tap.
		 * @return true if the event is consumed, else false
		 */
		public boolean onSingleTapConfirmed(MotionEvent e);

		/**
		 * Notified when a 2 fingers scroll occurs.
		 *
		 * @param down1
		 *            the first finger down
		 * @param down2
		 *            the second finger down
		 * @param move
		 *            the current motion event
		 * @param distanceMove
		 *            the distance between 2 finger when 2 the second finger
		 *            down
		 * @param distanceDown
		 *            the current distance between 2 finger
		 * @param distanceDiff
		 *            distanceDown - distanceMove
		 * 
		 * @return true if the event is consumed, else false
		 */
		public boolean on2FingerScroll(boolean isFrist, MotionEvent down1,
				MotionEvent down2, MotionEvent move, float distanceMove,
				float distanceDown, float distanceDiff);

		/**
		 * Notified when a 2 fingers scroll end occurs.
		 * 
		 * @param down1
		 *            the first finger down
		 * @param down2
		 *            the second finger down
		 * @param up2
		 *            1 finger up
		 *
		 * @return true if the event is consumed, else false
		 */
		public boolean on2FingerScrollEnd(MotionEvent down1, MotionEvent down2,
				MotionEvent up2);

		/**
		 * Notified when a 2 fingers scroll occurs.
		 *
		 * @param down1
		 *            the first finger down
		 * @param down2
		 *            the second finger down
		 * @param move
		 *            the current motion event
		 * @param distanceMove
		 *            the distance between 2 finger when 2 the second finger
		 *            down
		 * @param distanceDown
		 *            the current distance between 2 finger
		 * @param distanceDiff
		 *            distanceDown - distanceMove
		 * 
		 * @return true if the event is consumed, else false
		 */
		public boolean on2FingerScale(boolean isFrist, MotionEvent down1,
				MotionEvent down2, MotionEvent move, float distanceMove,
				float distanceDown, float distanceDiff);

		/**
		 * Notified when a 2 fingers scroll end occurs.
		 * 
		 * @param down1
		 *            the first finger down
		 * @param down2
		 *            the second finger down
		 * @param up2
		 *            1 finger up
		 *
		 * @return true if the event is consumed, else false
		 */
		public boolean on2FingerScaleEnd(MotionEvent down1, MotionEvent down2,
				MotionEvent up2);

		public boolean on2FingerParallelVerticalScroll(boolean isFrist,
				MotionEvent down1, MotionEvent down2, MotionEvent move,
				float verticalDistance);

		public boolean on2FingerParallelVerticalScrollEnd(MotionEvent down1,
				MotionEvent down2, MotionEvent up2);

		public boolean on2FingerParallelHorizontalScroll(boolean isFrist,
				MotionEvent down1, MotionEvent down2, MotionEvent move,
				float horizontalDistance);

		public boolean on2FingerParallelHorizontalScrollEnd(MotionEvent down1,
				MotionEvent down2, MotionEvent up2);

		/**
		 * Notified when double-tap occurs.
		 *
		 * @return true if the event is consumed, else false
		 */
		public boolean onDoubleTapConfirmed(MotionEvent e);

		/**
		 * Notified when 2 finger single-tap occurs.
		 * 
		 * @param down1
		 *            the first finger down
		 * @param down2
		 *            the second finger down
		 *
		 * @return true if the event is consumed, else false
		 */
		public boolean on2FingerSingleTapConfirmed(MotionEvent e1,
				MotionEvent e2);

		/**
		 * Notified when scroll occurs.
		 * 
		 * @param down
		 *            the finger down
		 * @param move
		 *            current motion event
		 * @param distanceX
		 *            distance between finger down and finger move in horizontal
		 *            direction
		 * @param distanceY
		 *            distance between finger down and finger move in vertical
		 *            direction
		 *
		 * @return true if the event is consumed, else false
		 */
		public boolean onScroll(boolean isFrist, MotionEvent down,
				MotionEvent move, float distanceX, float distanceY);

		/**
		 * Notified when scroll end occurs.
		 * 
		 * @param down
		 *            the finger down
		 * @param up
		 *            the finger down
		 *
		 * @return true if the event is consumed, else false
		 */
		public boolean onScrollEnd(MotionEvent down, MotionEvent up);
	}

	/**
	 * a simple gesture listener implements from {@link OnGestureListener}, you
	 * can easy to extends this class if you just need few gesture to listener.
	 * */
	public static class SimpleGestureListener implements OnGestureListener {
		@Override
		public boolean on2FingerScroll(boolean isFrist, MotionEvent down1,
				MotionEvent down2, MotionEvent move, float distanceMove,
				float distanceDown, float distanceDiff) {
			return false;
		}

		@Override
		public boolean on2FingerScrollEnd(MotionEvent down1, MotionEvent down2,
				MotionEvent up2) {
			return false;
		}

		@Override
		public boolean on2FingerSingleTapConfirmed(MotionEvent e1,
				MotionEvent e2) {
			return false;
		}

		@Override
		public boolean onDoubleTapConfirmed(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onLongPress(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(boolean isFrist, MotionEvent down,
				MotionEvent move, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public boolean onScrollEnd(MotionEvent down, MotionEvent up) {
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return false;
		}

		@Override
		public boolean on2FingerScale(boolean isFrist, MotionEvent down1,
				MotionEvent down2, MotionEvent move, float distanceMove,
				float distanceDown, float distanceDiff) {
			return false;
		}

		@Override
		public boolean on2FingerScaleEnd(MotionEvent down1, MotionEvent down2,
				MotionEvent up2) {
			return false;
		}

		@Override
		public boolean on2FingerParallelVerticalScroll(boolean isFrist,
				MotionEvent down1, MotionEvent down2, MotionEvent move,
				float verticalDistance) {
			return false;
		}

		@Override
		public boolean on2FingerParallelVerticalScrollEnd(MotionEvent down1,
				MotionEvent down2, MotionEvent up2) {
			return false;
		}

		@Override
		public boolean on2FingerParallelHorizontalScroll(boolean isFrist,
				MotionEvent down1, MotionEvent down2, MotionEvent move,
				float horizontalDistance) {
			return false;
		}

		@Override
		public boolean on2FingerParallelHorizontalScrollEnd(MotionEvent down1,
				MotionEvent down2, MotionEvent up2) {
			return false;
		}
	}

	private static final int SCROLL_TYPE_SCALE = 1;
	private static final int SCROLL_TYPE_VERTICAL_SCROLL = 2;
	private static final int SCROLL_TYPE_HORIZONTAL_SCROLL = 3;

	private final int mTouchSlopSquare;
	private final int mDoubleTapSlopSquare;

	private static final int LONGPRESS_TIMEOUT = ViewConfiguration
			.getLongPressTimeout();
	private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration
			.getDoubleTapTimeout();
	private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();

	private static final int LONG_PRESS = 2;
	private static final int TAP = 3;
	private boolean isStillDown;
	private boolean is2FingerDown;
	private boolean isScroll;
	private boolean is2FingerScroll;
	private boolean isScrollFrist = true;
	private boolean is2FingerScrollFirst = true;
	private int typeOf2FingerScroll;

	private final Handler mHandler;

	private MotionEvent mCurrentDownEvent;
	private MotionEvent mPreviousUpEvent;

	private MotionEvent mCurrentDownEvent2;
	private MotionEvent mPreviousUpEvent2;

	private final OnGestureListener mOnGestureListener;

	private class GestureHandler extends Handler {
		public GestureHandler() {
			super();
		}

		public GestureHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LONG_PRESS:
				dispatchLongPress();
				break;

			case TAP:
				// If the user's finger is still down, do not count it as a tap
				if (!isStillDown) {
					mOnGestureListener.onSingleTapConfirmed(mCurrentDownEvent);
				}
				break;

			default:
				throw new RuntimeException("Unknown message " + msg); // never
			}
		}
	}

	/**
	 * Creates a GestureDetector with the supplied listener.
	 *
	 * @param context
	 *            the application's context
	 * @param listener
	 *            the listener invoked for all the callbacks, this must not be
	 *            null.
	 *
	 */
	public GestureDetector(Context context,
			OnGestureListener onMapGestureListener) {
		this.mOnGestureListener = onMapGestureListener;
		mHandler = new GestureHandler();

		ViewConfiguration configuration = ViewConfiguration.get(context);
		int touchSlop = configuration.getScaledTouchSlop();
		int doubleTapSlop = configuration.getScaledDoubleTapSlop();

		mTouchSlopSquare = touchSlop * touchSlop;
		mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
		NaviLogUtil.debugTouch(String.format("mTouchSlopSquare is %s", mTouchSlopSquare));
		NaviLogUtil.debugTouch(String.format("mDoubleTapSlopSquare is %s", mDoubleTapSlopSquare));
	}

	public GestureDetector(OnGestureListener onMapGestureListener) {
		this.mOnGestureListener = onMapGestureListener;
		mHandler = new GestureHandler(Looper.getMainLooper());

		// ViewConfiguration configuration = ViewConfiguration.get(context);
		// int touchSlop = configuration.getScaledTouchSlop();
		// int doubleTapSlop = configuration.getScaledDoubleTapSlop();

		// TODO: 800*480 p
//		mTouchSlopSquare = 10;// touchSlop * touchSlop;
//		mDoubleTapSlopSquare = 10;// doubleTapSlop * doubleTapSlop;
		mTouchSlopSquare = 144;// touchSlop * touchSlop;
		mDoubleTapSlopSquare = 22500;// doubleTapSlop * doubleTapSlop;

	}

	/**
	 * Analyzes the given motion event. you should call this method in you
	 * method onTouchEvent() in your activity or in your view.
	 *
	 * @param ev
	 *            The current motion event.
	 * @return true if the {@link OnGestureListener} consumed the event, else
	 *         false.
	 */
	public boolean onTouchEvent(MotionEvent ev) {
		
		final int action = ev.getAction();
		final float y = ev.getY();
		final float x = ev.getX();

		
		// TODO
		NaviLogUtil.debugTouch("GestureDetector onTouchEvent: " + action);
		NaviLogUtil.debugTouch(String.format("Event x=%s , y=%s", x, y));

		boolean handled = false;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			boolean hadTapMessage = mHandler.hasMessages(TAP);
			if (hadTapMessage) {
				mHandler.removeMessages(TAP);
			}
			if ((mCurrentDownEvent != null)
					&& (mPreviousUpEvent != null)
					&& hadTapMessage
					&& isConsideredDoubleTap(mCurrentDownEvent,
							mPreviousUpEvent, ev)) {
				// Give a callback with down event of the double-tap
				handled |= mOnGestureListener.onDoubleTapConfirmed(ev);
			} else {
				// This is a first tap
				mHandler.sendEmptyMessageDelayed(TAP, DOUBLE_TAP_TIMEOUT);
			}
			mCurrentDownEvent = MotionEvent.obtain(ev);
			isStillDown = true;
			is2FingerDown = false;
			isScroll = false;
			is2FingerScroll = false;
			isScrollFrist = true;
			is2FingerScrollFirst = true;

			mHandler.removeMessages(LONG_PRESS);
			mHandler.sendEmptyMessageAtTime(LONG_PRESS,
					mCurrentDownEvent.getDownTime() + TAP_TIMEOUT
							+ LONGPRESS_TIMEOUT);
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN:
		case MotionEvent.ACTION_POINTER_2_DOWN:
			if (!isStillDown) {
				break;
			}
			is2FingerDown = true;
			mHandler.removeMessages(LONG_PRESS);
			mHandler.removeMessages(TAP);
			mCurrentDownEvent2 = MotionEvent.obtain(ev);
			if (isScroll) {
				handled |= mOnGestureListener.onScrollEnd(mCurrentDownEvent,
						mCurrentDownEvent2);
				isScroll = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isStillDown) {
				break;
			}
			if (ev.getPointerCount() == 1 && !is2FingerScroll) {
				final int deltaX = (int) (x - mCurrentDownEvent.getX());
				final int deltaY = (int) (y - mCurrentDownEvent.getY());
				int distance = (deltaX * deltaX) + (deltaY * deltaY);
				if (distance > mTouchSlopSquare) {
					handled |= mOnGestureListener.onScroll(isScrollFrist,
							mCurrentDownEvent, ev,
							x - mCurrentDownEvent.getX(),
							y - mCurrentDownEvent.getY());
					mHandler.removeMessages(LONG_PRESS);
					mHandler.removeMessages(TAP);
					isScroll = true;
					isScrollFrist = false;
				}
			} else if (ev.getPointerCount() == 2 && !isScroll) {
				float x1 = ev.getX(0);
				float y1 = ev.getY(0);
				float x2 = ev.getX(1);
				float y2 = ev.getY(1);

				float distanceMove = (float) Math.sqrt((x1 - x2) * (x1 - x2)
						+ (y1 - y2) * (y1 - y2));

				float downX1 = mCurrentDownEvent2.getX(0);
				float downY1 = mCurrentDownEvent2.getY(0);
				float downX2 = mCurrentDownEvent2.getX(1);
				float downY2 = mCurrentDownEvent2.getY(1);

				float distanceDown = (float) Math.sqrt((downX1 - downX2)
						* (downX1 - downX2) + (downY1 - downY2)
						* (downY1 - downY2));

				final float deltaX = downX1 - x1;
				final float deltaY = downY1 - y1;
				float distance1 = (deltaX * deltaX) + (deltaY * deltaY);

				final float deltaX2 = downX2 - x2;
				final float deltaY2 = downY2 - y2;
				float distance2 = (deltaX2 * deltaX2) + (deltaY2 * deltaY2);

				if (distance1 > mTouchSlopSquare
						|| distance2 > mTouchSlopSquare || is2FingerScroll) {
					handled |= mOnGestureListener.on2FingerScroll(
							is2FingerScrollFirst, mCurrentDownEvent,
							mCurrentDownEvent2, ev, distanceMove, distanceDown,
							distanceMove - distanceDown);
					handled |= on2FingerScroll(is2FingerScrollFirst,
							mCurrentDownEvent, mCurrentDownEvent2, ev,
							distanceMove, distanceDown, distanceMove
									- distanceDown);
					mHandler.removeMessages(LONG_PRESS);
					mHandler.removeMessages(TAP);
					is2FingerScroll = true;
					is2FingerScrollFirst = false;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:
			if (!isStillDown) {
				break;
			}
			isStillDown = false;
			mPreviousUpEvent = MotionEvent.obtain(ev);
			mHandler.removeMessages(LONG_PRESS);
			if (isScroll) {
				handled |= mOnGestureListener.onScrollEnd(mCurrentDownEvent,
						mPreviousUpEvent);
			} else if (!is2fingerOverTime(mCurrentDownEvent, ev)
					&& is2FingerDown && !is2FingerScroll) {
				handled |= mOnGestureListener.on2FingerSingleTapConfirmed(
						mCurrentDownEvent, mCurrentDownEvent2);
			}

			break;
		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
			if (!isStillDown) {
				break;
			}
			mPreviousUpEvent2 = MotionEvent.obtain(ev);
			if (is2FingerScroll) {
				handled |= mOnGestureListener.on2FingerScrollEnd(
						mCurrentDownEvent, mCurrentDownEvent2,
						mPreviousUpEvent2);
				handled |= on2FingerScrollEnd(mCurrentDownEvent,
						mCurrentDownEvent2, mPreviousUpEvent2);
				is2FingerScrollFirst = true;
			}
			break;
		}
		
		if( action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_CANCEL ){
			AppLinkService service = AppLinkService.getInstance();
			if (null != service && service.isRunning()) {
				ReadPixelControl.getInstance().addTouchMoveCount();
			}else{
				ReadPixelControl.getInstance().reinit();
			}
		}
		
		return handled;
	}

	// 2 finger single tap will not be over time
	private boolean is2fingerOverTime(MotionEvent down, MotionEvent up) {
		return up.getEventTime() - down.getEventTime() > DOUBLE_TAP_TIMEOUT;

	}

	// make sure not over time and not over the square
	private boolean isConsideredDoubleTap(MotionEvent firstDown,
			MotionEvent firstUp, MotionEvent secondDown) {

		if (secondDown.getEventTime() - firstUp.getEventTime() > DOUBLE_TAP_TIMEOUT) {
			return false;
		}

		int deltaX = (int) firstDown.getX() - (int) secondDown.getX();
		int deltaY = (int) firstDown.getY() - (int) secondDown.getY();
		return (deltaX * deltaX + deltaY * deltaY < mDoubleTapSlopSquare);
	}

	// dispatch long press
	private void dispatchLongPress() {
		mHandler.removeMessages(TAP);
		mOnGestureListener.onLongPress(mCurrentDownEvent);
	}

	private boolean on2FingerScroll(boolean isFrist, MotionEvent down1,
			MotionEvent down2, MotionEvent move, float distanceMove,
			float distanceDown, float distanceDiff) {
		if (isFrist) {
			double direction0 = direction(down2, move, 0);
			double direction1 = direction(down2, move, 1);

			if (isHorizontalMove(direction0) != 0
					&& isHorizontalMove(direction0) == isHorizontalMove(direction1)) {
				typeOf2FingerScroll = SCROLL_TYPE_HORIZONTAL_SCROLL;
			} else if (isVerticalMove(direction0) != 0
					&& isVerticalMove(direction0) == isVerticalMove(direction1)) {
				typeOf2FingerScroll = SCROLL_TYPE_VERTICAL_SCROLL;
			} else {
				typeOf2FingerScroll = SCROLL_TYPE_SCALE;
			}

		}

		switch (typeOf2FingerScroll) {
		case SCROLL_TYPE_SCALE:
			mOnGestureListener.on2FingerScale(isFrist, down1, down2, move,
					distanceMove, distanceDown, distanceDiff);
			break;
		case SCROLL_TYPE_VERTICAL_SCROLL:
			float verticalDistance = (move.getY(0) + move.getY(1)
					- down2.getY(0) - down2.getY(1)) / 2;
			mOnGestureListener.on2FingerParallelVerticalScroll(isFrist, down1,
					down2, move, verticalDistance);
			break;
		case SCROLL_TYPE_HORIZONTAL_SCROLL:
			float horizontalDistance = (move.getX(0) + move.getX(1)
					- down2.getX(0) - down2.getX(1)) / 2;
			mOnGestureListener.on2FingerParallelHorizontalScroll(isFrist,
					down1, down2, move, horizontalDistance);
			break;
		default:
			break;
		}

		return true;
	}

	private boolean on2FingerScrollEnd(MotionEvent down1, MotionEvent down2,
			MotionEvent up2) {
		switch (typeOf2FingerScroll) {
		case SCROLL_TYPE_SCALE:
			mOnGestureListener.on2FingerScaleEnd(down1, down2, up2);
			break;
		case SCROLL_TYPE_VERTICAL_SCROLL:
			mOnGestureListener.on2FingerParallelVerticalScrollEnd(down1, down2,
					up2);
			break;
		case SCROLL_TYPE_HORIZONTAL_SCROLL:
			mOnGestureListener.on2FingerParallelHorizontalScrollEnd(down1,
					down2, up2);
			break;
		default:
			break;
		}

		return true;
	}

	private int isVerticalMove(double direction) {
		if (direction > Math.PI / 6 && direction < Math.PI * 5 / 6) {
			return 1;
		} else if (direction > -Math.PI * 5 / 6 && direction < -Math.PI / 6) {
			return -1;
		} else {
			return 0;
		}
	}

	private int isHorizontalMove(double direction) {
		if (Math.abs(direction) < Math.PI / 3) {
			return 1;
		} else if (Math.abs(direction) > Math.PI * 2 / 3) {
			return -1;
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unused")
	private double dirctionParallel(double direction0, double direction1) {
		if (direction1 - direction0 < Math.PI / 4) {
			return (direction1 + direction0) / 2;
		} else if (direction1 - direction0 > 2 * Math.PI - Math.PI / 4) {
			return Math.PI - (direction1 + direction0) / 2;
		}

		return 10000 * Math.PI;
	}

	private double direction(MotionEvent startEvent, MotionEvent endEvent,
			int index) {
		if (compare(endEvent.getX(index), startEvent.getX(index))) {
			return (endEvent.getY(index) > startEvent.getY(index)) ? -Math.PI / 2
					: Math.PI / 2;
		}

		float tan = (-endEvent.getY(index) + startEvent.getY(index))
				/ (endEvent.getX(index) - startEvent.getX(index));
		double angle = Math.atan(tan);

		if (endEvent.getX(index) < startEvent.getX(index)) {
			if (endEvent.getY(index) > startEvent.getY(index)) {
				angle = angle - Math.PI;
			} else {
				angle = Math.PI + angle;
			}
		}

		return angle;
	}

	private boolean compare(float float1, float float2) {
		return Math.abs(float1 - float2) < 0.01;
	}

}
