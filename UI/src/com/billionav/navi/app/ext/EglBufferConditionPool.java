package com.billionav.navi.app.ext;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.billionav.navi.app.ext.log.NaviLogUtil;

public class EglBufferConditionPool {
	String TAG = "BUFFER_POOL";
	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();
	int count ;
	//	private static final int MAX_AVAILABLE = 2;
//	private final Semaphore available = new Semaphore(2, true);
	Queue<EglBufferItem> queue = new LinkedList<EglBufferItem>();

	static EglBufferConditionPool pool = null;

	static {
		pool = new EglBufferConditionPool();
	}

	private EglBufferConditionPool() {
		queue = new LinkedList<EglBufferItem>();
		count = 0;
	}

	public static EglBufferConditionPool getInstance() {
		if (null == pool) {
			pool = new EglBufferConditionPool();
		}
		return pool;
	}

//	public EglBufferItem getItem() throws InterruptedException {
//		Log.d(TAG, ": start acquire ...... ");
//		available.acquire();
//		Log.d(TAG, ": pass acquire ...... ");
//		return queue.poll();
//	}

	
	public EglBufferItem getItem() {
		boolean isSuccess = true;
		EglBufferItem item = null;
		lock.lock();
		try {
			while (count == 0) {
				NaviLogUtil.debugEglPool("it is empty");
				isSuccess = notEmpty.await(NaviConstant.BUFFER_POOL_GET_WAIT_TIME,TimeUnit.MILLISECONDS);
				if( !isSuccess ){
					break;
				}
			}
			if( isSuccess ){
				item = queue.poll();
				--count;
				notFull.signal();
				NaviLogUtil.debugEglPool( "it is not full");
			}else{
				NaviLogUtil.debugEglPool("wait not empty timeout");
			}
		} catch (InterruptedException e) {
			NaviLogUtil.debugEglPool( e.getMessage() );
		} finally {
			lock.unlock();
		}
		return item;
	}
	
	public boolean putItem(EglBufferItem item) {
		boolean isSuccess = true;
		lock.lock();
		try {
			while (count == 3) {
				NaviLogUtil.debugEglPool( "it is full");
				isSuccess = notFull.await(NaviConstant.BUFFER_POOL_PUT_WAIT_TIME,TimeUnit.MILLISECONDS);
				if( !isSuccess ){
					break;
				}
			}
			if( isSuccess ){
				isSuccess = queue.offer(item);
				++count;
				notEmpty.signal();
				NaviLogUtil.debugEglPool("it is not empty");
			}else{
				NaviLogUtil.debugEglPool("wait not full timeout");
			}
		} catch (InterruptedException e) {
			NaviLogUtil.debugEglPool(e.getMessage() );
		} 
		finally {
			lock.unlock();
		}
		return isSuccess;
	}
		
//	public boolean putItem(EglBufferItem item) {
//		boolean isPut = checkCanPut(item);
//		if (isPut) {
//			Log.d(TAG, "release ...... ");
//			available.release();
//		}
//		return isPut;
//	}

//	public synchronized boolean checkCanPut(EglBufferItem item) {
//		if (queue.size() < MAX_AVAILABLE) {
//			Log.d(TAG, "size if not full ...... ");
//			return queue.offer(item);
//		} else {
//			return false;
//		}
//	}

}
