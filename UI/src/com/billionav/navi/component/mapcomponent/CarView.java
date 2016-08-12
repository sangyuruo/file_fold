package com.billionav.navi.component.mapcomponent;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.billionav.ui.R;

public class CarView extends View {
	private static final short perAngle = 10;
	
	private final Bitmap bitmap;
	private float degrees;
	private final Paint paint;

	private static final int clockwise = 1;
	private static final int anticlockwise = 2;
	private static final long delayTimeOfLongClick = 50;
	
	private Handler longClickHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==clockwise || msg.what==anticlockwise){
				if(msg.what==clockwise){
					degrees -= perAngle;
				}else{
					degrees += perAngle;
				}
				degrees %= 360;
				setDegress(degrees);
				sendEmptyMessageDelayed(msg.what, delayTimeOfLongClick);
			}
		}
	};
	
	public CarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.navicloud_and_557a);
		degrees = 0;
		paint = new Paint();
	}

	public void setDegress(float degrees){
		this.degrees = degrees;
		invalidate();
	}
	
	public float getDegress() {
		return degrees;
	}
	
	public void incDegress() {
		degrees += perAngle;
		degrees %= 360;
		invalidate();
	}
	
	public void decDegress() {
		degrees -= perAngle;
		degrees %= 360;
		invalidate();
	}
	
	public void startRotate(boolean isClockwise) {
		longClickHandler.sendEmptyMessage(isClockwise ? clockwise : anticlockwise);
	}
	
	public void endRotate() {
		longClickHandler.removeMessages(clockwise);
		longClickHandler.removeMessages(anticlockwise);
	}
	
	
	protected void onDraw(Canvas canvas) {
		
		int x = getWidth()/2;
		int y = getHeight()/2;
		canvas.translate(x, y);
		canvas.rotate(-degrees);
		canvas.drawBitmap(bitmap, -bitmap.getWidth()/2, -30, paint);
	}

}
