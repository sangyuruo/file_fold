package com.billionav.navi.app.ext.sdl;

import java.util.ArrayList;
import java.util.List;

import com.smartdevicelink.proxy.rpc.OnTouchEvent;
import com.smartdevicelink.proxy.rpc.TouchCoord;
import com.smartdevicelink.proxy.rpc.TouchEvent;
import com.smartdevicelink.proxy.rpc.enums.TouchType;

public class TestTouchThread extends Thread {
	boolean isRun = true;

	public TestTouchThread() {
		isRun = true;
	}

	private OnTouchEvent createEvent(int x, int y, int id, int ts,
			TouchType type) {
		List<Integer> tss = new ArrayList<Integer>();
		tss.add(ts);
		TouchCoord touchCoord = new TouchCoord();
		touchCoord.setX(x);
		touchCoord.setY(y);
		List<TouchCoord> touchCoords = new ArrayList<TouchCoord>();
		touchCoords.add(touchCoord);

		List<TouchEvent> events = new ArrayList<TouchEvent>();
		TouchEvent event = new TouchEvent();
		event.setC(touchCoords);
		event.setId(id);
//		event.setTs(tss);
		events.add(event);

		OnTouchEvent start = new OnTouchEvent();
		start.setType(type);
		start.setEvent(events);
		return start;
	}

	@Override
	public void run() {
		while (isRun) {
			TouchEventHandler touchEventHandler = TouchEventHandler
					.getInstance();
			OnTouchEvent start = createEvent(100, 100, 1, 1, TouchType.BEGIN);
			touchEventHandler.execute(start);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			OnTouchEvent move = createEvent(200, 200, 2, 2, TouchType.MOVE);
			touchEventHandler.execute(move);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			OnTouchEvent move2 = createEvent(250, 250, 3, 3, TouchType.MOVE);
			touchEventHandler.execute(move2);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			OnTouchEvent end = createEvent(300, 300, 4, 4, TouchType.END);
			touchEventHandler.execute(end);
			break;
		}
	}

	public void exit() {
		isRun = false;
	}
}
