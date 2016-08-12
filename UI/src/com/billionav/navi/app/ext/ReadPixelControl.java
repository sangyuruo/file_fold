package com.billionav.navi.app.ext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author sangjun
 *
 */
public class ReadPixelControl {

	public void addTouchMoveCount() {
		int counts = touchMoveCount.incrementAndGet();
		if (counts > 100) {
			reinit();
		}
	}

	public void decressTouchMoveCount() {
		touchMoveCount.getAndDecrement();
	}

	public boolean isReadable() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - preReadTime > DISTANCE_TIME) {
			preReadTime = currentTime;
			decressTouchMoveCount();
			return true;
		}

		if (touchMoveCount.get() > 0) {
			preReadTime = currentTime;
			decressTouchMoveCount();
			return true;
		}

		return false;
	}

	static {
		instance = new ReadPixelControl();
	}

	public static ReadPixelControl getInstance() {
		return instance;
	}

	public void reinit() {
		touchMoveCount = new AtomicInteger(5);
		preReadTime = 0;
	}

	private ReadPixelControl() {
		reinit();
	}

	AtomicInteger touchMoveCount = new AtomicInteger(5);
	static ReadPixelControl instance;
	long preReadTime = 0;

	long DISTANCE_TIME = 400;
}
