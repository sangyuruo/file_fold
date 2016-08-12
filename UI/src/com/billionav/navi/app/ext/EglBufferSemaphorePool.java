package com.billionav.navi.app.ext;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.billionav.navi.app.ext.log.NaviLogUtil;

public class EglBufferSemaphorePool {
	String TAG = "BUFFER_POOL";
	private static final int MAX_AVAILABLE = 1;
	private final Semaphore available = new Semaphore(0, true);
	Queue<EglBufferItem> queue = new LinkedList<EglBufferItem>();
	Object lock = new Object();
	static EglBufferSemaphorePool pool = null;

	static {
		pool = new EglBufferSemaphorePool();
	}

	private EglBufferSemaphorePool() {
		queue = new LinkedList<EglBufferItem>();
	}

	public static EglBufferSemaphorePool getInstance() {
		if (null == pool) {
			pool = new EglBufferSemaphorePool();
		}
		return pool;
	}

	public EglBufferItem getItem() {
		NaviLogUtil.debugEglPool("start acquire , ready ...... ,"
				+ System.currentTimeMillis());
		try {
			// available.acquire();
			boolean isSuccess = available.tryAcquire(
					NaviConstant.BUFFER_POOL_GET_WAIT_TIME,
					TimeUnit.MILLISECONDS);
			if (isSuccess) {
				NaviLogUtil.debugEglPool("pass acquire , success ...... ,"
						+ System.currentTimeMillis());
				synchronized (lock) {
					return queue.poll();
				}
			} else {
				NaviLogUtil.debugEglPool("acquire failed , time out ......,"
						+ System.currentTimeMillis());
				return null;
			}
		} catch (InterruptedException e) {
			NaviLogUtil.debugEglPool(System.currentTimeMillis()
					+ " acquire failed ...... " + e.getMessage());
			return null;
		}
	}

	public void putItem(EglBufferItem item) {
		if (available.availablePermits() < MAX_AVAILABLE) {
			NaviLogUtil.debugEglPool("put , release ...... ,"
					+ System.currentTimeMillis());
			synchronized (lock) {
				queue.offer(item);
			}
			available.release();
		}
	}

	public boolean availablePermits() {
		int permits = available.availablePermits();
		NaviLogUtil.debugEglPool( "check available," + permits + ","
				+ System.currentTimeMillis());
		return permits < MAX_AVAILABLE;
	}

}
