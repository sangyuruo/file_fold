package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SpeedoMeter extends ImageView {
	
	private int speed;
	
	private static final int gapTime = 500;
	
	private static final int frameTime = 2000;
	
	private static final int[] speedImage = {/*R.drawable.jaguar_and_394a, R.drawable.jaguar_and_394b, R.drawable.jaguar_and_394c*/};
	
	private static final int frameGapTime = frameTime - (speedImage.length - 1) * gapTime;

	private final SpeedUpHandler speedUpHandler = new SpeedUpHandler();

	public SpeedoMeter(Context context) {
		super(context);
		initialize();
	}

	public SpeedoMeter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public SpeedoMeter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}
	
	private void initialize() {
		setImageResource(speedImage[0]);
	}
	
	public final void setSpeed(int speed) {
		this.speed = speed;
		setImageResource(speedImage[speed]);
	}
	
	public final void speedUp() {
		speedUpHandler.start();
	}
	
	public final void speedUpFinish() {
		speedUpHandler.stop();
		setSpeed(speed);
	}
	
	public final void speedUpFinish(int speed) {
		this.speed = speed;
		speedUpHandler.stopToSpeed(speed);
	}
	
	public final int getSpeed() {
		return speed;
	}
	
	
	private class SpeedUpHandler extends Handler {
		private int speed = 0;
		private int stopSeed = -1;
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what != 1) {
				return;
			}
			setImageResource(speedImage[speed]);
			
			if(speed == stopSeed) {
				stopSeed = -1;
				return;
			}
			
			if(speed == (speedImage.length-1)){
				sendEmptyMessageDelayed(1, frameGapTime);
			} else {
				sendEmptyMessageDelayed(1, gapTime);
			}
			speed++;
			speed %= speedImage.length;
		}
		
		public void start() {
			speed = 0;
			this.sendEmptyMessage(1);
		}
		
		public void stop() {
			removeMessages(1);
		}
		
		public void stopToSpeed(int speed) {
			stopSeed = speed;
		}
	}

}
